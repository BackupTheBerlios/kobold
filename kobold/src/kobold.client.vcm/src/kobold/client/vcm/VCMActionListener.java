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
 * $Id: VCMActionListener.java,v 1.6 2004/09/22 16:08:01 garbeam Exp $
 *
 */
package kobold.client.vcm;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.client.vcm.communication.ScriptServerConnection;
import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;
import kobold.client.vcm.controller.KoboldRepositoryHelper;
import kobold.client.vcm.controller.StatusUpdater;
import kobold.common.data.Productline;

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
    public void updateProductline(Productline spl, IProject p) {
        
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
            command[0] = KoboldRepositoryHelper.getScriptPath().toOSString().concat(KoboldRepositoryHelper.ADD).concat(KoboldRepositoryHelper.getScriptExtension());
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
    			connection.open(progress, command);
    			connection.close();	
			}
			catch (Exception e) {
			    e.printStackTrace();
			}
		}
    }

    /**
     * @see kobold.client.plam.listeners.IVCMActionListener#commitProductline(kobold.client.plam.model.productline.Productline)
     */
    public void commitProductline(kobold.client.plam.model.productline.Productline pl) {
        
        
    }

}
