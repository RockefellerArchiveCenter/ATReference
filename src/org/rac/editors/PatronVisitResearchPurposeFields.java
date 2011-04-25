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
 * Created by JFormDesigner on Sun Feb 27 13:07:38 EST 2011
 */

package org.rac.editors;

import java.awt.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.swing.ATBasicComponentFactory;
import org.rac.model.PatronVisitsResearchPurposes;

public class PatronVisitResearchPurposeFields extends RAC_DomainEditorFields {
	public PatronVisitResearchPurposeFields() {
		initComponents();
	}

	@Override
	public Component getInitialFocusComponent() {
		return researchPurpose;
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		label_researchPurpose = new JLabel();
		researchPurpose = ATBasicComponentFactory.createTextField(detailsModel.getModel(PatronVisitsResearchPurposes.PROPERTYNAME_RESEARCH_PURPOSE));
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setBorder(Borders.DLU4_BORDER);
		setBackground(new Color(234, 201, 250));
		setOpaque(false);
		setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
		setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec("max(default;400px):grow")
			},
			RowSpec.decodeSpecs("default")));

		//---- label_researchPurpose ----
		label_researchPurpose.setText("Research Purpose");
		ATFieldInfo.assignLabelInfo(label_researchPurpose, PatronVisitsResearchPurposes.class, PatronVisitsResearchPurposes.PROPERTYNAME_RESEARCH_PURPOSE);
		add(label_researchPurpose, cc.xy(1, 1));

		//---- researchPurpose ----
		researchPurpose.setColumns(30);
		add(researchPurpose, cc.xy(3, 1));
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel label_researchPurpose;
	private JTextField researchPurpose;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
