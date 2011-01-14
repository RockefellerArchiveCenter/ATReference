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
 * Created by JFormDesigner on Wed Dec 15 12:39:56 EST 2010
 */

package org.rac.editors;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.rac.model.PatronVisitsNames;

public class PatronVisitNamesFields extends RAC_DomainEditorFields {
	public PatronVisitNamesFields() {
		initComponents();
	}

	@Override
	public Component getInitialFocusComponent() {
		return role;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		sortNameDisplay = new JTextField();
		label_role = new JLabel();
		role = ATBasicComponentFactory.createComboBox(detailsModel, PatronVisitsNames.PROPERTYNAME_ROLE, PatronVisitsNames.class);
		label_form = new JLabel();
		form = ATBasicComponentFactory.createComboBox(detailsModel, PatronVisitsNames.PROPERTYNAME_FORM, PatronVisitsNames.class, true);
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
		setBackground(new Color(234, 201, 250));
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec("max(default;400px):grow")
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- sortNameDisplay ----
		sortNameDisplay.setEditable(false);
		sortNameDisplay.setBorder(null);
		sortNameDisplay.setForeground(new Color(0, 0, 102));
		sortNameDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		sortNameDisplay.setSelectionColor(new Color(204, 0, 51));
		sortNameDisplay.setOpaque(false);
		add(sortNameDisplay, cc.xywh(1, 1, 3, 1));

		//---- label_role ----
		label_role.setText("Role");
		ATFieldInfo.assignLabelInfo(label_role, PatronVisitsNames.class, PatronVisitsNames.PROPERTYNAME_ROLE);
		add(label_role, cc.xy(1, 3));

		//---- role ----
		role.setOpaque(false);
		add(role, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- label_form ----
		label_form.setText("Form Subdivision");
		ATFieldInfo.assignLabelInfo(label_form, PatronVisitsNames.class, PatronVisitsNames.PROPERTYNAME_FORM);
		add(label_form, cc.xy(1, 5));

		//---- form ----
		form.setOpaque(false);
		add(form, cc.xywh(3, 5, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JTextField sortNameDisplay;
	private JLabel label_role;
	private JComboBox role;
	private JLabel label_form;
	private JComboBox form;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
