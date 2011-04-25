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

import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.*;
import org.rac.model.Patrons;

import java.util.Date;

public class PatronFunding extends DomainObject {

	public static final String PROPERTYNAME_FUNDING_DATE = "fundingDate";
	public static final String PROPERTYNAME_FUNDING_TYPE = "fundingType";
	public static final String PROPERTYNAME_AWARD_DETAILS = "awardDetails";
	public static final String PROPERTYNAME_TOPIC = "topic";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long patronFundingId;

	@IncludeInApplicationConfiguration (1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(10)
	private String fundingDate ="";

	@IncludeInApplicationConfiguration(2)
	@DefaultIncludeInSearchEditor
	private String fundingType;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String awardDetails = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private String topic = "";

	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public PatronFunding() {
		fundingType = "";
	}

	public PatronFunding(String fundingDate) {
		this.fundingDate = fundingDate;
		fundingType = "";
	}

	public PatronFunding(Patrons patron) {
		this.patron = patron;
		fundingType = "";
	}

	public PatronFunding(String fundingDate, String fundingType, String awardDetails, Patrons patron) {
		this.fundingDate = fundingDate;
		this.fundingType = "";
		this.fundingType = fundingType;
		this.awardDetails = awardDetails;
		this.patron = patron;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getPatronFundingId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setPatronFundingId(identifier);
	}

	public Long getPatronFundingId() {
		return patronFundingId;
	}

	public void setPatronFundingId(Long patronFundingId) {
		this.patronFundingId = patronFundingId;
	}

	public String getFundingDate() {
		return this.fundingDate;
	}

	public void setFundingDate(String fundingDate) {
//		Object oldValue = getFundingDate();
		this.fundingDate = fundingDate;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM, oldValue, fundingDate);

	}

	public String getFundingType() {
		if (this.fundingType != null) {
			return this.fundingType;
		} else {
			return "";
		}
	}

	public void setFundingType(String fundingType) {
//		Object oldValue = getNotes();
		this.fundingType = fundingType;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM_TYPE, oldValue, subject);
	}

    public String getAwardDetails() {
        return awardDetails;
    }

    public void setAwardDetails(String awardDetails) {
        this.awardDetails = awardDetails;
    }

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Patrons getPatron() {
		return patron;
	}

	public void setPatron(Patrons patron) {
		this.patron = patron;
	}
}