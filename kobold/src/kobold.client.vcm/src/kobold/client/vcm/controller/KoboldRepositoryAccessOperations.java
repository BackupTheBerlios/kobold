/*
 * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
 *                    Martin Plies, Michael Grosse, Necati Aydin,
 *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package kobold.client.vcm.controller;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import kobold.client.vcm.communication.CVSSererConnection;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.common.data.UserContext;

import org.eclipse.core.internal.model.PluginMap;
import org.eclipse.core.internal.plugins.PluginDescriptor;
import org.eclipse.core.internal.plugins.PluginRegistry;
import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;

/**
 * @author schneipk
 *
 * This is the actual implementation of the repository Access Interface for use with
 * the Kobold PLAM Tool
 */
public class KoboldRepositoryAccessOperations implements KoboldRepositoryOperations {
	
	/* The Connection to the specified Repository used by the Repository Access Operations
	 */ 
//	CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de","come2me");
	private static UserContext userContext = null;
	
	public KoboldRepositoryAccessOperations()
	{
//		PluginDescriptor.
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckin(IResource[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException {
		// TODO Implement functionality in Iteration II
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckin(IResource[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException {
//		 TODO Implement functionality in Iteration II
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckout(IResource[] resources, int depth, IProgressMonitor progress, boolean performOperation
			) throws TeamException {
		try {
			if (performOperation) {
				progress = KoboldPolicy.monitorFor(progress);
				progress.beginTask("precheckout.working", 2);
//				File test = new File("C:\\temp\\test.bat");
				String[] test = {"schneipk","lalal"};
				Process pr = Runtime.getRuntime().exec("ssh",test );
				InputStream is = pr.getInputStream();
				OutputStream os = pr.getOutputStream();
				
				System.out.println("lala");
				CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
//				connection.open(monitor);			
			}

		} catch (Exception e) {
			e.printStackTrace();
			//			 FIXME Implement functionality in Iteration II
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckout(IResource[] resources, int depth,
			IProgressMonitor progress, boolean performOperation ) throws TeamException {
		if (performOperation) {
			
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#get(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void get(IResource[] resources, int depth, IProgressMonitor progress)
			throws TeamException {
		progress = KoboldPolicy.monitorFor(progress);
//		progress.beginTask(KoboldPolicy.bind("get.working"), resources.length);
//		userContext = Plugin.
		CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de","come2me");
		try
		{
			connection.open(progress);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkout(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		//		 FIXME Implement functionality 
		progress = KoboldPolicy.monitorFor(progress);
		progress.beginTask(KoboldPolicy.bind("checkout.working"), resources.length);
		CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de","come2me");
		try
		{
			connection.open(progress);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkin(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// FIXME Implement functionality 
		progress = KoboldPolicy.monitorFor(progress);
		progress.beginTask(KoboldPolicy.bind("checkin.working"), resources.length);
		CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de","come2me");
		try
		{
			connection.open(progress);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#uncheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void uncheckout(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// TODO No requirement not needed
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#delete(org.eclipse.core.resources.IResource[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void delete(IResource[] resources, IProgressMonitor progress)
			throws TeamException {
		// TODO NOT IN USE / NO REQUIREMENT
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#moved(org.eclipse.core.runtime.IPath, org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void moved(IPath source, IResource target, IProgressMonitor progress)
			throws TeamException {
		// TODO NOT IN USE
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#isCheckedOut(org.eclipse.core.resources.IResource)
	 */
	public boolean isCheckedOut(IResource resource) {
		// TODO NOT IN USE
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#hasRemote(org.eclipse.core.resources.IResource)
	 */
	public boolean hasRemote(IResource resource) {
		// TODO NOT IN USE
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#isDirty(org.eclipse.core.resources.IResource)
	 */
	public boolean isDirty(IResource resource) {
		// TODO NOT IN USE / IN ITERATION I
		return false;
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postGet(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postGet(IResource[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException
	{
		if (performOperation) {
			
		}
		// FIXME Imlpement functionality Iteraion II
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckout(org.eclipse.core.resources.IResource[], int)
	 */
	public void precheckout(IResource[] resources, int depth, boolean performOperation)
			throws TeamException
	{
		if (performOperation) {
			
		}
	}
	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#preget(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void preget(IResource[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException
	{
		if (performOperation) {
			
		}
		//		 FIXME Imlpement functionality Iteraion II
	}
}
