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

import java.io.File;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.dialog.PasswordDialog;
import kobold.client.vcm.preferences.VCMPreferencePage;
import kobold.common.io.RepositoryDescriptor;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author schneipk
 *
 * This is the actual implementation of the repository Access Interface for use with
 * the Kobold PLAM Tool
 */
public class KoboldRepositoryHelper {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(KoboldRepositoryHelper.class);

    public static final String IMPORT = "import.";
	public static final String ADD = "add.";
	public static final String UPDATE = "update.";
	public static final String COMMIT = "commit.";
	public static final String CHECKOUT = "checkout.";
	public static final String TAG = "tag.";
	public static final String REMOVE = "rm.";
	
	public static String localPathForAsset(AbstractAsset asset) {
	
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
	
	public static RepositoryDescriptor repositoryDescriptorForAsset(AbstractAsset asset)
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
	
    public static Path getScriptPath() {
        
    	KoboldVCMPlugin plugin = KoboldVCMPlugin.getDefault();
    	String tmpLocation = plugin.getBundle().getLocation();
    	// The Location String contains "@update/" this needs to be removed
       	//gets the scriptPath
    	String scriptPath = plugin.getPreferenceStore().getString(VCMPreferencePage.KOBOLD_VCM_SCRIPT_LOCATION);
    
    	Path result;
    	
    	if (scriptPath == null || scriptPath.equals("")) {
        	String tmpString = System.getProperty("os.name");
        	// This tests what OS is used and sets the skript extension accordingly
        	if (tmpString.indexOf("Win",0) != -1 ) 
        	{
        		result = new Path(tmpLocation.substring(8,tmpLocation.length()));
        		result = (Path)result.append("scripts" + IPath.SEPARATOR);
        	}
        	else
        	{
        		result = new Path(tmpLocation.substring(7,tmpLocation.length()));
        		result = (Path)result.append("scripts" + IPath.SEPARATOR);
        	}
    	}
    	else {
    	    result = new Path(scriptPath);
    	}
	    if (!result.hasTrailingSeparator())
        {
	        result = (Path)result.addTrailingSeparator();
        }
    	return result;
    }
    
    public static String getScriptExtension() {
     	KoboldVCMPlugin plugin = KoboldVCMPlugin.getDefault();
    	String tmpLocation = plugin.getBundle().getLocation();
    	// The Location String contains "@update/" this needs to be removed
	
    	String result;
    	String tmpString = System.getProperty("os.name");
    	// This tests what OS is used and sets the skript extension accordingly
    	if (tmpString.indexOf("Win",0) != -1 ) 
    	{
    		result = "bat";
    	}
    	else
    	{
    		result = "sh";
    	}
       
    	return result;
    }
    
    
    /**
     * Gets the userName
     * @return the username
     */
    public static String getUserName ()
    {
    	//gets the userName
    	String uN = KoboldVCMPlugin.getDefault().getPreferenceStore().getString(VCMPreferencePage.KOBOLD_VCM_USER_STR);
    
    	if (uN.equals("")) {
    		uN = getPreference(VCMPreferencePage.KOBOLD_VCM_USER_STR);
    		if (uN != null) {
    		    setUserName(uN);
    		}
    	}
    	return uN;
    }
    
    private static void setUserName (String userName)
    {
        KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_USER_STR, userName);
    }
    
    /**
     * gets the stored userName
     * @return the stored userName
     * 
     * if the user pressed cancel, the password will be ""
     */
    public static String getUserPassword ()
    {
    	//gets the userPassword
        Preferences prefs = KoboldVCMPlugin.getDefault().getPluginPreferences();
    	String uP = prefs.getString(VCMPreferencePage.KOBOLD_VCM_PWD_STR);
    	
    	if (prefs.getBoolean(VCMPreferencePage.KOBOLD_VCM_ASK_PWD) || prefs.getString(VCMPreferencePage.KOBOLD_VCM_PWD_STR).equals(""))
    	{
    		uP = getPreference(VCMPreferencePage.KOBOLD_VCM_PWD_STR);
    		if (uP != null) {
    			setUserPassword(uP);
    		}
    	}
    	return uP;
    }
    
    /**
     * Sets the new userName
     * @param userPassword the userPassword to store
     */
    static private void setUserPassword (String userPassword)
    {
    	KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_PWD_STR, userPassword);
    }
    
    /**
     * Opens a input Dialog to enter the user-data
     * @param type the variableName to get of the user
     * @return the input-value of the dialog
     */
    static private String getPreference (String type) {
        String preferenceName = type;
        if (type.equals(VCMPreferencePage.KOBOLD_VCM_PWD_STR))
        {
            type = "VCM User Password"; 
            PasswordDialog pd = new PasswordDialog(new Shell());
            //open the dialog
            if (pd.open() == Dialog.OK) {
                return pd.getPassword();
            }
            else {
    		    // CANCEL
    		    return null;
    		} 
        } 
        else if (type.equals(VCMPreferencePage.KOBOLD_VCM_USER_STR))
        {
            type = "VCM User Name";
    		InputDialog in = new InputDialog (new Shell(), "Please enter the " + type, "Please enter the " + type + ":", null, null);
    		//open the dialog
    		if (in.open() == Dialog.OK) {
    			return in.getValue();
    		}
    		else {
    		    // CANCEL
    		    return null;
    		}
        }
    	return "";
    }

    private static void deleteTree( File path )
    {
        File files[] = path.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) deleteTree(files[i]);
                files[i].delete();
            }
        }
        path.delete();
    }
    
    public static void deleteTree(String path) {
       if (path != null && !path.equals("")) {
           deleteTree(new File(path));
       }
    }
}
