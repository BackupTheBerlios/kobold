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
 * $Id: ComponentEditPart.java,v 1.5 2004/06/22 23:30:12 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.util.List;

import kobold.client.plam.editor.figure.ComponentFigure;
import kobold.common.model.productline.Component;

import org.eclipse.draw2d.IFigure;

/**
 * ComponentEditPart
 * 
 * @author Tammo van Lessen
 * @version $Id: ComponentEditPart.java,v 1.5 2004/06/22 23:30:12 vanto Exp $
 */
public class ComponentEditPart extends AbstractNodeEditPart {

    private ComponentFigure figure;
    
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    protected IFigure createFigure() {
        figure = new ComponentFigure();
        figure.setTitle(getAsset().getName());
        //figure.setSize(getAsset().getSize());
		//figure.setLocation(getAsset().getLocation());
        //refreshVisuals();

		//ScriptDescriptor sd = getNode().getScriptDescriptor();
		//figure.showScriptLabel(sd != null);

        return figure;
    }


    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren() You
     *      must implement this method if you want you root model to have
     *      children!
     */
    protected List getModelChildren()
    {
        List children = ((Component)getModel()).getVariants();
        return children;
    }

    /**
     * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
     */
    public IFigure getContentPane() {
        return figure.getContentPane();
    }
}
