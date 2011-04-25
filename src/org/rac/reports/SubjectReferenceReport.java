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
 * Date: Mar 23, 2011
 * Time: 10:44:40 AM
 */

package org.rac.reports;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.ReportExecutionException;
import org.rac.dialogs.GeneralLogDialog;
import org.rac.dialogs.StatisticalReportDates;
import org.rac.model.PatronVisits;
import org.rac.model.PatronVisitsNames;
import org.rac.model.PatronVisitsSubjects;
import org.rac.model.Patrons;
import org.rac.myDomain.PatronVisitsDAO;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

public class SubjectReferenceReport {

	private static final String NAMES_AS_SUBJECTS = "Names as Subjects";
	private Hashtable<String, SubjectTypeRecord> subjectTypes;
	private String reportText;

	public SubjectReferenceReport() {

		subjectTypes = new Hashtable<String, SubjectTypeRecord>();
	}

	public void runReport() throws ReportExecutionException {
		StatisticalReportDates dialog = new StatisticalReportDates(ApplicationFrame.getInstance());
		if (dialog.showDialog() == JOptionPane.OK_OPTION) {

			Date startDate = dialog.getStartDate();
			Date endDate = dialog.getEndDate();
			System.out.println(startDate + " " + endDate);
			PatronVisitsDAO dao = new PatronVisitsDAO();
			Collection visits = dao.findByDateRange(startDate, endDate);
			PatronVisits visit;
			SubjectTypeRecord subjectTypeRecord;
			//gather all of the subjects
			for (Object o: visits.toArray()) {
				visit = (PatronVisits)o;
//				System.out.println(visit);
				for (PatronVisitsSubjects subject: visit.getSubjects()) {
					subjectTypeRecord = subjectTypes.get(subject.getSubjectTermType());
					if (subjectTypeRecord == null) {
						subjectTypes.put(subject.getSubjectTermType(), new SubjectTypeRecord(subject.getSubjectTermType(), subject.getSubjectTerm(), visit.getPatron()));
					} else {
						subjectTypeRecord.addPatronVisit(subject.getSubjectTerm(), visit.getPatron());
					}
				}

				for (PatronVisitsNames name: visit.getNames()) {
					subjectTypeRecord = subjectTypes.get(NAMES_AS_SUBJECTS);
					if (subjectTypeRecord == null) {
						subjectTypes.put(NAMES_AS_SUBJECTS, new SubjectTypeRecord(NAMES_AS_SUBJECTS, name.getSortName(), visit.getPatron()));
					} else {
						subjectTypeRecord.addPatronVisit(name.getSortName(), visit.getPatron());
					}
				}
			}

			reportText = "Subject Reference Report\nVisits from: " + SimpleDateFormat.getDateInstance().format(startDate) +
					" to: " + SimpleDateFormat.getDateInstance().format(endDate);
			for (SubjectTypeRecord subjectType: subjectTypes.values()) {
				subjectType.addToReportText();
			}
			GeneralLogDialog logDialog = new GeneralLogDialog(ApplicationFrame.getInstance());
			logDialog.setTitleText("Subject Reference Report");
			logDialog.setLogText(reportText);
			logDialog.showDialog();
		}
	}

	private class SubjectTypeRecord {

		String subjectType;
		Hashtable<String, SubjectRecord> subjectRecords;

		private SubjectTypeRecord(String subjectType, String subject, Patrons patron) {
			this.subjectType = subjectType;
			subjectRecords = new Hashtable<String, SubjectRecord>();
			subjectRecords.put(subject, new SubjectRecord(subject, patron));
		}

		public void addPatronVisit(String subject, Patrons patron) throws ReportExecutionException {
			SubjectRecord subjectRecord = subjectRecords.get(subject);
			if (subjectRecord == null) {
				subjectRecords.put(subject, new SubjectRecord(subject, patron));
			} else {
				subjectRecord.addPatronVisit(patron);
			}
		}

		public void addToReportText() {
			reportText+= "\n\n" + subjectType;
			for (SubjectRecord subjectRecord: subjectRecords.values()) {
				subjectRecord.addToReportText();
			}
		}

		private class SubjectRecord {

			String subject;
			Hashtable<Patrons, Integer> visitCount;

			private SubjectRecord(String subject, Patrons patron) {
				this.subject = subject;
				visitCount = new Hashtable<Patrons, Integer>();
				visitCount.put(patron, 1);
			}

			public void addPatronVisit(Patrons patron) {

				Integer existingCount = visitCount.get(patron);
				Integer newCount;
				if (existingCount == null) {
					newCount = 1;
				} else {
					newCount = existingCount + 1;
				}
				visitCount.put(patron, newCount);
			}

			public void addToReportText() {
				reportText+= "\n\n" + subject;
				int totalVisits = 0;
				int totalPatrons = 0;
				for (Patrons patron: visitCount.keySet()) {
					totalPatrons++;
					String countText;
					int numberOfVisits = visitCount.get(patron);
					if (numberOfVisits == 1) {
						countText = "1 day";
					} else {
						countText = numberOfVisits + " days";
					}
					totalVisits += numberOfVisits;
					reportText+= "\n" + patron + "-" + countText;
				}
				reportText+= "\nTotal # of Researchers: " + totalPatrons + "\t" + "Total # of Days: " + totalVisits;
			}
		}

	}

}
