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
 * $Id: RepositoryDescriptor.java,v 1.3 2004/06/24 00:38:52 garbeam Exp $
 */

package kobold.common.io;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import kobold.common.data.ISerializable;

/**
 * Base class for locating repositories.
 * This descriptor is used for repositories (whatever type).
 * It provides host, protocol and file description.
 *
 * @author garbeam
 */
public class RepositoryDescriptor implements ISerializable {
	
	private String protocol = null;
	private String host = null;
	private String path = null;


	/**
	 * Basic default constructor.
	 */
	public RepositoryDescriptor() {
	}

	/**
	 * Convenient (initial) constructor.
	 */
	public RepositoryDescriptor(String protocol,
								String host,
								String path)
	{
		this.protocol = protocol;
		this.host = host;
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
		element.addAttribute("host", host);
		element.addAttribute("protocol", protocol);
		element.addAttribute("path", path);
		
		return element;
	}
	
	/**
	 * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
	 */
	public void deserialize(Element element) {
		host = element.attributeValue("host");
		protocol = element.attributeValue("protocol");
		path = element.attributeValue("path");
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
	 * @param protocol, the protocol type (userdefined).
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

}
