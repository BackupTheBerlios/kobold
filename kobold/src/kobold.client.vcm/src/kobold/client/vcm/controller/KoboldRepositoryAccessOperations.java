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



import kobold.client.plam.KoboldPLAMPlugin;
import kobold.common.model.AbstractAsset;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.communication.CVSSererConnection;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.common.data.UserContext;
import kobold.common.io.RepositoryDescriptor;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
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

	private static UserContext userContext = null;
	
	// RepositoryDescriptor of the current Project
	private RepositoryDescriptor currentVCMProvider = null;
	
	// skriptExtension
	private String skriptExtension = null;
	
	// Path of the current Skript Directory (installation Dir)
	private Path skriptPath = null;

	// The prefix/name for the skripts
	
	private final String IMPORT = "import.";
	private final String ADD = "add.";
	private final String UPDATE = "update.";
	private final String COMMIT = "commit.";
	public KoboldRepositoryAccessOperations()
	{
		KoboldVCMPlugin plugin = KoboldVCMPlugin.getDefault();
		String tmpLocation = plugin.getBundle().getLocation();
		// The Location String contains "@update/" this needs to be removed
		this.skriptPath = new Path(tmpLocation.substring(8,tmpLocation.length()));
		this.skriptPath = (Path)skriptPath.append("scripts" + IPath.SEPARATOR);
		String tmpString = System.getProperty("os.name");
		// This tests what OS is used and sets the skript extension accordingly
		if (tmpString.indexOf("Win",0) != -1 ) 
		{
			skriptExtension = "bat";
		}
		else skriptExtension = "sh";
		
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckin(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException {
		if (performOperation) {
			try {
				progress = KoboldPolicy.monitorFor(progress);
				progress.beginTask("precheckin working", 2);
				// @ FIXME read password and user out of whatever
				CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
				connection.setSkriptName(skriptPath.toOSString().concat(IMPORT).concat(skriptExtension));
				connection.open(progress);
				// wait(5000);
				connection.readInpuStreamsToConsole();
				connection.close();	
				progress.done();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckin(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException {
			if (performOperation) {
				try {
					progress = KoboldPolicy.monitorFor(progress);
					progress.beginTask("postcheckin working", 2);
					// @ FIXME read password and user out of whatever
					CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
					connection.setSkriptName(skriptPath.toOSString().concat(UPDATE).concat(skriptExtension));
					connection.open(progress);
					// wait(5000);
					connection.readInpuStreamsToConsole();
					connection.close();	
					progress.done();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#precheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void precheckout(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation
			) throws TeamException {
		if (performOperation) {
			try {
				progress = KoboldPolicy.monitorFor(progress);
				progress.beginTask("precheckout working", 2);
				// @ FIXME read password and user out of whatever
				CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
				// @  FIXME this needs to be changes to the given skript not the usual!
				connection.setSkriptName(skriptPath.toOSString().concat(IMPORT).concat(skriptExtension));
				connection.open(progress);
				// wait(5000);
				connection.readInpuStreamsToConsole();
				connection.close();	
				progress.done();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void postcheckout(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation ) throws TeamException {
		if (performOperation) {
			try {
				progress = KoboldPolicy.monitorFor(progress);
				progress.beginTask("postcheckout working", 2);
				// @ FIXME read password and user out of whatever
				CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
				// @  FIXME this needs to be changes to the given skript not the usual!
				connection.setSkriptName(skriptPath.toOSString().concat(IMPORT).concat(skriptExtension));
				connection.open(progress);
				// wait(5000);
				connection.readInpuStreamsToConsole();
				connection.close();	
				progress.done();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#get(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void get(AbstractAsset[] resources, int depth, IProgressMonitor progress)
			throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("update working", 2);
			// @ FIXME read password and user out of whatever
			CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
			// @  FIXME this needs to be changes to the given skript not the usual!
			connection.setSkriptName(skriptPath.toOSString().concat(UPDATE).concat(skriptExtension));
			connection.open(progress);
			// wait(5000);
			connection.readInpuStreamsToConsole();
			connection.close();	
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkout(AbstractAsset[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("checkout working", 2);
			// @ FIXME read password and user out of whatever
			CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
			// @  FIXME this needs to be changes to the given skript not the usual!
			connection.setSkriptName(skriptPath.toOSString().concat(UPDATE).concat(skriptExtension));
			connection.open(progress);
			// wait(5000);
			connection.readInpuStreamsToConsole();
			connection.close();	
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkin(AbstractAsset[] resources, int depth,
			IProgressMonitor progress) throws TeamException {
		try {
			progress = KoboldPolicy.monitorFor(progress);
			progress.beginTask("checkin working", 2);
			// @ FIXME read password and user out of whatever
			CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
			// @  FIXME this needs to be changes to the given skript not the usual!
			connection.setSkriptName(skriptPath.toOSString().concat(COMMIT).concat(skriptExtension));
			connection.open(progress);
			// wait(5000);
//			connection.readInpuStreamsToConsole();
			connection.close();	
			progress.done();
		} catch (Exception e) {
			e.printStackTrace();
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
	public void postGet(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException
	{
		if (performOperation) {
			try {
				progress = KoboldPolicy.monitorFor(progress);
				progress.beginTask("postget working", 2);
				// @ FIXME read password and user out of whatever
				CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
				// @  FIXME this needs to be changes to the given skript not the usual!
				connection.setSkriptName(skriptPath.toOSString().concat(UPDATE).concat(skriptExtension));
				connection.open(progress);
				// wait(5000);
				connection.readInpuStreamsToConsole();
				connection.close();	
				progress.done();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#preget(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void preget(AbstractAsset[] resources, int depth,
			IProgressMonitor progress, boolean performOperation) throws TeamException
	{
		if (performOperation) {
			try {
				progress = KoboldPolicy.monitorFor(progress);
				progress.beginTask("preget working", 2);
				// @ FIXME read password and user out of whatever
				CVSSererConnection connection = new CVSSererConnection("cvs.berlios.de.","come2me");
				// @  FIXME this needs to be changes to the given skript not the usual!
				connection.setSkriptName(skriptPath.toOSString().concat(UPDATE).concat(skriptExtension));
				connection.open(progress);
				// wait(5000);
				connection.readInpuStreamsToConsole();
				connection.close();	
				progress.done();
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	/**
	 * @param currentVCMProvider The currentVCMProvider to set.
	 */
	public void setCurrentVCMProvider(RepositoryDescriptor currentVCMProvider) {
		this.currentVCMProvider = currentVCMProvider;
	}
	/* (non-Javadoc)
	 * @see kobold.client.vcm.controller.KoboldRepositoryOperations#postcheckout(kobold.client.plam.model.AbstractAsset[], int, org.eclipse.core.runtime.IProgressMonitor, boolean)
	 */
	public void postcheckout(IResource[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException {
		// TODO Auto-generated method stub
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#get(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void get(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException {
//		 TODO Not needed with type IResource using AbstractAsset instead
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkout(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkout(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException {
//		 TODO Not needed with type IResource using AbstractAsset instead
		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations#checkin(org.eclipse.core.resources.IResource[], int, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void checkin(IResource[] resources, int depth, IProgressMonitor progress) throws TeamException {
		// TODO Not needed with type IResource using AbstractAsset instead
		
	}
}
