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
 * $Id: RepositoryDescriptor.java,v 1.7 2004/11/05 10:50:57 grosseml Exp $
 */

package kobold.common.io;

import org.apache.log4j.Logger;

import kobold.common.data.ISerializable;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Base class for locating repositories.
 * This descriptor is used for repositories (whatever type).
 * It provides host, protocol and file description.
 *
 * @author garbeam
 */
public class RepositoryDescriptor implements ISerializable {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(RepositoryDescriptor.class);
	
	public static final String CVS_REPOSITORY = "CVS";
	public static final String SVN_REPOSITORY = "SVN";
	
	// Defines the repository type, e.g. cvs, svn, arch, ...
	private String type = null;
	// Defines the protocol, e.g. ssh, pserver, svn, WebDAV, ...
	private String protocol = null;
	// Defines the hostname, e.g. cvs.berlios.de
	private String host = null;
	// Defines the repository root, e.g. /cvsroot/kobold/
	private String root = null;
	// Defines the module path without repository root, e.g. kobold
	private String path = null;

	/**
	 * Basic default constructor.
	 */
	public RepositoryDescriptor() {
	}

	/**
	 * Convenient (initial) constructor.
	 */
	public RepositoryDescriptor(String type,
								String protocol,
								String host,
								String root,
								String path)
	{
		this.type = type;
		this.protocol = protocol;
		this.host = host;
		this.root = root;
		this.path = path;
	}

	/**
	 * DOM constructor.
	 * @param element the DOM element.
	 */
    public RepositoryDescriptor(Element element) {
    	deserialize(element);
    }
    
	/**
	 * @see kobold.common.data.ISerializable#serialize()
	 */
	public Element serialize() {
		Element element = DocumentHelper.createElement("repository-descriptor");
		element.addAttribute("type", type);
		element.addAttribute("host", host);
		element.addAttribute("protocol", protocol);
		element.addAttribute("root", root);
		element.addAttribute("path", path);
		
		return element;
	}
	
	/**
	 * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
	 */
	public void deserialize(Element element) {
		type = element.attributeValue("type");
		host = element.attributeValue("host");
		protocol = element.attributeValue("protocol");
		root = element.attributeValue("root");
		path = element.attributeValue("path");
	}
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return host name where the repository is located.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return path where the repository (module) is located
	 * 		   at the specific host.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return protocol, only for convenience.
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param host the host name.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @param path the module/file path.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @param protocol the protocol type (userdefined).
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
