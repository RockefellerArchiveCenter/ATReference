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
 * Created by JFormDesigner on Thu Jun 10 13:55:28 EDT 2010
 */

package org.rac.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.model.Names;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.hibernate.NonUniqueResultException;
import org.rac.editors.PatronFields;
import org.rac.editors.PatronInitalDataEntryFields;
import org.rac.model.Patrons;
import org.rac.myDomain.PatronsDAO;

public class ReadingRoomLogonDialog extends JDialog {

	PatronsDAO patronDAO = new PatronsDAO();
	private Boolean createNewRecord = false;

	public ReadingRoomLogonDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public ReadingRoomLogonDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		createNewRecord = false;
		checkAndClose();
	}

	private void checkAndClose() {
		if (firstName.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "You must enter a first name");
		} else if (lastName.getText().length() == 0) {
			JOptionPane.showMessageDialog(this, "You must enter a last name");
		} else {
			status = JOptionPane.OK_OPTION;
			this.setVisible(false);
		}
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		status = javax.swing.JOptionPane.CANCEL_OPTION;
		this.setVisible(false);
	}

	public String getFirstName() {
		return firstName.getText().trim();
	}

	public String getLastName() {
		return lastName.getText().trim();
	}

	private void addPatronActionPerformed() {
		createNewRecord = true;
		status = JOptionPane.OK_OPTION;
		this.setVisible(false);
	}

    public String getSuffix() {
        return suffix.getText().trim();
    }

    public String getPrefix() {
        return prefix.getText().trim();
    }

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        dialogPane = new JPanel();
        panel1 = new JPanel();
        contentPanel = new JPanel();
        label4 = new JLabel();
        prefix = new JTextField();
        label1 = new JLabel();
        firstName = new JTextField();
        label2 = new JLabel();
        lastName = new JTextField();
        label3 = new JLabel();
        suffix = new JTextField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        addPatron = new JButton();
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

            //======== panel1 ========
            {
                panel1.setOpaque(false);
                panel1.setBorder(Borders.DIALOG_BORDER);
                panel1.setLayout(new FormLayout(
                    ColumnSpec.decodeSpecs("max(default;400px):grow"),
                    new RowSpec[] {
                        FormFactory.DEFAULT_ROWSPEC,
                        FormFactory.LINE_GAP_ROWSPEC,
                        FormFactory.DEFAULT_ROWSPEC
                    }));

                //======== contentPanel ========
                {
                    contentPanel.setBorder(new TitledBorder(null, "Patron Logon", TitledBorder.LEADING, TitledBorder.TOP));
                    contentPanel.setOpaque(false);
                    contentPanel.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            new ColumnSpec(ColumnSpec.FILL, Sizes.MINIMUM, 0.1),
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

                    //---- label4 ----
                    label4.setText("Prefix");
                    ATFieldInfo.assignLabelInfo(label4, Names.class, Names.PROPERTYNAME_PERSONAL_PREFIX);
                    contentPanel.add(label4, new CellConstraints(1, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));
                    contentPanel.add(prefix, new CellConstraints(3, 1, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

                    //---- label1 ----
                    label1.setText("First Name");
                    ATFieldInfo.assignLabelInfo(label1, Names.class, Names.PROPERTYNAME_PERSONAL_PRIMARY_NAME);
                    contentPanel.add(label1, new CellConstraints(1, 3, 1, 1, CellConstraints.FILL, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));
                    contentPanel.add(firstName, new CellConstraints(3, 3, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

                    //---- label2 ----
                    label2.setText("Last Name");
                    ATFieldInfo.assignLabelInfo(label2, Names.class, Names.PROPERTYNAME_PERSONAL_REST_OF_NAME);
                    contentPanel.add(label2, new CellConstraints(1, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));
                    contentPanel.add(lastName, new CellConstraints(3, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));

                    //---- label3 ----
                    label3.setText("Suffix");
                    ATFieldInfo.assignLabelInfo(label3, Names.class, Names.PROPERTYNAME_PERSONAL_SUFFIX);
                    contentPanel.add(label3, new CellConstraints(1, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));
                    contentPanel.add(suffix, new CellConstraints(3, 7, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT, new Insets( 0, 5, 0, 0)));
                }
                panel1.add(contentPanel, cc.xy(1, 1));

                //======== buttonBar ========
                {
                    buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
                    buttonBar.setBackground(new Color(231, 188, 251));
                    buttonBar.setOpaque(false);
                    buttonBar.setLayout(new FormLayout(
                        new ColumnSpec[] {
                            FormFactory.BUTTON_COLSPEC,
                            FormFactory.RELATED_GAP_COLSPEC,
                            FormFactory.BUTTON_COLSPEC,
                            FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
                            FormFactory.DEFAULT_COLSPEC
                        },
                        RowSpec.decodeSpecs("pref")));

                    //---- okButton ----
                    okButton.setText("OK");
                    okButton.setOpaque(false);
                    okButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            okButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(okButton, cc.xy(1, 1));

                    //---- cancelButton ----
                    cancelButton.setText("Cancel");
                    cancelButton.setOpaque(false);
                    cancelButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            cancelButtonActionPerformed(e);
                        }
                    });
                    buttonBar.add(cancelButton, cc.xy(3, 1));

                    //---- addPatron ----
                    addPatron.setText("Add Patron");
                    addPatron.setOpaque(false);
                    addPatron.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            addPatronActionPerformed();
                        }
                    });
                    buttonBar.add(addPatron, cc.xy(5, 1));
                }
                panel1.add(buttonBar, cc.xywh(1, 3, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));
            }
            dialogPane.add(panel1, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel dialogPane;
    private JPanel panel1;
    private JPanel contentPanel;
    private JLabel label4;
    private JTextField prefix;
    private JLabel label1;
    private JTextField firstName;
    private JLabel label2;
    private JTextField lastName;
    private JLabel label3;
    private JTextField suffix;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JButton addPatron;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	/**
	 * The status of the editor.
	 */
	protected int status = 0;

	/**
	 * Displays the dialog box representing the editor.
	 *
	 * @return true if it displayed okay
	 */

	public final int showDialog() {

		this.pack();

		setLocationRelativeTo(null);
		this.getRootPane().setDefaultButton(okButton);
		this.setVisible(true);

		return (status);
	}

	public void clearData() {
		firstName.setText("");
		lastName.setText("");
        prefix.setText("");
        suffix.setText("");
	}

	public void setInitialFocus() {
		firstName.requestFocus();
	}

	public void searchAndDisplayRecord() throws LookupException, PersistenceException, UnsupportedTableModelException, NonUniqueResultException {
		Patrons patrons = patronDAO.queryNameByFirstLastPrefixAndSuffix(getFirstName(), getLastName(), getPrefix(), getSuffix());
		DomainEditor dialog;
		Boolean newRecord;
		if (patrons != null || getCreateNewRecord()) {
			if (getCreateNewRecord()) {
				patrons = new Patrons();
				patrons.setRepository(ApplicationFrame.getInstance().getCurrentUserRepository());
				dialog = new DomainEditor(Patrons.class, this, "Patrons", new PatronInitalDataEntryFields());
				newRecord = true;
			} else {
				patrons = (Patrons) patronDAO.findByPrimaryKeyLongSession(patrons.getIdentifier());
				dialog = new DomainEditor(Patrons.class, this, "Patrons", new PatronFields());
				newRecord = false;
			}

            // center the dialog on screen
            dialog.setAbsoluteCentered(true);

            dialog.setModel(patrons, null);
			dialog.disableNavigationButtons();
			dialog.setButtonListeners();
			dialog.setIncludeSaveButton(true);
			dialog.clearRecordPositionText(); // printing record position in this dialog makes no sense

			//this is a hack but create a dummy calling table so that the save buttons does not blow up.
			DomainSortableTable dummyTable = new DomainSortableTable(Patrons.class);
			dialog.setCallingTable(dummyTable);

			if (!newRecord) {
				((PatronFields)dialog.editorFields).updateUIForClass0();
			}

            int status = dialog.showDialog();

            if (status == JOptionPane.OK_OPTION) {
				if (newRecord) {
                    patronDAO.getLongSession(); // make sure long session is opened
					patronDAO.updateLongSession(patrons, false);
				} else {
					patronDAO.updateLongSession(patrons);
				}
			} else {
				if (!newRecord) {
					try {
						patronDAO.closeLongSessionRollback();
					} catch (SQLException e1) {
						new ErrorDialog(dialog, "Error canceling record.", e1).showDialog();
					}
				}
			}
		} else {
            String fullName = new String(getPrefix() + " " + getFirstName() + " " +
                            getLastName() + " "  + getSuffix()).trim();

			JOptionPane.showMessageDialog(this,
                    "There were no patrons matching \"" + fullName + "\".\nPlease try again");
		}

	}


	public Boolean getCreateNewRecord() {
		return createNewRecord;
	}

	public void setCreateNewRecord(Boolean createNewRecord) {
		this.createNewRecord = createNewRecord;
	}
}
