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
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.exceptions.ValidationException;
import org.archiviststoolkit.importer.ImportException;
import org.archiviststoolkit.importer.ImportExportLogDialog;
import org.archiviststoolkit.importer.ImportHandler;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.util.NameUtils;
import org.rac.exceptions.DataTruncationException;
import org.rac.model.PatronAddresses;
import org.rac.model.PatronFunding;
import org.rac.model.PatronPublications;
import org.rac.model.PatronVisits;
import org.rac.model.PatronVisitsNames;
import org.rac.model.PatronVisitsSubjects;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;
import org.rac.model.*;
import org.rac.structure.patronImportSchema.*;
import org.rac.structure.patronImportSchema.PatronPublicationsSubjects;
import org.rac.utils.PatronImportUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImportPatronData2 extends ImportHandler {

	Boolean debug = true;

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
				PropertyDescriptor[] formDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronForms.class);

				PatronVisits newPatronVisit;
				String subjectString;
				String subjectTypeString;
				String subjectSourceString;
				Subjects subjectToAdd;
				PatronVisitsSubjects newSubjectVisit;
				org.rac.model.PatronPublicationsSubjects newSubjectPublication;
				int recordNumber = 0;

				for (Patron importedPatronRecord : records.getRecord()) {

					recordNumber++;

					patron = new Patrons();

					populateDomainObject(patron, patronDescriptors, importedPatronRecord, controller, recordNumber, patron);
					patron.createSortName();

					//deal with addresses
					PatronAddresses newAddress;
					for (org.rac.structure.patronImportSchema.PatronAddresses address: importedPatronRecord.getPatronAddresses()) {
						newAddress = new PatronAddresses(patron);
						populateDomainObject(newAddress, addressDescriptors, address, controller, recordNumber, patron);
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
						populateDomainObject(NewPhoneNumber, phoneNumberDescriptors, phoneNumber, controller, recordNumber, patron);
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
						populateDomainObject(newPatronVisit, visitDescriptors, patronVisit, controller, recordNumber, patron);
						patron.addPatronVisit(newPatronVisit);

						for (org.rac.structure.patronImportSchema.PatronVisitsSubjects subject: patronVisit.getPatronVisitsSubjects()) {
							subjectString = subject.getSubjectTerm();
							subjectTypeString = subject.getSubjectTermType();
							subjectSourceString = subject.getSubjectSource();
							subjectToAdd = subjectDao.lookupSubject(cleanAndTrim(subjectString, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM), subjectTypeString, subjectSourceString, true);
							newSubjectVisit = new PatronVisitsSubjects(subjectToAdd, newPatronVisit);
							newPatronVisit.addSubject(newSubjectVisit);
						}

						//add name as subject links
						addNamesVisits(newPatronVisit, patronVisit.getPatronVisitsNames());

					}

					//deal with Funding
					org.rac.model.PatronFunding newPatronFunding;
					for (org.rac.structure.patronImportSchema.PatronFunding patronFunding: importedPatronRecord.getPatronFunding()) {
						newPatronFunding = new org.rac.model.PatronFunding(patron);
						populateDomainObject(newPatronFunding, grantDescriptors, patronFunding, controller, recordNumber, patron);
						patron.addPatronFunding(newPatronFunding);
					}

					//deal with publications
					PatronPublications newPatronPublications;
					for (org.rac.structure.patronImportSchema.PatronPublications patronPublications: importedPatronRecord.getPatronPublications()) {
						newPatronPublications = new PatronPublications(patron);
						populateDomainObject(newPatronPublications, publicationDescriptors, patronPublications, controller, recordNumber, patron);
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

					//deal with forms
					org.rac.model.PatronForms newForm;
					for (org.rac.structure.patronImportSchema.PatronForms form: importedPatronRecord.getPatronForms()) {
						newForm = new org.rac.model.PatronForms(patron);
						populateDomainObject(newForm, formDescriptors, form, controller, recordNumber, patron);
						patron.addPatronForms(newForm);
					}
					//This is the RAC specific parsing. There are two fields in the schema (racPatronSubjects & racPatronTopics)
					//that contain infomation for re:discovery and need special processing

					String[] parts;
					String[] topics;
					Date visitDate;
					for (String topicString: importedPatronRecord.getRacPatronTopics()) {
						if (topicString != null && topicString.length() > 0) {
							topics = topicString.split("\\|\\|");
							for (String topic: topics) {
								parts = topic.split("__");
								if (parts.length == 2) {
									visitDate = createDateFromString(parts[1].trim(), "MM/dd/yyyy");
									newPatronVisit = lookupVisit(visitDate, patron);
									newPatronVisit.setVisitDate(visitDate);
								} else {
									newPatronVisit = new PatronVisits(patron);
								}
								try {
									PatronImportUtils.nullSafeSetTruncationTest(newPatronVisit,
											PatronVisits.PROPERTYNAME_TOPIC,
											parts[0].trim());
								} catch (DataTruncationException e) {
									controller.addLineToImportLog("Record # " + recordNumber + " - " + patron + " field: " + PatronVisits.PROPERTYNAME_TOPIC + " had data truncated");
								}
								patron.addPatronVisit(newPatronVisit);
							}
						}
					}

					String[] subjects;
					for (String racSubjectString: importedPatronRecord.getRacPatronSubjects()) {
						if (racSubjectString != null && racSubjectString.length() > 0) {
							subjects = racSubjectString.split("\\|\\|");
							for (String subject: subjects) {
								parts = subject.split("__");
								if (parts.length == 2) {
									visitDate = createDateFromString(parts[1].trim(), "MM/dd/yyyy");
									newPatronVisit = lookupVisit(visitDate,patron);
									newPatronVisit.setVisitDate(visitDate);
								} else {
									newPatronVisit = new PatronVisits(patron);
								}
								subjectString = parts[0].trim();
								subjectToAdd = subjectDao.lookupSubject(cleanAndTrim(subjectString, Subjects.class, Subjects.PROPERTYNAME_SUBJECT_TERM), "local", "local", true);
								if (debug) {
									if (subjectToAdd == null) {
										System.out.println("null subject");
									}
								}
								if (subjectToAdd != null) {
									newSubjectVisit = new PatronVisitsSubjects(subjectToAdd, newPatronVisit);
									newPatronVisit.addSubject(newSubjectVisit);
									patron.addPatronVisit(newPatronVisit);
								}
							}
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
			} catch (NoSuchAlgorithmException e) {
				new ErrorDialog("", e).showDialog();
			} catch (DuplicateLinkException e) {
				new ErrorDialog("", e).showDialog();
			} catch (UnsupportedEncodingException e) {
				new ErrorDialog("", e).showDialog();
			}
		}
		return true;
	}


	private PatronVisits lookupVisit(Date visitDate, Patrons patron) {

		for (PatronVisits thisVisit: patron.getPatronVisits()) {
			if (debug) {
				if (visitDate == null) {
					System.out.println("null date");
				}
				if (thisVisit.getVisitDate() == null) {
					System.out.println("null date");
				}
			}
			if (thisVisit.getVisitDate() != null && thisVisit.getVisitDate().equals(visitDate)) {
				return thisVisit;
			}
		}

		//if we got here then there was no match so just return a new one
		return new PatronVisits(patron);
	}

	private void addNamesVisits(PatronVisits patronVisits, List<org.rac.structure.patronImportSchema.PatronVisitsNames> nameList) throws IllegalAccessException, InvocationTargetException, DuplicateLinkException, UnknownLookupListException, PersistenceException, NoSuchAlgorithmException, UnsupportedEncodingException {
		NamesDAO namesDao = new NamesDAO();
		PatronVisitsNames patronVisitsName;

		for (org.rac.structure.patronImportSchema.PatronVisitsNames thisNameLink : nameList) {
			patronVisitsName = new PatronVisitsNames(lookupName(namesDao, thisNameLink.getName()), patronVisits);
			ImportUtils.nullSafeSet(patronVisitsName, PatronVisitsNames.PROPERTYNAME_FORM, thisNameLink.getNameLinkForm());
			ImportUtils.nullSafeSet(patronVisitsName, PatronVisitsNames.PROPERTYNAME_ROLE, thisNameLink.getNameLinkRole());
			patronVisits.addName(patronVisitsName);
		}

	}

	private Names lookupName(NamesDAO namesDao, org.rac.structure.patronImportSchema.NameComplexType nameFromImport) throws IllegalAccessException, InvocationTargetException, UnknownLookupListException, PersistenceException, NoSuchAlgorithmException, UnsupportedEncodingException {
		Names name = new Names();
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_NAME_TYPE, nameFromImport.getNameType());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_NAME_RULE, nameFromImport.getNameRule());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_NUMBER, nameFromImport.getNumber());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_QUALIFIER, nameFromImport.getQualifier());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_DESCRIPTION_TYPE, nameFromImport.getDescriptionType());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_DESCRIPTION_NOTE, nameFromImport.getDescriptionNote());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CITATION, nameFromImport.getCitation());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_SALUTATION, nameFromImport.getSalutation());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_ADDRESS_1, nameFromImport.getContactAddress1());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_ADDRESS_2, nameFromImport.getContactAddress2());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_CITY, nameFromImport.getContactCity());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_REGION, nameFromImport.getContactRegion());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_COUNTRY, nameFromImport.getContactCountry());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_MAIL_CODE, nameFromImport.getContactMailCode());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_PHONE, nameFromImport.getContactPhone());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_FAX, nameFromImport.getContactFax());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_EMAIL, nameFromImport.getContactEmail());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CONTACT_NAME, nameFromImport.getContactName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_FAMILY_NAME, nameFromImport.getFamilyName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_FAMILY_NAME_PREFIX, nameFromImport.getFamilyNamePrefix());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CORPORATE_PRIMARY_NAME, nameFromImport.getCorporatePrimaryName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CORPORATE_SUBORDINATE_1, nameFromImport.getCorporateSubordinate1());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_CORPORATE_SUBORDINATE_2, nameFromImport.getCorporateSubordinate2());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME, nameFromImport.getPersonalPrimaryName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_REST_OF_NAME, nameFromImport.getPersonalRestOfName());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_PREFIX, nameFromImport.getPersonalPrefix());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_SUFFIX, nameFromImport.getPersonalSuffix());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_FULLER_FORM, nameFromImport.getPersonalFullerForm());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_TITLE, nameFromImport.getPersonalTitle());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_DATES, nameFromImport.getPersonalDates());
		ImportUtils.nullSafeSet(name, Names.PROPERTYNAME_PERSONAL_DIRECT_ORDER, nameFromImport.isPersonalDirectOrder());
		if (nameFromImport.getNameSource() == null) {
			name.setNameSource("ingest");
		} else {
			name.setNameSource(nameFromImport.getNameSource());
		}
		if (name.getSortName() == null || name.getSortName().length() == 0) {
			name.createSortName();

            // check to see if sort name is still empty. If so, set it to " " so that
            // this record can be inserted into an Oracle DB without throwing an error
            if(name.getSortName().length() == 0) {
                name.setSortName(" ");
            }
		}
		NameUtils.setMd5Hash(name);

		return namesDao.lookupName(name, true);
	}

	private void populateDomainObject(DomainObject domainObject,
									  PropertyDescriptor[] descriptors,
									  Object jaxbObject,
									  DomainImportController controller,
									  int recordNumber,
									  Patrons patron) throws IllegalAccessException, InvocationTargetException, UnknownLookupListException, NoSuchMethodException {
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
				try {
					PatronImportUtils.nullSafeSetTruncationTest(domainObject, name, object);
				} catch (DataTruncationException e) {
					controller.addLineToImportLog("Record # " + recordNumber + " - " + patron + " field: " + descriptor.getName() + " had data truncated");
				}

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