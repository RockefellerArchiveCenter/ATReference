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
 */

package org.archiviststoolkit.exceptions;

/**
 * This exception is used to mark (fatal) failures in infrastructure and system code.
 *
 * @author Christian Bauer <christian@hibernate.org>
 */
public class DuplicateLinkException
	extends Exception {

	public DuplicateLinkException() {
	}

	public DuplicateLinkException(String message) {
		super(message);
	}

	public DuplicateLinkException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateLinkException(Throwable cause) {
		super(cause);
	}
}
