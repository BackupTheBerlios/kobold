/*
 * Created on 12.05.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.vcm.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;

/**
 * @author schneipk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class KoboldRepositoryAccessOperations implements KoboldRepositoryOperations {
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckin(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckin(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckout(IResource resources, int depth
			) throws TeamException {
		try {
			File test = new File("C:\\temp\\test.bat");
			Runtime.getRuntime().exec("cmd.exe",null,test);
			System.out.println("lala");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckout(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#get(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void get(IResource[] resources, int depth, IProgressMonitor progress)
			throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkout(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {

		
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkin(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#uncheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void uncheckout(IResource[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#delete(org.eclipse.core.resources.IResource[], org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void delete(IResource[] resources, IProgressMonitor progress)
			throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#moved(org.eclipse.core.runtime.IPath, org.eclipse.core.resources.IResource, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void moved(IPath source, IResource target, IProgressMonitor progress)
			throws TeamException {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#isCheckedOut(org.eclipse.core.resources.IResource)
	 */
	public boolean isCheckedOut(IResource resource) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#hasRemote(org.eclipse.core.resources.IResource)
	 */
	public boolean hasRemote(IResource resource) {
		// TODO Auto-generated method stub
		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#isDirty(org.eclipse.core.resources.IResource)
	 */
	public boolean isDirty(IResource resource) {
		// TODO Auto-generated method stub
		return false;
	}
}
