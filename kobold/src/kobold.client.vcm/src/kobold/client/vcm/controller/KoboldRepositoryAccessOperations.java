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

import org.apache.log4j.Logger;



import java.io.IOException;
import java.util.Iterator;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.Release;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.client.vcm.communication.ScriptServerConnection;
import kobold.common.io.RepositoryDescriptor;
import kobold.common.io.ScriptDescriptor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.connection.CVSAuthenticationException;

/**
 * @author schneipk
 *
 * This is the actual implementation of the repository Access Interface for use with
 * the Kobold PLAM Tool
 */
public class KoboldRepositoryAccessOperations implements KoboldRepositoryOperations {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(KoboldRepositoryAccessOperations.class);

    private Path scriptPath; 
    private String scriptExtension = "";
    private String userName;
    private String password;
	
	public KoboldRepositoryAccessOperations()
	{
	    scriptPath = KoboldRepositoryHelper.getScriptPath();
	    scriptExtension = KoboldRepositoryHelper.getScriptExtension();
		userName = KoboldRepositoryHelper.getUserName();
		password = KoboldRepositoryHelper.getUserPassword();
	}
	
    // XXXX

	private void performVCMAction(AbstractAsset[] assets, IProgressMonitor progress,
	        					  String vcmScriptPath, String lastParam,
	        					  String sdVcmType)
		throws CVSAuthenticationException, IOException
	        	
	{
		progress.beginTask("working", 2);
	    for (int i = 0; i < assets.length; i++) {
	        RepositoryDescriptor rd = KoboldRepositoryHelper.repositoryDescriptorForAsset(assets[i]);
        	ScriptServerConnection connection = ScriptServerConnection.getConnection(rd.getRoot());
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
			    String localPath = KoboldRepositoryHelper.localPathForAsset(assets[i]);
   		        
   		        // pre scripts hook
   		        for (Iterator it = assets[i].getBeforeScripts().iterator(); it.hasNext();) {
   		            ScriptDescriptor sd = (ScriptDescriptor) it.next();
   		            if (sd.getVcmActionType().equals(sdVcmType)) {
   		                String sdCommand[] = new String[] { sd.getPath(), localPath };
                        connection.open(progress, sdCommand);
                        connection.close();
   		            }
   		        }
   		        //
   		        
        		String command[] = new String[10];
                command[0] = vcmScriptPath;
			    command[1] = localPath;
			    command[2] = rd.getType();
			    command[3] = rd.getProtocol();
			    command[4] = userName;
			    command[5] = password; 
				command[6] = rd.getHost();
				command[7] = rd.getRoot();
				command[8] = rd.getPath();
				command[9] = lastParam != null ? lastParam : "";
				for (int j = 0; j < command.length; j++) {
					if (logger.isDebugEnabled()) {
						logger
								.debug("performVCMAction(AbstractAsset[], IProgressMonitor, String, String, String)"
										+ command[j]);
					}
					if (logger.isDebugEnabled()) {
						logger
								.debug("performVCMAction(AbstractAsset[], IProgressMonitor, String, String, String)");
					}
				}
    			connection.open(progress, command);
    			connection.close();	
    			
                // post scripts hook
   		        for (Iterator it = assets[i].getAfterScripts().iterator(); it.hasNext();) {
   		            ScriptDescriptor sd = (ScriptDescriptor) it.next();
   		            if (sd.getVcmActionType().equals(sdVcmType)) {
   		                String sdCommand[] = new String[] { sd.getPath(), localPath };
                        connection.open(progress, sdCommand);
                        connection.close();
   		            }
   		        }
   		        //
   		            			
		    }
			progress.done();
		}
	}

	
	/**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#add(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void add(AbstractAsset[] assets, IProgressMonitor progress) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			performVCMAction(assets, progress, scriptPath.toOSString().concat(KoboldRepositoryHelper.ADD).concat(scriptExtension),
			                 null, ScriptDescriptor.VCM_ADD);
		} catch (Exception e) {
			logger.error("add(AbstractAsset[], IProgressMonitor)", e);
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#commit(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor, java.lang.String)
     */
    public void commit(AbstractAsset[] assets, IProgressMonitor progress,
            		   String msg) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			performVCMAction(assets, progress, scriptPath.toOSString().concat(KoboldRepositoryHelper.COMMIT).concat(scriptExtension),
			                 msg, ScriptDescriptor.VCM_COMMIT);
		} catch (Exception e) {
			logger
					.error("commit(AbstractAsset[], IProgressMonitor, String)",
							e);
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#checkout(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor, java.lang.String, boolean)
     */
    public void checkout(AbstractAsset[] assets, IProgressMonitor progress, String tag, boolean isPl) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
	  			performVCMAction(assets, progress, scriptPath.toOSString().concat(KoboldRepositoryHelper.CHECKOUT).concat(scriptExtension),
    			                 tag, "checkout");
		} catch (Exception e) {
			logger
					.error(
							"checkout(AbstractAsset[], IProgressMonitor, String, boolean)",
							e);
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#importing(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor, java.lang.String, boolean)
     */
    public void importing(AbstractAsset[] assets, IProgressMonitor progress,
            			  String msg, boolean isPl) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
	  			performVCMAction(assets, progress, scriptPath.toOSString().concat(KoboldRepositoryHelper.IMPORT).concat(scriptExtension),
    			                 msg, ScriptDescriptor.VCM_IMPORT);
		} catch (Exception e) {
			logger
					.error(
							"importing(AbstractAsset[], IProgressMonitor, String, boolean)",
							e);
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#update(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor, java.lang.String)
     */
    public void update(AbstractAsset[] assets, IProgressMonitor progress, String tag) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			performVCMAction(assets, progress, scriptPath.toOSString().concat(KoboldRepositoryHelper.UPDATE).concat(scriptExtension),
			                 tag, "update");
		} catch (Exception e) {
			logger
					.error("update(AbstractAsset[], IProgressMonitor, String)",
							e);
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#remove(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void remove(AbstractAsset[] assets, IProgressMonitor progress) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			performVCMAction(assets, progress, scriptPath.toOSString().concat(KoboldRepositoryHelper.REMOVE).concat(scriptExtension),
			                 null, ScriptDescriptor.VCM_REMOVE);
		} catch (Exception e) {
			logger.error("remove(AbstractAsset[], IProgressMonitor)", e);
		}
    }
}
