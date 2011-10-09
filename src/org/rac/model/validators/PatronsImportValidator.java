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
 *
 * @author Lee Mandell
 * Date: May 16, 2006
 * Time: 11:16:26 AM
 */

package org.rac.model.validators;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.util.ValidationUtils;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.validators.ATAbstractValidator;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import org.rac.model.PatronAddresses;
import org.rac.model.PatronPhoneNumbers;
import org.rac.model.Patrons;

public class PatronsImportValidator extends ATAbstractValidator {
	public PatronsImportValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		Patrons modelToValidate = (Patrons)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Patrons");

		String primaryNameFieldlabel = ATFieldInfo.getFieldInfo(Names.class, Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME).getFieldLabel();
		String restOfNameFieldLabel = ATFieldInfo.getFieldInfo(Names.class, Names.PROPERTYNAME_PERSONAL_REST_OF_NAME).getFieldLabel();

		//Sort Name is manditory
		if (ValidationUtils.isBlank(modelToValidate.getSortName()))
			support.addError("Sort Name", "is mandatory");

		if (ValidationUtils.isBlank(modelToValidate.getPrimaryName())) {
			support.addError(primaryNameFieldlabel, "is mandatory");
		}

//		if (ValidationUtils.isBlank(modelToValidate.getRestOfName())) {
//			support.addError(restOfNameFieldLabel, "is mandatory");
//		}

//		Boolean emailAddress = false;
		Boolean phoneNumber = false;

		if (modelToValidate.getPatronPhoneNumbers().size() > 0) {
			phoneNumber = true;
		}

		//for patron import this restriction has been removed
//		if (ValidationUtils.isNotBlank(modelToValidate.getEmail1()) ||
//				ValidationUtils.isNotBlank(modelToValidate.getEmail2())) {
//			emailAddress = true;
//		}
//
//		if (!emailAddress && !phoneNumber) {
//			support.addError("At least one phone number or email address", "is mandatory");
//		}
//
		int preferredAddressCount = 0;
		for (PatronAddresses address: modelToValidate.getPatronAddresses()) {
			if (address.getPreferredAddress()) {
				preferredAddressCount++;
			}
		}
		if (preferredAddressCount > 1) {
			support.addSimpleError("There can be only one preferred address");
		}

		if (phoneNumber) {
			int preferredPhoneNumberCount = 0;
			for (PatronPhoneNumbers thisPhoneNumber: modelToValidate.getPatronPhoneNumbers()) {
				if (thisPhoneNumber.getPreferredPhoneNumber()) {
					preferredPhoneNumberCount++;
				}
			}
			if (preferredPhoneNumberCount > 1) {
				support.addSimpleError("There can be only one preferred phone number");
			}
		}

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}