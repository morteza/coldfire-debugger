package com.coldfire.debugger.ui;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.coldfire.debugger.core.Activator;
import com.coldfire.debugger.core.Constants;


public class CFProgrammerUI extends Window{
	Combo imagesList;
	public CFProgrammerUI(Shell parentShell) {
		super(parentShell);
	}
	protected void performDownload(String imagePath){
		IPreferenceStore store=Activator.getDefault().getPreferenceStore();
		boolean is_internal=store.getBoolean(Constants.pref_IS_INTERNAL_PROGRAMMER);
		if (is_internal){
			//TODO: internal programmer
		}else{
			String programmerCommand=store.getString(Constants.pref_EXTPROGRAMMER_PARAM);
			programmerCommand=programmerCommand.replaceAll("<%IMAGE>",imagePath);
			try{
				Process proc = Runtime.getRuntime().exec(programmerCommand);
				String feedback = "";
				int tmp = 0;
				
				InputStream in = proc.getErrorStream();
				while ((tmp = in.read()) != -1) {
					feedback += String.valueOf((char)tmp);
				}
				in.close();
			}catch(Exception e){	
			}
		}
	}

	protected Control createContents(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		parent.setLayout(gridLayout);
		Label label1=new Label(parent,SWT.NORMAL);
		label1.setText("Image Path:");
		imagesList=new Combo(parent,SWT.NORMAL);
		
		//TODO: add all ELF images
		imagesList.add("SALAM");
		Button okBtn=new Button(parent,SWT.NORMAL);
		okBtn.setText("Download Image");
		okBtn.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				performDownload(imagesList.getText());
			}
			public void widgetDefaultSelected(SelectionEvent e) {}
		
		});
 		gridLayout.numColumns = 3;
		parent.pack();
 		return new Composite(parent, SWT.NORMAL);
		
	}

	public static ArrayList<File> getELFList(IProject project){
		File projDir=new File(project.getLocation().toOSString());
		return getELFList(projDir);
	}
	
	private static ArrayList<File> getELFList(File root){
		ArrayList<File> list=new ArrayList<File>();
		for(File file:root.listFiles(new FileFilter() {
			public boolean accept(File arg0) {
				return (arg0!=null && arg0.isFile() && arg0.getName().toLowerCase().endsWith(".elf"));
			}
		})){
			list.add(file);
		}
		for(File file:root.listFiles(new FileFilter() {
			public boolean accept(File arg0) {
				return (arg0!=null && arg0.isDirectory() && arg0.canRead());
			}
		})){
			list.addAll(getELFList(file));
		}
		return list;
	}

}
