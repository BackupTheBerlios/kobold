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
 * $Id: VCMActionListener.java,v 1.31 2004/11/25 14:59:20 garbeam Exp $
 *
 */
package kobold.client.vcm;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Iterator;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.client.vcm.communication.ScriptServerConnection;
import kobold.client.vcm.controller.KoboldRepositoryHelper;
import kobold.client.vcm.controller.ScriptExecuter;
import kobold.client.vcm.controller.StatusUpdater;
import kobold.common.data.Asset;
import kobold.common.io.RepositoryDescriptor;
import kobold.common.io.ScriptDescriptor;

import org.eclipse.core.internal.resources.ResourceInfo;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.team.internal.ccvs.core.connection.CVSAuthenticationException;

/**
 * @author Tammo
 */
public class VCMActionListener implements IVCMActionListener
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(VCMActionListener.class);
    
    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#refreshFiledescriptors(kobold.client.plam.model.IFileDescriptorContainer)
     */
    public void refreshFiledescriptors(IFileDescriptorContainer container)
    {
		StatusUpdater st = new StatusUpdater();
		st.updateFileDescriptors(container);
    }

    private void execHooks(Iterator iter, ScriptServerConnection connection,
        				   IProgressMonitor progress, String localPath, String sdVcmType)
        throws CVSAuthenticationException, IOException
    {
        while(iter.hasNext()) {
           ScriptDescriptor sd = (ScriptDescriptor) iter.next();
           if (sd.getVcmActionType().equals(sdVcmType)) {
               String sdCommand[] = new String[] { sd.getPath(), localPath };
               connection.open(progress, sdCommand);
               connection.close();
           }
        }
    }
        
    private void runHooks(AbstractAsset asset, ScriptServerConnection connection,
            			  IProgressMonitor progress, String sdVcmType,
            			  boolean preHook, boolean preOrder)
    	throws CVSAuthenticationException, IOException
    	
    {
        Iterator sit = preHook ? asset.getBeforeScripts().iterator()
                			   : asset.getAfterScripts().iterator();
        String localPath = KoboldRepositoryHelper.localPathForAsset(asset);
        if (preOrder) {
            execHooks(sit, connection, progress, localPath, sdVcmType);
        }
        for (Iterator it = asset.getChildren().iterator(); it.hasNext(); ) {
            runHooks((AbstractAsset) it.next(), connection, progress, sdVcmType,
                     preHook, preOrder);
        }
        if (!preOrder) { // postOrder ;)
            execHooks(sit, connection, progress, localPath, sdVcmType);
        }
    }
    
    private String[] createCommitCommand(AbstractRootAsset asset) {
        /**
	     * 	# $1 working directory
			# $2 repo type
			# $3 protocoal type
			# $4 username
			# $5 password
			# $6 host
			# $7 root
			# $8 module
			# $9 userdef
	     */
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
	    String localPath = asset.getRoot().getKoboldProject().getProject().getLocation().toOSString();
		String command[] = new String[10];
        command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.COMMIT).concat(KoboldRepositoryHelper.getScriptExtension());
	    command[1] = localPath;
	    command[2] = asset.getRepositoryDescriptor().getType();
	    command[3] = asset.getRepositoryDescriptor().getProtocol();
	    command[4] = userName;
	    command[5] = password; 
		command[6] = asset.getRepositoryDescriptor().getHost();
		command[7] = asset.getRepositoryDescriptor().getRoot();
		command[8] = asset.getRepositoryDescriptor().getPath();
		command[9] = "\"default commit\"";
		if (logger.isDebugEnabled()) {
			for (int j = 0; j < command.length; j++) {
				logger.debug("createCommitCommand(AbstractRootAsset)"
						+ command[j]);
			}
		}
		return command;
    }

    /**
     */
    public void addToVariant(Variant v, ProductComponent pc) {
        
        // First we create a temporary folder into which we want to check out the product 
        ResourceInfo test = null;
        IFolder tmpFolder = null;
        KoboldProject koboldProject = KoboldPLAMPlugin.getCurrentKoboldProject();
        IProject project = koboldProject.getProject();
        try
        {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            tmpFolder = project.getFolder("VCMtmp");
            if (!tmpFolder.exists())
            {
                tmpFolder.create(false, true, null);
            }
            else
            {
                tmpFolder.delete(false, null);
                tmpFolder.create(false, true, null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();// TODO: handle exception
        }
        IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection();
		if (connection != null) {
		    /**
             * # $1 working directory # $2 repo type # $3 protocoal type # $4
             * username # $5 password # $6 host # $7 root # $8 module # $9
             * userdef
             */
		    String localPath = KoboldRepositoryHelper.localPathForAsset(pc);
    		String command[] = new String[9];
    		RepositoryDescriptor rd = pc.getRoot().getRepositoryDescriptor();
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.UPDATE).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = tmpFolder.getLocation().toOSString();
		    command[2] = rd.getType();
		    command[3] = rd.getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = rd.getHost();
			command[7] = rd.getRoot();
			command[8] = pc.getRoot().getResource() + "/" + pc.getResource();
			try {
			    connection.open(KoboldPolicy.monitorFor(null),command);
			    connection.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
// XXX
            // TODO: implement addtovariant.sh script
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat("addtovariant.").concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = tmpFolder.getLocation().toOSString();
			command[2] = pc.getRoot().getResource() + "/" + pc.getResource();
			command[3] = KoboldRepositoryHelper.localPathForAsset(v);
			try {
			    connection.open(KoboldPolicy.monitorFor(null),command);
			    connection.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
		}
    }
    
    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#addToProduct(kobold.client.plam.model.product.Product)
     */
    public void addToProduct(kobold.client.plam.model.product.Product product, Release release) {

        // First we create a temporary folder into which we want to check out the product 
        ResourceInfo test = null;
        IFolder tmpFolder = null;
        KoboldProject koboldProject = KoboldPLAMPlugin.getCurrentKoboldProject();
        IProject project = koboldProject.getProject();
        try
        {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            tmpFolder = project.getFolder("VCMtmp");
            if (!tmpFolder.exists())
            {
                tmpFolder.create(false, true, null);
            }
            else
            {
                tmpFolder.delete(false, null);
                tmpFolder.create(false, true, null);
            }
        } catch (Exception e)
        {
            e.printStackTrace();// TODO: handle exception
        }
        IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection();
		if (connection != null) {
		    /**
             * # $1 working directory # $2 repo type # $3 protocoal type # $4
             * username # $5 password # $6 host # $7 root # $8 module # $9
             * userdef
             */
		    String localPath = KoboldRepositoryHelper.localPathForAsset(release);
    		String command[] = new String[10];
    		RepositoryDescriptor rd = release.getRoot().getRepositoryDescriptor();
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.UPDATE).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = tmpFolder.getLocation().toOSString();
		    command[2] = rd.getType();
		    command[3] = rd.getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = rd.getHost();
			command[7] = rd.getRoot();
			command[8] = release.getRoot().getResource() + "/" + release.getResource();
			try {
			    connection.open(KoboldPolicy.monitorFor(null),command);
			    connection.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
// XXX
            // TODO: implement addtoproduct.sh script
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat("addtoproduct.").concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = tmpFolder.getLocation().toOSString();
			command[2] = release.getRoot().getResource() + "/" + release.getResource();
			command[3] = KoboldRepositoryHelper.localPathForAsset(product);
			try {
			    connection.open(KoboldPolicy.monitorFor(null),command);
			    connection.close();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
		}   
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#commitProductline(kobold.client.plam.model.productline.Productline)
     */
    public void commitProductline(Productline pl) {
        
        IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection();
		if (connection != null) {
		    String command[] = createCommitCommand(pl);
			try {
			    // first we try to commit
			    runHooks(pl, connection, progress, ScriptDescriptor.VCM_COMMIT, true, true);
    			connection.open(progress, command);
    			connection.close();	
			    runHooks(pl, connection, progress, ScriptDescriptor.VCM_COMMIT, false, true);
			} catch (Exception e) {
				logger.error("commitProductline(Productline)", e);
			}
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#updateProduct(kobold.common.data.Product, org.eclipse.core.resources.IProject)
     */
    public void updateAsset(Asset asset, IProject p) {
        //IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection();
		if (connection != null) {
		    /**
		     * 	# $1 working directory
				# $2 repo type
				# $3 protocoal type
				# $4 username
				# $5 password
				# $6 host
				# $7 root
				# $8 module
				# $9 userdef
		     */
		    String localPath = p.getLocation().toOSString();
    		String command[] = new String[10];
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.UPDATE).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = localPath;
		    command[2] = asset.getRepositoryDescriptor().getType();
		    command[3] = asset.getRepositoryDescriptor().getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = asset.getRepositoryDescriptor().getHost();
			command[7] = asset.getRepositoryDescriptor().getRoot();
			command[8] = asset.getRepositoryDescriptor().getPath();
			command[9] = "";
			if (logger.isDebugEnabled()) {
				for (int j = 0; j < command.length; j++) {
					logger.debug("updateAsset(Asset, IProject)" + command[j]);
				}
			}
			try {
    			connection.open(new NullProgressMonitor(), command);
    			connection.close();	
			}
			catch (Exception e) {
				logger.error("updateAsset(Asset, IProject)", e);
			}
		}
		else {
			logger.error("updateAsset(Asset, IProject) cannot establish ScriptServerConnection");
		}
    }

    
    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#commitProduct(kobold.client.plam.model.product.Product)
     */
    public void commitProduct(kobold.client.plam.model.product.Product product) {
        IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection();
		if (connection != null) {
		    String command[] = createCommitCommand(product);
			try {
			    // first we try to commit
			    runHooks(product, connection, progress, ScriptDescriptor.VCM_COMMIT, true, true);
    			connection.open(progress, command);
    			connection.close();	
			    runHooks(product, connection, progress, ScriptDescriptor.VCM_COMMIT, false, true);
			}
			catch (Exception e) {
				logger.error( "commitProduct(kobold.client.plam.model.product.Product)", e);
			}
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#tagRelease(kobold.client.plam.model.Release)
     */
    public void tagRelease(Release release) {
        
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		RepositoryDescriptor rd = ModelStorage.getRepositoryDescriptorForAsset(release.getParent());
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection();
        IProgressMonitor progress = new SubProgressMonitor(KoboldPolicy.monitorFor(null), release.getFileRevisions().size());
        
		if (connection != null) {
		    /**
		     * 	# $1 working directory
				# $2 repo type
				# $3 protocoal type
				# $4 username
				# $5 password
				# $6 host
				# $7 root
				# $8 module
				# $9 userdef
		     */
		    String localPath = KoboldRepositoryHelper.localPathForAsset(release.getParent());
    		String command[] = new String[10];
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.TAG).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = localPath;
		    command[2] = rd.getType();
		    command[3] = rd.getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = rd.getHost();
			command[7] = rd.getRoot();
			command[9] = release.getName();
			for (Iterator it = release.getFileRevisions().iterator(); it.hasNext();) {
    			    
			    Release.FileRevision fr = (Release.FileRevision) it.next();
    			command[8] = localPath + IPath.SEPARATOR + fr.getPath();
    			for (int j = 0; j < command.length; j++) {
					if (logger.isDebugEnabled()) {
						logger.debug("tagRelease(Release)" + command[j]);
					}
					if (logger.isDebugEnabled()) {
						logger.debug("tagRelease(Release)");
					}
    			}
    			try {
        			connection.open(progress, command);
        			connection.close();	
    			}
    			catch (Exception e) {
					logger.error("tagRelease(Release)", e);
    			}
			}
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#updateRelease(kobold.client.plam.model.product.RelatedComponent, kobold.client.plam.model.Release)
     */
    public void updateRelease(RelatedComponent rc, Release newRelease) {
        final String cleanVCMCmd = "cleanvcmdata.pl";
        String cleanDir = rc.getLocalPath().toOSString();
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		RepositoryDescriptor rd = ModelStorage.getRepositoryDescriptorForAsset(newRelease.getParent());
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection();
        IProgressMonitor progress = new SubProgressMonitor(KoboldPolicy.monitorFor(null), newRelease.getFileRevisions().size());
        
		if (connection != null) {
    		String command[] = new String[10];
		    // First clean existing component directory from existing VCM data
		    command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(cleanVCMCmd);
		    command[1] = cleanDir;
			try {
				connection.open(progress, command);
				connection.close();	
			}
			catch (Exception e) {
				logger.error("tagRelease(Release)", e);
			}
			// Next checkout component to cleanDir
		    /**
		     * 	# $1 working directory
				# $2 repo type
				# $3 protocoal type
				# $4 username
				# $5 password
				# $6 host
				# $7 root
				# $8 module
				# $9 userdef
		     */
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.UPDATE).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = cleanDir;
		    command[2] = rd.getType();
		    command[3] = rd.getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = rd.getHost();
			command[7] = rd.getRoot();
			command[9] = newRelease.getName();
			try {
			    // first we try to commit
    			connection.open(progress, command);
    			connection.close();	
			}
			catch (Exception e) {
				logger.error("tagRelease(Release)", e);
			}
			// Clean again
		    // First clean existing component directory from existing VCM data
		    command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(cleanVCMCmd);
		    command[1] = cleanDir;
			try {
				connection.open(progress, command);
				connection.close();	
			}
			catch (Exception e) {
				logger.error("tagRelease(Release)", e);
			}
			// Finally import the subdirectory
			rd = rc.getRemoteRepository();
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.COMMIT).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = cleanDir;
		    command[2] = rd.getType();
		    command[3] = rd.getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = rd.getHost();
			command[7] = rd.getRoot();
			command[9] = newRelease.getName();
			try {
				connection.open(progress, command);
				connection.close();	
			}
			catch (Exception e) {
				logger.error("tagRelease(Release)", e);
			}
		}
    }
}
    