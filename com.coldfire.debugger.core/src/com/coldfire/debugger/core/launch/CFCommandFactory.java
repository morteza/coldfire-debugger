/*******************************************************************************
 * Copyright (c) 2007 Morteza Ansari
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Morteza Ansari - Initial API and implementation
 *******************************************************************************/
package com.coldfire.debugger.core.launch;

import org.eclipse.cdt.debug.mi.core.command.factories.StandardCommandFactory;

public class CFCommandFactory extends StandardCommandFactory {

	public CFCommandFactory(String miVersion) {
		super(miVersion);
	}

}