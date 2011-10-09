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
	public static final String PROPERTYNAME_PUBLICATION_AUTHOR = "publicationAuthor";
	public static final String PROPERTYNAME_SERIAL_TITLE = "serialTitle";
	public static final String PROPERTYNAME_LOCATION_OF_PUBLICATION = "locationOfPublication";
	public static final String PROPERTYNAME_ISSUE_VOLUME = "issueVolume";
	public static final String PROPERTYNAME_PAGE_NUMBERS = "pageNumbers";
	public static final String PROPERTYNAME_URL = "url";
	public static final String PROPERTYNAME_DATE_ACCESSED = "dateAccessed";
	public static final String PROPERTYNAME_COPY_RECEIVED = "copyReceived";
	public static final String PROPERTYNAME_OTHER = "other";

	

	public static final String PROPERTYNAME_SUBJECTS = "subjects";
	public static final String PROPERTYNAME_NAMES = "names";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long patronPublicationId;

	@IncludeInApplicationConfiguration (1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String publicationDate;

	@IncludeInApplicationConfiguration(3)
	@DefaultIncludeInSearchEditor
	@StringLengthValidationRequried
	private String publicationType;

	@IncludeInApplicationConfiguration(2)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String publicationTitle;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String publisher = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String collaborators = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String publicationAuthor = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String serialTitle = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String locationOfPublication = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String issueVolume = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String pageNumbers = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String url = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Date dateAccessed;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean copyReceived;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String other = "";

	private Set<PatronPublicationsSubjects> subjects = new HashSet<PatronPublicationsSubjects>();
	private Set<PatronPublicationsNames> names = new HashSet<PatronPublicationsNames>();
	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public PatronPublications() {
		publicationTitle = "";
	}

	public PatronPublications(String publicationDate) {
		this.publicationDate = publicationDate;
		publicationTitle = "";
	}

	public PatronPublications(Patrons patron) {
		this.patron = patron;
		publicationTitle = "";
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

	public String getPublicationDate() {
		return this.publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
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
//		Object oldValue = getNotes();
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
			PatronPublicationsNames link = new PatronPublicationsNames(name, this, role, form);
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

	public String getPublicationAuthor() {
		return publicationAuthor;
	}

	public void setPublicationAuthor(String publicationAuthor) {
		this.publicationAuthor = publicationAuthor;
	}

	public String getSerialTitle() {
		return serialTitle;
	}

	public void setSerialTitle(String serialTitle) {
		this.serialTitle = serialTitle;
	}

	public String getLocationOfPublication() {
		return locationOfPublication;
	}

	public void setLocationOfPublication(String locationOfPublication) {
		this.locationOfPublication = locationOfPublication;
	}

	public String getIssueVolume() {
		return issueVolume;
	}

	public void setIssueVolume(String issueVolume) {
		this.issueVolume = issueVolume;
	}

	public String getPageNumbers() {
		return pageNumbers;
	}

	public void setPageNumbers(String pageNumbers) {
		this.pageNumbers = pageNumbers;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDateAccessed() {
		return dateAccessed;
	}

	public void setDateAccessed(Date dateAccessed) {
		this.dateAccessed = dateAccessed;
	}

	public Boolean getCopyReceived() {
		return copyReceived;
	}

	public void setCopyReceived(Boolean copyReceived) {
		this.copyReceived = copyReceived;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
}