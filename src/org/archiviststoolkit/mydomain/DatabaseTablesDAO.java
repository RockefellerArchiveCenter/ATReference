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
 * @author Lee Mandell
 * Date: Nov 28, 2005
 * Time: 3:17:18 PM
 */

package org.archiviststoolkit.mydomain;

import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.structure.DatabaseTables;
import org.archiviststoolkit.structure.DatabaseFields;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

import java.util.Set;

public class DatabaseTablesDAO extends DomainAccessObjectImpl {

	/**
	 * Constructor which builds a DAO for this class.
	 */
	public DatabaseTablesDAO() {
		super(DatabaseTables.class);
	}

	public Set<DatabaseFields> getFieldList(String tableName) {

		Session session = SessionFactory.getInstance().openSession();
		DatabaseTables table = (DatabaseTables) session.createCriteria(DatabaseTables.class)
				.add(Expression.eq("tableName", tableName))
				.uniqueResult();
		session.close();
		if (table != null) {
			return table.getDatabaseFields();
		} else {
			return null;
		}
	}
}
