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
 * $Id: MetaEditPart.java,v 1.4 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import org.apache.log4j.Logger;

import kobold.client.plam.editor.KoboldColors;
import kobold.client.plam.editor.policy.XYLayoutEditPolicyImpl;
import kobold.client.plam.model.MetaNode;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.gef.EditPolicy;


/**
 * @author Tammo
 */
public class MetaEditPart extends AbstractAssetEditPart
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MetaEditPart.class);

    private Label label;
    
    /**
     * @see kobold.client.plam.editor.editpart.AbstractAssetEditPart#createNodeFigure()
     */
    protected IFigure createNodeFigure()
    {
        RoundedRectangle rect = new RoundedRectangle();
        
        String l = getAsset().getType().equals(MetaNode.AND) ? "AND" : "OR";
        rect.setBackgroundColor(getAsset().getType().equals(MetaNode.AND)
            	? KoboldColors.andMeta
            	: KoboldColors.orMeta);
        label = new Label(l);
        rect.add(label);
        return rect;
    }
    
    protected void createEditPolicies()
    {
        super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyImpl());
    }
}
