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
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.io.Serializable;


@ExcludeFromDefaultValues
public class PatronVisitsResearchPurposes extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_RESEARCH_PURPOSE = "researchPurpose";

	@IncludeInApplicationConfiguration
	private Long patronVisitsResearchPurposesId = null;

	@IncludeInApplicationConfiguration(1)
	@StringLengthValidationRequried
	private String researchPurpose = "";

	private PatronVisits patronVisits;

	public PatronVisitsResearchPurposes() {
	}

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public PatronVisitsResearchPurposes(PatronVisits patronVisits, String researchPurpose) {
		this.researchPurpose = researchPurpose;
		this.patronVisits = patronVisits;

	}

	public PatronVisitsResearchPurposes(PatronVisits patronVisits) {
		this.patronVisits = patronVisits;
	}

	public String toString() {
		return getResearchPurpose();
	}

	/**
	 * Full constructor;
	 */
	public PatronVisitsResearchPurposes(String subject, PatronVisits patronVisits) {
		this.setResearchPurpose(subject);
		this.setPatronVisits(patronVisits);
	}

	// ********************** Accessor Methods ********************** //

	public Long getPatronVisitsResearchPurposesId() {
		return patronVisitsResearchPurposesId;
	}

	public void setPatronVisitsResearchPurposesId(Long patronVisitsResearchPurposesId) {
		this.patronVisitsResearchPurposesId = patronVisitsResearchPurposesId;
	}


	public Long getIdentifier() {
		return this.getPatronVisitsResearchPurposesId();
	}

	public void setIdentifier(Long identifier) {
		this.setPatronVisitsResearchPurposesId(identifier);
	}

	public void setResearchPurpose(String researchPurpose) {
		this.researchPurpose = researchPurpose;
	}

	public String getResearchPurpose() {
		return researchPurpose;
	}

	public PatronVisits getPatronVisits() {
		return patronVisits;
	}

	public void setPatronVisits(PatronVisits patronVisits) {
		this.patronVisits = patronVisits;
	}
}