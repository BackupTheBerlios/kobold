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
 * $Id: RelatedComponentEditPart.java,v 1.3 2004/08/06 11:04:29 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import kobold.client.plam.editor.figure.RelatedComponentFigure;

import org.eclipse.draw2d.IFigure;


/**
 * @author Tammo
 */
public class RelatedComponentEditPart extends AbstractAssetEditPart
{
    private RelatedComponentFigure figure;
    
    /**
     * @see kobold.client.plam.editor.editpart.AbstractAssetEditPart#createNodeFigure()
     */
    protected IFigure createNodeFigure()
    {
        figure = new RelatedComponentFigure();
		figure.setTitle(getAsset().getName());
    
        return figure;
    }

    protected void refreshVisuals()
    {
        super.refreshVisuals();
        figure.setTitle((getAsset().getName() == null) ? "" : getAsset().getName());
    }
}
