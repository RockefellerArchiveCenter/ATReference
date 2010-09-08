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
 * Created by JFormDesigner on Fri Feb 05 13:23:55 EST 2010
 */

package org.rac.editors;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.rac.model.PatronPhoneNumbers;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;

public class PhoneNumberFields extends DomainEditorFields {
	public PhoneNumberFields() {
		initComponents();
	}

	@Override
	public Component getInitialFocusComponent() {
		return phoneNumberType;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		Label_PhoneNumberType = new JLabel();
		phoneNumberType = ATBasicComponentFactory.createComboBox(detailsModel, PatronPhoneNumbers.PROPERTYNAME_PHONE_NUMBER_TYPE,  PatronPhoneNumbers.class);
		label_phoneNumber = new JLabel();
		phoneNumber = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronPhoneNumbers.PROPERTYNAME_PHONE_NUMBER));
		preferredAddress = ATBasicComponentFactory.createCheckBox(detailsModel, PatronPhoneNumbers.PROPERTYNAME_PREFERRED_PHONE_NUMBER, PatronPhoneNumbers.class);
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow",
			"fill:default:grow"));

		//======== panel1 ========
		{
			panel1.setBorder(Borders.DLU4_BORDER);
			panel1.setPreferredSize(new Dimension(450, 74));
			panel1.setLayout(new FormLayout(
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
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- Label_PhoneNumberType ----
			Label_PhoneNumberType.setText("Phone Number Type");
			ATFieldInfo.assignLabelInfo(Label_PhoneNumberType, PatronPhoneNumbers.class, PatronPhoneNumbers.PROPERTYNAME_PHONE_NUMBER_TYPE);
			panel1.add(Label_PhoneNumberType, cc.xy(1, 1));
			panel1.add(phoneNumberType, cc.xy(3, 1));

			//---- label_phoneNumber ----
			label_phoneNumber.setText("Phone Number");
			ATFieldInfo.assignLabelInfo(label_phoneNumber, PatronPhoneNumbers.class, PatronPhoneNumbers.PROPERTYNAME_PHONE_NUMBER);
			panel1.add(label_phoneNumber, cc.xy(1, 3));

			//---- phoneNumber ----
			phoneNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.add(phoneNumber, cc.xywh(3, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

			//---- preferredAddress ----
			preferredAddress.setText("Preferred Phone Number");
			preferredAddress.setOpaque(false);
			preferredAddress.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			preferredAddress.setText(ATFieldInfo.getLabel(PatronPhoneNumbers.class, PatronPhoneNumbers.PROPERTYNAME_PREFERRED_PHONE_NUMBER));
			panel1.add(preferredAddress, cc.xywh(1, 5, 3, 1));
		}
		add(panel1, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	private JLabel Label_PhoneNumberType;
	private JComboBox phoneNumberType;
	private JLabel label_phoneNumber;
	public JTextField phoneNumber;
	public JCheckBox preferredAddress;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
