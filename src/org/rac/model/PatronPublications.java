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
 * Created on July 19, 2005, 11:54 AM
 * @author leemandell
 *
 */

package org.rac.model;

import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.model.NameEnabledModel;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.SubjectEnabledModel;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class PatronPublications extends DomainObject implements SubjectEnabledModel, NameEnabledModel {

	public static final String PROPERTYNAME_PUBLICATION_DATE = "publicationDate";
	public static final String PROPERTYNAME_PUBLICATION_TITLE = "publicationTitle";
	public static final String PROPERTYNAME_PUBLICATION_TYPE = "publicationType";
	public static final String PROPERTYNAME_PUBLISHER = "publisher";
	public static final String PROPERTYNAME_COLLABORATORS = "collaborators";
	public static final String PROPERTYNAME_SUBJECTS = "subjects";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long patronPublicationId;

	@IncludeInApplicationConfiguration (1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private Date publicationDate;

	@IncludeInApplicationConfiguration(3)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private String publicationType;

	@IncludeInApplicationConfiguration(2)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private String publicationTitle;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String publisher = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String collaborators = "";

	private Set<PatronPublicationsSubjects> subjects = new HashSet<PatronPublicationsSubjects>();
	private Set<PatronPublicationsNames> names = new HashSet<PatronPublicationsNames>();
	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public PatronPublications() {
		publicationTitle = "";
	}

	public PatronPublications(Date publicationDate) {
		this.publicationDate = publicationDate;
		publicationTitle = "";
	}

	public PatronPublications(Patrons patron) {
		this.patron = patron;
		publicationTitle = "";
	}

	public PatronPublications(Date publicationDate, String publicationTitle, String publisher, Patrons name) {
		this.publicationDate = publicationDate;
		this.publicationTitle = "";
		this.publicationTitle = publicationTitle;
		this.publisher = publisher;
		this.patron = patron;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getPatronPublicationId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setPatronPublicationId(identifier);
	}

	public Long getPatronPublicationId() {
		return patronPublicationId;
	}

	public void setPatronPublicationId(Long patronPublicationId) {
		this.patronPublicationId = patronPublicationId;
	}

	public Date getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
//		Object oldValue = getFundingDate();
		this.publicationDate = publicationDate;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM, oldValue, publicationDate);

	}

	public String getPublicationTitle() {
		if (this.publicationTitle != null) {
			return this.publicationTitle;
		} else {
			return "";
		}
	}

	public void setPublicationTitle(String publicationTitle) {
//		Object oldValue = getDescription();
		this.publicationTitle = publicationTitle;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM_TYPE, oldValue, subject);
	}

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }


	public String getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(String collaborators) {
		this.collaborators = collaborators;
	}

	public String getPublicationType() {
		if (publicationType == null) {
			return "";
		} else {
			return publicationType;
		}
	}

	public void setPublicationType(String publicationType) {
		this.publicationType = publicationType;
	}

	public DomainObject addSubject(Subjects subject) throws DuplicateLinkException {
		if (containsSubjectLink(subject.getSubjectTerm())) {
			throw new DuplicateLinkException(subject.getSubjectTerm());
		} else {
			PatronPublicationsSubjects link = new PatronPublicationsSubjects(subject, this);
			if (addSubject(link)) {
				return (link);
			} else {
				return null;
			}
		}
	}

	public boolean addSubject(PatronPublicationsSubjects patronVisitsSubjects) {
		if (patronVisitsSubjects == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		//make sure that the subject is not already linked
		String subjectToAdd = patronVisitsSubjects.getSubjectTerm();
		for (PatronPublicationsSubjects link : getSubjects()) {
			if (link.getSubjectTerm().equalsIgnoreCase(subjectToAdd)) {
				return false;
			}
		}
		this.getSubjects().add(patronVisitsSubjects);
		return true;
	}

	private void removeSubject(PatronPublicationsSubjects subject) {
		if (subject == null)
			throw new IllegalArgumentException("Can't remove a null subject.");

		getSubjects().remove(subject);
	}

	public Set<PatronPublicationsSubjects> getSubjects() {
		return subjects;
	}

	public void setSubjects(Set<PatronPublicationsSubjects> subjects) {
		this.subjects = subjects;
	}

	public boolean containsSubjectLink(String subjectTerm) {
		for (PatronPublicationsSubjects subject: getSubjects()) {
			if (subject.getSubjectTerm().equalsIgnoreCase(subjectTerm)) {
				return true;
			}
		}
		return false;
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
		} else {
			super.addRelatedObject(domainObject);
		}
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	@Override
	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		super.removeRelatedObject(domainObject);
		if (domainObject instanceof PatronPublicationsSubjects) {
			this.removeSubject((PatronPublicationsSubjects)domainObject);
		} else if (domainObject instanceof PatronPublicationsNames) {
			this.removeName((PatronPublicationsNames)domainObject);
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

	public Set<PatronPublicationsNames> getNames() {
		return names;
	}

	public void setNames(Set<PatronPublicationsNames> names) {
		this.names = names;
	}

	public boolean addName(PatronPublicationsNames patronPublicationsName) {
		if (patronPublicationsName == null)
			throw new IllegalArgumentException("Can't add a null contact note.");
		//make sure that the subject is not already linked
		String nameToAdd = patronPublicationsName.getSortName();
		for (PatronPublicationsNames link : getNames()) {
			if (link.getSortName().equalsIgnoreCase(nameToAdd)) {
				return false;
			}
		}
		this.getNames().add(patronPublicationsName);
		return true;
	}

	public DomainObject addName(Names name, String function, String role, String form) throws DuplicateLinkException {
		if (containsNameLink(name.getSortName())) {
			throw new DuplicateLinkException(name.getSortName());
		} else {
			PatronPublicationsNames link = new PatronPublicationsNames(name, this);
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
		for (PatronPublicationsNames name: getNames()) {
			if (name.getSortName().equalsIgnoreCase(sortName)) {
				return true;
			}
		}
		return false;
	}


	private void removeName(PatronPublicationsNames name) {
		if (name == null)
			throw new IllegalArgumentException("Can't remove a null name.");

		getNames().remove(name);
	}
	
}