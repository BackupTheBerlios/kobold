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
 * $Id: AbstractAssetEditPart.java,v 1.11 2004/09/01 03:23:31 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import kobold.client.plam.editor.dialog.AssetConfigurationDialog;
import kobold.client.plam.editor.figure.AbstractNodeFigure;
import kobold.client.plam.editor.figure.ReleaseFigure;
import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.editor.policy.ComponentEditPolicyImpl;
import kobold.client.plam.editor.policy.GraphicalNodeEditPolicyImpl;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.edges.EdgeContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Tammo
 */
public abstract class AbstractAssetEditPart extends AbstractGraphicalEditPart
	implements PropertyChangeListener, NodeEditPart {

    private static final Log logger = LogFactory.getLog(AbstractAssetEditPart.class);
    private ChopboxAnchor anchor;
    private AbstractRootAsset root;
    
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new GraphicalNodeEditPolicyImpl());

		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicyImpl());
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		final String prop = evt.getPropertyName();
		Display.getDefault().asyncExec(new Runnable() {

            public void run()
            {
        		if (AbstractAsset.ID_CHILDREN.equals(prop) 
        		        || AbstractAsset.ID_FILE_DESCRIPTORS.equals(prop)) {
        			refreshChildren();
        		} else if (EdgeContainer.ID_SOURCE_CHANGED.equals(prop)) {
        			refreshSourceConnections();
        		} else if (EdgeContainer.ID_TARGET_CHANGED.equals(prop)) {
        			refreshTargetConnections();
        		} else /*if (prop.equals(ViewModel.ID_SIZE) || prop.equals(ViewModel.ID_LOCATION) 
        		        || prop.equals(AbstractAsset.ID_DATA))*/ {
        			refreshVisuals();
        		}
            }
        });
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return anchor;
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return anchor;
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return anchor;
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return anchor;
	}

	protected AbstractAsset getAsset()
	{
		return (AbstractAsset)getModel();
	}
	
	protected ViewModel getViewModel()
	{
	    IViewModelProvider vmp = (IViewModelProvider)((DefaultEditDomain)getViewer().getEditDomain()).getEditorPart();
	    return vmp.getViewModelContainer().getViewModel(getAsset());
	}

	/**
	 * Updates the visuals. 
	 */
	protected void refreshVisuals() 
	{
	    ViewModel vm = getViewModel();
		Point loc = vm.getLocation();
		Dimension size= vm.getSize();
		Rectangle r = new Rectangle(loc ,size);

		((GraphicalEditPart) getParent()).setLayoutConstraint(
			this, getFigure(), r);
		
		IFigure f = getFigure();
		if (f instanceof AbstractNodeFigure) {
		    ((AbstractNodeFigure)f).setStatusSet(getAsset().getStatusSet());
		} else if (f instanceof ReleaseFigure) {
		    ((ReleaseFigure)f).setStatusSet(getAsset().getStatusSet());
		}
	}

	/**
     * @see org.eclipse.gef.EditPart#activate()
     */
    public void activate()
    {
        if (isActive() == false) {
            super.activate();
            getAsset().addPropertyChangeListener(this);
            root = getAsset().getRoot();
            root.getEdgeContainer().addPropertyChangeListener(this);
            getViewModel().addPropertyChangeListener(this);
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
            if (root != null) {
                root.getEdgeContainer().removePropertyChangeListener(this);
            }
            getViewModel().removePropertyChangeListener(this);
        }
    }

    protected final IFigure createFigure()
    {
    	IFigure fig = createNodeFigure();
        anchor = new ChopboxAnchor(fig);
        return fig;
    }
    
    protected abstract IFigure createNodeFigure();
    
    public void performRequest(Request req)
    {
        if (req.getType() == RequestConstants.REQ_OPEN) {
            final AbstractAsset asset = getAsset();
            if (asset instanceof MetaNode) {
                super.performRequest(req);
                return;
            }
            getViewer().getControl().getDisplay().asyncExec(new Runnable() {
                public void run()
                {
                    Shell shell = getViewer().getControl().getShell();
                    AssetConfigurationDialog dialog = new AssetConfigurationDialog(shell, asset);
                    dialog.open();
                }
            });
        } else {
            super.performRequest(req);
        }
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
     */
    protected List getModelSourceConnections()
    {
        AbstractAsset asset = getAsset();
        AbstractRootAsset root = asset.getRoot();
        EdgeContainer ec = root.getEdgeContainer();
        List l = ec.getEdgesFrom(asset);
        return asset.getRoot().getEdgeContainer().getEdgesFrom(asset);
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
     */
    protected List getModelTargetConnections()
    {
        AbstractAsset asset = getAsset();
        return asset.getRoot().getEdgeContainer().getEdgesTo(asset);
    }
}
