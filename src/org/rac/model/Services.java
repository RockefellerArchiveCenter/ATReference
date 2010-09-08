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

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.StringLengthValidationRequried;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Services extends DomainObject {

    // Names of the Bound Bean Properties *************************************

    public static final String PROPERTYNAME_CATEGORY    = "category";
    public static final String PROPERTYNAME_DESCRIPTION  = "description";
	public static final String PROPERTYNAME_UNITS    = "units";
	public static final String PROPERTYNAME_COST    = "cost";
	public static final String PROPERTYNAME_MIN_QUANTITY_PER_CALENDAR_YEAR    = "minQuantityPerCalendarYear";
	public static final String PROPERTYNAME_MAX_QUANTITY_PER_CALENDAR_YEAR    = "maxQuantityPerCalendarYear";
	public static final String PROPERTYNAME_UNLIMITED_QUANTITY_PER_CALENDAR_YEAR    = "unlimitedQuantityPerCalendarYear";

    @IncludeInApplicationConfiguration
    @ExcludeFromDefaultValues
    private Long servicesId;

    @IncludeInApplicationConfiguration(1)
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
    private String category = "";

    @IncludeInApplicationConfiguration(2)
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
    private String description = "";

    @IncludeInApplicationConfiguration(3)
    @ExcludeFromDefaultValues
	@StringLengthValidationRequried
    private String units = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer minQuantityPerCalendarYear;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Integer maxQuantityPerCalendarYear;

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean unlimitedQuantityPerCalendarYear;

	@IncludeInApplicationConfiguration(4)
	@ExcludeFromDefaultValues
    private Double cost;

    public Long getServicesId() {
        return servicesId;
    }

    public void setServicesId(Long servicesId) {
        this.servicesId = servicesId;
    }

    public String getCategory() {
		if (this.category != null) {
			return this.category;
		} else {
			return "";
		}
	}

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
		if (this.description != null) {
			return this.description;
		} else {
			return "";
		}
	}

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnits() {
		if (this.units != null) {
			return this.units;
		} else {
			return "";
		}
	}

    public void setUnits(String units) {
        this.units = units;
    }

    public Long getIdentifier() {
        return this.getServicesId();
    }

    public void setIdentifier(Long identifier) {
        this.setServicesId(identifier);
    }

	public Integer getMinQuantityPerCalendarYear() {
		return minQuantityPerCalendarYear;
	}

	public void setMinQuantityPerCalendarYear(Integer minQuantityPerCalendarYear) {
		this.minQuantityPerCalendarYear = minQuantityPerCalendarYear;
	}

	public Integer getMaxQuantityPerCalendarYear() {
		return maxQuantityPerCalendarYear;
	}

	public void setMaxQuantityPerCalendarYear(Integer maxQuantityPerCalendarYear) {
		this.maxQuantityPerCalendarYear = maxQuantityPerCalendarYear;
	}

	public Boolean getUnlimitedQuantityPerCalendarYear() {
		return unlimitedQuantityPerCalendarYear;
	}

	public void setUnlimitedQuantityPerCalendarYear(Boolean unlimitedQuantityPerCalendarYear) {
		this.unlimitedQuantityPerCalendarYear = unlimitedQuantityPerCalendarYear;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
}