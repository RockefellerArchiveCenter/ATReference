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
 * Date: May 4, 2010
 * Time: 2:33:45 PM
 */

package org.rac.dialogs;

import org.archiviststoolkit.dialog.QueryEditorDatePanel;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.model.Names;
import org.rac.model.PatronVisits;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.rac.model.Patrons;

import java.util.ArrayList;

public class QueryEditorRelatedDatePanel extends QueryEditorDatePanel {

	private Class clazz;

	@Override
	public ATSearchCriterion getQueryCriterion(Class clazz, String field) {

		this.clazz = clazz;

		ArrayList<CriteriaRelationshipPairs> criterionList = new ArrayList<CriteriaRelationshipPairs>();
		CriteriaRelationshipPairs pair;
		String humanReadableSearchString;

		pair = new CriteriaRelationshipPairs(Restrictions.ge(PatronVisits.PROPERTYNAME_VISIT_DATE, getStartDate()), Patrons.PROPERTYNAME_PATRON_VISITS);
		pair.addCriteria(Restrictions.le(PatronVisits.PROPERTYNAME_VISIT_DATE, getEndDate()));
		pair.setHumanReadableSearchString(PatronVisits.PROPERTYNAME_VISIT_DATE + " is between " + getStartDate().getText() + " and " + getEndDate().getText());
		criterionList.add(pair);

		Criterion criterion = null;
//		criteria = Expression.between(field, startDateValue, endDateValue);
		Conjunction junction;
		junction = Restrictions.conjunction();



		return new ATSearchCriterion(criterion, pair.getHumanReadableSearchString(), pair.getContext());

	}

	public class CriteriaRelationshipPairs {
		private String propertyName;
		private String secondPropertyName = null;
		private ArrayList<Criterion> criteriaList;
		private String humanReadableSearchString;
		private String context = null;

		protected CriteriaRelationshipPairs(Criterion criterion, String propertyName) {
			this.propertyName = propertyName;
			criteriaList = new ArrayList<Criterion>();
			criteriaList.add(criterion);
		}

		protected CriteriaRelationshipPairs(Criterion criterion, String propertyName, String humanReadableSearchString) {
			this.propertyName = propertyName;
			this.humanReadableSearchString = humanReadableSearchString;
			criteriaList = new ArrayList<Criterion>();
			criteriaList.add(criterion);
		}

		protected CriteriaRelationshipPairs(Criterion criterion, String propertyName, String humanReadableSearchString, String context) {
			this.propertyName = propertyName;
			this.humanReadableSearchString = humanReadableSearchString;
			this.context = context;
			criteriaList = new ArrayList<Criterion>();
			criteriaList.add(criterion);
		}

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}

		public ArrayList<Criterion> getCriteriaList() {
			return criteriaList;
		}

		public void setCriteriaList(ArrayList<Criterion> criteriaList) {
			this.criteriaList = criteriaList;
		}

		public void addCriteria(Criterion criterion) {
			this.criteriaList.add(criterion);
		}

		public String getHumanReadableSearchString() {
			return humanReadableSearchString;
		}

		public void setHumanReadableSearchString(String humanReadableSearchString) {
			this.humanReadableSearchString = humanReadableSearchString;
		}

		public String getSecondPropertyName() {
			return secondPropertyName;
		}

		public void setSecondPropertyName(String secondPropertyName) {
			this.secondPropertyName = secondPropertyName;
		}

		public String getContext() {
			String fieldLabel;
			ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz, propertyName);
			if (context != null) {
				fieldLabel = context;
			} else if (fieldInfo != null){
				fieldLabel = fieldInfo.getFieldLabel();
			} else {
				fieldLabel = propertyName;
			}

			if (secondPropertyName == null) {
				return fieldLabel;
			} else {
				String secondContext = "";
				if (ATFieldInfo.getFieldInfo(clazz, secondPropertyName) != null) {
					secondContext = ATFieldInfo.getFieldInfo(clazz, secondPropertyName).getFieldLabel();
				}

				return StringHelper.concat("/", fieldLabel, secondContext);
			}
		}

		public void setContext(String context) {
			this.context = context;
		}

//		public ATSearchCriterion getQueryCriterion(Class clazz, String field) {
//			Date startDateValue = (Date) startDate.getValue();
//			Date endDateValue = (Date) endDate.getValue();
//			Criterion criteria = null;
//			String comparatorString = (String) comparator.getSelectedItem();
//			String humanReadableSearchString = getFieldLabel(clazz, field) + " " + comparatorString + " " + startDate.getText();
//			if (comparatorString.equalsIgnoreCase("Equals")) {
//				criteria = Expression.eq(field, startDateValue);
//			} else if (comparatorString.equalsIgnoreCase("Is between")) {
//				criteria = Expression.between(field, startDateValue, endDateValue);
//				humanReadableSearchString += " and " + endDate.getText();
//			} else if (comparatorString.equalsIgnoreCase("Is greater than")) {
//				criteria = Expression.gt(field, startDateValue);
//			} else if (comparatorString.equalsIgnoreCase("Is greater than or equal to")) {
//				criteria = Expression.ge(field, startDateValue);
//			} else if (comparatorString.equalsIgnoreCase("Is less than")) {
//				criteria = Expression.lt(field, startDateValue);
//			} else if (comparatorString.equalsIgnoreCase("Is less than or equal to")) {
//				criteria = Expression.le(field, startDateValue);
//			}
//			return new ATSearchCriterion(criteria, humanReadableSearchString, field);
//
//		}

	}

}
