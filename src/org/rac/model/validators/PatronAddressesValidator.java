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
import org.archiviststoolkit.structure.ATFieldInfo;
import org.rac.model.PatronAddresses;
import org.archiviststoolkit.model.validators.ATAbstractValidator;
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class PatronAddressesValidator extends ATAbstractValidator {
	public PatronAddressesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		PatronAddresses modelToValidate = (PatronAddresses)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "PatronAddresses");

		//address type manditory
		if (ValidationUtils.isBlank(modelToValidate.getAddressType()))
			support.addError("Address type", "is mandatory");

		//address line 1 is mandatory
		if (ValidationUtils.isBlank(modelToValidate.getAddress1()))
			support.addError("Address line 1", "is mandatory");

		//city is mandatory
		if (ValidationUtils.isBlank(modelToValidate.getCity()))
			support.addError(ATFieldInfo.getFieldInfo(PatronAddresses.class, PatronAddresses.PROPERTYNAME_CITY).getFieldLabel(),
					"is mandatory");

		//mail code is mandatory
		if (ValidationUtils.isBlank(modelToValidate.getMailCode()))
			support.addError(ATFieldInfo.getFieldInfo(PatronAddresses.class, PatronAddresses.PROPERTYNAME_MAIL_CODE).getFieldLabel(),
					"is mandatory");

		//country is mandatory
		if (ValidationUtils.isBlank(modelToValidate.getCountry()))
			support.addError("Country", "is mandatory");



		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}