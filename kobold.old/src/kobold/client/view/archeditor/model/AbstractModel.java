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
 * $Id: AbstractModel.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * AbstractModel
 * 
 * @author Tammo van Lessen
 */
public abstract class AbstractModel {

    private String name;
    private List children = new ArrayList();
    private Point position;
    private Dimension dimension;
    
    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    public AbstractModel(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        listeners.firePropertyChange("name", this.name, name);
        this.name = name;
    }

    public void addChild(AbstractModel model)
    {
        children.add(model);
    }
    
    public void removeChild(AbstractModel model)
    {
        children.remove(model);
    }
    
    public List getChildren()
    {
        return children;
    }

    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        listeners.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        listeners.removePropertyChangeListener(l);
    }

    /**
     * Fires an event when the current object's structure has changed. For
     * nodes, a structure change is a connection or a disconnection.
     */
    protected void fireStructureChange(String prop, Object child)
    {
        listeners.firePropertyChange(prop, null, child);
    }
    /**
     * @return
     */
    public Dimension getDimension() {
        return dimension;
    }

    /**
     * @return
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param dimension
     */
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    /**
     * @param point
     */
    public void setPosition(Point point) {
        position = point;
    }

}
