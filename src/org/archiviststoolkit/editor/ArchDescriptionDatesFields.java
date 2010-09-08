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
 * Created by JFormDesigner on Sun Aug 06 09:23:34 EDT 2006
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.Deaccessions;
import org.archiviststoolkit.model.ArchDescriptionDates;
import org.archiviststoolkit.structure.ATFieldInfo;

public class ArchDescriptionDatesFields extends DomainEditorFields {
	public ArchDescriptionDatesFields() {
		super();
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		repositoryInfo = new JPanel();
		label15 = new JLabel();
		dateExpression = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionDates.PROPERTYNAME_DATE_EXPRESSION));
		Date1Label = new JLabel();
		panel1 = new JPanel();
		label_date1Begin = new JLabel();
		date1Begin = ATBasicComponentFactory.createISODateField(detailsModel.getModel(ArchDescriptionDates.PROPERTYNAME_ISODATE_BEGIN));
		label_date1End = new JLabel();
		date1End = ATBasicComponentFactory.createISODateField(detailsModel.getModel(ArchDescriptionDates.PROPERTYNAME_ISODATE_END));
		BulkDatesLabel = new JLabel();
		panel2 = new JPanel();
		label_bulkDateBegin = new JLabel();
		bulkDateBegin = ATBasicComponentFactory.createISODateField(detailsModel.getModel(ArchDescriptionDates.PROPERTYNAME_ISOBULK_DATE_BEGIN));
		label_bulkDateEnd = new JLabel();
		bulkDateEnd = ATBasicComponentFactory.createISODateField(detailsModel.getModel(ArchDescriptionDates.PROPERTYNAME_ISOBULK_DATE_END));
		rights = ATBasicComponentFactory.createCheckBox(detailsModel, ArchDescriptionDates.PROPERTYNAME_CERTAINTY, ArchDescriptionDates.class);
		label3 = new JLabel();
		extentType = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionDates.PROPERTYNAME_DATE_TYPE, ArchDescriptionDates.class);
		label4 = new JLabel();
		era = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionDates.PROPERTYNAME_ERA, ArchDescriptionDates.class);
		label5 = new JLabel();
		calendar = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionDates.PROPERTYNAME_CALENDAR, ArchDescriptionDates.class);
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setLayout(new FormLayout(
			"default:grow",
			"top:default:grow"));

		//======== repositoryInfo ========
		{
			repositoryInfo.setBorder(Borders.DLU4_BORDER);
			repositoryInfo.setOpaque(false);
			repositoryInfo.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			repositoryInfo.setLayout(new FormLayout(
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

			//---- label15 ----
			label15.setText("Date Expression");
			label15.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			ATFieldInfo.assignLabelInfo(label15, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_DATE_EXPRESSION);
			repositoryInfo.add(label15, cc.xy(1, 1));

			//---- dateExpression ----
			dateExpression.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			dateExpression.setColumns(12);
			repositoryInfo.add(dateExpression, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

			//---- Date1Label ----
			Date1Label.setText("Inclusive Dates");
			Date1Label.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			repositoryInfo.add(Date1Label, cc.xywh(1, 3, 3, 1));

			//======== panel1 ========
			{
				panel1.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					RowSpec.decodeSpecs("default")));

				//---- label_date1Begin ----
				label_date1Begin.setText("Begin");
				label_date1Begin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_date1Begin, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_ISODATE_BEGIN);
				panel1.add(label_date1Begin, cc.xy(3, 1));

				//---- date1Begin ----
				date1Begin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel1.add(date1Begin, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

				//---- label_date1End ----
				label_date1End.setText("End");
				label_date1End.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_date1End, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_ISODATE_END);
				panel1.add(label_date1End, cc.xy(7, 1));

				//---- date1End ----
				date1End.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel1.add(date1End, new CellConstraints(9, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));
			}
			repositoryInfo.add(panel1, cc.xywh(1, 5, 3, 1));

			//---- BulkDatesLabel ----
			BulkDatesLabel.setText("Bulk Dates");
			BulkDatesLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			repositoryInfo.add(BulkDatesLabel, cc.xywh(1, 7, 3, 1));

			//======== panel2 ========
			{
				panel2.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					RowSpec.decodeSpecs("default")));

				//---- label_bulkDateBegin ----
				label_bulkDateBegin.setText("Begin");
				label_bulkDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_bulkDateBegin, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_ISOBULK_DATE_BEGIN);
				panel2.add(label_bulkDateBegin, cc.xy(3, 1));

				//---- bulkDateBegin ----
				bulkDateBegin.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel2.add(bulkDateBegin, cc.xywh(5, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

				//---- label_bulkDateEnd ----
				label_bulkDateEnd.setText("End");
				label_bulkDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_bulkDateEnd, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_ISOBULK_DATE_END);
				panel2.add(label_bulkDateEnd, cc.xy(7, 1));

				//---- bulkDateEnd ----
				bulkDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel2.add(bulkDateEnd, new CellConstraints(9, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));
			}
			repositoryInfo.add(panel2, cc.xywh(1, 9, 3, 1));

			//---- rights ----
			rights.setBackground(new Color(231, 188, 251));
			rights.setText("Notification");
			rights.setOpaque(false);
			rights.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			rights.setText(ATFieldInfo.getLabel(ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_CERTAINTY));
			repositoryInfo.add(rights, cc.xywh(1, 11, 3, 1));

			//---- label3 ----
			label3.setText("Date Type");
			ATFieldInfo.assignLabelInfo(label3, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_DATE_TYPE);
			repositoryInfo.add(label3, cc.xy(1, 13));

			//---- extentType ----
			extentType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			extentType.setOpaque(false);
			repositoryInfo.add(extentType, new CellConstraints(3, 13, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets( 0, 5, 5, 5)));

			//---- label4 ----
			label4.setText("Era");
			ATFieldInfo.assignLabelInfo(label4, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_ERA);
			repositoryInfo.add(label4, cc.xy(1, 15));

			//---- era ----
			era.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			era.setOpaque(false);
			repositoryInfo.add(era, new CellConstraints(3, 15, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets( 0, 5, 5, 5)));

			//---- label5 ----
			label5.setText("Calendar");
			ATFieldInfo.assignLabelInfo(label5, ArchDescriptionDates.class, ArchDescriptionDates.PROPERTYNAME_CALENDAR);
			repositoryInfo.add(label5, cc.xy(1, 17));

			//---- calendar ----
			calendar.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			calendar.setOpaque(false);
			repositoryInfo.add(calendar, new CellConstraints(3, 17, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets( 0, 5, 5, 5)));
		}
		add(repositoryInfo, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel repositoryInfo;
	private JLabel label15;
	private JTextField dateExpression;
	private JLabel Date1Label;
	private JPanel panel1;
	private JLabel label_date1Begin;
	public JTextField date1Begin;
	private JLabel label_date1End;
	public JTextField date1End;
	private JLabel BulkDatesLabel;
	private JPanel panel2;
	private JLabel label_bulkDateBegin;
	public JTextField bulkDateBegin;
	private JLabel label_bulkDateEnd;
	public JTextField bulkDateEnd;
	public JCheckBox rights;
	private JLabel label3;
	public JComboBox extentType;
	private JLabel label4;
	public JComboBox era;
	private JLabel label5;
	public JComboBox calendar;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Component getInitialFocusComponent() {
		return dateExpression;
	}
}