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
 * $Id: AbstractRootAsset.java,v 1.2 2004/06/28 22:35:23 vanto Exp $
 *
 */
package kobold.common.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;


/**
 * This is the abstract root for Productline.
 * 
 * @author Tammo
 */
public abstract class AbstractRootAsset extends AbstractMaintainedAsset
{
    private Object project;
    private Map userPool = new HashMap();
    protected Map releasePool = new HashMap();
    protected transient PropertyChangeSupport changeListeners = new PropertyChangeSupport(this); 
    
    /**
     * Default constructor. 
     */
    public AbstractRootAsset()
    {
    }

    /**
     * Name constructor. 
     */
    public AbstractRootAsset(String name)
    {
        super(name);
    }

    
    /**
     * @param project The project to set.
     */
    public void setProject(Object project)
    {
        this.project = project;
    }
    
    
    /**
     * @return Returns the project.
     */
    public Object getProject()
    {
        return project;
    }
    
    /**
     * @return Returns the userPool.
     */
    public Map getUserPool()
    {
        return userPool;
    }

    public void addModelChangeListener(PropertyChangeListener l)
	{
		changeListeners.addPropertyChangeListener(l);
	}

	public void removeModelChangeListener(PropertyChangeListener l)
	{
		changeListeners.removePropertyChangeListener(l);
	}
	
	public void fireModelChange() 
	{
	    changeListeners.firePropertyChange("refresh", false, true);
	}
}
