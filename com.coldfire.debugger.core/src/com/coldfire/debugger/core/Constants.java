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

public class Constants {

	public static final String PLUGIN_ID = "com.coldfire.debugger";
	
	public static final String launch_IMAGE_FILE = "launch.imagefile";
	public static final String launch_GDB_INIT_SCRIPT = "launch.gdbinitscript";
	public static final String launch_GDB_START_SCRIPT = "launch.gdbstartscript";

	public static final String pref_GDB_COMMAND = "pref.gdbcommand";
	public static final String pref_GDB_INIT_FILE = "pref.gdbinitfile";

	public static final String pref_IS_INTERNAL_PROGRAMMER="pref.internalprogrammer";
	public static final String pref_EXTPROGRAMMER_PARAM = "pref.extprogrammerparam";
	// default values
	public static final String launch_IMAGE_FILE_default = "main.elf";
	public static final String launch_GDB_INIT_SCRIPT_default = "target bdm /dev/bdmcf0\nload";
	public static final String launch_GDB_START_SCRIPT_default = "set $pc=_start\ncontinue";

	public static final String pref_GDB_COMMMAND_default = "m68k-elf-gdb";
	public static final String pref_GDB_INIT_FILE_default = ".gdbinit";
	public static final Boolean pref_IS_INTERNAL_PROGRAMMER_default = false;
}
