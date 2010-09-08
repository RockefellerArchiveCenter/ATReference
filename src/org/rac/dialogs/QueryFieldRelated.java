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
 * Date: May 4, 2010
 * Time: 2:24:54 PM
 */

package org.rac.dialogs;

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.QueryEditorPanel;
import org.archiviststoolkit.dialog.QueryField;
import org.archiviststoolkit.model.LookupListItems;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.util.ATBeanUtils;
import org.archiviststoolkit.util.LookupListUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.IntrospectionException;
import java.util.Date;
import java.util.Vector;

public class QueryFieldRelated extends QueryField {

	public QueryFieldRelated(DatabaseFields field, Class clazz) {
		this.fieldName = field.getFieldName();
		this.displayFieldName = field.getFieldLabel();
		this.tableName = clazz.getName();
		//first intercept accession number and resource id for special treatment
			try {
				Class fieldType = ATBeanUtils.getPropertyType(clazz, fieldName);
				if (fieldType == String.class) {
					ATFieldInfo fieldInfo = ATFieldInfo.getFieldInfo(this.tableName + "." + this.fieldName);
					Vector<LookupListItems> values = LookupListUtils.getLookupListValues2(fieldInfo.getLookupList());
					if (values.size() == 0) {
						setValueComponent(new QueryEditorRelatedTextPanel());
					} else {
//						JComboBox comboBox = new JComboBox(new DefaultComboBoxModel(values));
//						comboBox.setOpaque(false);
//                        comboBox.setMaximumSize(new Dimension(110, 32767)); // set the size so that it shows up properly in subject search
//                        comboBox.setMinimumSize(new Dimension(110, 27));
//                        setValueComponent(new QueryEditorRelatedTextPanel(comboBox));
					}
                } else if (fieldType == Date.class) {
                    setValueComponent(new QueryEditorRelatedDatePanel());
//                } else if (fieldType == Integer.class) {
//                    setValueComponent(new QueryEditorRelatedIntegerPanel(QueryEditorPanel.RETURN_INTEGER_VALUES));
//                } else if (fieldType == Long.class) {
//                    setValueComponent(new QueryEditorRelatedIntegerPanel(QueryEditorPanel.RETURN_LONG_VALUES));
//				} else if (fieldType == Double.class) {
//					setValueComponent(new QueryEditorRelatedDoublePanel());
//				} else if (fieldType == Boolean.class) {
//					setValueComponent(new QueryEditorRelatedBooleanPanel());
				}
			} catch (IntrospectionException e) {
				new ErrorDialog("", e).showDialog();
			}
	}


}
