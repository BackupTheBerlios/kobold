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
 * $Id: VariantEditPart.java,v 1.14 2004/09/01 01:08:29 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.editor.figure.VariantFigure;
import kobold.client.plam.editor.policy.VariantContainerEditPolicy;
import kobold.client.plam.editor.policy.XYLayoutEditPolicyImpl;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

/**
 * VariantEditPart
 * 
 * @author Tammo van Lessen
 * @version $Id: VariantEditPart.java,v 1.14 2004/09/01 01:08:29 vanto Exp $
 */
public class VariantEditPart extends AbstractComposableEditPart 
{
    private VariantFigure figure;

    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    protected IFigure createNodeFigure() {
        figure = new VariantFigure();
		figure.setTitle(getAsset().getName());
    
        return figure;
    }
    
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren() You
     *      must implement this method if you want you root model to have
     *      children!
     */
    protected List getModelChildren()
    {
        List children = new ArrayList(); 
        children.addAll(((IComponentContainer)getModel()).getComponents());
        children.addAll(((IReleaseContainer)getModel()).getReleases());
        return children;
    }
    
    /**
     * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
     */
    public IFigure getContentPane() {
        return figure.getContentPane();
    }
    
    public IFigure getReleasePane() {
        return figure.getReleasePane();
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies()
    {
        super.createEditPolicies();
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyImpl());
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new VariantContainerEditPolicy());
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#addChildVisual(org.eclipse.gef.EditPart, int)
     */
    protected void addChildVisual(EditPart childEditPart, int index)
    {
    	IFigure child = ((GraphicalEditPart)childEditPart).getFigure();
        if (childEditPart instanceof ReleaseEditPart) {
            int i = ((Variant)getAsset()).getReleases().indexOf(((ReleaseEditPart)childEditPart).getAsset());
            getReleasePane().add(child, i);
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
        if (childEditPart instanceof ReleaseEditPart) {
            getReleasePane().remove(child);
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
        if (child instanceof ReleaseEditPart) {
            getReleasePane().setConstraint(childFigure, constraint);
    	} else {
    	    getContentPane().setConstraint(childFigure, constraint);
    	}
    }
    
    protected void reorderChild(EditPart child, int index)
    {
    	IFigure childFigure = ((GraphicalEditPart) child).getFigure();
    	LayoutManager layout = null;
    	if (child instanceof ReleaseEditPart) {
    	    layout = getReleasePane().getLayoutManager();
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
    
    protected void refreshVisuals()
    {
        super.refreshVisuals();
        figure.setTitle((getAsset().getName() == null) ? "" : getAsset().getName());
    }
}
