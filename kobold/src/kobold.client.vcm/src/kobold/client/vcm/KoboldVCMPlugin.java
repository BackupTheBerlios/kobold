package kobold.client.vcm;

import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;
import java.util.*;

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
	public KoboldVCMPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		try {
			resourceBundle= ResourceBundle.getBundle("kobold.client.vcm.KoboldVCMPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
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
