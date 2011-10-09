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
 * Created by JFormDesigner on Wed Mar 23 11:01:24 EDT 2011
 */

package org.rac.dialogs;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.archiviststoolkit.swing.ATBasicDialog;

public class StatisticalReportDates extends ATBasicDialog {
	
	public StatisticalReportDates(Frame owner) {
		super(owner);
		initComponents();
	}

	public StatisticalReportDates(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		if (getStartDate() == null || getEndDate() == null) {
			JOptionPane.showMessageDialog(this, "You must enter a start and end date");
		} else {
			super.performOkAction();
		}
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		super.performCancelAction();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label3 = new JLabel();
		visitDateStart = ATBasicComponentFactory.createUnboundDateField();
		label2 = new JLabel();
		visitDateEnd = ATBasicComponentFactory.createUnboundDateField();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setModal(true);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(ColumnSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					},
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label3 ----
				label3.setText("Start Date");
				contentPanel.add(label3, cc.xy(1, 1));

				//---- visitDateStart ----
				visitDateStart.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				visitDateStart.setColumns(12);
				contentPanel.add(visitDateStart, cc.xy(3, 1));

				//---- label2 ----
				label2.setText("End Date");
				contentPanel.add(label2, cc.xy(1, 3));

				//---- visitDateEnd ----
				visitDateEnd.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
				visitDateEnd.setColumns(12);
				contentPanel.add(visitDateEnd, cc.xy(3, 3));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label3;
	private JFormattedTextField visitDateStart;
	private JLabel label2;
	private JFormattedTextField visitDateEnd;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public Date getStartDate () {
		return (Date)visitDateStart.getValue();

	}

	public Date getEndDate () {
		return (Date)visitDateEnd.getValue();

	}
}
