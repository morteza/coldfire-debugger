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
package com.coldfire.debugger.ui.launch;

import org.eclipse.cdt.launch.ui.CMainTab;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

import com.coldfire.debugger.ui.launch.tabs.GDBStartTab;
import com.coldfire.debugger.ui.launch.tabs.GDBTab;

public class CFTabGroup extends
		AbstractLaunchConfigurationTabGroup {
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new CMainTab(CMainTab.DONT_CHECK_PROGRAM),
				new GDBTab(),
				new GDBStartTab(),
				new SourceLookupTab(),
				new CommonTab() };
		setTabs(tabs);
	}
}
