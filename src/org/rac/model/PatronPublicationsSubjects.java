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
 */

package org.rac.model;

import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.rac.model.PatronPublications;

import java.io.Serializable;


@ExcludeFromDefaultValues
public class PatronPublicationsSubjects extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_SUBJECT_TERM = "subjectTerm";
	public static final String PROPERTYNAME_SUBJECT = "subject";

	@IncludeInApplicationConfiguration
	private Long patronPublicationSubjectsId = null;

	@IncludeInApplicationConfiguration(1)
	private Subjects subject;

	private PatronPublications patronPublications;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public PatronPublicationsSubjects() {}

	public String toString() {
		return getSubjectTerm();
	}

	/**
	 * Full constructor;
	 */
	public PatronPublicationsSubjects(Subjects subject, PatronPublications patronPublications) {
		this.setSubject(subject);
		this.setPatronPublications(patronPublications);
	}

	// ********************** Accessor Methods ********************** //

	public Long getPatronPublicationSubjectsId() {
		return patronPublicationSubjectsId;
	}

	public void setPatronPublicationSubjectsId(Long patronPublicationSubjectsId) {
		this.patronPublicationSubjectsId = patronPublicationSubjectsId;
	}


	public Long getIdentifier() {
		return this.getPatronPublicationSubjectsId();
	}

	public void setIdentifier(Long identifier) {
		this.setPatronPublicationSubjectsId(identifier);
	}

	public void setSubject(Subjects subject) {
		this.subject = subject;
	}

	public Subjects getSubject() {
		return subject;
	}

	public String getSubjectTerm() {
		return subject.getSubjectTerm();
	}

	public String getSubjectSource() {
		return subject.getSubjectSource();
	}

	public String getSubjectTermType() {
		return subject.getSubjectTermType();
	}

	public PatronPublications getPatronPublications() {
		return patronPublications;
	}

	public void setPatronPublications(PatronPublications patronPublications) {
		this.patronPublications = patronPublications;
	}
}