/*******************************************************************************
 * Copyright (c) 2007 Morteza Ansari.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Morteza Ansari - Initial implementation (based on QNX idleLaunch)
 *******************************************************************************/
package com.coldfire.debugger.core.launch;

import java.io.File;

import org.eclipse.cdt.core.IBinaryParser.IBinaryObject;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.debug.core.CDIDebugModel;
import org.eclipse.cdt.debug.core.ICDTLaunchConfigurationConstants;
import org.eclipse.cdt.debug.core.cdi.CDIException;
import org.eclipse.cdt.debug.core.cdi.ICDISession;
import org.eclipse.cdt.debug.core.cdi.model.ICDITarget;
import org.eclipse.cdt.launch.AbstractCLaunchDelegate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;

import com.coldfire.debugger.core.Activator;

public class DebugDelegate extends AbstractCLaunchDelegate {

	public DebugDelegate() {
		super();
	}

	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		SubMonitor submonitor = SubMonitor.convert(monitor, 2);

		setDefaultSourceLocator(launch, configuration);

		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			GDBDebugger debugger = new GDBDebugger();
			ICProject project = verifyCProject(configuration);
			IPath exePath = verifyProgramPath(configuration);
			File exeFile = exePath != null ? exePath.toFile() : null;
			ICDISession session = debugger.createSession(launch, exeFile, submonitor.newChild(1));
			IBinaryObject exeBinary = null;
			if ( exePath != null ) {
				exeBinary = verifyBinary(project, exePath);
			}
			
			try {
				ICDITarget[] targets = session.getTargets();
				for( int i = 0; i < targets.length; i++ ) {
					Process process = targets[i].getProcess();
					IProcess iprocess = null;
					if ( process != null ) {
						iprocess = DebugPlugin.newProcess(launch, process, renderProcessLabel(exePath.toOSString()),
								getDefaultProcessMap() );
					}
					CDIDebugModel.newDebugTarget(launch, project.getProject(), targets[i],
							renderProcessLabel("Coldfire Debugger Target"), iprocess, exeBinary, true, false, false);
				}
				
				debugger.doRunSession(launch, session, submonitor.newChild(1));
			} catch (CoreException e) {
				try {
					session.terminate();
				} catch (CDIException e1) {
					// ignore
				}
				throw e;
			}
		} else {
			cancel("TargetConfiguration not supported",
					ICDTLaunchConfigurationConstants.ERR_INTERNAL_ERROR);
		}
	}
	protected String getPluginID() {
		return Activator.PLUGIN_ID;
	};

}
