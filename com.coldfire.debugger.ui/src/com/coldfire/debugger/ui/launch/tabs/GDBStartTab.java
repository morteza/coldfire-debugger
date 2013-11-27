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
package com.coldfire.debugger.ui.launch.tabs;

import org.eclipse.cdt.launch.internal.ui.LaunchUIPlugin;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.core.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.coldfire.debugger.core.Constants;

public class GDBStartTab extends AbstractLaunchConfigurationTab{

	private Label lInit, lRun;
	private Text tInit, tRun;

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		createVerticalSpacer(comp, 1);
		createCommandsComponent(comp, 1);
		setControl(comp);
	}

	protected void createCommandsComponent(Composite comp, int i) {
		Composite argsComp = new Composite(comp, SWT.NONE);
		GridLayout projLayout = new GridLayout();
		projLayout.numColumns = 1;
		projLayout.marginHeight = 0;
		projLayout.marginWidth = 0;
		argsComp.setLayout(projLayout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = i;
		argsComp.setLayoutData(gd);

		lInit = new Label(argsComp, SWT.NONE);
		lInit.setText("GDB Initialize script:");
		tInit = new Text(argsComp, SWT.MULTI | SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 100;
		tInit.setLayoutData(gd);
		tInit.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});

		lRun = new Label(argsComp, SWT.NONE);
		lRun.setText("GDB Start script:");
		tRun = new Text(argsComp, SWT.MULTI | SWT.WRAP | SWT.BORDER
				| SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 100;
		tRun.setLayoutData(gd);
		tRun.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}
		});
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.launch_GDB_INIT_SCRIPT,
				Constants.launch_GDB_INIT_SCRIPT_default);
		configuration.setAttribute(Constants.launch_GDB_START_SCRIPT,
				Constants.launch_GDB_START_SCRIPT_default);
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			tInit.setText(configuration.getAttribute(Constants.launch_GDB_INIT_SCRIPT,
					Constants.launch_GDB_INIT_SCRIPT_default));
			tRun.setText(configuration.getAttribute(Constants.launch_GDB_START_SCRIPT,
					Constants.launch_GDB_START_SCRIPT_default));
		} catch (CoreException e) {
			setErrorMessage(e.getStatus().getMessage());
			LaunchUIPlugin.log(e);
		}

	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(Constants.launch_GDB_INIT_SCRIPT,
				getAttributeValueFrom(tInit));
		configuration.setAttribute(Constants.launch_GDB_START_SCRIPT,
				getAttributeValueFrom(tRun));

	}

	public String getName() {
		return "Initialize";
	}

	protected String getAttributeValueFrom(Text text) {
		String content = text.getText().trim();
		if (content.length() > 0) {
			return content;
		}
		return null;
	}

}
