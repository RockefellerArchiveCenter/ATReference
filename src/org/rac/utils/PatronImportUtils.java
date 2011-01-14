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
 * Date: Nov 27, 2010
 * Time: 11:18:32 AM
 */

package org.rac.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.importer.ImportUtils;
import org.archiviststoolkit.mydomain.DomainImportController;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.LookupListUtils;
import org.archiviststoolkit.util.StringHelper;
import org.rac.exceptions.DataTruncationException;

import java.lang.reflect.InvocationTargetException;

public class PatronImportUtils extends ImportUtils {


	public static void nullSafeSetTruncationTest(DomainObject domainObject,
								   String propertyName,
								   Object value) throws IllegalAccessException, InvocationTargetException, UnknownLookupListException, DataTruncationException {
        if (value != null) {
            //check to see if this field has a lookup list
            ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(domainObject.getClass(), propertyName);
            String lookupListName = fieldInfo.getLookupList();
            if (lookupListName != null && lookupListName.length() > 0) {
                //first trim to length
                String stringValue = StringHelper.cleanUpWhiteSpace((String) value);
                if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
                    stringValue = StringHelper.trimToLength(stringValue, fieldInfo.getStringLengthLimit());
                }
                //then check to see if it in the lookup list
                stringValue = LookupListUtils.addItemToList(lookupListName, stringValue, false);
                BeanUtils.setProperty(domainObject, propertyName, stringValue);
            } else {
                if (value instanceof String) {
                    String stringValue = StringHelper.cleanUpWhiteSpace((String) value);
                    if (fieldInfo.getStringLengthLimit() != null && fieldInfo.getStringLengthLimit() > 0) {
						String originalString = stringValue;
                        stringValue = StringHelper.trimToLength(stringValue, fieldInfo.getStringLengthLimit());
						if (originalString.length() > stringValue.length()) {
							throw new DataTruncationException();
						}
						
                    }
                    BeanUtils.setProperty(domainObject, propertyName, stringValue);
                } else {
                    BeanUtils.setProperty(domainObject, propertyName, value);
                }
            }

        }
	}

}
