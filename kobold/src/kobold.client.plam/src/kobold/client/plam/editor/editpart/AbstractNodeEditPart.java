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
 * $Id: AbstractNodeEditPart.java,v 1.10 2004/07/22 16:42:23 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import kobold.client.plam.editor.dialog.AssetConfigurationDialog;
import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.editor.policy.ComponentEditPolicy;
import kobold.client.plam.editor.policy.ComposeEditPolicy;
import kobold.client.plam.editor.policy.GraphicalNodeEditPolicy;
import kobold.client.plam.editor.policy.XYLayoutEditPolicy;
import kobold.client.plam.model.AbstractAsset;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.eclipse.swt.widgets.Shell;

/**
 * @author Tammo
 */
public abstract class AbstractNodeEditPart extends AbstractGraphicalEditPart
	implements PropertyChangeListener, NodeEditPart {

    private static final Log logger = LogFactory.getLog(AbstractNodeEditPart.class);
    
	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new GraphicalNodeEditPolicy());
		//installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy());
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicy());
		installEditPolicy("composer", new ComposeEditPolicy());
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();

		if (AbstractAsset.ID_CHILDREN.equals(prop)) {
			refreshChildren();
		}
		//else if (AbstractAsset.ID_INPUTS.equals(prop))
		//	refreshTargetConnections();
		//else if (AbstractAsset.ID_OUTPUTS.equals(prop))
		//	refreshSourceConnections();
		else if (prop.equals(ViewModel.ID_SIZE) || prop.equals(ViewModel.ID_LOCATION) 
		    	|| prop.equals(AbstractAsset.ID_DATA)) {
			refreshVisuals();
		}
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return null;
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return null;
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return null;
	}

	/**
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return null;
	}

	protected AbstractAsset getAsset()
	{
		return (AbstractAsset)getModel();
	}
	
	private ViewModel getViewModel()
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
	}
	

	
	/**
     * @see org.eclipse.gef.EditPart#activate()
     */
    public void activate()
    {
        if (isActive() == false) {
            super.activate();
            getAsset().addPropertyChangeListener(this);
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
            getViewModel().removePropertyChangeListener(this);
        }
    }

    protected IFigure createFigure()
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void performRequest(Request req)
    {
        if (req.getType() == RequestConstants.REQ_OPEN) {
            final AbstractAsset asset = getAsset();
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
}
