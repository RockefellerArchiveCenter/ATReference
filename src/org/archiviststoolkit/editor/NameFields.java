/**
 * Archivists' Toolkit(TM) Copyright � 2005-2007 Regents of the University of California, New York University, & Five Colleges, Inc.
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
 * Created by JFormDesigner on Sat Aug 27 11:09:03 EDT 2005
 */

package org.archiviststoolkit.editor;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainSortableTable;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;
import org.archiviststoolkit.ApplicationFrame;

public class NameFields extends DomainEditorFields {
    private ArrayList<ATPlugin> plugins = null; // stores any embedded editor plugins

    public NameFields() {
        super();
        initComponents();
		getNameContactNotesTable().setTransferable();
        initAccess();
        currentPrimaryNamePanel = detailsPanel;
        initPlugins();
    }

    private void addNote(String whereString) {

        Names namesModel = (Names) super.getModel();
        NameContactNotes newNote;
		DomainEditor dialog = null;
		try {
			dialog = DomainEditorFactory.getInstance()
                .createDomainEditorWithParent(NameContactNotes.class, this.getParentEditor(), false);
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for NameContactNotes", e).showDialog();

		}
		dialog.setNewRecord(true);
        Boolean done = false;
        int sequenceNumber = 0;
        Boolean first = true;
        int returnStatus;

        while (!done) {
            newNote = new NameContactNotes(namesModel);
            if (first) {
                sequenceNumber = SequencedObjectsUtils.determineSequenceOfNewItem(whereString, nameContactNotesTable);
                first = false;
            } else {
                sequenceNumber++;
            }
            newNote.setSequenceNumber(sequenceNumber);
            dialog.setModel(newNote, null);
            returnStatus = dialog.showDialog();
            if (returnStatus == javax.swing.JOptionPane.OK_OPTION) {
                namesModel.addNameContactNote(newNote);
                nameContactNotesTable.getEventList().add(newNote);
                done = true;
            } else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
                namesModel.addNameContactNote(newNote);
                nameContactNotesTable.getEventList().add(newNote);
            } else {
                done = true;
            }
        }
        dialog.setNewRecord(false);

    }

    private void removeContactNoteButtonActionPerformed() {
		try {
			removeContactNote();
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Contact Note not removed", e).showDialog();
		}
	}

    private void nameContactNotesTableMousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            insertContactNotePopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void nameContactNotesTableMouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            insertContactNotePopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void addNoteButtonActionPerformed() {
        if (nameContactNotesTable.getSelectedRow() == -1) {
            addNote(SequencedObjectsUtils.ADD_AT_END);
        } else {
            addNote(SequencedObjectsUtils.ADD_ABOVE_SELECTION);
        }
    }

    public JButton getAddNoteButton() {
        return addNoteButton;
    }

    private void nonPreferredNamesTableMouseClicked(MouseEvent e) {
        handleTableMouseClick(e, nonPreferredNamesTable, NonPreferredNames.class);
    }

    private void nameContactNotesTableMouseClicked(MouseEvent e) {
        handleTableMouseClick(e, nameContactNotesTable, NameContactNotes.class);
    }

    private void addNonPreferredNameButtonActionPerformed() {
        Names namesModel = (Names) this.getModel();
        NonPreferredNames newNonPreferredName;
		DomainEditor dialogNonPreferredNames = null;
		try {
			dialogNonPreferredNames = DomainEditorFactory.getInstance().createDomainEditorWithParent(NonPreferredNames.class, this.getParentEditor(), false);
		} catch (DomainEditorCreationException e) {
			new ErrorDialog(getParentEditor(), "Error creating editor for NonPreferredNames", e).showDialog();

		}
		dialogNonPreferredNames.setNewRecord(true);

        int returnStatus;
        Boolean done = false;
        while (!done) {
            newNonPreferredName = new NonPreferredNames(namesModel);
            newNonPreferredName.setNameType(namesModel.getNameType());
            dialogNonPreferredNames.setModel(newNonPreferredName, null);
            returnStatus = dialogNonPreferredNames.showDialog();
            if (returnStatus == JOptionPane.OK_OPTION) {
                namesModel.addNonPreferredName(newNonPreferredName);
                nonPreferredNamesTable.getEventList().add(newNonPreferredName);
                done = true;
            } else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
                namesModel.addNonPreferredName(newNonPreferredName);
                nonPreferredNamesTable.getEventList().add(newNonPreferredName);
            } else {
                done = true;
            }
        }
    }

    private void removeNonPreferredNameButtonActionPerformed() {
		try {
			this.removeRelatedTableRow(nonPreferredNamesTable, null, super.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Non-preferred name not removed", e).showDialog();
		}
		nonPreferredNamesTable.updateCollection(((Names) super.getModel()).getNonPreferredNames());
    }

	protected int editRelatedRecord(DomainGlazedListTable table, Class clazz, Boolean buffered, DomainEditor domainEditor) {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			DomainObject domainObject = table.getSortedList().get(selectedRow);
			if (domainEditor == null) {
				try {
					domainEditor = DomainEditorFactory.getInstance()
						.createDomainEditorWithParent(clazz, this.getParentEditor(), false);
					domainEditor.setCallingTable(table);
				} catch (UnsupportedTableModelException e1) {
					new ErrorDialog(this.getParentEditor(), "Error creating editor for " + clazz.getSimpleName(), e1).showDialog();
				} catch (DomainEditorCreationException e) {
					new ErrorDialog(this.getParentEditor(), "Error creating editor for " + clazz.getSimpleName(), e).showDialog();
				}
			}
			domainEditor.setBuffered(buffered);
			domainEditor.setSelectedRow(selectedRow);
			domainEditor.setNavigationButtons();
			domainEditor.setModel(domainObject, null);
			int returnValue =  domainEditor.showDialog();
			if (domainEditor.getBuffered()) {
				if (returnValue == JOptionPane.CANCEL_OPTION) {
					domainEditor.editorFields.cancelEdit();
				} else {
					domainEditor.editorFields.acceptEdit();
                    ApplicationFrame.getInstance().setRecordDirty(); // ok an edit was made, so set the record dirty
				}
			}
			return returnValue;
		} else {
			return JOptionPane.CANCEL_OPTION;
		}
	}


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		sortNameDisplay = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_SORT_NAME));
		tabbedPane = new JTabbedPane();
		detailsContainer = new JPanel();
		detailsPanel = new JPanel();
		separator3 = new JSeparator();
		descriptionPanel = new JPanel();
		label_nameDescriptionNote2 = new JLabel();
		nameDescriptionType = ATBasicComponentFactory.createComboBox(detailsModel, Names.PROPERTYNAME_DESCRIPTION_TYPE, Names.class);
		label_nameDescriptionNote = new JLabel();
		scrollPane2 = new JScrollPane();
		nameDescriptionNote = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Names.PROPERTYNAME_DESCRIPTION_NOTE));
		label_nameCitation = new JLabel();
		scrollPane23 = new JScrollPane();
		nameCitation = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Names.PROPERTYNAME_CITATION));
		nonPreferredNamePanel = new JPanel();
		label_subjectScopeNote4 = new JLabel();
		scrollPane1 = new JScrollPane();
		nonPreferredNamesTable = new DomainSortableTable(NonPreferredNames.class, NonPreferredNames.PROPERTYNAME_SORT_NAME);
		panel1 = new JPanel();
		addNonPreferredNameButton = new JButton();
		removeNonPreferredNameButton = new JButton();
		separator1 = new JSeparator();
		label_subjectScopeNote3 = new JLabel();
		scrollPane5 = new JScrollPane();
		accessionsTable = new DomainSortableTable(Accessions.class, Accessions.PROPERTYNAME_ACCESSION_NUMBER);
		separator2 = new JSeparator();
		label_subjectScopeNote2 = new JLabel();
		scrollPane4 = new JScrollPane();
		resourcesTable = new ResourceAndComponentLinkTable(Resources.class, Resources.PROPERTYNAME_RESOURCE_IDENTIFIER);
		label1 = new JLabel();
		separator4 = new JSeparator();
		label_subjectScopeNote5 = new JLabel();
		scrollPane6 = new JScrollPane();
		digitalObjectsTable = new DomainSortableTable(DigitalObjects.class, DigitalObjects.PROPERTYNAME_METS_IDENTIFIER);
		contactInfoPanel = new JPanel();
		label_nameContactAddress3 = new JLabel();
		salutation = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_SALUTATION));
		label_nameContactAddress1 = new JLabel();
		nameContactAddress1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_ADDRESS_1));
		label_nameContactAddress2 = new JLabel();
		nameContactAddress2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_ADDRESS_2));
		label_nameContactCity = new JLabel();
		nameContactCity = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_CITY));
		label_nameContactRegion = new JLabel();
		nameContactRegion = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_REGION));
		label_nameContactMailCode = new JLabel();
		nameContactMailCode = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_MAIL_CODE));
		label_nameContactCountry = new JLabel();
		nameContactCountry = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_COUNTRY));
		label_nameContactPhone = new JLabel();
		nameContactPhone = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_PHONE));
		label_nameContactFax = new JLabel();
		nameContactFax = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_FAX));
		label_nameContactEmail = new JLabel();
		nameContactEmail = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_EMAIL));
		label_nameContactName = new JLabel();
		nameContactName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_CONTACT_NAME));
		label_nameContactNotes = new JLabel();
		scrollPane3 = new JScrollPane();
		nameContactNotesTable = new DomainSortedTable(NameContactNotes.class);
		panel2 = new JPanel();
		addNoteButton = new JButton();
		removeContactNoteButton = new JButton();
		panel31 = new JPanel();
		panel32 = new JPanel();
		label_acknowledgementDate2 = new JLabel();
		acknowledgementDate2 = ATBasicComponentFactory.createDateField(detailsModel.getModel( Names.PROPERTYNAME_USER_DEFINED_DATE1));
		label_acknowledgementDate3 = new JLabel();
		acknowledgementDate3 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_DATE2));
		rights2 = ATBasicComponentFactory.createCheckBox(detailsModel, Names.PROPERTYNAME_USER_DEFINED_BOOLEAN1, Names.class);
		rights3 = ATBasicComponentFactory.createCheckBox(detailsModel, Names.PROPERTYNAME_USER_DEFINED_BOOLEAN2, Names.class);
		label_date1Begin2 = new JLabel();
		date1Begin2 = ATBasicComponentFactory.createIntegerField(detailsModel,Names.PROPERTYNAME_USER_DEFINED_INTEGER1);
		label_date1Begin3 = new JLabel();
		date1Begin3 = ATBasicComponentFactory.createIntegerField(detailsModel, Names.PROPERTYNAME_USER_DEFINED_INTEGER2);
		label_date1Begin4 = new JLabel();
		extentNumber2 = ATBasicComponentFactory.createDoubleField(detailsModel, Names.PROPERTYNAME_USER_DEFINED_REAL1);
		label_date1Begin5 = new JLabel();
		extentNumber3 = ATBasicComponentFactory.createDoubleField(detailsModel, Names.PROPERTYNAME_USER_DEFINED_REAL2);
		label_date1Begin6 = new JLabel();
		dateExpression2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_STRING1),false);
		label_date1Begin7 = new JLabel();
		dateExpression3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_STRING2),false);
		label_date1Begin8 = new JLabel();
		dateExpression4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_STRING3),false);
		label_date1Begin9 = new JLabel();
		scrollPane44 = new JScrollPane();
		title3 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_TEXT1));
		panel35 = new JPanel();
		label_date1Begin10 = new JLabel();
		scrollPane45 = new JScrollPane();
		title4 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_TEXT2));
		label_date1Begin11 = new JLabel();
		scrollPane46 = new JScrollPane();
		title5 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_TEXT3));
		label_date1Begin12 = new JLabel();
		scrollPane47 = new JScrollPane();
		title6 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Names.PROPERTYNAME_USER_DEFINED_TEXT4));
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setBackground(new Color(200, 205, 232));
		setPreferredSize(new Dimension(900, 500));
		setBorder(Borders.DLU4_BORDER);
		setLayout(new FormLayout(
			"default:grow",
			"default, fill:default:grow"));

		//---- sortNameDisplay ----
		sortNameDisplay.setEditable(false);
		sortNameDisplay.setBorder(null);
		sortNameDisplay.setForeground(new Color(0, 0, 102));
		sortNameDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		sortNameDisplay.setSelectionColor(new Color(204, 0, 51));
		sortNameDisplay.setOpaque(false);
		add(sortNameDisplay, cc.xy(1, 1));

		//======== tabbedPane ========
		{
			tabbedPane.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			tabbedPane.setBackground(new Color(200, 205, 232));
			tabbedPane.setOpaque(true);

			//======== detailsContainer ========
			{
				detailsContainer.setBackground(new Color(200, 205, 232));
				detailsContainer.setLayout(new FormLayout(
					ColumnSpec.decodeSpecs("default:grow"),
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//======== detailsPanel ========
				{
					detailsPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					detailsPanel.setBackground(new Color(200, 205, 232));
					detailsPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.NO_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec("max(default;300px):grow"),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
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
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));
				}
				detailsContainer.add(detailsPanel, cc.xy(1, 1));

				//---- separator3 ----
				separator3.setBackground(new Color(220, 220, 232));
				separator3.setForeground(new Color(147, 131, 86));
				separator3.setMinimumSize(new Dimension(1, 10));
				separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				detailsContainer.add(separator3, cc.xy(1, 3));

				//======== descriptionPanel ========
				{
					descriptionPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					descriptionPanel.setBackground(new Color(200, 205, 232));
					descriptionPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW)
						}));

					//---- label_nameDescriptionNote2 ----
					label_nameDescriptionNote2.setText("Name Description Type");
					label_nameDescriptionNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_nameDescriptionNote2, Names.class, Names.PROPERTYNAME_DESCRIPTION_TYPE);
					descriptionPanel.add(label_nameDescriptionNote2, cc.xy(1, 1));

					//---- nameDescriptionType ----
					nameDescriptionType.setOpaque(false);
					nameDescriptionType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					descriptionPanel.add(nameDescriptionType, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_nameDescriptionNote ----
					label_nameDescriptionNote.setText("Description Note");
					label_nameDescriptionNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_nameDescriptionNote, Names.class, Names.PROPERTYNAME_DESCRIPTION_NOTE);
					descriptionPanel.add(label_nameDescriptionNote, cc.xy(1, 3));

					//======== scrollPane2 ========
					{
						scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

						//---- nameDescriptionNote ----
						nameDescriptionNote.setRows(8);
						nameDescriptionNote.setLineWrap(true);
						nameDescriptionNote.setWrapStyleWord(true);
						nameDescriptionNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						scrollPane2.setViewportView(nameDescriptionNote);
					}
					descriptionPanel.add(scrollPane2, cc.xy(3, 3));

					//---- label_nameCitation ----
					label_nameCitation.setText("Citation(s)");
					label_nameCitation.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_nameCitation, Names.class, Names.PROPERTYNAME_CITATION);
					descriptionPanel.add(label_nameCitation, cc.xy(1, 5));

					//======== scrollPane23 ========
					{
						scrollPane23.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane23.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

						//---- nameCitation ----
						nameCitation.setRows(8);
						nameCitation.setLineWrap(true);
						nameCitation.setWrapStyleWord(true);
						nameCitation.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						scrollPane23.setViewportView(nameCitation);
					}
					descriptionPanel.add(scrollPane23, cc.xy(3, 5));
				}
				detailsContainer.add(descriptionPanel, cc.xy(1, 5));
			}
			tabbedPane.addTab("Details", detailsContainer);


			//======== nonPreferredNamePanel ========
			{
				nonPreferredNamePanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.setBackground(new Color(200, 205, 232));
				nonPreferredNamePanel.setLayout(new FormLayout(
					ColumnSpec.decodeSpecs("default:grow"),
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label_subjectScopeNote4 ----
				label_subjectScopeNote4.setText("Non-Preferred Forms");
				label_subjectScopeNote4.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(label_subjectScopeNote4, cc.xywh(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane1 ========
				{
					scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- nonPreferredNamesTable ----
					nonPreferredNamesTable.setPreferredScrollableViewportSize(new Dimension(450, 120));
					nonPreferredNamesTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							nonPreferredNamesTableMouseClicked(e);
						}
					});
					scrollPane1.setViewportView(nonPreferredNamesTable);
				}
				nonPreferredNamePanel.add(scrollPane1, cc.xywh(1, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.FILL));

				//======== panel1 ========
				{
					panel1.setBackground(new Color(231, 188, 251));
					panel1.setOpaque(false);
					panel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel1.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- addNonPreferredNameButton ----
					addNonPreferredNameButton.setText("Add Non-Preferred Form");
					addNonPreferredNameButton.setOpaque(false);
					addNonPreferredNameButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addNonPreferredNameButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addNonPreferredNameButtonActionPerformed();
						}
					});
					panel1.add(addNonPreferredNameButton, cc.xy(1, 1));

					//---- removeNonPreferredNameButton ----
					removeNonPreferredNameButton.setText("Remove Non-Preferred Form");
					removeNonPreferredNameButton.setOpaque(false);
					removeNonPreferredNameButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeNonPreferredNameButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeNonPreferredNameButtonActionPerformed();
						}
					});
					panel1.add(removeNonPreferredNameButton, cc.xy(3, 1));
				}
				nonPreferredNamePanel.add(panel1, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator1 ----
				separator1.setBackground(new Color(220, 220, 232));
				separator1.setForeground(new Color(147, 131, 86));
				separator1.setMinimumSize(new Dimension(1, 10));
				separator1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(separator1, cc.xy(1, 7));

				//---- label_subjectScopeNote3 ----
				label_subjectScopeNote3.setText("Accessions");
				label_subjectScopeNote3.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(label_subjectScopeNote3, cc.xywh(1, 9, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane5 ========
				{
					scrollPane5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- accessionsTable ----
					accessionsTable.setPreferredScrollableViewportSize(new Dimension(450, 120));
					scrollPane5.setViewportView(accessionsTable);
				}
				nonPreferredNamePanel.add(scrollPane5, cc.xy(1, 11));

				//---- separator2 ----
				separator2.setBackground(new Color(220, 220, 232));
				separator2.setForeground(new Color(147, 131, 86));
				separator2.setMinimumSize(new Dimension(1, 10));
				separator2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(separator2, cc.xy(1, 13));

				//---- label_subjectScopeNote2 ----
				label_subjectScopeNote2.setText("Resources");
				label_subjectScopeNote2.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(label_subjectScopeNote2, cc.xy(1, 15));

				//======== scrollPane4 ========
				{
					scrollPane4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- resourcesTable ----
					resourcesTable.setPreferredScrollableViewportSize(new Dimension(450, 120));
					scrollPane4.setViewportView(resourcesTable);
				}
				nonPreferredNamePanel.add(scrollPane4, cc.xy(1, 17));

				//---- label1 ----
				label1.setText("Resources in Red have the subject term linked at the component level");
				label1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(label1, cc.xy(1, 19));

				//---- separator4 ----
				separator4.setBackground(new Color(220, 220, 232));
				separator4.setForeground(new Color(147, 131, 86));
				separator4.setMinimumSize(new Dimension(1, 10));
				separator4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(separator4, cc.xy(1, 21));

				//---- label_subjectScopeNote5 ----
				label_subjectScopeNote5.setText("Digital Objects");
				label_subjectScopeNote5.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(label_subjectScopeNote5, cc.xy(1, 23));

				//======== scrollPane6 ========
				{
					scrollPane6.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- digitalObjectsTable ----
					digitalObjectsTable.setPreferredScrollableViewportSize(new Dimension(450, 120));
					scrollPane6.setViewportView(digitalObjectsTable);
				}
				nonPreferredNamePanel.add(scrollPane6, cc.xy(1, 25));
			}
			tabbedPane.addTab("Non-Preferred Forms, Accessions, Resources & Digital Objects", nonPreferredNamePanel);


			//======== contactInfoPanel ========
			{
				contactInfoPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.setBackground(new Color(200, 205, 232));
				contactInfoPanel.setBorder(Borders.DLU2_BORDER);
				contactInfoPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec("max(default;100px):grow"),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
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
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label_nameContactAddress3 ----
				label_nameContactAddress3.setText("Salutation");
				label_nameContactAddress3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactAddress3, Names.class, Names.PROPERTYNAME_SALUTATION);
				contactInfoPanel.add(label_nameContactAddress3, cc.xy(1, 1));

				//---- salutation ----
				salutation.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(salutation, cc.xywh(3, 1, 9, 1));

				//---- label_nameContactAddress1 ----
				label_nameContactAddress1.setText("Address");
				label_nameContactAddress1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactAddress1, Names.class, Names.PROPERTYNAME_CONTACT_ADDRESS_1);
				contactInfoPanel.add(label_nameContactAddress1, cc.xy(1, 3));

				//---- nameContactAddress1 ----
				nameContactAddress1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactAddress1, cc.xywh(3, 3, 9, 1));

				//---- label_nameContactAddress2 ----
				label_nameContactAddress2.setText("Address");
				label_nameContactAddress2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactAddress2, Names.class, Names.PROPERTYNAME_CONTACT_ADDRESS_2);
				contactInfoPanel.add(label_nameContactAddress2, cc.xy(1, 5));

				//---- nameContactAddress2 ----
				nameContactAddress2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactAddress2, cc.xywh(3, 5, 9, 1));

				//---- label_nameContactCity ----
				label_nameContactCity.setText("City");
				label_nameContactCity.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactCity, Names.class, Names.PROPERTYNAME_CONTACT_CITY);
				contactInfoPanel.add(label_nameContactCity, cc.xy(1, 7));

				//---- nameContactCity ----
				nameContactCity.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactCity, cc.xy(3, 7));

				//---- label_nameContactRegion ----
				label_nameContactRegion.setText("Region");
				label_nameContactRegion.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactRegion, Names.class, Names.PROPERTYNAME_CONTACT_REGION);
				contactInfoPanel.add(label_nameContactRegion, cc.xy(5, 7));

				//---- nameContactRegion ----
				nameContactRegion.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactRegion, cc.xy(7, 7));

				//---- label_nameContactMailCode ----
				label_nameContactMailCode.setText("Mail Code");
				label_nameContactMailCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactMailCode, Names.class, Names.PROPERTYNAME_CONTACT_MAIL_CODE);
				contactInfoPanel.add(label_nameContactMailCode, cc.xy(9, 7));

				//---- nameContactMailCode ----
				nameContactMailCode.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactMailCode, cc.xy(11, 7));

				//---- label_nameContactCountry ----
				label_nameContactCountry.setText("Country");
				label_nameContactCountry.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactCountry, Names.class, Names.PROPERTYNAME_CONTACT_COUNTRY);
				contactInfoPanel.add(label_nameContactCountry, cc.xy(1, 9));

				//---- nameContactCountry ----
				nameContactCountry.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactCountry, cc.xywh(3, 9, 9, 1));

				//---- label_nameContactPhone ----
				label_nameContactPhone.setText("Telephone");
				label_nameContactPhone.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactPhone, Names.class, Names.PROPERTYNAME_CONTACT_PHONE);
				contactInfoPanel.add(label_nameContactPhone, cc.xy(1, 11));

				//---- nameContactPhone ----
				nameContactPhone.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactPhone, cc.xywh(3, 11, 5, 1));

				//---- label_nameContactFax ----
				label_nameContactFax.setText("FAX");
				label_nameContactFax.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactFax, Names.class, Names.PROPERTYNAME_CONTACT_FAX);
				contactInfoPanel.add(label_nameContactFax, cc.xy(9, 11));

				//---- nameContactFax ----
				nameContactFax.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactFax, cc.xy(11, 11));

				//---- label_nameContactEmail ----
				label_nameContactEmail.setText("e-mail");
				label_nameContactEmail.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactEmail, Names.class, Names.PROPERTYNAME_CONTACT_EMAIL);
				contactInfoPanel.add(label_nameContactEmail, cc.xy(1, 13));

				//---- nameContactEmail ----
				nameContactEmail.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactEmail, cc.xywh(3, 13, 9, 1));

				//---- label_nameContactName ----
				label_nameContactName.setText("Contact");
				label_nameContactName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_nameContactName, Names.class, Names.PROPERTYNAME_CONTACT_NAME);
				contactInfoPanel.add(label_nameContactName, cc.xy(1, 15));

				//---- nameContactName ----
				nameContactName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(nameContactName, cc.xywh(3, 15, 9, 1));

				//---- label_nameContactNotes ----
				label_nameContactNotes.setText("Notes");
				label_nameContactNotes.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contactInfoPanel.add(label_nameContactNotes, cc.xywh(1, 17, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane3 ========
				{
					scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- nameContactNotesTable ----
					nameContactNotesTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					nameContactNotesTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent e) {
							nameContactNotesTableMousePressed(e);
						}
						@Override
						public void mouseReleased(MouseEvent e) {
							nameContactNotesTableMouseReleased(e);
						}
						@Override
						public void mouseClicked(MouseEvent e) {
							nameContactNotesTableMouseClicked(e);
						}
					});
					scrollPane3.setViewportView(nameContactNotesTable);
				}
				contactInfoPanel.add(scrollPane3, cc.xywh(3, 17, 9, 1));

				//======== panel2 ========
				{
					panel2.setBackground(new Color(231, 188, 251));
					panel2.setOpaque(false);
					panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel2.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- addNoteButton ----
					addNoteButton.setText("Add Note");
					addNoteButton.setOpaque(false);
					addNoteButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addNoteButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addNoteButtonActionPerformed();
						}
					});
					panel2.add(addNoteButton, cc.xy(1, 1));

					//---- removeContactNoteButton ----
					removeContactNoteButton.setText("Remove Contact Note");
					removeContactNoteButton.setOpaque(false);
					removeContactNoteButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeContactNoteButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeContactNoteButtonActionPerformed();
						}
					});
					panel2.add(removeContactNoteButton, cc.xy(3, 1));
				}
				contactInfoPanel.add(panel2, cc.xywh(3, 19, 9, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
			}
			tabbedPane.addTab("Contact Info", contactInfoPanel);


			//======== panel31 ========
			{
				panel31.setBackground(new Color(200, 205, 232));
				panel31.setLayout(new FormLayout(
					new ColumnSpec[] {
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					RowSpec.decodeSpecs("fill:default:grow")));
				((FormLayout)panel31.getLayout()).setColumnGroups(new int[][] {{1, 3}});

				//======== panel32 ========
				{
					panel32.setOpaque(false);
					panel32.setLayout(new FormLayout(
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
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						}));

					//---- label_acknowledgementDate2 ----
					label_acknowledgementDate2.setText("User Defined Date 1");
					label_acknowledgementDate2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_acknowledgementDate2, Names.class, Names.PROPERTYNAME_USER_DEFINED_DATE1);
					panel32.add(label_acknowledgementDate2, cc.xy(1, 1));

					//---- acknowledgementDate2 ----
					acknowledgementDate2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					acknowledgementDate2.setColumns(10);
					panel32.add(acknowledgementDate2, cc.xywh(3, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_acknowledgementDate3 ----
					label_acknowledgementDate3.setText("User Defined Date 2");
					label_acknowledgementDate3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_acknowledgementDate3, Names.class, Names.PROPERTYNAME_USER_DEFINED_DATE2);
					panel32.add(label_acknowledgementDate3, cc.xy(1, 3));

					//---- acknowledgementDate3 ----
					acknowledgementDate3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					acknowledgementDate3.setColumns(10);
					panel32.add(acknowledgementDate3, cc.xywh(3, 3, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- rights2 ----
					rights2.setText("User Defined Boolean 1");
					rights2.setOpaque(false);
					rights2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					rights2.setText(ATFieldInfo.getLabel(Names.class, Names.PROPERTYNAME_USER_DEFINED_BOOLEAN1));
					panel32.add(rights2, cc.xywh(1, 5, 3, 1));

					//---- rights3 ----
					rights3.setText("User Defined Boolean 2");
					rights3.setOpaque(false);
					rights3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					rights3.setText(ATFieldInfo.getLabel(Accessions.class, Accessions.PROPERTYNAME_USER_DEFINED_BOOLEAN2));
					panel32.add(rights3, cc.xywh(1, 7, 3, 1));

					//---- label_date1Begin2 ----
					label_date1Begin2.setText("User Defined Integer 1");
					label_date1Begin2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin2, Names.class, Names.PROPERTYNAME_USER_DEFINED_INTEGER1);
					panel32.add(label_date1Begin2, cc.xy(1, 9));

					//---- date1Begin2 ----
					date1Begin2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					date1Begin2.setColumns(6);
					panel32.add(date1Begin2, cc.xywh(3, 9, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin3 ----
					label_date1Begin3.setText("User Defined Integer 2");
					label_date1Begin3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin3, Names.class, Names.PROPERTYNAME_USER_DEFINED_INTEGER2);
					panel32.add(label_date1Begin3, cc.xy(1, 11));

					//---- date1Begin3 ----
					date1Begin3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					date1Begin3.setColumns(6);
					panel32.add(date1Begin3, cc.xywh(3, 11, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin4 ----
					label_date1Begin4.setText("User Defined Real 1");
					label_date1Begin4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin4,Names.class, Names.PROPERTYNAME_USER_DEFINED_REAL1);
					panel32.add(label_date1Begin4, cc.xy(1, 13));

					//---- extentNumber2 ----
					extentNumber2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					extentNumber2.setColumns(5);
					panel32.add(extentNumber2, cc.xywh(3, 13, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin5 ----
					label_date1Begin5.setText("User Defined Real 2");
					label_date1Begin5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin5, Names.class, Names.PROPERTYNAME_USER_DEFINED_REAL2);
					panel32.add(label_date1Begin5, cc.xy(1, 15));

					//---- extentNumber3 ----
					extentNumber3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					extentNumber3.setColumns(5);
					panel32.add(extentNumber3, cc.xywh(3, 15, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin6 ----
					label_date1Begin6.setText("User Defined String 1");
					label_date1Begin6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin6, Names.class, Names.PROPERTYNAME_USER_DEFINED_STRING1);
					panel32.add(label_date1Begin6, cc.xy(1, 17));

					//---- dateExpression2 ----
					dateExpression2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel32.add(dateExpression2, new CellConstraints(3, 17, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));

					//---- label_date1Begin7 ----
					label_date1Begin7.setText("User Defined String 2");
					label_date1Begin7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin7, Names.class, Names.PROPERTYNAME_USER_DEFINED_STRING2);
					panel32.add(label_date1Begin7, cc.xy(1, 19));

					//---- dateExpression3 ----
					dateExpression3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel32.add(dateExpression3, new CellConstraints(3, 19, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));

					//---- label_date1Begin8 ----
					label_date1Begin8.setText("User Defined String 3");
					label_date1Begin8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin8, Names.class, Names.PROPERTYNAME_USER_DEFINED_STRING3);
					panel32.add(label_date1Begin8, cc.xy(1, 21));

					//---- dateExpression4 ----
					dateExpression4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel32.add(dateExpression4, new CellConstraints(3, 21, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 0, 0, 5)));

					//---- label_date1Begin9 ----
					label_date1Begin9.setText("User Defined Text 1");
					label_date1Begin9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin9, Names.class, Names.PROPERTYNAME_USER_DEFINED_TEXT1);
					panel32.add(label_date1Begin9, cc.xy(1, 23));

					//======== scrollPane44 ========
					{
						scrollPane44.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane44.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						scrollPane44.setPreferredSize(new Dimension(200, 68));

						//---- title3 ----
						title3.setRows(4);
						title3.setLineWrap(true);
						title3.setWrapStyleWord(true);
						title3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						title3.setMinimumSize(new Dimension(200, 16));
						scrollPane44.setViewportView(title3);
					}
					panel32.add(scrollPane44, cc.xywh(1, 25, 3, 1));
				}
				panel31.add(panel32, cc.xy(1, 1));

				//======== panel35 ========
				{
					panel35.setOpaque(false);
					panel35.setLayout(new FormLayout(
						ColumnSpec.decodeSpecs("default:grow"),
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						}));

					//---- label_date1Begin10 ----
					label_date1Begin10.setText("User Defined Text 2");
					label_date1Begin10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin10, Names.class, Names.PROPERTYNAME_USER_DEFINED_TEXT2);
					panel35.add(label_date1Begin10, cc.xy(1, 1));

					//======== scrollPane45 ========
					{
						scrollPane45.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane45.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						scrollPane45.setPreferredSize(new Dimension(200, 68));

						//---- title4 ----
						title4.setRows(4);
						title4.setLineWrap(true);
						title4.setWrapStyleWord(true);
						title4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						title4.setMinimumSize(new Dimension(200, 16));
						scrollPane45.setViewportView(title4);
					}
					panel35.add(scrollPane45, cc.xy(1, 3));

					//---- label_date1Begin11 ----
					label_date1Begin11.setText("User Defined Text 3");
					label_date1Begin11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin11, Names.class, Names.PROPERTYNAME_USER_DEFINED_TEXT3);
					panel35.add(label_date1Begin11, cc.xy(1, 5));

					//======== scrollPane46 ========
					{
						scrollPane46.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane46.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						scrollPane46.setPreferredSize(new Dimension(200, 68));

						//---- title5 ----
						title5.setRows(4);
						title5.setLineWrap(true);
						title5.setWrapStyleWord(true);
						title5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						title5.setMinimumSize(new Dimension(200, 16));
						scrollPane46.setViewportView(title5);
					}
					panel35.add(scrollPane46, cc.xy(1, 7));

					//---- label_date1Begin12 ----
					label_date1Begin12.setText("User Defined Text 4");
					label_date1Begin12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin12, Names.class, Names.PROPERTYNAME_USER_DEFINED_TEXT4);
					panel35.add(label_date1Begin12, cc.xy(1, 9));

					//======== scrollPane47 ========
					{
						scrollPane47.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane47.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						scrollPane47.setPreferredSize(new Dimension(200, 68));

						//---- title6 ----
						title6.setRows(4);
						title6.setLineWrap(true);
						title6.setWrapStyleWord(true);
						title6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						title6.setMinimumSize(new Dimension(200, 16));
						scrollPane47.setViewportView(title6);
					}
					panel35.add(scrollPane47, cc.xy(1, 11));
				}
				panel31.add(panel35, cc.xy(3, 1));
			}
			tabbedPane.addTab("User defined fields", panel31);

		}
		add(tabbedPane, cc.xy(1, 2));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JTextField sortNameDisplay;
	private JTabbedPane tabbedPane;
	private JPanel detailsContainer;
	private JPanel detailsPanel;
	private JSeparator separator3;
	private JPanel descriptionPanel;
	private JLabel label_nameDescriptionNote2;
	public JComboBox nameDescriptionType;
	private JLabel label_nameDescriptionNote;
	private JScrollPane scrollPane2;
	public JTextArea nameDescriptionNote;
	private JLabel label_nameCitation;
	private JScrollPane scrollPane23;
	public JTextArea nameCitation;
	private JPanel nonPreferredNamePanel;
	private JLabel label_subjectScopeNote4;
	private JScrollPane scrollPane1;
	private DomainSortableTable nonPreferredNamesTable;
	private JPanel panel1;
	private JButton addNonPreferredNameButton;
	private JButton removeNonPreferredNameButton;
	private JSeparator separator1;
	private JLabel label_subjectScopeNote3;
	private JScrollPane scrollPane5;
	private DomainSortableTable accessionsTable;
	private JSeparator separator2;
	private JLabel label_subjectScopeNote2;
	private JScrollPane scrollPane4;
	private ResourceAndComponentLinkTable resourcesTable;
	private JLabel label1;
	private JSeparator separator4;
	private JLabel label_subjectScopeNote5;
	private JScrollPane scrollPane6;
	private DomainSortableTable digitalObjectsTable;
	private JPanel contactInfoPanel;
	private JLabel label_nameContactAddress3;
	public JTextField salutation;
	private JLabel label_nameContactAddress1;
	public JTextField nameContactAddress1;
	private JLabel label_nameContactAddress2;
	public JTextField nameContactAddress2;
	private JLabel label_nameContactCity;
	public JTextField nameContactCity;
	private JLabel label_nameContactRegion;
	public JTextField nameContactRegion;
	private JLabel label_nameContactMailCode;
	public JTextField nameContactMailCode;
	private JLabel label_nameContactCountry;
	public JTextField nameContactCountry;
	private JLabel label_nameContactPhone;
	public JTextField nameContactPhone;
	private JLabel label_nameContactFax;
	public JTextField nameContactFax;
	private JLabel label_nameContactEmail;
	public JTextField nameContactEmail;
	private JLabel label_nameContactName;
	public JTextField nameContactName;
	private JLabel label_nameContactNotes;
	private JScrollPane scrollPane3;
	private DomainSortedTable nameContactNotesTable;
	private JPanel panel2;
	private JButton addNoteButton;
	private JButton removeContactNoteButton;
	private JPanel panel31;
	private JPanel panel32;
	private JLabel label_acknowledgementDate2;
	public JFormattedTextField acknowledgementDate2;
	private JLabel label_acknowledgementDate3;
	public JFormattedTextField acknowledgementDate3;
	public JCheckBox rights2;
	public JCheckBox rights3;
	private JLabel label_date1Begin2;
	public JFormattedTextField date1Begin2;
	private JLabel label_date1Begin3;
	public JFormattedTextField date1Begin3;
	private JLabel label_date1Begin4;
	public JFormattedTextField extentNumber2;
	private JLabel label_date1Begin5;
	public JFormattedTextField extentNumber3;
	private JLabel label_date1Begin6;
	public JTextField dateExpression2;
	private JLabel label_date1Begin7;
	public JTextField dateExpression3;
	private JLabel label_date1Begin8;
	public JTextField dateExpression4;
	private JLabel label_date1Begin9;
	private JScrollPane scrollPane44;
	public JTextArea title3;
	private JPanel panel35;
	private JLabel label_date1Begin10;
	private JScrollPane scrollPane45;
	public JTextArea title4;
	private JLabel label_date1Begin11;
	private JScrollPane scrollPane46;
	public JTextArea title5;
	private JLabel label_date1Begin12;
	private JScrollPane scrollPane47;
	public JTextArea title6;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    private ActionListener menuListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JMenuItem selectedItem = (JMenuItem) e.getSource();
            addNote(selectedItem.getText());
        }
    };

    protected ActionListener menuDeleteListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
			try {
				removeContactNote();
			} catch (ObjectNotRemovedException e1) {
				new ErrorDialog("Contact Note not removed", e1).showDialog();
			}
		}
    };

    protected void removeContactNote() throws ObjectNotRemovedException {
        this.removeRelatedTableRow(getNameContactNotesTable(), null, super.getModel());
        getNameContactNotesTable().updateCollection(((Names) super.getModel()).getContactNotes());
   }

    protected JPopupMenu insertContactNotePopUpMenu = new JPopupMenu();
    private Component currentPrimaryNamePanel;

    public void setDetailsPanel(String nameType) {

        CellConstraints cc = new CellConstraints();
        detailsContainer.remove(currentPrimaryNamePanel);

        if (nameType.equalsIgnoreCase(Names.PERSON_TYPE)) {
            currentPrimaryNamePanel = new NamePersonalFields(this.detailsModel);
        } else if (nameType.equalsIgnoreCase(Names.FAMILY_TYPE)) {
            currentPrimaryNamePanel = new NameFamilyFields(this.detailsModel);
        } else if (nameType.equalsIgnoreCase(Names.CORPORATE_BODY_TYPE)) {
            currentPrimaryNamePanel = new NameCorpFields(this.detailsModel);
        }
        detailsContainer.add(currentPrimaryNamePanel, cc.xy(1, 1));
        if (currentPrimaryNamePanel instanceof NamePrimaryNameFields) {
            ((NamePrimaryNameFields) currentPrimaryNamePanel).setModel(super.getModel(), null);
        }
    }

    /**
     * Sets the model for this editor.
     *
     * @param model the model to be used
     */

    public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
        super.setModel(model, progressPanel);

        Names namesModel = (Names) model;
        resourcesTable.updateCollection(namesModel.getResources());
        accessionsTable.updateCollection(namesModel.getAccessions());
        digitalObjectsTable.updateCollection(namesModel.getDigitalObjects());
        nameContactNotesTable.updateCollection(namesModel.getRelatedCollection(NameContactNotes.class));
        nonPreferredNamesTable.updateCollection(namesModel.getRelatedCollection(NonPreferredNames.class));

        setPluginModel(); // update any plugins with this new domain object
    }

    public Component getInitialFocusComponent() {
        if (currentPrimaryNamePanel instanceof DomainEditorFields) {
            return ((DomainEditorFields) currentPrimaryNamePanel).getInitialFocusComponent();
        } else {
            return null;
        }
    }


    public DomainSortedTable getNameContactNotesTable() {
        return nameContactNotesTable;
    }

    public DomainSortableTable getNonPreferredNamesTable() {
        return nonPreferredNamesTable;
    }

    public void setNonPreferredNamesTable(DomainSortableTable nonPreferredNamesTable) {
        this.nonPreferredNamesTable = nonPreferredNamesTable;
    }

    public JButton getAddNonPreferredNameButton() {
        return addNonPreferredNameButton;
    }

    public void setAddNonPreferredNameButton(JButton addNonPreferredNameButton) {
        this.addNonPreferredNameButton = addNonPreferredNameButton;
    }

    public JButton getRemoveNonPreferredNameButton() {
        return removeNonPreferredNameButton;
    }

    public void setRemoveNonPreferredNameButton(JButton removeNonPreferredNameButton) {
        this.removeNonPreferredNameButton = removeNonPreferredNameButton;
    }

    public JButton getRemoveContactNoteButton() {
        return removeContactNoteButton;
    }

    public void setRemoveContactNoteButton(JButton remoeContactNoteButton) {
        this.removeContactNoteButton = remoeContactNoteButton;
    }

    private void initAccess() {
        //hide repository popup if necessary
		//todo RAC class 2 change - let level 2 users read/write name contact records
//		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_PROJECT_MANAGER)) {
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
            tabbedPane.removeTabAt(2);

        }
        //if user is not at least advanced data entry set the record to read only
        if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
            setFormToReadOnly();
            this.getAddNonPreferredNameButton().setVisible(false);
            this.getRemoveNonPreferredNameButton().setVisible(false);
            nameDescriptionType.setEnabled(false);
        }
    }

    protected void setDisplayToFirstTab() {
        this.tabbedPane.setSelectedIndex(0);
    }

    /**
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedNameEditorPlugins();
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setEditorField(this);
                HashMap pluginPanels = plugin.getEmbeddedPanels();
                for(Object key : pluginPanels.keySet()) {
                    String panelName = (String)key;
                    JPanel pluginPanel = (JPanel)pluginPanels.get(key);
                    tabbedPane.addTab(panelName, pluginPanel);
                }
            }
        }
    }

    /**
     * Method set the model for any plugins loaded
     */
    private void setPluginModel() {
        if(plugins != null) {
            for(ATPlugin plugin : plugins) {
                plugin.setModel(getModel(), null);
            }
        }
    }
}

