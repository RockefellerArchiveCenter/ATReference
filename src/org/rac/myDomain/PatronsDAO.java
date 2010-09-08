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
		super(Names.class);
	}


	public Collection findByQueryEditorAlt(QueryEditor editor, InfiniteProgressPanel progressPanel) {
		Session session;
		Criteria criteria;
		Set returnCollection = new HashSet();
		Set subsequentCollections = null;
		Set returnComponentCollection = new HashSet();
		Set subsequentComponentCollections = null;
		HashMap<DomainObject, String> contextMap = new HashMap<DomainObject, String>();
		boolean includeComponents = false;

		//this is a loop of set intersections. In the first pass create a set
		//in all subsequent passes create a new set and perform an intersection on the
		//two sets. Java does this with the set.retainAll() method.
		boolean firstPass = true;
		subsequentCollections = new HashSet();
//		super.humanReadableSearchString = "";
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
				if (includeComponents) {
					returnComponentCollection.retainAll(subsequentComponentCollections);
				}
			}
		}

		//deal with subject search
		PatronQueryEditor patronQueryEditor = (PatronQueryEditor) editor;
		Subjects selectedSubject = patronQueryEditor.getSelectedSubject();
		DomainAccessObject patronDAO = null;
		Collection patrons = null;
		PatronVisits patronVisit;
		String localHumanReadableSearchString;
		try {
			patronDAO = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Names.class);
		} catch (PersistenceException e) {
			new ErrorDialog("Error creating patron domain access object", e).showDialog();
		}
		if (selectedSubject != null) {
			//time to search through 2 tiers of records
			//there is probably a hibernate way to do this but here we do it the brute force way.
			localHumanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", getHumanReadableSearchString(), " Subject equals " + selectedSubject.getSubjectTerm());
			setHumanReadableSearchString(localHumanReadableSearchString);

			patrons = new HashSet();
			PatronVisitsDAO visitLookup = new PatronVisitsDAO();
			ArrayList patronVisits = new ArrayList(visitLookup.findBySubject(selectedSubject));
			returnCollection = findPatronsBySubject(returnCollection, patrons, patronVisits);

			//now search for subjects associated with publications
			patrons = new HashSet();
			PatronPublicationsDAO publicationsLookup = new PatronPublicationsDAO();
			ArrayList patronPublications = new ArrayList(publicationsLookup.findBySubject(selectedSubject));
			returnCollection = findPatronsBySubject(returnCollection, patrons, patronPublications);
		}


		progressPanel.close();
		return returnCollection;
	}

	private Set findPatronsBySubject(Set returnCollection, Collection patrons, ArrayList linkedToSubjects) {
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
		if (returnCollection.size() == 0) {
			returnCollection = new HashSet(patrons);
		} else {
			returnCollection.retainAll(patrons);
		}
		return returnCollection;
	}

	/**
	 * Find a collection of domain objects by direct hql query.
	 *
	 * @param editor an AT query editor
	 * @return the collection of domain objects
	 */
//	public final Collection findByQueryEditor(final QueryEditor editor, InfiniteProgressPanel progressPanel) {
//
//		boolean includeComponents = false;
//		if (persistentClass == Resources.class && editor.getIncludeComponents()) {
//			includeComponents = true;
//		}
//		if (editor.getAlternateQuery()) {
//			return findByQueryEditorAlt(editor, progressPanel);
//
//		} else if (!includeComponents) {
//			Session session = SessionFactory.getInstance().openSession(getPersistentClass());
//			Criteria criteria = processQueryEditorCriteria(session, editor.getClazz(), editor);
//
//            // if searching digital object then need to see if to only search for parent digital objects
//            if(persistentClass == DigitalObjects.class && !editor.getIncludeComponents()) {
//                criteria.add(Restrictions.isNull("parent"));
//            }
//
//            Collection collection = criteria.list();
//			SessionFactory.getInstance().closeSession(session);
//			return collection;
//
//		} else {
//			Collection<ResourcesComponentsSearchResult> resourcesAndComponetsResults = new ArrayList<ResourcesComponentsSearchResult>();
//			HashMap<DomainObject, String> contextMap = new HashMap<DomainObject, String>();
//			HashMap<ResourcesComponents, Resources> componentParentResourceMap = new HashMap<ResourcesComponents, Resources>();
//
//
//			Session session = SessionFactory.getInstance().openSession(getPersistentClass());
//
//			ATSearchCriterion comparison1 = editor.getCriterion1();
//			ATSearchCriterion comparison2 = editor.getCriterion2();
//
//
//			Criteria criteria = session.createCriteria(editor.getClazz());
//			criteria.add(comparison1.getCiterion());
//			Collection collection = criteria.list();
//
//			if (comparison2.getCiterion() == null) {
//				addContextInfo(contextMap, collection, comparison1.getContext());
//				addResourcesCommonToComponetResultSet(collection, resourcesAndComponetsResults, contextMap);
//				humanReadableSearchString = comparison1.getSearchString();
//			} else {
//				// we have a boolean search
//				Set returnCollection = new HashSet(collection);
//				criteria = session.createCriteria(editor.getClazz());
//				criteria.add(comparison2.getCiterion());
//				Collection collection2 = criteria.list();
//				if (editor.getChosenBoolean1().equalsIgnoreCase("and")) {
//					returnCollection.retainAll(collection2);
//					humanReadableSearchString = comparison1.getSearchString() + " and " + comparison2.getSearchString();
//				} else {
//					returnCollection.addAll(collection2);
//					humanReadableSearchString = comparison1.getSearchString() + " or " + comparison2.getSearchString();
//				}
//				addContextInfo(contextMap, collection, comparison1.getContext());
//				addContextInfo(contextMap, collection2, comparison2.getContext());
//				addResourcesCommonToComponetResultSet(returnCollection, resourcesAndComponetsResults, contextMap);
//
//			}
//			SessionFactory.getInstance().closeSession(session);
//
//
//			Resources resource;
////			addResourcesCommonToComponetResultSet(collection, resourcesAndComponetsResults, null);
//
//			ResourcesDAO resourceDao = new ResourcesDAO();
//			progressPanel.setTextLine("Searching for components that match the criteria", 2);
//
//			session = SessionFactory.getInstance().openSession(ResourcesComponents.class);
//			criteria = session.createCriteria(ResourcesComponents.class);
//			criteria.add(comparison1.getCiterion());
//			Collection components = criteria.list();
//			addContextInfo(contextMap, components, comparison1.getContext());
//			ResourcesComponents component;
//			if (comparison2.getCiterion() == null) {
//				int numberOfComponents = components.size();
//				int count = 1;
//				for (Object object : components) {
//					component = (ResourcesComponents) object;
//					progressPanel.setTextLine("Gathering resources by component matches " + count++ + " of " + numberOfComponents, 2);
//					resource = resourceDao.findResourceByComponent(component);
//					resourcesAndComponetsResults.add(new ResourcesComponentsSearchResult(resource, component, comparison1.getContext()));
//				}
//				SessionFactory.getInstance().closeSession(session);
//				return resourcesAndComponetsResults;
//
//			} else {
//
//				criteria = session.createCriteria(ResourcesComponents.class);
//				criteria.add(comparison2.getCiterion());
//				Collection components2 = criteria.list();
//				addContextInfo(contextMap, components2, comparison2.getContext());
//
//				Set returnCollection = new HashSet(components);
//				if (editor.getChosenBoolean1().equalsIgnoreCase("and")) {
//					returnCollection.retainAll(components2);
//				} else {
//					returnCollection.addAll(components2);
//				}
//				//find the parents for all the components
//				for (Object object : returnCollection) {
//					int numberOfComponents = components.size();
//					int count = 1;
//					component = (ResourcesComponents) object;
//					progressPanel.setTextLine("Gathering resources by component matches " + count++ + " of " + numberOfComponents, 2);
//					resource = resourceDao.findResourceByComponent(component);
//					componentParentResourceMap.put(component, resource);
//				}
//				addResourcesCommonToComponetResultSet(returnCollection, resourcesAndComponetsResults, contextMap, componentParentResourceMap);
//				return resourcesAndComponetsResults;
//			}
//
//
////			criteria = processQueryEditorCriteria(session, ResourcesComponents.class, editor);
//////			Collection components = criteria.list();
//////			ResourcesComponents component;
////			int numberOfComponents = components.size();
////			int count = 1;
////			for (Object object : components) {
////				component = (ResourcesComponents) object;
////				progressPanel.setTextLine("Gathering resources by component matches " + count++ + " of " + numberOfComponents, 2);
////				resource = resourceDao.findResourceByComponent(component);
////				resourcesAndComponetsResults.add(new ResourcesComponentsSearchResult(resource, component, "dummy string"));
////			}
////			SessionFactory.getInstance().closeSession(session);
////			return resourcesAndComponetsResults;
//
//		}
//	}


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

	public Names queryByFirstLastName(String firstName, String lastName) {
		Session session = SessionFactory.getInstance().openSession(getPersistentClass());
		return (Names) session.createCriteria(this.getPersistentClass())
				.add(Restrictions.eq(Names.PROPERTYNAME_PERSONAL_REST_OF_NAME, firstName))
				.add(Restrictions.eq(Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME, lastName)).uniqueResult();

	}
}
