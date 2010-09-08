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

public class UpgradeTo_1_7_0 extends Upgrade {
    protected boolean doUpgrade(Connection conn, int initialStep, int numberOfSteps, DeferredWizardResult.ResultProgressHandle progress) {
		return true;
	}

    /**
     * Run any sql and hibernate code that needs to be ran after the database has been
     * initialized by hibernate.
     *
     * @param conn The connection to the database
     * @return true if all the code executed fine
     */
	protected boolean runPostDBInitializationSQLCode(Connection conn) {
        // first we need to set a repository id for all digital objects otherwise
        // the dont show up in hibernates list all
        String sqlString = "";
		Statement stmt;

        try {
            stmt = conn.createStatement();
            sqlString = "UPDATE DigitalObjects SET repositoryId = 1";
            stmt.execute(sqlString);
            conn.commit();
        } catch (SQLException e) {
            setErrorString(e.getMessage());
            return false;
        }

        // now use hibernate to upgrade digital objects with correct repository and identifyer information
		try {
            // this access is used for finding the repository of the DO, and findind only parent DO
            DigitalObjectDAO access = new DigitalObjectDAO();
            access.getLongSession();

			DigitalObjects digitalObject;

            // set the repository for digital objects and clear out the mets ID if they
            // are duplicates
            HashMap<String,String> metsIDs = new HashMap<String,String>();
            String metsID;

            for (Object o : access.findAllLongSession(LockMode.READ)) {
                digitalObject = (DigitalObjects) o;

                // get the parent resource this digital object belongs to
                ArchDescriptionDigitalInstances digitalInstance = digitalObject.getDigitalInstance();
                if(digitalInstance != null) {
                    Resources parentResource = access.findResourceByDigitalObject(digitalInstance);

                    // now update the repository
                    setRepository(digitalObject, parentResource.getRepository());

                    // now set the parent resoure in the digital object instance to allow searching
                    // by resource for digital objects
                    digitalInstance.setParentResource(parentResource);
                }

                // modify the metId if is not unique
                metsID = digitalObject.getMetsIdentifier();
                if(metsID.length() != 0) {
                    if(!metsIDs.containsKey(metsID)) {
                        metsIDs.put(metsID, metsID);
                    } else { // key already exist so clear it out
                        digitalObject.setMetsIdentifier("");
                    }
                }

                access.updateLongSession(digitalObject, false);
			}
            
            access.closeLongSession();
		} catch (PersistenceException e) {
			setErrorString(e.getMessage());
			return false;
		} catch (LookupException e) {
			setErrorString(e.getMessage());
			return false;
		} catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
		return true;
	}

    /**
     * Method to set the digital object repositories
     * 
     * @param digitalObject The digital object
     * @param repository The repository
     */
    protected void setRepository(DigitalObjects digitalObject, Repositories repository) {
        digitalObject.setRepository(repository);

        // now set the respository for any child digital objects through recursion
        for(DigitalObjects childDigitalObject: digitalObject.getChildren()) {
            setRepository(childDigitalObject, repository);
        }
    }

	protected int getNumberOfSteps() {
		return 2;
	}

    //load any new values for the lookup list
    protected boolean runLoadLookupLists() {
        return true;
    }

    protected boolean runFieldInit() {
        return true;
    }

	protected boolean upgradeNeeded(Connection conn) throws SQLException {
		return Constants.compareVersions("1.7.0", DatabaseConnectionUtils.getDatabaseVersionInfo(conn)) == Constants.VERSION_GREATER;
	}

    protected String getWarningMessage() {
        String message = "Digital Object Records will be updated.";

        return message;
    }
}