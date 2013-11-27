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

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.coldfire.debugger.core.Activator;
import com.coldfire.debugger.core.Constants;

public class GDBPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	IPreferenceStore store;

	// constructor & initialization
	public GDBPreferencePage() {
		super(GRID);
		store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(store);
		setDescription("Coldfire Debugger Preferences");
	}

	public void createFieldEditors() {
		addField(new StringFieldEditor(Constants.pref_GDB_COMMAND, "GDB Path:",
				getFieldEditorParent()));
		addField(new FileFieldEditor(Constants.pref_GDB_INIT_FILE,
				"GDB Init Script:", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
}