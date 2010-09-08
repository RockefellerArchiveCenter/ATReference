/*
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
 * Created by JFormDesigner on Sun Jul 25 12:02:57 EDT 2010
 */

package org.rac.editors;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.rac.model.CompletedForms;

public class CompletedFormFields extends RAC_DomainEditorFields {
	public CompletedFormFields() {
		initComponents();
	}

	@Override
	public Component getInitialFocusComponent() {
		return formType;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		Label_FormType = new JLabel();
		formType = ATBasicComponentFactory.createComboBox(detailsModel, CompletedForms.PROPERTYNAME_FORM_TYPE, CompletedForms.class,40);
		label_description = new JLabel();
		scrollPane48 = new JScrollPane();
		description = ATBasicComponentFactory.createTextArea(detailsModel.getModel(CompletedForms.PROPERTYNAME_DESCRIPTION));
		label_dateCompleted = new JLabel();
		dateCompleted = ATBasicComponentFactory.createDateField(detailsModel.getModel(CompletedForms.PROPERTYNAME_DATE_COMPLETED));
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow",
			"fill:default:grow"));

		//======== panel1 ========
		{
			panel1.setBorder(Borders.DLU4_BORDER);
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- Label_FormType ----
			Label_FormType.setText("Form Type");
			ATFieldInfo.assignLabelInfo(Label_FormType, CompletedForms.class, CompletedForms.PROPERTYNAME_FORM_TYPE);
			panel1.add(Label_FormType, cc.xy(1, 1));
			panel1.add(formType, cc.xy(3, 1));

			//---- label_description ----
			label_description.setText("Description");
			ATFieldInfo.assignLabelInfo(label_description, CompletedForms.class, CompletedForms.PROPERTYNAME_DESCRIPTION);
			panel1.add(label_description, cc.xy(1, 3));

			//======== scrollPane48 ========
			{
				scrollPane48.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane48.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane48.setPreferredSize(new Dimension(200, 68));

				//---- description ----
				description.setRows(4);
				description.setLineWrap(true);
				description.setWrapStyleWord(true);
				description.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				description.setMinimumSize(new Dimension(200, 16));
				scrollPane48.setViewportView(description);
			}
			panel1.add(scrollPane48, cc.xywh(3, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

			//---- label_dateCompleted ----
			label_dateCompleted.setText("Date Completed");
			ATFieldInfo.assignLabelInfo(label_dateCompleted, CompletedForms.class, CompletedForms.PROPERTYNAME_DATE_COMPLETED);
			panel1.add(label_dateCompleted, cc.xy(1, 5));

			//---- dateCompleted ----
			dateCompleted.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			dateCompleted.setColumns(10);
			panel1.add(dateCompleted, cc.xy(3, 5));
		}
		add(panel1, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	private JLabel Label_FormType;
	private JComboBox formType;
	private JLabel label_description;
	private JScrollPane scrollPane48;
	public JTextArea description;
	private JLabel label_dateCompleted;
	public JFormattedTextField dateCompleted;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
