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
 * Date: 7/18/11
 * Time: 9:26 AM
 */

package org.rac.exporter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.LookupException;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.rac.model.*;
import org.rac.model.PatronAddresses;
import org.rac.model.PatronForms;
import org.rac.model.PatronFunding;
import org.rac.model.PatronPhoneNumbers;
import org.rac.model.PatronPublications;
import org.rac.model.PatronPublicationsNames;
import org.rac.model.PatronPublicationsSubjects;
import org.rac.model.PatronVisits;
import org.rac.model.PatronVisitsNames;
import org.rac.model.PatronVisitsSubjects;
import org.rac.myDomain.PatronsDAO;
import org.rac.structure.patronImportSchema.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ExportPatronData {

	File exportFile;
	private int recordsExported = 0;

	public ExportPatronData(File exportFile) {
		this.exportFile = exportFile;
	}

	public void export(ArrayList<DomainObject> recordsToExport, InfiniteProgressPanel monitor) {

		try {
			PropertyDescriptor[] patronDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.Patron.class);
			PropertyDescriptor[] addressDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronAddresses.class);
			PropertyDescriptor[] phoneNumberDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronPhoneNumbers.class);
			PropertyDescriptor[] visitDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronVisits.class);
			PropertyDescriptor[] grantDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronFunding.class);
			PropertyDescriptor[] publicationDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronPublications.class);
			PropertyDescriptor[] formDescriptors = PropertyUtils.getPropertyDescriptors(org.rac.structure.patronImportSchema.PatronForms.class);
			PropertyDescriptor[] nameDescriptors = PropertyUtils.getPropertyDescriptors(NameComplexType.class);


			JAXBContext context =
					JAXBContext.newInstance("org.rac.structure.patronImportSchema");
			FileOutputStream fileOutputStream = new FileOutputStream(exportFile);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
			Writer bufferedWriter = new BufferedWriter(outputStreamWriter);
			Marshaller marshaller = context.createMarshaller();
//			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
//						  "http://www.loc.gov/MARC21/slim http://www.loc.gov/standards/marcxml/schema/MARC21slim.xsd");
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			ObjectFactory objectFactory = new ObjectFactory();

			PatronRecords patronRecords = objectFactory.createPatronRecords();

			Patron jaxbPatron = null;
			Patrons patron, fullPatronRecord;
			PatronsDAO patronsDAO = new PatronsDAO();
			int totalRecords = recordsToExport.size();
			for (DomainObject object: recordsToExport) {

                // check to see if the process was cancelled if so then just return right away
                if(monitor.isProcessCancelled()) {
                    return;
                }

				monitor.setTextLine("Exporting file  " + recordsExported++ + " of " + totalRecords + " - " +
												exportFile.getAbsolutePath(), 2);
				patron = (Patrons)object;
				fullPatronRecord = (Patrons)patronsDAO.findByPrimaryKeyLongSession(patron.getIdentifier());
				jaxbPatron = objectFactory.createPatron();
				populateJaxbObjectFromDomainObject(fullPatronRecord, patronDescriptors, jaxbPatron);

				org.rac.structure.patronImportSchema.PatronAddresses jaxbPatronAddress;
				for (PatronAddresses patronAddress: fullPatronRecord.getPatronAddresses()) {
					jaxbPatronAddress = objectFactory.createPatronAddresses();
					populateJaxbObjectFromDomainObject(patronAddress, addressDescriptors, jaxbPatronAddress);
				    jaxbPatron.getPatronAddresses().add(jaxbPatronAddress);
				}

				org.rac.structure.patronImportSchema.PatronPhoneNumbers jaxbPatronPhoneNumber;
				for (PatronPhoneNumbers patronPhoneNumber: fullPatronRecord.getPatronPhoneNumbers()) {
					jaxbPatronPhoneNumber = objectFactory.createPatronPhoneNumbers();
					populateJaxbObjectFromDomainObject(patronPhoneNumber, phoneNumberDescriptors, jaxbPatronPhoneNumber);
					jaxbPatron.getPatronPhoneNumbers().add(jaxbPatronPhoneNumber);
				}

				org.rac.structure.patronImportSchema.PatronVisits jaxbPatronVisit;
				for (PatronVisits patronVisit: fullPatronRecord.getPatronVisits()) {
					jaxbPatronVisit = objectFactory.createPatronVisits();
					populateJaxbObjectFromDomainObject(patronVisit, visitDescriptors, jaxbPatronVisit);
					//deal with research ppurposes
					for (PatronVisitsResearchPurposes researchPurpose: patronVisit.getResearchPurposes()) {
						jaxbPatronVisit.getResearchPurpose().add(researchPurpose.getResearchPurpose());
					}
					//deal with subjects associated with visits
					for (PatronVisitsSubjects subject: patronVisit.getSubjects()) {
						org.rac.structure.patronImportSchema.PatronVisitsSubjects jaxbPatronVisitSubject = objectFactory.createPatronVisitsSubjects();
						jaxbPatronVisitSubject.setSubjectTerm(subject.getSubjectTerm());
						jaxbPatronVisitSubject.setSubjectSource(subject.getSubjectSource());
						jaxbPatronVisitSubject.setSubjectTermType(subject.getSubjectTermType());
						jaxbPatronVisit.getPatronVisitsSubjects().add(jaxbPatronVisitSubject);
					}
					//deal with names associated with visits
					for (PatronVisitsNames name: patronVisit.getNames()) {
						org.rac.structure.patronImportSchema.PatronVisitsNames jaxbPatronVisitName = objectFactory.createPatronVisitsNames();
						jaxbPatronVisitName.setNameLinkForm(name.getForm());
						jaxbPatronVisitName.setNameLinkRole(name.getRole());
						jaxbPatronVisitName.setName(createJaxbNameRecord(name.getName(), objectFactory, nameDescriptors));
						jaxbPatronVisit.getPatronVisitsNames().add(jaxbPatronVisitName);
					}
					jaxbPatron.getPatronVisits().add(jaxbPatronVisit);
				}

				for (PatronFunding patronFunding: fullPatronRecord.getPatronFunding()) {
					org.rac.structure.patronImportSchema.PatronFunding jaxbPatronFunding = objectFactory.createPatronFunding();
					populateJaxbObjectFromDomainObject(patronFunding, grantDescriptors, jaxbPatronFunding);
					jaxbPatron.getPatronFunding().add(jaxbPatronFunding);
				}

				for (PatronPublications patronPublication: fullPatronRecord.getPatronPublications()) {
					org.rac.structure.patronImportSchema.PatronPublications jaxbPatronPublication = objectFactory.createPatronPublications();
					populateJaxbObjectFromDomainObject(patronPublication, publicationDescriptors, jaxbPatronPublication);
					//deal with subjects associated with publications
					for (PatronPublicationsSubjects subject: patronPublication.getSubjects()) {
						org.rac.structure.patronImportSchema.PatronPublicationsSubjects jaxbPatronPublicationSubject = objectFactory.createPatronPublicationsSubjects();
						jaxbPatronPublicationSubject.setSubjectTerm(subject.getSubjectTerm());
						jaxbPatronPublicationSubject.setSubjectSource(subject.getSubjectSource());
						jaxbPatronPublicationSubject.setSubjectTermType(subject.getSubjectTermType());
						jaxbPatronPublication.getPatronPublicationsSubjects().add(jaxbPatronPublicationSubject);
					}
					jaxbPatron.getPatronPublications().add(jaxbPatronPublication);
					//deal with names associated with publications
					for (PatronPublicationsNames name: patronPublication.getNames()) {
						org.rac.structure.patronImportSchema.PatronPublicationsNames jaxbPatronPublicationName = objectFactory.createPatronPublicationsNames();
						jaxbPatronPublicationName.setNameLinkForm(name.getForm());
						jaxbPatronPublicationName.setNameLinkRole(name.getRole());
						jaxbPatronPublicationName.setName(createJaxbNameRecord(name.getName(), objectFactory, nameDescriptors));
						jaxbPatronPublication.getPatronPublicationsNames().add(jaxbPatronPublicationName);
					}
				}

				for (PatronForms patronForm: fullPatronRecord.getPatronForms()) {
					org.rac.structure.patronImportSchema.PatronForms jaxbPatronForm = objectFactory.createPatronForms();
					populateJaxbObjectFromDomainObject(patronForm, formDescriptors, jaxbPatronForm);
					jaxbPatron.getPatronForms().add(jaxbPatronForm);
				}

				patronRecords.getRecord().add(jaxbPatron);
			}

			marshaller.marshal(patronRecords, bufferedWriter);
			fileOutputStream.close();
		} catch (JAXBException e) {
			new ErrorDialog("", e).showDialog();
		} catch (IOException e) {
			new ErrorDialog("", e).showDialog();
		} catch (IllegalAccessException e) {
			new ErrorDialog("", e).showDialog();
		} catch (InvocationTargetException e) {
			new ErrorDialog("", e).showDialog();
		} catch (DatatypeConfigurationException e) {
			new ErrorDialog("", e).showDialog();
		} catch (LookupException e) {
			new ErrorDialog("", e).showDialog();
		}

	}

	private void populateJaxbObjectFromDomainObject (DomainObject domainObject,
													 PropertyDescriptor[] descriptors,
													 Object jaxbObject) throws InvocationTargetException, IllegalAccessException, DatatypeConfigurationException {
		Class propertyType;
		String propertyName;
		ATFieldInfo fieldInfo;
		Method domainObjectReadMethod;

		for (PropertyDescriptor descriptor: descriptors) {
			propertyType = descriptor.getPropertyType();
			propertyName = descriptor.getName();
			fieldInfo = ATFieldInfo.getFieldInfo(domainObject.getClass(), propertyName);
			if (fieldInfo != null) {
				domainObjectReadMethod = fieldInfo.getGetMethod();

				if (propertyType == String.class) {
					String propertyValue = (String)domainObjectReadMethod.invoke(domainObject);
					if (propertyValue!=null && propertyValue.length() > 0) {
						BeanUtils.setProperty(jaxbObject, propertyName, propertyValue);
					}
				} else if (propertyType == XMLGregorianCalendar.class) {
					Date propertyValue = (Date)domainObjectReadMethod.invoke(domainObject);
					if (propertyValue != null) {
						Calendar calendar = Calendar.getInstance();
//					GregorianCalendar gregorianCalendar = new GregorianCalendar();
						calendar.setTime(propertyValue);
						XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();
						xmlGregorianCalendar.setYear(calendar.get(Calendar.YEAR));
						xmlGregorianCalendar.setMonth(calendar.get(Calendar.MONTH) + 1);
						xmlGregorianCalendar.setDay(calendar.get(Calendar.DAY_OF_MONTH));
//					xmlGregorianCalendar.setMillisecond(calendar.getTimeInMillis());
//					calendar.setTime(propertyValue);
						if (propertyValue!=null) {
							BeanUtils.setProperty(jaxbObject, propertyName, xmlGregorianCalendar);
						}
					}

				}  else {
					Object propertyValue = domainObjectReadMethod.invoke(domainObject);
					if (propertyValue!=null ) {
						BeanUtils.setProperty(jaxbObject, propertyName, propertyValue);
					}
				}
			}

		}
	}

	NameComplexType createJaxbNameRecord(Names name, ObjectFactory objectFactory, PropertyDescriptor[] namePropertyDescriptors) throws InvocationTargetException, DatatypeConfigurationException, IllegalAccessException {
		NameComplexType jaxbNameRecord = objectFactory.createNameComplexType();
		populateJaxbObjectFromDomainObject(name, namePropertyDescriptors, jaxbNameRecord);
		return jaxbNameRecord;
	}

	public int getRecordsExported() {
		return recordsExported;
	}
}
