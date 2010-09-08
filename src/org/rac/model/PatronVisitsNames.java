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
 */

package org.rac.model;

import org.archiviststoolkit.model.Names;
import org.rac.model.PatronVisits;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;

import java.io.Serializable;


@ExcludeFromDefaultValues
public class PatronVisitsNames extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_SORT_NAME = "sortName";
	public static final String PROPERTYNAME_PATRON = "patron";

	@IncludeInApplicationConfiguration
	private Long patronVisitsNamesId = null;

	@IncludeInApplicationConfiguration(1)
	private Names name;

	private PatronVisits patronVisits;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public PatronVisitsNames() {}

	public String toString() {
		return getSortName();
	}

	/**
	 * Full constructor;
	 */
	public PatronVisitsNames(Names name, PatronVisits patronVisits) {
		this.setName(name);
		this.setPatronVisits(patronVisits);
	}

	// ********************** Accessor Methods ********************** //

	public Long getPatronVisitsNamesId() {
		return patronVisitsNamesId;
	}

	public void setPatronVisitsNamesId(Long patronVisitsNamesId) {
		this.patronVisitsNamesId = patronVisitsNamesId;
	}


	public Long getIdentifier() {
		return this.getPatronVisitsNamesId();
	}

	public void setIdentifier(Long identifier) {
		this.setPatronVisitsNamesId(identifier);
	}

	public String getSortName() {
		return name.getSortName();
	}

	public PatronVisits getPatronVisits() {
		return patronVisits;
	}

	public void setPatronVisits(PatronVisits patronVisits) {
		this.patronVisits = patronVisits;
	}

	public Names getName() {
		return name;
	}

	public void setName(Names name) {
		this.name = name;
	}
}