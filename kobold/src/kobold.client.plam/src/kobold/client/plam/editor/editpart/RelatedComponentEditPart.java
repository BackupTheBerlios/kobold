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
 * $Id: RelatedComponentEditPart.java,v 1.12 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kobold.client.plam.editor.figure.RelatedComponentFigure;
import kobold.client.plam.editor.policy.ProductComponentContainerEditPolicy;
import kobold.client.plam.editor.policy.XYLayoutEditPolicyImpl;
import kobold.client.plam.model.AbstractStatus;
import kobold.client.plam.model.product.RelatedComponent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.internal.WorkbenchPlugin;


/**
 * @author Tammo
 */
public class RelatedComponentEditPart extends AbstractAssetEditPart
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(RelatedComponentEditPart.class);

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
        figure.setDescription(((RelatedComponent)getAsset()).getRelatedVariant().getName()
            + ": " + ((RelatedComponent)getAsset()).getRelatedRelease().getName());
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
     * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
     */
    public IFigure getFileDescriptorPane() {
        return figure.getFileDescriptorPane();
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies()
    {
        super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyImpl());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new ProductComponentContainerEditPolicy());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
     */
    protected List getModelChildren()
    {
        List children = new ArrayList(); 
        children.addAll(((RelatedComponent)getModel()).getProductComponents());
        children.addAll(((RelatedComponent)getModel()).getFileDescriptors());
        return children;
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#addChildVisual(org.eclipse.gef.EditPart, int)
     */
    protected void addChildVisual(EditPart childEditPart, int index)
    {
    	IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
        if (childEditPart instanceof FileDescriptorEditPart) {
            //int i = ((ProductComponent)getAsset()).getFileDescriptors().indexOf(((FileDescriptorEditPart)childEditPart).getModel());
            //getFileDescriptorPane().add(child, i);
            getFileDescriptorPane().add(child);
    	} else {
    	    getContentPane().add(child, index);
    	}
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#removeChildVisual(org.eclipse.gef.EditPart)
     */
    protected void removeChildVisual(EditPart childEditPart)
    {
        IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
        if (childEditPart instanceof FileDescriptorEditPart) {
            getFileDescriptorPane().remove(child);
    	} else {
           	getContentPane().remove(child);
    	}
    }
    
    /**
     * @see org.eclipse.gef.GraphicalEditPart#setLayoutConstraint(org.eclipse.gef.EditPart, org.eclipse.draw2d.IFigure, java.lang.Object)
     */
    public void setLayoutConstraint(EditPart child, IFigure childFigure,
            Object constraint)
    {
        if (child instanceof FileDescriptorEditPart) {
            getFileDescriptorPane().setConstraint(childFigure, constraint);
    	} else {
    	    getContentPane().setConstraint(childFigure, constraint);
    	}
    }

    protected void reorderChild(EditPart child, int index)
    {
    	IFigure childFigure = ((GraphicalEditPart) child).getFigure();
    	LayoutManager layout = null;
    	if (child instanceof FileDescriptorEditPart) {
    	    layout = getFileDescriptorPane().getLayoutManager();
        } else {
            layout = getContentPane().getLayoutManager();
        }
    	Object constraint = null;
    	if (layout != null)
    		constraint = layout.getConstraint(childFigure);
    	
    	removeChildVisual(child);
    	List children = getChildren();
    	children.remove(child);
    	children.add(index, child);
    	addChildVisual(child, index);
    	
    	setLayoutConstraint(child, childFigure, constraint);
    }
    
    private class RelatedStatus extends AbstractStatus 
    {
		/**
		 * Logger for this class
		 
		private static final Logger logger = Logger
				.getLogger(RelatedStatus.class);
        */
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
