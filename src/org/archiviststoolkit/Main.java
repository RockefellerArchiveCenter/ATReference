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
 */

package org.archiviststoolkit;

//==============================================================================
// Import Declarations
//==============================================================================

import org.archiviststoolkit.dialog.ErrorDialog;
import org.archiviststoolkit.dialog.SplashScreen;
import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.*;
import org.archiviststoolkit.structure.ATFieldInfo;
import org.archiviststoolkit.structure.DefaultValues;
import org.archiviststoolkit.util.*;
import org.archiviststoolkit.swing.LoginDialog;
import org.archiviststoolkit.exceptions.DefaultExceptionHandler;
import org.archiviststoolkit.exceptions.WrongNumberOfConstantsRecordsException;
import org.archiviststoolkit.exceptions.UnsupportedDatabaseType;
import org.archiviststoolkit.report.ReportFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.rac.dialogs.ReadingRoomLogonDialog;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.JTextComponent;
import javax.swing.text.DefaultEditorKit;
import java.util.Date;
import java.util.ResourceBundle;
import java.sql.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

public final class Main {

	/**
	 * Class logger.
	 */

	private static Logger logger = Logger.getLogger(Main.class.getPackage().getName());

	private static SplashScreen fSplashScreen;

	private static Boolean showPrefs = false;

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("org.archiviststoolkit.resources.messages.messages");


//	private static Boolean updateToNewVersion = false;

	/**
	 * Dummy contructor to cover the default constructor.
	 * It is infact never called by the code, except for
	 * getting hold of the preference usernode.
	 */

	private Main() {
	}

	/**
	 * Main entry point for the salesforge application.
	 * Currently no command line parameters are required by the application.
	 *
	 * @param args command line parameters
	 */

	public static void main(final String[] args) {

		checkJavaVersion();

		System.setProperty("apple.laf.useScreenMenuBar", "true");
		ApplicationFrame mainFrame = null;
		fSplashScreen = new SplashScreen("Initializing");
		try {
			mainFrame = ApplicationFrame.getInstance();
		} catch (RuntimeException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		} catch (Exception e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		}
		showSplashScreen();
		ApplicationFrame.getInstance().getTimer().reset();
		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler());
		Toolkit.getDefaultToolkit().addAWTEventListener(new MyAWTListener(), AWTEvent.MOUSE_EVENT_MASK);
		Toolkit.getDefaultToolkit().addAWTEventListener(new StartupKeyListener(), AWTEvent.KEY_EVENT_MASK);

		UIDefaults uiDefaults = UIManager.getDefaults();
		uiDefaults.put("TextArea.background", Color.white);

		//get user preferences
		UserPreferences userPrefs = UserPreferences.getInstance();
		userPrefs.populateFromPreferences();

        Font userFont =  userPrefs.getFont();

        // see if to use the default font
        if(userFont == null) { // use the old default font
            userFont = new Font("Trebuchet MS", Font.PLAIN, 13);   
        }

        // if font is not null 
        if(userFont != null) {
            UIManager.put("TextField.font", new FontUIResource(userFont));
            UIManager.put("TextArea.font", new FontUIResource(userFont));
            UIManager.put("PasswordField.font", new FontUIResource(userFont));
            UIManager.put("Tree.font", new FontUIResource(userFont));
            UIManager.put("Table.font", new FontUIResource(userFont));
            UIManager.put("Label.font", new FontUIResource(userFont));
            UIManager.put("ComboBox.font", new FontUIResource(userFont));
            UIManager.put("ComboBoxItem.font", new FontUIResource(userFont));
            //UIManager.put("TableHeader.font", new FontUIResource(userFont));
        }

        // now see if to bybass login
		Boolean skipLogon = false;
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("bypassLogin")) {
				skipLogon = true;
			}
			if (args.length == 2) {
				if (args[1].equalsIgnoreCase("showPrefs")) {
					showPreferences(userPrefs);
				}
			}
		}

		if (showPrefs) {
			showPreferences(userPrefs);
		}

		if (!resourceBundle.getString("archiviststoolkit.releaseType").equals("production")) {
			JOptionPane.showMessageDialog(fSplashScreen, "WARNING: This is an unreleased version of the Archivists' Toolkit.\n" +
					"Please use this version for testing purposes only. \nThe Archivists' Toolkit is not responsible for any " +
					"data loss resulting from the use of an unreleased software version. \nThanks and happy testing!", "Pre-release software", JOptionPane.WARNING_MESSAGE);
		}

		// add shutdown hook
		MyShutdownHook shutdownHook = new MyShutdownHook();
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		} catch (InstantiationException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		} catch (IllegalAccessException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		} catch (UnsupportedLookAndFeelException e) {
			new ErrorDialog("", StringHelper.getStackTrace(e)).showDialog();
		}

		if (!userPrefs.checkForDatabaseUrl()) {
			JOptionPane.showMessageDialog(fSplashScreen, "This appears to be the first time the AT was launched. Please fill out the database" +
					" connection information");
			userPrefs.displayUserPreferencesDialog(fSplashScreen);
		}
		try {
			userPrefs.updateSessionFactoryInfo();
		} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
			new ErrorDialog("Error connecting to database", unsupportedDatabaseType).showDialog();
		}

		fSplashScreen.setMessageText("Accessing database");
		try {
			connectAndTest(userPrefs);
		} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
			new ErrorDialog("Error connecting to database", unsupportedDatabaseType).showDialog();
		}

		fSplashScreen.setMessageText("Loading repositories");
		if (!Repositories.loadRepositories()) {
			System.exit(1);
		}

		fSplashScreen.setMessageText("User logon");
		if (skipLogon) {
			Users currentUser = new Users();
			currentUser.setUserName(Users.USERNAME_DEVELOPER);
			currentUser.setRepository(Repositories.lookupRepositoryByName("Archivists' Toolkit"));
			currentUser.setAccessClass(Users.ACCESS_CLASS_SUPERUSER);
			ApplicationFrame.getInstance().setCurrentUser(currentUser);
		} else {
			try {
				ApplicationFrame.getInstance().setCurrentUser(diaplayLoginAndRetrunUser(userPrefs));
			} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
				new ErrorDialog("Error connecting to database", unsupportedDatabaseType).showDialog();
			}
		}

		logSession();
//		showSplashScreen();
		fSplashScreen.setMessageText("Loading Lookup Lists");
//		logger.info("Loading Lookup Lists");
		if (!LookupListUtils.loadLookupLists()) {
			System.exit(1);
		}

		fSplashScreen.setMessageText("Loading Notes Etc. Types");
//		logger.info("Loading Notes Etc. Types");
		if (!NoteEtcTypesUtils.loadNotesEtcTypes()) {
			System.exit(1);
		}

		try {
			Constants.loadDefaultDateFormat();
		} catch (PersistenceException e) {
			new ErrorDialog("", e).showDialog();
			System.exit(1);
		} catch (LookupException e) {
			new ErrorDialog("", e).showDialog();
			System.exit(1);
		} catch (WrongNumberOfConstantsRecordsException e) {
			new ErrorDialog("", e).showDialog();
			System.exit(1);
		}

		fSplashScreen.setMessageText("Loading Field Information");
		ATFieldInfo.loadFieldList();

		fSplashScreen.setMessageText("Loading Rapid Data Entry Screens");
//		logger.info("Loading Notes Etc. Types");
		if (!RDEFactory.getInstance().loadRDEs()) {
			System.exit(1);
		}

		fSplashScreen.setMessageText("Loading Location Information");
		LocationsUtils.initLocationLookupList();

		fSplashScreen.setMessageText("Loading Default Value Information");
		DefaultValues.initDefaultValueLookup();

		fSplashScreen.setMessageText("Loading In-line tags");
		InLineTagsUtils.loadInLineTags();

//		ThreadGroup group = new LoggingThreadGroup("Logger");
//		new Thread(group, "myThread") {
//		  public void run() {
		fSplashScreen.setMessageText("Loading Reports");
		ReportFactory.getInstance().ParseReportDirectory();

        // load any plugins that are found here
        fSplashScreen.setMessageText("Loading plugins");
        org.archiviststoolkit.plugin.ATPluginFactory.getInstance().parsePluginDirectory();

		mainFrame.finishStartupLog(fSplashScreen);
		mainFrame.initializeMainFrame(fSplashScreen);
		logger.log(Level.INFO, "\nApplication Startup times\n" + ApplicationFrame.getInstance().getStartupLog());

		//this is for the reference module for reading rooms
		if (ApplicationFrame.getInstance().getCurrentUser().getAccessClass() == Users.ACCESS_CLASS_REFERENCE_STAFF) {
//			try {
			EventQueue.invokeLater(new SplashScreenCloser());
				 int status = JOptionPane.CANCEL_OPTION;
				ReadingRoomLogonDialog logonDialog = new ReadingRoomLogonDialog(ApplicationFrame.getInstance());
				 do {
					 logonDialog.setInitialFocus();
					 status = logonDialog.showDialog();
					 if (status == JOptionPane.OK_OPTION) {
						 try {
							 logonDialog.searchAndDisplayRecord();
						 } catch (LookupException e) {
							 new ErrorDialog("Error displaying patron record", e).showDialog();
						 } catch (PersistenceException e) {
							 new ErrorDialog("Error saving record", e).showDialog();
						 } catch (UnsupportedTableModelException e) {
							 new ErrorDialog("Error creating dummy calling table", e).showDialog();
						 }
					 }
					 logonDialog.clearData();
				 } while (status == JOptionPane.OK_OPTION);
			System.exit(0);
			     
//				 PatronManagement dialog = new PatronManagement();
//				 dialog.setDialogTitle("Patron Records");
//				dialog.setReadingRoomLogon(true);
//				 ValidatorFactory validatorFactory = ValidatorFactory.getInstance();
//				 //load the custom patron validator
//				 validatorFactory.addValidator(Names.class, new PatronsValidator());
//				 dialog.showDialog();
//				 //restore the regular names validator
//				 validatorFactory.addValidator(Names.class, new NamesValidator());
//			 } catch (DomainEditorCreationException e) {
//				 new ErrorDialog("Error creating editor for Patrons", e).showDialog();
//			 }
		} else {
			mainFrame.setVisible(true);
		}
//		  }
//		}.start();

		EventQueue.invokeLater(new SplashScreenCloser());

        // start the thread that updates any record locks time
        startRecordLockUpdaterThread();
    }

	public static void checkJavaVersion() {
		String javaVersion = System.getProperty("java.version");
		if (javaVersion.startsWith("1.4") || javaVersion.startsWith("1.3") || javaVersion.startsWith("1.2")) {
			new ErrorDialog("Incorrect Java Version",
					"This program requires Java 1.5.x or greater, you are currently running version " + javaVersion,
					ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
			System.exit(1);
		}
	}

	private static void showPreferences(UserPreferences userPrefs) {
		if (userPrefs.displayUserPreferencesDialog(fSplashScreen) == JOptionPane.OK_OPTION) {
			try {
				userPrefs.updateSessionFactoryInfo();
			} catch (UnsupportedDatabaseType unsupportedDatabaseType) {
				new ErrorDialog("Error connecting to database", unsupportedDatabaseType, ErrorDialog.DIALOG_TYPE_ERROR).showDialog();
				System.exit(1);
			}
		} else {
			System.exit(1);
		}
	}

	private static Users diaplayLoginAndRetrunUser(UserPreferences userPrefs) throws UnsupportedDatabaseType {
		LoginDialog login = new LoginDialog(fSplashScreen);
		boolean tryAgain = true;
		Users user;

		int returnStatus;
		do {
			returnStatus = login.showDialog();

			if (returnStatus == JOptionPane.CANCEL_OPTION) {
				System.exit(1);
			} else if (returnStatus == JOptionPane.OK_OPTION) {
				user = Users.lookupUser(login.getUserName(), login.getPassword());
				if (user != null) {
					tryAgain = false;
					return user;
				} else {
					JOptionPane.showMessageDialog(null,
							"Unknown user or wrong password",
							"Login error",
							JOptionPane.WARNING_MESSAGE);
				}
			} else if (returnStatus == LoginDialog.CHOOSE_SERVER) {
				if (userPrefs.displayUserPreferencesDialog(fSplashScreen) == JOptionPane.OK_OPTION) {
					userPrefs.updateSessionFactoryInfo();
					connectAndTest(userPrefs);
					Repositories.loadRepositories();
				}
			}

		} while (tryAgain);

		return null;
	}

	private static void shutdown() {
		UserPreferences.getInstance().saveToPreferences();
		System.out.println("shutdown");
	}

	private static class MyShutdownHook extends Thread {
		public void run() {
			shutdown();
		}
	}

	private static void logSession() {
		try {
			DomainAccessObject access = DomainAccessObjectFactory.getInstance().getDomainAccessObject(Sessions.class);
			Sessions session = new Sessions(ApplicationFrame.getInstance().getCurrentUserName(), new Date());
			access.add(session);
		} catch (PersistenceException e) {
			ErrorDialog dialog = new ErrorDialog("There is a problem logging the session.",
					StringHelper.getStackTrace(e));
			dialog.showDialog();
			System.exit(1);
//             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}

	}

	private static void connectAndTest(UserPreferences userPrefs) throws UnsupportedDatabaseType {
		while (!DatabaseConnectionUtils.testDbConnection()) {
			if (userPrefs.displayUserPreferencesDialog(fSplashScreen) == javax.swing.JOptionPane.OK_OPTION) {
				userPrefs.updateSessionFactoryInfo();
			} else {
				System.exit(1);
			}
		}

		try {
			while (!DatabaseConnectionUtils.checkVersion(DatabaseConnectionUtils.CHECK_VERSION_FROM_MAIN)) {
				if (userPrefs.displayUserPreferencesDialog(fSplashScreen) == JOptionPane.OK_OPTION) {
					userPrefs.updateSessionFactoryInfo();
				} else {
					System.exit(1);
				}
			}
		} catch (ClassNotFoundException e) {
			new ErrorDialog("There is a problem with the database connection. Please check the settings and try again",
					StringHelper.getStackTrace(e)).showDialog();
		} catch (SQLException e) {
			new ErrorDialog("The jdbc driver is missing",
					StringHelper.getStackTrace(e)).showDialog();
		}

        boolean done = false;
        while (!done) {
            try {
                SessionFactory.testHibernate();
                done = true;
            } catch (Exception e) {
                ErrorDialog dialog = new ErrorDialog("There is a problem initializing hibernate. Please contact your system administrator.",
                        StringHelper.getStackTrace(e));
                dialog.showDialog();
                if (userPrefs.displayUserPreferencesDialog(fSplashScreen) == JOptionPane.OK_OPTION) {
                    userPrefs.updateSessionFactoryInfo();
                } else {
                    System.exit(1);
                }
            }
        }

//		if (updateToNewVersion) {
//			//make sure they are a superuser
//			JOptionPane.showMessageDialog(fSplashScreen, "You must be a superuser to do this.");
//        Users user = diaplayLoginAndRetrunUser(userPrefs);
//			if (user.getAccessClass() == Users.ACCESS_CLASS_SUPERUSER) {
//				if (!Constants.updateOrCreateVersionRecord(versionString)) {
//					System.exit(1);
//				}
//			} else {
//				JOptionPane.showMessageDialog(fSplashScreen, "User: " + user.getCategory() + " does not have superuser access.");
//				System.exit(1);
//			}
//		}

	}


	/**
	 * Show a simple graphical splash screen, as a quick preliminary to the main screen.
	 */
	private static void showSplashScreen() {
		fSplashScreen.splash();
	}

	/**
	 * Removes the splash screen.
	 * <p/>
	 * Invoke this <code>Runnable</code> using
	 * <code>EventQueue.invokeLater</code>, in order to remove the splash screen
	 * in a thread-safe manner.
	 */
	private static final class SplashScreenCloser implements Runnable {
		public void run() {
			fSplashScreen.dispose();
		}
	}

	public static class StartupKeyListener implements AWTEventListener {

		public void eventDispatched(AWTEvent awtEvent) {

			KeyEvent keyEvent = (KeyEvent) awtEvent;
			if (keyEvent.isControlDown()) {
				System.out.println("controll down main");
				showPrefs = true;
			}
		}
	}

	public static class MyAWTListener implements AWTEventListener {

		public void eventDispatched(AWTEvent awe) {
			MouseEvent me = (MouseEvent) awe;
			if (! me.isPopupTrigger()) return;

			Object source = me.getSource();
			if (source instanceof JTextComponent) {
				JTextComponent text = (JTextComponent) source;
				JPopupMenu menu = new JPopupMenu();

				JMenuItem menuItem = null;
				if (text.isEditable()) {
					menuItem = new JMenuItem(new DefaultEditorKit.CutAction());
					menuItem.setText("Cut");
					menu.add(menuItem);
				}

				menuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
				menuItem.setText("Copy");
				menu.add(menuItem);

				if (text.isEditable()) {
					menuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
					menuItem.setText("Paste");
					menu.add(menuItem);
				}
				Point p = SwingUtilities.convertPoint(me.getComponent(), me.getPoint(), text);
				menu.show(text, p.x, p.y);
			}
		}
	}

    /**
     * Method to begin start the thread that updates the
     */
    private static void startRecordLockUpdaterThread() {
        Thread performer = new Thread(new Runnable() {
            public void run() {
                try {
                    while(true) {
                        Thread.sleep(RecordLocks.RECORD_LOCK_EXPIRE_TIME); // sleep for this amount of time, for now 5 min
                        RecordLockUtils.updateRecordLocksTime();
                    }
                } catch (InterruptedException e) {
                    System.err.println("Record Lock Updator Thread Interrupted");
                }
            }
        }, "Record Lock Updater Thread");
        performer.start();    
    }
}