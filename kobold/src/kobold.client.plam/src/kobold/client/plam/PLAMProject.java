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
 * $Id: PLAMProject.java,v 1.4 2004/05/15 16:18:16 vanto Exp $
 *
 */
package kobold.client.plam;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

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
import org.eclipse.core.runtime.CoreException;

/**
 * Stores all PLAM project information
 * 
 * @author Tammo
 */
public class PLAMProject 
{
	private final static Log logger = LogFactory.getLog(PLAMProject.class);
	
	private IFile plamFile;
	
	private URL serverUrl;
	private String username;
	private String password;
	private String productline;
	
	
	/**
	 * Creates a PLAM Project.
	 * 
	 * @param plamFile the plam config file
	 * @param updateFromFile load data from file?
	 */
	public PLAMProject(IFile plamFile)
	{
		this.plamFile = plamFile;
	}
	
	/**
	 * Loads the configuration data from the config file.
	 */
	public void update()
	{
		if (!plamFile.exists())
			return;
		
		try {
			InputStream in = plamFile.getContents();
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

		    logger.debug("server-uri: " + serverUrl);
		    logger.debug("username: " + username);
		    logger.debug("password: " + password);
			logger.debug("productline: " + productline);
		    in.close();
		} catch (CoreException e) {
			logger.debug("Error while reading PLAM config.", e);
		} catch (DocumentException e) {
			logger.debug("Error while parsing PLAM config.", e);
		} catch (IOException e) {
		}
	}
	
	public void store()
	{
		OutputFormat format = OutputFormat.createCompactFormat();
        XMLWriter writer;
		try {
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("kobold-config");
			root.addElement("server-url").setText(serverUrl.toString());
			root.addElement("username").setText(username);
			root.addElement("password").setText(password);
			root.addElement("productline").setText(productline);
			
			writer = new XMLWriter(new FileWriter(plamFile.getLocation().toOSString()),
						format);
			writer.write(document);
			writer.close();
			
		} catch (IOException e) {
			logger.debug("Error while writing PLAM config.", e);
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
	public String getProductline() {
		return productline;
	}
	
	/**
	 * @param productline The productline to set.
	 */
	public void setProductline(String productline) 
	{
		this.productline = productline;
	}

}
