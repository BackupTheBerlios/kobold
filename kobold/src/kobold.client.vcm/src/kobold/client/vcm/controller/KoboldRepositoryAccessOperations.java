/*
 * Created on 12.05.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.vcm.controller;

import kobold.client.vcm.communication.CVSSererConnection;
import kobold.client.vcm.communication.KoboldPolicy;

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
	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckin(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// TODO Implement functionality in Iteration II
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckin(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
//		 TODO Implement functionality in Iteration II
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckout(IResource[] resources, int depth, IProgressMonitor progress
			) throws TeamException {
		try {
//			progress = KoboldPolicy.monitorFor(progress);
//			progress.beginTask(KoboldPolicy.bind("precheckout.working"), resources.length);
//			File test = new File("C:\\temp\\test.bat");
//			Runtime.getRuntime().exec("cmd.exe",null,test);
//			System.out.println("lala");
/*			CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
			connection.open(monitor);*/
		} catch (Exception e) {
			//			 TODO Implement functionality in Iteration II
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckout(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		//		 TODO Implement functionality in Iteration II
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#get(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void get(IResource[] resources, int depth, IProgressMonitor progress)
			throws TeamException {
		progress = KoboldPolicy.monitorFor(progress);
		progress.beginTask(KoboldPolicy.bind("get.working"), resources.length);
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
		// TODO Implement functionality in Iteration II
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
			IProgressMonitor progress) throws TeamException
	{
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckout(org.eclipse.core.resources.IResource[], int)
	 */
	public void precheckout(IResource[] resources, int depth)
			throws TeamException
	{
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#preget(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void preget(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException
	{
		// TODO Auto-generated method stub
	}
}
