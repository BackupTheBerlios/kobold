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
 * $Id: AbstractAsset.java,v 1.31 2004/11/25 21:28:06 martinplies Exp $
 *
 */
package kobold.client.plam.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kobold.client.plam.model.edges.INode;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Productline;
import kobold.common.data.ISerializable;
import kobold.common.data.IdManager;
import kobold.common.exception.GXLException;
import kobold.common.io.ScriptDescriptor;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * TODO: Kopfkommentar
 * @author Tammo
 *  
 */
public abstract class AbstractAsset implements ISerializable, INode
{

    public static final String PRODUCT_LINE = "productline";
    public static final String PRODUCT = "product";
    public static final String COMPONENT = "component";
    public static final String RELATED_COMPONENT = "related-component";
    public static final String SPECIFIC_COMPONENT = "specific-component";
    public static final String VARIANT = "variant";
    public static final String RELEASE = "release";
    public static final String PRODUCT_RELEASE = "product-release";
    public static final String FILE_DESCRIPTOR = "filedesc";
    public static final String META_NODE = "meta-node";
    
    protected static final Log logger = LogFactory.getLog(AbstractAsset.class);

    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
	public static final String
		ID_CHILDREN = "children", 	//$NON-NLS-1$
		ID_META = "meta", //$NON-NLS-1$
		ID_DATA = "data", //$NON-NLS-1$
		ID_COMPOSE = "compose", //$NON-NLS-1$
		ID_STATUS = "status", //$NON-NLS-1$
		ID_FILE_DESCRIPTORS= "filedescs"; //$NON-NLS-1$

    private String name = "";
    private String resource;
  
    private String id;
    private AbstractAsset parent;
    private String description = "";
    private Set statusSet = new HashSet();
    private boolean isResourceDefined = false;
    private List beforeScripts = new ArrayList();
    private List afterScripts = new ArrayList();
    
    public AbstractAsset()
    {
        this.id = IdManager.nextId(getType());
    }

    public AbstractAsset(String name)
    {
        this();
        setName(name);
    }
    
    protected void setId(String id)
    {
        this.id = id;
    }
    
    /**
     * Returns the id.
     */
    public String getId()
    {
        return id;
    }
    
    /**
     * Create a GXL Node and add the Children and Attributes of this class and 
     * the Childclass.
     * @return GXLNode
     */
    public GXLNode createGXLGraph(Map nodes) throws GXLException {          
        GXLNode node = new GXLNode(IdManager.nextId(id));
        nodes.put(this, node);
        if (name != null) {
            node.setAttr("name", new GXLString(name));
        }
        if (description != null) {
            node.setAttr("description", new GXLString(description));
        }
        if (resource != null){
            node.setAttr("resource", new GXLString(resource));             
        }

        List children = this.getGXLChildren();
        if (children != null && children.size() > 0) {
            GXLGraph graph = new GXLGraph(IdManager.nextId("graph"));
            for (Iterator ite = children.iterator(); ite.hasNext();) {
                AbstractAsset asset = (AbstractAsset) ite.next();
                graph.add(asset.createGXLGraph(nodes));
            }
            node.add(graph);
        }

        node.setType(URI.create(this.getGXLType()));
        return node;
    }
    
    /**
	 * The List contains only Childclassse of this class.
	 * @return List List with the children of this GXLNode
	 */
	public abstract List getGXLChildren();
	
	/**
	 * 
	 * @return String the GXL type of the Childclass.
	 */
	public abstract String getGXLType();
	
       
    public void addToPool(AbstractAsset asset){
        // add asset to id pool
    	if (this.getRoot()!=null){
    		AbstractRootAsset ara = this.getRoot();
        	Productline pl = ara.getProductline();
        	pl.addAssetToPool(asset);
    	}
    }
    
    
    /**
     * Returns the assets name.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the assets name.
     */
    public void setName(String name)
    {
        this.name = name;
        firePropertyChange(ID_DATA, null, name);
    }

    /**
     * Sets the assets description.
     * 
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
        firePropertyChange(ID_DATA, null, description);
    }

    /**
     * Returns the description
     * 
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Adds a status object to the status set.
     * 
     * @param status
     */
    public void addStatus(AbstractStatus status)
    {
        statusSet.add(status);
        firePropertyChange(ID_STATUS, null, status);
    }

    /**
     * Removes a status object from the set.
     * 
     * @param status
     */
    public void removeStatus(AbstractStatus status)
    {
        statusSet.remove(status);
        firePropertyChange(ID_STATUS, null, status);
    }

    /**
     * Returns an unmodifiable instance of the status set.
     * 
     * @return statusSet
     */
    public Set getStatusSet()
    {
        return Collections.unmodifiableSet(statusSet);
    }

    /**
     * Returns the type of this Asset. Possible values are AbstractAsset.PRODUCT
     * and AbstractAsset.PRODUCT_LINE
     * 
     * @return the type of this asset
     */
    public abstract String getType();

    /**
     * Returns the root of the model tree, an AbstractRootAsset
     */
    public AbstractRootAsset getRoot()
    {
        AbstractAsset parent = getParent();
 
        if (this instanceof AbstractRootAsset && parent == null) {
            return (AbstractRootAsset)this;
        }
        
        while ((parent != null) 
                && !(parent instanceof AbstractRootAsset)) {
            
            parent = parent.getParent();
        }

        return (AbstractRootAsset)parent;
    }

    /**
     * Sets the parent asset or the PLAMProject as root object.
     * This should be set in every add/remove operation in this package
     * 
     * @param parent
     */
    public void setParent(AbstractAsset parent)
    {
        this.parent = parent;
    }

    public Element serialize() 
    {
        Element element = DocumentHelper.createElement(getType());
        element.addAttribute("id", id);
        
        if (resource != null) {
            element.addAttribute("resource", resource);
        }
        element.addAttribute("resource-is-defined", isResourceDefined ? "yes" : "no");
        
        Element nameEl = element.addElement("name");
        if (name != null) {
            nameEl.setText(name);
        }
        
        Element descEl = element.addElement("description");
        if (description != null) {
            descEl.setText(description);
        }

        Element statesEl = element.addElement("states");
        Iterator it = statusSet.iterator();
        while (it.hasNext()) {
            AbstractStatus status = (AbstractStatus)it.next();
            statesEl.add(status.serialize());
        }
        
        Element bsEl = element.addElement("before-scripts");
        for (Iterator iterator = beforeScripts.iterator(); iterator.hasNext();) {
            bsEl.add(((ScriptDescriptor)iterator.next()).serialize());
        }
        
        Element asEl = element.addElement("after-scripts");
        for (Iterator iterator = afterScripts.iterator(); iterator.hasNext();) {
            asEl.add(((ScriptDescriptor)iterator.next()).serialize());
        }
        
        return element;
    }
    
    public void deserialize(Element element) 
    {
    	id = element.attributeValue("id");
    	resource = element.attributeValue("resource");
    	if (resource != null) {
    	    isResourceDefined = true;
    	}
    	else {
            isResourceDefined = element.attributeValue("resource-is-defined") == "yes";
    	}
    	
    	name = element.elementTextTrim("name");
    	description = element.elementTextTrim("description");

    	if (element.element("states") != null) {
    		Iterator it = element.element("states").elementIterator("status");
    		while (it.hasNext()) {
    			Element stEl = (Element)it.next();
    			addStatus(AbstractStatus.createStatus(stEl));
    		}
    	}
    	if (element.element("before-scripts") != null) {
    		Iterator it = element.element("before-scripts").elementIterator("script-descriptor");
    		while (it.hasNext()) {
    			Element el = (Element)it.next();
    			beforeScripts.add(new ScriptDescriptor(el));
    		}
    	}
    	if (element.element("after-scripts") != null) {
    		Iterator it = element.element("after-scripts").elementIterator("script-descriptor");
    		while (it.hasNext()) {
    			Element el = (Element)it.next();
    			afterScripts.add(new ScriptDescriptor(el));
    		}
    	}
    }
    
    /**
     * @return Returns the parent.
     */
    public AbstractAsset getParent()
    {
        return parent;
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		listeners.removePropertyChangeListener(l);
	}

	protected final void firePropertyChange(String prop, Object old, Object newValue){
		listeners.firePropertyChange(prop, old, newValue);

		AbstractRootAsset root = getRoot();
		if (root != null) {
		    root.fireModelChange();
		}
	}

	protected final void fireStructureChange(String prop, Object child){
		listeners.firePropertyChange(prop, null, child);

		AbstractRootAsset root = getRoot();
		if (root != null) {
		    root.fireModelChange();
		    if (root.getParent() instanceof AbstractRootAsset) {
		        ((AbstractRootAsset)root.getParent()).fireModelChange();
		    }
		}

	}

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
    	if (this == obj) {
    	    return true;
    	}
    	
    	if (!(obj instanceof AbstractAsset)) {
    	    return false;
    	}
    	
    	AbstractAsset other = (AbstractAsset)obj;
    	return id.equals(other.getId());
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        return id.hashCode();
    }

    public String getResource() {
        return resource;
    }
    
    public void setResource(String resource) {
        this.resource = resource;
        firePropertyChange(ID_DATA, null, resource);
    }
    
    public boolean isResourceDefined() {
        return isResourceDefined;
    }
    
    public void setResourceDefined(boolean isResourceDefined) {
        this.isResourceDefined = isResourceDefined;
    }
    
    public List getAfterScripts() {
        return afterScripts;
    }
    
    public List getBeforeScripts() {
        return beforeScripts;
    }
    
    public List getChildren() 
    {
        return Collections.EMPTY_LIST;
    }
}
