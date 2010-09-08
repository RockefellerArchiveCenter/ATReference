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
 * Created by JFormDesigner on Tue Sep 22 10:33:00 EDT 2009
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.model.ArchDescriptionPhysicalDescriptions;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.structure.ATFieldInfo;

public class ArchDescPhysicalDescFields extends DomainEditorFields {
	public ArchDescPhysicalDescFields() {
		initComponents();
	}

	public Component getInitialFocusComponent() {
		return null;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		repositoryInfo = new JPanel();
		panel21 = new JPanel();
		label_resourcesExtentNumber = new JLabel();
		resourcesExtentNumber = ATBasicComponentFactory.createDoubleField(detailsModel, ArchDescriptionPhysicalDescriptions.PROPERTYNAME_EXTENT_NUMBER);
		extentType2 = ATBasicComponentFactory.createComboBox(detailsModel, ArchDescriptionPhysicalDescriptions.PROPERTYNAME_EXTENT_TYPE, ArchDescriptionPhysicalDescriptions.class);
		label_resourcesExtentDescription = new JLabel();
		scrollPane422 = new JScrollPane();
		containerSummary = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescriptionPhysicalDescriptions.PROPERTYNAME_CONTAINER_SUMMARY),false);
		label_resourcesExtentDescription2 = new JLabel();
		scrollPane423 = new JScrollPane();
		containerSummary2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescriptionPhysicalDescriptions.PROPERTYNAME_PHYSICAL_DETAILS),false);
		label_resourcesExtentDescription3 = new JLabel();
		scrollPane424 = new JScrollPane();
		containerSummary3 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(ArchDescriptionPhysicalDescriptions.PROPERTYNAME_DIMENSIONS),false);
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
					FormFactory.DEFAULT_ROWSPEC
				}));

			//======== panel21 ========
			{
				panel21.setOpaque(false);
				panel21.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- label_resourcesExtentNumber ----
				label_resourcesExtentNumber.setText("Extent");
				label_resourcesExtentNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_resourcesExtentNumber, ArchDescriptionPhysicalDescriptions.class, ArchDescriptionPhysicalDescriptions.PROPERTYNAME_EXTENT_NUMBER);
				panel21.add(label_resourcesExtentNumber, cc.xywh(1, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

				//---- resourcesExtentNumber ----
				resourcesExtentNumber.setColumns(4);
				resourcesExtentNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel21.add(resourcesExtentNumber, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

				//---- extentType2 ----
				extentType2.setOpaque(false);
				extentType2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel21.add(extentType2, new CellConstraints(5, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT, new Insets( 0, 5, 5, 5)));
			}
			repositoryInfo.add(panel21, cc.xywh(1, 1, 3, 1));

			//---- label_resourcesExtentDescription ----
			label_resourcesExtentDescription.setText("Container Summary");
			label_resourcesExtentDescription.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			ATFieldInfo.assignLabelInfo(label_resourcesExtentDescription, ArchDescriptionPhysicalDescriptions.class, ArchDescriptionPhysicalDescriptions.PROPERTYNAME_CONTAINER_SUMMARY);
			repositoryInfo.add(label_resourcesExtentDescription, cc.xywh(1, 3, 3, 1));

			//======== scrollPane422 ========
			{
				scrollPane422.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane422.setOpaque(false);
				scrollPane422.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

				//---- containerSummary ----
				containerSummary.setRows(4);
				containerSummary.setWrapStyleWord(true);
				containerSummary.setLineWrap(true);
				containerSummary.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane422.setViewportView(containerSummary);
			}
			repositoryInfo.add(scrollPane422, cc.xywh(1, 5, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

			//---- label_resourcesExtentDescription2 ----
			label_resourcesExtentDescription2.setText("Other Physical Details");
			label_resourcesExtentDescription2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			ATFieldInfo.assignLabelInfo(label_resourcesExtentDescription2, ArchDescriptionPhysicalDescriptions.class, ArchDescriptionPhysicalDescriptions.PROPERTYNAME_PHYSICAL_DETAILS);
			repositoryInfo.add(label_resourcesExtentDescription2, cc.xywh(1, 7, 3, 1));

			//======== scrollPane423 ========
			{
				scrollPane423.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane423.setOpaque(false);
				scrollPane423.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

				//---- containerSummary2 ----
				containerSummary2.setRows(4);
				containerSummary2.setWrapStyleWord(true);
				containerSummary2.setLineWrap(true);
				containerSummary2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane423.setViewportView(containerSummary2);
			}
			repositoryInfo.add(scrollPane423, cc.xywh(1, 9, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

			//---- label_resourcesExtentDescription3 ----
			label_resourcesExtentDescription3.setText("Container Summary");
			label_resourcesExtentDescription3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			ATFieldInfo.assignLabelInfo(label_resourcesExtentDescription3, ArchDescriptionPhysicalDescriptions.class, ArchDescriptionPhysicalDescriptions.PROPERTYNAME_DIMENSIONS);
			repositoryInfo.add(label_resourcesExtentDescription3, cc.xywh(1, 11, 3, 1));

			//======== scrollPane424 ========
			{
				scrollPane424.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane424.setOpaque(false);
				scrollPane424.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

				//---- containerSummary3 ----
				containerSummary3.setRows(4);
				containerSummary3.setWrapStyleWord(true);
				containerSummary3.setLineWrap(true);
				containerSummary3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane424.setViewportView(containerSummary3);
			}
			repositoryInfo.add(scrollPane424, cc.xywh(1, 13, 3, 1, CellConstraints.DEFAULT, CellConstraints.FILL));
		}
		add(repositoryInfo, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel repositoryInfo;
	private JPanel panel21;
	private JLabel label_resourcesExtentNumber;
	public JFormattedTextField resourcesExtentNumber;
	public JComboBox extentType2;
	private JLabel label_resourcesExtentDescription;
	private JScrollPane scrollPane422;
	public JTextArea containerSummary;
	private JLabel label_resourcesExtentDescription2;
	private JScrollPane scrollPane423;
	public JTextArea containerSummary2;
	private JLabel label_resourcesExtentDescription3;
	private JScrollPane scrollPane424;
	public JTextArea containerSummary3;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
