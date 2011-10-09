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
 * Created by JFormDesigner on Tue Aug 29 14:14:15 EDT 2006
 */

package org.rac.editors;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.model.ArchDescriptionNames;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.util.LookupListUtils;
import org.rac.model.PatronPublicationsNames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;

public class PatronPublicationNamesFields extends RAC_DomainEditorFields {

	public PatronPublicationNamesFields() {
		super();
		initComponents();
	}

	public Component getInitialFocusComponent() {
		return role;
	}


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		sortNameDisplay = new JTextField();
		label_role = new JLabel();
		role = ATBasicComponentFactory.createComboBox(detailsModel, PatronPublicationsNames.PROPERTYNAME_ROLE, PatronPublicationsNames.class);
		label_form = new JLabel();
		form = ATBasicComponentFactory.createComboBox(detailsModel, PatronPublicationsNames.PROPERTYNAME_FORM, PatronPublicationsNames.class, true);
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
		ATFieldInfo.assignLabelInfo(label_role, PatronPublicationsNames.class, PatronPublicationsNames.PROPERTYNAME_ROLE);
		add(label_role, cc.xy(1, 3));

		//---- role ----
		role.setOpaque(false);
		add(role, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

		//---- label_form ----
		label_form.setText("Form Subdivision");
		ATFieldInfo.assignLabelInfo(label_form, PatronPublicationsNames.class, PatronPublicationsNames.PROPERTYNAME_FORM);
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


	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */
	public void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		PatronPublicationsNames archDescriptionNamesModel = (PatronPublicationsNames)model;
		sortNameDisplay.setText(archDescriptionNamesModel.getName().getSortName());
	}

}