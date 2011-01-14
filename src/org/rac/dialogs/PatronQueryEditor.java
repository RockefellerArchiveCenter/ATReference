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
 * Created by JFormDesigner on Fri Apr 30 15:42:25 EDT 2010
 */

package org.rac.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.dialog.*;
import org.archiviststoolkit.hibernate.ATSearchCriterion;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DatabaseTablesDAO;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DatabaseFields;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.util.LookupListUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.rac.model.*;

public class PatronQueryEditor extends QueryEditor {

	private static final String SELECT_A_FIELD = "Select a field";

	public PatronQueryEditor(Frame owner) {
		super(owner);
		setClazz(Patrons.class);
		initComponents();
		createPopupValues();
	}

	public PatronQueryEditor(Dialog owner) {
		super(owner);
		setClazz(Patrons.class);
		initComponents();
		createPopupValues();
	}

	private void thisWindowClosing() {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	private void fieldSelector1ActionPerformed(ActionEvent e) {
		if (fieldSelector1.getSelectedIndex() == 0) {
			contentPane.remove(currentValue1Component);
			currentValue1Component = placeHolder1;
			contentPane.add(currentValue1Component, value1CellConstraints);
		} else {
			QueryField queryField = (QueryField) fieldSelector1.getSelectedItem();
			contentPane.remove(currentValue1Component);
			currentValue1Component = queryField.getValueComponent();
			contentPane.add(currentValue1Component, value1CellConstraints);
			currentValue1Component.requestInitialFocus();
		}
		this.invalidate();
		this.validate();
		this.repaint();
	}

	private void fieldSelector2ActionPerformed(ActionEvent e) {
		if (fieldSelector2.getSelectedIndex() == 0) {
			contentPane.remove(currentValue2Component);
			currentValue2Component = placeHolder2;
			contentPane.add(currentValue2Component, value2CellConstraints);
		} else {
			QueryField queryField = (QueryField) fieldSelector2.getSelectedItem();
			contentPane.remove(currentValue2Component);
			currentValue2Component = queryField.getValueComponent();
			contentPane.add(currentValue2Component, value2CellConstraints);
			currentValue2Component.requestInitialFocus();
		}
		this.invalidate();
		this.validate();
		this.repaint();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	private void queryButtonActionPerformed(ActionEvent e) {
		//make sure there is information filled out
		if (fieldSelector1.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "You must select a field first.");

		} else if (!currentValue1Component.validDataEntered()) {
			JOptionPane.showMessageDialog(this, "You must enter information to search on first.");

		} else if (fieldSelector2.getSelectedIndex() != 0 && !currentValue2Component.validDataEntered()) {
			JOptionPane.showMessageDialog(this, "You must enter information to search on first.");

		} else {
			status = javax.swing.JOptionPane.OK_OPTION;
			this.setVisible(false);
		}
	}

	private void lookupNameActionPerformed() {
		NameAuthorityLookup nameLookupDialog = new NameAuthorityLookup(this);
		if (nameLookupDialog.showDialog() == JOptionPane.OK_OPTION) {
			selectedName = nameLookupDialog.getSelectedName();
			name.setText(selectedName.getSortName());
		}
	}

	private void clearNameActionPerformed() {
		selectedName = null;
		name.setText("");
	}

	public Subjects getSelectedSubject() {
		return selectedSubject;
	}

	public Names getSelectedName() {
		return selectedName;
	}

	private void visitDateEndFocusGained() {
		endDateFocusGained(visitDateEnd, visitDateStart);
		if (visitDateEnd.getValue() == null) {
			visitDateEnd.setValue(visitDateStart.getValue());
			/* After a formatted text field gains focus, it replaces its text with its
			 * current value, formatted appropriately of course. It does this _after_
			 * any focus listeners are notified. So, if we are editable, we queue
			 * up a selectAll to be done after the current events in the thread are done. */
			Runnable doSelect = new Runnable() {
				public void run() {
					visitDateEnd.selectAll();
				}
			};
			SwingUtilities.invokeLater(doSelect);
		}
	}

	private void researchPurposeActionPerformed() {
		selectedResearchPurpose = (String) researchPurpose.getSelectedItem();
	}

	private void queryButton2ActionPerformed(ActionEvent e) {
		if (selectedResearchPurpose.length() == 0
				&& contactArchivist.getText().length() == 0
				&& visitTopic.getText().length() == 0
				&& !areVisitDatesFilledOut()

				&& addressCountry.getText().length() == 0
				&& addressRegion.getText().length() == 0

				&& selectedFundingType.length() == 0
				&& fundingTopic.getText().length() == 0
				&& !areFundingDatesFilledOut()

				&& selectedPublicationType.length() == 0
				&& publicationTitle.getText().length() == 0
				&& publicationDate.getText().length() == 0

				&& selectedSubject == null

				&& selectedName == null) {
			JOptionPane.showMessageDialog(this, "You must enter information to search on first");
		} else {
			status = javax.swing.JOptionPane.OK_OPTION;
			setAlternateQuery(true);
			this.setVisible(false);
		}
	}

	private void fundingTypeActionPerformed() {
		selectedFundingType = (String) fundingType.getSelectedItem();
	}

	private void fundingDateEndFocusGained() {
		endDateFocusGained(fundingDateEnd, fundingDateStart);
	}

	private void endDateFocusGained(final JFormattedTextField endDate, JFormattedTextField startDate) {
		if (endDate.getValue() == null) {
			endDate.setValue(startDate.getValue());
			/* After a formatted text field gains focus, it replaces its text with its
			 * current value, formatted appropriately of course. It does this _after_
			 * any focus listeners are notified. So, if we are editable, we queue
			 * up a selectAll to be done after the current events in the thread are done. */
			Runnable doSelect = new Runnable() {
				public void run() {
					endDate.selectAll();
				}
			};
			SwingUtilities.invokeLater(doSelect);
		}
	}

	private void publicationTypeActionPerformed() {
		selectedPublicationType = (String) publicationType.getSelectedItem();
	}

	private void lookupSubjectActionPerformed() {
		SubjectTermLookup subjectTermLookupDialog = new SubjectTermLookup(this);
		subjectTermLookupDialog.setMainHeaderText("Patrons");
		if (subjectTermLookupDialog.showDialog() == JOptionPane.OK_OPTION) {
			selectedSubject = subjectTermLookupDialog.getSelectedSubject();
			subject.setText(selectedSubject.getSubjectTerm());
		}
	}

	private void clearSubjectActionPerformed() {
		selectedSubject = null;
		subject.setText("");
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		HeaderPanel = new JPanel();
		mainHeaderPanel = new JPanel();
		mainHeaderLabel = new JLabel();
		subHeaderPanel = new JPanel();
		subHeaderLabel = new JLabel();
		tabbedPane1 = new JTabbedPane();
		panel1 = new JPanel();
		searchEditorLabel = new JLabel();
		contentPane = new JPanel();
		fieldSelector1 = new JComboBox();
		placeHolder1 = new QueryEditorTextPanel();
		boolean1 = new JComboBox();
		fieldSelector2 = new JComboBox();
		placeHolder2 = new QueryEditorTextPanel();
		buttonBar2 = new JPanel();
		cancelButton2 = new JButton();
		queryButton = new JButton();
		classSpecificPanel = new JPanel();
		altQueryLabel = new JLabel();
		panel2 = new JPanel();
		label5 = new JLabel();
		subject = new JTextField();
		lookupSubject = new JButton();
		clearSubject = new JButton();
		label23 = new JLabel();
		name = new JTextField();
		lookupName = new JButton();
		clearName = new JButton();
		label9 = new JLabel();
		label8 = new JLabel();
		panel3 = new JPanel();
		visitDateStart = ATBasicComponentFactory.createUnboundDateField();
		label3 = new JLabel();
		visitDateEnd = ATBasicComponentFactory.createUnboundDateField();
		label10 = new JLabel();
		contactArchivist = new JTextField();
		label7 = new JLabel();
		researchPurpose = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(PatronVisits.class, PatronVisits.PROPERTYNAME_RESEARCH_PURPOSE));
		label15 = new JLabel();
		visitTopic = new JTextField();
		label11 = new JLabel();
		label12 = new JLabel();
		panel4 = new JPanel();
		fundingDateStart = ATBasicComponentFactory.createUnboundDateField();
		label4 = new JLabel();
		fundingDateEnd = ATBasicComponentFactory.createUnboundDateField();
		label13 = new JLabel();
		fundingTopic = new JTextField();
		label14 = new JLabel();
		fundingType = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(PatronFunding.class, PatronFunding.PROPERTYNAME_FUNDING_TYPE));
		label16 = new JLabel();
		label17 = new JLabel();
		publicationDate = new JTextField();
		label18 = new JLabel();
		publicationTitle = new JTextField();
		label19 = new JLabel();
		publicationType = ATBasicComponentFactory.createUnboundComboBox(LookupListUtils.getLookupListValues(PatronPublications.class, PatronPublications.PROPERTYNAME_PUBLICATION_TYPE));
		label20 = new JLabel();
		label21 = new JLabel();
		addressCountry = new JTextField();
		label22 = new JLabel();
		addressRegion = new JTextField();
		buttonBar = new JPanel();
		cancelButton = new JButton();
		queryButton2 = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				thisWindowClosing();
			}
		});
		Container contentPane2 = getContentPane();
		contentPane2.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(null);
			dialogPane.setBackground(new Color(200, 205, 232));
			dialogPane.setLayout(new BorderLayout());

			//======== HeaderPanel ========
			{
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
					mainHeaderLabel.setText("Patrons");
					mainHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					mainHeaderLabel.setForeground(Color.white);
					mainHeaderPanel.add(mainHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(mainHeaderPanel, cc.xy(1, 1));

				//======== subHeaderPanel ========
				{
					subHeaderPanel.setBackground(new Color(66, 60, 111));
					subHeaderPanel.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
					subHeaderPanel.setLayout(new FormLayout(
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
					subHeaderLabel.setText("Search Editor");
					subHeaderLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));
					subHeaderLabel.setForeground(Color.white);
					subHeaderPanel.add(subHeaderLabel, cc.xy(2, 2));
				}
				HeaderPanel.add(subHeaderPanel, cc.xy(2, 1));
			}
			dialogPane.add(HeaderPanel, BorderLayout.CENTER);

			//======== tabbedPane1 ========
			{

				//======== panel1 ========
				{
					panel1.setBorder(Borders.DIALOG_BORDER);
					panel1.setOpaque(false);
					panel1.setLayout(new FormLayout(
						ColumnSpec.decodeSpecs("default:grow"),
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(RowSpec.TOP, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
						}));

					//---- searchEditorLabel ----
					searchEditorLabel.setText("Search patron records");
					panel1.add(searchEditorLabel, cc.xy(1, 1));

					//======== contentPane ========
					{
						contentPane.setBackground(new Color(231, 188, 251));
						contentPane.setOpaque(false);
						contentPane.setBorder(Borders.DIALOG_BORDER);
						contentPane.setLayout(new FormLayout(
							new ColumnSpec[] {
								FormFactory.DEFAULT_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								new ColumnSpec("400px:grow"),
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								new ColumnSpec(ColumnSpec.LEFT, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
							},
							new RowSpec[] {
								FormFactory.DEFAULT_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC,
								FormFactory.LINE_GAP_ROWSPEC,
								FormFactory.DEFAULT_ROWSPEC
							}));

						//---- fieldSelector1 ----
						fieldSelector1.setOpaque(false);
						fieldSelector1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								fieldSelector1ActionPerformed(e);
							}
						});
						contentPane.add(fieldSelector1, cc.xy(1, 1));

						//---- placeHolder1 ----
						placeHolder1.setOpaque(false);
						contentPane.add(placeHolder1, cc.xy(3, 1));

						//---- boolean1 ----
						boolean1.setModel(new DefaultComboBoxModel(new String[] {
							"and",
							"or"
						}));
						boolean1.setOpaque(false);
						contentPane.add(boolean1, cc.xy(5, 1));

						//---- fieldSelector2 ----
						fieldSelector2.setOpaque(false);
						fieldSelector2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								fieldSelector2ActionPerformed(e);
							}
						});
						contentPane.add(fieldSelector2, cc.xy(1, 2));

						//---- placeHolder2 ----
						placeHolder2.setOpaque(false);
						contentPane.add(placeHolder2, cc.xy(3, 2));

						//======== buttonBar2 ========
						{
							buttonBar2.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
							buttonBar2.setBackground(new Color(231, 188, 251));
							buttonBar2.setOpaque(false);
							buttonBar2.setLayout(new FormLayout(
								new ColumnSpec[] {
									FormFactory.GLUE_COLSPEC,
									FormFactory.BUTTON_COLSPEC,
									FormFactory.RELATED_GAP_COLSPEC,
									FormFactory.BUTTON_COLSPEC
								},
								RowSpec.decodeSpecs("pref")));

							//---- cancelButton2 ----
							cancelButton2.setText("Cancel");
							cancelButton2.setOpaque(false);
							cancelButton2.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									cancelButtonActionPerformed(e);
								}
							});
							buttonBar2.add(cancelButton2, cc.xy(2, 1));

							//---- queryButton ----
							queryButton.setText("Search");
							queryButton.setOpaque(false);
							queryButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									queryButtonActionPerformed(e);
								}
							});
							buttonBar2.add(queryButton, cc.xy(4, 1));
						}
						contentPane.add(buttonBar2, cc.xywh(1, 4, 5, 1));
					}
					panel1.add(contentPane, cc.xy(1, 3));
				}
				tabbedPane1.addTab("Search", panel1);


				//======== classSpecificPanel ========
				{
					classSpecificPanel.setOpaque(false);
					classSpecificPanel.setBorder(Borders.DIALOG_BORDER);
					classSpecificPanel.setLayout(new FormLayout(
						ColumnSpec.decodeSpecs("default:grow"),
						new RowSpec[] {
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- altQueryLabel ----
					altQueryLabel.setText("Search by linked record:");
					classSpecificPanel.add(altQueryLabel, cc.xy(1, 1));

					//======== panel2 ========
					{
						panel2.setOpaque(false);
						panel2.setLayout(new FormLayout(
							new ColumnSpec[] {
								FormFactory.UNRELATED_GAP_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								FormFactory.DEFAULT_COLSPEC,
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW),
								FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
								FormFactory.DEFAULT_COLSPEC,
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

						//---- label5 ----
						label5.setText("Find by subject");
						panel2.add(label5, cc.xy(3, 1));

						//---- subject ----
						subject.setOpaque(false);
						subject.setEditable(false);
						panel2.add(subject, cc.xy(5, 1));

						//---- lookupSubject ----
						lookupSubject.setText("Lookup");
						lookupSubject.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
						lookupSubject.setOpaque(false);
						lookupSubject.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								lookupSubjectActionPerformed();
							}
						});
						panel2.add(lookupSubject, cc.xy(7, 1));

						//---- clearSubject ----
						clearSubject.setText("Clear");
						clearSubject.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
						clearSubject.setOpaque(false);
						clearSubject.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								clearSubjectActionPerformed();
							}
						});
						panel2.add(clearSubject, cc.xy(9, 1));

						//---- label23 ----
						label23.setText("Find by name");
						panel2.add(label23, cc.xy(3, 3));

						//---- name ----
						name.setOpaque(false);
						name.setEditable(false);
						panel2.add(name, cc.xy(5, 3));

						//---- lookupName ----
						lookupName.setText("Lookup");
						lookupName.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
						lookupName.setOpaque(false);
						lookupName.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								lookupNameActionPerformed();
							}
						});
						panel2.add(lookupName, cc.xy(7, 3));

						//---- clearName ----
						clearName.setText("Clear");
						clearName.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
						clearName.setOpaque(false);
						clearName.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								clearNameActionPerformed();
							}
						});
						panel2.add(clearName, cc.xy(9, 3));

						//---- label9 ----
						label9.setText("Visits");
						label9.setFont(new Font("Lucida Grande", Font.BOLD, 13));
						panel2.add(label9, cc.xywh(1, 5, 3, 1));

						//---- label8 ----
						label8.setText("Visit Date");
						panel2.add(label8, cc.xy(3, 7));

						//======== panel3 ========
						{
							panel3.setOpaque(false);
							panel3.setLayout(new FormLayout(
								new ColumnSpec[] {
									FormFactory.DEFAULT_COLSPEC,
									FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
									FormFactory.DEFAULT_COLSPEC,
									FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
									FormFactory.DEFAULT_COLSPEC
								},
								RowSpec.decodeSpecs("default")));

							//---- visitDateStart ----
							visitDateStart.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
							visitDateStart.setColumns(12);
							panel3.add(visitDateStart, cc.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

							//---- label3 ----
							label3.setText("-");
							panel3.add(label3, cc.xy(3, 1));

							//---- visitDateEnd ----
							visitDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
							visitDateEnd.setColumns(12);
							visitDateEnd.addFocusListener(new FocusAdapter() {
								@Override
								public void focusGained(FocusEvent e) {
									visitDateEndFocusGained();
								}
							});
							panel3.add(visitDateEnd, cc.xywh(5, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
						}
						panel2.add(panel3, cc.xy(5, 7));

						//---- label10 ----
						label10.setText("Contact Archivist");
						panel2.add(label10, cc.xy(3, 9));
						panel2.add(contactArchivist, cc.xy(5, 9));

						//---- label7 ----
						label7.setText("Research Purpose");
						panel2.add(label7, cc.xy(3, 11));

						//---- researchPurpose ----
						researchPurpose.setOpaque(false);
						researchPurpose.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								researchPurposeActionPerformed();
							}
						});
						panel2.add(researchPurpose, cc.xywh(5, 11, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

						//---- label15 ----
						label15.setText("Visit Topic");
						panel2.add(label15, cc.xy(3, 13));
						panel2.add(visitTopic, cc.xy(5, 13));

						//---- label11 ----
						label11.setText("Funding");
						label11.setFont(new Font("Lucida Grande", Font.BOLD, 13));
						panel2.add(label11, cc.xywh(1, 15, 3, 1));

						//---- label12 ----
						label12.setText("Funding Date");
						panel2.add(label12, cc.xy(3, 17));

						//======== panel4 ========
						{
							panel4.setOpaque(false);
							panel4.setLayout(new FormLayout(
								new ColumnSpec[] {
									FormFactory.DEFAULT_COLSPEC,
									FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
									FormFactory.DEFAULT_COLSPEC,
									FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
									FormFactory.DEFAULT_COLSPEC
								},
								RowSpec.decodeSpecs("default")));

							//---- fundingDateStart ----
							fundingDateStart.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
							fundingDateStart.setColumns(12);
							panel4.add(fundingDateStart, cc.xywh(1, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

							//---- label4 ----
							label4.setText("-");
							panel4.add(label4, cc.xy(3, 1));

							//---- fundingDateEnd ----
							fundingDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
							fundingDateEnd.setColumns(12);
							fundingDateEnd.addFocusListener(new FocusAdapter() {
								@Override
								public void focusGained(FocusEvent e) {
									fundingDateEndFocusGained();
								}
							});
							panel4.add(fundingDateEnd, cc.xywh(5, 1, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
						}
						panel2.add(panel4, cc.xy(5, 17));

						//---- label13 ----
						label13.setText("Funding Topic");
						panel2.add(label13, cc.xy(3, 19));
						panel2.add(fundingTopic, cc.xy(5, 19));

						//---- label14 ----
						label14.setText("Funding Type");
						panel2.add(label14, cc.xy(3, 21));

						//---- fundingType ----
						fundingType.setOpaque(false);
						fundingType.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								fundingTypeActionPerformed();
							}
						});
						panel2.add(fundingType, cc.xywh(5, 21, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

						//---- label16 ----
						label16.setText("Publications");
						label16.setFont(new Font("Lucida Grande", Font.BOLD, 13));
						panel2.add(label16, cc.xywh(1, 23, 5, 1));

						//---- label17 ----
						label17.setText("Publication Date");
						panel2.add(label17, cc.xy(3, 25));
						panel2.add(publicationDate, cc.xy(5, 25));

						//---- label18 ----
						label18.setText("Publication Title");
						panel2.add(label18, cc.xy(3, 27));
						panel2.add(publicationTitle, cc.xy(5, 27));

						//---- label19 ----
						label19.setText("Publication Type");
						panel2.add(label19, cc.xy(3, 29));

						//---- publicationType ----
						publicationType.setOpaque(false);
						publicationType.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								publicationTypeActionPerformed();
							}
						});
						panel2.add(publicationType, cc.xywh(5, 29, 1, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));

						//---- label20 ----
						label20.setText("Addresses");
						label20.setFont(new Font("Lucida Grande", Font.BOLD, 13));
						panel2.add(label20, cc.xywh(1, 31, 9, 1));

						//---- label21 ----
						label21.setText("Country");
						ATFieldInfo.assignLabelInfo(label21, PatronAddresses.class, PatronAddresses.PROPERTYNAME_COUNTRY);
						panel2.add(label21, cc.xy(3, 33));
						panel2.add(addressCountry, cc.xy(5, 33));

						//---- label22 ----
						label22.setText("Region");
						ATFieldInfo.assignLabelInfo(label22, PatronAddresses.class, PatronAddresses.PROPERTYNAME_REGION);
						panel2.add(label22, cc.xy(3, 35));
						panel2.add(addressRegion, cc.xy(5, 35));
					}
					classSpecificPanel.add(panel2, cc.xy(1, 3));

					//======== buttonBar ========
					{
						buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
						buttonBar.setBackground(new Color(231, 188, 251));
						buttonBar.setOpaque(false);
						buttonBar.setLayout(new FormLayout(
							new ColumnSpec[] {
								FormFactory.GLUE_COLSPEC,
								FormFactory.BUTTON_COLSPEC,
								FormFactory.RELATED_GAP_COLSPEC,
								FormFactory.BUTTON_COLSPEC
							},
							RowSpec.decodeSpecs("pref")));

						//---- cancelButton ----
						cancelButton.setText("Cancel");
						cancelButton.setOpaque(false);
						cancelButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								cancelButtonActionPerformed(e);
							}
						});
						buttonBar.add(cancelButton, cc.xy(2, 1));

						//---- queryButton2 ----
						queryButton2.setText("Search");
						queryButton2.setOpaque(false);
						queryButton2.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								queryButton2ActionPerformed(e);
							}
						});
						buttonBar.add(queryButton2, cc.xy(4, 1));
					}
					classSpecificPanel.add(buttonBar, cc.xy(1, 5));
				}
				tabbedPane1.addTab("Search By Linked Records", classSpecificPanel);

			}
			dialogPane.add(tabbedPane1, BorderLayout.SOUTH);
		}
		contentPane2.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel HeaderPanel;
	private JPanel mainHeaderPanel;
	private JLabel mainHeaderLabel;
	private JPanel subHeaderPanel;
	private JLabel subHeaderLabel;
	private JTabbedPane tabbedPane1;
	private JPanel panel1;
	private JLabel searchEditorLabel;
	private JPanel contentPane;
	private JComboBox fieldSelector1;
	private QueryEditorTextPanel placeHolder1;
	private JComboBox boolean1;
	private JComboBox fieldSelector2;
	private QueryEditorTextPanel placeHolder2;
	private JPanel buttonBar2;
	private JButton cancelButton2;
	private JButton queryButton;
	private JPanel classSpecificPanel;
	private JLabel altQueryLabel;
	private JPanel panel2;
	private JLabel label5;
	private JTextField subject;
	private JButton lookupSubject;
	private JButton clearSubject;
	private JLabel label23;
	private JTextField name;
	private JButton lookupName;
	private JButton clearName;
	private JLabel label9;
	private JLabel label8;
	private JPanel panel3;
	private JFormattedTextField visitDateStart;
	private JLabel label3;
	private JFormattedTextField visitDateEnd;
	private JLabel label10;
	private JTextField contactArchivist;
	private JLabel label7;
	private JComboBox researchPurpose;
	private JLabel label15;
	private JTextField visitTopic;
	private JLabel label11;
	private JLabel label12;
	private JPanel panel4;
	private JFormattedTextField fundingDateStart;
	private JLabel label4;
	private JFormattedTextField fundingDateEnd;
	private JLabel label13;
	private JTextField fundingTopic;
	private JLabel label14;
	private JComboBox fundingType;
	private JLabel label16;
	private JLabel label17;
	private JTextField publicationDate;
	private JLabel label18;
	private JTextField publicationTitle;
	private JLabel label19;
	private JComboBox publicationType;
	private JLabel label20;
	private JLabel label21;
	private JTextField addressCountry;
	private JLabel label22;
	private JTextField addressRegion;
	private JPanel buttonBar;
	private JButton cancelButton;
	private JButton queryButton2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;

//    private ArrayList fieldValues;

	private QueryEditorPanel currentValue1Component;
	private CellConstraints value1CellConstraints;

	private QueryEditorPanel currentValue2Component;
	private CellConstraints value2CellConstraints;

	private String selectedFundingType = "";
	private Date fundingStartDate = null;
	private Date fundingEndDate = null;

	private String selectedResearchPurpose = "";
	private Date visitStartDate = null;
	private Date visitEndDate = null;

	private String selectedPublicationType = "";
	private Date publicationStartDate = null;
	private Date publicationEndDate = null;

	private Subjects selectedSubject = null;
	private Names selectedName = null;

	public final int showDialog() {

		this.pack();

		setLocationRelativeTo(null);
		this.setVisible(true);

		return (status);
	}

	public String getChosenField1() {
		Object choice = fieldSelector1.getSelectedItem();
		if (choice instanceof QueryField) {
			return ((QueryField) choice).getFieldName();
		} else {
			return "";
		}
	}

	public String getChosenField2() {
		Object choice = fieldSelector2.getSelectedItem();
		if (choice instanceof QueryField) {
			return ((QueryField) choice).getFieldName();
		} else {
			return "";
		}
	}

	public ATSearchCriterion getCriterion1() {
		return this.currentValue1Component.getQueryCriterion(getClazz(), this.getChosenField1());
	}

	public ATSearchCriterion getCriterion2() {
		return this.currentValue2Component.getQueryCriterion(getClazz(), this.getChosenField2());
	}

	public String getChosenBoolean1() {
		return (String) boolean1.getSelectedItem();
	}

	public void setFieldValues(ArrayList fieldValues1, ArrayList fieldValues2, ArrayList sortFieldValues) {
		fieldValues1.add(0, SELECT_A_FIELD);
		fieldValues2.add(0, SELECT_A_FIELD);
		fieldSelector1.setModel(new DefaultComboBoxModel(fieldValues1.toArray()));
		fieldSelector2.setModel(new DefaultComboBoxModel(fieldValues2.toArray()));
//		sortFieldSelector.setModel(new DefaultComboBoxModel(sortFieldValues.toArray()));
	}

	private void createPopupValues() {

		ArrayList<QueryField> fieldValues1 = new ArrayList<QueryField>();
		ArrayList<QueryField> fieldValues2 = new ArrayList<QueryField>();
		ArrayList<QueryField> sortFieldValues = new ArrayList<QueryField>();

		Set<DatabaseFields> fields = new DatabaseTablesDAO().getFieldList(Patrons.class.getSimpleName());
		for (DatabaseFields field : fields) {
			if (field.getIncludeInSearchEditor()) {
				fieldValues1.add(new QueryField(field, Patrons.class));
				fieldValues2.add(new QueryField(field, Patrons.class));
			}
//			if (field.getAllowSort()) {
//				sortFieldValues.add(new QueryField(field.getFieldName(), getClazz()));
//			}
		}
		Collections.sort(fieldValues1);
		Collections.sort(fieldValues2);
		Collections.sort(sortFieldValues);
		setFieldValues(fieldValues1, fieldValues2, sortFieldValues);

		FormLayout formLayout = (FormLayout) contentPane.getLayout();
		placeHolder1.disableValueField();
		currentValue1Component = placeHolder1;
		value1CellConstraints = formLayout.getConstraints(placeHolder1);
		placeHolder2.disableValueField();
		currentValue2Component = placeHolder2;
		value2CellConstraints = formLayout.getConstraints(placeHolder2);
	}

	public ArrayList<CriteriaRelationshipPairs> getAltFormCriteria() {
		ArrayList<CriteriaRelationshipPairs> criterionList = new ArrayList<CriteriaRelationshipPairs>();
		CriteriaRelationshipPairs pair;
		String humanReadableSearchString;

// Patron Funding
		if (areFundingDatesFilledOut()) {
			pair = new CriteriaRelationshipPairs(Restrictions.ge(PatronFunding.PROPERTYNAME_FUNDING_DATE, fundingStartDate), Patrons.PROPERTYNAME_PATRON_FUNDING);
			pair.addCriteria(Restrictions.le(PatronFunding.PROPERTYNAME_FUNDING_DATE, fundingEndDate));
			pair.setHumanReadableSearchString(PatronFunding.PROPERTYNAME_FUNDING_DATE + " is between " + fundingDateStart.getText() + " and " + fundingDateEnd.getText());
			criterionList.add(pair);
		}

		if (selectedFundingType.length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(PatronFunding.PROPERTYNAME_FUNDING_TYPE, selectedFundingType),
					Patrons.PROPERTYNAME_PATRON_FUNDING, PatronFunding.PROPERTYNAME_FUNDING_TYPE + " equals " + selectedFundingType));
		}

		if (fundingTopic != null && fundingTopic.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(PatronFunding.PROPERTYNAME_TOPIC, fundingTopic.getText(), MatchMode.ANYWHERE),
					Patrons.PROPERTYNAME_PATRON_FUNDING,
					PatronFunding.PROPERTYNAME_TOPIC + " contains " + fundingTopic.getText(),
					"Funding Topic"));
		}


// Patron Publications
		if (publicationDate != null && publicationDate.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(PatronPublications.PROPERTYNAME_PUBLICATION_DATE, publicationDate.getText(), MatchMode.ANYWHERE),
					Patrons.PROPERTYNAME_PATRON_PUBLICATIONS,
					PatronPublications.PROPERTYNAME_PUBLICATION_DATE + " contains " + publicationDate.getText(),
					"Publication Date"));
		}

		if (selectedPublicationType.length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(PatronPublications.PROPERTYNAME_PUBLICATION_TYPE, selectedPublicationType),
					Patrons.PROPERTYNAME_PATRON_PUBLICATIONS, PatronPublications.PROPERTYNAME_PUBLICATION_TYPE + " equals " + selectedPublicationType));
		}

		if (publicationTitle != null && publicationTitle.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(PatronPublications.PROPERTYNAME_PUBLICATION_TITLE, publicationTitle.getText(), MatchMode.ANYWHERE),
					Patrons.PROPERTYNAME_PATRON_PUBLICATIONS,
					PatronPublications.PROPERTYNAME_PUBLICATION_TITLE + " contains " + publicationTitle.getText(),
					"Publication Title"));
		}

// Patron Visits
		if (areVisitDatesFilledOut()) {
			pair = new CriteriaRelationshipPairs(Restrictions.ge(PatronVisits.PROPERTYNAME_VISIT_DATE, visitStartDate), Patrons.PROPERTYNAME_PATRON_VISITS);
			pair.addCriteria(Restrictions.le(PatronVisits.PROPERTYNAME_VISIT_DATE, visitEndDate));
			pair.setHumanReadableSearchString(PatronVisits.PROPERTYNAME_VISIT_DATE + " is between " + visitDateStart.getText() + " and " + visitDateEnd.getText());
			criterionList.add(pair);
		}

		if (contactArchivist != null && contactArchivist.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(PatronVisits.PROPERTYNAME_CONTACT_ARCHIVIST, contactArchivist.getText(), MatchMode.ANYWHERE),
					Patrons.PROPERTYNAME_PATRON_VISITS,
					PatronVisits.PROPERTYNAME_CONTACT_ARCHIVIST + " contains " + contactArchivist.getText(),
					"Contact Archivist"));
		}

		if (visitTopic != null && visitTopic.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(PatronVisits.PROPERTYNAME_TOPIC, visitTopic.getText(), MatchMode.ANYWHERE),
					Patrons.PROPERTYNAME_PATRON_VISITS,
					PatronVisits.PROPERTYNAME_TOPIC + " contains " + visitTopic.getText(),
					"Visit Topic"));
		}

		if (selectedResearchPurpose.length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.eq(PatronVisits.PROPERTYNAME_RESEARCH_PURPOSE, selectedResearchPurpose),
					Patrons.PROPERTYNAME_PATRON_VISITS, PatronVisits.PROPERTYNAME_RESEARCH_PURPOSE + " equals " + selectedResearchPurpose));
		}

// Patron addresses
		if (addressRegion != null && addressRegion.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(PatronAddresses.PROPERTYNAME_REGION, addressRegion.getText(), MatchMode.ANYWHERE),
					Patrons.PROPERTYNAME_PATRON_ADDRESSES,
					PatronAddresses.PROPERTYNAME_REGION + " contains " + addressRegion.getText(),
					"Region"));
		}

		if (addressCountry != null && addressCountry.getText().length() > 0) {
			criterionList.add(new CriteriaRelationshipPairs(Restrictions.like(PatronAddresses.PROPERTYNAME_COUNTRY, addressCountry.getText(), MatchMode.ANYWHERE),
					Patrons.PROPERTYNAME_PATRON_ADDRESSES,
					PatronAddresses.PROPERTYNAME_COUNTRY + " contains " + addressCountry.getText(),
					"Country"));
		}

		return criterionList;
	}

	private boolean areDatesFilledOut(Date startDate, Date endDate) {
		if (startDate == null && endDate == null) {
			return false;
		} else if (startDate == null) {
			startDate = endDate;
			return true;
		} else if (endDate == null) {
			endDate = startDate;
			return true;
		} else {
			return true;
		}
	}

	private boolean areVisitDatesFilledOut() {
		visitStartDate = (Date)visitDateStart.getValue();
		visitEndDate = (Date)visitDateEnd.getValue();
		return areDatesFilledOut(visitStartDate, visitEndDate);
	}

	private boolean areFundingDatesFilledOut() {
		fundingStartDate = (Date)fundingDateStart.getValue();
		fundingEndDate = (Date)fundingDateEnd.getValue();
		return areDatesFilledOut(fundingStartDate, fundingEndDate);
	}

}
