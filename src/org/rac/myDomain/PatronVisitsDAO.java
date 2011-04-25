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
 * Date: Jul 16, 2010
 * Time: 10:46:23 AM
 */

package org.rac.myDomain;

import org.archiviststoolkit.hibernate.SessionFactory;
import org.archiviststoolkit.model.Names;
import org.rac.model.PatronVisits;
import org.rac.model.PatronVisitsNames;
import org.rac.model.PatronVisitsResearchPurposes;
import org.rac.model.PatronVisitsSubjects;
import org.archiviststoolkit.model.Subjects;
import org.archiviststoolkit.mydomain.DomainAccessObjectImpl;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.Collection;
import java.util.Date;

public class PatronVisitsDAO extends DomainAccessObjectImpl {

	public PatronVisitsDAO() {
		super(PatronVisits.class);
	}

	public Collection findBySubject(Subjects subject) {

		Session session = SessionFactory.getInstance().openSession();
		return  session.createCriteria(PatronVisits.class)
				.createCriteria(PatronVisits.PROPERTYNAME_SUBJECTS)
					.add(Restrictions.eq(PatronVisitsSubjects.PROPERTYNAME_SUBJECT, subject)).list();

	}

	public Collection findByName(Names name) {

		Session session = SessionFactory.getInstance().openSession();
		return  session.createCriteria(PatronVisits.class)
				.createCriteria(PatronVisits.PROPERTYNAME_NAMES)
					.add(Restrictions.eq(PatronVisitsNames.PROPERTYNAME_NAME, name)).list();

	}

	public Collection findByResearchPurpose(String researchPurpose) {

		Session session = SessionFactory.getInstance().openSession();
		return  session.createCriteria(PatronVisits.class)
				.createCriteria(PatronVisits.PROPERTYNAME_RESEARCH_PURPOSES)
					.add(Restrictions.eq(PatronVisitsResearchPurposes.PROPERTYNAME_RESEARCH_PURPOSE, researchPurpose)).list();

	}

	public Collection findByDateRange (Date startDate, Date endDate){

		Session session = SessionFactory.getInstance().openSession();
		return  session.createCriteria(PatronVisits.class)
					.add(Restrictions.ge(PatronVisits.PROPERTYNAME_VISIT_DATE, startDate))
					.add(Restrictions.le(PatronVisits.PROPERTYNAME_VISIT_DATE, endDate)).list();
	}
}
