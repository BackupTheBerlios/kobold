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
 * $Id: KoboldPLAMPlugin.java,v 1.4 2004/05/15 15:03:29 vanto Exp $
 *
 */
package kobold.client.plam;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import kobold.client.plam.controller.SecureKoboldClient;
import kobold.client.plam.listeners.IProjectChangeListener;
import kobold.client.plam.workflow.LocalMessageQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The main plugin class to be used in the desktop.
 */
public class KoboldPLAMPlugin extends AbstractUIPlugin {
	
	private static final Log logger = LogFactory.getLog(KoboldPLAMPlugin.class);
	
	//The shared instance.
	private static KoboldPLAMPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	// current kobold-natured project
	private IProject currentProject;
	
	protected final HashSet projectChangeListeners = new HashSet(); 
	
	/**
	 * The constructor.
	 */
	public KoboldPLAMPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		currentProject = null;
		
		try {
			resourceBundle= ResourceBundle.getBundle("kobold.client.plam.KoboldPLAMPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static KoboldPLAMPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle= KoboldPLAMPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	
    public static ImageDescriptor getImageDescriptor(String name)
    {
        try
        {
            URL installURL = getDefault().getDescriptor().getInstallURL();
            URL url = new URL(installURL, name);
            return ImageDescriptor.createFromURL(url);
        }
        catch(MalformedURLException _ex)
        {
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }
    
    /**
     * Returns the current selected kobold-natured project or null if none is seleted.
     * 
     * @return
     */
    public static IProject getCurrentProject()
    {
    	return getDefault().currentProject; 	
    }
    
	/**
	 * Sets the current select kobold project. It must be open and have the kobold 
	 * project nature.
	 * Set it to null to deselect.
	 * 
	 * @param p
	 * @throws CoreException if p has wrong nature or is not accessible
	 */
    public void setCurrentProject(IProject p) throws CoreException
    {
		IProject old = currentProject;
    	if (p == null) {
    		// deselect
    		currentProject = null;
    	} else {
			boolean hasKoboldNature = p.hasNature(KoboldProjectNature.NATURE_ID);
			if (hasKoboldNature) {
				currentProject = p;	
			} else {
    			throw new ResourceException(IResourceStatus.RESOURCE_WRONG_TYPE, p.getFullPath(), "Project has wrong nature", null);		
			}
	   	}
		fireProjectChange(old, currentProject);
    }

	protected void fireProjectChange(IProject oldProject, IProject newProject) 
	{
		logger.info("Active project has changed from " + oldProject + " to " + newProject);
		for (Iterator it = projectChangeListeners.iterator(); it.hasNext();) {
			IProjectChangeListener listener = (IProjectChangeListener) it.next();
			listener.projectChanged(oldProject, newProject);
		}
	}
	
	/**
	 * Adds a listener for project change events.
	 */
	public void addProjectChangeListener(IProjectChangeListener listener) 
	{
		projectChangeListeners.add(listener);
	}

	/**
	 * Removes a listener for project change events.
	 */
	public void removeProjectChangeListener(IProjectChangeListener listener) 
	{
		projectChangeListeners.remove(listener);
	}

	public static KoboldProjectNature getCurrentProjectNature() 
	{
		if (getCurrentProject() == null)
			return null;
		
		try {
			return (KoboldProjectNature)getCurrentProject().getNature(KoboldProjectNature.NATURE_ID);
		} catch (CoreException e) {
			return null;
		}
	}
	
	public static SecureKoboldClient getCurrentClient() 
	{
		return (getCurrentProjectNature() == null)?null 
			: getCurrentProjectNature().getClient(); 
	}
	
	public static LocalMessageQueue getCurrentMessageQueue() 
	{
		return (getCurrentProjectNature() == null)?null 
			: getCurrentProjectNature().getMessageQueue(); 
	}

}
