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
 * 
 */

package kobold.client.vcm;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.vcm.controller.StatusUpdater;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class KoboldVCMPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static KoboldVCMPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	
	//The Plugin ID as set in the plugin.xml
	public static final String ID = "kobold.client.vcm";
	//The Provider ID as set in the plugin.xml
	public static final String PROVIDER_ID = "kobold.client.vcm.controller.WrapperProvider";
	
	/**
	 * The constructor.
	 */
	public KoboldVCMPlugin() { // Removed the deprecated constructor using - IPluginDescriptor descriptor
		super();
		plugin = this;
		try {
			resourceBundle= ResourceBundle.getBundle("kobold.client.vcm.KoboldVCMPluginResources");

		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Plugin plugin = KoboldPLAMPlugin.getDefault();
		IProject projects[] = KoboldPLAMPlugin.getDefault().getWorkspace().getRoot().getProjects();
		if (projects != null) {
			for (int i = 0; i < projects.length; i++) {
				IProjectNature nature = projects[i].getNature(KoboldProject.NATURE_ID);
				if (nature != null) {
//					()
//					((KoboldProject)nature.getProject()).addVCMActionListener(new IVCMActionListener() {
//						/* (non-Javadoc)
//						 * @see kobold.client.plam.listeners.IVCMActionListener#refreshFiledescriptors(kobold.client.plam.model.IFileDescriptorContainer)
//						 */
//						public void refreshFiledescriptors(
//								IFileDescriptorContainer container) {
//							StatusUpdater st = new StatusUpdater();
//							st.updateFileDescriptors(container);
//							System.out.println("TEST STATUS");
//						}
//					});
				}
			}
			
		} else {

		} 

		
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static KoboldVCMPlugin getDefault() {
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
		ResourceBundle bundle= KoboldVCMPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
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
	/**
	 * @return Returns the iD.
	 */
	public static String getID() {
		return ID;
	}
	/**
	 * @return Returns the PROVIDER_ID.
	 */
	public static String getPROVIDER_ID() {
		return PROVIDER_ID;
	}
}
