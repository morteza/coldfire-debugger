package com.coldfire.debugger.ui;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

import com.coldfire.debugger.core.Constants;

public class ProjectPropertyPage extends PropertyPage {

	private Combo imageNameCombo;
	
	public ProjectPropertyPage() {
		super();
	}

	private String getStoredImagePath(){
		String imagePath = Constants.launch_IMAGE_FILE_default;
		try{
			String imageName = ((IResource) getElement()).getPersistentProperty(
					new QualifiedName("", Constants.launch_IMAGE_FILE));
			if(imageName !=null && !imageName.equals(""))
				imageNameCombo.setText(imageName);
		} catch (CoreException e) {
			return imagePath;
		}
		return imagePath;
	}
	
	private void addImageFileSection(Composite parent) {
		Composite composite = createDefaultComposite(parent);

		Label imageNameLabel = new Label(composite, SWT.NONE);
		imageNameLabel.setText("Programmer Image File:");

		imageNameCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData();
		//gd.widthHint = convertWidthInCharsToPixels(50);
		imageNameCombo.setLayoutData(gd);

		IResource selected=(IResource)getElement();
		if (selected==null){
			imageNameCombo.setEnabled(false);
			return;
		}
		IProject prj=selected.getProject();
		for(File file:getELFList(prj)){
			imageNameCombo.add(file.getPath());
		}
	}

	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		GridData data = new GridData(GridData.FILL);
		data.grabExcessHorizontalSpace = true;
		composite.setLayoutData(data);

		addImageFileSection(composite);
		getStoredImagePath();
		return composite;
	}

	private Composite createDefaultComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData data = new GridData();
		data.verticalAlignment = GridData.FILL;
		data.horizontalAlignment = GridData.FILL;
		composite.setLayoutData(data);

		return composite;
	}

	public boolean performOk() {
		try {
			((IResource) getElement()).setPersistentProperty(
				new QualifiedName("", Constants.launch_IMAGE_FILE),
				imageNameCombo.getText());
		} catch (CoreException e) {
			return false;
		}
		return true;
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