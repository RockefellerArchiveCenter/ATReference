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
 * @author Lee Mandell
 * Date: Dec 4, 2009
 * Time: 5:12:59 PM
 */

package org.rac.editors;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.archiviststoolkit.exceptions.*;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.structure.FieldNotFoundException;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.StandardEditor;
import org.archiviststoolkit.util.SequencedObjectsUtils;
import org.rac.model.PatronAddresses;
import org.rac.model.Patrons;
import org.rac.utils.RACLookupListUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class RAC_DomainEditorFields extends DomainEditorFields {


	protected void addOtherTermIfNecessary(JComboBox comboBox, JPanel parentPanel, Class clazz, String fieldName) {
		LookupListItems selectedItem = (LookupListItems) comboBox.getSelectedItem();
		if (selectedItem.getListItem().equals(LookupList.OTHER_TEXT)) {
			try {
				System.out.println("other selected");
				String newTerm = JOptionPane.showInputDialog("Enter the new term");
				if (newTerm != null && newTerm.length() > 0) {
					FormLayout formLayout = (FormLayout) parentPanel.getLayout();
					CellConstraints cc = formLayout.getConstraints(comboBox);
					parentPanel.remove(comboBox);
					LookupListItems newItem = RACLookupListUtils.addItemToListByFieldProvisional(clazz, fieldName, newTerm);
					comboBox = ATBasicComponentFactory.createComboBox(detailsModel, fieldName, clazz);
					parentPanel.add(comboBox, cc);
					comboBox.setSelectedItem(newItem);
					this.invalidate();
					this.validate();
					this.repaint();
				} else {
					comboBox.setSelectedIndex(0);
					this.invalidate();
					this.validate();
					this.repaint();
				}
			} catch (FieldNotFoundException e1) {
				new ErrorDialog("Error adding item to lookup list", e1).showDialog();
			} catch (UnknownLookupListException e1) {
				new ErrorDialog("Error adding item to lookup list", e1).showDialog();
			}
		}
	}
	

	protected int editRelatedRecord(DomainGlazedListTable table, Class clazz, Boolean buffered) {
		return editRelatedRecord(table, clazz, buffered, null);
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

	public void removeRelatedTableRow(DomainGlazedListTable relatedTable, DomainObject model) throws ObjectNotRemovedException {
		int selectedRow = relatedTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select a row to remove.", "warning", JOptionPane.WARNING_MESSAGE);
		} else {
           int response = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete " + relatedTable.getSelectedRows().length + " record(s)",
                    "Delete records", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.OK_OPTION) {
                ArrayList<DomainObject> relatedObjects = relatedTable.removeSelectedRows();
                for (DomainObject relatedObject: relatedObjects) {
					removeRelatedObject(model, relatedObject);
                }
                int rowCount = relatedTable.getRowCount();
                if (rowCount == 0) {
                    // do nothing
                } else if (selectedRow >= rowCount) {
                    relatedTable.setRowSelectionInterval(rowCount - 1, rowCount - 1);
                } else {
                    relatedTable.setRowSelectionInterval(selectedRow, selectedRow);
                }
				//set record to dirty
				ApplicationFrame.getInstance().setRecordDirty();
            }
        }
	}

	private void removeRelatedObject(DomainObject model, DomainObject objectToRemove) throws ObjectNotRemovedException {

			if (objectToRemove instanceof PatronAddresses) {
				if (objectToRemove == null)
					throw new IllegalArgumentException("Can't remove a date.");
				((Patrons)model).getPatronAddresses().remove(objectToRemove);
			} else {
				model.removeRelatedObject(objectToRemove);
			}
	}

	protected void addRelatedRecord(DomainEditor dialog, Class objectClass, DomainGlazedListTable table) throws AddRelatedObjectException, DuplicateLinkException {
		addRelatedRecord(null, dialog, objectClass, table);
	}

	protected void addRelatedRecord(String whereString, DomainEditor dialog, Class objectClass, DomainGlazedListTable table) throws AddRelatedObjectException, DuplicateLinkException {

		DomainObject parentDomainObject = this.getModel();
		DomainObject objectToAdd = null;
		dialog.setNewRecord(true);
		Boolean done = false;
		int sequenceNumber = 0;
		Boolean first = true;
		int returnStatus;
		try {
		Constructor constructor = objectClass.getConstructor(parentDomainObject.getClass());

			while (!done) {
				objectToAdd = (DomainObject)constructor.newInstance(parentDomainObject);
				if (objectToAdd instanceof SequencedObject) {
					if (first) {
						sequenceNumber = SequencedObjectsUtils.determineSequenceOfNewItem(whereString, (DomainSortedTable)table);
						first = false;
					} else {
						sequenceNumber++;
					}
					((SequencedObject)objectToAdd).setSequenceNumber(sequenceNumber);
				}
				dialog.setModel(objectToAdd, null);
				returnStatus = dialog.showDialog();
				if (returnStatus == JOptionPane.OK_OPTION) {
					parentDomainObject.addRelatedObject(objectToAdd);
					table.getEventList().add(objectToAdd);
					done = true;
				} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
					parentDomainObject.addRelatedObject(objectToAdd);
					table.getEventList().add(objectToAdd);
				} else {
					done = true;
				}
			}
		} catch (InstantiationException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		} catch (IllegalAccessException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		} catch (InvocationTargetException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		} catch (NoSuchMethodException e) {
			dialog.setNewRecord(false);
			throw new AddRelatedObjectException(parentDomainObject, objectToAdd, e);
		}
		dialog.setNewRecord(false);


//		PatronAddresses newAddresses;
//		dialogAddresses.setNavigationButtonListeners((ActionListener)this.getParentEditor());
//		dialogAddresses.setNewRecord(true);
//
//		boolean done = false;
//		int returnStatus;
//		while (!done) {
//			newAddresses = new PatronAddresses(namesModel);
//			dialogAddresses.setModel(newAddresses, null);
//			returnStatus = dialogAddresses.showDialog();
//			if (returnStatus == JOptionPane.OK_OPTION) {
//				namesModel.addPatonsAddresses(newAddresses);
//				addressesTable.updateCollection(namesModel.getPatronAddresses());
//				done = true;
//			} else if (returnStatus == StandardEditor.OK_AND_ANOTHER_OPTION) {
//				namesModel.addPatonsAddresses(newAddresses);
//				addressesTable.updateCollection(namesModel.getPatronAddresses());
//			} else {
//				done = true;
//			}
//		}
	}
}
