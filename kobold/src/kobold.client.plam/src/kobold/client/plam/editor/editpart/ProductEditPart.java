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
 * $Id: ProductEditPart.java,v 1.6 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import org.apache.log4j.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import kobold.client.plam.editor.model.AssetView;
import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.policy.ProductComponentContainerEditPolicy;
import kobold.client.plam.editor.policy.XYLayoutEditPolicyImpl;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.product.Product;

import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Font;


/**
 * ProductlineEditPart
 * 
 * @author Tammo van Lessen
 * @version $Id: ProductEditPart.java,v 1.6 2004/11/05 10:32:32 grosseml Exp $
 */
public class ProductEditPart extends AbstractGraphicalEditPart
        implements  PropertyChangeListener {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ProductEditPart.class);

    /** Singleton instance of MarqueeDragTracker. */
    static DragTracker dragTracker = null;
    
    private Label plNameLabel;
    private Label plDescLabel;

    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    protected IFigure createFigure() {
        Figure f = new FreeformLayer();
        f.setLayoutManager(new FreeformLayout());

        // Don't know why, but if you don't setOpaque(true), you cannot move by drag&drop!
        f.setOpaque(true);
        
        plNameLabel = new Label();
        plNameLabel.setLabelAlignment(Label.LEFT);
		Font font = JFaceResources.getBannerFont();
		plNameLabel.setFont(font);
        plNameLabel.setLocation(new Point(5,5));
		plNameLabel.setForegroundColor(ColorConstants.black);
        f.add(plNameLabel);

        plDescLabel = new Label();
        plDescLabel.setLabelAlignment(Label.LEFT);
		font = JFaceResources.getDefaultFont();
		plDescLabel.setFont(font);
		plDescLabel.setForegroundColor(ColorConstants.gray);
        f.add(plDescLabel);

        return f;
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicyImpl());
        installEditPolicy(EditPolicy.CONTAINER_ROLE, new ProductComponentContainerEditPolicy());
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals(AbstractAsset.ID_CHILDREN) 
            || prop.equals(AbstractAsset.ID_META)) {
            refreshChildren();
        } else {
            refreshVisuals();
        }
    }

    /**
     * This method is not mandatory to implement, but if you do not implement
     * it, you will not have the ability to rectangle-selects several
     * figures...
     *
     * @param req DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DragTracker getDragTracker(Request req)
    {
        // Unlike in Logical Diagram Editor example, I use a singleton because this 
        // method is called several time, so I prefer to save memory ; and it works!
        if (dragTracker == null) {
            dragTracker = new MarqueeDragTracker();
        }

        return dragTracker;
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren() You
     *      must implement this method if you want you root model to have
     *      children!
     */
    protected List getModelChildren()
    {
        List children = new ArrayList(); 
        children.addAll(((Product)getModel()).getRelatedComponents());
        children.addAll(((Product)getModel()).getSpecificComponents());
        return children;
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
     */
    protected void refreshVisuals()
    {
        String name = ((AbstractRootAsset)getModel()).getName(); 
        if (name != null) {
            plNameLabel.setText(((AbstractRootAsset)getModel()).getName());
    		plNameLabel.setSize(FigureUtilities.getTextExtents(name, plNameLabel.getFont()));
    		plNameLabel.setVisible(true);
        } else {
            plNameLabel.setVisible(false);
        }

        String desc = ((AbstractRootAsset)getModel()).getDescription();
        if (desc != null) {
            plDescLabel.setText(desc);
            plDescLabel.setLocation(new Point(5, 5 + plNameLabel.getSize().height));
            plDescLabel.setSize(FigureUtilities.getTextExtents(desc, plDescLabel.getFont()));
            plDescLabel.setVisible(true);
        } else {
            plDescLabel.setVisible(false);
        }

        
        ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);

        // It seems that Manhanttan routing cannot be used with bendpoints...
        if (false) {
            cLayer.setConnectionRouter(new ManhattanConnectionRouter());
        } else {
            AutomaticRouter router = new FanRouter();
            router.setNextRouter(new BendpointConnectionRouter());
            cLayer.setConnectionRouter(router);
        }
    }

	protected AbstractAsset getAsset()
	{
		return (AbstractAsset)getModel();
	}
	
	private AssetView getViewModel()
	{
	    IViewModelProvider vmp = (IViewModelProvider)((DefaultEditDomain)getViewer().getEditDomain()).getEditorPart();
	    return vmp.getViewModel().getAssetView(getAsset());
	}
    
    /**
     * @see org.eclipse.gef.EditPart#activate()
     */
    public void activate()
    {
        if (isActive() == false) {
            super.activate();
            getAsset().addPropertyChangeListener(this);
        }
    }

    /**
     * @see org.eclipse.gef.EditPart#deactivate()
     */
    public void deactivate()
    {
        if (isActive()) {
            super.deactivate();
            getAsset().removePropertyChangeListener(this);
        }
    }
}
