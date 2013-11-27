/*******************************************************************************
 * Copyright (c) 2007 Morteza Ansari.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Morteza Ansari - Initial implementation
 *******************************************************************************/
package com.coldfire.debugger.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class CFProgrammer implements IWorkbenchWindowActionDelegate {

	IWorkbenchWindow window;

	public CFProgrammer() {}

	public void selectionChanged(IAction action, ISelection selection) {
	}

	public void run(IAction action) {
		CFProgrammerUI ui= new CFProgrammerUI(window.getShell());
		ui.open();
	}

	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	public void dispose() {
	}
}
