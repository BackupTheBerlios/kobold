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
 * $Id: PLAMProject.java,v 1.21 2004/08/02 09:23:04 vanto Exp $
 *
 */
package kobold.client.plam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kobold.client.plam.controller.ServerHelper;
import kobold.client.plam.editor.model.ViewModelContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.ProductlineFactory;
import kobold.client.plam.model.productline.Productline;
import kobold.common.data.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Stores all PLAM project information
 * 
 * @author Tammo
 */
public class PLAMProject 
{
	private final static Log logger = LogFactory.getLog(PLAMProject.class);
	
	private IFile plamFile;
	private IProject project;
	
	private URL serverUrl;
	private String username;
	private String password;
	private String productline;
	private Productline pl;

	private Map userPool = new HashMap();
	
	/**
	 * Creates a PLAM Project.
	 * 
	 * @param plamFile the plam config file
	 * @param updateFromFile load data from file?
	 */
	public PLAMProject(IFile plamFile)
	{
		this.plamFile = plamFile;
		this.project = plamFile.getProject();
	}
	
	/**
	 * Loads the configuration data from the config file.
	 */
	public void update()
	{
		try {
			if (plamFile.exists()) {
			    InputStream in = plamFile.getContents(true);
			    SAXReader reader = new SAXReader();
			    Document document = reader.read(in);
			    
			    Node node = document.getRootElement().selectSingleNode("server-url"); 
			    serverUrl = (node != null)?new URL(node.getText()):null;
			    
			    node = document.getRootElement().selectSingleNode("username"); 
			    username = (node != null)?node.getText():null;
			    
			    node = document.getRootElement().selectSingleNode("password"); 
			    password = (node != null)?node.getText():null;
			    
			    node = document.getRootElement().selectSingleNode("productline"); 
			    productline = (node != null)?node.getText():null;
			    
			    //logger.debug("server-uri: " + serverUrl);
			    //logger.debug("username: " + username);
			    //logger.debug("password: " + password);
			    //logger.debug("productline: " + productline);
			    in.close();
			}
		} catch (CoreException e) {
			logger.info("Error while reading PLAM config.", e);
		} catch (DocumentException e) {
			logger.info("Error while parsing PLAM config.", e);
		} catch (IOException e) {
		}
		
		updateUserMap();
	}
	
	public void store()
	{
		OutputFormat format = OutputFormat.createCompactFormat();
        XMLWriter writer;
		try {
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("kobold-config");
			// FIXME
			root.addElement("server-url").setText(serverUrl.toString());
			root.addElement("username").setText(username);
			root.addElement("password").setText(password);
			root.addElement("productline").setText(productline);
			
			writer = new XMLWriter(new FileWriter(plamFile.getLocation().toOSString()),
						format);
			writer.write(document);
			writer.close();
			
			if (pl != null) {
			    ModelStorage.storeModel(pl);
			}
		} catch (IOException e) {
			logger.debug("Error while writing PLAM config.", e);
		}
		
		
	}
	
	private void updateUserMap()
	{
	    List users = ServerHelper.fetchAllUsers(this);
	    
	    if (users != null) {
	        userPool = new HashMap();
	        Iterator it = users.iterator();
	        while (it.hasNext()) {
	            User u = (User)it.next();
	            userPool.put(u.getUsername(), u);
	        }
	    } else {
	        logger.error("Could not fetch user pool - using old one");
	    }
	}
	/**
	 * @return Returns the password or null if not stored.
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
	 * @return Returns the serverUrl or null if not stored.
	 */
	public URL getServerUrl() 
	{
		return serverUrl;
	}
	
	/**
	 * @param serverUrl The serverUrl to set.
	 */
	public void setServerUrl(URL serverUrl) 
	{
		this.serverUrl = serverUrl;
	}
	
	/**
	 * @return Returns the username or null if not stored.
	 */
	public String getUsername() 
	{
		return username;
	}
	
	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	/**
	 * @return Returns the productline or null if not stored.
	 */
	public Productline getProductline() {
	    if (pl == null) {
		    pl = ModelStorage.loadModel(plamFile.getProject());
	        
		    if (pl == null) {
		        pl = ProductlineFactory.create(ServerHelper.fetchProductline(this));
		    }
		    pl.setProject(this);
	    }

	    return null;
	}
	
	/**
	 * @return Returns the productline or null if not stored.
	 */
	public String getProductlineName() {
		return productline;
	}

	/**
	 * @param productline The productline to set.
	 */
	public void setProductlineName(String productline) 
	{
		this.productline = productline;
	}

    /**
     * @param viewModel
     * @param monitor
     */
    public void storeViewModelContainer(ViewModelContainer viewModel, IProgressMonitor monitor) 
    		throws Exception
    {
        
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
   		XMLWriter writer;
   		writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
   		writer.write(viewModel.serialize());
   		writer.close();
   		IFile vmFile = project.getFile("viewdata");
   		if (vmFile.exists()) {
   		    vmFile.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
   		} else {
   		    vmFile.create(new ByteArrayInputStream(out.toByteArray()), 
    			true, monitor);
   		}
    	out.close();
    }

    public ViewModelContainer restoreViewModelContainer() throws Exception 
    {
        IFile vmFile = project.getFile("viewdata");
        InputStream is = vmFile.getContents(false);
		SAXReader reader = new SAXReader();
	    Document document = reader.read(is);
	    ViewModelContainer vmc = new ViewModelContainer(document.getRootElement());        
        return vmc;
    }
    
    /**
     * Returns the location of the project as an absolute IPath
     * 
     * @return path
     */
    public IFolder getPath() 
    {
        return project.getFolder(project.getLocation());// getFolder(  File.separator)
    }
    
    /**
     * HACK:
     * Returns base interface, because it's not implemented by PLAMProject.
     * @return
     */
    public IProject getIProject() {
    	return project;
    }
    
    /**
     * Returns an unmodifiable user map (uid -> User object).
     * 
     * Refresh this map using update();
     * @return
     */
    public Map getUserPool()
    {
        if (userPool == null) {
            updateUserMap();
        }
        return Collections.unmodifiableMap(userPool);
    }
}
