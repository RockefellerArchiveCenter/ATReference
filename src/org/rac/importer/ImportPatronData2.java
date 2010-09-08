/**
 * Archivists' Toolkit(TM) Copyright © 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.  
 * All rights reserved. 
 *
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL) 
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php) 
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations. 
 *
 *
 * Archivists' Toolkit(TM) 
 * http://www.archiviststoolkit.org 
 * info@archiviststoolkit.org 
 *
 * @author Lee Mandell
 * Date: Nov 9, 2009
 * Time: 10:01:15 AM
 */

package org.rac.importer;

import org.apache.commons.beanutils.PropertyUtils;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.importer.ImportException;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.importer.ImportHandler;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.model.*;
import org.rac.model.PatronAddresses;
import org.rac.model.PatronFunding;
import org.rac.model.PatronPublications;
import org.rac.model.PatronVisits;
import org.rac.model.PatronVisitsSubjects;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;
import org.rac.model.*;
import org.rac.structure.patronImportSchema.*;
import org.rac.structure.patronImportSchema.PatronPublicationsSubjects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class ImportPatronData2 extends ImportHandler {

	public ImportPatronData2() {
	}

	private void addPhone(Patrons patron, String number, String phoneNumberType) throws UnknownLookupListException, InvocationTargetException, IllegalAccessException {
		org.rac.model.PatronPhoneNumbers phoneNumber = new org.rac.model.PatronPhoneNumbers(patron);
		phoneNumber.setPhoneNumber(number);
		ImportUtils.nullSafeSet(phoneNumber, org.rac.model.PatronPhoneNumbers.PROPERTYNAME_PHONE_NUMBER_TYPE, phoneNumberType);
		patron.addPatronPhoneNumbers(phoneNumber);
	}

	private Date createDateFromString(String dateString, String format) {
//		String[] parts = dateString.split("T");
		DateFormat df = new SimpleDateFormat(format);
		java.util.Date utilDate = null;
		try {
			utilDate = df.parse(dateString);
		} catch (ParseException e) {
			return new Date(System.currentTimeMillis());
		}
		return new Date(utilDate.getTime());
	}


	/**
	 * Can we import this file.
	 *
	 * @param importFile the file to import.
	 * @return can we.
	 */
	@Override
	public boolean canImportFile(File importFile) {
		return true;
	}

	/**
	 * Import the file.
	 *
	 * @param importFile	the file to import.
	 * @param controller	the controller to use.
	 * @param progressPanel
	 * @return if we succeded
	 */
	@Override
	public boolean importFile(File importFile, DomainImportController controller, InfiniteProgressPanel progressPanel) throws ImportException {
		Collection<DomainObject> collection = new ArrayList<DomainObject>();
		SubjectsDAO subjectDao = new SubjectsDAO();


		if (importFile != null) {

			JAXBContext context;

			try {
				context = JAXBContext.newInstance("org.rac.structure.patronImportSchema");
				LookupListUtils.initIngestReport();

				PatronRecords records = (PatronRecords) context.createUnmarshaller().unmarshal(importFile);
				Patrons patron;
				PropertyDescriptor[] patronDescriptors = PropertyUtils.getPropertyDescriptors(Patron.class);
				PropertyDescriptor[] addressDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronAddresses.class);
				PropertyDescriptor[] phoneNumberDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronPhoneNumbers.class);
				PropertyDescriptor[] visitDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronVisits.class);
				PropertyDescriptor[] grantDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronFunding.class);
				PropertyDescriptor[] publicationDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronPublications.class);

				PatronVisits newPatronVisit;
				String subjectString;
				String subjectTypeString;
				String subjectSourceString;
				Subjects subjectToAdd;
				PatronVisitsSubjects newSubjectVisit;
				org.rac.model.PatronPublicationsSubjects newSubjectPublication;

				for (Patron importedPatronRecord : records.getRecord()) {

					patron = new Patrons();

					populateDomainObject(patron, patronDescriptors, importedPatronRecord);
					patron.createSortName();

					//deal with addresses
					PatronAddresses newAddress;
					for (org.rac.structure.patronImportSchema.PatronAddresses address: importedPatronRecord.getPatronAddresses()) {
						newAddress = new PatronAddresses(patron);
						populateDomainObject(newAddress, addressDescriptors, address);
						patron.addPatonsAddresses(newAddress);
					}
					//check for preferred address
					PatronAddresses firstAddress = null;
					Boolean found = false;
					for (PatronAddresses address: patron.getPatronAddresses()) {
						if (firstAddress == null){
							firstAddress = address;
						}
						if (address.getPreferredAddress()) {
							found = true;
							break;
						}
						if (!found) {
							firstAddress.setPreferredAddress(true);
						}
					}

					//deal with phone numbers
					org.rac.model.PatronPhoneNumbers NewPhoneNumber;
					for (org.rac.structure.patronImportSchema.PatronPhoneNumbers phoneNumber: importedPatronRecord.getPatronPhoneNumbers()) {
						NewPhoneNumber = new org.rac.model.PatronPhoneNumbers(patron);
						populateDomainObject(NewPhoneNumber, phoneNumberDescriptors, phoneNumber);
						patron.addPatronPhoneNumbers(NewPhoneNumber);
					}
					//check for preferred phone number
					org.rac.model.PatronPhoneNumbers firstPhoneNumber = null;
					found = false;
					for (org.rac.model.PatronPhoneNumbers patronPhoneNumber: patron.getPatronPhoneNumbers()) {
						if (firstPhoneNumber == null){
							firstPhoneNumber = patronPhoneNumber;
						}
						if (patronPhoneNumber.getPreferredPhoneNumber()) {
							found = true;
							break;
						}
						if (!found) {
							firstPhoneNumber.setPreferredPhoneNumber(true);
						}
					}

					//deal with visits
					for (org.rac.structure.patronImportSchema.PatronVisits patronVisit: importedPatronRecord.getPatronVisits()) {
						newPatronVisit = new PatronVisits(patron);
						populateDomainObject(newPatronVisit, visitDescriptors, patronVisit);
						patron.addPatronVisit(newPatronVisit);

						for (org.rac.structure.patronImportSchema.PatronVisitsSubjects subject: patronVisit.getPatronVisitsSubjects()) {
							subjectString = subject.getSubjectTerm();
							subjectTypeString = subject.getSubjectTermType();
							subjectSourceString = subject.getSubjectSource();
							subjectToAdd = subjectDao.lookupSubject(cleanAndTrim(subjectString, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM), subjectTypeString, subjectSourceString, true);
							newSubjectVisit = new PatronVisitsSubjects(subjectToAdd, newPatronVisit);
							newPatronVisit.addSubject(newSubjectVisit);
						}
					}

					//deal with Funding
					org.rac.model.PatronFunding newPatronFunding;
					for (org.rac.structure.patronImportSchema.PatronFunding patronFunding: importedPatronRecord.getPatronFunding()) {
						newPatronFunding = new org.rac.model.PatronFunding(patron);
						populateDomainObject(newPatronFunding, grantDescriptors, patronFunding);
						patron.addPatronFunding(newPatronFunding);
					}

					//deal with publications
					PatronPublications newPatronPublications;
					for (org.rac.structure.patronImportSchema.PatronPublications patronPublications: importedPatronRecord.getPatronPublications()) {
						newPatronPublications = new PatronPublications(patron);
						populateDomainObject(newPatronPublications, publicationDescriptors, patronPublications);
						patron.addPatronPublication(newPatronPublications);

						for (PatronPublicationsSubjects subject: patronPublications.getPatronPublicationsSubjects()) {
							subjectString = subject.getSubjectTerm();
							subjectTypeString = subject.getSubjectTermType();
							subjectSourceString = subject.getSubjectSource();
							subjectToAdd = subjectDao.lookupSubject(cleanAndTrim(subjectString, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM), subjectTypeString, subjectSourceString, true);
							newSubjectPublication = new org.rac.model.PatronPublicationsSubjects(subjectToAdd, newPatronPublications);
							newPatronPublications.addSubject(newSubjectPublication);
						}
					}

					collection.add(patron);
				}


				controller.domainImport(collection, ApplicationFrame.getInstance(), progressPanel);
				ImportExportLogDialog dialog = new ImportExportLogDialog(controller.constructFinalImportLogText() + "\n\n" + LookupListUtils.getIngestReport(),
						ImportExportLogDialog.DIALOG_TYPE_IMPORT);
				progressPanel.close();
				dialog.showDialog();

			} catch (JAXBException e) {
				new ErrorDialog("", e).showDialog();
			} catch (NoSuchMethodException e) {
				new ErrorDialog("", e).showDialog();
			} catch (IllegalAccessException e) {
				new ErrorDialog("", e).showDialog();
			} catch (UnknownLookupListException e) {
				new ErrorDialog("", e).showDialog();
			} catch (InvocationTargetException e) {
				new ErrorDialog("", e).showDialog();
			} catch (PersistenceException e) {
				new ErrorDialog("", e).showDialog();
			} catch (ValidationException e) {
				new ErrorDialog("", e).showDialog();
			}
		}
		return true;
	}

	private void populateDomainObject(DomainObject domainObject, PropertyDescriptor[] descriptors,  Object jaxbObject) throws IllegalAccessException, InvocationTargetException, UnknownLookupListException, NoSuchMethodException {
		for (PropertyDescriptor descriptor : descriptors) {
			Class propertyType = descriptor.getPropertyType();
			if (propertyType == XMLGregorianCalendar.class) {
				ImportUtils.nullSafeDateSet(domainObject, descriptor.getName(), (XMLGregorianCalendar) descriptor.getReadMethod().invoke(jaxbObject));
			} else if (propertyType == String.class ||
					propertyType == BigInteger.class ||
					propertyType == BigDecimal.class) {
				String name = descriptor.getName();
				Method readMethod = descriptor.getReadMethod();
				Object object = readMethod.invoke(jaxbObject);
				ImportUtils.nullSafeSet(domainObject, name, object);

			} else if (propertyType == Boolean.class) {
				//this hack is needed because jaxb has a bug and the read method starts
				//with "is" instead of "get"
				String writeMethodName = descriptor.getWriteMethod().getName();
				String readMethodName = writeMethodName.replaceFirst("set", "is");
				Method readMethod = jaxbObject.getClass().getMethod(readMethodName);
				ImportUtils.nullSafeSet(domainObject, descriptor.getName(), readMethod.invoke(jaxbObject));
			}
		}
	}

	private String cleanAndTrim(String valueToClean, Class clazz, String propertyName) {
		String returnString = StringHelper.cleanUpWhiteSpace(valueToClean);
		ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz, propertyName);
		if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
			return StringHelper.trimToLength(returnString, fieldInfo.getStringLengthLimit());
		} else {
			return returnString;
		}
	}

}