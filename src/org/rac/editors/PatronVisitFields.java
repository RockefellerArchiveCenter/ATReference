/*
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
import org.archiviststoolkit.dialog.ResourceLookup;
import org.archiviststoolkit.dialog.SubjectTermLookup;
import org.archiviststoolkit.editor.NameEnabledEditor;
import org.archiviststoolkit.editor.SubjectEnabledEditorFields;
import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.rac.dialogs.PatronManagement;
import org.rac.dialogs.PatronNameAuthorityLookup;
import org.rac.model.*;

public class PatronVisitFields extends RAC_DomainEditorFields implements SubjectEnabledEditorFields, NameEnabledEditor {

	private Boolean readOnly = false;
	private Boolean referenceNewPatron = false;

	public PatronVisitFields(DomainEditor parent) {
		setParentEditor(parent);
		initComponents();
		initAccess();
	}

	public PatronVisitFields(DomainEditor parent, Boolean referenceNewPatron) {
		this.referenceNewPatron = referenceNewPatron;
		setParentEditor(parent);
		initComponents();
		initAccess();
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
		researchPurposeTable.updateCollection(visitModel.getResearchPurposes());
        resourcesTable.updateCollection(visitModel.getResources());
	}

	public Component getInitialFocusComponent() {
		return visitDate;
	}

	private void addSubjectActionPerformed() {
		SubjectTermLookup subjectLookupDialog = new SubjectTermLookup(getParentEditor(), this);
        subjectLookupDialog.setMainHeaderText("Patron Visits");
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
		nameLookupDialog.setMainHeaderText("Patron Visits");
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

	private void editNameRelationshipButtonActionPerformed() {
		try {
			DomainSortableTable namesTable = getNamesTable();
			DomainEditor nameEditor = new DomainEditor(PatronVisitsNames.class, this.getParentEditor(), "Name Link", new PatronVisitNamesFields());
			nameEditor.setCallingTable(namesTable);
			nameEditor.setNavigationButtonListeners(nameEditor);
			int selectedRow = namesTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select a name to edit.", "warning", JOptionPane.WARNING_MESSAGE);
			} else {
				editRelatedRecord(namesTable, PatronVisitsNames.class, true, nameEditor);
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
					DomainEditor nameEditor = new DomainEditor(PatronVisitsNames.class, this.getParentEditor(), "Name Link", new PatronVisitNamesFields());
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

	private void addResearchPurposeActionPerformed() {
		try {
			DomainEditor domainEditor = new DomainEditor(PatronVisitsResearchPurposes.class, this.getParentEditor(), "Research Purpose", new PatronVisitResearchPurposeFields());
			domainEditor.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(domainEditor, PatronVisitsResearchPurposes.class, researchPurposeTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
//
//
//		String researchPurpose = JOptionPane.showInputDialog(this.getParentEditor(), "Enter a Research Purpose");
//
//		if (researchPurpose != null && researchPurpose.length() > 0) {
//			PatronVisits visitModel = (PatronVisits)getModel();
//			visitModel.addResearchPurposes(researchPurpose);
//			researchPurposeTable.updateCollection(visitModel.getResearchPurposes());
//		}
	}

	private void removeResearchPurposeActionPerformed() {
		try {
			removeRelatedTableRow(researchPurposeTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Subject not removed", e).showDialog();
		}
	}

	private void researchPurposeTableMouseClicked(MouseEvent e) {
		if (readOnly) {
			//do nothing as read only users can't change anything
		} else {
			if (e.getClickCount() == 2) {
				try {
					DomainEditor researchPurposeEditor = new DomainEditor(PatronVisitsNames.class, this.getParentEditor(), "Research Purpose", new PatronVisitResearchPurposeFields());
					researchPurposeEditor.setCallingTable(researchPurposeTable);
					researchPurposeEditor.setNavigationButtonListeners(researchPurposeEditor);
					editRelatedRecord(researchPurposeTable, this.getClass(), true, researchPurposeEditor);
				} catch (UnsupportedTableModelException e1) {
					new ErrorDialog("Error creating editor", e1).showDialog();
				}
			}
			researchPurposeTable.invalidate();
			researchPurposeTable.validate();
			researchPurposeTable.repaint();
		}
	}

	private void editResearchPurposeButtonActionPerformed() {
		try {
			DomainEditor researchPurposeEditor = new DomainEditor(PatronVisitsResearchPurposes.class, this.getParentEditor(), "Research Purpose", new PatronVisitResearchPurposeFields());
			researchPurposeEditor.setCallingTable(researchPurposeTable);
			researchPurposeEditor.setNavigationButtonListeners(researchPurposeEditor);
			int selectedRow = researchPurposeTable.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select a research purpose to edit.", "warning", JOptionPane.WARNING_MESSAGE);
			} else {
				editRelatedRecord(researchPurposeTable, PatronVisitsResearchPurposes.class, true, researchPurposeEditor);
				researchPurposeTable.invalidate();
				researchPurposeTable.validate();
				researchPurposeTable.repaint();
			}
		} catch (UnsupportedTableModelException e) {
			new ErrorDialog("Error creating editor", e).showDialog();
		}
	}

    private void addResourceButtonActionPerformed() {
        ResourceLookup resourcePicker = new ResourceLookup(getParentEditor(), this);
		resourcePicker.setMainHeaderText("Patron Visits");
        resourcePicker.setMainHeaderColor(PatronManagement.headerColor);
        resourcePicker.showDialog(this);
    }

    private void removeResourceButtonActionPerformed() {
        try {
			removeRelatedTableRow(resourcesTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Resource not removed", e).showDialog();
		}
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        contentPanel = new JPanel();
        label_visitDate = new JLabel();
        visitDate = ATBasicComponentFactory.createDateField(detailsModel.getModel( PatronVisits.PROPERTYNAME_VISIT_DATE));
        visitTypeLabel = new JLabel();
        visitTypeComboBox = ATBasicComponentFactory.createComboBox(detailsModel, PatronVisits.PROPERTYNAME_VISIT_TYPE, PatronVisits.class);
        label_subject = new JLabel();
        address1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronVisits.PROPERTYNAME_CONTACT_ARCHIVIST));
        label_topic = new JLabel();
        scrollPane1 = new JScrollPane();
        patronNotes = ATBasicComponentFactory.createTextArea(detailsModel.getModel(PatronVisits.PROPERTYNAME_TOPIC));
        scrollPane5 = new JScrollPane();
        researchPurposeTable = new DomainSortableTable(PatronVisitsResearchPurposes.class);
        panel12 = new JPanel();
        addResearchPurpose = new JButton();
        editResearchPurposeButton = new JButton();
        removeResearchPurpose = new JButton();
        separator5 = new JSeparator();
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        subjectLabel = new JLabel();
        scrollPane3 = new JScrollPane();
        subjectsTable = new DomainSortableTable(PatronVisitsSubjects.class);
        panel10 = new JPanel();
        addSubject = new JButton();
        removeSubject = new JButton();
        separator1 = new JSeparator();
        namesLabel = new JLabel();
        scrollPane4 = new JScrollPane();
        namesTable = new DomainSortableTable(PatronVisitsNames.class);
        panel11 = new JPanel();
        editNameRelationshipButton = new JButton();
        addName = new JButton();
        removeName = new JButton();
        panel2 = new JPanel();
        scrollPane2 = new JScrollPane();
        resourcesTable = new DomainSortableTable(PatronVisitsResources.class);
        panel3 = new JPanel();
        addResourceButton = new JButton();
        removeResourceButton = new JButton();
        resourcesUsedLabel = new JLabel();
        scrollPane6 = new JScrollPane();
        textArea1 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(PatronVisits.PROPERTYNAME_DETAILS_ON_RESOURCES));
        panel4 = new JPanel();
        userDefinedStringLabel = new JLabel();
        userDefinedTextField1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronVisits.PROPERTYNAME_USER_DEFINED_STRING1),false);
        userDefinedBooleanLabel = new JLabel();
        userDefinedCheckBox1 = ATBasicComponentFactory.createCheckBox(detailsModel, PatronVisits.PROPERTYNAME_USER_DEFINED_BOOLEAN1, PatronVisits.class);
        userDefinedTextLabel = new JLabel();
        scrollPane7 = new JScrollPane();
        userDefinedTextArea1 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(PatronVisits.PROPERTYNAME_USER_DEFINED_TEXT1));
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
                    new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
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

            //---- visitTypeLabel ----
            visitTypeLabel.setText("Visit Type");
            ATFieldInfo.assignLabelInfo(visitTypeLabel, PatronVisits.class, PatronVisits.PROPERTYNAME_VISIT_TYPE);
            contentPanel.add(visitTypeLabel, cc.xy(1, 3));
            contentPanel.add(visitTypeComboBox, cc.xy(3, 3));

            //---- label_subject ----
            label_subject.setText("Contact");
            ATFieldInfo.assignLabelInfo(label_subject, PatronVisits.class, PatronVisits.PROPERTYNAME_CONTACT_ARCHIVIST);
            contentPanel.add(label_subject, cc.xy(1, 5));

            //---- address1 ----
            address1.setColumns(30);
            contentPanel.add(address1, cc.xy(3, 5));

            //---- label_topic ----
            label_topic.setText("Topic");
            ATFieldInfo.assignLabelInfo(label_topic, PatronVisits.class, PatronVisits.PROPERTYNAME_TOPIC);
            contentPanel.add(label_topic, cc.xy(1, 7));

            //======== scrollPane1 ========
            {

                //---- patronNotes ----
                patronNotes.setRows(4);
                patronNotes.setLineWrap(true);
                patronNotes.setWrapStyleWord(true);
                patronNotes.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                patronNotes.setMinimumSize(new Dimension(200, 16));
                scrollPane1.setViewportView(patronNotes);
            }
            contentPanel.add(scrollPane1, cc.xy(3, 7));

            //======== scrollPane5 ========
            {
                scrollPane5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                scrollPane5.setPreferredSize(new Dimension(219, 100));

                //---- researchPurposeTable ----
                researchPurposeTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
                researchPurposeTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        researchPurposeTableMouseClicked(e);
                    }
                });
                scrollPane5.setViewportView(researchPurposeTable);
            }
            contentPanel.add(scrollPane5, cc.xywh(1, 9, 3, 1));

            //======== panel12 ========
            {
                panel12.setBackground(new Color(231, 188, 251));
                panel12.setOpaque(false);
                panel12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                panel12.setLayout(new FormLayout(
                    new ColumnSpec[] {
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC,
                        FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                        FormFactory.DEFAULT_COLSPEC
                    },
                    RowSpec.decodeSpecs("default")));

                //---- addResearchPurpose ----
                addResearchPurpose.setText("Add Reseach Purpose");
                addResearchPurpose.setOpaque(false);
                addResearchPurpose.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                addResearchPurpose.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addResearchPurposeActionPerformed();
                    }
                });
                panel12.add(addResearchPurpose, cc.xy(1, 1));

                //---- editResearchPurposeButton ----
                editResearchPurposeButton.setText("Edit Research Purpose");
                editResearchPurposeButton.setOpaque(false);
                editResearchPurposeButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                editResearchPurposeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        editResearchPurposeButtonActionPerformed();
                    }
                });
                panel12.add(editResearchPurposeButton, cc.xy(3, 1));

                //---- removeResearchPurpose ----
                removeResearchPurpose.setText("Remove Reseach Purpose");
                removeResearchPurpose.setOpaque(false);
                removeResearchPurpose.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                removeResearchPurpose.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeResearchPurposeActionPerformed();
                    }
                });
                panel12.add(removeResearchPurpose, cc.xy(5, 1));
            }
            contentPanel.add(panel12, cc.xywh(1, 11, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

            //---- separator5 ----
            separator5.setBackground(new Color(220, 220, 232));
            separator5.setForeground(new Color(147, 131, 86));
            separator5.setMinimumSize(new Dimension(1, 10));
            separator5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
            contentPanel.add(separator5, cc.xywh(1, 13, 3, 1));

            //======== tabbedPane1 ========
            {

                //======== panel1 ========
                {
                    panel1.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
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

                    //---- subjectLabel ----
                    subjectLabel.setText("Subjects");
                    panel1.add(subjectLabel, cc.xy(1, 1));

                    //======== scrollPane3 ========
                    {
                        scrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        scrollPane3.setPreferredSize(new Dimension(219, 100));

                        //---- subjectsTable ----
                        subjectsTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
                        scrollPane3.setViewportView(subjectsTable);
                    }
                    panel1.add(scrollPane3, cc.xy(1, 3));

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
                    panel1.add(panel10, cc.xywh(1, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                    //---- separator1 ----
                    separator1.setMinimumSize(new Dimension(1, 10));
                    separator1.setForeground(new Color(147, 131, 86));
                    panel1.add(separator1, cc.xy(1, 7));

                    //---- namesLabel ----
                    namesLabel.setText("Names");
                    panel1.add(namesLabel, cc.xy(1, 9));

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
                    panel1.add(scrollPane4, cc.xy(1, 11));

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
                        addName.setText("Add Name");
                        addName.setOpaque(false);
                        addName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        addName.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                addNameActionPerformed();
                            }
                        });
                        panel11.add(addName, cc.xy(3, 1));

                        //---- removeName ----
                        removeName.setText("Remove Name");
                        removeName.setOpaque(false);
                        removeName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
                        removeName.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeNameActionPerformed();
                            }
                        });
                        panel11.add(removeName, cc.xy(5, 1));
                    }
                    panel1.add(panel11, cc.xywh(1, 13, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
                }
                tabbedPane1.addTab("Subjects and Names", panel1);


                //======== panel2 ========
                {
                    panel2.setLayout(new FormLayout(
                        ColumnSpec.decodeSpecs("default:grow"),
                        new RowSpec[] {
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            FormFactory.DEFAULT_ROWSPEC,
                            FormFactory.LINE_GAP_ROWSPEC,
                            new RowSpec(RowSpec.CENTER, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //======== scrollPane2 ========
                    {
                        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                        scrollPane2.setPreferredSize(new Dimension(219, 100));

                        //---- resourcesTable ----
                        resourcesTable.setPreferredScrollableViewportSize(new Dimension(200, 200));
                        scrollPane2.setViewportView(resourcesTable);
                    }
                    panel2.add(scrollPane2, cc.xy(1, 1));

                    //======== panel3 ========
                    {
                        panel3.setLayout(new FlowLayout());

                        //---- addResourceButton ----
                        addResourceButton.setText("Add Resource");
                        addResourceButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                addResourceButtonActionPerformed();
                            }
                        });
                        panel3.add(addResourceButton);

                        //---- removeResourceButton ----
                        removeResourceButton.setText("Reomve Resource");
                        removeResourceButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                removeResourceButtonActionPerformed();
                            }
                        });
                        panel3.add(removeResourceButton);
                    }
                    panel2.add(panel3, cc.xy(1, 3));

                    //---- resourcesUsedLabel ----
                    resourcesUsedLabel.setText("Details On Resources Used");
                    ATFieldInfo.assignLabelInfo(resourcesUsedLabel, PatronVisits.class, PatronVisits.PROPERTYNAME_DETAILS_ON_RESOURCES);
                    panel2.add(resourcesUsedLabel, cc.xy(1, 5));

                    //======== scrollPane6 ========
                    {

                        //---- textArea1 ----
                        textArea1.setRows(10);
                        scrollPane6.setViewportView(textArea1);
                    }
                    panel2.add(scrollPane6, cc.xy(1, 7));
                }
                tabbedPane1.addTab("Resources Used", panel2);


                //======== panel4 ========
                {
                    panel4.setLayout(new FormLayout(
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
                            new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
                        }));

                    //---- userDefinedStringLabel ----
                    userDefinedStringLabel.setText("User String");
                    ATFieldInfo.assignLabelInfo(userDefinedStringLabel, PatronVisits.class, PatronVisits.PROPERTYNAME_USER_DEFINED_STRING1);
                    panel4.add(userDefinedStringLabel, cc.xy(1, 1));
                    panel4.add(userDefinedTextField1, cc.xy(3, 1));

                    //---- userDefinedBooleanLabel ----
                    userDefinedBooleanLabel.setText("User Boolean");
                    ATFieldInfo.assignLabelInfo(userDefinedBooleanLabel, PatronVisits.class, PatronVisits.PROPERTYNAME_USER_DEFINED_BOOLEAN1);
                    panel4.add(userDefinedBooleanLabel, cc.xy(1, 3));

                    //---- userDefinedCheckBox1 ----
                    userDefinedCheckBox1.setText("Option");
                    panel4.add(userDefinedCheckBox1, cc.xy(3, 3));

                    //---- userDefinedTextLabel ----
                    userDefinedTextLabel.setText("User Text");
                    ATFieldInfo.assignLabelInfo(userDefinedTextLabel, PatronVisits.class, PatronVisits.PROPERTYNAME_USER_DEFINED_TEXT1);
                    panel4.add(userDefinedTextLabel, cc.xywh(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

                    //======== scrollPane7 ========
                    {
                        scrollPane7.setViewportView(userDefinedTextArea1);
                    }
                    panel4.add(scrollPane7, cc.xy(3, 5));
                }
                tabbedPane1.addTab("User Defined Fields", panel4);

            }
            contentPanel.add(tabbedPane1, cc.xywh(1, 15, 3, 1));
        }
        add(contentPanel, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel contentPanel;
    private JLabel label_visitDate;
    public JFormattedTextField visitDate;
    private JLabel visitTypeLabel;
    private JComboBox visitTypeComboBox;
    private JLabel label_subject;
    private JTextField address1;
    private JLabel label_topic;
    private JScrollPane scrollPane1;
    public JTextArea patronNotes;
    private JScrollPane scrollPane5;
    private DomainSortableTable researchPurposeTable;
    private JPanel panel12;
    private JButton addResearchPurpose;
    private JButton editResearchPurposeButton;
    private JButton removeResearchPurpose;
    private JSeparator separator5;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JLabel subjectLabel;
    private JScrollPane scrollPane3;
    private DomainSortableTable subjectsTable;
    private JPanel panel10;
    private JButton addSubject;
    private JButton removeSubject;
    private JSeparator separator1;
    private JLabel namesLabel;
    private JScrollPane scrollPane4;
    private DomainSortableTable namesTable;
    private JPanel panel11;
    private JButton editNameRelationshipButton;
    private JButton addName;
    private JButton removeName;
    private JPanel panel2;
    private JScrollPane scrollPane2;
    private DomainSortableTable resourcesTable;
    private JPanel panel3;
    private JButton addResourceButton;
    private JButton removeResourceButton;
    private JLabel resourcesUsedLabel;
    private JScrollPane scrollPane6;
    private JTextArea textArea1;
    private JPanel panel4;
    private JLabel userDefinedStringLabel;
    private JTextField userDefinedTextField1;
    private JLabel userDefinedBooleanLabel;
    private JCheckBox userDefinedCheckBox1;
    private JLabel userDefinedTextLabel;
    private JScrollPane scrollPane7;
    private JTextArea userDefinedTextArea1;
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

    public DomainSortableTable getResourcesTable() {
        return resourcesTable;
    }

    private void initAccess() {
		//set form to read only for reference staff
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_BEGINNING_DATA_ENTRY) &&
				!this.getParentEditor().getNewRecord() &&
				!referenceNewPatron) {
			readOnly = true;
			setFormToReadOnly();
			//research purpose
			addResearchPurpose.setEnabled(false);
			removeResearchPurpose.setEnabled(false);
			//subject buttons
			addSubject.setEnabled(false);
			removeSubject.setEnabled(false);
			//name buttons
			addName.setEnabled(false);
			removeName.setEnabled(false);
			editNameRelationshipButton.setEnabled(false);
            // resource button
            addResourceButton.setEnabled(false);
            removeResourceButton.setEnabled(false);
		}
	}

}