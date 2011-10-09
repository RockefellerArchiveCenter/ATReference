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
 * Created by JFormDesigner on Fri Dec 18 16:08:07 EST 2009
 */

package org.rac.editors;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.rac.model.PatronFunding;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.structure.ATFieldInfo;

public class FundingFields extends DomainEditorFields {
	public FundingFields() {
		initComponents();
	}

	public Component getInitialFocusComponent() {
		return fundingType;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		Label_FundingType = new JLabel();
		fundingType = ATBasicComponentFactory.createComboBox(detailsModel, PatronFunding.PROPERTYNAME_FUNDING_TYPE, PatronFunding.class,40);
		label_addressTypeOther = new JLabel();
		fundingDate = ATBasicComponentFactory.createISODateField(detailsModel.getModel(PatronFunding.PROPERTYNAME_FUNDING_DATE));
		topic = new JLabel();
		address1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronFunding.PROPERTYNAME_TOPIC));
		label_address2 = new JLabel();
		scrollPane48 = new JScrollPane();
		patronNotes = ATBasicComponentFactory.createTextArea(detailsModel.getModel(PatronFunding.PROPERTYNAME_AWARD_DETAILS));
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
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				}));

			//---- Label_FundingType ----
			Label_FundingType.setText("Funding Type");
			ATFieldInfo.assignLabelInfo(Label_FundingType, PatronFunding.class, PatronFunding.PROPERTYNAME_FUNDING_TYPE);
			panel1.add(Label_FundingType, cc.xy(1, 1));
			panel1.add(fundingType, cc.xy(3, 1));

			//---- label_addressTypeOther ----
			label_addressTypeOther.setText("Date");
			ATFieldInfo.assignLabelInfo(label_addressTypeOther, PatronFunding.class, PatronFunding.PROPERTYNAME_FUNDING_DATE);
			panel1.add(label_addressTypeOther, cc.xy(1, 3));

			//---- fundingDate ----
			fundingDate.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.add(fundingDate, cc.xy(3, 3));

			//---- topic ----
			topic.setText("Topic");
			ATFieldInfo.assignLabelInfo(topic, PatronFunding.class, PatronFunding.PROPERTYNAME_TOPIC);
			panel1.add(topic, cc.xy(1, 5));

			//---- address1 ----
			address1.setColumns(30);
			panel1.add(address1, cc.xy(3, 5));

			//---- label_address2 ----
			label_address2.setText("Award Details");
			ATFieldInfo.assignLabelInfo(label_address2, PatronFunding.class, PatronFunding.PROPERTYNAME_AWARD_DETAILS);
			panel1.add(label_address2, cc.xy(1, 7));

			//======== scrollPane48 ========
			{
				scrollPane48.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane48.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane48.setPreferredSize(new Dimension(200, 68));

				//---- patronNotes ----
				patronNotes.setRows(4);
				patronNotes.setLineWrap(true);
				patronNotes.setWrapStyleWord(true);
				patronNotes.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				patronNotes.setMinimumSize(new Dimension(200, 16));
				scrollPane48.setViewportView(patronNotes);
			}
			panel1.add(scrollPane48, cc.xywh(3, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		}
		add(panel1, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	private JLabel Label_FundingType;
	private JComboBox fundingType;
	private JLabel label_addressTypeOther;
	public JTextField fundingDate;
	private JLabel topic;
	private JTextField address1;
	private JLabel label_address2;
	private JScrollPane scrollPane48;
	public JTextArea patronNotes;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

}
