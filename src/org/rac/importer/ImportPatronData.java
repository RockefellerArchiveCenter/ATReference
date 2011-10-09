/*
 * ATReference Copyright © 2011 Rockefeller Archive Center

 * All rights reserved.
 *
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 *
 *
 * ATReference
 * https://github.com/RockefellerArchiveCenter/ATReference/wiki
 *
 * @author Lee Mandell
 * Date: Nov 9, 2009
 * Time: 10:01:15 AM
 */

package org.rac.importer;

import org.rac.model.PatronAddresses;
import org.rac.model.PatronPhoneNumbers;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.mydomain.*;
import org.rac.model.PatronVisits;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.hibernate.AuditInterceptor;
import org.archiviststoolkit.ApplicationFrame;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.HibernateException;
import org.rac.model.Patrons;
import org.rac.utils.PatronUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.sql.Date;

public class ImportPatronData {

	public ImportPatronData() {
	}

	public void doImport(File file, InfiniteProgressPanel monitor) {

		try {

//			DomainAccessObject namesDAO = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Names.class);
			if (file != null) {
				SAXBuilder builder = new SAXBuilder();
				Document document = builder.build(file);
				Element root = document.getRootElement();
				Element row;
				Element attribute;
				String patronType;
				String companyName;
				String lastName;
				PatronAddresses address;
				String dateString;
//				Date date;
				int count = 1;
				for (Object o: root.getChildren("row")) {
					row = (Element)o;
					companyName = row.getChild("Company").getText();
					lastName = row.getChild("Last").getText();
					Patrons patron = new Patrons();
					ImportUtils.nullSafeSet(patron, Patrons.PROPERTYNAME_INSTITUTIONAL_AFFILIATION, companyName);
					ImportUtils.nullSafeSet(patron, Patrons.PROPERTYNAME_PRIMARY_NAME, lastName);
					ImportUtils.nullSafeSet(patron, Patrons.PROPERTYNAME_REST_OF_NAME, row.getChild("First").getText());

					ImportUtils.nullSafeSet(patron, Patrons.PROPERTYNAME_EMAIL1, row.getChild("E-Mail").getText());
					String fax = row.getChild("Fax").getText();
					if (fax != null && fax.length() > 0) {
//						ImportUtils.nullSafeSet(patron, Names.PROPERTYNAME_USER_DEFINED_STRING2, "FAX " + fax);
						addPhone(patron, fax, "fax");
					}
					addPhone(patron, row.getChild("Phone").getText(), "phone");
//					ImportUtils.nullSafeSet(patron, Names.PROPERTYNAME_CONTACT_PHONE, row.getChild("Phone").getText());

					patronType = row.getChild("Patron_Type").getText();
					if (patronType.length() == 0) {
						ImportUtils.nullSafeSet(patron, Patrons.PROPERTYNAME_PATRON_TYPE, "blank");
					} else {
						ImportUtils.nullSafeSet(patron, Patrons.PROPERTYNAME_PATRON_TYPE, patronType);
					}
					patron.setCreateSortNameAutomatically(true);
					patron.createSortName();
					System.out.println(count++ + ": " + patron.getSortName());

					//address info
					address = new PatronAddresses(patron);
					ImportUtils.nullSafeSet(address, PatronAddresses.PROPERTYNAME_CITY, row.getChild("Mail_City_St_Zi").getText());
					ImportUtils.nullSafeSet(address, PatronAddresses.PROPERTYNAME_COUNTRY, row.getChild("Mail_Country").getText());
					ImportUtils.nullSafeSet(address, PatronAddresses.PROPERTYNAME_REGION, row.getChild("Mail_State").getText());
					ImportUtils.nullSafeSet(address, PatronAddresses.PROPERTYNAME_ADDRESS1, row.getChild("Mail_Street_1").getText());
					ImportUtils.nullSafeSet(address, PatronAddresses.PROPERTYNAME_ADDRESS2, row.getChild("Mail_Street_2").getText());
					ImportUtils.nullSafeSet(address, PatronAddresses.PROPERTYNAME_MAIL_CODE, row.getChild("Mail_Zip").getText());
					address.setPreferredAddress(true);
					patron.addPatonsAddresses(address);

//					date = new Date();
//					ImportUtils.nullSafeSet(patron, Names.CREATED_BY, row.getChild("Add_By").getText());
//					ImportUtils.nullSafeSet(patron, Names.LAST_UPDATED_BY, row.getChild("Upd_By").getText());
					AuditInfo auditInfo = patron.getAuditInfo();

					auditInfo.setCreated(createDateFromString(row.getChild("Add_Dte").getText(), "yyyy-MM-dd'T'HH:mm:ss"));
					auditInfo.setCreatedBy(row.getChild("Add_By").getText());
					auditInfo.setLastUpdated(createDateFromString(row.getChild("Upd_Dte").getText(), "yyyy-MM-dd'T'HH:mm:ss"));
					auditInfo.setLastUpdatedBy(row.getChild("Upd_By").getText());
					PatronUtils.setMd5Hash(patron);

					//get visit info

					PatronVisits visit;
					String lastVisitDateString = row.getChild("Most_Rec._Visit").getText();
					if (lastVisitDateString != null && lastVisitDateString.length() > 0) {
						visit = new PatronVisits(patron);
						visit.setVisitDate(createDateFromString(lastVisitDateString, "yyyy-MM-dd'T'HH:mm:ss"));
						patron.addPatronVisit(visit);
					}

					String topicString = row.getChild("Topic").getText();
					String[] parts;
					if (topicString != null && topicString.length() > 0) {
						String[] topics = topicString.split("\\|\\|");
						for (String topic: topics) {
							parts = topic.split("__");
							visit = new PatronVisits(patron);
							visit.setTopic(parts[0].trim());
							if (parts.length == 2) {
								visit.setVisitDate(createDateFromString(parts[1].trim(), "MM/dd/yyyy"));
							}
							patron.addPatronVisit(visit);
						}
					}

					String subjectString = row.getChild("Subject").getText();
					if (subjectString != null && subjectString.length() > 0) {
						String[] subjects = subjectString.split("\\|\\|");
						for (String subject: subjects) {
							parts = subject.split("__");
							visit = new PatronVisits(patron);
							visit.setTopic(parts[0].trim());
							if (parts.length == 2) {
								visit.setVisitDate(createDateFromString(parts[1].trim(), "MM/dd/yyyy"));
							}
							patron.addPatronVisit(visit);
						}
					}


					try {
						add(patron);
					} catch (PersistenceException e) {
						System.out.println("Duplicate entry: " + count + ": " + patron.getSortName());
						new ErrorDialog("", e).showDialog();
					}
				}
			}
		} catch (JDOMException e) {
			monitor.close();
			new ErrorDialog("", e).showDialog();
		} catch (IOException e) {
			monitor.close();
			new ErrorDialog("", e).showDialog();
		} catch (UnknownLookupListException e) {
			monitor.close();
			new ErrorDialog("", e).showDialog();
		} catch (InvocationTargetException e) {
			monitor.close();
			new ErrorDialog("", e).showDialog();
		} catch (IllegalAccessException e) {
			monitor.close();
			new ErrorDialog("", e).showDialog();
		} catch (NoSuchAlgorithmException e) {
			monitor.close();
			new ErrorDialog("", e).showDialog();
//		} catch (PersistenceException e) {
//			monitor.close();
//			new ErrorDialog("", e).showDialog();
		}
	}

	private void addPhone(Patrons patron, String number, String phoneNumberType) throws UnknownLookupListException, InvocationTargetException, IllegalAccessException {
		PatronPhoneNumbers phoneNumber = new PatronPhoneNumbers(patron);
		phoneNumber.setPhoneNumber(number);
		ImportUtils.nullSafeSet(phoneNumber, PatronPhoneNumbers.PROPERTYNAME_PHONE_NUMBER_TYPE, phoneNumberType);
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

	public final void add(final DomainObject domainObject) throws PersistenceException {
		Session session = SessionFactory.getInstance().openSession();
		Transaction tx = null;
		try {

			tx = session.beginTransaction();
			session.saveOrUpdate(domainObject);
			tx.commit();
//			session.flush();
//			session.connection().commit();

		} catch (HibernateException hibernateException) {
			try {
				tx.rollback();
			} catch (HibernateException e) {
				//todo log error
			}
//			throw new PersistenceException("failed to add, class: " + this.getClass() + " object: " + domainObject, hibernateException);
		} catch (Exception sqlException) {
			try {
				tx.rollback();
			} catch (HibernateException e) {
				//todo log error
			}
			throw new PersistenceException("failed to add, class: " + this.getClass() + " object: " + domainObject, sqlException);
		} finally {
			session.close();
		}

//		SessionFactory.getInstance().closeSession(session);
//		this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.INSERT, domainObject));
	}

	public final void update(final DomainObject domainObject) throws PersistenceException {
		Session session = SessionFactory.getInstance().openSession(new AuditInterceptor(ApplicationFrame.getInstance().getCurrentUser()));
		Transaction tx = null;

		try {
//			updateClassSpecific(domainObject, session);
			tx = session.beginTransaction();
			session.saveOrUpdate(domainObject);
			tx.commit();
//			session.flush();
//			session.connection().commit();
		} catch (HibernateException hibernateException) {
			try {
				tx.rollback();
			} catch (HibernateException e) {
				//todo log error
			}
			throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, hibernateException);
		} catch (Exception sqlException) {
			try {
				tx.rollback();
			} catch (HibernateException e) {
				//todo log error
			}
			throw new PersistenceException("failed to update, class: " + this.getClass() + " object: " + domainObject, sqlException);
		} finally {
			session.close();
		}

//		SessionFactory.getInstance().closeSession(session);
//		this.notifyListeners(new DomainAccessEvent(DomainAccessEvent.UPDATE, domainObject));
	}

}
