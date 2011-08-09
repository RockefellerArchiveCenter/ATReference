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
import org.rac.model.PatronVisitsResearchPurposes;

public class PatronVisitsResearchPurposesValidator extends ATAbstractValidator {
	public PatronVisitsResearchPurposesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		PatronVisitsResearchPurposes modelToValidate = (PatronVisitsResearchPurposes)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Patron Visits Research Purposes");

		//Funding Type is manditory
		if (ValidationUtils.isBlank(modelToValidate.getResearchPurpose()))
			support.addError("Research Purpose", "is mandatory");


		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}