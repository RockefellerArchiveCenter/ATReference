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
 * Date: Mar 12, 2010
 * Time: 12:34:09 PM
 */

package org.rac.utils;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.UnknownLookupListException;
import org.archiviststoolkit.model.LookupList;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.mydomain.LookupListsDAO;
import org.archiviststoolkit.mydomain.PersistenceException;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.FieldNotFoundException;
import org.archiviststoolkit.util.LookupListUtils;

import java.util.TreeSet;

public class RACLookupListUtils extends LookupListUtils{

	public static LookupListItems addItemToListByFieldProvisional(Class clazz, String fieldName, String value) throws FieldNotFoundException, UnknownLookupListException {

		if (value == null) {
			return null;
		} else {
			ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(clazz, fieldName);
			String lookupListName = "";
			if (fieldInfo == null) {
				throw new FieldNotFoundException(clazz.getName() + "." + fieldName);
			} else {
				lookupListName = fieldInfo.getLookupList();
				if (fieldInfo.getDataType().equalsIgnoreCase("java.lang.String")) {
					if (lookupListName != null && lookupListName.length() != 0) {
						return addItemToListProvisional(lookupListName, value, true);
					} else {
						throw new UnknownLookupListException("Class: " + clazz.getSimpleName() + " Field: " + fieldName + " does not have a lookup list");
					}
				} else {
					throw new UnknownLookupListException("Class: " + clazz.getSimpleName() + " Field: " + fieldName + " does not have a lookup list");
				}
			}
		}
	}

	public static LookupListItems addItemToListProvisional(String listName, String item, boolean loadLookupLists) throws UnknownLookupListException {
		TreeSet<LookupListItems> list = getLookupListsObjects().get(listName.toLowerCase());

		if (list == null) {
			throw new UnknownLookupListException(listName);
		} else {
			LookupList lookupList = null;
			LookupListItems newItem = null;
			try {
				LookupListsDAO lookupListDAO = new LookupListsDAO();
				lookupList = lookupListDAO.findLookupListByName(listName);
				newItem = new LookupListItems(lookupList, item);
				newItem.setProvisional(true);
				lookupList.addListItem(newItem);
				lookupListDAO.update(lookupList);
				if (loadLookupLists) {
					loadLookupLists();
				}
			} catch (PersistenceException e) {
				new ErrorDialog("Error adding " + item + " to lookup list " + lookupList.getListName(), e).showDialog();
			}
			return newItem;
		}
	}

}
