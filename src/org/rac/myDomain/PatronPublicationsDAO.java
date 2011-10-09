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
 * @author Lee Mandell
 * Date: Jul 16, 2010
 * Time: 10:46:23 AM
 */

package org.rac.myDomain;

import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.*;
import org.archiviststoolkit.mydomain.DomainAccessObjectImpl;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.rac.model.PatronPublications;
import org.rac.model.PatronPublicationsNames;
import org.rac.model.PatronPublicationsSubjects;

import java.util.Collection;

public class PatronPublicationsDAO extends DomainAccessObjectImpl {

	public PatronPublicationsDAO() {
		super(PatronPublications.class);
	}

	public Collection findBySubject(Subjects subject) {

		Session session = SessionFactory.getInstance().openSession();
		return  session.createCriteria(PatronPublications.class)
				.createCriteria(PatronPublications.PROPERTYNAME_SUBJECTS)
					.add(Restrictions.eq(PatronPublicationsSubjects.PROPERTYNAME_SUBJECT, subject)).list();

	}

	public Collection findByName(Names name) {

		Session session = SessionFactory.getInstance().openSession();
		return  session.createCriteria(PatronPublications.class)
				.createCriteria(PatronPublications.PROPERTYNAME_NAMES)
					.add(Restrictions.eq(PatronPublicationsNames.PROPERTYNAME_NAME, name)).list();

	}
}