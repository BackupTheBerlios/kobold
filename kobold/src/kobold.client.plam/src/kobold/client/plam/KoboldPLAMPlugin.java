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
 * $Id: KoboldPLAMPlugin.java,v 1.28 2004/11/22 21:51:54 garbeam Exp $
 *
 */
package kobold.client.plam;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

import kobold.client.plam.controller.SSLHelper;
import kobold.client.plam.listeners.IProjectChangeListener;
import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.IVariantContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.productline.Variant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class KoboldPLAMPlugin extends AbstractUIPlugin 
							  implements IResourceChangeListener {
	
	private static final Log logger = LogFactory.getLog(KoboldPLAMPlugin.class);
	public static final String ID = "kobold.client.plam";
	
	//The shared instance.
	private static KoboldPLAMPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	// current kobold-natured project
	private IProject currentProject;
	private IVCMActionListener vcmListener;
	
	protected final HashSet projectChangeListeners = new HashSet(5); 
	
	/**
	 * The constructor.
	 */
	public KoboldPLAMPlugin() {
		super();
		plugin = this;
		currentProject = null;
		
		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("[%p] %C - %F - %m%n")));
		
		try {
			resourceBundle= ResourceBundle.getBundle("kobold.client.plam.KoboldPLAMPluginResources");
		} catch (Exception x) {
		    resourceBundle = null;
		}
	}

	/**
	 * Returns the registered VCM-Plugin or null if none has been registered.
	 */
	public IVCMActionListener getVCMListener()
	{
	    return vcmListener;
	}
	
	public void setVCMListener(IVCMActionListener l)
	{
	    this.vcmListener = l;
	}
	
	/**
	 * Convenience method for logging statuses to the plugin log
	 * 
	 * @param status  the status to log
	 */
	public static void log(IStatus status) {
		getDefault().getLog().log(status);
	}
	
	/**
	 * Convenience method for logging a TeamException in such a way that the
	 * stacktrace is logged as well.
	 * @param e
	 */
	public static void log(CoreException e) {
		IStatus status = e.getStatus();
		log (status.getSeverity(), status.getMessage(), e);
	}
	
	/**
	 * Log the given exception along with the provided message and severity indicator
	 */
	public static void log(int severity, String message, Throwable e) {
		log(new Status(severity, ID, 0, message, e));
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
            URL installURL = getDefault().getBundle().getEntry("/");//getDefault().getDescriptor().getInstallURL();
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
			boolean hasKoboldNature = p.hasNature(KoboldProject.NATURE_ID);
			if (hasKoboldNature) {
				currentProject = p;
				// (de)activate msgqueues and update markers
				if (old != null) {
					KoboldProject kpn = ((KoboldProject)old.getNature(KoboldProject.NATURE_ID));
				    kpn.getMessageQueue().deactivate();
					kpn.store();
				}
				
				KoboldProject kpn = ((KoboldProject)p.getNature(KoboldProject.NATURE_ID));
				kpn.load();
				kpn.getMessageQueue().activate();
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

	public static KoboldProject getCurrentKoboldProject() 
	{
		if (getCurrentProject() == null)
			return null;
		
		try {
			return (KoboldProject)getCurrentProject().getNature(KoboldProject.NATURE_ID);
		} catch (CoreException e) {
			return null;
		}
	}
	
//	public static LocalMessageQueue getCurrentMessageQueue() 
//	{
//		return (getCurrentProjectNature() == null)?null 
//			: getCurrentProjectNature().getMessageQueue(); 
//	}
	
    public void start(BundleContext ctx) throws Exception
    {
        super.start(ctx);
        getWorkspace().addResourceChangeListener(this);
        initSSLProperties();
    }
    
    /* (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        getWorkspace().removeResourceChangeListener(this);
        super.stop(context);
    }

    public void initSSLProperties() {
		// set properties
		try {
		    Properties props = new Properties(System.getProperties());
		    props.setProperty(SSLHelper.PROTOCOL_HANDLER, getPluginPreferences().getString(SSLHelper.PROTOCOL_HANDLER));
		    props.setProperty(SSLHelper.JAVA_DEBUG, getPluginPreferences().getString(SSLHelper.JAVA_DEBUG));
		    props.setProperty(SSLHelper.KEY_STORE, getPluginPreferences().getString(SSLHelper.KEY_STORE));
		    props.setProperty(SSLHelper.KEY_STORE_PASSWORD, getPluginPreferences().getString(SSLHelper.KEY_STORE_PASSWORD));
		    props.setProperty(SSLHelper.TRUST_STORE, getPluginPreferences().getString(SSLHelper.TRUST_STORE));
		    props.setProperty(SSLHelper.TRUST_STORE_PASSWORD, getPluginPreferences().getString(SSLHelper.TRUST_STORE_PASSWORD));
		    System.setProperties(props);
        } catch (Exception e) {
			logger.error("Could not find client configuration",e);
        }
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
     */
    public void resourceChanged(final IResourceChangeEvent event)
    {
        logger.debug("resource changed: "+event.getType()+";"+event.getDelta());
        final List assets = getAssetsByDelta(event.getDelta());
        if (assets == null) {
            logger.debug("no corresponding asset found");
            return;
        }
        Display.getDefault().asyncExec(new Runnable() {
        public void run()
        {

        Iterator it = assets.iterator();
        while (it.hasNext()) {
            AbstractAsset asset = (AbstractAsset)it.next();
            if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                // update vcm
                if (asset != null && asset instanceof IFileDescriptorContainer) {
                            getVCMListener().refreshFiledescriptors((IFileDescriptorContainer)asset);
                }
            } else if (event.getType() == IResourceChangeEvent.PRE_DELETE) {
                // delete asset
                if (asset != null && asset instanceof Variant) {
                    ((IVariantContainer)asset.getParent()).removeVariant((Variant)asset);
                }
                // FIXME
                /*if (asset != null && asset instanceof FileDescriptor) {
                    ((IVariantContainer)asset.getParent()).removeVariant((Variant)asset);
                }*/
            }
        }
        }
        });

    }

    private List getAssetsByDelta(IResourceDelta delta)
    {
        try {
	        AssetFinderVisitor afv = new AssetFinderVisitor();
            //delta.accept(afv);
            return afv.getAssets();
        } catch (/*Core*/Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    /**
     * Finds the asset corresponding to the given changed delta (only one resource!!)
     */
    private class AssetFinderVisitor implements IResourceDeltaVisitor {
        private AbstractAsset asset = null;    
        private boolean valid = false;
        private IResourceDelta oldDelta = null;
        private IResourceDelta oDelta = null;
        private List assets = new ArrayList();        
        public boolean visit(IResourceDelta delta) throws CoreException
        {
            if (oDelta == null) {
                oDelta = delta;
            }

            if (delta.getResource() instanceof IFile) {
                return false;
            } else if (delta.getResource() instanceof IProject) {
                IProject p = (IProject) delta.getResource();
                if (KoboldProject.hasKoboldNature(p)) {
                    asset = ((KoboldProject) p.getNature(KoboldProject.NATURE_ID)).getProductline();
                    logger.debug(asset.getName());
                    //
                    getAssetsByDelta(asset, assets, oDelta);

                }
                return false;
            } 
            return true;
        }

        public List getAssets() 
        {
            return assets;
        }

        private void getAssetsByDelta(AbstractAsset root, List assets, IResourceDelta delta) 
        {
            if (delta.findMember(ModelStorage.getFolderForAsset(root).getFullPath()) != null) {
                assets.add(root);
                logger.debug("asset resource has been changed:"+root);
            }
            Iterator it = root.getChildren().iterator();
            while (it.hasNext()) {
                getAssetsByDelta((AbstractAsset)it.next(), assets, delta);
            }
        }

    }
}
