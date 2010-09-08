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
 * Created by JFormDesigner on Wed Jun 02 12:45:46 EDT 2010
 */

package org.rac.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.*;

import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.apache.log4j.Logger;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.GeneralAdminDialog;
import org.archiviststoolkit.exceptions.DeleteException;
import org.archiviststoolkit.exceptions.DomainEditorCreationException;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.ATProgressUtil;
import org.archiviststoolkit.swing.InfiniteProgressPanel;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.rac.editors.ServiceFields;
import org.rac.model.Services;

public class ServicesManagement extends GeneralAdminDialog implements ActionListener {

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());
    private DomainObject currentSearchResultObject;
	protected String humanReadableSearchString = "";
	protected DomainTableListEventListener eventListener;
	int rowCount = 0;
	FilterList<DomainObject> textFilteredIssues;
	EventTableModel lookupTableModel;
	
	public ServicesManagement(Frame owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		// initialize the search dialog
		setClazz(Services.class);
		initLookup();
	}

	public ServicesManagement(Dialog owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		setClazz(Services.class);
		initLookup();
	}

	protected void contentTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedRow = getContentTable().getSelectedRow();
			dialog.setSelectedRow(selectedRow);

			if (selectedRow != -1) {
				try {
					access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

                    currentSearchResultObject = textFilteredIssues.get(selectedRow);
					currentDomainObject = (DomainObject) access.findByPrimaryKeyLongSession(currentSearchResultObject.getIdentifier());
					currentObjectSublist = textFilteredIssues.subList(selectedRow, selectedRow + 1);
					dialog.setModel(currentDomainObject, null);
                    dialog.setNavigationButtons();
                    dialog.setIncludeSaveButton(true);
                    dialog.clearRecordPositionText(); // printing record position in this dialog makes no sense

                    int status = dialog.showDialog();

					if (status == JOptionPane.OK_OPTION) {
						access.updateLongSession(currentDomainObject);
						currentObjectSublist.set(0, currentSearchResultObject);
                        // refresh the main display
                        findAll();
					} else {
						try {
							access.closeLongSessionRollback();
						} catch (SQLException e1) {
							new ErrorDialog(dialog, "Error canceling record.", e1).showDialog();
						}
					}
				} catch (PersistenceException persistenceException) {
					if (persistenceException.getCause() instanceof ConstraintViolationException) {
						String message = "Can't save, duplicate record" + currentDomainObject;
						JOptionPane.showMessageDialog(this, message);
						logger.error(message, persistenceException);
					} else {
						new ErrorDialog(this, "Error saving new record", persistenceException).showDialog();
					}
					try {
						access.closeLongSessionRollback();
					} catch (SQLException sqlex) {
						new ErrorDialog(this, "Error canceling record.", sqlex).showDialog();
					}
				} catch (ConstraintViolationException persistenceException) {
					String message = "Can't save, duplicate record" + currentDomainObject;
					JOptionPane.showMessageDialog(this, message);
					logger.error(message, persistenceException);
					try {
						access.closeLongSessionRollback();
					} catch (SQLException e1) {
						new ErrorDialog(this, "Error canceling record.", e1).showDialog();
					}
				} catch (LookupException lookupException) {
					new ErrorDialog(this, "Error saving new record", lookupException).showDialog();
					try {
						access.closeLongSessionRollback();
					} catch (SQLException sqlex) {
						new ErrorDialog(this, "Error canceling record.", sqlex).showDialog();
					}
				}

			}
		}
	}

	private void searchActionPerformed() {
		// TODO add your code here
	}

	private void listAllActionPerformed() {
		findAll();
	}

	private void reportButtonActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	protected void addRecordButtonActionPerformed(ActionEvent e) {
		Services newService = null;

		dialog.setNewRecord(true);
		Boolean done = false;
		int returnStatus;

		while (!done) {
			newService = new Services();

            dialog.setIncludeSaveButton(true);
            dialog.setModel((DomainObject) newService, null);

			returnStatus = dialog.showDialog();
			try {
				if (returnStatus == javax.swing.JOptionPane.OK_OPTION) {
					DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

					access.getLongSession(); // make sure we have a long session
					access.updateLongSession(dialog.getModel());
					getContentTable().getEventList().add(newService);
 					done = true;
				} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
					DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

					access.getLongSession(); // make sure we have a long session
					access.updateLongSession(dialog.getModel());
					getContentTable().getEventList().add(newService);
                    dialog.setNewRecord(true); // tell the dialog this is a new record
				} else {
					done = true;
				}
			} catch (ConstraintViolationException persistenceException) {
				JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + newService);
				((DomainObject) newService).removeIdAndAuditInfo();
			} catch (PersistenceException persistenceException) {
				if (persistenceException.getCause() instanceof ConstraintViolationException) {
					JOptionPane.showMessageDialog(this,  "Can't save, Duplicate record:" + newService);
				} else {
					done = true;
					new ErrorDialog("Error saving new record.",
							StringHelper.getStackTrace(persistenceException)).showDialog();
				}
			}
		}
		dialog.setNewRecord(false);
		findAll();
	}

	protected void removeRecordButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the following service record(s)",
				"Delete records", JOptionPane.YES_NO_OPTION);

		if (response == JOptionPane.OK_OPTION) {
			int selectedRow = getContentTable().getSelectedRow();

			if (selectedRow != -1) {
				// get a list of AssessmentSearch Results to delete
                ArrayList<DomainObject> deleteList = new ArrayList<DomainObject>();
				for (int index : getContentTable().getSelectedRows()) {
					if (getContentTable().getFilteredList()!= null) {
						deleteList.add(getContentTable().getFilteredList().get(index));
					} else {
						deleteList.add(getContentTable().getSortedList().get(index));
					}
				}

                // Now get a list of assessment records to deleted based on if they no longer
                // have any linked records other
                final HashSet<Services> servicesDeleteList = new HashSet<Services>();

                for (DomainObject object: deleteList) {
                    servicesDeleteList.add((Services)object);
                }

                // run the deletion in a thread to enable using a progress monitor
                Thread performer = new Thread(new Runnable() {
                    public void run() {
                        String deleteProblems = "";

                        DomainAccessObject access = null;
                        try {
                            access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
                        } catch (PersistenceException e1) {
                            new ErrorDialog("Error deleting records", StringHelper.getStackTrace(e1)).showDialog();
                        }

                        // get the progress monitor which allows for canceling
                        InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(getThisAsJDialog(), 1000, true);
                        monitor.start("Deleting Records...");

                        int count = 1; // keep track of the number of record(s) deleted
                        int size = servicesDeleteList.size();
                        for (DomainObject domainObject : servicesDeleteList) {
                            try {
                                access.delete(domainObject);
                                //getContentTable().getEventList().remove(domainObject);

                                // update the monitor
                                monitor.setTextLine(count + " of " + size + " record(s) deleted", 2);
                                count++;

                                // check to see if the cancel button was pressed
                                if (monitor.isProcessCancelled()) {
                                    break;
                                }
                            } catch (PersistenceException e1) {
                                deleteProblems += "\nCould not delete " + domainObject + ". \nReason: " + e1.getMessage();
                            } catch (DeleteException e1) {
                                deleteProblems += "\nCould not delete " + domainObject + ". \nReason: " + e1.getMessage();
                            }
                        }

                        // close the monitor
                        monitor.close();

                        if (deleteProblems.length() > 0) {
                            JOptionPane.showMessageDialog(getThisAsJDialog(), "There were problems deleting some records\n" + deleteProblems);
                        }
                        findAll();
                    }
                }, "Deleting Records");
                performer.start();
			}
		}
	}

	protected void doneButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	public JTextField getFilterField() {
		return filterField;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		HeaderPanel = new JPanel();
		panel2 = new JPanel();
		mainHeaderLabel = new JLabel();
		panel3 = new JPanel();
		subHeaderLabel = new JLabel();
		contentPanel = new JPanel();
		panel1 = new JPanel();
		label2 = new JLabel();
		filterField = new JTextField();
		resultSizeDisplay = new JLabel();
		scrollPane1 = new JScrollPane();
		servicesTable = new DomainSortableTable(Services.class, filterField);
		buttonBar = new JPanel();
		search = new JButton();
		listAll = new JButton();
		reportsButton = new JButton();
		addRecordButton = new JButton();
		removeRecordButton = new JButton();
		doneButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

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

				//======== panel2 ========
				{
					panel2.setBackground(new Color(80, 69, 57));
					panel2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel2.setLayout(new FormLayout(
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
					mainHeaderLabel.setText("Administration");
					mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					mainHeaderLabel.setForeground(Color.white);
					panel2.add(mainHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(panel2, cc.xy(1, 1));

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
					subHeaderLabel.setText("Services");
					subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					subHeaderLabel.setForeground(Color.white);
					panel3.add(subHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(panel3, cc.xy(2, 1));
			}
			dialogPane.add(HeaderPanel, BorderLayout.NORTH);

			//======== contentPanel ========
			{
				contentPanel.setOpaque(false);
				contentPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.RELATED_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.RELATED_GAP_COLSPEC
					},
					new RowSpec[] {
						FormFactory.UNRELATED_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.UNRELATED_GAP_ROWSPEC
					}));

				//======== panel1 ========
				{
					panel1.setOpaque(false);
					panel1.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label2 ----
					label2.setText("Filter:");
					panel1.add(label2, cc.xy(1, 1));
					panel1.add(filterField, cc.xy(3, 1));
				}
				contentPanel.add(panel1, cc.xy(2, 2));

				//---- resultSizeDisplay ----
				resultSizeDisplay.setText("resultSetDisplay");
				resultSizeDisplay.setBackground(new Color(238, 238, 238));
				contentPanel.add(resultSizeDisplay, cc.xy(2, 4));

				//======== scrollPane1 ========
				{

					//---- servicesTable ----
					servicesTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
					servicesTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							contentTableMouseClicked(e);
						}
					});
					scrollPane1.setViewportView(servicesTable);
				}
				contentPanel.add(scrollPane1, cc.xy(2, 6));

				//======== buttonBar ========
				{
					buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
					buttonBar.setOpaque(false);
					buttonBar.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.GLUE_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.BUTTON_COLSPEC
						},
						RowSpec.decodeSpecs("pref")));

					//---- search ----
					search.setText("Search");
					search.setOpaque(false);
					search.setEnabled(false);
					search.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							searchActionPerformed();
						}
					});
					buttonBar.add(search, cc.xy(1, 1));

					//---- listAll ----
					listAll.setText("List All");
					listAll.setOpaque(false);
					listAll.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							listAllActionPerformed();
						}
					});
					buttonBar.add(listAll, cc.xy(3, 1));

					//---- reportsButton ----
					reportsButton.setText("Reports");
					reportsButton.setOpaque(false);
					reportsButton.setEnabled(false);
					reportsButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							reportButtonActionPerformed(e);
						}
					});
					buttonBar.add(reportsButton, cc.xy(5, 1));

					//---- addRecordButton ----
					addRecordButton.setText("Add Record");
					addRecordButton.setOpaque(false);
					addRecordButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addRecordButtonActionPerformed(e);
						}
					});
					buttonBar.add(addRecordButton, cc.xy(7, 1));

					//---- removeRecordButton ----
					removeRecordButton.setText("Remove Record");
					removeRecordButton.setOpaque(false);
					removeRecordButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeRecordButtonActionPerformed(e);
						}
					});
					buttonBar.add(removeRecordButton, cc.xy(9, 1));

					//---- doneButton ----
					doneButton.setText("Done");
					doneButton.setOpaque(false);
					doneButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							doneButtonActionPerformed(e);
						}
					});
					buttonBar.add(doneButton, cc.xy(13, 1));
				}
				contentPanel.add(buttonBar, cc.xy(2, 8));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel HeaderPanel;
	private JPanel panel2;
	private JLabel mainHeaderLabel;
	private JPanel panel3;
	private JLabel subHeaderLabel;
	private JPanel contentPanel;
	private JPanel panel1;
	private JLabel label2;
	private JTextField filterField;
	private JLabel resultSizeDisplay;
	private JScrollPane scrollPane1;
	private DomainSortableTable servicesTable;
	private JPanel buttonBar;
	private JButton search;
	private JButton listAll;
	private JButton reportsButton;
	private JButton addRecordButton;
	private JButton removeRecordButton;
	private JButton doneButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	protected class DomainTableListEventListener implements ListEventListener {
		public void listChanged(ListEvent event) {
			updateRowCount();
		}
	}

	protected void updateRowCount() {
		rowCount = servicesTable.getFilteredList().size();
		int totalRecords = servicesTable.getEventList().size();
		String resultString;
		if (rowCount == totalRecords) {
			resultString = rowCount + " Record(s)";
		} else {
			resultString = rowCount + " of " + totalRecords + " Record(s)";
		}
		if (humanReadableSearchString != null && humanReadableSearchString.length() > 0) {
			resultString ="<html>" + resultString + " found for search \"<FONT COLOR='blue'>" + humanReadableSearchString + "</FONT>\"</html>";
		}
		resultSizeDisplay.setText(resultString);
	}

	private void initLookup() {
		SortedList sortedServices = new SortedList(servicesTable.getEventList());
		textFilteredIssues = new FilterList<DomainObject>(sortedServices, new TextComponentMatcherEditor(filterField, new DomainFilterator()));
		lookupTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Services.class));
		servicesTable.setModel(lookupTableModel);
		TableComparatorChooser tableSorter = new TableComparatorChooser(servicesTable, sortedServices, true);
		filterField.requestFocusInWindow();
		eventListener = new DomainTableListEventListener();
		servicesTable.getFilteredList().addListEventListener(eventListener);
	}

	@Override
	public void setClazz(Class clazz) throws DomainEditorCreationException {
		this.clazz = clazz;
		findAll();

		dialog = new DomainEditor(clazz, this, "Services", new ServiceFields());
		dialog.setNavigationButtonListeners((ActionListener)this);
		try {
			dialog.setCallingTable(getContentTable());
		} catch (UnsupportedTableModelException e) {
			new ErrorDialog("Error creating domain editor", e).showDialog();
		}
	}

	protected void findAll() {
		final DomainAccessObject access = new DomainAccessObjectImpl(Services.class);
		try {
			Collection results = access.findAll();
			servicesTable.updateCollection(results);
			updateRowCount();
			humanReadableSearchString = "list all";
			filterField.setText("");
		} catch (LookupException e) {
			new ErrorDialog("Error searching.", e).showDialog();
		}
	}

	public DomainSortableTable getContentTable() {
		return servicesTable;
	}

}
