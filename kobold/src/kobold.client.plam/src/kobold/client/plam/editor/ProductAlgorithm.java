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
 *
 */
package kobold.client.plam.editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import kobold.client.plam.model.AbstractAsset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author pliesmn
 * 
 *
 * 
 */
public class ProductAlgorithm {
    private static final Log logger = LogFactory.getLog(ProductAlgorithm.class);
    protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    
    public static final String STATE_OPEN = "sopen";
    public static final String STATE_USED = "sused";
    public static final String STATE_MUST_NOT = "sdont";
    
    private Map stateByAsset = new HashMap();
    
    public ProductAlgorithm () {
        
    }
    
    public void setUsed(AbstractAsset node) {
        stateByAsset.put(node, STATE_USED);
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_USED);
        logger.debug(node + " has new state USED");
    }
    
    public void setMustNotUse(AbstractAsset node) {
        stateByAsset.put(node, STATE_MUST_NOT);
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_MUST_NOT);
        logger.debug(node + " has new state MUST_NOT");
    }
    
    public void setOpen(AbstractAsset node) {
        stateByAsset.put(node, STATE_OPEN);
        listeners.firePropertyChange(AbstractAsset.ID_COMPOSE, null, STATE_OPEN);
        logger.debug(node + " has new state OPEN");
    }
    
    
    public boolean isUsed(AbstractAsset node){
        String state = (String)stateByAsset.get(node);
        if (state == STATE_USED) {
            return true;
        }
        return false;
    }
    
    public boolean mustNotUse(AbstractAsset node){
        String state = (String)stateByAsset.get(node);
        if (state == STATE_MUST_NOT) {
            return true;
        }
        return false;
    }
    
    public boolean isOpen(AbstractAsset node){
        String state = (String)stateByAsset.get(node);
        if (state == STATE_OPEN || state == null) {
            return true;
        }
        return false;
    }
    
    public String getState(AbstractAsset node) {
        String state = (String)stateByAsset.get(node);
        if (state == null) {
            return STATE_OPEN;
        }
        return state;
    }
    
    public boolean isToWorkOn(AbstractAsset node) {
        return false;
    }
    
    public boolean hasWarning(AbstractAsset node) {
        return false;
    }
    
    public String getWarning(AbstractAsset node){
        return " ";
    }
 
    public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		listeners.removePropertyChangeListener(l);
	}


}
