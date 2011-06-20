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
 * Created by JFormDesigner on Wed Apr 19 14:46:37 EDT 2006
 */

package org.rac.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.sql.SQLException;
import javax.swing.*;

import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.report.ReportUtils;
import org.archiviststoolkit.report.ReportDestinationProperties;
import org.archiviststoolkit.exceptions.*;
import org.archiviststoolkit.Main;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.GeneralAdminDialog;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.util.StringHelper;
import org.hibernate.exception.ConstraintViolationException;
import org.apache.log4j.Logger;
import org.rac.model.Patrons;
import org.rac.editors.PatronFields;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import ca.odell.glazedlists.swing.EventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import org.rac.myDomain.PatronsDAO;

public class PatronManagement extends GeneralAdminDialog implements ActionListener {

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());
    private DomainObject currentSearchResultObject;
	protected String humanReadableSearchString = "";
	protected DomainTableListEventListener eventListener;
	int rowCount = 0;
    private PatronQueryEditor searchDialog;
	private Boolean standAlone = false;
	private Boolean readingRoomLogon = false;

	public PatronManagement() throws DomainEditorCreationException {
		super(false);
		standAlone = true;
		initComponents();
		setClazz(Patrons.class);
		initLookup();

		//if we have a reference staff logon then have the close window exit the application
		if (ApplicationFrame.getInstance().getCurrentUser().getAccessClass() == Users.ACCESS_CLASS_REFERENCE_STAFF) {
			addWindowListener(new WindowAdapter() {
				public void windowClosing(final WindowEvent windowevent) {
					System.exit(0);
				}
			});
		}
		// initialize the search dialog
        searchDialog = new PatronQueryEditor(this);
	}

	public PatronManagement(Frame owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		setClazz(Patrons.class);
		initLookup();

        // initialize the search dialog
        searchDialog = new PatronQueryEditor(this);
	}

	public PatronManagement(Dialog owner) throws DomainEditorCreationException {
		super(owner, false);
		initComponents();
		setClazz(Patrons.class);
		initLookup();
	}


	@Override
	public void setClazz(Class clazz) throws DomainEditorCreationException {
		this.clazz = clazz;

		//set up the dialog for editing records
//		dialog = DomainEditorFactory.getInstance().createDomainEditorWithParent(clazz, this, getContentTable(), true);
		dialog = new DomainEditor(Patrons.class, this, "Patrons", new PatronFields());
		dialog.setNavigationButtonListeners((ActionListener)this);
		try {
			dialog.setCallingTable(getContentTable());
		} catch (UnsupportedTableModelException e) {
			new ErrorDialog("Error creating domain editor", e).showDialog();
		}
	}

	protected void findAll() {
		final DomainAccessObject access = new DomainAccessObjectImpl(Patrons.class);
		try {
			Collection results = access.findAll();
			humanReadableSearchString = "list all";
			patronTable.updateCollection(results);
			filterField.setText("");
		} catch (LookupException e) {
			new ErrorDialog("Error searching.", e).showDialog();
		}
	}


	// TODO 5/5/2009 This method might have to be modified based on usability issue
    protected void removePatronButtonActionPerformed() {
		int selectedRow = getContentTable().getSelectedRow();

		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select a patron to remove.", "warning", JOptionPane.WARNING_MESSAGE);
		} else {
			int response = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the following patron record(s)",
				"Delete records", JOptionPane.YES_NO_OPTION);

			if (response == JOptionPane.OK_OPTION) {
				selectedRow = getContentTable().getSelectedRow();

					// get a list of patron search Results to delete
					final ArrayList<Patrons> patronsDeleteList = new ArrayList<Patrons>();
					for (int index : getContentTable().getSelectedRows()) {
						if (getContentTable().getFilteredList()!= null) {
							patronsDeleteList.add((Patrons)getContentTable().getFilteredList().get(index));
						} else {
							patronsDeleteList.add((Patrons)getContentTable().getSortedList().get(index));
						}
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
							int size = patronsDeleteList.size();
							for (DomainObject domainObject : patronsDeleteList) {
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

	public JTextField getFilterField() {
		return filterField;
	}

    /**
     * Method to print jasper report of assessments records
     *
     * @param progressPanel The progress panel
     * @param printScreenReport Is this report a print screen report
     * @return The list of records to be printing
     */
    private ArrayList<DomainObject> getResultSetForPrinting(InfiniteProgressPanel progressPanel, boolean printScreenReport) {
		ArrayList<DomainObject> listForPrinting = new ArrayList<DomainObject>();
		DomainAccessObject access = null;
		try {
			access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);
			int[] selectedRows = getContentTable().getSelectedRows();

            if(printScreenReport) { // just get all records listed for doing print screen
                for (DomainObject domainObject : textFilteredIssues) {
                    listForPrinting.add(domainObject);

                    // check to see if cancel wasn't press. If it was then return null
                    if(progressPanel.isProcessCancelled()) {
                        return null;
                    }
				}
            } else if (selectedRows.length == 0) {
				int count = 1;
				for (DomainObject domainObject : textFilteredIssues) {
					Patrons patrons = (Patrons)access.findByPrimaryKeyLongSession(domainObject.getIdentifier());
					listForPrinting.add(patrons);
					progressPanel.setTextLine("Loading record number " + count++ + " for printing", 1);
                    // check to see if cancel wasn't press. If it was then return null
                    if(progressPanel.isProcessCancelled()) {
                        return null;
                    }
				}
			} else {
				int count = 1;
				for (int selectedRow : selectedRows) {
					Patrons patrons = (Patrons)access.findByPrimaryKeyLongSession(getContentTable().getFilteredList().get(selectedRow).getIdentifier());
					listForPrinting.add(patrons);
					 progressPanel.setTextLine("Loading record number " + count++ + " for printing", 1);


                    // check to see if cancel wasn't press in that case return null;
                    if(progressPanel.isProcessCancelled()) {
                        return null;
                    }
				}
			}
		} catch (PersistenceException e) {
			new ErrorDialog("Error gathering records for printing", e).showDialog();
		} catch (LookupException e) {
			new ErrorDialog("Error gathering records for printing", e).showDialog();
		}

		return listForPrinting;
	}

	private void reportButtonActionPerformed(ActionEvent e) {
		ReportWorkerRunnable runnable = new ReportWorkerRunnable(this);
		Thread backgroundWorker = new Thread(runnable);
		backgroundWorker.start();
	}

    /**
     * Performes searching on patron records
     */
    private void searchActionPerformed() {
//		findAll();
//		humanReadableSearchString = "list all";

        if (searchDialog.showDialog() == JOptionPane.OK_OPTION) {
			final PatronsDAO access = new PatronsDAO();
            Thread performer = new Thread(new Runnable() {
                public void run() {
                    InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(ApplicationFrame.getInstance(), 0);
                    monitor.start("Performing search...");
                    try {
                        Collection results = access.findByQueryEditor(searchDialog, monitor);
						humanReadableSearchString = access.getHumanReadableSearchString();
						patronTable.updateCollection(results);
                         monitor.close();
//                    } catch (LookupException e) {
//                        monitor.close();
//                        new ErrorDialog(ApplicationFrame.getInstance(), "Error searching.", e).showDialog();
                    } finally {
                        monitor.close();
                    }
                }
            }, "Search");
            performer.start();
        }
    }

	protected void updateRowCount() {
		rowCount = patronTable.getFilteredList().size();
		int totalRecords = patronTable.getEventList().size();
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


	private void listAllActionPerformed() {
		findAll();
	}

	@Override
	public void showDialog() {
		updateRowCount();
		super.showDialog();
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
		patronTable = new DomainSortableTable(Patrons.class, filterField);
		buttonBar = new JPanel();
		search = new JButton();
		listAll = new JButton();
		reportsButton = new JButton();
		addPatronButton = new JButton();
		removePatronButton = new JButton();
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
					subHeaderLabel.setText("Patrons");
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

					//---- patronTable ----
					patronTable.setPreferredScrollableViewportSize(new Dimension(800, 400));
					patronTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							contentTableMouseClicked(e);
						}
					});
					scrollPane1.setViewportView(patronTable);
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
					reportsButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							reportButtonActionPerformed(e);
						}
					});
					buttonBar.add(reportsButton, cc.xy(5, 1));

					//---- addPatronButton ----
					addPatronButton.setText("Add Patron");
					addPatronButton.setOpaque(false);
					addPatronButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addPatronButtonActionPerformed(e);
						}
					});
					buttonBar.add(addPatronButton, cc.xy(7, 1));

					//---- removePatronButton ----
					removePatronButton.setText("Remove Patron");
					removePatronButton.setOpaque(false);
					removePatronButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removePatronButtonActionPerformed();
						}
					});
					buttonBar.add(removePatronButton, cc.xy(9, 1));

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
//                    dialog.clearRecordPositionText(); // printing record position in this dialog makes no sense
					dialog.setRecordPositionText(selectedRow, getContentTable().getFilteredList().size());

					if (readingRoomLogon) {
						((PatronFields)dialog.editorFields).updateUIForClass0();
					}
					Component temp = this.getParent();
                    int status = dialog.showDialog();

					if (status == JOptionPane.OK_OPTION) {
						access.updateLongSession(currentDomainObject);
						currentObjectSublist.set(0, currentDomainObject);
//                        // refresh the main display
//                        findAll();
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

	protected void addPatronButtonActionPerformed(ActionEvent e) {
		Patrons newPatron = null;

		dialog.setNewRecord(true);
		Boolean done = false;
		boolean createNewInstance = true;
		Boolean savedNewRecord;
 		int returnStatus;

		while (!done) {
			if (createNewInstance) {
				newPatron = new Patrons();
				newPatron.setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
			} else {
				createNewInstance = true;
			}

			dialog.setIncludeSaveButton(true);
            dialog.setModel((DomainObject) newPatron, null);

			returnStatus = dialog.showDialog();
			savedNewRecord = dialog.getSavedNewRecord();
			try {
				if (returnStatus == javax.swing.JOptionPane.OK_OPTION) {
					access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

					access.getLongSession(); // make sure we have a long session
					if (savedNewRecord) {
						access.updateLongSession(dialog.getModel());
					} else {
						access.add(dialog.getModel());
					}
					getContentTable().getEventList().add(newPatron);
					updateRowCount();
 					done = true;
				} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
					access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(clazz);

					access.getLongSession(); // make sure we have a long session
					if (savedNewRecord) {
						access.updateLongSession(dialog.getModel());
					} else {
						access.add(dialog.getModel());
					}
					getContentTable().getEventList().add(newPatron);
					updateRowCount();
                    dialog.setNewRecord(true); // tell the dialog this is a new record
				} else if (returnStatus == JOptionPane.CANCEL_OPTION) {
					try {
						access.closeLongSessionRollback();
					} catch (SQLException e1) {
						new ErrorDialog("Error canceling record.", e1).showDialog();
					}
				} else {
					done = true;
				}
			} catch (ConstraintViolationException persistenceException) {
				JOptionPane.showMessageDialog(this, "Can't save, Duplicate record:" + newPatron);
				((DomainObject) newPatron).removeIdAndAuditInfo();
				createNewInstance = false;

                // need to get close session here to prevent bug 99
                // when pressing the save button
                try {
                    access.closeLongSessionRollback();
                } catch (SQLException e2) {
                    new ErrorDialog("Error resetting session",
							StringHelper.getStackTrace(persistenceException)).showDialog();
                }
			} catch (PersistenceException persistenceException) {
				if (persistenceException.getCause() instanceof ConstraintViolationException) {
					JOptionPane.showMessageDialog(this,  "Can't save, Duplicate record:" + newPatron);
					((DomainObject) newPatron).removeIdAndAuditInfo();
					createNewInstance = false;

                    // need to close the session here to prevent bug 99
                    // when pressing the save button
                    try {
                        access.closeLongSessionRollback();
                    } catch (SQLException e2) {
                        new ErrorDialog("Error resetting session",
                                StringHelper.getStackTrace(persistenceException)).showDialog();
                    }
				} else {
					done = true;
					new ErrorDialog("Error saving new record.",
							StringHelper.getStackTrace(persistenceException)).showDialog();
				}
			}
		}
		dialog.setNewRecord(false);
//		findAll();
	}


	protected void doneButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
		//if we have a reference staff logon then have the close window exit the application
		if (ApplicationFrame.getInstance().getCurrentUser().getAccessClass() == Users.ACCESS_CLASS_REFERENCE_STAFF) {
			System.exit(0);
		}
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
	private DomainSortableTable patronTable;
	private JPanel buttonBar;
	private JButton search;
	private JButton listAll;
	private JButton reportsButton;
	private JButton addPatronButton;
	private JButton removePatronButton;
	private JButton doneButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	FilterList<DomainObject> textFilteredIssues;
	EventTableModel lookupTableModel;

	public DomainSortableTable getContentTable() {
		return patronTable;
	}

	public void setContentTable(DomainSortableTable contentTable) {
		this.patronTable = contentTable;
	}

	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
		Object actionSource = e.getSource();
		if (actionSource == dialog.getNextButton() ||
				actionSource == dialog.getPreviousButton() ||
				actionSource == dialog.getFirstButton() ||
				actionSource == dialog.getLastButton()) {
			if (dialog.getModel().validateAndDisplayDialog(e)) {
				//adjust the selected row
				int numberOfRows = getContentTable().getRowCount();
				if (actionSource == dialog.getNextButton()) {
					dialog.incrementSelectedRow();
				} else if (actionSource == dialog.getPreviousButton()) {
					dialog.decrementSelectedRow();
				} else if (actionSource == dialog.getFirstButton()) {
					dialog.setSelectedRow(0);
				} else if (actionSource == dialog.getLastButton()) {
					dialog.setSelectedRow(numberOfRows - 1);
				}
				dialog.setNavigationButtons();

				try {
//					int index = getTableModel().getIndex(currentDomainObject);
//					getTableModel().setDomainObject(index, currentDomainObject);
					access.updateLongSession(currentDomainObject);
					currentObjectSublist.set(0, currentSearchResultObject);
					int selectedRow = dialog.getSelectedRow();

                    currentSearchResultObject = textFilteredIssues.get(selectedRow);
					currentDomainObject = access.findByPrimaryKeyLongSession(currentSearchResultObject.getIdentifier());
					textFilteredIssues.set(selectedRow, currentSearchResultObject);
					currentObjectSublist = getContentTable().getFilteredList().subList(selectedRow, selectedRow + 1);
					getContentTable().getFilteredList().set(selectedRow, currentSearchResultObject);
					dialog.setModel(currentDomainObject, null);
					dialog.repaint();
					this.invalidate();
					this.repaint();
				} catch (PersistenceException persistenceException) {
					System.out.println("Persistence Exception " + persistenceException);
				} catch (LookupException lookupException) {
					System.out.println("Lookup Exception " + lookupException);
				}
			}
		}
	}


	private void initLookup() {
		SortedList sortedLocations = new SortedList(patronTable.getEventList());
		textFilteredIssues = new FilterList<DomainObject>(sortedLocations, new TextComponentMatcherEditor(filterField, new DomainFilterator()));
		lookupTableModel = new EventTableModel(textFilteredIssues, new DomainTableFormat(Patrons.class));
		patronTable.setModel(lookupTableModel);
		TableComparatorChooser tableSorter = new TableComparatorChooser(patronTable, sortedLocations, true);
		filterField.requestFocusInWindow();
		eventListener = new DomainTableListEventListener();
		patronTable.getFilteredList().addListEventListener(eventListener);
	}

  // dummy method to set the dialog title this prevents errors from being thrown
  public void setDialogTitle(String title) { }

	public Boolean isReadingRoomLogon() {
		return readingRoomLogon;
	}

	public void setReadingRoomLogon(Boolean readingRoomLogon) {
		this.readingRoomLogon = readingRoomLogon;
	}

	protected class DomainTableListEventListener implements ListEventListener {
		public void listChanged(ListEvent event) {
			updateRowCount();
		}
	}

	class ReportWorkerRunnable implements Runnable {
		  private Dialog parent;

		  ReportWorkerRunnable(Dialog parent) {
			  this.parent = parent;
		  }

		  public void run() {

			  try {
				  ReportDestinationProperties reportDestinationProperties = new ReportDestinationProperties(clazz, parent, true);

				  if (reportDestinationProperties.showDialog(textFilteredIssues.size())) {
					  InfiniteProgressPanel monitor = ATProgressUtil.createModalProgressMonitor(parent, 1000, true);
					  try {
						  // decide on what records to return based whether we are printing the screen
						  String reportTitle = reportDestinationProperties.getSelectedReport().getReportTitle();
						  ArrayList setForPrinting;

						  if(reportTitle.equals("Print Screen")) {
							  ReportDestinationProperties reportDestinationProperties2 = new ReportDestinationProperties(Patrons.class, parent, true);
							  reportDestinationProperties2.setReportDesitation(reportDestinationProperties.getReportDesitation());
							  reportDestinationProperties2.reportSaveDestination = reportDestinationProperties.reportSaveDestination;
							  reportDestinationProperties = reportDestinationProperties2;

							  setForPrinting = getResultSetForPrinting(monitor, true); // Get records for print screen
						  } else {
							  setForPrinting = getResultSetForPrinting(monitor, false); // Just get patron records
						  }

						  if(setForPrinting != null) {
							  ReportUtils.printReport(reportDestinationProperties, setForPrinting, monitor);
						  }
					  } catch (UnsupportedParentComponentException e) {
						  monitor.close();
						  new ErrorDialog("There was a problem printing the report", e).showDialog();
					  } finally {
						  // to ensure that progress dlg is closed in case of any exception
						  monitor.close();
					  }
				  }
			  } catch (UnsupportedClassException e) {
				  new ErrorDialog("There was a problem printing the report", e).showDialog();
			  } catch (UnsupportedReportDestination unsupportedReportDestination) {
				  new ErrorDialog("There was a problem printing the report", unsupportedReportDestination).showDialog();
			  } catch (UnsupportedReportType unsupportedReportType) {
				  new ErrorDialog("There was a problem printing the report", unsupportedReportType).showDialog();
			  }
		  }
	  }

}