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
package com.coldfire.debugger.core;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		// TODO: clean-up the default values
		store.setDefault(Constants.pref_GDB_COMMAND,
				Constants.pref_GDB_COMMMAND_default);
		store.setDefault(Constants.pref_GDB_INIT_FILE,
				Constants.pref_GDB_INIT_FILE_default);
		store.setDefault(Constants.pref_IS_INTERNAL_PROGRAMMER,
				Constants.pref_IS_INTERNAL_PROGRAMMER_default);

	}

}
