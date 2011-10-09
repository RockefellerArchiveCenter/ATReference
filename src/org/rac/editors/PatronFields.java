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
 * Created by JFormDesigner on Sat Aug 27 11:09:03 EDT 2005
 */

package org.rac.editors;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.model.Accessions;
import org.archiviststoolkit.model.Repositories;
import org.archiviststoolkit.model.Users;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.mydomain.DomainObject;
import org.archiviststoolkit.mydomain.DomainEditorFields;
import org.archiviststoolkit.mydomain.DomainSortableTable;
import org.archiviststoolkit.swing.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.exceptions.ObjectNotRemovedException;
import org.archiviststoolkit.exceptions.AddRelatedObjectException;
import org.archiviststoolkit.exceptions.DuplicateLinkException;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.plugin.ATPlugin;
import org.archiviststoolkit.plugin.ATPluginFactory;
import org.rac.model.*;

public class PatronFields extends RAC_DomainEditorFields {
    private ArrayList<ATPlugin> plugins = null; // stores any embedded editor plugins
	private Boolean readingRoomLogon = false;

    public PatronFields() {
        super();
        initComponents();
		initAccess();
        initPlugins();
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


	private void sortNameConstruction(FocusEvent e) {
		((Patrons)this.getModel()).createSortName();		
	}

	private void createAutomaticallyStateChanged(ChangeEvent e) {
		enableDisableSortName(sortName, true);
	}


	protected void enableDisableSortName(JTextField sortName, Boolean updateSortName) {
		Patrons patronModel = (Patrons)this.getModel();
		if (patronModel != null && patronModel.getCreateSortNameAutomatically()) {
			sortName.setEditable(false);
			sortName.setOpaque(false);
			if (updateSortName) {
				patronModel.createSortName();
			}
		} else {
			sortName.setEditable(true);
			sortName.setOpaque(true);
		}
	}

	private void visitsTableMouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				try {
					//todo this is very clumsy. The fields need to know their parent for controlling access
					//todo so you have to do things one step at a time.
					//create the domain editor
					DomainEditor domainEditor = new DomainEditor(PatronVisits.class, this.getParentEditor(), "Patron Visits");
					//create the fields with the editor as parent
					PatronVisitFields patronVisitsFields = new PatronVisitFields(domainEditor);
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
			PatronVisitFields patronVisitsFields = new PatronVisitFields(domainEditor);
			//associate the fields with the editor
			domainEditor.setContentPanel(patronVisitsFields);
			domainEditor.editorFields = patronVisitsFields;

			domainEditor.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(domainEditor, PatronVisits.class, visitsTable);
            domainEditor.pack();
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
	}

	private void removeVisitActionPerformed() {
		try {
			removeRelatedTableRow(visitsTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Address not removed", e).showDialog();
		}
	}

	private void fundingTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				DomainEditor domainEditor = new DomainEditor(PatronFunding.class, this.getParentEditor(), "Funding", new FundingFields());
				domainEditor.setCallingTable(fundingTable);
				domainEditor.setNavigationButtonListeners(domainEditor);
				editRelatedRecord(fundingTable, PatronFunding.class, true, domainEditor);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog("Error creating editor for Dates", e1).showDialog();
			}
		}
	}

	private void addFundingActionPerformed() {
		try {
			DomainEditor patronVisitsDialog = new DomainEditor(PatronFunding.class, this.getParentEditor(), "Funding", new FundingFields());
			patronVisitsDialog.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(patronVisitsDialog, PatronFunding.class, fundingTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
	}

	private void removeFundingActionPerformed() {
		try {
			removeRelatedTableRow(fundingTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Address not removed", e).showDialog();
		}
	}

	public JButton getAddFunding() {
		return addFunding;
	}

	public JButton getAddPublication() {
		return addPublication;
	}

	private void publicationsTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			try {
				DomainEditor domainEditor = new DomainEditor(PatronPublications.class, this.getParentEditor(), "Publications", new PublicationFields());
				domainEditor.setCallingTable(publicationsTable);
				domainEditor.setNavigationButtonListeners(domainEditor);
				editRelatedRecord(publicationsTable, PatronPublications.class, true, domainEditor);
			} catch (UnsupportedTableModelException e1) {
				new ErrorDialog("Error creating editor for Dates", e1).showDialog();
			}
		}
	}

	private void removePublicationActionPerformed() {
		try {
			removeRelatedTableRow(publicationsTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Address not removed", e).showDialog();
		}
	}

	private void addPublicationActionPerformed() {
		try {
			DomainEditor patronVisitsDialog = new DomainEditor(PatronPublications.class, this.getParentEditor(), "Publications", new PublicationFields());
			patronVisitsDialog.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(patronVisitsDialog, PatronPublications.class, publicationsTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
	}

	public JButton getAddPhoneNumber() {
		return addPhoneNumber;
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

	public JButton getAddVisit() {
		return addVisit;
	}

	private void duplicateVisitActionPerformed() {
		if (visitsTable.getSelectedRowCount() != 1) {
			JOptionPane.showMessageDialog(this, "You must select one and only one visit to duplicate.");
		} else {
			int selectedRow = visitsTable.getSelectedRow();
			PatronVisits visitToDuplicate = (PatronVisits)visitsTable.getSortedList().get(selectedRow);
			PatronVisits newVisit = new PatronVisits(new Date(),
					visitToDuplicate.getVisitType(),
					visitToDuplicate.getContactArchivist(),
					visitToDuplicate.getTopic(),
					visitToDuplicate.getDetailsOnResourcesUsed(),
					visitToDuplicate.getUserDefinedString1(),
					visitToDuplicate.getUserDefinedBoolean1(),
					visitToDuplicate.getUserDefinedText1(),
					visitToDuplicate.getPatron());
			//research purposes
			for (PatronVisitsResearchPurposes researchPurpose: visitToDuplicate.getResearchPurposes()) {
				newVisit.addResearchPurposes(researchPurpose.getResearchPurpose());
			}
//			newVisit.setResearchPurposes(visitToDuplicate.getResearchPurposes());
			//duplicate subjects
			try {
				for (PatronVisitsSubjects patronVisitsSubject: visitToDuplicate.getSubjects()) {
					newVisit.addSubject(patronVisitsSubject.getSubject());
				}
				//duplicate names
				for (PatronVisitsNames patronVisitName: visitToDuplicate.getNames()) {
					newVisit.addName(patronVisitName.getName());
				}
			} catch (DuplicateLinkException e) {
				new ErrorDialog("Error adding a name or subject", e).showDialog();
			}
			((Patrons)this.getModel()).addPatronVisit(newVisit);
			visitsTable.getEventList().add(newVisit);
			ApplicationFrame.getInstance().setRecordDirty();
		}
	}

	public JButton getDuplicateVisit() {
		return duplicateVisit;
	}

	public JButton getAddAddress2() {
		return addAddress2;
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

	public JButton getAddForm() {
		return addForm;
	}

	public JButton getAddService() {
		return addService;
	}

	private void addFormActionPerformed() {
		try {
			DomainEditor domainEditor = new DomainEditor(PatronForms.class, this.getParentEditor(), "Completed Forms", new PatronFormsFields());
			domainEditor.setNavigationButtonListeners((ActionListener)this.getParentEditor());
			addRelatedRecord(domainEditor, PatronForms.class, patronFormsTable);
		} catch (AddRelatedObjectException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		} catch (DuplicateLinkException e) {
			new ErrorDialog("Error adding address", e).showDialog();
		}
	}

	private void removeFormActionPerformed() {
		try {
			removeRelatedTableRow(patronFormsTable, this.getModel());
		} catch (ObjectNotRemovedException e) {
			new ErrorDialog("Address not removed", e).showDialog();
		}
	}

	private void howDidYouHearAboutUsItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			addOtherTermIfNecessary(howDidYouHearAboutUs, panel14, Patrons.class, Patrons.PROPERTYNAME_HOW_DID_YOU_HEAR_ABOUT_US);
		}

	}

	private void changeRepositoryButtonActionPerformed() {
		Vector repositories = Repositories.getRepositoryList();
		Repositories currentRepostory = ((Patrons) this.getModel()).getRepository();
		Patrons model = (Patrons) this.getModel();
        SelectFromList dialog = new SelectFromList(this.getParentEditor(), "Select a repository", repositories.toArray());
        dialog.setSelectedValue(currentRepostory);
        if (dialog.showDialog() == JOptionPane.OK_OPTION) {
            model.setRepository((Repositories)dialog.getSelectedValue());
            setRepositoryText(model);
        }
	}

	public JButton getChangeRepositoryButton() {
		return changeRepositoryButton;
	}

	private void setRepositoryText(Patrons model) {
		if (model.getRepository() == null) {
			this.repositoryName.setText("");
		} else {
			this.repositoryName.setText(model.getRepository().getShortName());
		}
	}


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		sortNameDisplay = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_SORT_NAME));
		tabbedPane = new JTabbedPane();
		mainPatronFieldsPanel = new JPanel();
		panel1 = new JPanel();
		panel7 = new JPanel();
		label_namePersonalPrefix = new JLabel();
		namePersonalPrefix = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_PREFIX));
		label_namePersonalPrimaryName = new JLabel();
		namePersonalPrimaryName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_PRIMARY_NAME));
		label_namePersonalRestOfName = new JLabel();
		namePersonalRestOfName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_REST_OF_NAME));
		label_namePersonalSuffix = new JLabel();
		namePersonalSuffix = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_SUFFIX));
		separator1 = new JSeparator();
		panel14 = new JPanel();
		label_patronType = new JLabel();
		addressType = ATBasicComponentFactory.createComboBox(detailsModel, Patrons.PROPERTYNAME_PATRON_TYPE, Patrons.class,40);
		label_department = new JLabel();
		department = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_DEPARTMENT));
		label_title = new JLabel();
		title = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_TITLE));
		label_institutionalAffiliation = new JLabel();
		institutionalAffiliation = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_INSTITUTIONAL_AFFILIATION));
		label_emailAddress2 = new JLabel();
		howDidYouHearAboutUs = ATBasicComponentFactory.createComboBox(detailsModel, Patrons.PROPERTYNAME_HOW_DID_YOU_HEAR_ABOUT_US, Patrons.class);
		panel2 = new JPanel();
		label_sortName = new JLabel();
		panel5 = new JPanel();
		sortName = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_SORT_NAME));
		namePersonalDirectOrder2 = ATBasicComponentFactory.createCheckBox(detailsModel, Patrons.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY, Patrons.class);
		label_partornNotes = new JLabel();
		scrollPane48 = new JScrollPane();
		patronNotes = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Patrons.PROPERTYNAME_PATRON_NOTES));
		label_emailAddress1 = new JLabel();
		panel10 = new JPanel();
		emailAddress1 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_EMAIL1));
		emailAddress2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_EMAIL2));
		panel4 = new JPanel();
		label_nameContactNotes3 = new JLabel();
		label_subjectScopeNote7 = new JLabel();
		scrollPane8 = new JScrollPane();
		addressesTable = new DomainSortableTable(PatronAddresses.class);
		scrollPane12 = new JScrollPane();
		phoneNumbersTable = new DomainSortableTable(PatronPhoneNumbers.class);
		panel6 = new JPanel();
		addAddress2 = new JButton();
		removeAddress2 = new JButton();
		setPreferedAddress = new JButton();
		panel13 = new JPanel();
		addPhoneNumber = new JButton();
		removePhoneNumber = new JButton();
		setPreferedPhoneNumber = new JButton();
		panel15 = new JPanel();
		label_repositoryName = new JLabel();
		repositoryName = new JTextField();
		changeRepositoryButton = new JButton();
		inactiveCheckBox = ATBasicComponentFactory.createCheckBox(detailsModel, Patrons.PROPERTYNAME_INACTIVE, Patrons.class);
		panel11 = new JPanel();
		label_subjectScopeNote4 = new JLabel();
		scrollPane9 = new JScrollPane();
		visitsTable = new DomainSortableTable(PatronVisits.class);
		panel12 = new JPanel();
		addVisit = new JButton();
		duplicateVisit = new JButton();
		removeVisit = new JButton();
		separator3 = new JSeparator();
		label_subjectScopeNote8 = new JLabel();
		scrollPane13 = new JScrollPane();
		patronFormsTable = new DomainSortableTable(PatronForms.class);
		panel16 = new JPanel();
		addForm = new JButton();
		removeForm = new JButton();
		separator4 = new JSeparator();
		label_subjectScopeNote9 = new JLabel();
		scrollPane14 = new JScrollPane();
		servicesTable = new DomainSortableTable();
		panel17 = new JPanel();
		addService = new JButton();
		removeService = new JButton();
		nonPreferredNamePanel = new JPanel();
		label_subjectScopeNote5 = new JLabel();
		scrollPane10 = new JScrollPane();
		fundingTable = new DomainSortableTable(PatronFunding.class);
		panel8 = new JPanel();
		addFunding = new JButton();
		removeFunding = new JButton();
		separator2 = new JSeparator();
		label_subjectScopeNote6 = new JLabel();
		scrollPane11 = new JScrollPane();
		publicationsTable = new DomainSortableTable(PatronPublications.class);
		panel9 = new JPanel();
		addPublication = new JButton();
		removePublication = new JButton();
		panel31 = new JPanel();
		panel32 = new JPanel();
		label_acknowledgementDate2 = new JLabel();
		acknowledgementDate2 = ATBasicComponentFactory.createDateField(detailsModel.getModel( Patrons.PROPERTYNAME_USER_DEFINED_DATE1));
		label_acknowledgementDate3 = new JLabel();
		acknowledgementDate3 = ATBasicComponentFactory.createDateField(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_DATE2));
		rights2 = ATBasicComponentFactory.createCheckBox(detailsModel, Patrons.PROPERTYNAME_USER_DEFINED_BOOLEAN1, Patrons.class);
		rights3 = ATBasicComponentFactory.createCheckBox(detailsModel, Patrons.PROPERTYNAME_USER_DEFINED_BOOLEAN2, Patrons.class);
		label_date1Begin2 = new JLabel();
		date1Begin2 = ATBasicComponentFactory.createIntegerField(detailsModel,Patrons.PROPERTYNAME_USER_DEFINED_INTEGER1);
		label_date1Begin3 = new JLabel();
		date1Begin3 = ATBasicComponentFactory.createIntegerField(detailsModel, Patrons.PROPERTYNAME_USER_DEFINED_INTEGER2);
		label_date1Begin4 = new JLabel();
		extentNumber2 = ATBasicComponentFactory.createDoubleField(detailsModel, Patrons.PROPERTYNAME_USER_DEFINED_REAL1);
		label_date1Begin5 = new JLabel();
		extentNumber3 = ATBasicComponentFactory.createDoubleField(detailsModel, Patrons.PROPERTYNAME_USER_DEFINED_REAL2);
		label_date1Begin6 = new JLabel();
		dateExpression2 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_STRING1),false);
		label_date1Begin7 = new JLabel();
		dateExpression3 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_STRING2),false);
		label_date1Begin8 = new JLabel();
		dateExpression4 = ATBasicComponentFactory.createTextField(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_STRING3),false);
		label_date1Begin9 = new JLabel();
		scrollPane44 = new JScrollPane();
		title3 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_TEXT1));
		panel35 = new JPanel();
		label_date1Begin10 = new JLabel();
		scrollPane45 = new JScrollPane();
		title4 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_TEXT2));
		label_date1Begin11 = new JLabel();
		scrollPane46 = new JScrollPane();
		title5 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_TEXT3));
		label_date1Begin12 = new JLabel();
		scrollPane47 = new JScrollPane();
		title6 = ATBasicComponentFactory.createTextArea(detailsModel.getModel(Patrons.PROPERTYNAME_USER_DEFINED_TEXT4));
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

			//======== mainPatronFieldsPanel ========
			{
				mainPatronFieldsPanel.setOpaque(false);
				mainPatronFieldsPanel.setLayout(new FormLayout(
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
						new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//======== panel1 ========
				{
					panel1.setOpaque(false);
					panel1.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("top:default")));
					((FormLayout)panel1.getLayout()).setColumnGroups(new int[][] {{1, 5}});

					//======== panel7 ========
					{
						panel7.setOpaque(false);
						panel7.setLayout(new FormLayout(
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
								FormFactory.DEFAULT_ROWSPEC
							}));

						//---- label_namePersonalPrefix ----
						label_namePersonalPrefix.setText("Prefix");
						label_namePersonalPrefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_namePersonalPrefix, Patrons.class, Patrons.PROPERTYNAME_PREFIX);
						panel7.add(label_namePersonalPrefix, cc.xy(1, 1));

						//---- namePersonalPrefix ----
						namePersonalPrefix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						namePersonalPrefix.setColumns(10);
						namePersonalPrefix.addFocusListener(new FocusAdapter() {
							@Override
							public void focusLost(FocusEvent e) {
								sortNameConstruction(e);
							}
						});
						panel7.add(namePersonalPrefix, cc.xy(3, 1));

						//---- label_namePersonalPrimaryName ----
						label_namePersonalPrimaryName.setText("Primary Name");
						label_namePersonalPrimaryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_namePersonalPrimaryName, Patrons.class, Patrons.PROPERTYNAME_PRIMARY_NAME);
						panel7.add(label_namePersonalPrimaryName, cc.xy(1, 3));

						//---- namePersonalPrimaryName ----
						namePersonalPrimaryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						namePersonalPrimaryName.addFocusListener(new FocusAdapter() {
							@Override
							public void focusLost(FocusEvent e) {
								sortNameConstruction(e);
							}
						});
						panel7.add(namePersonalPrimaryName, cc.xy(3, 3));

						//---- label_namePersonalRestOfName ----
						label_namePersonalRestOfName.setText("Rest of Name");
						label_namePersonalRestOfName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_namePersonalRestOfName, Patrons.class, Patrons.PROPERTYNAME_REST_OF_NAME);
						panel7.add(label_namePersonalRestOfName, cc.xy(1, 5));

						//---- namePersonalRestOfName ----
						namePersonalRestOfName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						namePersonalRestOfName.addFocusListener(new FocusAdapter() {
							@Override
							public void focusLost(FocusEvent e) {
								sortNameConstruction(e);
							}
						});
						panel7.add(namePersonalRestOfName, cc.xy(3, 5));

						//---- label_namePersonalSuffix ----
						label_namePersonalSuffix.setText("Suffix");
						label_namePersonalSuffix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_namePersonalSuffix, Patrons.class, Patrons.PROPERTYNAME_SUFFIX);
						panel7.add(label_namePersonalSuffix, cc.xy(1, 7));

						//---- namePersonalSuffix ----
						namePersonalSuffix.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						namePersonalSuffix.setColumns(10);
						namePersonalSuffix.addFocusListener(new FocusAdapter() {
							@Override
							public void focusLost(FocusEvent e) {
								sortNameConstruction(e);
							}
						});
						panel7.add(namePersonalSuffix, cc.xy(3, 7));
					}
					panel1.add(panel7, cc.xy(1, 1));

					//---- separator1 ----
					separator1.setOrientation(SwingConstants.VERTICAL);
					panel1.add(separator1, cc.xy(3, 1, CellConstraints.CENTER, CellConstraints.FILL));

					//======== panel14 ========
					{
						panel14.setOpaque(false);
						panel14.setLayout(new FormLayout(
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
								new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.NO_GROW)
							}));

						//---- label_patronType ----
						label_patronType.setText("PatronType");
						label_patronType.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_patronType, Patrons.class, Patrons.PROPERTYNAME_PATRON_TYPE);
						panel14.add(label_patronType, cc.xy(1, 1));
						panel14.add(addressType, cc.xy(3, 1));

						//---- label_department ----
						label_department.setText("Department");
						label_department.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_department, Patrons.class, Patrons.PROPERTYNAME_DEPARTMENT);
						panel14.add(label_department, cc.xy(1, 3));

						//---- department ----
						department.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						panel14.add(department, cc.xy(3, 3));

						//---- label_title ----
						label_title.setText("Title");
						label_title.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_title, Patrons.class, Patrons.PROPERTYNAME_TITLE);
						panel14.add(label_title, cc.xy(1, 5));

						//---- title ----
						title.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						panel14.add(title, cc.xy(3, 5));

						//---- label_institutionalAffiliation ----
						label_institutionalAffiliation.setText("<html>Institutional <br/>Affiliations</html>");
						label_institutionalAffiliation.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_institutionalAffiliation, Patrons.class, Patrons.PROPERTYNAME_INSTITUTIONAL_AFFILIATION);
						panel14.add(label_institutionalAffiliation, cc.xy(1, 7));

						//---- institutionalAffiliation ----
						institutionalAffiliation.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						panel14.add(institutionalAffiliation, cc.xy(3, 7));

						//---- label_emailAddress2 ----
						label_emailAddress2.setText("<html>How did you <br/>hear about <br/>the archive</html>");
						label_emailAddress2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						ATFieldInfo.assignLabelInfo(label_emailAddress2, Patrons.class, Patrons.PROPERTYNAME_HOW_DID_YOU_HEAR_ABOUT_US);
						panel14.add(label_emailAddress2, cc.xy(1, 9));

						//---- howDidYouHearAboutUs ----
						howDidYouHearAboutUs.addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent e) {
								howDidYouHearAboutUsItemStateChanged(e);
							}
						});
						panel14.add(howDidYouHearAboutUs, cc.xy(3, 9));
					}
					panel1.add(panel14, cc.xy(5, 1));
				}
				mainPatronFieldsPanel.add(panel1, cc.xywh(1, 1, 3, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== panel2 ========
				{
					panel2.setOpaque(false);
					panel2.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));
				}
				mainPatronFieldsPanel.add(panel2, cc.xy(3, 1));

				//---- label_sortName ----
				label_sortName.setText("Sort Name");
				label_sortName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_sortName, Patrons.class, Patrons.PROPERTYNAME_SORT_NAME);
				mainPatronFieldsPanel.add(label_sortName, cc.xy(1, 3));

				//======== panel5 ========
				{
					panel5.setOpaque(false);
					panel5.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- sortName ----
					sortName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel5.add(sortName, cc.xy(1, 1));

					//---- namePersonalDirectOrder2 ----
					namePersonalDirectOrder2.setText("Create automatically");
					namePersonalDirectOrder2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					namePersonalDirectOrder2.setOpaque(false);
					namePersonalDirectOrder2.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent e) {
							createAutomaticallyStateChanged(e);
						}
					});
					namePersonalDirectOrder2.setText(ATFieldInfo.getLabel(Patrons.class, Patrons.PROPERTYNAME_CREATE_SORT_NAME_AUTOMATICALLY));
					panel5.add(namePersonalDirectOrder2, cc.xy(3, 1));
				}
				mainPatronFieldsPanel.add(panel5, cc.xy(3, 3));

				//---- label_partornNotes ----
				label_partornNotes.setText("Patron Notes");
				label_partornNotes.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_partornNotes, Patrons.class, Patrons.PROPERTYNAME_PATRON_NOTES);
				mainPatronFieldsPanel.add(label_partornNotes, cc.xy(1, 5));

				//======== scrollPane48 ========
				{
					scrollPane48.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane48.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					scrollPane48.setPreferredSize(new Dimension(200, 68));

					//---- patronNotes ----
					patronNotes.setRows(4);
					patronNotes.setLineWrap(true);
					patronNotes.setWrapStyleWord(true);
					patronNotes.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					patronNotes.setMinimumSize(new Dimension(200, 16));
					scrollPane48.setViewportView(patronNotes);
				}
				mainPatronFieldsPanel.add(scrollPane48, cc.xy(3, 5));

				//---- label_emailAddress1 ----
				label_emailAddress1.setText("Email address");
				label_emailAddress1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				ATFieldInfo.assignLabelInfo(label_emailAddress1, Patrons.class, Patrons.PROPERTYNAME_EMAIL1);
				mainPatronFieldsPanel.add(label_emailAddress1, cc.xy(1, 7));

				//======== panel10 ========
				{
					panel10.setOpaque(false);
					panel10.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));
					((FormLayout)panel10.getLayout()).setColumnGroups(new int[][] {{1, 3}});

					//---- emailAddress1 ----
					emailAddress1.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					emailAddress1.addFocusListener(new FocusAdapter() {
						@Override
						public void focusLost(FocusEvent e) {
							sortNameConstruction(e);
						}
					});
					panel10.add(emailAddress1, cc.xy(1, 1));

					//---- emailAddress2 ----
					emailAddress2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					emailAddress2.addFocusListener(new FocusAdapter() {
						@Override
						public void focusLost(FocusEvent e) {
							sortNameConstruction(e);
						}
					});
					panel10.add(emailAddress2, cc.xy(3, 1));
				}
				mainPatronFieldsPanel.add(panel10, cc.xy(3, 7));

				//======== panel4 ========
				{
					panel4.setOpaque(false);
					panel4.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- label_nameContactNotes3 ----
					label_nameContactNotes3.setText("Addresses");
					label_nameContactNotes3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel4.add(label_nameContactNotes3, cc.xy(1, 1));

					//---- label_subjectScopeNote7 ----
					label_subjectScopeNote7.setText("Phone Numbers");
					label_subjectScopeNote7.setVerticalAlignment(SwingConstants.TOP);
					label_subjectScopeNote7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel4.add(label_subjectScopeNote7, cc.xy(3, 1));

					//======== scrollPane8 ========
					{
						scrollPane8.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

						//---- addressesTable ----
						addressesTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
						addressesTable.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								addressesTableMouseClicked(e);
							}
						});
						scrollPane8.setViewportView(addressesTable);
					}
					panel4.add(scrollPane8, cc.xy(1, 3));

					//======== scrollPane12 ========
					{
						scrollPane12.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
						scrollPane12.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						scrollPane12.setPreferredSize(new Dimension(300, 154));

						//---- phoneNumbersTable ----
						phoneNumbersTable.setPreferredScrollableViewportSize(new Dimension(250, 150));
						phoneNumbersTable.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								phoneNumbersTableMouseClicked(e);
							}
						});
						scrollPane12.setViewportView(phoneNumbersTable);
					}
					panel4.add(scrollPane12, cc.xy(3, 3));

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
					panel4.add(panel6, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));

					//======== panel13 ========
					{
						panel13.setBackground(new Color(231, 188, 251));
						panel13.setOpaque(false);
						panel13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						panel13.setLayout(new FormLayout(
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
						panel13.add(addPhoneNumber, cc.xy(1, 1));

						//---- removePhoneNumber ----
						removePhoneNumber.setText("Remove Number");
						removePhoneNumber.setOpaque(false);
						removePhoneNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						removePhoneNumber.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								removePhoneNumberActionPerformed();
							}
						});
						panel13.add(removePhoneNumber, cc.xy(3, 1));

						//---- setPreferedPhoneNumber ----
						setPreferedPhoneNumber.setText("Set Preferred");
						setPreferedPhoneNumber.setOpaque(false);
						setPreferedPhoneNumber.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
						setPreferedPhoneNumber.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								setPreferedPhoneNumberActionPerformed();
							}
						});
						panel13.add(setPreferedPhoneNumber, cc.xy(5, 1));
					}
					panel4.add(panel13, cc.xy(3, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));
				}
				mainPatronFieldsPanel.add(panel4, cc.xywh(1, 9, 3, 1));

				//======== panel15 ========
				{
					panel15.setOpaque(false);
					panel15.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.NO_GROW),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(ColumnSpec.RIGHT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						},
						RowSpec.decodeSpecs("default")));

					//---- label_repositoryName ----
					label_repositoryName.setText("Repository");
					label_repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_repositoryName, Accessions.class, Accessions.PROPERTYNAME_REPOSITORY);
					panel15.add(label_repositoryName, cc.xy(1, 1));

					//---- repositoryName ----
					repositoryName.setEditable(false);
					repositoryName.setFocusable(false);
					repositoryName.setBorder(null);
					repositoryName.setOpaque(false);
					repositoryName.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					repositoryName.setHorizontalAlignment(SwingConstants.LEFT);
					panel15.add(repositoryName, cc.xy(3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

					//---- changeRepositoryButton ----
					changeRepositoryButton.setText("Change Repository");
					changeRepositoryButton.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					changeRepositoryButton.setOpaque(false);
					changeRepositoryButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							changeRepositoryButtonActionPerformed();
						}
					});
					panel15.add(changeRepositoryButton, cc.xy(5, 1));

					//---- inactiveCheckBox ----
					inactiveCheckBox.setText("Inactive");
					inactiveCheckBox.setOpaque(false);
					inactiveCheckBox.setText(ATFieldInfo.getLabel(Patrons.class, Patrons.PROPERTYNAME_INACTIVE));
					panel15.add(inactiveCheckBox, cc.xy(7, 1));
				}
				mainPatronFieldsPanel.add(panel15, cc.xywh(1, 11, 3, 1));
			}
			tabbedPane.addTab("Patron Information", mainPatronFieldsPanel);


			//======== panel11 ========
			{
				panel11.setOpaque(false);
				panel11.setLayout(new FormLayout(
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

				//---- label_subjectScopeNote4 ----
				label_subjectScopeNote4.setText("Visits");
				label_subjectScopeNote4.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel11.add(label_subjectScopeNote4, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane9 ========
				{
					scrollPane9.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- visitsTable ----
					visitsTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					visitsTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							visitsTableMouseClicked(e);
						}
					});
					scrollPane9.setViewportView(visitsTable);
				}
				panel11.add(scrollPane9, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));

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
				panel11.add(panel12, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator3 ----
				separator3.setBackground(new Color(220, 220, 232));
				separator3.setForeground(new Color(147, 131, 86));
				separator3.setMinimumSize(new Dimension(1, 10));
				separator3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel11.add(separator3, cc.xy(1, 7));

				//---- label_subjectScopeNote8 ----
				label_subjectScopeNote8.setText("Forms");
				label_subjectScopeNote8.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel11.add(label_subjectScopeNote8, cc.xy(1, 9, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane13 ========
				{
					scrollPane13.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane13.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- patronFormsTable ----
					patronFormsTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					patronFormsTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							patronFormsTableMouseClicked(e);
						}
					});
					scrollPane13.setViewportView(patronFormsTable);
				}
				panel11.add(scrollPane13, cc.xy(1, 11, CellConstraints.DEFAULT, CellConstraints.FILL));

				//======== panel16 ========
				{
					panel16.setBackground(new Color(231, 188, 251));
					panel16.setOpaque(false);
					panel16.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel16.setLayout(new FormLayout(
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
					panel16.add(addForm, cc.xy(1, 1));

					//---- removeForm ----
					removeForm.setText("Remove Form");
					removeForm.setOpaque(false);
					removeForm.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeForm.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeFormActionPerformed();
						}
					});
					panel16.add(removeForm, cc.xy(3, 1));
				}
				panel11.add(panel16, cc.xy(1, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator4 ----
				separator4.setBackground(new Color(220, 220, 232));
				separator4.setForeground(new Color(147, 131, 86));
				separator4.setMinimumSize(new Dimension(1, 10));
				separator4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel11.add(separator4, cc.xy(1, 15));

				//---- label_subjectScopeNote9 ----
				label_subjectScopeNote9.setText("Services (coming soon)");
				label_subjectScopeNote9.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				panel11.add(label_subjectScopeNote9, cc.xy(1, 17, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane14 ========
				{
					scrollPane14.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane14.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- servicesTable ----
					servicesTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					servicesTable.setEnabled(false);
					scrollPane14.setViewportView(servicesTable);
				}
				panel11.add(scrollPane14, cc.xy(1, 19, CellConstraints.DEFAULT, CellConstraints.FILL));

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

					//---- addService ----
					addService.setText("Add Service");
					addService.setOpaque(false);
					addService.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addService.setEnabled(false);
					panel17.add(addService, cc.xy(1, 1));

					//---- removeService ----
					removeService.setText("Remove Service");
					removeService.setOpaque(false);
					removeService.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeService.setEnabled(false);
					panel17.add(removeService, cc.xy(3, 1));
				}
				panel11.add(panel17, cc.xy(1, 21, CellConstraints.CENTER, CellConstraints.DEFAULT));
			}
			tabbedPane.addTab("Visits, Forms and Services", panel11);


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
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label_subjectScopeNote5 ----
				label_subjectScopeNote5.setText("Funding");
				label_subjectScopeNote5.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(label_subjectScopeNote5, cc.xy(1, 1, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane10 ========
				{
					scrollPane10.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane10.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- fundingTable ----
					fundingTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					fundingTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							fundingTableMouseClicked(e);
						}
					});
					scrollPane10.setViewportView(fundingTable);
				}
				nonPreferredNamePanel.add(scrollPane10, cc.xy(1, 3, CellConstraints.DEFAULT, CellConstraints.FILL));

				//======== panel8 ========
				{
					panel8.setBackground(new Color(231, 188, 251));
					panel8.setOpaque(false);
					panel8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel8.setLayout(new FormLayout(
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
					panel8.add(addFunding, cc.xy(1, 1));

					//---- removeFunding ----
					removeFunding.setText("Remove Funding");
					removeFunding.setOpaque(false);
					removeFunding.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removeFunding.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removeFundingActionPerformed();
						}
					});
					panel8.add(removeFunding, cc.xy(3, 1));
				}
				nonPreferredNamePanel.add(panel8, cc.xy(1, 5, CellConstraints.CENTER, CellConstraints.DEFAULT));

				//---- separator2 ----
				separator2.setBackground(new Color(220, 220, 232));
				separator2.setForeground(new Color(147, 131, 86));
				separator2.setMinimumSize(new Dimension(1, 10));
				separator2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(separator2, cc.xy(1, 7));

				//---- label_subjectScopeNote6 ----
				label_subjectScopeNote6.setText("Publications");
				label_subjectScopeNote6.setVerticalAlignment(SwingConstants.TOP);
				label_subjectScopeNote6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				nonPreferredNamePanel.add(label_subjectScopeNote6, cc.xy(1, 9, CellConstraints.DEFAULT, CellConstraints.TOP));

				//======== scrollPane11 ========
				{
					scrollPane11.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					scrollPane11.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));

					//---- publicationsTable ----
					publicationsTable.setPreferredScrollableViewportSize(new Dimension(450, 150));
					publicationsTable.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							publicationsTableMouseClicked(e);
						}
					});
					scrollPane11.setViewportView(publicationsTable);
				}
				nonPreferredNamePanel.add(scrollPane11, cc.xy(1, 11, CellConstraints.DEFAULT, CellConstraints.FILL));

				//======== panel9 ========
				{
					panel9.setBackground(new Color(231, 188, 251));
					panel9.setOpaque(false);
					panel9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel9.setLayout(new FormLayout(
						new ColumnSpec[] {
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC
						},
						RowSpec.decodeSpecs("default")));

					//---- addPublication ----
					addPublication.setText("Add Publications");
					addPublication.setOpaque(false);
					addPublication.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					addPublication.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							addPublicationActionPerformed();
						}
					});
					panel9.add(addPublication, cc.xy(1, 1));

					//---- removePublication ----
					removePublication.setText("Remove Publication");
					removePublication.setOpaque(false);
					removePublication.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					removePublication.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							removePublicationActionPerformed();
						}
					});
					panel9.add(removePublication, cc.xy(3, 1));
				}
				nonPreferredNamePanel.add(panel9, cc.xy(1, 13, CellConstraints.CENTER, CellConstraints.DEFAULT));
			}
			tabbedPane.addTab("Funding and Publications", nonPreferredNamePanel);


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
					ATFieldInfo.assignLabelInfo(label_acknowledgementDate2, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_DATE1);
					panel32.add(label_acknowledgementDate2, cc.xy(1, 1));

					//---- acknowledgementDate2 ----
					acknowledgementDate2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					acknowledgementDate2.setColumns(10);
					panel32.add(acknowledgementDate2, cc.xy(3, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_acknowledgementDate3 ----
					label_acknowledgementDate3.setText("User Defined Date 2");
					label_acknowledgementDate3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_acknowledgementDate3, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_DATE2);
					panel32.add(label_acknowledgementDate3, cc.xy(1, 3));

					//---- acknowledgementDate3 ----
					acknowledgementDate3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					acknowledgementDate3.setColumns(10);
					panel32.add(acknowledgementDate3, cc.xy(3, 3, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- rights2 ----
					rights2.setText("User Defined Boolean 1");
					rights2.setOpaque(false);
					rights2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					rights2.setText(ATFieldInfo.getLabel(Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_BOOLEAN1));
					panel32.add(rights2, cc.xywh(1, 5, 3, 1));

					//---- rights3 ----
					rights3.setText("User Defined Boolean 2");
					rights3.setOpaque(false);
					rights3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					rights3.setText(ATFieldInfo.getLabel(Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_BOOLEAN2));
					panel32.add(rights3, cc.xywh(1, 7, 3, 1));

					//---- label_date1Begin2 ----
					label_date1Begin2.setText("User Defined Integer 1");
					label_date1Begin2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin2, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_INTEGER1);
					panel32.add(label_date1Begin2, cc.xy(1, 9));

					//---- date1Begin2 ----
					date1Begin2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					date1Begin2.setColumns(6);
					panel32.add(date1Begin2, cc.xy(3, 9, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin3 ----
					label_date1Begin3.setText("User Defined Integer 2");
					label_date1Begin3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin3, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_INTEGER2);
					panel32.add(label_date1Begin3, cc.xy(1, 11));

					//---- date1Begin3 ----
					date1Begin3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					date1Begin3.setColumns(6);
					panel32.add(date1Begin3, cc.xy(3, 11, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin4 ----
					label_date1Begin4.setText("User Defined Real 1");
					label_date1Begin4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin4,Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_REAL1);
					panel32.add(label_date1Begin4, cc.xy(1, 13));

					//---- extentNumber2 ----
					extentNumber2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					extentNumber2.setColumns(5);
					panel32.add(extentNumber2, cc.xy(3, 13, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin5 ----
					label_date1Begin5.setText("User Defined Real 2");
					label_date1Begin5.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin5, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_REAL2);
					panel32.add(label_date1Begin5, cc.xy(1, 15));

					//---- extentNumber3 ----
					extentNumber3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					extentNumber3.setColumns(5);
					panel32.add(extentNumber3, cc.xy(3, 15, CellConstraints.LEFT, CellConstraints.DEFAULT));

					//---- label_date1Begin6 ----
					label_date1Begin6.setText("User Defined String 1");
					label_date1Begin6.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin6, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_STRING1);
					panel32.add(label_date1Begin6, cc.xy(1, 17));

					//---- dateExpression2 ----
					dateExpression2.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel32.add(dateExpression2, new CellConstraints(3, 17, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

					//---- label_date1Begin7 ----
					label_date1Begin7.setText("User Defined String 2");
					label_date1Begin7.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin7, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_STRING2);
					panel32.add(label_date1Begin7, cc.xy(1, 19));

					//---- dateExpression3 ----
					dateExpression3.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel32.add(dateExpression3, new CellConstraints(3, 19, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

					//---- label_date1Begin8 ----
					label_date1Begin8.setText("User Defined String 3");
					label_date1Begin8.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin8, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_STRING3);
					panel32.add(label_date1Begin8, cc.xy(1, 21));

					//---- dateExpression4 ----
					dateExpression4.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					panel32.add(dateExpression4, new CellConstraints(3, 21, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets(0, 0, 0, 5)));

					//---- label_date1Begin9 ----
					label_date1Begin9.setText("User Defined Text 1");
					label_date1Begin9.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					ATFieldInfo.assignLabelInfo(label_date1Begin9, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_TEXT1);
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
					ATFieldInfo.assignLabelInfo(label_date1Begin10, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_TEXT2);
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
					ATFieldInfo.assignLabelInfo(label_date1Begin11, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_TEXT3);
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
					ATFieldInfo.assignLabelInfo(label_date1Begin12, Patrons.class, Patrons.PROPERTYNAME_USER_DEFINED_TEXT4);
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
			tabbedPane.addTab("User Defined Fields", panel31);

		}
		add(tabbedPane, cc.xy(1, 2));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JTextField sortNameDisplay;
	private JTabbedPane tabbedPane;
	private JPanel mainPatronFieldsPanel;
	private JPanel panel1;
	private JPanel panel7;
	private JLabel label_namePersonalPrefix;
	public JTextField namePersonalPrefix;
	private JLabel label_namePersonalPrimaryName;
	public JTextField namePersonalPrimaryName;
	private JLabel label_namePersonalRestOfName;
	public JTextField namePersonalRestOfName;
	private JLabel label_namePersonalSuffix;
	public JTextField namePersonalSuffix;
	private JSeparator separator1;
	private JPanel panel14;
	private JLabel label_patronType;
	private JComboBox addressType;
	private JLabel label_department;
	public JTextField department;
	private JLabel label_title;
	public JTextField title;
	private JLabel label_institutionalAffiliation;
	public JTextField institutionalAffiliation;
	private JLabel label_emailAddress2;
	private JComboBox howDidYouHearAboutUs;
	private JPanel panel2;
	private JLabel label_sortName;
	private JPanel panel5;
	public JTextField sortName;
	public JCheckBox namePersonalDirectOrder2;
	private JLabel label_partornNotes;
	private JScrollPane scrollPane48;
	public JTextArea patronNotes;
	private JLabel label_emailAddress1;
	private JPanel panel10;
	public JTextField emailAddress1;
	public JTextField emailAddress2;
	private JPanel panel4;
	private JLabel label_nameContactNotes3;
	private JLabel label_subjectScopeNote7;
	private JScrollPane scrollPane8;
	private DomainSortableTable addressesTable;
	private JScrollPane scrollPane12;
	private DomainSortableTable phoneNumbersTable;
	private JPanel panel6;
	private JButton addAddress2;
	private JButton removeAddress2;
	private JButton setPreferedAddress;
	private JPanel panel13;
	private JButton addPhoneNumber;
	private JButton removePhoneNumber;
	private JButton setPreferedPhoneNumber;
	private JPanel panel15;
	private JLabel label_repositoryName;
	public JTextField repositoryName;
	private JButton changeRepositoryButton;
	private JCheckBox inactiveCheckBox;
	private JPanel panel11;
	private JLabel label_subjectScopeNote4;
	private JScrollPane scrollPane9;
	private DomainSortableTable visitsTable;
	private JPanel panel12;
	private JButton addVisit;
	private JButton duplicateVisit;
	private JButton removeVisit;
	private JSeparator separator3;
	private JLabel label_subjectScopeNote8;
	private JScrollPane scrollPane13;
	private DomainSortableTable patronFormsTable;
	private JPanel panel16;
	private JButton addForm;
	private JButton removeForm;
	private JSeparator separator4;
	private JLabel label_subjectScopeNote9;
	private JScrollPane scrollPane14;
	private DomainSortableTable servicesTable;
	private JPanel panel17;
	private JButton addService;
	private JButton removeService;
	private JPanel nonPreferredNamePanel;
	private JLabel label_subjectScopeNote5;
	private JScrollPane scrollPane10;
	private DomainSortableTable fundingTable;
	private JPanel panel8;
	private JButton addFunding;
	private JButton removeFunding;
	private JSeparator separator2;
	private JLabel label_subjectScopeNote6;
	private JScrollPane scrollPane11;
	private DomainSortableTable publicationsTable;
	private JPanel panel9;
	private JButton addPublication;
	private JButton removePublication;
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


    protected JPopupMenu insertContactNotePopUpMenu = new JPopupMenu();
    private Component currentPrimaryNamePanel;


    /**
     * Sets the model for this editor.
     *
     * @param model the model to be used
     */

    public final void setModel(final DomainObject model, InfiniteProgressPanel progressPanel) {
        super.setModel(model, progressPanel);

        Patrons patronModel = (Patrons) model;
		addressesTable.updateCollection(patronModel.getPatronAddresses());
		visitsTable.updateCollection(patronModel.getPatronVisits());
		fundingTable.updateCollection(patronModel.getPatronFunding());
		publicationsTable.updateCollection(patronModel.getPatronPublications());
		phoneNumbersTable.updateCollection(patronModel.getPatronPhoneNumbers());
		patronFormsTable.updateCollection(patronModel.getPatronForms());

		setRepositoryText(patronModel);

        setPluginModel(); // update any plugins with this new domain object
    }

    public Component getInitialFocusComponent() {
        return namePersonalPrefix;
    }

    protected void setDisplayToFirstTab() {
        this.tabbedPane.setSelectedIndex(0);
    }

	public void updateUIForClass0 (){
		tabbedPane.remove(3);
		//visits
		duplicateVisit.setEnabled(false);
		removeVisit.setEnabled(false);
		//forms
//		addForm.setEnabled(false);
//		removeForm.setEnabled(false);
		//publications
		addPublication.setEnabled(false);
		removePublication.setEnabled(false);
		
		readingRoomLogon = true;
	}

	protected void initAccess() {
		if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_SUPERUSER)) {
			if (getChangeRepositoryButton() != null) {
				getChangeRepositoryButton().setVisible(false);
			}
		}

        // see whether to hide the inactive checkbox. It should only be used for level
        // 2 and above users
        if (!Users.doesCurrentUserHaveAccess(Users.ACCESS_CLASS_ADVANCED_DATA_ENTRY)) {
            inactiveCheckBox.setVisible(false);
		}
	}


    /**
     * Method that initializes any embedded plugins that would add an editor
     */
    private void initPlugins() {
        plugins = ATPluginFactory.getInstance().getEmbeddedPatronEditorPlugins();
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