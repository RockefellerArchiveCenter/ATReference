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
import org.archiviststoolkit.util.StringHelper;
import org.rac.model.Patrons;

public class PatronAddresses extends DomainObject {

	public static final String PROPERTYNAME_ADDRESS_TYPE = "addressType";
	public static final String PROPERTYNAME_ADDRESS1 = "address1";
	public static final String PROPERTYNAME_ADDRESS2 = "address2";
	public static final String PROPERTYNAME_ADDRESS3 = "address3";
	public static final String PROPERTYNAME_CITY = "city";
	public static final String PROPERTYNAME_REGION = "region";
	public static final String PROPERTYNAME_MAIL_CODE = "mailCode";
	public static final String PROPERTYNAME_COUNTRY = "country";
	public static final String PROPERTYNAME_NAME = "contactName";
	public static final String PROPERTYNAME_ADDRESS_STRING = "addressString";
	public static final String PREFERRED_ADDRESS = "preferredAddress";


	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long addressId;

	@IncludeInApplicationConfiguration (1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String addressType = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String address1 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String address2 = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String address3 = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String city = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String region = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(150)
	private String mailCode = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried(50)
	private String country = "";

	@IncludeInApplicationConfiguration(3)
	@ExcludeFromDefaultValues
	private Boolean preferredAddress = false;


	private Patrons patron;

	/**
	 * Creates a new instance of Subject
	 */
	public PatronAddresses() {
	}

	public PatronAddresses(String addressType) {
		this.addressType = addressType;
	}

	public PatronAddresses(Patrons patron) {
		this.patron = patron;
	}

	/**
	 * @return Returns the identifier.
	 */
	public Long getIdentifier() {
		return getAddressId();
	}

	public String toString() {
		return this.addressType;
	}

	/**
	 * @param identifier The identifier to set.
	 */
	public void setIdentifier(Long identifier) {
		this.setAddressId(identifier);
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getAddressType() {
		if (this.addressType != null) {
			return this.addressType;
		} else {
			return "";
		}
	}

	@IncludeMethodInApplicationConfiguration(fieldName = PROPERTYNAME_ADDRESS_STRING, value=2)
	@ExcludeFromDefaultValues
	public String getAddressString() {
		return StringHelper.concat(", ", this.getAddress1(), this.getAddress2(), this.getCity(), this.getMailCode(), this.getCountry());
	}

	public void setAddressType(String addressType) {
//		Object oldValue = getFundingDate();
		this.addressType = addressType;
//		firePropertyChange(PROPERTYNAME_SUBJECT_TERM, oldValue, addressType);

	}

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMailCode() {
        return mailCode;
    }

    public void setMailCode(String mailCode) {
        this.mailCode = mailCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

	public Boolean getPreferredAddress() {
		return preferredAddress;
	}

	public void setPreferredAddress(Boolean preferredAddress) {
		this.preferredAddress = preferredAddress;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public Patrons getPatron() {
		return patron;
	}

	public void setPatron(Patrons patron) {
		this.patron = patron;
	}
}