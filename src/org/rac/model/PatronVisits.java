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
 * Created on July 19, 2005, 11:54 AM
 * @author leemandell
 *
 */

package org.rac.model;

import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.model.SubjectEnabledModel;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PatronVisits extends DomainObject implements SubjectEnabledModel, NameEnabledModel {

	public static final String PROPERTYNAME_VISIT_DATE = "visitDate";
	public static final String PROPERTYNAME_CONTACT_ARCHIVIST = "contactArchivist";
	public static final String PROPERTYNAME_TOPIC = "topic";
	public static final String PROPERTYNAME_RESEARCH_PURPOSES = "researchPurposes";
	public static final String PROPERTYNAME_SUBJECTS = "subjects";
	public static final String PROPERTYNAME_NAMES = "names";
    public static final String PROPERTYNAME_RESOURCES = "resources";
    public static final String PROPERTYNAME_VISIT_TYPE = "visitType";
    public static final String PROPERTYNAME_DETAILS_ON_RESOURCES = "detailsOnResourcesUsed";

    //a suite of user defined fields
	public static final String PROPERTYNAME_USER_DEFINED_DATE1 = "userDefinedDate1";
	public static final String PROPERTYNAME_USER_DEFINED_BOOLEAN1 = "userDefinedBoolean1";
	public static final String PROPERTYNAME_USER_DEFINED_INTEGER1 = "userDefinedInteger1";
	public static final String PROPERTYNAME_USER_DEFINED_REAL1 = "userDefinedReal1";
	public static final String PROPERTYNAME_USER_DEFINED_STRING1 = "userDefinedString1";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT1 = "userDefinedText1";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long patronVisitId;

	@IncludeInApplicationConfiguration (1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private Date visitDate;

	@IncludeInApplicationConfiguration(2)
	@DefaultIncludeInSearchEditor
	@StringLengthValidationRequried(50)
	private String contactArchivist = "";

	@IncludeInApplicationConfiguration(3)
	private String topic = "";

    @IncludeInApplicationConfiguration
    private String visitType = "";

    @IncludeInApplicationConfiguration
	private String detailsOnResourcesUsed = "";

    /* Define suit of user defined fields */

    // These user defined variables are used
    @IncludeInApplicationConfiguration
	private String userDefinedString1 = "";

    @IncludeInApplicationConfiguration
    private Boolean userDefinedBoolean1 = null;

    @IncludeInApplicationConfiguration
	private String userDefinedText1 = "";

    // These user defined fields are not used for now (9/1/2011)
    // but are part of the data model for future potential use
	private Date userDefinedDate1 = null;
    private Integer userDefinedInteger1 = null;
    private Double userDefinedReal1 = null;

    /*End user defined fields */

	private Set<PatronVisitsResearchPurposes> researchPurposes = new HashSet<PatronVisitsResearchPurposes>();
	private Set<PatronVisitsSubjects> subjects = new HashSet<PatronVisitsSubjects>();
	private Set<PatronVisitsNames> names = new HashSet<PatronVisitsNames>();
    private Set<PatronVisitsResources> resources = new HashSet<PatronVisitsResources>();

	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public PatronVisits() {
	}

	public PatronVisits(Date visitDate) {
		this.visitDate = visitDate;
	}

	public PatronVisits(Patrons patron) {
		this.patron = patron;
	}

    /**
     * Constructor used when duplicating the visit
     * 
     * @param visitDate
     * @param visitType
     * @param contactArchivist
     * @param topic
     * @param detailsOnResourcesUsed
     * @param userDefinedString1
     * @param userDefinedBoolean1
     * @param userDefinedText1
     * @param patron
     */
	public PatronVisits(Date visitDate, String visitType, String contactArchivist,
                        String topic, String detailsOnResourcesUsed,
                        String userDefinedString1, Boolean userDefinedBoolean1,
                        String userDefinedText1, Patrons patron) {

        this.visitDate = visitDate;
        this.visitType = visitType;
		this.contactArchivist = contactArchivist;
		this.topic = topic;
        this.detailsOnResourcesUsed = detailsOnResourcesUsed;
        this.userDefinedString1 = userDefinedString1;
        this.userDefinedBoolean1 = userDefinedBoolean1;
        this.userDefinedText1 = userDefinedText1;
		this.patron = patron;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getPatronVisitId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setPatronVisitId(identifier);
	}

	public Long getPatronVisitId() {
		return patronVisitId;
	}

	public void setPatronVisitId(Long patronVisitId) {
		this.patronVisitId = patronVisitId;
	}

	@Override
	public String toString() {
		return getVisitDate() + "-" + getTopic();
	}

	public Date getVisitDate() {
		return this.visitDate;
	}

	public void setVisitDate(Date visitDate) {
//		Object oldValue = getFundingDate();
		this.visitDate = visitDate;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM, oldValue, visitDate);

	}

	public String getContactArchivist() {
		if (this.contactArchivist != null) {
			return this.contactArchivist;
		} else {
			return "";
		}
	}

	public void setContactArchivist(String contactArchivist) {
//		Object oldValue = getNotes();
		this.contactArchivist = contactArchivist;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM_TYPE, oldValue, contactArchivist);
	}

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getVisitType() {
        return visitType;
    }

    public void setVisitType(String visitType) {
        this.visitType = visitType;
    }

    public String getDetailsOnResourcesUsed() {
        return detailsOnResourcesUsed;
    }

    public void setDetailsOnResourcesUsed(String detailsOnResourcesUsed) {
        this.detailsOnResourcesUsed = detailsOnResourcesUsed;
    }

    public String getUserDefinedString1() {
        return userDefinedString1;
    }

    public void setUserDefinedString1(String userDefinedString1) {
        this.userDefinedString1 = userDefinedString1;
    }

    public Boolean getUserDefinedBoolean1() {
        return userDefinedBoolean1;
    }

    public void setUserDefinedBoolean1(Boolean userDefinedBoolean1) {
        this.userDefinedBoolean1 = userDefinedBoolean1;
    }

    public String getUserDefinedText1() {
        return userDefinedText1;
    }

    public void setUserDefinedText1(String userDefinedText1) {
        this.userDefinedText1 = userDefinedText1;
    }

    public Date getUserDefinedDate1() {
        return userDefinedDate1;
    }

    public void setUserDefinedDate1(Date userDefinedDate1) {
        this.userDefinedDate1 = userDefinedDate1;
    }

    public Integer getUserDefinedInteger1() {
        return userDefinedInteger1;
    }

    public void setUserDefinedInteger1(Integer userDefinedInteger1) {
        this.userDefinedInteger1 = userDefinedInteger1;
    }

    public Double getUserDefinedReal1() {
        return userDefinedReal1;
    }

    public void setUserDefinedReal1(Double userDefinedReal1) {
        this.userDefinedReal1 = userDefinedReal1;
    }

    public Set<PatronVisitsSubjects> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<PatronVisitsSubjects> subjects) {
		this.subjects = subjects;
	}


	public boolean addSubject(PatronVisitsSubjects patronVisitsSubjects) {
		if (patronVisitsSubjects == null)
			throw new IllegalArgumentException("Can't add a null subject.");
		//make sure that the subject is not already linked
		String subjectToAdd = patronVisitsSubjects.getSubjectTerm();
		for (PatronVisitsSubjects link : getSubjects()) {
			if (link.getSubjectTerm().equalsIgnoreCase(subjectToAdd)) {
				return false;
			}
		}
		this.getSubjects().add(patronVisitsSubjects);
		return true;
	}

	public DomainObject addSubject(Subjects subject) throws DuplicateLinkException {
		if (containsSubjectLink(subject.getSubjectTerm())) {
			throw new DuplicateLinkException(subject.getSubjectTerm());
		} else {
			PatronVisitsSubjects link = new PatronVisitsSubjects(subject, this);
			if (addSubject(link)) {
				return (link);
			} else {
				return null;
			}
		}
	}

	public boolean containsSubjectLink(String subjectTerm) {
		for (PatronVisitsSubjects subject: getSubjects()) {
			if (subject.getSubjectTerm().equalsIgnoreCase(subjectTerm)) {
				return true;
			}
		}
		return false;
	}

	private void removeSubject(PatronVisitsSubjects subject) {
		if (subject == null)
			throw new IllegalArgumentException("Can't remove a null subject.");

		getSubjects().remove(subject);
	}

	//name links
	public Set<PatronVisitsNames> getNames() {
		return names;
	}

	public void setNames(Set<PatronVisitsNames> names) {
		this.names = names;
	}

	public boolean addName(PatronVisitsNames patronVisitsName) {
		if (patronVisitsName == null)
			throw new IllegalArgumentException("Can't add a null name note.");
		//make sure that the subject is not already linked
		String nameToAdd = patronVisitsName.getSortName();
		for (PatronVisitsNames link : getNames()) {
			if (link.getSortName().equalsIgnoreCase(nameToAdd)) {
				return false;
			}
		}
		this.getNames().add(patronVisitsName);
		return true;
	}

	public DomainObject addName(Names name, String function, String role, String form) throws DuplicateLinkException {
		if (containsNameLink(name.getSortName())) {
			throw new DuplicateLinkException(name.getSortName());
		} else {
			PatronVisitsNames link = new PatronVisitsNames(name, this, role, form);
			if (addName(link)) {
				return (link);
			} else {
				return null;
			}
		}
	}

	public DomainObject addName(Names name) throws DuplicateLinkException {
		return addName(name, Patrons.NAME_LINK_FUNCTION_SUBJECT, "", "");
	}

	public boolean containsNameLink(String sortName) {
		for (PatronVisitsNames name: getNames()) {
			if (name.getSortName().equalsIgnoreCase(sortName)) {
				return true;
			}
		}
		return false;
	}


	private void removeName(PatronVisitsNames name) {
		if (name == null)
			throw new IllegalArgumentException("Can't remove a null name.");

		getNames().remove(name);
	}
	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	@Override
	public void addRelatedObject(DomainObject domainObject) throws AddRelatedObjectException, DuplicateLinkException {
		if (domainObject instanceof Subjects) {
			this.addSubject((Subjects)domainObject);
		} else if (domainObject instanceof Names) {
			this.addName((Names)domainObject);
		} else if (domainObject instanceof PatronVisitsResearchPurposes) {
			this.addResearchPurposes((PatronVisitsResearchPurposes)domainObject);
		} else {
			super.addRelatedObject(domainObject);
		}
	}

	/**
        * Method to remove any related objects
	 * @param domainObject the domain object to be removed
	 */
	@Override
	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		super.removeRelatedObject(domainObject);
		if (domainObject instanceof PatronVisitsSubjects) {
			this.removeSubject((PatronVisitsSubjects)domainObject);
		} else if (domainObject instanceof PatronVisitsNames) {
			this.removeName((PatronVisitsNames)domainObject);
		} else if (domainObject instanceof PatronVisitsResearchPurposes) {
			this.removeResearchPurposes((PatronVisitsResearchPurposes)domainObject);
		} else if (domainObject instanceof PatronVisitsResources) {
			this.removeResource((PatronVisitsResources)domainObject);
		} else {
			super.removeRelatedObject(domainObject);			
		}
	}

	public Patrons getPatron() {
		return patron;
	}

	public void setPatron(Patrons patron) {
		this.patron = patron;
	}

	public Set<PatronVisitsResearchPurposes> getResearchPurposes() {
		return researchPurposes;
	}

	public void setResearchPurposes(Set<PatronVisitsResearchPurposes> researchPurposes) {
		this.researchPurposes = researchPurposes;
	}

    public Set<PatronVisitsResources> getResources() {
        return resources;
    }

    public void setResources(Set<PatronVisitsResources> resources) {
        this.resources = resources;
    }

    private void removeResearchPurposes(PatronVisitsResearchPurposes researchPurpose) {
		if (researchPurpose == null)
			throw new IllegalArgumentException("Can't remove a null research purpose.");

		getResearchPurposes().remove(researchPurpose);
	}

	public void addResearchPurposes(String researchPurpose)  {

		if (researchPurpose == null) {
			throw new IllegalArgumentException("Can't add a null research purpose.");
        }

		getResearchPurposes().add(new PatronVisitsResearchPurposes(this, researchPurpose));
	}

    public void addResearchPurposes(PatronVisitsResearchPurposes patronVisitsResearchPurposes) {
        if (patronVisitsResearchPurposes == null) {
			throw new IllegalArgumentException("Can't add a null research purpose.");
        }

        getResearchPurposes().add(patronVisitsResearchPurposes);
    }

    /**
     * Method to link a resource to the assessment object
     * @param resource the resource to link
     * @return The PatronVisitsResource linking object
     */
    public PatronVisitsResources addResource(Resources resource) throws DuplicateLinkException {
		if (resource == null)
			throw new IllegalArgumentException("Can't add a null resource.");
		if (containsResource(resource)) {
			throw new DuplicateLinkException(resource.toString());
		} else {
			PatronVisitsResources link = new PatronVisitsResources(resource, this);
			this.getResources().add(link);
			return link;
		}
	}

    /**
     * Method to check to see if this resource is already linked to this object
     * @param resource The resource to look for
     *
     * @return true if the resource is already link or false if it is not
     */
    private boolean containsResource(Resources resource) {
		for (PatronVisitsResources resourceLink: getResources()) {
			if (resourceLink.getResource() == resource) {
				return true;
			}
		}
		return false;
	}

    /**
     * Method to remove the resource record link
     *
     * @param patronVisitsResource
     */
    private void removeResource(PatronVisitsResources patronVisitsResource) {
		if (patronVisitsResource == null)
			throw new IllegalArgumentException("Can't remove a null resource link");

		getResources().remove(patronVisitsResource);
	}
}