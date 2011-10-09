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

//==============================================================================
// Import Declarations
//==============================================================================

import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.DefaultIncludeInSearchEditor;
import org.archiviststoolkit.structure.ExcludeFromDefaultValues;
import org.archiviststoolkit.structure.IncludeInApplicationConfiguration;
import org.archiviststoolkit.structure.StringLengthValidationRequried;
import org.archiviststoolkit.util.StringHelper;

import java.util.*;

/**
 * The Names class represents a name authority record.
 */

public class Patrons extends DomainObject {

	public static final String PROPERTYNAME_PATRON_TYPE = "patronType";

	public static final String PROPERTYNAME_PRIMARY_NAME = "primaryName";
	public static final String PROPERTYNAME_REST_OF_NAME = "restOfName";
	public static final String PROPERTYNAME_PREFIX = "prefix";
	public static final String PROPERTYNAME_SUFFIX = "suffix";
	public static final String PROPERTYNAME_DIRECT_ORDER = "directOrder";
	public static final String PROPERTYNAME_INSTITUTIONAL_AFFILIATION = "institutionalAffiliation";
	public static final String PROPERTYNAME_TITLE = "title";
	public static final String PROPERTYNAME_DEPARTMENT = "department";
	public static final String PROPERTYNAME_DISPLAY_LAST_VISIT = "displayLastVisit";

	public static final String PROPERTYNAME_SORT_NAME = "sortName";
	public static final String PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY = "createSortNameAutomatically";
	public static final String PROPERTYNAME_NAME_MD5_HASH = "md5Hash";

	public static final String PROPERTYNAME_PATRON_NOTES = "patronNotes";
	public static final String PROPERTYNAME_HOW_DID_YOU_HEAR_ABOUT_US = "howDidYouHearAboutUs";
	public static final String PROPERTYNAME_EMAIL1 = "email1";
	public static final String PROPERTYNAME_EMAIL2 = "email2";

    public static final String PROPERTYNAME_INACTIVE = "inactive";

	public static final String PROPERTYNAME_PATRON_VISITS = "patronVisits";
	public static final String PROPERTYNAME_PATRON_FUNDING = "patronFunding";
	public static final String PROPERTYNAME_PATRON_PUBLICATIONS = "patronPublications";
	public static final String PROPERTYNAME_PATRON_ADDRESSES = "patronAddresses";
	public static final String PROPERTYNAME_COMPLETED_FORMS = "patronForms";

	//a suite of user defined fields
	public static final String PROPERTYNAME_USER_DEFINED_DATE1 = "userDefinedDate1";
	public static final String PROPERTYNAME_USER_DEFINED_DATE2 = "userDefinedDate2";
	public static final String PROPERTYNAME_USER_DEFINED_BOOLEAN1 = "userDefinedBoolean1";
	public static final String PROPERTYNAME_USER_DEFINED_BOOLEAN2 = "userDefinedBoolean2";
	public static final String PROPERTYNAME_USER_DEFINED_INTEGER1 = "userDefinedInteger1";
	public static final String PROPERTYNAME_USER_DEFINED_INTEGER2 = "userDefinedInteger2";
	public static final String PROPERTYNAME_USER_DEFINED_REAL1 = "userDefinedReal1";
	public static final String PROPERTYNAME_USER_DEFINED_REAL2 = "userDefinedReal2";
	public static final String PROPERTYNAME_USER_DEFINED_STRING1 = "userDefinedString1";
	public static final String PROPERTYNAME_USER_DEFINED_STRING2 = "userDefinedString2";
	public static final String PROPERTYNAME_USER_DEFINED_STRING3 = "userDefinedString3";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT1 = "userDefinedText1";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT2 = "userDefinedText2";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT3 = "userDefinedText3";
	public static final String PROPERTYNAME_USER_DEFINED_TEXT4 = "userDefinedText4";

    // for linking names. Always as a subject
	public static final String NAME_LINK_FUNCTION_SUBJECT = "Subject";


	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Long patronId;

	@IncludeInApplicationConfiguration(2)
	@StringLengthValidationRequried
    @DefaultIncludeInSearchEditor
	private String patronType = "";

	//added fields for user registration

	/**
	 * primary name (surname etc).
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
    @DefaultIncludeInSearchEditor
	private String primaryName = "";
	/**
	 * rest of name.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
    @DefaultIncludeInSearchEditor
	private String restOfName = "";
	/**
	 * prefix.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String prefix = "";
	/**
	 * suffix.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String suffix = "";
	/**
	 * dates.
	 */
	/**
	 * direct order.
	 */
	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean directOrder = false;
	/**
	 * Title.
	 */

	@IncludeInApplicationConfiguration(1)
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
	private String sortName = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean createSortNameAutomatically = true;

	@IncludeInApplicationConfiguration(3)
	@ExcludeFromDefaultValues
	@StringLengthValidationRequried
    @DefaultIncludeInSearchEditor
	private String institutionalAffiliation = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String title = "";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String department = "";

	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
    @DefaultIncludeInSearchEditor
	private String patronNotes = "";

	@IncludeInApplicationConfiguration(4)
	private String email1 = "";

	@IncludeInApplicationConfiguration
	private String email2 = "";

    @IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Boolean inactive;

	@IncludeInApplicationConfiguration
    @DefaultIncludeInSearchEditor
	private String howDidYouHearAboutUs = "";

	//a suite of user defined fields

	@IncludeInApplicationConfiguration
	private Date userDefinedDate1;

	@IncludeInApplicationConfiguration
	private Date userDefinedDate2;

	@IncludeInApplicationConfiguration
	private Boolean userDefinedBoolean1;

	@IncludeInApplicationConfiguration
	private Boolean userDefinedBoolean2;

	@IncludeInApplicationConfiguration
	private Integer userDefinedInteger1;

	@IncludeInApplicationConfiguration
	private Integer userDefinedInteger2;

	@IncludeInApplicationConfiguration
	private Double userDefinedReal1;

	@IncludeInApplicationConfiguration
	private Double userDefinedReal2;

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String userDefinedString1 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String userDefinedString2 ="";

	@IncludeInApplicationConfiguration
	@StringLengthValidationRequried
	private String userDefinedString3 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText1 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText2 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText3 ="";

	@IncludeInApplicationConfiguration
	private String userDefinedText4 ="";

	@IncludeInApplicationConfiguration
	@DefaultIncludeInSearchEditor
	@ExcludeFromDefaultValues
	private Repositories repository;


	@IncludeInApplicationConfiguration
	@ExcludeFromDefaultValues
	private Date displayLastVisit;
	/**
	 * md5 hash used to index the bean in the database
	 */
	@ExcludeFromDefaultValues
	private String md5Hash = "";

	private Set<PatronAddresses> patronAddresses = new HashSet<PatronAddresses>();
	private Set<PatronVisits> patronVisits = new HashSet<PatronVisits>();
	private Set<PatronPublications> patronPublications = new HashSet<PatronPublications>();
	private Set<PatronFunding> patronFunding = new HashSet<PatronFunding>();
	private Set<PatronPhoneNumbers> patronPhoneNumbers = new HashSet<PatronPhoneNumbers>();
	private Set<PatronForms> patronForms = new HashSet<PatronForms>();

	public Patrons() {
	}

	public Long getIdentifier() {
		return this.getPatronId();
	}

	public void setIdentifier(Long identifier) {
		this.setPatronId(identifier);
	}

	public String toString() {
		return sortName;
	}

	public Long getPatronId() {
		return patronId;
	}

	public void setPatronId(Long patronId) {
		this.patronId = patronId;
	}


	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	public void removeRelatedObject(DomainObject domainObject) throws ObjectNotRemovedException {
		if (domainObject instanceof PatronAddresses) {
			removePatronAddresses((PatronAddresses) domainObject);
		} else if (domainObject instanceof PatronVisits) {
			removePatronVisit((PatronVisits) domainObject);
		} else if (domainObject instanceof PatronPublications) {
			removePatronPublication((PatronPublications) domainObject);
		} else if (domainObject instanceof PatronFunding) {
			removePatronFunding((PatronFunding) domainObject);
		} else if (domainObject instanceof PatronPhoneNumbers) {
			removePatronPhoneNumbers((PatronPhoneNumbers) domainObject);
		} else if (domainObject instanceof PatronForms) {
			removePatronForms((PatronForms) domainObject);
		} else {
			super.removeRelatedObject(domainObject);
		}
	}

	/**
	 * A dummy place holder to be overridden by classes that have related objects
	 *
	 * @param domainObject the domain object to be removed
	 */
	@Override
	public void addRelatedObject(DomainObject domainObject) throws AddRelatedObjectException, DuplicateLinkException {
		if (domainObject instanceof PatronAddresses) {
			addPatonsAddresses((PatronAddresses) domainObject);
		} else if (domainObject instanceof PatronVisits){
			addPatronVisit((PatronVisits) domainObject);
		} else if (domainObject instanceof PatronPublications){
			addPatronPublication((PatronPublications) domainObject);
		} else if (domainObject instanceof PatronFunding){
			addPatronFunding((PatronFunding) domainObject);
		} else if (domainObject instanceof PatronPhoneNumbers){
			addPatronPhoneNumbers((PatronPhoneNumbers) domainObject);
		} else if (domainObject instanceof PatronForms){
			addPatronForms((PatronForms) domainObject);
		} else {
			super.addRelatedObject(domainObject);
		}
	}


	public void createSortName() {
		//only do the automatic assignment of sort name for new records

		if (this.createSortNameAutomatically) {

				String primaryName = this.getDirectOrder() ?
						StringHelper.concat(" ",
								this.getRestOfName(),
								this.getPrimaryName()) :
						StringHelper.concat(", ",
								this.getPrimaryName(),
								this.getRestOfName());

				this.setSortName(StringHelper.concat(", ",
						primaryName,
						this.getPrefix(),
						this.getSuffix()));
		}

	}

	// Method to set the md5 hash that uniquely identifies this record
	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}

	// Method to return the md5 hash of this basic name object
	public String getMd5Hash() {
		return this.md5Hash;
	}

	public String getPatronType() {
		return patronType;
	}

	public void setPatronType(String patronType) {
		this.patronType = patronType;
	}

	public String getInstitutionalAffiliation() {
		return institutionalAffiliation;
	}

	public void setInstitutionalAffiliation(String institutionalAffiliation) {
		this.institutionalAffiliation = institutionalAffiliation;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPatronNotes() {
		return patronNotes;
	}

	public void setPatronNotes(String patronNotes) {
		this.patronNotes = patronNotes;
	}

	public Boolean getUserDefinedBoolean1() {
		return userDefinedBoolean1;
	}

	public void setUserDefinedBoolean1(Boolean userDefinedBoolean1) {
		this.userDefinedBoolean1 = userDefinedBoolean1;
	}

	public Boolean getUserDefinedBoolean2() {
		return userDefinedBoolean2;
	}

	public void setUserDefinedBoolean2(Boolean userDefinedBoolean2) {
		this.userDefinedBoolean2 = userDefinedBoolean2;
	}

	public Integer getUserDefinedInteger1() {
		return userDefinedInteger1;
	}

	public void setUserDefinedInteger1(Integer userDefinedInteger1) {
		this.userDefinedInteger1 = userDefinedInteger1;
	}

	public Integer getUserDefinedInteger2() {
		return userDefinedInteger2;
	}

	public void setUserDefinedInteger2(Integer userDefinedInteger2) {
		this.userDefinedInteger2 = userDefinedInteger2;
	}

	public Double getUserDefinedReal1() {
		return userDefinedReal1;
	}

	public void setUserDefinedReal1(Double userDefinedReal1) {
		this.userDefinedReal1 = userDefinedReal1;
	}

	public Double getUserDefinedReal2() {
		return userDefinedReal2;
	}

	public void setUserDefinedReal2(Double userDefinedReal2) {
		this.userDefinedReal2 = userDefinedReal2;
	}

	public Date getUserDefinedDate1() {
		return userDefinedDate1;
	}

	public void setUserDefinedDate1(Date userDefinedDate1) {
		this.userDefinedDate1 = userDefinedDate1;
	}

	public Date getUserDefinedDate2() {
		return userDefinedDate2;
	}

	public void setUserDefinedDate2(Date userDefinedDate2) {
		this.userDefinedDate2 = userDefinedDate2;
	}

	public String getUserDefinedString1() {
		if (this.userDefinedString1 != null) {
			return this.userDefinedString1;
		} else {
			return "";
		}
	}

	public void setUserDefinedString1(String userDefinedString1) {
		this.userDefinedString1 = userDefinedString1;
	}

	public String getUserDefinedString2() {
		if (this.userDefinedString2 != null) {
			return this.userDefinedString2;
		} else {
			return "";
		}
	}

	public void setUserDefinedString2(String userDefinedString2) {
		this.userDefinedString2 = userDefinedString2;
	}

	public String getUserDefinedString3() {
		if (this.userDefinedString3 != null) {
			return this.userDefinedString3;
		} else {
			return "";
		}
	}

	public void setUserDefinedString3(String userDefinedString3) {
		this.userDefinedString3 = userDefinedString3;
	}

	public String getUserDefinedText1() {
		if (this.userDefinedText1 != null) {
			return this.userDefinedText1;
		} else {
			return "";
		}
	}

	public void setUserDefinedText1(String userDefinedText1) {
		this.userDefinedText1 = userDefinedText1;
	}

	public String getUserDefinedText2() {
		if (this.userDefinedText2 != null) {
			return this.userDefinedText2;
		} else {
			return "";
		}
	}

	public void setUserDefinedText2(String userDefinedText2) {
		this.userDefinedText2 = userDefinedText2;
	}

	public String getUserDefinedText3() {
		if (this.userDefinedText3 != null) {
			return this.userDefinedText3;
		} else {
			return "";
		}
	}

	public void setUserDefinedText3(String userDefinedText3) {
		this.userDefinedText3 = userDefinedText3;
	}

	public String getUserDefinedText4() {
		if (this.userDefinedText4 != null) {
			return this.userDefinedText4;
		} else {
			return "";
		}
	}

	public void setUserDefinedText4(String userDefinedText4) {
		this.userDefinedText4 = userDefinedText4;
	}


	public Set<PatronAddresses> getPatronAddresses() {
		return patronAddresses;
	}

	public void setPatronAddresses(Set<PatronAddresses> patronAddresses) {
		this.patronAddresses = patronAddresses;
	}

	public void addPatonsAddresses(PatronAddresses patronAddress) {
		if (patronAddress == null)
			throw new IllegalArgumentException("Can't add a null address.");
		this.getPatronAddresses().add(patronAddress);

	}

	private void removePatronAddresses(PatronAddresses patronAddress) {
		if (patronAddress == null)
			throw new IllegalArgumentException("Can't remove a null address.");

		getPatronAddresses().remove(patronAddress);
	}

	//patron visits
	public Set<PatronVisits> getPatronVisits() {
		return patronVisits;
	}

	public void setPatronVisits(Set<PatronVisits> patronVisits) {
		this.patronVisits = patronVisits;
	}

	public void addPatronVisit(PatronVisits patronVisits) {
		if (patronVisits == null)
			throw new IllegalArgumentException("Can't add a null patron visit.");
		this.getPatronVisits().add(patronVisits);

	}

	private void removePatronVisit(PatronVisits patronVisits) {
		if (patronVisits == null)
			throw new IllegalArgumentException("Can't remove a null patron visit.");

		getPatronVisits().remove(patronVisits);
	}

	public Date getDateOfLastVisit () {
		Date returnDate = null;
		for (PatronVisits visit: getPatronVisits()) {
			if (returnDate == null) {
				returnDate = visit.getVisitDate();
			} else if (visit.getVisitDate().after(returnDate)) {
				returnDate = visit.getVisitDate();
			}
		}
		return returnDate;
	}

	//patron publications
	public Set<PatronPublications> getPatronPublications() {
		return patronPublications;
	}

	public void setPatronPublications(Set<PatronPublications> patronPublications) {
		this.patronPublications = patronPublications;
	}

	public void addPatronPublication(PatronPublications patronPublications) {
		if (patronVisits == null)
			throw new IllegalArgumentException("Can't add a null patron publication.");
		this.getPatronPublications().add(patronPublications);

	}

	private void removePatronPublication(PatronPublications patronPublications) {
		if (patronPublications == null)
			throw new IllegalArgumentException("Can't remove a null patron publication.");

		getPatronPublications().remove(patronPublications);
	}

	//patron funding
	public Set<PatronFunding> getPatronFunding() {
		return patronFunding;
	}

	public void setPatronFunding(Set<PatronFunding> patronFunding) {
		this.patronFunding = patronFunding;
	}


	public void addPatronFunding(PatronFunding patronFunding) {
		if (patronFunding == null)
			throw new IllegalArgumentException("Can't add a null funding record.");
		this.getPatronFunding().add(patronFunding);

	}

	private void removePatronFunding(PatronFunding patronFunding) {
		if (patronFunding == null)
			throw new IllegalArgumentException("Can't remove a null funding record.");

		getPatronFunding().remove(patronFunding);
	}

	//patron phone numbers
	public Set<PatronPhoneNumbers> getPatronPhoneNumbers() {
		return patronPhoneNumbers;
	}

	public void setPatronPhoneNumbers(Set<PatronPhoneNumbers> patronPhoneNumbers) {
		this.patronPhoneNumbers = patronPhoneNumbers;
	}


	public void addPatronPhoneNumbers(PatronPhoneNumbers patronPhoneNumber) {
		if (patronPhoneNumber == null)
			throw new IllegalArgumentException("Can't add a null patron publication.");
		this.getPatronPhoneNumbers().add(patronPhoneNumber);

	}

	private void removePatronPhoneNumbers(PatronPhoneNumbers patronPhoneNumber) {
		if (patronPhoneNumber == null)
			throw new IllegalArgumentException("Can't remove a null patron publication.");

		getPatronPhoneNumbers().remove(patronPhoneNumber);
	}

	public String getHowDidYouHearAboutUs() {
		return howDidYouHearAboutUs;
	}

	public void setHowDidYouHearAboutUs(String howDidYouHearAboutUs) {
		this.howDidYouHearAboutUs = howDidYouHearAboutUs;
	}

	public Set<PatronForms> getPatronForms() {
		return patronForms;
	}

	public void setPatronForms(Set<PatronForms> patronForms) {
		this.patronForms = patronForms;
	}

	public void addPatronForms(PatronForms patronForms) {
		if (patronForms == null)
			throw new IllegalArgumentException("Can't add a null completed form.");
		this.getPatronForms().add(patronForms);

	}

	private void removePatronForms(PatronForms patronForm) {
		if (patronForm == null)
			throw new IllegalArgumentException("Can't remove a null completed form.");

		getPatronForms().remove(patronForm);
	}

	public String getPrimaryName() {
		return primaryName;
	}

	public void setPrimaryName(String primaryName) {
		this.primaryName = primaryName;
	}

	public String getRestOfName() {
		return restOfName;
	}

	public void setRestOfName(String restOfName) {
		this.restOfName = restOfName;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public Boolean getDirectOrder() {
		return directOrder;
	}

	public void setDirectOrder(Boolean directOrder) {
		this.directOrder = directOrder;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		Object oldValue = getSortName();
		this.sortName = sortName;
	    firePropertyChange(PROPERTYNAME_SORT_NAME, oldValue, this.sortName);
	}

	public Boolean getCreateSortNameAutomatically() {
		return createSortNameAutomatically;
	}

	public void setCreateSortNameAutomatically(Boolean createSortNameAutomatically) {
		this.createSortNameAutomatically = createSortNameAutomatically;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Repositories getRepository() {
		return repository;
	}

	public void setRepository(Repositories repository) {
		this.repository = repository;
	}

	public Date getDisplayLastVisit() {
		return displayLastVisit;
	}

	public void setDisplayLastVisit(Date displayLastVisit) {
		this.displayLastVisit = displayLastVisit;
	}
}