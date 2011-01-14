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

import java.util.Date;

public class PatronForms extends DomainObject {

	public static final String PROPERTYNAME_FORM_TYPE = "formType";
	public static final String PROPERTYNAME_NOTES = "notes";
	public static final String PROPERTYNAME_DATE_COMPLETED = "dateCompleted";
	public static final String PROPERTYNAME_COMPLETED = "completed";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long patronFormId;

	@IncludeInApplicationConfiguration(1)
	@DefaultIncludeInSearchEditor
	private String formType;

	@IncludeInApplicationConfiguration(2)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private String notes = "";

	@IncludeInApplicationConfiguration(4)
	@DefaultIncludeInSearchEditor
	private Boolean completed;

	@IncludeInApplicationConfiguration(3)
	@ExcludeFromDefaultValues
	private Date dateCompleted = new Date();

	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public PatronForms() {
		formType = "";
	}

	public PatronForms(String formType) {
		this.formType = formType;
	}

	public PatronForms(Patrons patron) {
		this.patron = patron;
		formType = "";
	}

	public PatronForms(String formType, String notes, Patrons patron) {
		this.formType = formType;
		this.notes = notes;
		this.patron = patron;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getPatronFormId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setPatronFormId(identifier);
	}

	public Long getPatronFormId() {
		return patronFormId;
	}

	public void setPatronFormId(Long patronFormId) {
		this.patronFormId = patronFormId;
	}

	public String getFormType() {
		return this.formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;

	}

	public String getNotes() {
		if (this.notes != null) {
			return this.notes;
		} else {
			return "";
		}
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}


	public Date getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(Date dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public Patrons getPatron() {
		return patron;
	}

	public void setPatron(Patrons patron) {
		this.patron = patron;
	}

	public Boolean getCompleted() {
		return completed;
	}

	public void setCompleted(Boolean completed) {
		this.completed = completed;
	}
}