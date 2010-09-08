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
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.model.validators.ATAbstractValidator;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.ATPropertyValidationSupport;
import org.rac.model.Services;

public class ServicesValidator extends ATAbstractValidator {
	public ServicesValidator() {
	}

	// Validation *************************************************************

	/**
	 * Validates this Validator's Order and returns the result
	 * as an instance of {@link com.jgoodies.validation.ValidationResult}.
	 *
	 * @return the ValidationResult of the objectToValidate validation
	 */
	public ValidationResult validate() {

		Services modelToValidate = (Services)objectToValidate;
		ATPropertyValidationSupport support =
				new ATPropertyValidationSupport(modelToValidate, "Services");

		String primaryNameFieldlabel = ATFieldInfo.getFieldInfo(Names.class, Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME).getFieldLabel();
		String restOfNameFieldLabel = ATFieldInfo.getFieldInfo(Names.class, Names.PROPERTYNAME_PERSONAL_REST_OF_NAME).getFieldLabel();
		//Category is manditory
		if (ValidationUtils.isBlank(modelToValidate.getCategory()))
			support.addError("Category", "is mandatory");

		//Sort Name is manditory
		if (ValidationUtils.isBlank(modelToValidate.getDescription()))
			support.addError("Description", "is mandatory");

		if (ValidationUtils.isBlank(modelToValidate.getUnits())) {
			support.addError("Units", "is mandatory");
		}

		if (!modelToValidate.getUnlimitedQuantityPerCalendarYear()) {
			if (modelToValidate.getMinQuantityPerCalendarYear() == 0) {
				support.addError("Min Qty/Year", "can't be 0");
			}

			if (modelToValidate.getMaxQuantityPerCalendarYear() == 0) {
				support.addError("Max Qty/Year", "can't be 0");
			}

			if (modelToValidate.getMaxQuantityPerCalendarYear() < modelToValidate.getMinQuantityPerCalendarYear()) {
				support.addError("Max Qty/Year", "can't be less than Min Qty/Year");
			}
		}

		checkForStringLengths(modelToValidate, support);

		return support.getResult();
	}

}