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

import org.eclipse.ui.*;

public class DebugPerspective implements IPerspectiveFactory {
	private IPageLayout factory;

	// constructor
	public DebugPerspective() {
		super();
	}

	public void createInitialLayout(IPageLayout factory) {
		this.factory = factory;
		addViews();
		addActionSets();
		addNewWizardShortcuts();
		addPerspectiveShortcuts();
		addViewShortcuts();
	}

	private void addViews() {

		// Creates the overall folder layout.
		IFolderLayout bottom = factory.createFolder("Bottom",
				IPageLayout.BOTTOM, 0.75f, factory.getEditorArea());

		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);

		IFolderLayout left = factory.createFolder("Left", IPageLayout.LEFT,
				0.5f, factory.getEditorArea());
		left.addView("com.coldfire.debugger.ui.RegistersView");
	}

	private void addActionSets() {
		factory.addActionSet("com.coldfire.debugger.ui.ProgrammerActionSet");
		/*
		 * factory.addActionSet("org.eclipse.debug.ui.launchActionSet");
		 * factory.addActionSet("org.eclipse.debug.ui.debugActionSet");
		 * factory.addActionSet("org.eclipse.debug.ui.profileActionSet");
		 * factory.addActionSet("org.eclipse.jdt.debug.ui.JDTDebugActionSet");
		 * factory.addActionSet("org.eclipse.jdt.junit.JUnitActionSet");
		 * //NON-NLS-1 factory.addActionSet("org.eclipse.team.ui.actionSet");
		 * factory.addActionSet("org.eclipse.team.cvs.ui.CVSActionSet");
		 * factory.addActionSet("org.eclipse.ant.ui.actionSet.presentation");
		 * //NON-NLS-1 factory.addActionSet(JavaUI.ID_ACTION_SET);
		 * factory.addActionSet(JavaUI.ID_ELEMENT_CREATION_ACTION_SET);
		 * factory.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET); //NON-NLS-1
		 */
	}

	private void addPerspectiveShortcuts() {
		/*
		 * factory.addPerspectiveShortcut("org.eclipse.team.ui.TeamSynchronizingPerspective");
		 * factory.addPerspectiveShortcut("org.eclipse.team.cvs.ui.cvsPerspective");
		 * factory.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective");
		 */
	}

	private void addNewWizardShortcuts() {
		/*
		 * factory.addNewWizardShortcut("org.eclipse.team.cvs.ui.newProjectCheckout");//NON-NLS-1
		 * factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.folder");//NON-NLS-1
		 * factory.addNewWizardShortcut("org.eclipse.ui.wizards.new.file");//NON-NLS-1
		 */
	}

	private void addViewShortcuts() {
		factory.addShowViewShortcut("com.coldfire.debugger.ui.SimpleView");
	}

}
