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
 * $Id: KoboldProject.java,v 1.35 2004/11/17 11:53:16 garbeam Exp $
 *
 */
package kobold.client.plam;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kobold.client.plam.controller.ServerHelper;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.listeners.IVCMActionListener;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractMaintainedAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.ProductlineFactory;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.workflow.LocalMessageQueue;
import kobold.common.data.Component;
import kobold.common.data.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

/**
 * @author Tammo
 */
public class KoboldProject implements IProjectNature, IResourceChangeListener, 
	PropertyChangeListener 
{
	public static final Log logger = LogFactory.getLog(KoboldProject.class);
	
    public static final String NATURE_ID = "kobold.client.plam.KoboldProject"; //$NON-NLS-1$
	public static final String MESSAGES_FILENAME = ".messages"; //$NON-NLS-1$
	public static final String PLAM_FILENAME = ".kobold"; //$NON-NLS-1$

    private IProject project;
	private LocalMessageQueue mqueue;
	
	private URL serverURL;
	private String userName;
	private String password;
	private String productlineId;
	private Productline productline;

	private Map userPool;
	private boolean isDirty = false;

    protected transient List vcmListeners = new LinkedList();
    
	/**
     * @see org.eclipse.core.resources.IProjectNature#configure()
     */
    public void configure() throws CoreException
    {
    }

    /**
     * @see org.eclipse.core.resources.IProjectNature#deconfigure()
     */
    public void deconfigure() throws CoreException
    {
    }

    /**
     * @see org.eclipse.core.resources.IProjectNature#getProject()
     */
    public IProject getProject()
    {
        return project;
    }

    /**
     * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
     */
    public void setProject(IProject project)
    {
		this.project = project;
		load();
    }

    /**
     * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
     */
    public void resourceChanged(IResourceChangeEvent event)
    {
        
    }

	/**
	 * Returns the one an only messagequeue instance.
	 */
	public LocalMessageQueue getMessageQueue() 
	{
		// lazy
		if (mqueue == null) {
			mqueue = new LocalMessageQueue(project.getFile(MESSAGES_FILENAME));
		}
		
		return mqueue;
	}

    /**
     * @return Returns the password.
     */
    public String getPassword()
    {
        return password;
    }
    
    /**
     * @param password The password to set.
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    /**
     * @return Returns the productline.
     */
    public Productline getProductline()
    {
	    // lazy
        if (productline == null) {
            if (!isConfigured())
                return null;
            
            kobold.common.data.Productline spl = ServerHelper.fetchProductline(this);
            if (spl == null) {
                logger.error("Connection failed: Could not fetch productline descriptor");
                MessageDialog.openError(Display.getDefault().getActiveShell(), "Server Error", "Productline does not exist on server.");
                return null;
            }
            
            // FIXME: perform an vcm update.
            updateProductline(spl, getProject());
            
            productline = ModelStorage.loadModel(this, spl);
	        
		    if (productline == null) {
		        productline = ProductlineFactory.create(spl);
			    productline.setProject(this);
			    productline.setRepositoryDescriptor(spl.getRepositoryDescriptor());
		        
		        ModelStorage.storeModel(productline);
		        commitProductline(productline);
			    //check out from repo
			    logger.debug("checkout pl");
			    
		    } else {
		        productline.setRepositoryDescriptor(spl.getRepositoryDescriptor());
		        productline.setProject(this);
		    }

            List sMaints = spl.getMaintainers();
	        productline.getMaintainers().clear();
	        Iterator it = sMaints.iterator();
	        while (it.hasNext()) {
	            User user = (User)it.next();
	            productline.addMaintainer(user);
	            logger.debug("Maintainer added: " + user);
	        }
	        
	        it = spl.getCoreAssets().iterator();
	        while (it.hasNext()) {
	            Component sc = (Component)it.next();
	            AbstractAsset a = productline.getAssetById(sc.getId());
	            if (a != null && a instanceof AbstractMaintainedAsset) {
	                Iterator mit = sc.getMaintainers().iterator();
	                while (mit.hasNext())
	                ((AbstractMaintainedAsset)a).addMaintainer((User)mit.next());
	            }
	        }
	        
	        productline.addModelChangeListener(this);

	    }

	    return productline;
    }
    
    /**
     * @param productline The productline to set.
     */
    public void setProductline(Productline productline)
    {
        this.productline = productline;
    }
    
    /**
     * @return Returns the productlineId.
     */
    public String getProductlineId()
    {
        return productlineId;
    }
    
    /**
     * @param productlineId The productlineId to set.
     */
    public void setProductlineId(String productlineId)
    {
        this.productlineId = productlineId;
    }
    
    /**
     * @return Returns the serverURL.
     */
    public URL getServerURL()
    {
        return serverURL;
    }
    
    /**
     * @param serverURL The serverURL to set.
     */
    public void setServerURL(URL serverURL)
    {
        this.serverURL = serverURL;
    }
    
    /**
     * @return Returns the username.
     */
    public String getUserName()
    {
        return userName;
    }
    
    /**
     * @param userName The username to set.
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
	
	/**
	 * Loads the configuration data from the config file.
	 */
	public void load()
	{
		try {
			IFile file = project.getFile(PLAM_FILENAME);
		    if (file.exists()) {
				InputStream stream= null;
				try {
					stream = new BufferedInputStream(file.getContents(true));
				    SAXReader reader = new SAXReader();
				    Document document = reader.read(stream);
				    
				    Element root = document.getRootElement();

				    String url = root.elementTextTrim("server-url");
				    setServerURL((url != null) ? new URL(url) : null);
				    
				    setUserName(root.elementTextTrim("username"));
				    setPassword(root.elementTextTrim("password"));
				    setProductlineId(root.elementTextTrim("pl-id"));
        
				} catch (CoreException e) {
					
				} finally {
				    if (stream != null)
				        stream.close();
				}
		    }
		} catch (DocumentException e) {
			logger.info("Error while parsing PLAM config.", e);
		} catch (IOException e) {
		}
	}

	public void store()
	{
	    final IFile file = project.getFile(PLAM_FILENAME);
	    final OutputFormat format = OutputFormat.createPrettyPrint();
	    
	    final Document document = DocumentHelper.createDocument();
	    Element root = document.addElement("kobold-config");
	    // FIXME
	    root.addElement("server-url").setText(serverURL.toString());
	    root.addElement("username").setText(userName);
	    root.addElement("password").setText(password);
	    root.addElement("pl-id").setText(productlineId);
	    
	    IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
	    
	    try {
	        progressService.busyCursorWhile(new IRunnableWithProgress(){
	            public void run(IProgressMonitor monitor) {
	                XMLWriter writer;
	                ByteArrayOutputStream out = new ByteArrayOutputStream();
	                try {
	                    writer = new XMLWriter(out,	format);
	                    writer.write(document);
	                    writer.close();
	                } catch (Exception e) {
	                    logger.warn("Error during serializing kobold nature", e);
	                }
	                
	                
	                try {
	                    if (file.exists()) {
	                        file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
	                    } else {
	                        file.create(new ByteArrayInputStream(out.toByteArray()), 
	                                true, monitor);
	                    }
	                } catch (CoreException e1) {
	                    logger.warn("could not store kobold nature prefs.");
	                } finally {
	                    monitor.done();
	                }
	            }});
	    } catch (InvocationTargetException e) {
	    } catch (InterruptedException e) {}

	    if (productline != null) {
	        ModelStorage.storeModel(productline);
	        commitProductline(productline);     
	        isDirty = false;
	    }
	}
    
	public void updateUserPool()
	{
	    if (isConfigured()) {
	        List users = ServerHelper.fetchAllUsers(this);
	        
	        userPool.clear();
	        Iterator it = users.iterator();
	        while (it.hasNext()) {
	            User u = (User)it.next();
	            userPool.put(u.getUsername(), u);
	        }
	    }
	}
	
	public Map getUserPool() 
	{
	    if (userPool == null) {
	        userPool = new HashMap();
	        updateUserPool();
	    }
	    return Collections.unmodifiableMap(userPool);
	}
	
	private Path calcViewModelStorePath(AbstractRootAsset root) 
	{
   		String path = "";
   		boolean isManager = false;
   		Iterator it = root.getMaintainers().iterator(); 
   		while (it.hasNext()) {
   		    User u = (User)it.next();
   		    if (u.getUsername().equals(getUserName())) {
   		        isManager = true;
   		        break;
   		    }
   		}
   		
   		if (isManager) {
   		    //if (root instanceof Productline) {
   		        path = root.getResource() + IPath.SEPARATOR;
   		    /*} else {
   		        path = ModelStorage.PRODUCTS_FOLDER_NAME + IPath.SEPARATOR
   		    		+ root.getResource() + IPath.SEPARATOR;
   		    }
   		    */
   		}
   		
   		path = path + root.getResource() + ".vm";
   		
   		return new Path(path);
	}
	
	private Path calcViewModelLoadPath(AbstractRootAsset root) 
	{
   		String path = "";

   		boolean isManager = false;
   		Iterator it = root.getMaintainers().iterator(); 
   		while (it.hasNext()) {
   		    User u = (User)it.next();
   		    if (u.getUsername().equals(getUserName())) {
   		        isManager = true;
   		        break;
   		    }
   		}
   		
	    String mPath = "";
   		//if (root instanceof Productline) {
	        mPath = root.getResource() + IPath.SEPARATOR;

   		Path p = new Path(mPath + root.getResource() + ".vm");
   		if (isManager) {
   		    return p;
   		} else {
   		    Path lp = new Path(root.getResource() + ".vm");
   		    if (project.getFile(lp).exists()) {
   		        return lp;
   		    } else {
   		        return p;
   		    }
   		}
	}
	
	/**
     * @param viewModel
     * @param monitor
     */
    public void storeViewModelContainer(AbstractRootAsset root, ViewModel viewModel, IProgressMonitor monitor) 
    		throws Exception
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
   		XMLWriter writer;
   		writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
   		writer.write(viewModel.serialize());
   		writer.close();
   		
   		
   		IFile vmFile = project.getFile(calcViewModelStorePath(root));
   		if (vmFile.exists()) {
   		    vmFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
   		} else {
   		    vmFile.create(new ByteArrayInputStream(out.toByteArray()), 
    			true, monitor);
   		}
    	out.close();
    }

    public ViewModel restoreViewModelContainer(AbstractRootAsset root) 
    {
        IFile vmFile = project.getFile(calcViewModelLoadPath(root));
        if (vmFile.exists()) {
            InputStream is;
            try {
                is = vmFile.getContents(false);
                SAXReader reader = new SAXReader();
                Document document;
                document = reader.read(is);
  
                return new ViewModel(document.getRootElement());
            } catch (DocumentException e) {
                logger.warn("Could not parse viewmodel", e);
            } catch (CoreException e) {
                logger.warn("Could not open view model", e);
            }
        }
        return null;
    }

	public boolean isConfigured() 
	{
	    return (serverURL != null) && (userName != null) && (password != null);
	}
	
    /**
	 * Returns true if the given project is accessible and it has
	 * a kobold nature, otherwise false.
	 * @param project IProject
	 * @return boolean
	 */
	public static boolean hasKoboldNature(IProject project) { 
		try {
			return project.hasNature(KoboldProject.NATURE_ID);
		} catch (CoreException e) {
			// project does not exist or is not open
		}
		return false;
	}

    public void addVCMActionListener(IVCMActionListener l)
	{
		vcmListeners.add(l);
	}

	public void removeVCMActionListener(IVCMActionListener l)
	{
		vcmListeners.remove(l);
	}

	public void refreshResources(IFileDescriptorContainer fdCont)
	{
	    IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
	    if (l != null) {
	        l.refreshFiledescriptors(fdCont);
	    } else {
	        logger.error("no vcm listener registered");
	    }
	}
	
    public void updateProduct(kobold.common.data.Product p, IProject prj) 
	{
	    IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
	    if (l != null) {
	        l.updateAsset(p, prj);
	    } else {
	        logger.error("no vcm listener registered");
	    }
	}
	
	public void commitProduct(Product p)
	{
	    IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
	    if (l != null) {
	        l.commitProduct(p);
	    } else {
	        logger.error("no vcm listener registered");
	    }
	}
	
    public void updateProductline(kobold.common.data.Productline spl, IProject p) 
	{
	    IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
	    if (l != null) {
	        l.updateAsset(spl, p);
	    } else {
	        logger.error("no vcm listener registered");
	    }
	}
	
	public void commitProductline(Productline pl)
	{
	    IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
	    if (l != null) {
	        l.commitProductline(pl);
	    } else {
	        logger.error("no vcm listener registered");
	    }
	}
	
	public void updateRelease(RelatedComponent rc, Release newRelease){
	    IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
	    if (l != null) {
	        l.updateRelease(rc, newRelease);
	    } else {
	        logger.error("no vcm listener registered");
	    }	    
	}
	
	public void tagRelease(Release release)
	{
	    IVCMActionListener l = KoboldPLAMPlugin.getDefault().getVCMListener();
	    if (l != null) {
	        l.tagRelease(release);
	    } else {
	        logger.error("no vcm listener registered");
	    }
	}

	public boolean isDirty()
	{
	    return isDirty;
	}
	
    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent arg0)
    {
        isDirty = true;
    }

    /**
     * @see java.lang.Object#finalize()
     */
    protected void finalize() throws Throwable
    {
        if (productline != null) {
            productline.removeModelChangeListener(this);
        }
        super.finalize();
    }
}
