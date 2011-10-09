/**
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
 *
 * Created by JFormDesigner on Wed Mar 23 15:17:40 EDT 2011
 */

package org.rac.dialogs;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.dialog.ATFileChooser;
import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.swing.*;
public class GeneralLogDialog extends JDialog  implements ClipboardOwner {
	public GeneralLogDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public GeneralLogDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void copyButtonActionPerformed() {
		StringSelection stringSelection = new StringSelection(logText.getText());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, this);
	}

	private void printActionPerformed(ActionEvent e) {
		logText.print();
	}

	private void saveActionPerformed(ActionEvent e) {
		ATFileChooser filechooser = new ATFileChooser();
		if (filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = filechooser.getSelectedFile();

			try {
				FileWriter fileWriter = new FileWriter(selectedFile);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

				bufferedWriter.write(logText.getText());

				bufferedWriter.close();
			} catch (IOException ioe) {
				new ErrorDialog(this, "Error saving log message", ioe).showDialog();
			}
		}
	}

	private void okButtonActionPerformed(ActionEvent e) {
		this.setVisible(false);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		dialogTitle = new JLabel();
		scrollPane1 = new JScrollPane();
		logText = new PrintableJTextArea();
		buttonBar = new JPanel();
		copyButton = new JButton();
		printButton = new JButton();
		saveButton = new JButton();
		okButton = new JButton();
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
					ColumnSpec.decodeSpecs("max(default;600px):grow"),
					new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(RowSpec.FILL, Sizes.DEFAULT, FormSpec.DEFAULT_GROW)
					}));

				//---- dialogTitle ----
				dialogTitle.setText("Dialog Title");
				contentPanel.add(dialogTitle, cc.xy(1, 1));

				//======== scrollPane1 ========
				{
					scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

					//---- logText ----
					logText.setRows(20);
					logText.setLineWrap(true);
					scrollPane1.setViewportView(logText);
				}
				contentPanel.add(scrollPane1, cc.xy(1, 3));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.GLUE_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- copyButton ----
				copyButton.setText("Copy");
				copyButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						copyButtonActionPerformed();
					}
				});
				buttonBar.add(copyButton, cc.xy(4, 1));

				//---- printButton ----
				printButton.setText("Print");
				printButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						printActionPerformed(e);
					}
				});
				buttonBar.add(printButton, cc.xy(6, 1));

				//---- saveButton ----
				saveButton.setText("Save");
				saveButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveActionPerformed(e);
					}
				});
				buttonBar.add(saveButton, cc.xy(8, 1));

				//---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(10, 1));
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
	private JLabel dialogTitle;
	private JScrollPane scrollPane1;
	private PrintableJTextArea logText;
	private JPanel buttonBar;
	private JButton copyButton;
	private JButton printButton;
	private JButton saveButton;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables

	public void setTitleText (String titleText) {
		dialogTitle.setText(titleText);
	}

	public void setLogText (String logText) {
		this.logText.setText(logText);
	}

	public final void showDialog() {

		this.pack();
		setLocationRelativeTo(null);
		this.setVisible(true);
	}	

	public void lostOwnership(Clipboard clipboard, Transferable transferable) {
	}
}
