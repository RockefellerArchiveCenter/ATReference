package org.rac.model;

import org.archiviststoolkit.model.Assessments;
import org.archiviststoolkit.model.Resources;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.IncludeMethodInApplicationConfiguration;

import java.io.Serializable;

/**
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

  * Class that links Patrons Visits to Resources
 *
 * Created by IntelliJ IDEA.
 * User: nathan
 * Date: Aug 30, 2011
 * Time: 7:36:44 PM
 */
@ExcludeFromDefaultValues
public class PatronVisitsResources extends DomainObject implements Serializable, Comparable {
    public static final String PROPERTYNAME_PATRON_VISIT_IDENTIFIER = "patronVisitIdentifier";
	public static final String PROPERTYNAME_RESOURCE_IDENTIFIER = "resourceIdentifier";
	public static final String PROPERTYNAME_RESOURCE_TITLE = "resourceTitle";
    public static final String PROPERTYNAME_RESOURCE = "resource";

	private Long PatronVisitsResourcesId = null;

	@IncludeInApplicationConfiguration
	private Resources resource;

	@IncludeInApplicationConfiguration
	private PatronVisits patronVisit;

    /**
     The constructor needed for serializing
      */
    public PatronVisitsResources() {}

    /**
     * The main constructor
     *
     * @param resource
     * @param patronVisit
     */
    public PatronVisitsResources(Resources resource, PatronVisits patronVisit) {
		this.setResource(resource);
		this.setPatronVisit(patronVisit);
	}

    public Long getPatronVisitsResourcesId() {
        return PatronVisitsResourcesId;
    }

    public Long getIdentifier() {
        return getPatronVisitsResourcesId();
    }

    public void setPatronVisitsResourcesId(Long patronVisitsResourcesId) {
        PatronVisitsResourcesId = patronVisitsResourcesId;
    }

    public void setIdentifier(Long identifier) {
        setPatronVisitsResourcesId(identifier);
    }

    public Resources getResource() {
        return resource;
    }

    public void setResource(Resources resource) {
        this.resource = resource;
    }

    public PatronVisits getPatronVisit() {
        return patronVisit;
    }

    public void setPatronVisit(PatronVisits patronVisit) {
        this.patronVisit = patronVisit;
    }

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_IDENTIFIER, value = 1)
	public String getResourceIdentifier() {
		if (this.getResource() != null) {
			return this.getResource().getResourceIdentifier();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_RESOURCE_TITLE, value = 2)
	public String getResourceTitle() {
		if (this.getResource() != null) {
			return this.getResource().getTitle();
		} else {
			return "";
		}
	}

    @IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_PATRON_VISIT_IDENTIFIER)
	public Long getPatronVisitsIdentifier() {
		if (this.getPatronVisit() != null) {
			return this.getPatronVisit().getIdentifier();
		} else {
			return null;
		}
	}
}
