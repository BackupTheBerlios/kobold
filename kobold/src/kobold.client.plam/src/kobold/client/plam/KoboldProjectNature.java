/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, 
 * Bettina Druckenmueller, Anselm Garbe, Michael Grosse, 
 * Tammo van Lessen,  Martin Plies, Oliver Rendgen, Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 *
 * $Id: KoboldProjectNature.java,v 1.9 2004/05/19 15:08:03 garbeam Exp $
 *
 */
package kobold.client.plam;

import kobold.client.plam.controller.SecureKoboldClient;
import kobold.client.plam.workflow.LocalMessageQueue;
import kobold.common.data.UserContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

/**
 * @see IProjectNature
 */
public class KoboldProjectNature implements IProjectNature, IResourceChangeListener {

	public static final Log logger = LogFactory.getLog(KoboldProjectNature.class);
	public static final String NATURE_ID = "kobold.client.plam.KoboldProjectNature"; //$NON-NLS-1$
	public static final String WORKFLOW_FILENAME = ".messages"; //$NON-NLS-1$
	public static final String PLAM_FILENAME = ".kobold"; //$NON-NLS-1$

	private IProject project;
	private PLAMProject plamProject = null;
	private LocalMessageQueue mqueue = null;
	private SecureKoboldClient client = null;
	private UserContext userContext = null;
	
	/**
	 *
	 */
	public KoboldProjectNature() 
	{
	}

	/**
	 * @see IProjectNature#configure
	 */
	public void configure() throws CoreException 
	{
		logger.info("configure nature");
	}

	/**
	 * @see IProjectNature#deconfigure
	 */
	public void deconfigure() throws CoreException 
	{
		logger.info("deconfigure nature");
	}

	/**
	 * @see IProjectNature#getProject
	 */
	public IProject getProject()  
	{
		return project;
	}

	/**
	 * @see IProjectNature#setProject
	 */
	public void setProject(IProject project)  
	{
		this.project = project;
		project.getWorkspace().addResourceChangeListener(this);
	}
	
	/**
	 * Returns the one an only messagequeue instance.
	 */
	public LocalMessageQueue getMessageQueue() 
	{
		// lazy
		if (mqueue == null) {
			IFile qFile = project.getFile(WORKFLOW_FILENAME);
			mqueue = new LocalMessageQueue(qFile);
		}
		
		return mqueue;
	}
	
	/**
	 * Returns the local workflow file
	 */
	protected IFile getPLAMProjectFile() 
	{
		return project.getFile(PLAM_FILENAME);
	}

	/**
	 * Returns the PLAM project for this Project. (lazy)
	 */	
	public PLAMProject getPLAMProject() 
	{
		// lazy
		if (plamProject == null) {
			IFile pFile = getPLAMProjectFile();
			plamProject = new PLAMProject(pFile);
		}
		
		// update
		plamProject.update();
		return plamProject;
	}			

	public SecureKoboldClient getClient() 
	{
		// lazy
		if (client == null) {
			PLAMProject p = getPLAMProject();
			try {
				client = new SecureKoboldClient(p.getServerUrl());
			} catch (Exception e) {
				logger.error("An error has occured during connecting to server "+p.getServerUrl(), e);
			}
		}
		return client;
	}

	/**
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event) {
		//logger.info(event);		
	}
	
	/**
	 * @return
	 */
	public UserContext getUserContext() {
		return userContext;
	}

	/**
	 * @param context
	 */
	public void setUserContext(UserContext context) {
		this.userContext = context;
	}

}
