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
 * Date: Apr 30, 2010
 * Time: 4:11:17 PM
 */

package org.rac.myDomain;

import org.archiviststoolkit.dialog.QueryEditor;
import org.archiviststoolkit.exceptions.MergeException;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.util.ATDateUtils;
import org.hibernate.*;
import org.rac.model.*;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.rac.dialogs.PatronQueryEditor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class PatronsDAO extends DomainAccessObjectImpl {

	private String humanReadableSearchString = "";

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public PatronsDAO() {
		super(Patrons.class);
	}

    /**
     * Method to search by link records
     *
     * @param patronQueryEditor
     * @param progressPanel
     * @return
     */
	public Collection findByQueryEditorAlt(PatronQueryEditor patronQueryEditor, InfiniteProgressPanel progressPanel) {
        Session session;
		Criteria criteria;
		Set returnCollection = new HashSet();
		Set subsequentCollections = null;

		//this is a loop of set intersections. In the first pass create a set
		//in all subsequent passes create a new set and perform an intersection on the
		//two sets. Java does this with the set.retainAll() method.
		boolean firstPass = true;
		subsequentCollections = new HashSet();
		for (QueryEditor.CriteriaRelationshipPairs criteriaPair : patronQueryEditor.getAltFormCriteria()) {
			session = SessionFactory.getInstance().openSession(getPersistentClass());
			criteria = processCriteria(session, patronQueryEditor.getClazz(), criteriaPair);

            // Determine whether to search patron records which are inactive or
            // just the active ones
            if(!patronQueryEditor.searchInActivePatrons()) {
                criteria.add(Restrictions.or(
                Restrictions.eq(Patrons.PROPERTYNAME_INACTIVE, false),
                Restrictions.isNull(Patrons.PROPERTYNAME_INACTIVE)));
            }

			if (firstPass) {
				returnCollection = new HashSet(criteria.list());
			} else {
				subsequentCollections = new HashSet(criteria.list());
			}

            SessionFactory.getInstance().closeSession(session);

			if (firstPass) {
				firstPass = false;
			} else {
				returnCollection.retainAll(subsequentCollections);
			}
		}

        // deal with subject, names, resource, and funding date range search
        Subjects selectedSubject = patronQueryEditor.getSelectedSubject();
		Names selectedName = patronQueryEditor.getSelectedName();
        Resources selectedResource = patronQueryEditor.getSelectedResource();

        String localHumanReadableSearchString;
		Collection patrons = null;
        Set subjectCollection = new HashSet();
		Set nameCollection = new HashSet();
        Set resourceCollection = new HashSet();
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

        if (selectedResource != null) {
			//time to search through 2 tiers of records
			//there is probably a hibernate way to do this but here we do it the brute force way.
			localHumanReadableSearchString = StringHelper.concat(" <font color='red'>and</font> ", getHumanReadableSearchString(), " Resource equals " + selectedResource.toString());
			setHumanReadableSearchString(localHumanReadableSearchString);

			//now search for resources associated with visits
			patrons = new HashSet();
			PatronVisitsDAO visitLookup = new PatronVisitsDAO();
			ArrayList patronVisits = new ArrayList(visitLookup.findByResource(selectedResource));
			findPatronsByResource(resourceCollection, patrons, patronVisits);

			if (returnCollection.size() == 0) {
				returnCollection = resourceCollection;
			} else {
				returnCollection.retainAll(resourceCollection);
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

        // Deal with funding date range search. Since the funding date is stored
        // has an iso date string, we need to do the actually do the
        // date range search on all patron records that where returned, removing
        // those that didn't match the date range.
        Set fundingDateCollection = new HashSet();

        if(patronQueryEditor.areFundingDatesFilledOut()) {
            Date[] fundingDates = patronQueryEditor.getFundingDates();
            findPatronsByFundingDates(fundingDateCollection, returnCollection, fundingDates);
            returnCollection = fundingDateCollection;
        }

		progressPanel.close();

        return returnCollection;
	}

	private void findPatronsBySubject(Set subjectCollection, Collection patrons, ArrayList linkedToSubjects) {
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
     * Method to find the patron record from a patron visit record which has a resource
     * record linked to it
     *
     * @param resourceCollection
     * @param patrons
     * @param linkedToResource
     */
    private void findPatronsByResource(Set resourceCollection, Collection patrons, ArrayList linkedToResource) {
		linkedToResource.toArray();
		Patrons patronToAdd = null;
		for (Object o : linkedToResource) {
			if (o instanceof PatronVisits) {
				patronToAdd = ((PatronVisits)o).getPatron();
			}

            patrons.add(patronToAdd);
		}
		resourceCollection.addAll(patrons);
	}

    /**
     * Method to find patrons by funding date range search
     * 
     * @param fundingDatesCollection
     * @param returnCollection
     * @param fundingDates
     */
    private void findPatronsByFundingDates(Set fundingDatesCollection, Set returnCollection, Date[] fundingDates) {
		getLongSession(); // get the long session so we can get funding dates

        Patrons patronToAdd = null;

        DateFormat isodf = new SimpleDateFormat("yyyy-MM-dd");

        for (Object o : returnCollection) {
			Patrons patron = (Patrons)o;

            try {
                patronToAdd = (Patrons)findByPrimaryKeyLongSession(patron.getIdentifier());
                Set<PatronFunding> patronFundings = patronToAdd.getPatronFunding();
                for(PatronFunding patronFunding: patronFundings) {
                    // convert the dateString which should be in the
                    // iso format yyyy-MM-dd to a date object
                    String dateString = patronFunding.getFundingDate();
                    Date fundDate = isodf.parse(dateString);

                    if(ATDateUtils.isWithinRange(fundingDates, fundDate)) {
                        fundingDatesCollection.add(patron);
                        // break out of inner loop since one of the
                        // funding dates matched
                        break;
                    }

                    System.out.println("Date is " + dateString);
                }
            } catch(LookupException e ) {
                e.printStackTrace();
            } catch(ParseException e) {
                e.printStackTrace();
            }
		}
	}

	/**
	 * Method to search by query editor using long session
	 *
	 * @param editor		The query editor
	 * @param progressPanel The progress panel
	 * @return The result that were found or an empty string
	 */
	public final Collection findByQueryEditorLongSession(PatronQueryEditor editor, InfiniteProgressPanel progressPanel) {

		Criteria criteria = processQueryEditorCriteria(getLongSession(), Patrons.class, editor);
		return criteria.list();
	}


	private Criteria processQueryEditorCriteria(Session session, Class clazz, PatronQueryEditor editor) {
		Criteria criteria = session.createCriteria(clazz);
		ATSearchCriterion comparison1 = editor.getCriterion1();
		ATSearchCriterion comparison2 = editor.getCriterion2();

        // Determine whether to search patron records which are inactive or just the
        // active ones
        if(!editor.searchInActivePatrons()) {
            criteria.add(Restrictions.or(
                    Restrictions.eq(Patrons.PROPERTYNAME_INACTIVE, false),
                    Restrictions.isNull(Patrons.PROPERTYNAME_INACTIVE)));
        }

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

    /**
     * Method to look up a patron record by the first, last name, and suffix.
     * If suffix is empty, then just search by the first and last name.
     *
     * @param firstName
     * @param lastName
     * @param suffix
     * @return  patron record that is found
     */
    public Patrons queryNameByFirstLastPrefixAndSuffix(String firstName, String lastName, String prefix, String suffix) {
        Session session = SessionFactory.getInstance().openSession(getPersistentClass());

        return (Patrons) session.createCriteria(this.getPersistentClass())
				.add(Restrictions.eq(Patrons.PROPERTYNAME_REST_OF_NAME, lastName))
				.add(Restrictions.eq(Patrons.PROPERTYNAME_PRIMARY_NAME, firstName))
                .add(Restrictions.eq(Patrons.PROPERTYNAME_PREFIX, prefix))
                .add(Restrictions.eq(Patrons.PROPERTYNAME_SUFFIX, suffix)).uniqueResult();
	}

    /**
     * Method to perform merging of patron records. Essentially, it will ont copy the linked records
     * into the patron record being merged to.
     *
     * @param mergeFrom
     * @param mergeTo
     * @param progressPanel
     * @return
     * @throws MergeException
     */
    public int merge(Collection<DomainObject> mergeFrom, DomainObject mergeTo, InfiniteProgressPanel progressPanel) throws MergeException {
		Session session = SessionFactory.getInstance().openSession();
		Transaction tx = session.beginTransaction();

        Patrons patronMergeTo = (Patrons)mergeTo;
		session.lock(patronMergeTo, LockMode.NONE);

		Patrons patron;
		String message;

        int totalCount = 0;
		int patronsToMerge = mergeFrom.size() - 1;
		int patronMerged = 1;

		for (DomainObject domainObject: mergeFrom) {
			try {
				patron = (Patrons)domainObject;
				if (!patron.equals(patronMergeTo)) {
					session.lock(patron, LockMode.NONE);

                    progressPanel.setTextLine("Merging (record " + patronMerged++ + " of " + patronsToMerge + ")...", 1);
					progressPanel.setTextLine(patron + " -> " + patronMergeTo, 2);

                    int count = 0;
					int numberOfLinks = 0;

                    // Transfer the patron addresses
                    count = 1;
                    numberOfLinks = patron.getPatronAddresses().size();
					for (PatronAddresses address: patron.getPatronAddresses()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
                            address.setPatron(patronMergeTo);
                            address.setPreferredAddress(false);
							patronMergeTo.addPatonsAddresses(address);
							totalCount++;
						} catch (IllegalArgumentException e) {
							//do nothing
						}
					}

                    // Transfer the patron visits
                    count = 1;
                    numberOfLinks = patron.getPatronVisits().size();
					for (PatronVisits visit: patron.getPatronVisits()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
                            visit.setPatron(patronMergeTo);
							patronMergeTo.addPatronVisit(visit);
							totalCount++;
						} catch (IllegalArgumentException e) {
							//do nothing
						}
					}

                    // Transfer the patron publications
                    count = 1;
                    numberOfLinks = patron.getPatronPublications().size();
					for (PatronPublications publication: patron.getPatronPublications()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
                            publication.setPatron(patronMergeTo);
							patronMergeTo.addPatronPublication(publication);
							totalCount++;
						} catch (IllegalArgumentException e) {
							//do nothing
						}
					}

                    // Transfer the patron funding
                    count = 1;
                    numberOfLinks = patron.getPatronFunding().size();
					for (PatronFunding funding: patron.getPatronFunding()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
                            funding.setPatron(patronMergeTo);
							patronMergeTo.addPatronFunding(funding);
							totalCount++;
						} catch (IllegalArgumentException e) {
							//do nothing
						}
					}

                    // Transfer the patron phone number
                    count = 1;
                    numberOfLinks = patron.getPatronPhoneNumbers().size();
					for (PatronPhoneNumbers phone: patron.getPatronPhoneNumbers()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
                            phone.setPatron(patronMergeTo);
                            phone.setPreferredPhoneNumber(false);
							patronMergeTo.addPatronPhoneNumbers(phone);
							totalCount++;
						} catch (IllegalArgumentException e) {
							//do nothing
						}
					}

                    // Transfer the patron forms
                    count = 1;
                    numberOfLinks = patron.getPatronForms().size();
					for (PatronForms form: patron.getPatronForms()) {
						try {
							message = "relationship " + count++ + " of " + numberOfLinks;
							System.out.println(message);
							progressPanel.setTextLine(message, 3);
                            form.setPatron(patronMergeTo);
							patronMergeTo.addPatronForms(form);
							totalCount++;
						} catch (IllegalArgumentException e) {
							//do nothing
						}
					}

                    /**
                                  * TODO 7/15/2011 Add support for merging services here
                                 **/

                    // Set all link records to null, so we don't get a hibernate
                    // exception complaining about object will be re-created by
                    // cascade
                    patron.setPatronAddresses(null);
                    patron.setPatronVisits(null);
                    patron.setPatronPublications(null);
                    patron.setPatronFunding(null);
                    patron.setPatronPhoneNumbers(null);
                    patron.setPatronForms(null);

                    // now delete this patron record
                    session.delete(patron);
				}
			} catch (HibernateException e) {
				throw new MergeException("Error merging patrons", e);
			}
		}

        session.update(patronMergeTo);
		tx.commit();
		session.close();
		return totalCount;
	}
}
