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
 * $Id: AbstractComposableEditPart.java,v 1.3 2004/08/05 14:19:33 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.beans.PropertyChangeEvent;

import kobold.client.plam.editor.KoboldGraphicalViewer;
import kobold.client.plam.editor.figure.ComposableFigure;
import kobold.client.plam.editor.policy.ComposeEditPolicy;
import kobold.client.plam.model.AbstractAsset;



/**
 * @author Tammo
 */
public abstract class AbstractComposableEditPart extends AbstractAssetEditPart
{
	/**
	 * Toggles the composing mode on refresh. 
	 */
	protected void refreshVisuals() 
	{
	    super.refreshVisuals();
	    
		String comp = (String)getViewer().getProperty("composing");
		if (comp != null && comp.equals("true")) {
		    ((ComposableFigure)getFigure()).startComposing();
		    ((ComposableFigure)getFigure()).setState(((KoboldGraphicalViewer)getViewer()).getProductComposer().getState(getAsset()));
		} else {
		    ((ComposableFigure)getFigure()).stopComposing();
		}
	}
  
    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
		if (AbstractAsset.ID_COMPOSE.equals(evt.getPropertyName())) {
			refreshVisuals();
		}
        super.propertyChange(evt);
    }
    
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies()
    {
        super.createEditPolicies();
		installEditPolicy("composer", new ComposeEditPolicy());
    }
}
