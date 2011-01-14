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

import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.rac.model.Patrons;

public class PatronPhoneNumbers extends DomainObject {

	public static final String PROPERTYNAME_PHONE_NUMBER = "phoneNumber";
	public static final String PROPERTYNAME_PHONE_NUMBER_TYPE = "phoneNumberType";
	public static final String PROPERTYNAME_PREFERRED_PHONE_NUMBER = "preferredPhoneNumber";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long patronPhoneNumberId;

	@IncludeInApplicationConfiguration(1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private String phoneNumber;

	@IncludeInApplicationConfiguration(2)
	private String phoneNumberType = "";

	@IncludeInApplicationConfiguration(3)
	@ExcludeFromDefaultValues
	private Boolean preferredPhoneNumber = false;

	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public PatronPhoneNumbers() {
	}

	public PatronPhoneNumbers(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public PatronPhoneNumbers(Patrons patron) {
		this.patron = patron;
	}

	public PatronPhoneNumbers(String phoneNumber, String phoneNumberType, Patrons patron) {
		this.phoneNumber = phoneNumber;
		this.phoneNumberType = phoneNumberType;
		this.patron = patron;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getPatronPhoneNumberId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setPatronPhoneNumberId(identifier);
	}

	public Long getPatronPhoneNumberId() {
		return patronPhoneNumberId;
	}

	public void setPatronPhoneNumberId(Long patronPhoneNumberId) {
		this.patronPhoneNumberId = patronPhoneNumberId;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
//		Object oldValue = getFundingDate();
		this.phoneNumber = phoneNumber;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM, oldValue, phoneNumber);

	}

	public String getPhoneNumberType() {
		if (this.phoneNumberType != null) {
			return this.phoneNumberType;
		} else {
			return "";
		}
	}

	public void setPhoneNumberType(String phoneNumberType) {
//		Object oldValue = getNotes();
		this.phoneNumberType = phoneNumberType;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM_TYPE, oldValue, phoneNumberType);
	}

	public Boolean getPreferredPhoneNumber() {
		return preferredPhoneNumber;
	}

	public void setPreferredPhoneNumber(Boolean preferredPhoneNumber) {
		this.preferredPhoneNumber = preferredPhoneNumber;
	}

	public Patrons getPatron() {
		return patron;
	}

	public void setPatron(Patrons patron) {
		this.patron = patron;
	}
}