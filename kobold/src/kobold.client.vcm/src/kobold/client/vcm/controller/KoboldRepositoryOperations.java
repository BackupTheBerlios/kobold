/*
 * Created on 12.05.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.vcm.controller;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations;

/**
 * @author schneipk
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface KoboldRepositoryOperations extends SimpleAccessOperations {
	
	public void precheckin(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException;
	
	public void postcheckin(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException;
	
	public void precheckout(IResource resources, int depth) throws TeamException;
	
	public void postcheckout(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException;
}
