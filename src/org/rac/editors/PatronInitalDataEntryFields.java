/*
 * ATReference Copyright © 2011 Rockefeller Archive Center

 * All rights reserved.
 *
 * This software is free. You can redistribute it and / or modify it under the terms of the Educational Community License (ECL)
 * version 1.0 (http://www.opensource.org/licenses/ecl1.php)
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the ECL license for more details about permissions and limitations.
 *
 *
 * ATReference
 * https://github.com/RockefellerArchiveCenter/ATReference/wiki
 *
 * Created by JFormDesigner on Sun Feb 27 14:03:54 EST 2011
 */

package org.rac.editors;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.rac.model.*;

public class PatronInitalDataEntryFields extends RAC_DomainEditorFields {
	public PatronInitalDataEntryFields() {
		initComponents();
	}

	@Override
	public Component getInitialFocusComponent() {
		return prefix;
	}

	private void sortNameConstruction(FocusEvent e) {
		((Patrons)this.getModel()).createSortName();
	}

	private void howDidYouHearAboutUsItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			addOtherTermIfNecessary(howDidYouHearAboutUs, contentPanel, Patrons.class, Patrons.PROPERTYNAME_HOW_DID_YOU_HEAR_ABOUT_US);
		}
	}

	private void componentFocusGained(FocusEvent e) {
				contentPanel.scrollRectToVisible(e.getComponent().getBounds());
		repaint();		
	}

	private void addressesTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				DomainEditor domainEditor = new DomainEditor(PatronAddresses.class, this.getParentEditor(), "PatronAddresses", new AddressFields());
				domainEditor.setCallingTable(addressesTable);
				domainEditor.setNavigationButtonListeners(domainEditor);
				editRelatedRecord(addressesTable, PatronAddresses.class, true, domainEditor);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog("Error creating editor for Dates", e1).showDialog();
			}
		}
	}

	private void addAddressActionPerformed() {
		try {
			DomainEditor dialogAddresses = new DomainEditor(PatronAddresses.class, this.getParentEditor(), "PatronAddresses", new AddressFields());
			dialogAddresses.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(dialogAddresses, PatronAddresses.class, addressesTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
	}

	private void removeAddressActionPerformed() {
		try {
			removeRelatedTableRow(addressesTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Address not removed", e).showDialog();
		}
	}

	private void setPreferedAddressActionPerformed() {
		if (addressesTable.getSelectedRowCount() != 1) {
			JOptionPane.showMessageDialog(this, "You must select one and only one address.");
		} else {
			ArrayList<DomainObject> selectedAddresses = addressesTable.getSelectedObjects();
			PatronAddresses selectedAddress = (PatronAddresses)selectedAddresses.get(0);
			PatronAddresses thisAddress;
			for (DomainObject o: addressesTable.getSortedList()) {
				thisAddress = (PatronAddresses)o;
				if (thisAddress.equals(selectedAddress)) {
					thisAddress.setPreferredAddress(true);
				} else {
					thisAddress.setPreferredAddress(false);
				}
			}
			//set record to dirty
			ApplicationFrame.getInstance().setRecordDirty();

			this.invalidate();
			this.validate();
			this.repaint();
		}
	}

	public JButton getAddAddress2() {
		return addAddress2;
	}

	private void phoneNumbersTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				DomainEditor domainEditor = new DomainEditor(PatronPhoneNumbers.class, this.getParentEditor(), "Phone Numbers", new PhoneNumberFields());
				domainEditor.setCallingTable(phoneNumbersTable);
				domainEditor.setNavigationButtonListeners(domainEditor);
				editRelatedRecord(phoneNumbersTable, PatronPhoneNumbers.class, true, domainEditor);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog("Error creating editor for Dates", e1).showDialog();
			}
		}
	}

	private void addPhoneNumberActionPerformed() {
		try {
			DomainEditor patronPhoneNumberssDialog = new DomainEditor(PatronPhoneNumbers.class, this.getParentEditor(), "Phone Numbers", new PhoneNumberFields());
			patronPhoneNumberssDialog.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(patronPhoneNumberssDialog, PatronPhoneNumbers.class, phoneNumbersTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
	}

	private void removePhoneNumberActionPerformed() {
		try {
			removeRelatedTableRow(phoneNumbersTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Phone Number not removed", e).showDialog();
		}
	}

	private void setPreferedPhoneNumberActionPerformed() {
		if (phoneNumbersTable.getSelectedRowCount() != 1) {
			JOptionPane.showMessageDialog(this, "You must select one and only one phone number.");
		} else {
			ArrayList<DomainObject> selectedPhoneNumbers = phoneNumbersTable.getSelectedObjects();
			PatronPhoneNumbers selectedPhoneNumber = (PatronPhoneNumbers)selectedPhoneNumbers.get(0);
			PatronPhoneNumbers thisPhoneNumber;
			for (DomainObject o: phoneNumbersTable.getSortedList()) {
				thisPhoneNumber = (PatronPhoneNumbers)o;
				if (thisPhoneNumber.equals(selectedPhoneNumber)) {
					thisPhoneNumber.setPreferredPhoneNumber(true);
				} else {
					thisPhoneNumber.setPreferredPhoneNumber(false);
				}
			}
			//set record to dirty
			ApplicationFrame.getInstance().setRecordDirty();

			this.invalidate();
			this.validate();
			this.repaint();
		}
	}

	public JButton getAddPhoneNumber() {
		return addPhoneNumber;
	}

	private void visitsTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				//todo this is very clumsy. The fields need to know their parent for controlling access
				//todo so you have to do things one step at a time.
				//create the domain editor
				DomainEditor domainEditor = new DomainEditor(PatronVisits.class, this.getParentEditor(), "Patron Visits");
				//create the fields with the editor as parent
				PatronVisitFields patronVisitsFields = new PatronVisitFields(domainEditor, true);
				//associate the fields with the editor
				domainEditor.setContentPanel(patronVisitsFields);
				domainEditor.editorFields = patronVisitsFields;
				domainEditor.setCallingTable(visitsTable);
				domainEditor.setNavigationButtonListeners(domainEditor);
				editRelatedRecord(visitsTable, PatronVisits.class, true, domainEditor);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog("Error creating editor for Visits", e1).showDialog();
			}
		}
	}

	private void addVisitActionPerformed() {
		try {
			//todo this is very clumsy. The fields need to know their parent for controlling access
			//todo so you have to do things one step at a time.
			//create the domain editor
			DomainEditor domainEditor = new DomainEditor(PatronVisits.class, this.getParentEditor(), "Patron Visits");
			domainEditor.setNewRecord(true);
			//create the fields with the editor as parent
			PatronVisitFields patronVisitsFields = new PatronVisitFields(domainEditor, true);
			//associate the fields with the editor
			domainEditor.setContentPanel(patronVisitsFields);
			domainEditor.editorFields = patronVisitsFields;

			domainEditor.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(domainEditor, PatronVisits.class, visitsTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
	}

	private void duplicateVisitActionPerformed() {
		duplicatePatronVisitRecord(visitsTable);
	}

	private void removeVisitActionPerformed() {
		try {
			removeRelatedTableRow(visitsTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Address not removed", e).showDialog();
		}
	}

	public JButton getAddVisit() {
		return addVisit;
	}

	public JButton getDuplicateVisit() {
		return duplicateVisit;
	}

	private void patronFormsTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				DomainEditor domainEditor = new DomainEditor(PatronForms.class, this.getParentEditor(), "Completed Forms", new PatronFormsFields());
				domainEditor.setCallingTable(patronFormsTable);
				domainEditor.setNavigationButtonListeners(domainEditor);
				editRelatedRecord(patronFormsTable, PatronForms.class, true, domainEditor);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog("Error creating editor for forms", e1).showDialog();
			}
		}
	}

	private void addFormActionPerformed() {
		try {
			DomainEditor domainEditor = new DomainEditor(PatronForms.class, this.getParentEditor(), "Completed Forms", new PatronFormsFields());
			domainEditor.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(domainEditor, PatronForms.class, patronFormsTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding form", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding form", e).showDialog();
		}
	}

	private void removeFormActionPerformed() {
		try {
			removeRelatedTableRow(patronFormsTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Form not removed", e).showDialog();
		}
	}

	public JButton getAddForm() {
		return addForm;
	}

	private void addFundingActionPerformed() {
		try {
			DomainEditor domainEditor = new DomainEditor(PatronFunding.class, this.getParentEditor(), "Funding", new FundingFields());
			domainEditor.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(domainEditor, PatronFunding.class, patronFundingTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding funding", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding funding", e).showDialog();
		}
	}

	private void removeFundingActionPerformed() {
		try {
			removeRelatedTableRow(patronFundingTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("funding not removed", e).showDialog();
		}
	}

	public JButton getAddFunding() {
		return addFunding;
	}

	private void patronFundingTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				DomainEditor domainEditor = new DomainEditor(PatronFunding.class, this.getParentEditor(), "Funding", new FundingFields());
				domainEditor.setCallingTable(patronFundingTable);
				domainEditor.setNavigationButtonListeners(domainEditor);
				editRelatedRecord(patronFundingTable, PatronFunding.class, true, domainEditor);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog("Error creating editor for funding", e1).showDialog();
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		scrollPane1 = new JScrollPane();
		contentPanel = new JPanel();
		label_namePersonalPrefix = new JLabel();
		prefix = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_PREFIX));
		label_namePersonalPrimaryName = new JLabel();
		namePersonalPrimaryName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_PRIMARY_NAME));
		label_namePersonalRestOfName = new JLabel();
		namePersonalRestOfName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_REST_OF_NAME));
		label_title = new JLabel();
		title = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_TITLE));
		label_namePersonalSuffix = new JLabel();
		namePersonalSuffix = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_SUFFIX));
		label_patronType = new JLabel();
		addressType = ATBasicComponentFactory.createComboBox(detailsModel, Patrons.PROPERTYNAME_PATRON_TYPE, Patrons.class,40);
		label_department = new JLabel();
		department = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_DEPARTMENT));
		label_institutionalAffiliation = new JLabel();
		institutionalAffiliation = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_INSTITUTIONAL_AFFILIATION));
		label_emailAddress2 = new JLabel();
		howDidYouHearAboutUs = ATBasicComponentFactory.createComboBox(detailsModel, Patrons.PROPERTYNAME_HOW_DID_YOU_HEAR_ABOUT_US, Patrons.class);
		label_partornNotes = new JLabel();
		scrollPane48 = new JScrollPane();
		patronNotes2 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Patrons.PROPERTYNAME_PATRON_NOTES));
		label_title2 = new JLabel();
		title2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_EMAIL1));
		title3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_EMAIL2));
		separator3 = new JSeparator();
		label_nameContactNotes3 = new JLabel();
		scrollPane8 = new JScrollPane();
		addressesTable = new DomainSortableTable(PatronAddresses.class);
		panel6 = new JPanel();
		addAddress2 = new JButton();
		removeAddress2 = new JButton();
		setPreferedAddress = new JButton();
		separator4 = new JSeparator();
		label_subjectScopeNote7 = new JLabel();
		scrollPane12 = new JScrollPane();
		phoneNumbersTable = new DomainSortableTable(PatronPhoneNumbers.class);
		panel16 = new JPanel();
		addPhoneNumber = new JButton();
		removePhoneNumber = new JButton();
		setPreferedPhoneNumber = new JButton();
		separator6 = new JSeparator();
		label_subjectScopeNote4 = new JLabel();
		scrollPane9 = new JScrollPane();
		visitsTable = new DomainSortableTable(PatronVisits.class);
		panel12 = new JPanel();
		addVisit = new JButton();
		duplicateVisit = new JButton();
		removeVisit = new JButton();
		separator7 = new JSeparator();
		label_subjectScopeNote8 = new JLabel();
		scrollPane13 = new JScrollPane();
		patronFormsTable = new DomainSortableTable(PatronForms.class);
		panel17 = new JPanel();
		addForm = new JButton();
		removeForm = new JButton();
		separator8 = new JSeparator();
		label_subjectScopeNote9 = new JLabel();
		scrollPane14 = new JScrollPane();
		patronFundingTable = new DomainSortableTable(PatronFunding.class);
		panel18 = new JPanel();
		addFunding = new JButton();
		removeFunding = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBackground(new Color(200, 205, 232));
		setPreferredSize(new Dimension(519, 800));
		setLayout(new FormLayout(
			"default:grow",
			"top:default:grow"));

		//======== scrollPane1 ========
		{
			scrollPane1.setBackground(new Color(200, 205, 232));
			scrollPane1.setPreferredSize(new Dimension(519, 1300));

			//======== contentPanel ========
			{
				contentPanel.setBorder(Borders.DIALOG_BORDER);
				contentPanel.setMinimumSize(new Dimension(500, 751));
				contentPanel.setPreferredSize(new Dimension(500, 1300));
				contentPanel.setBackground(new Color(200, 205, 232));
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
						new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
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
						new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
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

				//---- label_namePersonalPrefix ----
				label_namePersonalPrefix.setText("Prefix");
				label_namePersonalPrefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_namePersonalPrefix, Patrons.class, Patrons.PROPERTYNAME_PREFIX);
				contentPanel.add(label_namePersonalPrefix, cc.xy(1, 1));

				//---- prefix ----
				prefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				prefix.setColumns(10);
				prefix.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
					@Override
					public void focusLost(FocusEvent e) {
						sortNameConstruction(e);
					}
				});
				contentPanel.add(prefix, cc.xy(3, 1));

				//---- label_namePersonalPrimaryName ----
				label_namePersonalPrimaryName.setText("Primary Name");
				label_namePersonalPrimaryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_namePersonalPrimaryName, Patrons.class, Patrons.PROPERTYNAME_PRIMARY_NAME);
				contentPanel.add(label_namePersonalPrimaryName, cc.xy(1, 3));

				//---- namePersonalPrimaryName ----
				namePersonalPrimaryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				namePersonalPrimaryName.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
					@Override
					public void focusLost(FocusEvent e) {
						sortNameConstruction(e);
					}
				});
				contentPanel.add(namePersonalPrimaryName, cc.xy(3, 3));

				//---- label_namePersonalRestOfName ----
				label_namePersonalRestOfName.setText("Rest of Name");
				label_namePersonalRestOfName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_namePersonalRestOfName, Patrons.class, Patrons.PROPERTYNAME_REST_OF_NAME);
				contentPanel.add(label_namePersonalRestOfName, cc.xy(1, 5));

				//---- namePersonalRestOfName ----
				namePersonalRestOfName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				namePersonalRestOfName.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
					@Override
					public void focusLost(FocusEvent e) {
						sortNameConstruction(e);
					}
				});
				contentPanel.add(namePersonalRestOfName, cc.xy(3, 5));

				//---- label_title ----
				label_title.setText("Title");
				label_title.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_title, Patrons.class, Patrons.PROPERTYNAME_TITLE);
				contentPanel.add(label_title, cc.xy(1, 7));

				//---- title ----
				title.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				title.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
				});
				contentPanel.add(title, cc.xy(3, 7));

				//---- label_namePersonalSuffix ----
				label_namePersonalSuffix.setText("Suffix");
				label_namePersonalSuffix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_namePersonalSuffix, Patrons.class, Patrons.PROPERTYNAME_SUFFIX);
				contentPanel.add(label_namePersonalSuffix, cc.xy(1, 9));

				//---- namePersonalSuffix ----
				namePersonalSuffix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				namePersonalSuffix.setColumns(10);
				namePersonalSuffix.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
					@Override
					public void focusLost(FocusEvent e) {
						sortNameConstruction(e);
					}
				});
				contentPanel.add(namePersonalSuffix, cc.xy(3, 9));

				//---- label_patronType ----
				label_patronType.setText("PatronType");
				label_patronType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_patronType, Patrons.class, Patrons.PROPERTYNAME_PATRON_TYPE);
				contentPanel.add(label_patronType, cc.xy(1, 11));

				//---- addressType ----
				addressType.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
				});
				contentPanel.add(addressType, cc.xy(3, 11));

				//---- label_department ----
				label_department.setText("Department");
				label_department.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_department, Patrons.class, Patrons.PROPERTYNAME_DEPARTMENT);
				contentPanel.add(label_department, cc.xy(1, 13));

				//---- department ----
				department.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				department.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
				});
				contentPanel.add(department, cc.xy(3, 13));

				//---- label_institutionalAffiliation ----
				label_institutionalAffiliation.setText("<html>Institutional <br/>Affiliations</html>");
				label_institutionalAffiliation.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_institutionalAffiliation, Patrons.class, Patrons.PROPERTYNAME_INSTITUTIONAL_AFFILIATION);
				contentPanel.add(label_institutionalAffiliation, cc.xy(1, 15));

				//---- institutionalAffiliation ----
				institutionalAffiliation.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				institutionalAffiliation.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
				});
				contentPanel.add(institutionalAffiliation, cc.xy(3, 15));

				//---- label_emailAddress2 ----
				label_emailAddress2.setText("<html>How did you <br/>hear about <br/>the archive</html>");
				label_emailAddress2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_emailAddress2, Patrons.class, Patrons.PROPERTYNAME_HOW_DID_YOU_HEAR_ABOUT_US);
				contentPanel.add(label_emailAddress2, cc.xy(1, 17));

				//---- howDidYouHearAboutUs ----
				howDidYouHearAboutUs.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent e) {
						howDidYouHearAboutUsItemStateChanged(e);
					}
				});
				howDidYouHearAboutUs.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
				});
				contentPanel.add(howDidYouHearAboutUs, cc.xy(3, 17));

				//---- label_partornNotes ----
				label_partornNotes.setText("Patron Notes");
				label_partornNotes.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_partornNotes, Patrons.class, Patrons.PROPERTYNAME_PATRON_NOTES);
				contentPanel.add(label_partornNotes, cc.xy(1, 19));

				//======== scrollPane48 ========
				{
					scrollPane48.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane48.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					scrollPane48.setPreferredSize(new Dimension(200, 68));

					//---- patronNotes2 ----
					patronNotes2.setRows(10);
					patronNotes2.setLineWrap(true);
					patronNotes2.setWrapStyleWord(true);
					patronNotes2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					patronNotes2.setMinimumSize(new Dimension(200, 16));
					patronNotes2.addFocusListener(new FocusAdapter() {
						@Override
						public void focusGained(FocusEvent e) {
							componentFocusGained(e);
						}
					});
					scrollPane48.setViewportView(patronNotes2);
				}
				contentPanel.add(scrollPane48, cc.xy(3, 19));

				//---- label_title2 ----
				label_title2.setText("Email");
				label_title2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_title2, Patrons.class, Patrons.PROPERTYNAME_EMAIL1);
				contentPanel.add(label_title2, cc.xy(1, 21));

				//---- title2 ----
				title2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				title2.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
				});
				contentPanel.add(title2, cc.xy(3, 21));

				//---- title3 ----
				title3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				title3.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						componentFocusGained(e);
					}
				});
				contentPanel.add(title3, cc.xy(3, 23));

				//---- separator3 ----
				separator3.setBackground(new Color(220, 220, 232));
				separator3.setForeground(new Color(147, 131, 86));
				separator3.setMinimumSize(new Dimension(1, 10));
				separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator3, cc.xywh(1, 25, 3, 1));

				//---- label_nameContactNotes3 ----
				label_nameContactNotes3.setText("Addresses");
				label_nameContactNotes3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(label_nameContactNotes3, cc.xywh(1, 27, 3, 1));

				//======== scrollPane8 ========
				{
					scrollPane8.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- addressesTable ----
					addressesTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					addressesTable.setFocusable(false);
					addressesTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							addressesTableMouseClicked(e);
						}
					});
					scrollPane8.setViewportView(addressesTable);
				}
				contentPanel.add(scrollPane8, cc.xywh(1, 29, 3, 1));

				//======== panel6 ========
				{
					panel6.setBackground(new Color(231, 188, 251));
					panel6.setOpaque(false);
					panel6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel6.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- addAddress2 ----
					addAddress2.setText("Add Address");
					addAddress2.setOpaque(false);
					addAddress2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addAddress2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addAddressActionPerformed();
						}
					});
					panel6.add(addAddress2, cc.xy(1, 1));

					//---- removeAddress2 ----
					removeAddress2.setText("Remove Address");
					removeAddress2.setOpaque(false);
					removeAddress2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeAddress2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeAddressActionPerformed();
						}
					});
					panel6.add(removeAddress2, cc.xy(3, 1));

					//---- setPreferedAddress ----
					setPreferedAddress.setText("Set Preferred");
					setPreferedAddress.setOpaque(false);
					setPreferedAddress.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					setPreferedAddress.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							setPreferedAddressActionPerformed();
						}
					});
					panel6.add(setPreferedAddress, cc.xy(5, 1));
				}
				contentPanel.add(panel6, cc.xywh(1, 31, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator4 ----
				separator4.setBackground(new Color(220, 220, 232));
				separator4.setForeground(new Color(147, 131, 86));
				separator4.setMinimumSize(new Dimension(1, 10));
				separator4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator4, cc.xywh(1, 33, 3, 1));

				//---- label_subjectScopeNote7 ----
				label_subjectScopeNote7.setText("Phone Numbers");
				label_subjectScopeNote7.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(label_subjectScopeNote7, cc.xywh(1, 35, 3, 1));

				//======== scrollPane12 ========
				{
					scrollPane12.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					scrollPane12.setPreferredSize(new Dimension(300, 154));

					//---- phoneNumbersTable ----
					phoneNumbersTable.setPreferredScrollableViewportSize(new Dimension(250, 150));
					phoneNumbersTable.setFocusable(false);
					phoneNumbersTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							phoneNumbersTableMouseClicked(e);
						}
					});
					scrollPane12.setViewportView(phoneNumbersTable);
				}
				contentPanel.add(scrollPane12, cc.xywh(1, 37, 3, 1));

				//======== panel16 ========
				{
					panel16.setBackground(new Color(231, 188, 251));
					panel16.setOpaque(false);
					panel16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel16.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- addPhoneNumber ----
					addPhoneNumber.setText("Add Number");
					addPhoneNumber.setOpaque(false);
					addPhoneNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addPhoneNumber.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addPhoneNumberActionPerformed();
						}
					});
					addPhoneNumber.addFocusListener(new FocusAdapter() {
						@Override
						public void focusGained(FocusEvent e) {
							componentFocusGained(e);
						}
					});
					panel16.add(addPhoneNumber, cc.xy(1, 1));

					//---- removePhoneNumber ----
					removePhoneNumber.setText("Remove Number");
					removePhoneNumber.setOpaque(false);
					removePhoneNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removePhoneNumber.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removePhoneNumberActionPerformed();
						}
					});
					panel16.add(removePhoneNumber, cc.xy(3, 1));

					//---- setPreferedPhoneNumber ----
					setPreferedPhoneNumber.setText("Set Preferred");
					setPreferedPhoneNumber.setOpaque(false);
					setPreferedPhoneNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					setPreferedPhoneNumber.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							setPreferedPhoneNumberActionPerformed();
						}
					});
					panel16.add(setPreferedPhoneNumber, cc.xy(5, 1));
				}
				contentPanel.add(panel16, cc.xywh(1, 39, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator6 ----
				separator6.setBackground(new Color(220, 220, 232));
				separator6.setForeground(new Color(147, 131, 86));
				separator6.setMinimumSize(new Dimension(1, 10));
				separator6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator6, cc.xywh(1, 41, 3, 1));

				//---- label_subjectScopeNote4 ----
				label_subjectScopeNote4.setText("Visits");
				label_subjectScopeNote4.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(label_subjectScopeNote4, cc.xywh(1, 43, 3, 1));

				//======== scrollPane9 ========
				{
					scrollPane9.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- visitsTable ----
					visitsTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					visitsTable.setFocusable(false);
					visitsTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							visitsTableMouseClicked(e);
						}
					});
					scrollPane9.setViewportView(visitsTable);
				}
				contentPanel.add(scrollPane9, cc.xywh(1, 45, 3, 1));

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

					//---- addVisit ----
					addVisit.setText("Add Visit");
					addVisit.setOpaque(false);
					addVisit.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addVisit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addVisitActionPerformed();
						}
					});
					panel12.add(addVisit, cc.xy(1, 1));

					//---- duplicateVisit ----
					duplicateVisit.setText("Duplicate Visit");
					duplicateVisit.setOpaque(false);
					duplicateVisit.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					duplicateVisit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							duplicateVisitActionPerformed();
						}
					});
					panel12.add(duplicateVisit, cc.xy(3, 1));

					//---- removeVisit ----
					removeVisit.setText("Remove Visit");
					removeVisit.setOpaque(false);
					removeVisit.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeVisit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeVisitActionPerformed();
						}
					});
					panel12.add(removeVisit, cc.xy(5, 1));
				}
				contentPanel.add(panel12, cc.xywh(1, 47, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator7 ----
				separator7.setBackground(new Color(220, 220, 232));
				separator7.setForeground(new Color(147, 131, 86));
				separator7.setMinimumSize(new Dimension(1, 10));
				separator7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator7, cc.xywh(1, 51, 3, 1));

				//---- label_subjectScopeNote8 ----
				label_subjectScopeNote8.setText("Forms");
				label_subjectScopeNote8.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(label_subjectScopeNote8, cc.xywh(1, 53, 3, 1));

				//======== scrollPane13 ========
				{
					scrollPane13.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- patronFormsTable ----
					patronFormsTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					patronFormsTable.setFocusable(false);
					patronFormsTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							patronFormsTableMouseClicked(e);
						}
					});
					scrollPane13.setViewportView(patronFormsTable);
				}
				contentPanel.add(scrollPane13, cc.xywh(1, 55, 3, 1));

				//======== panel17 ========
				{
					panel17.setBackground(new Color(231, 188, 251));
					panel17.setOpaque(false);
					panel17.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel17.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- addForm ----
					addForm.setText("Add Form");
					addForm.setOpaque(false);
					addForm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addForm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addFormActionPerformed();
						}
					});
					panel17.add(addForm, cc.xy(1, 1));

					//---- removeForm ----
					removeForm.setText("Remove Form");
					removeForm.setOpaque(false);
					removeForm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeForm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeFormActionPerformed();
						}
					});
					panel17.add(removeForm, cc.xy(3, 1));
				}
				contentPanel.add(panel17, cc.xywh(1, 57, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator8 ----
				separator8.setBackground(new Color(220, 220, 232));
				separator8.setForeground(new Color(147, 131, 86));
				separator8.setMinimumSize(new Dimension(1, 10));
				separator8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(separator8, cc.xywh(1, 59, 3, 1));

				//---- label_subjectScopeNote9 ----
				label_subjectScopeNote9.setText("Funding");
				label_subjectScopeNote9.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				contentPanel.add(label_subjectScopeNote9, cc.xywh(1, 61, 3, 1));

				//======== scrollPane14 ========
				{
					scrollPane14.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- patronFundingTable ----
					patronFundingTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					patronFundingTable.setFocusable(false);
					patronFundingTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							patronFundingTableMouseClicked(e);
						}
					});
					scrollPane14.setViewportView(patronFundingTable);
				}
				contentPanel.add(scrollPane14, cc.xywh(1, 63, 3, 1));

				//======== panel18 ========
				{
					panel18.setBackground(new Color(231, 188, 251));
					panel18.setOpaque(false);
					panel18.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel18.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- addFunding ----
					addFunding.setText("Add Funding");
					addFunding.setOpaque(false);
					addFunding.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addFunding.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addFundingActionPerformed();
						}
					});
					panel18.add(addFunding, cc.xy(1, 1));

					//---- removeFunding ----
					removeFunding.setText("Remove Funding");
					removeFunding.setOpaque(false);
					removeFunding.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeFunding.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeFundingActionPerformed();
						}
					});
					panel18.add(removeFunding, cc.xy(3, 1));
				}
				contentPanel.add(panel18, cc.xywh(1, 65, 3, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
			}
			scrollPane1.setViewportView(contentPanel);
		}
		add(scrollPane1, cc.xy(1, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JScrollPane scrollPane1;
	private JPanel contentPanel;
	private JLabel label_namePersonalPrefix;
	public JTextField prefix;
	private JLabel label_namePersonalPrimaryName;
	public JTextField namePersonalPrimaryName;
	private JLabel label_namePersonalRestOfName;
	public JTextField namePersonalRestOfName;
	private JLabel label_title;
	public JTextField title;
	private JLabel label_namePersonalSuffix;
	public JTextField namePersonalSuffix;
	private JLabel label_patronType;
	private JComboBox addressType;
	private JLabel label_department;
	public JTextField department;
	private JLabel label_institutionalAffiliation;
	public JTextField institutionalAffiliation;
	private JLabel label_emailAddress2;
	private JComboBox howDidYouHearAboutUs;
	private JLabel label_partornNotes;
	private JScrollPane scrollPane48;
	public JTextArea patronNotes2;
	private JLabel label_title2;
	public JTextField title2;
	public JTextField title3;
	private JSeparator separator3;
	private JLabel label_nameContactNotes3;
	private JScrollPane scrollPane8;
	private DomainSortableTable addressesTable;
	private JPanel panel6;
	private JButton addAddress2;
	private JButton removeAddress2;
	private JButton setPreferedAddress;
	private JSeparator separator4;
	private JLabel label_subjectScopeNote7;
	private JScrollPane scrollPane12;
	private DomainSortableTable phoneNumbersTable;
	private JPanel panel16;
	private JButton addPhoneNumber;
	private JButton removePhoneNumber;
	private JButton setPreferedPhoneNumber;
	private JSeparator separator6;
	private JLabel label_subjectScopeNote4;
	private JScrollPane scrollPane9;
	private DomainSortableTable visitsTable;
	private JPanel panel12;
	private JButton addVisit;
	private JButton duplicateVisit;
	private JButton removeVisit;
	private JSeparator separator7;
	private JLabel label_subjectScopeNote8;
	private JScrollPane scrollPane13;
	private DomainSortableTable patronFormsTable;
	private JPanel panel17;
	private JButton addForm;
	private JButton removeForm;
	private JSeparator separator8;
	private JLabel label_subjectScopeNote9;
	private JScrollPane scrollPane14;
	private DomainSortableTable patronFundingTable;
	private JPanel panel18;
	private JButton addFunding;
	private JButton removeFunding;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
