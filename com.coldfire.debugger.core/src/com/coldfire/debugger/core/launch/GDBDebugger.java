/*******************************************************************************
 * Copyright (c) 2004, 2005 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX Software Systems - Initial API and implementation
 * Morteza Ansari - Coldfire Debugger Adaption
 *******************************************************************************/
package com.coldfire.debugger.core.launch;

import java.io.File;

import org.eclipse.cdt.core.IBinaryParser.IBinaryObject;
import org.eclipse.cdt.debug.core.CDebugCorePlugin;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.debug.core.cdi.ICDISession;
import org.eclipse.cdt.debug.core.cdi.model.ICDITarget;
import org.eclipse.cdt.debug.mi.core.AbstractGDBCDIDebugger;
import org.eclipse.cdt.debug.mi.core.MIException;
import org.eclipse.cdt.debug.mi.core.MIPlugin;
import org.eclipse.cdt.debug.mi.core.MISession;
import org.eclipse.cdt.debug.mi.core.cdi.Session;
import org.eclipse.cdt.debug.mi.core.cdi.model.Target;
import org.eclipse.cdt.debug.mi.core.command.CLICommand;
import org.eclipse.cdt.debug.mi.core.command.CommandFactory;
import org.eclipse.cdt.debug.mi.core.command.MIGDBSetNewConsole;
import org.eclipse.cdt.debug.mi.core.command.MITargetDownload;
import org.eclipse.cdt.debug.mi.core.output.MIInfo;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.DebugUITools;

import com.coldfire.debugger.core.Activator;
import com.coldfire.debugger.core.Constants;

public class GDBDebugger extends AbstractGDBCDIDebugger {

	public ICDISession createDebuggerSession(DebugDelegate launch2,
			ILaunch launch, IBinaryObject exe, IProgressMonitor monitor)
			throws CoreException {
		return super.createDebuggerSession(launch, exe, monitor);
	}

	public ICDISession createSession(ILaunch launch, File executable,
			IProgressMonitor monitor) throws CoreException {
		return super.createSession(launch, executable, monitor);
	}

	public ICDISession createDebuggerSession(ILaunch launch, IBinaryObject exe,
			IProgressMonitor monitor) throws CoreException {
		return super.createDebuggerSession(launch, exe, monitor);
	}

	protected MISession getMISession(Session session) {
		ICDITarget[] targets = session.getTargets();
		if (targets.length == 0 || !(targets[0] instanceof Target))
			return null;
		return ((Target) targets[0]).getMISession();
	}

	protected CFCommandFactory getCommandFactory(ILaunchConfiguration config)
			throws CoreException {
		return new CFCommandFactory(MIPlugin.getMIVersion(config));
	}

	private void executeGDBScript(ILaunchConfiguration configuration,
			String attribute, MISession miSession) throws CoreException {
		String script = configuration.getAttribute(attribute, "");
		script = VariablesPlugin.getDefault().getStringVariableManager()
				.performStringSubstitution(script);
		String[] commands = script.split("\\r?\\n");
		for (int j = 0; j < commands.length; ++j) {
			try {
				CLICommand cli = new CLICommand(commands[j]);
				miSession.postCommand(cli, MISession.FOREVER);
				MIInfo info = cli.getMIInfo();
				if (info == null) {
					throw new MIException("GDB/MI Exception: Timeout");
				}
			} catch (MIException e) {
				MultiStatus status = new MultiStatus(Activator.PLUGIN_ID,
						ICDTLaunchConfigurationConstants.ERR_INTERNAL_ERROR,
						"Inernal Error: Failed command", e);
				status.add(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						ICDTLaunchConfigurationConstants.ERR_INTERNAL_ERROR,
						e == null ? "" : e.getLocalizedMessage(),e));
				CDebugCorePlugin.log(status);
			}
		}
	}

	protected void doStartSession(ILaunch launch, Session session,
			IProgressMonitor monitor) throws CoreException {
		ILaunchConfiguration config = launch.getLaunchConfiguration();
		ICDITarget[] targets = session.getTargets();
		if (targets.length == 0 || !(targets[0] instanceof Target))
			return;
		MISession miSession = ((Target) targets[0]).getMISession();
		getMISession(session);
		CommandFactory factory = miSession.getCommandFactory();
		try {
			MIGDBSetNewConsole newConsole = factory.createMIGDBSetNewConsole();
			miSession.postCommand(newConsole);
			MIInfo info = newConsole.getMIInfo();
			if (info == null) {
				throw new MIException(MIPlugin
						.getResourceString("src.common.No_answer")); //$NON-NLS-1$
			}
		} catch (MIException e) {
		}

		monitor.beginTask("Executing init commands", 1);
		executeGDBScript(config, Constants.launch_GDB_INIT_SCRIPT, miSession);

		// download image
		IResource currProj = DebugUITools.getSelectedResource();
		IProject prj = currProj.getProject();
		String imagePath = prj.getPersistentProperty(new QualifiedName("",
				Constants.launch_IMAGE_FILE_default));
		if (imagePath!=null && !imagePath.equals("")) {
			try {
				monitor.beginTask("Downloading image", 1);
				imagePath = VariablesPlugin.getDefault()
						.getStringVariableManager().performStringSubstitution(
								imagePath);
				MITargetDownload download = factory
						.createMITargetDownload(imagePath);
				miSession.postCommand(download, MISession.FOREVER);
				MIInfo info = download.getMIInfo();
				if (info == null) {
					throw new MIException(MIPlugin
							.getResourceString("src.common.No_answer")); //$NON-NLS-1$
				}
			} catch (MIException e) {
			}
		}
	}

	public void doRunSession(ILaunch launch, ICDISession session,
			IProgressMonitor monitor) throws CoreException {
		ILaunchConfiguration config = launch.getLaunchConfiguration();
		ICDITarget[] targets = session.getTargets();
		if (targets.length == 0 || !(targets[0] instanceof Target))
			return;
		MISession miSession = ((Target) targets[0]).getMISession();
		monitor.beginTask("Executing start commands", 1);
		executeGDBScript(config, Constants.launch_GDB_START_SCRIPT, miSession);
	}
}
