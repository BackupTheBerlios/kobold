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
 * $Id: Asset.java,v 1.5 2004/08/03 16:46:35 garbeam Exp $
 *
 */
package kobold.common.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.common.io.RepositoryDescriptor;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Base class for architectural elements which are managed by the Kobold Server.
 * Implements the {@see kobold.common.data.ISerializable} class for client-server
 * interchange.
 */
public class Asset implements ISerializable {

	public static final String COMPONENT = "component";
	public static final String PRODUCT = "product";
	public static final String PRODUCT_LINE = "productline";

	// resource (file or directory name) of this asset
	private String resource = null;
	// name of this asset
	private String name = null;
	// id of this asset
	private String id = null;
	// type of this asset
	private String type = null;
	// repository location of this asset
	private RepositoryDescriptor repositoryDescriptor = null;
	// maintainers of this asset
	private List maintainer = new ArrayList();
	// parent asset
	private Asset parent = null;
	
	/**
	 * Base constructor for server side assets.
	 * @param parent of this asset, if <code>null</code> this
	 * 		  asset is the root.
	 * @param type the type of this asset, must be a value of the
	 * 		  static final members of this class.
	 * @param id a unique id of this asset.
	 * @param name the name
	 * @param resource the file or directory name (no PATH!)
	 * @param repositoryDescriptor
	 */
	public Asset(Asset parent, String type, String name, String resource,
			     RepositoryDescriptor repositoryDescriptor)
	{
		this.parent = parent;
		this.type = type;
		this.name = name;
		this.resource = resource;
		this.repositoryDescriptor = repositoryDescriptor;
	
		this.id = IdManager.nextId(name);
	}

	
	public Asset(Asset parent) 
	{
	    this.parent = parent;
	}
	
	/**
	 * DOM constructor for deserialization.
	 */
//	public Asset(Asset parent, Element element) {
//		this.parent = parent;
//		deserialize(element);
//	}
	
	/**
	 * Serializes this asset.
	 */
	public Element serialize() {
		Element element = DocumentHelper.createElement(type);
		element.addAttribute("id", this.id);
		element.addAttribute("name", this.name);
		element.addAttribute("resource", this.resource);
		if (parent != null) {
			element.addAttribute("parent-id", parent.getId());
		}
		element.add(repositoryDescriptor.serialize());
		Element maintainerElements = element.addElement("maintainers");
		for (Iterator iterator = maintainer.iterator(); iterator.hasNext(); ) {
			User user = (User) iterator.next();
			maintainerElements.add(user.serialize());
		}
		return element;
	}

	/**
	 * Deserializes this asset.
	 */
	public void deserialize(Element element) {
		this.type = element.getName();
		this.id = element.attributeValue("id");
		this.name = element.attributeValue("name");
		this.resource = element.attributeValue("resource");
		this.repositoryDescriptor =
			new RepositoryDescriptor(element.element("repository-descriptor"));
		Element maintainerElements = element.element("maintainers");
		for (Iterator iterator = maintainerElements.elementIterator("user");
		     iterator.hasNext(); )
		{
			Element elem = (Element) iterator.next();
			maintainer.add(new User(elem));
		}
	}

	/**
	 * Returns the id of this asset.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns a list of all maintainer.
	 */
	public List getMaintainers() {
		return maintainer;
	}
	
	/**
	 * Adds new maintainer.
	 * @param user the maintainer.
	 */
	public void addMaintainer(User user) {
		maintainer.add(user);
	}
	
	/**
	 * Removes maintainer.
	 * @param user the maintainer.
	 */
	public void removeMaintainer(User user) {
		maintainer.remove(user);
	}
	
	/**
	 * Returns the name of this asset.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the parent asset.
	 */
	public Asset getParent() {
		return parent;
	}
	
	/**
	 * Sets the parent.
	 * @parent parent the parent asset.
	 */
	public void setParent(Asset parent) {
		this.parent = parent;
	}
	
	/**
	 * Returns the repositoryDescriptor.
	 */
	public RepositoryDescriptor getRepositoryDescriptor() {
		return repositoryDescriptor;
	}
	
	/**
	 * Returns the type.
	 */
	public String getType() {
		return type;
	}
	
    public String getResource() {
        return resource;
    }
    
    public void setResource(String resource) {
        this.resource = resource;
    }
}