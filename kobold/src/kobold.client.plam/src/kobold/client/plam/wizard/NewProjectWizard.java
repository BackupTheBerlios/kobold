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
 * $Id: NewProjectWizard.java,v 1.21 2004/08/02 16:52:31 garbeam Exp $
 *  
 */
package kobold.client.plam.wizard;

import java.lang.reflect.InvocationTargetException;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProjectNature;
import kobold.client.plam.PLAMProject;
import kobold.client.plam.controller.ServerHelper;
import kobold.client.plam.workflow.LocalMessageQueue;
import kobold.common.data.KoboldMessage;
import kobold.common.data.Productline;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
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
	private NewProjectWizardServerPage serverPage;

	// cache of newly-created project
	private IProject newProject;
	private IConfigurationElement config;
	private ProductlineChooserWizardPage chooserPage;
	
	/**
	 */
	public NewProjectWizard() 
	{
	    super();
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
		setNeedsProgressMonitor(true);
		setWindowTitle(Messages.getString("NewProjectWizard.WindowTitle")); //$NON-NLS-1$
		setDefaultPageImageDescriptor(KoboldPLAMPlugin
				.getImageDescriptor("icons/kprojectwiz.gif")); //$NON-NLS-1$
	}
	
	public void addPages() 
	{
		super.addPages();

		serverPage = new NewProjectWizardServerPage(); //$NON-NLS-1$
		serverPage.setTitle(Messages.getString("NewProjectWizard.ServerPageTitle")); //$NON-NLS-1$
		serverPage.setDescription(Messages.getString("NewProjectWizard.ServerPageDesc")); //$NON-NLS-1$
		serverPage.setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/kprojectwiz.gif")); //$NON-NLS-1$
		this.addPage(serverPage);
		
		chooserPage = new ProductlineChooserWizardPage(); //$NON-NLS-1$
		chooserPage.setTitle(Messages.getString("NewProjectWizard.ChooseThePLTitle")); //$NON-NLS-1$
		chooserPage.setDescription(Messages.getString("NewProjectWizard.ChooseThePLDesc")); //$NON-NLS-1$
		serverPage.setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/kprojectwiz.gif")); //$NON-NLS-1$
		this.addPage(chooserPage);
		
		mainPage = new WizardNewProjectCreationPage("basicNewProjectPage"); //$NON-NLS-1$
		mainPage.setTitle(Messages.getString("NewProjectWizard.CreationPageTitle")); //$NON-NLS-1$
		mainPage.setDescription(Messages.getString("NewProjectWizard.CreationPageDesc")); //$NON-NLS-1$
		mainPage.setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/kprojectwiz.gif")); //$NON-NLS-1$
		this.addPage(mainPage);

	}
	
	/**
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		this.config = config;
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
				createProjectResources(newProject);
			}
		};
		
		BasicNewProjectResourceWizard.updatePerspective(config);
		// create project
		try {
			getContainer().run(true, true, op);
		} catch (InterruptedException e) {
			return null;
		} catch (InvocationTargetException e) {
			return null;
		}

		newProject = newProjectHandle;

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
			// create project info
			PLAMProject p = kNature.getPLAMProject();
			p.setServerUrl(serverPage.getServerURL());
			p.setUsername(serverPage.getUsername());
			p.setPassword(serverPage.getPassword());
			p.setProductlineName(chooserPage.getProductlineName());
			p.store();
			
			// fetch server PL
			//Productline pl = ServerHelper.fetchProductline(p);
			//System.out.println(pl.getRepositoryDescriptor());
			//System.out.println(pl.getId());
			// create secure client
			// -> moved lazy to KoboldProjectNature
									
			// create local message queue.
			LocalMessageQueue mq = kNature.getMessageQueue();
			KoboldMessage welcome = new KoboldMessage();
			welcome.setSender("null");
			welcome.setReceiver(p.getUsername());
			welcome.setSubject("Welcome");
			welcome.setMessageText("Just have fun!");
			mq.addMessage(welcome);
			
			//TODO: Remove test stuff
			WorkflowMessage msg = new WorkflowMessage("testworkflow");
			//msg.setWorkflowType("testwf");
			msg.setSender("vanto");
			msg.setReceiver("vanto");
			msg.setSubject("Test workflow");
			msg.setMessageText("Das ist die Erklärung");
			msg.addWorkflowControl(new WorkflowItem("false", "Kobold ist doof", WorkflowItem.CHECK));
			msg.addWorkflowControl(new WorkflowItem("true", "Kobold ist toll", WorkflowItem.CHECK));
			mq.addMessage(msg);
			
			
			WorkflowMessage msg1 = new WorkflowMessage("testwf1");
			//msg1.setWorkflowType("testwf1");
			msg1.setSender("pliesmn");
			msg1.setReceiver("server");
			msg1.setSubject("Test workflow");
			msg1.setMessageText("Das ist die Erklärung");
			msg1.addWorkflowControl(new WorkflowItem("koboldDoof", "Kobold ist doof", WorkflowItem.CHECK));
			msg1.addWorkflowControl(new WorkflowItem("koboldToll", "Kobold ist toll", WorkflowItem.CHECK));	
			msg1.addWorkflowControl(new WorkflowItem("koboldDoof", "Kobold ist doof", WorkflowItem.CHECK));
			msg1.addWorkflowControl(new WorkflowItem("koboldToll", "Kobold ist toll", WorkflowItem.CHECK));
			WorkflowItem c1 = new WorkflowItem("cont1","",WorkflowItem.CONTAINER);
			  msg1.addWorkflowControl(c1);
			  c1.addChild( new WorkflowItem("radio1","radio r1",WorkflowItem.RADIO));
			  c1.addChild( new WorkflowItem("radio2","radio r1",WorkflowItem.RADIO));
			  c1.addChild( new WorkflowItem("check1","check ch1",WorkflowItem.CHECK));
			  WorkflowItem c2 = new WorkflowItem("chek2","description c2",WorkflowItem.CONTAINER);
			    c1.addChild(c2);
			    c2.addChild( new WorkflowItem("raido3","radio r3",WorkflowItem.RADIO));
			    c2.addChild( new WorkflowItem("radio4","radio r4",WorkflowItem.RADIO));
			  c1.addChild( new WorkflowItem("text1","text t1",WorkflowItem.TEXT));	
			  c1.addChild( new WorkflowItem("text2","anderer Text",WorkflowItem.TEXT));	
			mq.addMessage(msg1);		
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