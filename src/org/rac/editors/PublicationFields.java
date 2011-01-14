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
 * Created by JFormDesigner on Fri Dec 18 16:28:34 EST 2009
 */

package org.rac.editors;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.SubjectTermLookup;
import org.archiviststoolkit.editor.NameEnabledEditor;
import org.archiviststoolkit.editor.SubjectEnabledEditorFields;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.rac.dialogs.PatronNameAuthorityLookup;
import org.rac.model.PatronPublications;
import org.rac.model.PatronPublicationsNames;
import org.rac.model.PatronPublicationsSubjects;

public class PublicationFields extends RAC_DomainEditorFields implements SubjectEnabledEditorFields, NameEnabledEditor {

	Boolean readOnly = false;

	public PublicationFields() {
		initComponents();
		initAccess();
	}

	public Component getInitialFocusComponent() {
		return title;
	}

	private void fundingTypeItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			addOtherTermIfNecessary(publicationType, contentPanel, PatronPublications.class, PatronPublications.PROPERTYNAME_PUBLICATION_TYPE);
		}
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

	private void addNameActionPerformed() {
		PatronNameAuthorityLookup nameLookupDialog = new PatronNameAuthorityLookup(getParentEditor(), this);
		nameLookupDialog.showDialog();
	}

	private void removeNameActionPerformed() {
		PatronPublications patronPublicationModel = (PatronPublications) super.getModel();
		try {
			this.removeRelatedTableRow(getNamesTable(), null, patronPublicationModel);
		} catch (ObjectNotRemovedException e1) {
			new ErrorDialog("Name link not removed", e1).showDialog();
		}
	}

	private void editNameRelationshipButtonActionPerformed() {
		try {
			DomainSortableTable namesTable = getNamesTable();
			DomainEditor nameEditor = new DomainEditor(PatronPublicationsNames.class, this.getParentEditor(), "Name Link", new PatronPublicationNamesFields());
			nameEditor.setCallingTable(namesTable);
			nameEditor.setNavigationButtonListeners(nameEditor);
			int selectedRow = namesTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select a name to edit.", "warning", JOptionPane.WARNING_MESSAGE);
			} else {
				editRelatedRecord(namesTable, PatronPublicationsNames.class, true, nameEditor);
				namesTable.invalidate();
				namesTable.validate();
				namesTable.repaint();
			}
		} catch (UnsupportedTableModelException e) {
			new ErrorDialog("Error creating editor", e).showDialog();
		}

	}

	private void namesTableMouseClicked(MouseEvent e) {
		if (readOnly) {
			//do nothing as read only users can't change anything
		} else {
			if (e.getClickCount() == 2) {
				try {
					DomainSortableTable namesTable = getNamesTable();
					DomainEditor nameEditor = new DomainEditor(PatronPublicationsNames.class, this.getParentEditor(), "Name Link", new PatronPublicationNamesFields());
					nameEditor.setCallingTable(namesTable);
					nameEditor.setNavigationButtonListeners(nameEditor);
					editRelatedRecord(namesTable, this.getClass(), true, nameEditor);
				} catch (UnsupportedTableModelException e1) {
					new ErrorDialog("Error creating editor", e1).showDialog();
				}
			}
			namesTable.invalidate();
			namesTable.validate();
			namesTable.repaint();
		}

	}


	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		contentPanel = new JPanel();
		Label_PublicationType = new JLabel();
		publicationType = ATBasicComponentFactory.createComboBox(detailsModel, PatronPublications.PROPERTYNAME_PUBLICATION_TYPE, PatronPublications.class);
		Label_title = new JLabel();
		title = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronPublications.PROPERTYNAME_PUBLICATION_TITLE));
		label_addressTypeOther = new JLabel();
		publicationDate = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronPublications.PROPERTYNAME_PUBLICATION_DATE));
		label_publisher = new JLabel();
		publisher = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronPublications.PROPERTYNAME_PUBLISHER));
		label_collaborators = new JLabel();
		scrollPane48 = new JScrollPane();
		collaborators = ATBasicComponentFactory.createTextArea(detailsModel.getModel(PatronPublications.PROPERTYNAME_COLLABORATORS));
		separator5 = new JSeparator();
		SubjectsLabel = new JLabel();
		scrollPane3 = new JScrollPane();
		subjectsTable = new DomainSortableTable(PatronPublicationsSubjects.class);
		panel10 = new JPanel();
		addSubject = new JButton();
		removeSubject = new JButton();
		scrollPane4 = new JScrollPane();
		namesTable = new DomainSortableTable(PatronPublicationsNames.class);
		panel11 = new JPanel();
		editNameRelationshipButton = new JButton();
		addName = new JButton();
		removeName = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setLayout(new FormLayout(
			"default:grow",
			"fill:default:grow"));

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
					new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
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

			//---- Label_PublicationType ----
			Label_PublicationType.setText("Publication Type");
			ATFieldInfo.assignLabelInfo(Label_PublicationType, PatronPublications.class, PatronPublications.PROPERTYNAME_PUBLICATION_TYPE);
			contentPanel.add(Label_PublicationType, cc.xy(1, 1));

			//---- publicationType ----
			publicationType.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					fundingTypeItemStateChanged(e);
				}
			});
			contentPanel.add(publicationType, cc.xy(3, 1));

			//---- Label_title ----
			Label_title.setText("Title");
			ATFieldInfo.assignLabelInfo(Label_title, PatronPublications.class, PatronPublications.PROPERTYNAME_PUBLICATION_TITLE);
			contentPanel.add(Label_title, cc.xy(1, 3));

			//---- title ----
			title.setColumns(30);
			contentPanel.add(title, cc.xy(3, 3));

			//---- label_addressTypeOther ----
			label_addressTypeOther.setText("Date");
			ATFieldInfo.assignLabelInfo(label_addressTypeOther, PatronPublications.class, PatronPublications.PROPERTYNAME_PUBLICATION_DATE);
			contentPanel.add(label_addressTypeOther, cc.xy(1, 5));

			//---- publicationDate ----
			publicationDate.setColumns(30);
			contentPanel.add(publicationDate, cc.xy(3, 5));

			//---- label_publisher ----
			label_publisher.setText("Publisher");
			ATFieldInfo.assignLabelInfo(label_publisher, PatronPublications.class, PatronPublications.PROPERTYNAME_PUBLISHER);
			contentPanel.add(label_publisher, cc.xy(1, 7));

			//---- publisher ----
			publisher.setColumns(30);
			contentPanel.add(publisher, cc.xy(3, 7));

			//---- label_collaborators ----
			label_collaborators.setText("Collaborators");
			ATFieldInfo.assignLabelInfo(label_collaborators, PatronPublications.class, PatronPublications.PROPERTYNAME_COLLABORATORS);
			contentPanel.add(label_collaborators, cc.xy(1, 9));

			//======== scrollPane48 ========
			{
				scrollPane48.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane48.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane48.setPreferredSize(new Dimension(200, 68));

				//---- collaborators ----
				collaborators.setRows(4);
				collaborators.setLineWrap(true);
				collaborators.setWrapStyleWord(true);
				collaborators.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				collaborators.setMinimumSize(new Dimension(200, 16));
				scrollPane48.setViewportView(collaborators);
			}
			contentPanel.add(scrollPane48, cc.xywh(3, 9, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

			//---- separator5 ----
			separator5.setBackground(new Color(220, 220, 232));
			separator5.setForeground(new Color(147, 131, 86));
			separator5.setMinimumSize(new Dimension(1, 10));
			separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			contentPanel.add(separator5, cc.xywh(1, 11, 3, 1));

			//---- SubjectsLabel ----
			SubjectsLabel.setText("Subjects");
			SubjectsLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			contentPanel.add(SubjectsLabel, cc.xywh(1, 13, 3, 1));

			//======== scrollPane3 ========
			{
				scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane3.setPreferredSize(new Dimension(219, 100));

				//---- subjectsTable ----
				subjectsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
				scrollPane3.setViewportView(subjectsTable);
			}
			contentPanel.add(scrollPane3, cc.xywh(1, 15, 3, 1));

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
				addSubject.setText("Add Subject Link");
				addSubject.setOpaque(false);
				addSubject.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				addSubject.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addSubjectActionPerformed();
					}
				});
				panel10.add(addSubject, cc.xy(1, 1));

				//---- removeSubject ----
				removeSubject.setText("Remove Subject Link");
				removeSubject.setOpaque(false);
				removeSubject.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				removeSubject.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeSubjectActionPerformed();
					}
				});
				panel10.add(removeSubject, cc.xy(3, 1));
			}
			contentPanel.add(panel10, cc.xywh(1, 17, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

			//======== scrollPane4 ========
			{
				scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				scrollPane4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				scrollPane4.setPreferredSize(new Dimension(219, 100));

				//---- namesTable ----
				namesTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
				namesTable.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						namesTableMouseClicked(e);
					}
				});
				scrollPane4.setViewportView(namesTable);
			}
			contentPanel.add(scrollPane4, cc.xywh(1, 19, 3, 1));

			//======== panel11 ========
			{
				panel11.setBackground(new Color(231, 188, 251));
				panel11.setOpaque(false);
				panel11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel11.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- editNameRelationshipButton ----
				editNameRelationshipButton.setText("Edit Name Link");
				editNameRelationshipButton.setOpaque(false);
				editNameRelationshipButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				editNameRelationshipButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						editNameRelationshipButtonActionPerformed();
					}
				});
				panel11.add(editNameRelationshipButton, cc.xy(1, 1));

				//---- addName ----
				addName.setText("Add Name Link");
				addName.setOpaque(false);
				addName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				addName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addNameActionPerformed();
					}
				});
				panel11.add(addName, cc.xy(3, 1));

				//---- removeName ----
				removeName.setText("Remove Name Link");
				removeName.setOpaque(false);
				removeName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				removeName.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeNameActionPerformed();
					}
				});
				panel11.add(removeName, cc.xy(5, 1));
			}
			contentPanel.add(panel11, cc.xywh(1, 21, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
		}
		add(contentPanel, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel contentPanel;
	private JLabel Label_PublicationType;
	private JComboBox publicationType;
	private JLabel Label_title;
	private JTextField title;
	private JLabel label_addressTypeOther;
	private JTextField publicationDate;
	private JLabel label_publisher;
	private JTextField publisher;
	private JLabel label_collaborators;
	private JScrollPane scrollPane48;
	public JTextArea collaborators;
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
	private JButton editNameRelationshipButton;
	private JButton addName;
	private JButton removeName;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * Set the domain model for this editor.
	 *
	 * @param model the model
	 */
	@Override
	public void setModel(DomainObject model, InfiniteProgressPanel progressPanel) {
		super.setModel(model, progressPanel);
		PatronPublications publicationModel = (PatronPublications)model;
		subjectsTable.updateCollection(publicationModel.getSubjects());
		namesTable.updateCollection(publicationModel.getNames());
	}

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

	private void initAccess() {
		//set form to read only for reference staff
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_BEGINNING_DATA_ENTRY)) {
			readOnly = true;
			setFormToReadOnly();
			publicationType.setEnabled(false);
			//subject buttons
			addSubject.setEnabled(false);
			removeSubject.setEnabled(false);
			//name buttons
			addName.setEnabled(false);
			removeName.setEnabled(false);
			editNameRelationshipButton.setEnabled(false);
		}
	}

}
