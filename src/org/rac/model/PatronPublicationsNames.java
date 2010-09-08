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
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;

import java.io.Serializable;


@ExcludeFromDefaultValues
public class PatronPublicationsNames extends DomainObject implements Serializable, Comparable {

	public static final String PROPERTYNAME_SORT_NAME = "sortName";

	@IncludeInApplicationConfiguration
	private Long patronPublicationsNamesId = null;

	@IncludeInApplicationConfiguration(1)
	private Names name;

	private PatronPublications patronPublication;

	/**
	 * No-arg constructor for JavaBean tools.
	 */
	public PatronPublicationsNames() {}

	public String toString() {
		return getSortName();
	}

	/**
	 * Full constructor;
	 */
	public PatronPublicationsNames(Names name, PatronPublications patronPublication) {
		this.setName(name);
		this.setPatronPublication(patronPublication);
	}

	// ********************** Accessor Methods ********************** //

	public Long getPatronPublicationsNamesId() {
		return patronPublicationsNamesId;
	}

	public void setPatronPublicationsNamesId(Long patronPublicationsNamesId) {
		this.patronPublicationsNamesId = patronPublicationsNamesId;
	}


	public Long getIdentifier() {
		return this.getPatronPublicationsNamesId();
	}

	public void setIdentifier(Long identifier) {
		this.setPatronPublicationsNamesId(identifier);
	}

	public String getSortName() {
		return name.getSortName();
	}

	public Names getName() {
		return name;
	}

	public void setName(Names name) {
		this.name = name;
	}

	public PatronPublications getPatronPublication() {
		return patronPublication;
	}

	public void setPatronPublication(PatronPublications patronPublication) {
		this.patronPublication = patronPublication;
	}
}