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
 * $Id: VCMActionListener.java,v 1.12 2004/10/18 00:12:59 garbeam Exp $
 *
 */
package kobold.client.vcm;

import java.util.Iterator;

import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.client.vcm.communication.ScriptServerConnection;
import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;
import kobold.client.vcm.controller.KoboldRepositoryHelper;
import kobold.client.vcm.controller.StatusUpdater;
import kobold.common.data.Product;
import kobold.common.io.RepositoryDescriptor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

/**
 * @author Tammo
 */
public class VCMActionListener implements IVCMActionListener
{
    
    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#refreshFiledescriptors(kobold.client.plam.model.IFileDescriptorContainer)
     */
    public void refreshFiledescriptors(IFileDescriptorContainer container)
    {
		StatusUpdater st = new StatusUpdater();
		st.updateFileDescriptors(container);
		//System.out.println("TEST STATUS");
    }

    /* (non-Javadoc)
     * @see kobold.client.plam.listeners.IVCMActionListener#checkoutProductline(kobold.client.plam.model.AbstractRootAsset)
     */
    public void updateProductline(AbstractRootAsset rootAsset)
    {
		KoboldRepositoryAccessOperations repoAccess = new KoboldRepositoryAccessOperations();
		try
		{
			AbstractAsset tmpAsset[] = {rootAsset};
			repoAccess.checkout(tmpAsset, null, null ,true);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#updateProductline(kobold.common.data.Productline, org.eclipse.core.resources.IProject)
     */
    public void updateProductline(kobold.common.data.Productline spl, IProject p) {
        
        IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection(spl.getRepositoryDescriptor().getRoot());
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
		    String localPath = p.getLocation().toOSString() + IPath.SEPARATOR + spl.getResource();
    		String command[] = new String[10];
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.UPDATE).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = localPath;
		    command[2] = spl.getRepositoryDescriptor().getType();
		    command[3] = spl.getRepositoryDescriptor().getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = spl.getRepositoryDescriptor().getHost();
			command[7] = spl.getRepositoryDescriptor().getRoot();
			command[8] = spl.getRepositoryDescriptor().getPath();
			command[9] = "";
			for (int j = 0; j < command.length; j++) {
				System.out.print(command[j]);
				System.out.print(" ");
			}
			try {
			    // first we try update
    			connection.open(progress, command);
    			connection.close();	
    			
    			if (connection.getReturnValue() != 0) {
    			    // update failed, let's try to checkout
                    command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.CHECKOUT).concat(KoboldRepositoryHelper.getScriptExtension());
        			connection.open(progress, command);
        			connection.close();	
        			
        			// if that fails, don't care
    			}
			}
			catch (Exception e) {
			    e.printStackTrace();
			}
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#commitProductline(kobold.client.plam.model.productline.Productline)
     */
    public void commitProductline(Productline pl) {
        
        IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection(pl.getRepositoryDescriptor().getRoot());
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
		    String localPath = KoboldRepositoryHelper.localPathForAsset(pl);
    		String command[] = new String[10];
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.COMMIT).concat(KoboldRepositoryHelper.getScriptExtension());
		    command[1] = localPath;
		    command[2] = pl.getRepositoryDescriptor().getType();
		    command[3] = pl.getRepositoryDescriptor().getProtocol();
		    command[4] = userName;
		    command[5] = password; 
			command[6] = pl.getRepositoryDescriptor().getHost();
			command[7] = pl.getRepositoryDescriptor().getRoot();
			command[8] = pl.getRepositoryDescriptor().getPath();
			command[9] = "\"default commit\"";
			for (int j = 0; j < command.length; j++) {
				System.out.print(command[j]);
				System.out.print(" ");
			}
			try {
			    // first we try to commit
    			connection.open(progress, command);
    			connection.close();	
    			
    			if (connection.getReturnValue() != 0) {
    			    // next we initially import
                    command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.IMPORT).concat(KoboldRepositoryHelper.getScriptExtension());
        			command[9] = "\"initial pl import\"";
                    connection.open(progress, command);
        			connection.close();	
    			    
        			if (connection.getReturnValue() == 0) {
        			// VERY DANGEROUS :)
//        			    KoboldRepositoryHelper.deleteTree(localPath);
        			    
        			    updateProductline(new kobold.common.data.Productline(pl.getName(), pl.getResource(), pl.getRepositoryDescriptor()),
        			            		  pl.getKoboldProject().getProject());
        			}
    			}
			}
			catch (Exception e) {
			    e.printStackTrace();
			}
		}
        
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#updateProduct(kobold.common.data.Product, org.eclipse.core.resources.IProject)
     */
    public void updateProduct(Product prod, IProject p) {
        // TODO Auto-generated method stub
      
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#commitProduct(kobold.client.plam.model.product.Product)
     */
    public void commitProduct(kobold.client.plam.model.product.Product product) {
        // TODO Auto-generated method stub
        
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#tagRelease(kobold.client.plam.model.Release)
     */
    public void tagRelease(Release release) {
        
		String userName = KoboldRepositoryHelper.getUserName();
		String password = KoboldRepositoryHelper.getUserPassword();
		RepositoryDescriptor rd = ModelStorage.getRepositoryDescriptorForAsset(release.getParent());
		ScriptServerConnection connection =
		    ScriptServerConnection.getConnection(rd.getRoot());
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
    				System.out.print(command[j]);
    				System.out.print(" ");
    			}
    			try {
    			    // first we try to commit
        			connection.open(progress, command);
        			connection.close();	
        			
    //    			if (connection.getReturnValue() != 0) {
    //				}
    			}
    			catch (Exception e) {
    			    e.printStackTrace();
    			}
			}
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#updateRelease(kobold.client.plam.model.product.RelatedComponent, kobold.client.plam.model.Release)
     */
    public void updateRelease(RelatedComponent rc, Release newRelase) {
        // TODO Auto-generated method stub
        
    }
}
