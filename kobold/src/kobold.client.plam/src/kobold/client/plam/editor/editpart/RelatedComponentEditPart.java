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
 * $Id: RelatedComponentEditPart.java,v 1.5 2004/08/25 02:27:02 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kobold.client.plam.editor.figure.RelatedComponentFigure;
import kobold.client.plam.editor.policy.XYLayoutEditPolicyImpl;
import kobold.client.plam.model.AbstractStatus;
import kobold.client.plam.model.product.RelatedComponent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.internal.WorkbenchPlugin;


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
		Set set = new HashSet(getAsset().getStatusSet());
		set.add(new RelatedStatus());
		figure.setStatusSet(set);
    
        return figure;
    }

    protected void refreshVisuals()
    {
        super.refreshVisuals();
        figure.setTitle((getAsset().getName() == null) ? "" : getAsset().getName());
		Set set = new HashSet(getAsset().getStatusSet());
		set.add(new RelatedStatus());
		figure.setStatusSet(set);
    }

    /**
     * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
     */
    public IFigure getContentPane() {
        return figure.getContentPane();
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies()
    {
        super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyImpl());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    protected List getModelChildren()
    {
        List children = new ArrayList(); 
        children.addAll(((RelatedComponent)getModel()).getProductComponents());
        return children;
    }
    
    private class RelatedStatus extends AbstractStatus 
    {

        /* (non-Javadoc)
         * @see kobold.client.plam.model.AbstractStatus#getId()
         */
        public String getId()
        {
            return "related";
        }

        /* (non-Javadoc)
         * @see kobold.client.plam.model.AbstractStatus#getName()
         */
        public String getName()
        {
            return "Related Component";
        }

        /* (non-Javadoc)
         * @see kobold.client.plam.model.AbstractStatus#getDescription()
         */
        public String getDescription()
        {
            return "{0} is a related component and points to a core asset";
        }

        /* (non-Javadoc)
         * @see kobold.client.plam.model.AbstractStatus#getIcon()
         */
        public ImageDescriptor getIcon()
        {
            return WorkbenchPlugin.getDefault().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UP);
        }
        
    }
}
