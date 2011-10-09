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
import org.rac.model.PatronPhoneNumbers;
import org.archiviststoolkit.model.validators.ATAbstractValidator;
import org.archiviststoolkit.util.ATPropertyValidationSupport;

public class PatronPhoneNumbersValidator extends ATAbstractValidator {
	public PatronPhoneNumbersValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		PatronPhoneNumbers modelToValidate = (PatronPhoneNumbers)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Phone numbers");

		//phone number is manditory
		if (ValidationUtils.isBlank(modelToValidate.getPhoneNumber()))
			support.addError("Phone number", "is mandatory");

		//phone number type is mandatory
		if (ValidationUtils.isBlank(modelToValidate.getPhoneNumberType()))
			support.addError("Phone number type", "is mandatory");



		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}