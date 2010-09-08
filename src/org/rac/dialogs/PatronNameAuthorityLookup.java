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
 * Created by JFormDesigner on Mon Sep 19 15:26:15 EDT 2005
 */

package org.rac.dialogs;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.editor.NameEnabledEditor;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.UnsupportedDomainObjectModelException;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.swing.WorkSurfaceContainer;
import org.archiviststoolkit.util.LookupListUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.rac.model.Patrons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class PatronNameAuthorityLookup extends JDialog {

	/**
	 * Constructor used when linking names to archDescription records
 	 * @param owner
	 * @param parentEditor
	 */
	public PatronNameAuthorityLookup(Dialog owner, NameEnabledEditor parentEditor) {
		super(owner);
		this.parentEditor = parentEditor;
		initComponents();
		selectPanel.setVisible(false);
		this.getRootPane().setDefaultButton(this.linkButton);
	}

	private void doneButtonActionPerformed(ActionEvent e) {
        status = JOptionPane.CANCEL_OPTION;
        this.setVisible(false);
	}

	public JButton getSelectButton() {
		return selectButton;
	}

	private void selectButtonActionPerformed() {
		if (namesLookupTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a name");
		} else {
			status = JOptionPane.OK_OPTION;
			this.setVisible(false);
		}
	}

	public Names getSelectedName() {
		return (Names)namesTableModel.getElementAt(namesLookupTable.getSelectedRow());
	}

	public JButton getLinkButton() {
		return linkButton;
	}

	private void linkButtonActionPerformed() {
		if (namesLookupTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a name to link");
		} else {
			addSelectedNames();
		}
	}

	private void namesLookupTableKeyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER) {
			 linkButtonActionPerformed();
		}

	}

	private void initComponents() {

		setModal(true);

		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		HeaderPanel = new JPanel();
		mainHeaderPanel = new JPanel();
		mainHeaderLabel = new JLabel();
		panel3 = new JPanel();
		subHeaderLabel = new JLabel();
		contentPane = new JPanel();
		label1 = new JLabel();
		nameLookup = new JTextField();
		scrollPane1 = new JScrollPane();
		namesLookupTable = new DomainSortableTable(Names.class, nameLookup);
		linkingPanel = new JPanel();
		label4 = new JLabel();
		label3 = new JLabel();
		buttonBarLinking = new JPanel();
		linkButton = new JButton();
		createName = new JButton();
		doneButton = new JButton();
		selectPanel = new JPanel();
		selectButton = new JButton();
		doneButton2 = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		Container contentPane2 = getContentPane();
		contentPane2.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(null);
			dialogPane.setBackground(new Color(200, 205, 232));
			dialogPane.setLayout(new BorderLayout());

			//======== HeaderPanel ========
			{
				HeaderPanel.setBackground(new Color(80, 69, 57));
				HeaderPanel.setOpaque(false);
				HeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				HeaderPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						new ColumnSpec(Sizes.bounded(Sizes.MINIMUM, Sizes.dluX(100), Sizes.dluX(200))),
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					RowSpec.decodeSpecs("default")));

				//======== mainHeaderPanel ========
				{
					mainHeaderPanel.setBackground(new Color(80, 69, 57));
					mainHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					mainHeaderPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.RELATED_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC
						}));

					//---- mainHeaderLabel ----
					mainHeaderLabel.setText("Main Header");
					mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					mainHeaderLabel.setForeground(Color.white);
					mainHeaderPanel.add(mainHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(mainHeaderPanel, cc.xy(1, 1));

				//======== panel3 ========
				{
					panel3.setBackground(new Color(66, 60, 111));
					panel3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel3.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.RELATED_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						new RowSpec[] {
							FormFactory.RELATED_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.RELATED_GAP_ROWSPEC
						}));

					//---- subHeaderLabel ----
					subHeaderLabel.setText("Name Lookup");
					subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					subHeaderLabel.setForeground(Color.white);
					panel3.add(subHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(panel3, cc.xy(2, 1));
			}
			dialogPane.add(HeaderPanel, BorderLayout.NORTH);

			//======== contentPane ========
			{
				contentPane.setOpaque(false);
				contentPane.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.UNRELATED_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.UNRELATED_GAP_COLSPEC
					},
					new RowSpec[] {
						FormFactory.UNRELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.UNRELATED_GAP_ROWSPEC
					}));

				//---- label1 ----
				label1.setText("Filter:");
				contentPane.add(label1, cc.xy(2, 2));
				contentPane.add(nameLookup, cc.xy(4, 2));

				//======== scrollPane1 ========
				{
					scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

					//---- namesLookupTable ----
					namesLookupTable.setPreferredScrollableViewportSize(new Dimension(450, 300));
					namesLookupTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							namesLookupTableMouseClicked(e);
						}
					});
					namesLookupTable.addKeyListener(new KeyAdapter() {
						@Override
						public void keyTyped(KeyEvent e) {
							namesLookupTableKeyTyped(e);
						}
					});
					scrollPane1.setViewportView(namesLookupTable);
				}
				contentPane.add(scrollPane1, cc.xywh(2, 4, 3, 1));

				//======== linkingPanel ========
				{
					linkingPanel.setOpaque(false);
					linkingPanel.setLayout(new FormLayout(
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

					//---- label4 ----
					label4.setText("Double click on a Name to add it to the record.");
					linkingPanel.add(label4, cc.xywh(1, 1, 3, 1));

					//---- label3 ----
					label3.setText("Or hit enter if a Term is highlighted.");
					linkingPanel.add(label3, cc.xywh(1, 3, 3, 1));

					//======== buttonBarLinking ========
					{
						buttonBarLinking.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
						buttonBarLinking.setBackground(new Color(231, 188, 251));
						buttonBarLinking.setOpaque(false);
						buttonBarLinking.setLayout(new FormLayout(
							new ColumnSpec[] {
								FormFactory.DEFAULT_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								FormFactory.BUTTON_COLSPEC,
								FormFactory.RELATED_GAP_COLSPEC,
								FormFactory.BUTTON_COLSPEC
							},
							RowSpec.decodeSpecs("pref")));

						//---- linkButton ----
						linkButton.setText("Link");
						linkButton.setOpaque(false);
						linkButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								linkButtonActionPerformed();
							}
						});
						buttonBarLinking.add(linkButton, cc.xy(1, 1));

						//---- createName ----
						createName.setText("Create Name");
						createName.setBackground(new Color(231, 188, 251));
						createName.setOpaque(false);
						createName.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								createNameActionPerformed(e);
							}
						});
						buttonBarLinking.add(createName, cc.xy(3, 1));

						//---- doneButton ----
						doneButton.setText("Close Window");
						doneButton.setBackground(new Color(231, 188, 251));
						doneButton.setOpaque(false);
						doneButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								doneButtonActionPerformed(e);
							}
						});
						buttonBarLinking.add(doneButton, cc.xy(5, 1));
					}
					linkingPanel.add(buttonBarLinking, cc.xywh(1, 5, 3, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
				}
				contentPane.add(linkingPanel, cc.xywh(2, 6, 3, 1));

				//======== selectPanel ========
				{
					selectPanel.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
					selectPanel.setBackground(new Color(231, 188, 251));
					selectPanel.setOpaque(false);
					selectPanel.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.BUTTON_COLSPEC,
							FormFactory.RELATED_GAP_COLSPEC,
							FormFactory.BUTTON_COLSPEC
						},
						RowSpec.decodeSpecs("pref")));

					//---- selectButton ----
					selectButton.setText("Select");
					selectButton.setOpaque(false);
					selectButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							selectButtonActionPerformed();
						}
					});
					selectPanel.add(selectButton, cc.xy(3, 1));

					//---- doneButton2 ----
					doneButton2.setText("Cancel");
					doneButton2.setBackground(new Color(231, 188, 251));
					doneButton2.setOpaque(false);
					doneButton2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							doneButtonActionPerformed(e);
						}
					});
					selectPanel.add(doneButton2, cc.xy(5, 1));
				}
				contentPane.add(selectPanel, cc.xywh(1, 8, 4, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
			}
			dialogPane.add(contentPane, BorderLayout.CENTER);
		}
		contentPane2.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	public void setMainHeaderByClass(Class clazz) {
		StandardEditor.setMainHeaderColorAndTextByClass(clazz, mainHeaderPanel, mainHeaderLabel);
	}

	private void namesLookupTableMouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
			addSelectedNames();
		}
    }

	private void addSelectedNames() {
		if (namesLookupTable.getSelectedRow() == -1) {
			JOptionPane.showMessageDialog(this, "You must select a name");
		} else {
			Names name;
            for (int selectedRow: namesLookupTable.getSelectedRows()) {
                try {
                    name = (Names)namesTableModel.getElementAt(selectedRow);
                    NameEnabledModel nameEnabledModel = (NameEnabledModel)parentEditor.getNameEnabledModel();
                    DomainObject link = nameEnabledModel.addName(name, Patrons.NAME_LINK_FUNCTION_SUBJECT, "", "");
                    if (link != null) {
                        parentEditor.getNamesTable().addDomainObject(link);
                    }
					//set the record to dirty
					ApplicationFrame.getInstance().setRecordDirty();
                } catch (DuplicateLinkException e) {
                    new ErrorDialog(this, "That name is already linked").showDialog();
                }
            }
		}
	}


	private void createNameActionPerformed(ActionEvent e) {
		NameEnabledEditor parentEditor = this.parentEditor;
		DomainEditor dialog = null;
		try {
			dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(Names.class, this, false);
		} catch (DomainEditorCreationException e1) {
			new ErrorDialog(this, "Error creating editor for Names", e1).showDialog();
		}
		dialog.setButtonListeners();
		String nameType = Names.selectNameType(this);
		if ((nameType != null) && (nameType.length() > 0)) {
			Names instance = new Names();
			instance.setNameType(nameType);
			dialog.setModel(instance, null);
			dialog.disableNavigationButtons();
			if (dialog.showDialog() == JOptionPane.OK_OPTION) {
				try {
					DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Names.class);
					access.add(instance);
					Names.addNameToLookupList(instance);
					WorkSurfaceContainer.getWorkSurfaceByClass(Names.class).addToResultSet(instance);
					initLookup();
					nameLookup.setText(instance.getSortName());
					namesLookupTable.setRowSelectionInterval(0,0);

				} catch (ConstraintViolationException persistenceException) {
					JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
				} catch (PersistenceException persistenceException) {
					if (persistenceException.getCause() instanceof ConstraintViolationException) {
						JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + instance);
						return;
					}
					new ErrorDialog(this, "Error saving new record.", persistenceException).showDialog();
				}
			}
		}

	}

	private void initLookup() {
		SortedList sortedNames = Names.getNamesGlazedList();
		textFilteredIssues = new FilterList(sortedNames, new TextComponentMatcherEditor(nameLookup, new DomainFilterator()));
		namesTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Names.class));
		namesLookupTable.setModel(namesTableModel);
		TableComparatorChooser tableSorter = new TableComparatorChooser(namesLookupTable, sortedNames, true);
		nameLookup.requestFocusInWindow();
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel HeaderPanel;
	private JPanel mainHeaderPanel;
	private JLabel mainHeaderLabel;
	private JPanel panel3;
	private JLabel subHeaderLabel;
	private JPanel contentPane;
	private JLabel label1;
	private JTextField nameLookup;
	private JScrollPane scrollPane1;
	private DomainSortableTable namesLookupTable;
	private JPanel linkingPanel;
	private JLabel label4;
	private JLabel label3;
	private JPanel buttonBarLinking;
	private JButton linkButton;
	private JButton createName;
	private JButton doneButton;
	private JPanel selectPanel;
	private JButton selectButton;
	private JButton doneButton2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;

	private Names nameModel = null;
	private NameEnabledEditor parentEditor;
	FilterList textFilteredIssues;
	EventTableModel namesTableModel;

	/**
	 * Flag indicating if the "Cancel" button was pressed to close dialog.
	 */
	protected boolean myIsDialogCancelled = true;

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public final int showDialog() {

		this.pack();
		initLookup();
		setLocationRelativeTo(this.getOwner());
		this.setVisible(true);

		return (status);
	}


	public Names getNameModel() {
		return nameModel;
	}

	public void setNameModel(Names nameModel) {
		this.nameModel = nameModel;
	}

}