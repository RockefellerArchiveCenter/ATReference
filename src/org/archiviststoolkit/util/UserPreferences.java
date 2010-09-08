/**
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
 * @author Lee Mandell
 * Date: Dec 30, 2005
 * Time: 10:26:36 AM
 */

package org.archiviststoolkit.util;

import org.archiviststoolkit.ApplicationFrame;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.dialog.UserPreferencesDialog;
import org.archiviststoolkit.dialog.ErrorDialog;

import java.util.prefs.Preferences;
import java.util.prefs.BackingStoreException;
import java.awt.*;

public class UserPreferences {

	public static final String DATABASE_URL = "databaseUrl";
	public static final String DATABASE_USERNAME = "databaseUsername";
	public static final String DATABASE_PASSWORD = "databasePassword";
	public static final String SAVE_PATH = "savepath";
	public static final String DATABASE_TYPE = "databaseType";

	private static UserPreferences singleton = null;

	private UserPreferences() {
	}

	public static UserPreferences getInstance() {
		if (singleton == null) {
			singleton = new UserPreferences();
		}

		return singleton;
	}

	private String databaseUrl = "";
	private String databaseUserName = "";
	private String databasePassword = "";
	private String savePath = "";
	private String databaseType = "";

	public String getDatabaseUrl() {
		return databaseUrl;
	}

	public void populateFromPreferences() {
		Preferences userPrefs = Preferences.userNodeForPackage(ApplicationFrame.class);
		this.setDatabaseUrl(userPrefs.get(DATABASE_URL, ""));
		this.setDatabaseUserName(userPrefs.get(DATABASE_USERNAME, ""));
		this.setDatabasePassword(userPrefs.get(DATABASE_PASSWORD, ""));
		this.setDatabaseType(userPrefs.get(DATABASE_TYPE, SessionFactory.DATABASE_TYPE_MYSQL));
		this.setSavePath(userPrefs.get(SAVE_PATH, "."));
	}

	public void saveToPreferences() {
		Preferences userPrefs = Preferences.userNodeForPackage(ApplicationFrame.class);
		userPrefs.put(DATABASE_URL, this.getDatabaseUrl());
		userPrefs.put(DATABASE_USERNAME, this.getDatabaseUserName());
		userPrefs.put(DATABASE_PASSWORD, this.getDatabasePassword());
		userPrefs.put(DATABASE_TYPE, this.getDatabaseType());
		userPrefs.put(SAVE_PATH, this.getSavePath());
		try {
			userPrefs.flush();
		} catch (BackingStoreException e) {
			new ErrorDialog("", e).showDialog();
		}
	}

	public boolean checkForDatabaseUrl() {
		if (databaseUrl.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public int displayUserPreferencesDialog() {
		UserPreferencesDialog userPrefDialog = new UserPreferencesDialog();
		return init(userPrefDialog);
	}

	public int displayUserPreferencesDialog(Frame owner) {
		UserPreferencesDialog userPrefDialog = new UserPreferencesDialog(owner);
		return init(userPrefDialog);
	}

	public int displayUserPreferencesDialog(Dialog owner) {
		UserPreferencesDialog userPrefDialog = new UserPreferencesDialog(owner);
		return init(userPrefDialog);
	}

	private int init(UserPreferencesDialog userPrefDialog) {
		userPrefDialog.populateFromUserPreferences(this);
		int returnValue = userPrefDialog.showDialog();
		if (returnValue == javax.swing.JOptionPane.OK_OPTION) {
			this.setDatabaseUrl(userPrefDialog.getDatabaseUrl().trim());
			this.setDatabaseUserName(userPrefDialog.getUserName().trim());
			this.setDatabasePassword(userPrefDialog.getPassword());
			this.setDatabaseType(userPrefDialog.getDatabaseType());
			this.saveToPreferences();
		}

		return returnValue;
	}

	public void updateSessionFactoryInfo() throws UnsupportedDatabaseType {
		SessionFactory.resetFactory();
		SessionFactory.setDatabaseUrl(this.getDatabaseUrl());
		SessionFactory.setUserName(this.getDatabaseUserName());
		SessionFactory.setPassword(this.getDatabasePassword());
		SessionFactory.setDatabaseType(this.getDatabaseType());
	}

	public void setDatabaseUrl(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public String getDatabaseUserName() {
		return databaseUserName;
	}

	public void setDatabaseUserName(String databaseUserName) {
		this.databaseUserName = databaseUserName;
	}

	public String getDatabasePassword() {
		return databasePassword;
	}

	public void setDatabasePassword(char[] databasePassword) {
		this.databasePassword = new String(databasePassword).trim();
	}

	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}
}
