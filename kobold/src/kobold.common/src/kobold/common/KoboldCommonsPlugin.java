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
 * $Id: KoboldCommonsPlugin.java,v 1.4 2004/11/05 10:50:57 grosseml Exp $
 *
 */
package kobold.common;

import org.apache.log4j.Logger;

import kobold.common.data.IdManager;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;


/**
 * The main plugin class to be used in the desktop.
 */
public class KoboldCommonsPlugin extends Plugin 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(KoboldCommonsPlugin.class);

	private static KoboldCommonsPlugin plugin;
    String oldState = null;
    private IdManager.NodeManagerImpl nodeManager;
    
	/**
	 * The constructor.
	 */
	public KoboldCommonsPlugin() {
		super();
		//BasicConfigurator.configure();
		plugin = this;
	}
	
    /**
	 * Returns the shared instance.
	 */
	public static KoboldCommonsPlugin getDefault() {
			return plugin;
	}
	
    /**
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        
        // set UUID node manager class
        oldState = System.getProperty("org.apache.commons.id.uuid.NodeManager");
        System.setProperty("org.apache.commons.id.uuid.NodeManager", IdManager.NodeManagerImpl.class.getName());
    }
    
    /**
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception
    {
        super.stop(context);
        // restore UUID node manager
        if (nodeManager != null) {
            nodeManager.store();
        }
        if (oldState != null) {
            System.setProperty("org.apache.commons.id.uuid.NodeManager", oldState);
        }
    }

    /**
     * @param impl
     */
    public void setNodeManager(IdManager.NodeManagerImpl impl)
    {
        this.nodeManager = impl;
    }
}
