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
 * Created by JFormDesigner on Tue Oct 13 11:13:39 EDT 2009
 */

package org.rac.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.rac.model.PatronAddresses;

public class AddressFields extends RAC_DomainEditorFields {
	public AddressFields() {
		initComponents();
	}

	public Component getInitialFocusComponent() {
		return addressType;
	}

	private void addressTypeItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			addOtherTermIfNecessary(addressType, contentPanel, PatronAddresses.class, PatronAddresses.PROPERTYNAME_ADDRESS_TYPE);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		contentPanel = new JPanel();
		Label_addressType = new JLabel();
		addressType = ATBasicComponentFactory.createComboBox(detailsModel, PatronAddresses.PROPERTYNAME_ADDRESS_TYPE, PatronAddresses.class);
		label_address1 = new JLabel();
		address1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronAddresses.PROPERTYNAME_ADDRESS1));
		label_address2 = new JLabel();
		address2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronAddresses.PROPERTYNAME_ADDRESS2));
		label_address3 = new JLabel();
		address3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronAddresses.PROPERTYNAME_ADDRESS3));
		label_city = new JLabel();
		city = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronAddresses.PROPERTYNAME_CITY));
		label_region = new JLabel();
		region = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronAddresses.PROPERTYNAME_REGION));
		label_mailCode = new JLabel();
		mailCode = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronAddresses.PROPERTYNAME_MAIL_CODE));
		label_country = new JLabel();
		country = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronAddresses.PROPERTYNAME_COUNTRY));
		preferredAddress = ATBasicComponentFactory.createCheckBox(detailsModel, PatronAddresses.PREFERRED_ADDRESS, PatronAddresses.class);
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
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- Label_addressType ----
			Label_addressType.setText("Address Type");
			ATFieldInfo.assignLabelInfo(Label_addressType, PatronAddresses.class, PatronAddresses.PROPERTYNAME_ADDRESS_TYPE);
			contentPanel.add(Label_addressType, cc.xy(1, 1));

			//---- addressType ----
			addressType.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					addressTypeItemStateChanged(e);
				}
			});
			contentPanel.add(addressType, cc.xy(3, 1));

			//---- label_address1 ----
			label_address1.setText("Address ");
			ATFieldInfo.assignLabelInfo(label_address1, PatronAddresses.class, PatronAddresses.PROPERTYNAME_ADDRESS1);
			contentPanel.add(label_address1, cc.xy(1, 3));

			//---- address1 ----
			address1.setColumns(30);
			contentPanel.add(address1, cc.xy(3, 3));

			//---- label_address2 ----
			label_address2.setText("Address ");
			ATFieldInfo.assignLabelInfo(label_address2, PatronAddresses.class, PatronAddresses.PROPERTYNAME_ADDRESS2);
			contentPanel.add(label_address2, cc.xy(1, 5));

			//---- address2 ----
			address2.setColumns(30);
			contentPanel.add(address2, cc.xy(3, 5));

			//---- label_address3 ----
			label_address3.setText("Address ");
			ATFieldInfo.assignLabelInfo(label_address3, PatronAddresses.class, PatronAddresses.PROPERTYNAME_ADDRESS3);
			contentPanel.add(label_address3, cc.xy(1, 7));

			//---- address3 ----
			address3.setColumns(30);
			contentPanel.add(address3, cc.xy(3, 7));

			//---- label_city ----
			label_city.setText("City");
			ATFieldInfo.assignLabelInfo(label_city, PatronAddresses.class, PatronAddresses.PROPERTYNAME_CITY);
			contentPanel.add(label_city, cc.xy(1, 9));

			//---- city ----
			city.setColumns(30);
			contentPanel.add(city, cc.xy(3, 9));

			//---- label_region ----
			label_region.setText("Region");
			ATFieldInfo.assignLabelInfo(label_region, PatronAddresses.class, PatronAddresses.PROPERTYNAME_REGION);
			contentPanel.add(label_region, cc.xy(1, 11));

			//---- region ----
			region.setColumns(30);
			contentPanel.add(region, cc.xy(3, 11));

			//---- label_mailCode ----
			label_mailCode.setText("Mail Code");
			ATFieldInfo.assignLabelInfo(label_mailCode, PatronAddresses.class, PatronAddresses.PROPERTYNAME_MAIL_CODE);
			contentPanel.add(label_mailCode, cc.xy(1, 13));

			//---- mailCode ----
			mailCode.setColumns(30);
			contentPanel.add(mailCode, cc.xy(3, 13));

			//---- label_country ----
			label_country.setText("Country");
			ATFieldInfo.assignLabelInfo(label_country, PatronAddresses.class, PatronAddresses.PROPERTYNAME_COUNTRY);
			contentPanel.add(label_country, cc.xy(1, 15));

			//---- country ----
			country.setColumns(30);
			contentPanel.add(country, cc.xy(3, 15));

			//---- preferredAddress ----
			preferredAddress.setText("Preferred Address");
			preferredAddress.setOpaque(false);
			preferredAddress.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			preferredAddress.setText(ATFieldInfo.getLabel(PatronAddresses.class, PatronAddresses.PREFERRED_ADDRESS));
			contentPanel.add(preferredAddress, cc.xywh(1, 17, 3, 1));
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
	private JLabel label_address3;
	private JTextField address3;
	private JLabel label_city;
	private JTextField city;
	private JLabel label_region;
	private JTextField region;
	private JLabel label_mailCode;
	private JTextField mailCode;
	private JLabel label_country;
	private JTextField country;
	public JCheckBox preferredAddress;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}