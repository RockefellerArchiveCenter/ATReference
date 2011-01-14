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
 * Created by JFormDesigner on Mon Apr 24 10:47:08 EDT 2006
 */

package org.archiviststoolkit.editor;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.archiviststoolkit.util.InLineTagsUtils;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.InLineTags;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BibliographyFields extends ArchDescriptionStructuredDataFields {

	protected BibliographyFields(DomainEditor parentEditor) {
		super();
		this.setParentEditor(parentEditor);
		initComponents();
        super.init();
		initUndo(note);
	}

	private void bibItemsTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			super.handleTableMouseClicked(BibItems.class);
		}
	}

    protected DomainSortedTable getItemsTable() {
        return itemsTable;
    }

	private void itemsTableMousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            insertItemPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
	}

	private void itemsTableMouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            insertItemPopUpMenu.show(e.getComponent(), e.getX(), e.getY());
        }
	}

	private void addItemButtonActionPerformed(ActionEvent e) {
		super.addItemButtonAction();
	}

	public JButton getAddItemButton() {
		return addItemButton;
	}

	private void insertInlineTagActionPerformed() {
		InLineTagsUtils.wrapInTagActionPerformed(insertInlineTag, note,  this.getParentEditor());
	}

	private void undoButtonActionPerformed() {
		handleUndoButtonAction();
	}

	private void redoButtonActionPerformed() {
		handleRedoButtonAction();
	}

//	public JButton getUndoButton() {
//		return undoButton;
//	}
//
//	public JButton getRedoButton() {
//		return redoButton;
//	}
//
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		panel1 = new JPanel();
		internalOnly = ATBasicComponentFactory.createCheckBox(detailsModel, ArchDescriptionStructuredData.PROPERTYNAME_ARCH_DESCRIPTION_NOTE_INTERNAL_ONLY, Bibliography.class);
		panel4 = new JPanel();
		label4 = new JLabel();
		persistentId2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(ArchDescriptionRepeatingData.PROPERTYNAME_PERSISTENT_ID));
		label_subjectTerm = new JLabel();
		title = ATBasicComponentFactory.createTextField(detailsModel.getModel(Bibliography.PROPERTYNAME_TITLE));
		label_subjectScopeNote = new JLabel();
		scrollPane1 = new JScrollPane();
		note = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Bibliography.PROPERTYNAME_NOTE));
		tagApplicatorPanel = new JPanel();
		insertInlineTag = ATBasicComponentFactory.createUnboundComboBox(InLineTagsUtils.getInLineTagList());
		label_subjectScopeNote2 = new JLabel();
		scrollPane2 = new JScrollPane();
		ingestProblems = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Bibliography.PROPERTYNAME_EAD_INGEST_PROBLEMS));
		scrollPane7 = new JScrollPane();
		itemsTable = new DomainSortedTable(BibItems.class);
		panel3 = new JPanel();
		panel30 = new JPanel();
		addItemButton = new JButton();
		removeItemButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setPreferredSize(new Dimension(800, 500));
		setLayout(new FormLayout(
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

		//======== panel1 ========
		{
			panel1.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));

			//---- internalOnly ----
			internalOnly.setBackground(new Color(231, 188, 251));
			internalOnly.setText("Internal Only");
			internalOnly.setOpaque(false);
			internalOnly.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			panel1.add(internalOnly, cc.xy(1, 1));

			//======== panel4 ========
			{
				panel4.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- label4 ----
				label4.setText("Persistent ID");
				label4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel4.add(label4, cc.xy(1, 1));

				//---- persistentId2 ----
				persistentId2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				persistentId2.setColumns(3);
				persistentId2.setBorder(null);
				persistentId2.setEditable(false);
				persistentId2.setOpaque(false);
				panel4.add(persistentId2, cc.xywh(3, 1, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
			}
			panel1.add(panel4, cc.xywh(5, 1, 1, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
		}
		add(panel1, cc.xywh(1, 1, 3, 1));

		//---- label_subjectTerm ----
		label_subjectTerm.setText("Title");
		label_subjectTerm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		ATFieldInfo.assignLabelInfo(label_subjectTerm, Bibliography.class, Bibliography.PROPERTYNAME_TITLE);
		add(label_subjectTerm, cc.xy(1, 3));
		add(title, cc.xy(3, 3));

		//---- label_subjectScopeNote ----
		label_subjectScopeNote.setText("Note");
		label_subjectScopeNote.setVerticalAlignment(SwingConstants.TOP);
		label_subjectScopeNote.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		ATFieldInfo.assignLabelInfo(label_subjectScopeNote, Bibliography.class, Bibliography.PROPERTYNAME_NOTE);
		add(label_subjectScopeNote, cc.xywh(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

		//======== scrollPane1 ========
		{
			scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane1.setMaximumSize(new Dimension(32767, 100));
			scrollPane1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

			//---- note ----
			note.setRows(4);
			note.setLineWrap(true);
			note.setTabSize(20);
			note.setWrapStyleWord(true);
			scrollPane1.setViewportView(note);
		}
		add(scrollPane1, cc.xy(3, 5));

		//======== tagApplicatorPanel ========
		{
			tagApplicatorPanel.setOpaque(false);
			tagApplicatorPanel.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				RowSpec.decodeSpecs("default")));

			//---- insertInlineTag ----
			insertInlineTag.setOpaque(false);
			insertInlineTag.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
			insertInlineTag.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					insertInlineTagActionPerformed();
				}
			});
			tagApplicatorPanel.add(insertInlineTag, cc.xy(1, 1));
		}
		add(tagApplicatorPanel, cc.xy(3, 7));

		//---- label_subjectScopeNote2 ----
		label_subjectScopeNote2.setText("Ingest Problems");
		label_subjectScopeNote2.setVerticalAlignment(SwingConstants.TOP);
		label_subjectScopeNote2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		ATFieldInfo.assignLabelInfo(label_subjectScopeNote2, Bibliography.class, Bibliography.PROPERTYNAME_EAD_INGEST_PROBLEMS);
		add(label_subjectScopeNote2, cc.xywh(1, 9, 1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

		//======== scrollPane2 ========
		{
			scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane2.setMaximumSize(new Dimension(32767, 100));
			scrollPane2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

			//---- ingestProblems ----
			ingestProblems.setRows(4);
			ingestProblems.setLineWrap(true);
			ingestProblems.setTabSize(20);
			ingestProblems.setWrapStyleWord(true);
			scrollPane2.setViewportView(ingestProblems);
		}
		add(scrollPane2, cc.xy(3, 9));

		//======== scrollPane7 ========
		{
			scrollPane7.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

			//---- itemsTable ----
			itemsTable.setFocusable(false);
			itemsTable.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					itemsTableMousePressed(e);
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					itemsTableMouseReleased(e);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					bibItemsTableMouseClicked(e);
				}
			});
			scrollPane7.setViewportView(itemsTable);
		}
		add(scrollPane7, cc.xywh(1, 11, 3, 1));

		//======== panel3 ========
		{
			panel3.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
				},
				RowSpec.decodeSpecs("default")));
			((FormLayout)panel3.getLayout()).setColumnGroups(new int[][] {{1, 3, 5}});

			//======== panel30 ========
			{
				panel30.setOpaque(false);
				panel30.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel30.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					RowSpec.decodeSpecs("default")));

				//---- addItemButton ----
				addItemButton.setText("Add Item");
				addItemButton.setOpaque(false);
				addItemButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				addItemButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addItemButtonActionPerformed(e);
					}
				});
				panel30.add(addItemButton, cc.xy(1, 1));

				//---- removeItemButton ----
				removeItemButton.setBackground(new Color(231, 188, 251));
				removeItemButton.setText("Remove Item");
				removeItemButton.setOpaque(false);
				removeItemButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				removeItemButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeItemButtonActionPerformed(e);
					}
				});
				panel30.add(removeItemButton, cc.xy(3, 1));
			}
			panel3.add(panel30, cc.xy(3, 1));
		}
		add(panel3, cc.xywh(1, 13, 3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	private void removeItemButtonActionPerformed(ActionEvent e) {
		super.removeItem();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel panel1;
	public JCheckBox internalOnly;
	private JPanel panel4;
	private JLabel label4;
	public JTextField persistentId2;
	private JLabel label_subjectTerm;
	public JTextField title;
	private JLabel label_subjectScopeNote;
	private JScrollPane scrollPane1;
	public JTextArea note;
	private JPanel tagApplicatorPanel;
	public JComboBox insertInlineTag;
	private JLabel label_subjectScopeNote2;
	private JScrollPane scrollPane2;
	public JTextArea ingestProblems;
	private JScrollPane scrollPane7;
	private DomainSortedTable itemsTable;
	private JPanel panel3;
	private JPanel panel30;
	private JButton addItemButton;
	private JButton removeItemButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public void setModel(DomainObject model, InfiniteProgressPanel progressPanel) {
		inSetModel = true;
		super.setModel(model, progressPanel);
		Bibliography bibligraphyModel = (Bibliography)model;
        itemsTable.updateCollection(bibligraphyModel.getBibItems());
		inSetModel = false;
	}

	public Component getInitialFocusComponent() {
		return title;
	}
}
