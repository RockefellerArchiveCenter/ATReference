/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.  
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
 * Date: Apr 30, 2010
 * Time: 4:11:17 PM
 */

package org.rac.myDomain;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.QueryEditor;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.Names;
import org.rac.model.PatronPublications;
import org.rac.model.PatronVisits;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.rac.dialogs.PatronQueryEditor;
import org.rac.model.Patrons;

import java.util.*;

public class PatronsDAO extends DomainAccessObjectImpl {

	private String humanReadableSearchString = "";

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public PatronsDAO() {
		super(Patrons.class);
	}


	public Collection findByQueryEditorAlt(QueryEditor editor, InfiniteProgressPanel progressPanel) {
		Session session;
		Criteria criteria;
		Set returnCollection = new HashSet();
		Set subsequentCollections = null;
		HashMap<DomainObject, String> contextMap = new HashMap<DomainObject, String>();
		boolean includeComponents = false;

		//this is a loop of set intersections. In the first pass create a set
		//in all subsequent passes create a new set and perform an intersection on the
		//two sets. Java does this with the set.retainAll() method.
		boolean firstPass = true;
		subsequentCollections = new HashSet();
		for (QueryEditor.CriteriaRelationshipPairs criteriaPair : editor.getAltFormCriteria()) {
			session = SessionFactory.getInstance().openSession(getPersistentClass());
			criteria = processCriteria(session, editor.getClazz(), criteriaPair);

			if (firstPass) {
				returnCollection = new HashSet(criteria.list());
			} else {
				subsequentCollections = new HashSet(criteria.list());
			}
			if (includeComponents) {
				addContextInfo(contextMap, criteria.list(), criteriaPair.getContext());
			}
			SessionFactory.getInstance().closeSession(session);

			if (firstPass) {
				firstPass = false;
			} else {
				returnCollection.retainAll(subsequentCollections);
			}
		}

		//deal with subject and names search
		PatronQueryEditor patronQueryEditor = (PatronQueryEditor) editor;
		Subjects selectedSubject = patronQueryEditor.getSelectedSubject();
		Names selectedName = patronQueryEditor.getSelectedName();
		Collection patrons = null;
		String localHumanReadableSearchString;
		Set subjectCollection = new HashSet();
		Set nameCollection = new HashSet();
		Set researchPurposeCollection = new HashSet();

		if (selectedSubject != null) {
			//time to search through 2 tiers of records
			//there is probably a hibernate way to do this but here we do it the brute force way.
			localHumanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", getHumanReadableSearchString(), " Subject equals " + selectedSubject.getSubjectTerm());
			setHumanReadableSearchString(localHumanReadableSearchString);

			//now search for subjects associated with visits
			patrons = new HashSet();
			PatronVisitsDAO visitLookup = new PatronVisitsDAO();
			ArrayList patronVisits = new ArrayList(visitLookup.findBySubject(selectedSubject));
			findPatronsBySubject(subjectCollection, patrons, patronVisits);

			//now search for subjects associated with publications
			patrons = new HashSet();
			PatronPublicationsDAO publicationsLookup = new PatronPublicationsDAO();
			ArrayList patronPublications = new ArrayList(publicationsLookup.findBySubject(selectedSubject));
			findPatronsBySubject(subjectCollection, patrons, patronPublications);


			if (returnCollection.size() == 0) {
				returnCollection = subjectCollection;
			} else {
				returnCollection.retainAll(subjectCollection);
			}

		}

		if (selectedName != null) {
			//time to search through 2 tiers of records
			//there is probably a hibernate way to do this but here we do it the brute force way.
			localHumanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", getHumanReadableSearchString(), " Subject equals " + selectedName.getSortName());
			setHumanReadableSearchString(localHumanReadableSearchString);

			//now search for names associated with visits
			patrons = new HashSet();
			PatronVisitsDAO visitLookup = new PatronVisitsDAO();
			ArrayList patronVisits = new ArrayList(visitLookup.findByName(selectedName));
			findPatronsByName(nameCollection, patrons, patronVisits);

			//now search for names associated with publications
			patrons = new HashSet();
			PatronPublicationsDAO publicationsLookup = new PatronPublicationsDAO();
			ArrayList patronPublications = new ArrayList(publicationsLookup.findByName(selectedName));
			findPatronsByName(nameCollection, patrons, patronPublications);


			if (returnCollection.size() == 0) {
				returnCollection = nameCollection;
			} else {
				returnCollection.retainAll(nameCollection);
			}

		}

		String researchPurpose = patronQueryEditor.getResearchPurpose();
		if (researchPurpose.length() > 0) {
			//time to search through 2 tiers of records
			//there is probably a hibernate way to do this but here we do it the brute force way.
			localHumanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", getHumanReadableSearchString(), " Research Purpose contains " + researchPurpose);
			setHumanReadableSearchString(localHumanReadableSearchString);

			//now search for research purposes associated with visits
			patrons = new HashSet();
			PatronVisitsDAO visitLookup = new PatronVisitsDAO();
			ArrayList patronVisits = new ArrayList(visitLookup.findByResearchPurpose(researchPurpose));
			Patrons patronToAdd = null;
			for (Object o: patronVisits) {
				patronToAdd = ((PatronVisits)o).getPatron();
				researchPurposeCollection.add(patronToAdd);
			}

			if (returnCollection.size() == 0) {
				returnCollection = researchPurposeCollection;
			} else {
				returnCollection.retainAll(researchPurposeCollection);
			}

		}

		progressPanel.close();
		return returnCollection;
	}

	private void findPatronsBySubject(Set subjectCollection, Collection patrons, ArrayList linkedToSubjects) {
		PatronVisits patronVisit;
		linkedToSubjects.toArray();
		Patrons patronToAdd = null;
		for (Object o : linkedToSubjects) {
			if (o instanceof PatronVisits) {
				patronToAdd = ((PatronVisits)o).getPatron();
			} else if (o instanceof PatronPublications){
				patronToAdd = ((PatronPublications)o).getPatron();
			}
			patrons.add(patronToAdd);
		}
		subjectCollection.addAll(patrons);
	}

	private void findPatronsByName(Set nameCollection, Collection patrons, ArrayList linkedToNames) {
		linkedToNames.toArray();
		Patrons patronToAdd = null;
		for (Object o : linkedToNames) {
			if (o instanceof PatronVisits) {
				patronToAdd = ((PatronVisits)o).getPatron();
			} else if (o instanceof PatronPublications){
				patronToAdd = ((PatronPublications)o).getPatron();
			}
			patrons.add(patronToAdd);
		}
		nameCollection.addAll(patrons);
	}

	/**
	 * Method to search by query editory using long session
	 *
	 * @param editor		The quesry editor
	 * @param progressPanel The progress panel
	 * @return The result that were found or an empty string
	 */
	public final Collection findByQueryEditorLongSession(PatronQueryEditor editor, InfiniteProgressPanel progressPanel) {

		Criteria criteria = processQueryEditorCriteria(getLongSession(), Names.class, editor);
		return criteria.list();
	}


	private Criteria processQueryEditorCriteria(Session session, Class clazz, PatronQueryEditor editor) {
		Criteria criteria = session.createCriteria(clazz);
		ATSearchCriterion comparison1 = editor.getCriterion1();
		ATSearchCriterion comparison2 = editor.getCriterion2();
		if (comparison2.getCiterion() == null) {
			criteria.add(comparison1.getCiterion());
			humanReadableSearchString = comparison1.getSearchString();
		} else {
			if (editor.getChosenBoolean1().equalsIgnoreCase("and")) {
				criteria.add(comparison1.getCiterion());
				criteria.add(comparison2.getCiterion());
				humanReadableSearchString = comparison1.getSearchString() + " <font color='red'>and</font> " + comparison2.getSearchString();
			} else {
				criteria.add(Expression.or(comparison1.getCiterion(), comparison2.getCiterion()));
				humanReadableSearchString = comparison1.getSearchString() + " <font color='red'>or</font> " + comparison2.getSearchString();
			}
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return criteria;
	}

	public Patrons queryByFirstLastName(String firstName, String lastName) {
		Session session = SessionFactory.getInstance().openSession(getPersistentClass());
		return (Patrons) session.createCriteria(this.getPersistentClass())
				.add(Restrictions.eq(Patrons.PROPERTYNAME_REST_OF_NAME, firstName))
				.add(Restrictions.eq(Patrons.PROPERTYNAME_PRIMARY_NAME, lastName)).uniqueResult();

	}
}
