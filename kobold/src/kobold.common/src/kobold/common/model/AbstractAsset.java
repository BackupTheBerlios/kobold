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
 * $Id: AbstractAsset.java,v 1.3 2004/06/22 11:29:15 vanto Exp $
 *
 */
package kobold.common.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import kobold.common.data.ISerializable;
import kobold.common.data.IdManager;

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

    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    private String name;
    private String id;
    private AbstractAsset parent;
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
        //sets the parent
        //setParent ();
    }

    /**
     * Returns the id.
     */
    public String getId()
    {
        return id;
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
    }

    /**
     * Sets the assets description.
     * 
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
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
    }

    /**
     * Removes a status object from the set.
     * 
     * @param status
     */
    public void removeStatus(AbstractStatus status)
    {
        statusSet.remove(status);
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
        AbstractAsset parent = getParent();
        while (parent != null) {
            parent = parent.getParent();
        }

        return parent;
    }

    /**
     * Sets the parent asset.
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
        element.addElement("id").setText(id);
        
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
      //TODO    
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

	protected void firePropertyChange(String prop, Object old, Object newValue){
		listeners.firePropertyChange(prop, old, newValue);
	}

	protected void fireStructureChange(String prop, Object child){
		listeners.firePropertyChange(prop, null, child);
	}
}

