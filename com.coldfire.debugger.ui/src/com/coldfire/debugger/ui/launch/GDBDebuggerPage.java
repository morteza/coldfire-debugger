/*******************************************************************************
 * Copyright (c) 2000, 2004 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     Morteza Ansari - Coldfire Debugger Adaption
 ********************************************************************************/
package com.coldfire.debugger.ui.launch;

import java.io.File;

import org.eclipse.cdt.debug.mi.core.IMILaunchConfigurationConstants;
import org.eclipse.cdt.debug.mi.internal.ui.MIUIMessages;
import org.eclipse.cdt.debug.mi.internal.ui.StandardGDBDebuggerPage;
import org.eclipse.cdt.utils.ui.controls.ControlFactory;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;

import com.coldfire.debugger.core.*;

public class GDBDebuggerPage extends StandardGDBDebuggerPage {
	private boolean isInit = false;

	public void createMainTabX(Composite tabFolder) {
		Composite comp = ControlFactory.createCompositeEx(tabFolder, 1,
				GridData.FILL_BOTH);
		((GridLayout) comp.getLayout()).makeColumnsEqualWidth = false;
		Composite subComp = ControlFactory.createCompositeEx(comp, 3,
				GridData.FILL_HORIZONTAL);
		((GridLayout) subComp.getLayout()).makeColumnsEqualWidth = false;
		Label label = ControlFactory.createLabel(subComp, MIUIMessages
				.getString("GDBDebuggerPage.3"));
		GridData gd = new GridData();
		label.setLayoutData(gd);
		fGDBCommandText = ControlFactory.createTextField(subComp, SWT.SINGLE
				| SWT.BORDER);
		fGDBCommandText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent evt) {
				if (!isInitializing())
					updateLaunchConfigurationDialog();
			}
		});
		Button button = createPushButton(subComp, MIUIMessages
				.getString("GDBDebuggerPage.4"), null);
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent evt) {
				handleGDBButtonSelected();
				updateLaunchConfigurationDialog();
			}

			private void handleGDBButtonSelected() {
				FileDialog dialog = new FileDialog(getShell(), SWT.NONE);
				dialog.setText(MIUIMessages.getString("GDBDebuggerPage.5"));
				String gdbCommand = fGDBCommandText.getText().trim();
				int lastSeparatorIndex = gdbCommand.lastIndexOf(File.separator);
				if (lastSeparatorIndex != -1) {
					dialog.setFilterPath(gdbCommand.substring(0,
							lastSeparatorIndex));
				}
				String res = dialog.open();
				if (res == null) {
					return;
				}
				fGDBCommandText.setText(res);
			}
		});
	}

	public void createSolibTab(TabFolder tabFolder) {
	}

	protected boolean isInitializing() {
		return isInit;
	}

	private void setInitializing(boolean isInitializing) {
		isInit = isInitializing;
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		setInitializing(true);
		super.initializeFrom(configuration);
		String gdbCommand=Constants.pref_GDB_COMMMAND_default;
		String gdbInit=Constants.pref_GDB_INIT_FILE_default;
		try {
			gdbCommand = configuration.getAttribute(Constants.pref_GDB_COMMAND, Constants.pref_GDB_COMMMAND_default);
			gdbInit = configuration.getAttribute(Constants.pref_GDB_INIT_FILE, Constants.pref_GDB_INIT_FILE_default);
			gdbCommand = configuration
					.getAttribute(
							IMILaunchConfigurationConstants.ATTR_DEBUG_NAME,
							gdbCommand);
			gdbInit = configuration.getAttribute(
					IMILaunchConfigurationConstants.ATTR_GDB_INIT, gdbInit);

		} catch (CoreException e) {
		}
		fGDBCommandText.setText(gdbCommand);
		fGDBInitText.setText(gdbInit);
		setInitializing(false);
	}


	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		super.performApply(configuration);
		String gdbCommand = fGDBCommandText.getText();
		gdbCommand.trim();
		configuration.setAttribute(
				IMILaunchConfigurationConstants.ATTR_DEBUG_NAME, gdbCommand);

		String gdbInit = fGDBInitText.getText();
		gdbInit.trim();
		configuration.setAttribute(
				IMILaunchConfigurationConstants.ATTR_GDB_INIT, gdbInit);
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		try{
		String gdbCommand = configuration.getAttribute(Constants.pref_GDB_COMMAND, Constants.pref_GDB_COMMMAND_default);
		String gdbInit = configuration.getAttribute(Constants.pref_GDB_INIT_FILE, Constants.pref_GDB_INIT_FILE_default);
		configuration.setAttribute(
				IMILaunchConfigurationConstants.ATTR_DEBUG_NAME,
				gdbCommand);
		configuration.setAttribute(
				IMILaunchConfigurationConstants.ATTR_GDB_INIT,
				gdbInit);
		}catch(CoreException e){
			
		}
	}

}