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
 * Created by JFormDesigner on Wed Jun 02 10:52:51 EDT 2010
 */

package org.rac.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.rac.model.Services;

public class ServiceFields extends RAC_DomainEditorFields {
	public ServiceFields() {
		initComponents();
	}

	@Override
	public Component getInitialFocusComponent() {
		return addressType;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		contentPanel = new JPanel();
		Label_addressType = new JLabel();
		addressType = ATBasicComponentFactory.createComboBox(detailsModel, Services.PROPERTYNAME_CATEGORY, Services.class);
		label_address1 = new JLabel();
		address1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Services.PROPERTYNAME_DESCRIPTION));
		label_address2 = new JLabel();
		address2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Services.PROPERTYNAME_UNITS));
		label_address4 = new JLabel();
		extentNumber = ATBasicComponentFactory.createDoubleField(detailsModel,Services.PROPERTYNAME_COST);
		label_address3 = new JLabel();
		date1Begin = ATBasicComponentFactory.createIntegerField(detailsModel,Services.PROPERTYNAME_MIN_QUANTITY_PER_CALENDAR_YEAR);
		label_city = new JLabel();
		date1Begin2 = ATBasicComponentFactory.createIntegerField(detailsModel,Services.PROPERTYNAME_MAX_QUANTITY_PER_CALENDAR_YEAR);
		preferredAddress = ATBasicComponentFactory.createCheckBox(detailsModel, Services.PROPERTYNAME_UNLIMITED_QUANTITY_PER_CALENDAR_YEAR, Services.class);
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow",
			"top:default:grow"));

		//======== contentPanel ========
		{
			contentPanel.setBorder(Borders.DLU4_BORDER);
			contentPanel.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- Label_addressType ----
			Label_addressType.setText("Category");
			ATFieldInfo.assignLabelInfo(Label_addressType, Services.class, Services.PROPERTYNAME_CATEGORY);
			contentPanel.add(Label_addressType, cc.xy(1, 1));
			contentPanel.add(addressType, cc.xy(3, 1));

			//---- label_address1 ----
			label_address1.setText("Description");
			ATFieldInfo.assignLabelInfo(label_address1, Services.class, Services.PROPERTYNAME_DESCRIPTION);
			contentPanel.add(label_address1, cc.xy(1, 3));

			//---- address1 ----
			address1.setColumns(30);
			contentPanel.add(address1, cc.xy(3, 3));

			//---- label_address2 ----
			label_address2.setText("Units");
			ATFieldInfo.assignLabelInfo(label_address2, Services.class, Services.PROPERTYNAME_UNITS);
			contentPanel.add(label_address2, cc.xy(1, 5));

			//---- address2 ----
			address2.setColumns(30);
			contentPanel.add(address2, cc.xy(3, 5));

			//---- label_address4 ----
			label_address4.setText("Cost");
			ATFieldInfo.assignLabelInfo(label_address4, Services.class, Services.PROPERTYNAME_COST);
			contentPanel.add(label_address4, cc.xy(1, 7));

			//---- extentNumber ----
			extentNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			extentNumber.setColumns(5);
			contentPanel.add(extentNumber, cc.xywh(3, 7, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

			//---- label_address3 ----
			label_address3.setText("Min/Calendar Year");
			ATFieldInfo.assignLabelInfo(label_address3, Services.class, Services.PROPERTYNAME_MIN_QUANTITY_PER_CALENDAR_YEAR);
			contentPanel.add(label_address3, cc.xy(1, 9));

			//---- date1Begin ----
			date1Begin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			date1Begin.setColumns(5);
			contentPanel.add(date1Begin, cc.xywh(3, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

			//---- label_city ----
			label_city.setText("Max/Calendar Year");
			ATFieldInfo.assignLabelInfo(label_city, Services.class, Services.PROPERTYNAME_MAX_QUANTITY_PER_CALENDAR_YEAR);
			contentPanel.add(label_city, cc.xy(1, 11));

			//---- date1Begin2 ----
			date1Begin2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			date1Begin2.setColumns(5);
			contentPanel.add(date1Begin2, cc.xywh(3, 11, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

			//---- preferredAddress ----
			preferredAddress.setText("Unlimited/Calendar Year");
			preferredAddress.setOpaque(false);
			preferredAddress.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			preferredAddress.setText(ATFieldInfo.getLabel(Services.class, Services.PROPERTYNAME_UNLIMITED_QUANTITY_PER_CALENDAR_YEAR));
			contentPanel.add(preferredAddress, cc.xywh(1, 13, 3, 1));
		}
		add(contentPanel, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel contentPanel;
	private JLabel Label_addressType;
	private JComboBox addressType;
	private JLabel label_address1;
	private JTextField address1;
	private JLabel label_address2;
	private JTextField address2;
	private JLabel label_address4;
	public JFormattedTextField extentNumber;
	private JLabel label_address3;
	public JFormattedTextField date1Begin;
	private JLabel label_city;
	public JFormattedTextField date1Begin2;
	public JCheckBox preferredAddress;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
