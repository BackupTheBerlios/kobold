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
 * $Id: ViewModel.java,v 1.4 2004/06/28 01:24:24 vanto Exp $
 *
 */
package kobold.client.plam.editor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import kobold.common.data.ISerializable;
import kobold.common.model.AbstractAsset;


/**
 * @author Tammo
 */
public class ViewModel implements ISerializable
{
	public static final String
		ID_SIZE = "size",         //$NON-NLS-1$
		ID_LOCATION = "location"; //$NON-NLS-1$

    private Dimension size;
	private Point location;
	
	protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private AbstractAsset model;
 
    public ViewModel() 
    {
    	size = new Dimension(150,100);
    	location = new Point(5,5);
    }
    
    public ViewModel(Element element)
    {
        size = new Dimension();
        location = new Point();
        deserialize(element);
    }
    
    public Point getLocation()
    {
        return location;
    }
    
    public void setLocation(Point location)
    {
        this.location = location;
        firePropertyChange(ID_LOCATION, null, location);
    }
    
    
    /**
     * @return Returns the dimension.
     */
    public Dimension getSize()
    {
        return size;
    }
    
    
    /**
     * @param dimension The dimension to set.
     */
    public void setSize(Dimension size)
    {
        this.size = size;
        firePropertyChange(ID_SIZE, null, size);
    }
    
    /**
     * @see kobold.common.data.ISerializable#serialize()
     */
    public Element serialize()
    {
        Element element = DocumentHelper.createElement("prop");
        
        element.addAttribute("x", ""+location.x);
        element.addAttribute("y", ""+location.y);
        element.addAttribute("h", ""+size.height);
        element.addAttribute("w", ""+size.height);
        
        return element;
    }

    /**
     * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
     */
    public void deserialize(Element element)
    {
        location.setLocation(Integer.parseInt(element.attributeValue("x")),
            Integer.parseInt(element.attributeValue("y")));
        size.height = Integer.parseInt(element.attributeValue("h"));
        size.width = Integer.parseInt(element.attributeValue("w"));
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

}
