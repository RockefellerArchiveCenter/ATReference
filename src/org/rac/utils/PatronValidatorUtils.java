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
 * Date: Mar 12, 2011
 * Time: 11:18:47 AM
 */

package org.rac.utils;

import org.archiviststoolkit.model.validators.ValidatorFactory;
import org.rac.model.*;
import org.rac.model.validators.*;

public class PatronValidatorUtils {

	public static void loadPatronValidators() {
		ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
		//load the custom patron validator
		validatorFactory.addValidator(Patrons.class, new PatronsValidator());
		validatorFactory.addValidator(PatronFunding.class, new PatronFundingValidator());
		validatorFactory.addValidator(PatronPhoneNumbers.class, new PatronPhoneNumbersValidator());
		validatorFactory.addValidator(PatronVisits.class, new PatronVisitsValidator());
		validatorFactory.addValidator(PatronPublications.class, new PatronPublicationsValidator());
		validatorFactory.addValidator(PatronAddresses.class, new PatronAddressesValidator());
		validatorFactory.addValidator(PatronForms.class, new PatronFormsValidator());
		validatorFactory.addValidator(Services.class, new ServicesValidator());
		validatorFactory.addValidator(PatronVisitsResearchPurposes.class, new PatronVisitsResearchPurposesValidator());
		
	}
}
