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
 * $Id: AbstractAsset.java,v 1.12 2004/06/25 11:41:55 martinplies Exp $
 *
 */
package kobold.common.model;

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

import kobold.common.data.ISerializable;
import kobold.common.data.IdManager;

import net.sourceforge.gxl.GXLElement;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author Tammo
 *  
 */
public abstract class AbstractAsset implements ISerializable
{

    public static final String PRODUCT_LINE = "productline";
    public static final String PRODUCT = "product";
    public static final String COMPONENT = "component";
    public static final String VARIANT = "variant";
    public static final String RELEASE = "release";
    public static final String FILE_DESCRIPTOR = "filedesc";
    
    protected static final Log logger = LogFactory.getLog(AbstractAsset.class);

    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
	public static final String
		ID_CHILDREN = "children", 	//$NON-NLS-1$
		ID_DATA = "data", //$NON-NLS-1$
		ID_STATUS = "status"; //$NON-NLS-1$

    private String name;
    private String id;
    private Object parent;
    private String description;
    private String owner;
    private Set statusSet = new HashSet();

    public AbstractAsset()
    {
    }

    public AbstractAsset(String name)
    {
        this.name = name;
        this.id = IdManager.getInstance().getModelId(getType());
        this.parent = null;
    }

    /**
     * Returns the id.
     */
    public String getId()
    {
        return id;
    }
    
    public final GXLNode getGXLGraph() {
    	
    	GXLNode node = new GXLNode(IdManager.getInstance().getMessageId(id));
    	try {
			//node.setAttr("id", new GXLString(id));
    		if (name != null)
			  node.setAttr("name",new GXLString(name));
    		if (description != null)
			  node.setAttr("description", new GXLString(description));
    		if (owner != null)
			  node.setAttr("owner", new GXLString(owner));
			Map attributes = getGXLAttributes();
			if (attributes != null) {
				for (Iterator ite = attributes.keySet().iterator(); ite.hasNext();){
					String key = (String) ite.next();
					node.setAttr(key, new GXLString((String) attributes.get(key)));
				}
			}
			List children = getGXLChildren();
			if (children != null){
			  GXLGraph graph = new GXLGraph(IdManager.getInstance().getGXLGraphId("graph"));
			  for (Iterator ite = children.iterator(); ite.hasNext();){
				AbstractAsset asset = (AbstractAsset) ite.next();
				graph.add(asset.getGXLGraph());
			  }
			  node.add(graph);
			}
			try {
				node.setType(new URI(getGXLType()));
			} catch (Exception e) {
				logger.info("Wrong node type uri specified", e);
			}
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return node;
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
     * @return
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
        fireStructureChange(ID_STATUS, status);
    }

    /**
     * Removes a status object from the set.
     * 
     * @param status
     */
    public void removeStatus(AbstractStatus status)
    {
        statusSet.remove(status);
        fireStructureChange(ID_STATUS, status);
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
     * Returns the root of the model tree. Is mostly type of Productline
     * 
     * @return
     */
    public AbstractAsset getRoot()
    {
        if (!(getParent() instanceof AbstractAsset)) {
            return null;
        }
        
        AbstractAsset parent = (AbstractAsset)getParent();
        while (parent != null) {
            if (!(parent.getParent() instanceof AbstractAsset)) {
                return parent;
            }
            parent = (AbstractAsset)parent.getParent();
        }

        return parent;
    }

    /**
     * Sets the parent asset or the PLAMProject as root object.
     * This should be set in every add/remove operation in this package
     * 
     * @param parent
     */
    public void setParent(Object parent)
    {
        this.parent = parent;
    }

    public Element serialize() 
    {
        Element element = DocumentHelper.createElement(getType());
        element.addAttribute("id", id);
        
        Element nameEl = element.addElement("name");
        if (name != null) {
            nameEl.setText(name);
        }
        
        Element descEl = element.addElement("description");
        if (description != null) {
            descEl.setText(name);
        }

        Element statesEl = element.addElement("states");
        Iterator it = statusSet.iterator();
        while (it.hasNext()) {
            AbstractStatus status = (AbstractStatus)it.next();
            statesEl.add(status.serialize());
        }
        
        return element;
    }
    
    public void deserialize(Element element) 
    {
        id = element.attributeValue("id");
        System.out.println ("set id to " + id);
        //FIXME: Notify IdManager about the usage of this id
        name = element.elementTextTrim("name");
        description = element.elementTextTrim("description");
        
		if (element.element("states") != null)
        {
	    	Iterator it = element.element("states").elementIterator("status");
			while (it.hasNext()) {
			    Element stEl = (Element)it.next();
			    addStatus(AbstractStatus.createStatus(stEl));
			}
		}
    }
    
    /**
     * @return Returns the parent.
     */
    public Object getParent()
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
	}

	protected final void fireStructureChange(String prop, Object child){
		listeners.firePropertyChange(prop, null, child);
	}
	
	public abstract Map getGXLAttributes();
	public abstract List getGXLChildren();
	public abstract String getGXLType();
	/**
	 * @return Returns the owner.
	 */
	public String getOwner() {
		return owner;
	}
	/**
	 * @param owner The owner to set.
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
}

