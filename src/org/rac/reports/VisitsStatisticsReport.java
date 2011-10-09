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
import java.util.TreeMap;

public class VisitsStatisticsReport {

	private TreeMap<String, PatronVisitSummary> patronSummaries;
	private String reportText;

	public VisitsStatisticsReport() {

		patronSummaries = new TreeMap<String, PatronVisitSummary>();
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
			Patrons patron;
			PatronVisitSummary patronVisitSummary;
			//gather all of the subjects
			for (Object o: visits.toArray()) {
				visit = (PatronVisits)o;
				patron = visit.getPatron();
				patronVisitSummary = patronSummaries.get(patron.getSortName());
				if (patronVisitSummary == null) {
					patronSummaries.put(patron.getSortName(), new PatronVisitSummary(visit.getTopic(), patron));
				} else {
					patronVisitSummary.addPatronVisit(visit.getTopic());
				}
			}

			reportText = "Patron visit summary\nVisits from: " + SimpleDateFormat.getDateInstance().format(startDate) +
					" to: " + SimpleDateFormat.getDateInstance().format(endDate) +
					"\n\nResearcher Types Summary";
			//generate stats
			Integer visitCount = 0;
			Integer researchersCount = 0;
			Integer researcherTypeCount = 0;
			String patronType;
			Hashtable<String, Integer> researcherTypes = new Hashtable<String, Integer>();
			for (PatronVisitSummary patronSummary: patronSummaries.values()) {
				researchersCount++;
				visitCount += patronSummary.getNumberOfVisits();
				patronType = patronSummary.patron.getPatronType();
				researcherTypeCount = researcherTypes.get(patronType);
				if (researcherTypeCount == null) {
					researcherTypes.put(patronType, 1);
				} else {
					researcherTypes.put(patronType, researcherTypeCount + 1);
				}
			}

			reportText += "\n\nNumber of researchers: " + researchersCount;
			reportText += "\nNumber of visits: " + visitCount + "\n";

			for (String type: researcherTypes.keySet()) {
				reportText += "\n" + type + "\t" + researcherTypes.get(type);
			}

			for (PatronVisitSummary patronSummary: patronSummaries.values()) {
				patronSummary.addToReportText();
			}
			GeneralLogDialog logDialog = new GeneralLogDialog(ApplicationFrame.getInstance());
			logDialog.setTitleText("Patron visit summary");
			logDialog.setLogText(reportText);
			logDialog.showDialog();
		}
	}

	private class PatronVisitSummary {

		Patrons patron;
		Hashtable<String, TopicRecord> topicRecords;

		private PatronVisitSummary(String topic, Patrons patron) {
			this.patron = patron;
			topicRecords = new Hashtable<String, TopicRecord>();
			topicRecords.put(topic, new TopicRecord(topic));
		}

		public void addPatronVisit(String topic) throws ReportExecutionException {
			TopicRecord topicRecord = topicRecords.get(topic);
			if (topicRecord == null) {
				topicRecords.put(topic, new TopicRecord(topic));
			} else {
				topicRecord.addVisit();
			}
		}

		public void addToReportText() {
			reportText+= "\n\n" + patron + "\n" + patron.getTitle();
			for (TopicRecord topicRecord : topicRecords.values()) {
				topicRecord.addToReportText();
			}
		}

		public Integer getNumberOfVisits() {
			Integer numberOfVisists = 0;
			for (TopicRecord topic: topicRecords.values()) {
				numberOfVisists += topic.visitCount;
			}
			return numberOfVisists;
		}

		private class TopicRecord {

			String topic;
			Integer visitCount = 0;

			private TopicRecord(String topic) {
				this.topic = topic;
				visitCount = 1;
			}

			public void addVisit() {

				visitCount++;
			}

			public void addToReportText() {
				reportText+= "\n" + "Topic: " + topic + "\nNumber of days: " + visitCount;
			}
		}

	}

}