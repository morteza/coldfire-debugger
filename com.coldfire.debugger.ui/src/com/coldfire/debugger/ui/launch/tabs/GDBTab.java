/*******************************************************************************
 * Copyright (c) 2000-2007 Eclipse.org and others.
 * All rights reserved.
 *
 * Contributors:
 * Eclipse.org - Initial implementation
 * Morteza Ansari - Coldfire Debugger Adaption
 *******************************************************************************/
package com.coldfire.debugger.ui.launch.tabs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.cdt.core.model.IBinary;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.debug.core.CDebugCorePlugin;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.debug.core.ICDebugConfiguration;
import org.eclipse.cdt.launch.internal.ui.AbstractCDebuggerTab;
import org.eclipse.cdt.launch.internal.ui.LaunchMessages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class GDBTab extends AbstractCDebuggerTab {

	public GDBTab() {
	}

	public void createControl(Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		setControl(comp);
		GridLayout layout = new GridLayout(2, true);
		comp.setLayout(layout);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL_HORIZONTAL;
		gd.grabExcessHorizontalSpace = true;
		comp.setLayoutData(gd);

		createDebuggerCombo(comp, 1);
		createDebuggerGroup(comp, 2);
	}

	protected void loadDebuggerComboBox(ILaunchConfiguration config,
			String selection) {
		ICDebugConfiguration[] debugConfigs;
		String configPlatform = getPlatform(config);
		debugConfigs = CDebugCorePlugin.getDefault().getDebugConfigurations();
		Arrays.sort(debugConfigs, new Comparator<ICDebugConfiguration>() {

			public int compare(ICDebugConfiguration o1,ICDebugConfiguration o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		List<ICDebugConfiguration> list = new ArrayList<ICDebugConfiguration>();
		String mode=ICDTLaunchConfigurationConstants.DEBUGGER_MODE_RUN;
		String defaultSelection = selection;
		for (int i = 0; i < debugConfigs.length; i++) {
			if (debugConfigs[i].supportsMode(mode)) {
				String debuggerPlatform = debugConfigs[i].getPlatform();
				if (validatePlatform(config, debugConfigs[i])) {
					list.add(debugConfigs[i]);
					if ((defaultSelection.equals("") && debuggerPlatform.equalsIgnoreCase(configPlatform))) { //$NON-NLS-1$
						defaultSelection = debugConfigs[i].getID();
					}
				}
			}
		}
		setInitializeDefault(selection.equals("") ? true : false);
		loadDebuggerCombo((ICDebugConfiguration[]) list
				.toArray(new ICDebugConfiguration[list.size()]),
				defaultSelection);
	}

	protected void updateComboFromSelection() {
		super.updateComboFromSelection();
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		super.setDefaults(config);
		config.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE,
				ICDTLaunchConfigurationConstants.DEBUGGER_MODE_RUN);
		config
				.setAttribute(
						ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ENABLE_VARIABLE_BOOKKEEPING,
						false);
		config
				.setAttribute(
						ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ENABLE_REGISTER_BOOKKEEPING,
						false);
	}

	public void initializeFrom(ILaunchConfiguration config) {
		setInitializing(true);
		super.initializeFrom(config);
		try {
			String id = config.getAttribute(
					ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ID, "");
			loadDebuggerComboBox(config, id);
		} catch (CoreException e) {
		}
		setInitializing(false);
	}

	public void performApply(ILaunchConfigurationWorkingCopy config) {
		super.performApply(config);
		config.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_STOP_AT_MAIN,
				false);
		config.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_START_MODE,
				ICDTLaunchConfigurationConstants.DEBUGGER_MODE_RUN);
		enableAdvancedAttributes(config);
	}

	public boolean isValid(ILaunchConfiguration config) {
		if (!validateDebuggerConfig(config)) {
			return false;
		}
		ICDebugConfiguration debugConfig = getDebugConfig();
		String mode = ICDTLaunchConfigurationConstants.DEBUGGER_MODE_RUN;
		if (!debugConfig.supportsMode(mode)) {
			setErrorMessage(MessageFormat
					.format(
							LaunchMessages
									.getString("CDebuggerTab.Mode_not_supported"), (Object[]) new String[] { mode })); //$NON-NLS-1$
			return false;
		}
		if (super.isValid(config) == false) {
			return false;
		}
		return true;
	}

	protected boolean validatePlatform(ILaunchConfiguration config,
			ICDebugConfiguration debugConfig) {
		String configPlatform = getPlatform(config);
		String debuggerPlatform = debugConfig.getPlatform();
		return (debuggerPlatform.equals("*") || debuggerPlatform.equalsIgnoreCase(configPlatform));
	}

	protected boolean validateCPU(ILaunchConfiguration config,
			ICDebugConfiguration debugConfig) {
		ICElement ce = getContext(config, null);
		String projectCPU = ICDebugConfiguration.CPU_NATIVE;
		if (ce != null) {
			if (ce instanceof IBinary) {
				IBinary bin = (IBinary) ce;
				projectCPU = bin.getCPU();
			}
		}
		return debugConfig.supportsCPU(projectCPU);
	}

	protected boolean validateDebuggerConfig(ILaunchConfiguration config) {
		ICDebugConfiguration debugConfig = getDebugConfig();
		if (debugConfig == null) {
			setErrorMessage(LaunchMessages
					.getString("CDebuggerTab.No_debugger_available")); //$NON-NLS-1$
			return false;
		}
		if (!validatePlatform(config, debugConfig)) {
			setErrorMessage(LaunchMessages
					.getString("CDebuggerTab.Platform_is_not_supported")); //$NON-NLS-1$
			return false;
		}
		if (!validateCPU(config, debugConfig)) {
			setErrorMessage(LaunchMessages
					.getString("CDebuggerTab.CPU_is_not_supported")); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	protected void updateLaunchConfigurationDialog() {
		super.updateLaunchConfigurationDialog();
	}

	private void enableAdvancedAttributes(ILaunchConfigurationWorkingCopy config) {
		config.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ENABLE_VARIABLE_BOOKKEEPING,
				Boolean.TRUE);
		config.setAttribute(
				ICDTLaunchConfigurationConstants.ATTR_DEBUGGER_ENABLE_REGISTER_BOOKKEEPING,
				Boolean.TRUE);
	}

	protected Shell getShell() {
		return super.getShell();
	}

	public void dispose() {
		super.dispose();
	}

	protected void setInitializeDefault(boolean init) {
		super.setInitializeDefault(init);
	}
}
