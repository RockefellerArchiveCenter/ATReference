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

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.rac.model.PatronVisits;

import java.io.Serializable;


@ExcludeFromDefaultValues
public class PatronVisitsSubjects extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_SUBJECT_TERM = "subjectTerm";
	public static final String PROPERTYNAME_SUBJECT = "subject";

	@IncludeInApplicationConfiguration
	private Long patronVisitsSubjectsId = null;

	@IncludeInApplicationConfiguration(1)
	private Subjects subject;

	private PatronVisits patronVisits;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public PatronVisitsSubjects() {}

	public String toString() {
		return getSubjectTerm();
	}

	/**
	 * Full constructor;
	 */
	public PatronVisitsSubjects(Subjects subject, PatronVisits patronVisits) {
		this.setSubject(subject);
		this.setPatronVisits(patronVisits);
	}

	// ********************** Accessor Methods ********************** //

	public Long getPatronVisitsSubjectsId() {
		return patronVisitsSubjectsId;
	}

	public void setPatronVisitsSubjectsId(Long patronVisitsSubjectsId) {
		this.patronVisitsSubjectsId = patronVisitsSubjectsId;
	}


	public Long getIdentifier() {
		return this.getPatronVisitsSubjectsId();
	}

	public void setIdentifier(Long identifier) {
		this.setPatronVisitsSubjectsId(identifier);
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

	public PatronVisits getPatronVisits() {
		return patronVisits;
	}

	public void setPatronVisits(PatronVisits patronVisits) {
		this.patronVisits = patronVisits;
	}
}