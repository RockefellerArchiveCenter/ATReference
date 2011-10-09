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
import org.archiviststoolkit.model.validators.ATAbstractValidator;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import org.rac.model.PatronForms;
import org.rac.model.PatronVisits;

public class PatronFormsValidator extends ATAbstractValidator {
	public PatronFormsValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		PatronForms modelToValidate = (PatronForms)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Patron Forms");

		//Funding Type is manditory
		if (ValidationUtils.isBlank(modelToValidate.getFormType()))
			support.addError("Form Type", "is mandatory");


		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}