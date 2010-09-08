package org.archiviststoolkit.maintenance.upgrades;

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
 * @author Nathan Stevens
 * Date: June 10, 2009
 * Time: 9:09:19 PM
 */

import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.util.DatabaseConnectionUtils;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.netbeans.spi.wizard.DeferredWizardResult;
import org.hibernate.LockMode;

import java.sql.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;

public class UpgradeTo_2_0_2 extends Upgrade {
    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
		return true;
	}


	protected int getNumberOfSteps() {
		return 1;
	}


    protected boolean runFieldInit() {
        return true;
    }

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("2.0.2", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

}