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



import java.io.IOException;
import java.util.Iterator;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.communication.ScriptServerConnection;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.client.vcm.dialog.PasswordDialog;
import kobold.client.vcm.preferences.VCMPreferencePage;
import kobold.common.io.RepositoryDescriptor;
import kobold.common.io.ScriptDescriptor;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ccvs.core.connection.CVSAuthenticationException;

/**
 * @author schneipk
 *
 * This is the actual implementation of the repository Access Interface for use with
 * the Kobold PLAM Tool
 */
public class KoboldRepositoryAccessOperations implements KoboldRepositoryOperations {

    private final String IMPORT = "import.";
	private final String ADD = "add.";
	private final String UPDATE = "update.";
	private final String COMMIT = "commit.";
	private final String CHECKOUT = "checkout.";
	private final String TAG = "tag.";
	private final String REMOVE = "rm.";
	
    private Path scriptPath; 
    private String scriptExtension = "";
    private String userName;
    private String password;
	
	public KoboldRepositoryAccessOperations()
	{
		KoboldVCMPlugin plugin = KoboldVCMPlugin.getDefault();
		String tmpLocation = plugin.getBundle().getLocation();
		// The Location String contains "@update/" this needs to be removed
		
		String tmpString = System.getProperty("os.name");
		// This tests what OS is used and sets the skript extension accordingly
		if (tmpString.indexOf("Win",0) != -1 ) 
		{
			this.scriptPath = new Path(tmpLocation.substring(8,tmpLocation.length()));
			this.scriptPath = (Path)scriptPath.append("scripts" + IPath.SEPARATOR);
			this.scriptExtension = "bat";
		}
		else
		{
			this.scriptPath = new Path(tmpLocation.substring(7,tmpLocation.length()));
			this.scriptPath = (Path)scriptPath.append("scripts" + IPath.SEPARATOR);
			this.scriptExtension = "sh";
		}
		userName = getUserName();
		password = getUserPassword();
	}
	
    // XXXX

    /**
     * Gets the userName
     * @return the username
     */
    private String getUserName ()
    {
    	//gets the userName
    	String uN = KoboldVCMPlugin.getDefault().getPreferenceStore().getString(VCMPreferencePage.KOBOLD_VCM_USER_STR);
    
    	if (uN.equals(""))
    	{
    		uN = getPreference (VCMPreferencePage.KOBOLD_VCM_USER_STR);
    		if (uN != null) {
    			setUserName(uN);
    		}
    	}
    	return uN;
    }
    /**
     * Sets the userName to the preferences
     * @param userName, the userName to store
     */
    protected void setUserName (String userName)
    {
    	//set the default userName (initial)
        if (userName != null)
        {
            KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_USER_STR, userName);
        }
        else
        {
            MessageDialog.openError(new Shell(),"VCM User not set","VCM User not set, please reconfigure!");
        }
    }
    /**
     * gets the stored userName
     * @return the stored userName
     * 
     * if the user pressed cancel, the password will be ""
     */
    private String getUserPassword ()
    {
    	//gets the userPassword
        Preferences prefs = KoboldVCMPlugin.getDefault().getPluginPreferences();
    	String uP = prefs.getString(VCMPreferencePage.KOBOLD_VCM_PWD_STR);
    	
    	if (prefs.getBoolean(VCMPreferencePage.KOBOLD_VCM_ASK_PWD) || prefs.getString(VCMPreferencePage.KOBOLD_VCM_PWD_STR).equals(""))
    	{
    		uP = getPreference (VCMPreferencePage.KOBOLD_VCM_PWD_STR);
    		if (uP != null) {
    			setUserPassword(uP);
    		}
    	}
    	return uP;
    }
    
    /**
     * Sets the new userName
     * @param userPassword, the userPassword to store
     */
    private void setUserPassword (String userPassword)
    {
    	KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_PWD_STR,userPassword);
    }
    
    /**
     * Opens a input Dialog to enter the user-data
     * @param type, the variableName to get of the user
     * @return the input-value of the dialog
     */
    private String getPreference (String type)
    {
        String preferenceName = "";
        if (type.equals(VCMPreferencePage.KOBOLD_VCM_PWD_STR))
        {
            preferenceName = type;
            type = "VCM User Password"; 
            PasswordDialog pd = new PasswordDialog (new Shell());
            //open the dialog
            if (pd.open() == Dialog.OK) {
                return KoboldVCMPlugin.getDefault().getPreferenceStore().getString(VCMPreferencePage.KOBOLD_VCM_PWD_STR);
            }
            else {
    		    // CANCEL
    		    return null;
    		} 
        } 
        else if (type.equals(VCMPreferencePage.KOBOLD_VCM_USER_STR))
        {
            preferenceName = type;
            type = "VCM User Name";
    		InputDialog in = new InputDialog (new Shell(), "Please enter the " + type, "Please enter the " + type +":", null, null);
    		//open the dialog
    		if (in.open() == Dialog.OK) {
    			return KoboldVCMPlugin.getDefault().getPreferenceStore().getString(VCMPreferencePage.KOBOLD_VCM_USER_STR);
    		}
    		else {
    		    // CANCEL
    		    return null;
    		}
        }
    	return "";
    }
	
	private String localPathForAsset(AbstractAsset asset) {
	
		if(asset == null) {
		    return null;
		}
				
		if (asset instanceof Productline) {
		    return ((Productline)asset).getLocalPath().toOSString();
		}
		else if (asset instanceof Product) {
		    return ((Product)asset).getLocalPath().toOSString();
		}
		else if (asset instanceof Variant) {
		    return ((Variant)asset).getLocalPath().toOSString();
		}
		else if (asset instanceof Component) {
		    return ((Component)asset).getLocalPath().toOSString();
		}	
		
		return null;
	}
	
	private RepositoryDescriptor repositoryDescriptorForAsset(AbstractAsset asset)
	{
		if(asset == null) {
		    return null;
		}
				
		if (asset instanceof Productline) {
		    return ((Productline)asset).getRepositoryDescriptor();
		}
		else if (asset instanceof Product) {
		    return ((Product)asset).getRepositoryDescriptor();
		}
		else if (asset instanceof Variant) {
		    return ((Variant)asset).getRemoteRepository();
		}
		else if (asset instanceof Component) {
		    return ((Component)asset).getRemoteRepository();
		}	
		
		return null;
	}
	
	private void performVCMAction(AbstractAsset[] assets, IProgressMonitor progress,
	        					  String vcmScriptPath, String lastParam,
	        					  String sdVcmType)
		throws CVSAuthenticationException, IOException
	        	
	{
		progress.beginTask("working", 2);
	    for (int i = 0; i < assets.length; i++) {
	        RepositoryDescriptor rd = repositoryDescriptorForAsset(assets[i]);
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
			    String localPath = localPathForAsset(assets[i]);
   		        
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
					System.out.print(command[j]);
					System.out.print(" ");
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
			performVCMAction(assets, progress, scriptPath.toOSString().concat(ADD).concat(scriptExtension),
			                 null, ScriptDescriptor.VCM_ADD);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#commit(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void commit(AbstractAsset[] assets, IProgressMonitor progress,
            		   String msg) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			performVCMAction(assets, progress, scriptPath.toOSString().concat(COMMIT).concat(scriptExtension),
			                 msg, ScriptDescriptor.VCM_COMMIT);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#checkout(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor, java.lang.String, boolean)
     */
    public void checkout(AbstractAsset[] assets, IProgressMonitor progress, String tag, boolean isPl) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			if (isPl) {
    			performVCMAction(assets, progress, scriptPath.toOSString().concat(CHECKOUT).concat("pl").concat(scriptExtension),
    			                 tag, ScriptDescriptor.VCM_CHECKOUT);
			}
			else {
	  			performVCMAction(assets, progress, scriptPath.toOSString().concat(CHECKOUT).concat(scriptExtension),
    			                 tag, ScriptDescriptor.VCM_CHECKOUT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#importing(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor, boolean)
     */
    public void importing(AbstractAsset[] assets, IProgressMonitor progress,
            			  String msg, boolean isPl) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			if (isPl) {
    			performVCMAction(assets, progress, scriptPath.toOSString().concat(IMPORT).concat("pl").concat(scriptExtension),
    			                 msg, ScriptDescriptor.VCM_IMPORT);
			}
			else {
	  			performVCMAction(assets, progress, scriptPath.toOSString().concat(IMPORT).concat(scriptExtension),
    			                 msg, ScriptDescriptor.VCM_IMPORT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#update(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor, java.lang.String)
     */
    public void update(AbstractAsset[] assets, IProgressMonitor progress, String tag) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			performVCMAction(assets, progress, scriptPath.toOSString().concat(UPDATE).concat(scriptExtension),
			                 tag, ScriptDescriptor.VCM_UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#remove(kobold.client.plam.model.AbstractAsset[], org.eclipse.core.runtime.IProgressMonitor)
     */
    public void remove(AbstractAsset[] assets, IProgressMonitor progress) throws TeamException {
        try {
			progress = KoboldPolicy.monitorFor(progress);
			performVCMAction(assets, progress, scriptPath.toOSString().concat(REMOVE).concat(scriptExtension),
			                 null, ScriptDescriptor.VCM_DELETE);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * @see kobold.client.vcm.controller.KoboldRepositoryOperations#tag(kobold.client.plam.model.Release, org.eclipse.core.runtime.IProgressMonitor, java.lang.String)
     */
    public void tag(Release release, IProgressMonitor progress, String tag) throws TeamException {
        // TODO Auto-generated method stub
        
    }
	   
    // XXXXX
}
