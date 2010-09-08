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

public class CompletedForms extends DomainObject {

	public static final String PROPERTYNAME_FORM_TYPE = "formType";
	public static final String PROPERTYNAME_DESCRIPTION = "description";
	public static final String PROPERTYNAME_DATE_COMPLETED = "dateCompleted";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long completedFormId;

	@IncludeInApplicationConfiguration(1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private String formType;

	@IncludeInApplicationConfiguration(2)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private String description = "";

	@IncludeInApplicationConfiguration(3)
	@ExcludeFromDefaultValues
	private Date dateCompleted = new Date();

	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public CompletedForms() {
		formType = "";
	}

	public CompletedForms(String formType) {
		this.formType = formType;
	}

	public CompletedForms(Patrons patron) {
		this.patron = patron;
		formType = "";
	}

	public CompletedForms(String formType, String description, Patrons patron) {
		this.formType = formType;
		this.description = description;
		this.patron = patron;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getCompletedFormId();
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setCompletedFormId(identifier);
	}

	public Long getCompletedFormId() {
		return completedFormId;
	}

	public void setCompletedFormId(Long completedFormId) {
		this.completedFormId = completedFormId;
	}

	public String getFormType() {
		return this.formType;
	}

	public void setFormType(String formType) {
//		Object oldValue = getFundingDate();
		this.formType = formType;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM, oldValue, formType);

	}

	public String getDescription() {
		if (this.description != null) {
			return this.description;
		} else {
			return "";
		}
	}

	public void setDescription(String description) {
//		Object oldValue = getDescription();
		this.description = description;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM_TYPE, oldValue, description);
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
}