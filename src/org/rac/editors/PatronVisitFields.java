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
 * Created by JFormDesigner on Tue Oct 13 11:13:39 EDT 2009
 */

package org.rac.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.NameAuthorityLookup;
import org.archiviststoolkit.dialog.SubjectTermLookup;
import org.archiviststoolkit.editor.NameEnabledEditor;
import org.archiviststoolkit.editor.SubjectEnabledEditorFields;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.rac.dialogs.PatronNameAuthorityLookup;
import org.rac.model.PatronVisits;
import org.rac.model.PatronVisitsNames;
import org.rac.model.PatronVisitsSubjects;

public class PatronVisitFields extends RAC_DomainEditorFields implements SubjectEnabledEditorFields, NameEnabledEditor {
	public PatronVisitFields() {
		initComponents();
	}

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */
	@Override
	public void setModel(DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		PatronVisits visitModel = (PatronVisits)model;
		subjectsTable.updateCollection(visitModel.getSubjects());
		namesTable.updateCollection(visitModel.getNames());
	}

	public Component getInitialFocusComponent() {
		return visitDate;
	}

	private void addSubjectActionPerformed() {
		SubjectTermLookup subjectLookupDialog = new SubjectTermLookup(getParentEditor(), this);
//		subjectLookupDialog.setMainHeaderByClass(archDescriptionModel.getClass());
		subjectLookupDialog.showDialog();
	}

	private void removeSubjectActionPerformed() {
		try {
			removeRelatedTableRow(subjectsTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Subject not removed", e).showDialog();
		}
	}

	private void researchPurposeStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			addOtherTermIfNecessary(researchPurpose, contentPanel, PatronVisits.class, PatronVisits.PROPERTYNAME_RESEARCH_PURPOSE);
		}
	}

	private void addNameActionPerformed() {
		PatronNameAuthorityLookup nameLookupDialog = new PatronNameAuthorityLookup(getParentEditor(), this);
		nameLookupDialog.showDialog();
	}

	private void removeNameActionPerformed() {
		PatronVisits patronVisitModel = (PatronVisits) super.getModel();
		try {
			this.removeRelatedTableRow(getNamesTable(), null, patronVisitModel);
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Name link not removed", e1).showDialog();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		contentPanel = new JPanel();
		label_visitDate = new JLabel();
		visitDate = ATBasicComponentFactory.createDateField(detailsModel.getModel( PatronVisits.PROPERTYNAME_VISIT_DATE));
		label_subject = new JLabel();
		address1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronVisits.PROPERTYNAME_CONTACT_ARCHIVIST));
		label_topic = new JLabel();
		topic = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronVisits.PROPERTYNAME_TOPIC));
		label_topic2 = new JLabel();
		researchPurpose = ATBasicComponentFactory.createComboBox(detailsModel, PatronVisits.PROPERTYNAME_RESEARCH_PURPOSE, PatronVisits.class);
		separator5 = new JSeparator();
		SubjectsLabel = new JLabel();
		scrollPane3 = new JScrollPane();
		subjectsTable = new DomainSortableTable(PatronVisitsSubjects.class);
		panel10 = new JPanel();
		addSubject = new JButton();
		removeSubject = new JButton();
		scrollPane4 = new JScrollPane();
		namesTable = new DomainSortableTable(PatronVisitsNames.class);
		panel11 = new JPanel();
		addName = new JButton();
		removeName = new JButton();
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
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- label_visitDate ----
			label_visitDate.setText("Date");
			ATFieldInfo.assignLabelInfo(label_visitDate, PatronVisits.class, PatronVisits.PROPERTYNAME_VISIT_DATE);
			contentPanel.add(label_visitDate, cc.xy(1, 1));

			//---- visitDate ----
			visitDate.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			visitDate.setColumns(10);
			contentPanel.add(visitDate, cc.xy(3, 1));

			//---- label_subject ----
			label_subject.setText("Subject");
			ATFieldInfo.assignLabelInfo(label_subject, PatronVisits.class, PatronVisits.PROPERTYNAME_CONTACT_ARCHIVIST);
			contentPanel.add(label_subject, cc.xy(1, 3));

			//---- address1 ----
			address1.setColumns(30);
			contentPanel.add(address1, cc.xy(3, 3));

			//---- label_topic ----
			label_topic.setText("Topic");
			ATFieldInfo.assignLabelInfo(label_topic, PatronVisits.class, PatronVisits.PROPERTYNAME_TOPIC);
			contentPanel.add(label_topic, cc.xy(1, 5));

			//---- topic ----
			topic.setColumns(30);
			contentPanel.add(topic, cc.xy(3, 5));

			//---- label_topic2 ----
			label_topic2.setText("Research Purpose");
			ATFieldInfo.assignLabelInfo(label_topic2, PatronVisits.class, PatronVisits.PROPERTYNAME_RESEARCH_PURPOSE);
			contentPanel.add(label_topic2, cc.xy(1, 7));

			//---- researchPurpose ----
			researchPurpose.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					researchPurposeStateChanged(e);
				}
			});
			contentPanel.add(researchPurpose, cc.xy(3, 7));

			//---- separator5 ----
			separator5.setBackground(new Color(220, 220, 232));
			separator5.setForeground(new Color(147, 131, 86));
			separator5.setMinimumSize(new Dimension(1, 10));
			separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			contentPanel.add(separator5, cc.xywh(1, 9, 3, 1));

			//---- SubjectsLabel ----
			SubjectsLabel.setText("Subjects");
			SubjectsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			contentPanel.add(SubjectsLabel, cc.xywh(1, 11, 3, 1));

			//======== scrollPane3 ========
			{
				scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane3.setPreferredSize(new Dimension(219, 100));

				//---- subjectsTable ----
				subjectsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
				scrollPane3.setViewportView(subjectsTable);
			}
			contentPanel.add(scrollPane3, cc.xywh(1, 13, 3, 1));

			//======== panel10 ========
			{
				panel10.setBackground(new Color(231, 188, 251));
				panel10.setOpaque(false);
				panel10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel10.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- addSubject ----
				addSubject.setText("Add Subject");
				addSubject.setOpaque(false);
				addSubject.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				addSubject.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addSubjectActionPerformed();
					}
				});
				panel10.add(addSubject, cc.xy(1, 1));

				//---- removeSubject ----
				removeSubject.setText("Remove Subject");
				removeSubject.setOpaque(false);
				removeSubject.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				removeSubject.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeSubjectActionPerformed();
					}
				});
				panel10.add(removeSubject, cc.xy(3, 1));
			}
			contentPanel.add(panel10, cc.xywh(1, 15, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

			//======== scrollPane4 ========
			{
				scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane4.setPreferredSize(new Dimension(219, 100));

				//---- namesTable ----
				namesTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
				scrollPane4.setViewportView(namesTable);
			}
			contentPanel.add(scrollPane4, cc.xywh(1, 17, 3, 1));

			//======== panel11 ========
			{
				panel11.setBackground(new Color(231, 188, 251));
				panel11.setOpaque(false);
				panel11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel11.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- addName ----
				addName.setText("Add Name");
				addName.setOpaque(false);
				addName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				addName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addNameActionPerformed();
					}
				});
				panel11.add(addName, cc.xy(1, 1));

				//---- removeName ----
				removeName.setText("Remove Name");
				removeName.setOpaque(false);
				removeName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				removeName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeNameActionPerformed();
					}
				});
				panel11.add(removeName, cc.xy(3, 1));
			}
			contentPanel.add(panel11, cc.xywh(1, 19, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		}
		add(contentPanel, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel contentPanel;
	private JLabel label_visitDate;
	public JFormattedTextField visitDate;
	private JLabel label_subject;
	private JTextField address1;
	private JLabel label_topic;
	private JTextField topic;
	private JLabel label_topic2;
	private JComboBox researchPurpose;
	private JSeparator separator5;
	private JLabel SubjectsLabel;
	private JScrollPane scrollPane3;
	private DomainSortableTable subjectsTable;
	private JPanel panel10;
	private JButton addSubject;
	private JButton removeSubject;
	private JScrollPane scrollPane4;
	private DomainSortableTable namesTable;
	private JPanel panel11;
	private JButton addName;
	private JButton removeName;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	public SubjectEnabledModel getSubjectEnabledModel() {
		return (SubjectEnabledModel)this.getModel();
	}

	public DomainSortableTable getSubjectsTable() {
		return subjectsTable;
	}

	public NameEnabledModel getNameEnabledModel() {
		return (NameEnabledModel)this.getModel();
	}

	public DomainSortableTable getNamesTable() {
		return namesTable;
	}
}