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
 * $Id: KoboldGraphicalViewer.java,v 1.4 2004/08/01 18:23:06 martinplies Exp $
 *
 */
package kobold.client.plam.editor;

import java.util.Iterator;

import kobold.client.plam.editor.editpart.AbstractAssetEditPart;
import kobold.client.plam.editor.tool.ProductComposer;
import kobold.client.plam.model.productline.Productline;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;


/**
 * An enhanced {@link org.eclipse.gef.ui.parts.ScrollingGraphicalViewer}.
 * 
 * @author Tammo
 */
public class KoboldGraphicalViewer extends ScrollingGraphicalViewer
{
	private boolean isComposing = false; 
	private ProductComposer composer = null; 

    /**
     * @return Returns the isComposing.
     */
    public boolean isComposing()
    {
        return isComposing;
    }
    
    /**
     * @param isComposing The isComposing to set.
     */
    public void setComposing(boolean isComposing)
    {
        if (this.isComposing == isComposing)
            return;
        
        if (!(getContents().getModel() instanceof Productline))
            return;
        
        this.isComposing = isComposing;
        setProperty("composing", (isComposing)?"true":"false");
        if (isComposing) {
            ProductComposer oldComp = composer;
            composer = new ProductComposer((Productline)getContents().getModel());

            Iterator it = getEditPartRegistry().values().iterator();
            while (it.hasNext()) {
                EditPart ep = (EditPart)it.next();
                if (ep instanceof AbstractAssetEditPart) {
                    if (oldComp != null) {
                        oldComp.removePropertyChangeListener((AbstractAssetEditPart)ep);
                    }
                    composer.addPropertyChangeListener((AbstractAssetEditPart)ep);
                    ep.refresh();
                }
            }
        } else {
            Iterator it = getEditPartRegistry().values().iterator();
            while (it.hasNext()) {
                EditPart ep = (EditPart)it.next();
                if (ep instanceof AbstractAssetEditPart) {
                    composer.removePropertyChangeListener((AbstractAssetEditPart)ep);
                    ep.refresh();
                }
            }
            composer = null;
        }
    }
    
    public ProductComposer getProductComposer() 
    {
        return composer;
    }

}
