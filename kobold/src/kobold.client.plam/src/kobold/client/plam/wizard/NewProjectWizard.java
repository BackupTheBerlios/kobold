/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, Bettina Druckenmueller,
 * Anselm Garbe, Michael Grosse, Tammo van Lessen, Martin Plies, Oliver Rendgen,
 * Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * $Id: NewProjectWizard.java,v 1.2 2004/05/13 15:04:37 vanto Exp $
 *  
 */
package kobold.client.plam.wizard;

import java.lang.reflect.InvocationTargetException;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProjectNature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

/**
 * Wizard for creating new Kobold Productlines Projects
 */
public class NewProjectWizard extends Wizard implements INewWizard, IExecutableExtension {
	private IWorkbench workbench;
	private IStructuredSelection selection;

	private WizardNewProjectCreationPage mainPage;
	private NewProjectWizardServerPage projectWizardServerPage;

	// cache of newly-created project
	private IProject newProject;
	private IConfigurationElement config;
	
	
	/**
	 *  
	 */
	public NewProjectWizard() 
	{
	}
	
	/**
	 * @see Wizard#performFinish
	 */
	public boolean performFinish() 
	{
		createNewProject();
		if (newProject == null) {
			return false;
		} else {	
			return true;
		}

	}
	
	/**
	 * @see Wizard#init
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) 
	{
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(Messages.getString("NewProjectWizard.WindowTitle")); //$NON-NLS-1$
		setDefaultPageImageDescriptor(KoboldPLAMPlugin
				.getImageDescriptor("icons/kprojectwiz.gif")); //$NON-NLS-1$
	}
	
	public void addPages() 
	{
		super.addPages();
		mainPage = new WizardNewProjectCreationPage("basicNewProjectPage"); //$NON-NLS-1$
		mainPage.setTitle(Messages.getString("NewProjectWizard.CreationPageTitle")); //$NON-NLS-1$
		mainPage
				.setDescription(Messages.getString("NewProjectWizard.CreationPageDesc")); //$NON-NLS-1$
		mainPage.setImageDescriptor(KoboldPLAMPlugin
				.getImageDescriptor("icons/kprojectwiz.gif")); //$NON-NLS-1$
		this.addPage(mainPage);
		projectWizardServerPage = new NewProjectWizardServerPage("serverpage"); //$NON-NLS-1$
		projectWizardServerPage
				.setTitle(Messages.getString("NewProjectWizard.ServerPageTitle")); //$NON-NLS-1$
		projectWizardServerPage
				.setDescription(Messages.getString("NewProjectWizard.ServerPageDesc")); //$NON-NLS-1$
		projectWizardServerPage.setImageDescriptor(KoboldPLAMPlugin
				.getImageDescriptor("icons/kprojectwiz.gif")); //$NON-NLS-1$
		this.addPage(projectWizardServerPage);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		this.config = config;
		System.out.println(config);
	}
	
	/**
	 * Creates a new project resource with the selected name.
	 * <p>
	 * In normal usage, this method is invoked after the user has pressed Finish
	 * on the wizard; the enablement of the Finish button implies that all
	 * controls on the pages currently contain valid values.
	 * </p>
	 * <p>
	 * Note that this wizard caches the new project once it has been
	 * successfully created; subsequent invocations of this method will answer
	 * the same project resource without attempting to create it again.
	 * </p>
	 * 
	 * @return the created project resource, or <code>null</code> if the
	 *         project was not created
	 */
	private IProject createNewProject() 
	{
		if (newProject != null)
			return newProject;
		
		// get a project handle
		final IProject newProjectHandle = mainPage.getProjectHandle();
		
		// get a project descriptor
		IPath defaultPath = Platform.getLocation();
		IPath newPath = mainPage.getLocationPath();
		if (defaultPath.equals(newPath))
			newPath = null;
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		final IProjectDescription description = workspace
				.newProjectDescription(newProjectHandle.getName());
		
		description.setLocation(newPath);
		
		// set project nature
		description.setNatureIds(new String[] {KoboldProjectNature.NATURE_ID});
		
		// set project set builder
		// TODO
		
		// create the new project operation
		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
			protected void execute(IProgressMonitor monitor)
					throws CoreException {
				createProject(description, newProjectHandle, monitor);
			}
		};
		
		BasicNewProjectResourceWizard.updatePerspective(config);
		// create project
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return null;
		} catch (InvocationTargetException e) {
			// ie.- one of the steps resulted in a core exception
			Throwable t = e.getTargetException();
			if (t instanceof CoreException) {
				if (((CoreException) t).getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
					MessageDialog.openError(getShell(), Messages.getString("NewProjectWizard.Error"), //$NON-NLS-1$
								Messages.getString("NewProjectWizard.CaseErrorMsg")); //$NON-NLS-1$
				} else {
					ErrorDialog.openError(getShell(), Messages.getString("NewProjectWizard.Error"),	null, ((CoreException) t).getStatus());
				}
			} else {
				// CoreExceptions are handled above, but unexpected runtime
				// exceptions and errors may still occur.
				Platform.getPlugin(PlatformUI.PLUGIN_ID).getLog().log(
						new Status(Status.ERROR, PlatformUI.PLUGIN_ID, 0, t
								.toString(), t));
				MessageDialog.openError(getShell(),	Messages.getString("NewProjectWizard.Error"),
							Messages.getString("NewProjectWizard.InteralError")); //$NON-NLS-1$
			}
			return null;
		}

		newProject = newProjectHandle;
		
		createProjectResources(newProject);

		try {
			newProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return newProjectHandle;
	}
	
	protected void createProjectResources(IProject project) {
		KoboldProjectNature kNature = null;
		try {
			kNature = (KoboldProjectNature)project.getNature(KoboldProjectNature.NATURE_ID);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		if (kNature != null) {
			//TODO
		}
	}
	
	/**
	 * Creates a project resource given the project handle and description.
	 */
	private void createProject(IProjectDescription description, IProject projectHandle, IProgressMonitor monitor) throws CoreException, OperationCanceledException {
		try {
			monitor.beginTask("",2000);//$NON-NLS-1$
	
			projectHandle.create(description, new SubProgressMonitor(monitor,1000));
	
			if (monitor.isCanceled())
				throw new OperationCanceledException();
	
			projectHandle.open(new SubProgressMonitor(monitor,1000));
	
		} finally {
			monitor.done();
		}
	}

}