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
 * $Id: FileDescriptorEditPart.java,v 1.3 2004/09/01 03:23:31 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import kobold.client.plam.editor.figure.AbstractNodeFigure;
import kobold.client.plam.editor.figure.ReleaseFigure;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.editor.policy.FlowLayoutEditPolicyImpl;
import kobold.client.plam.editor.policy.GraphicalNodeEditPolicyImpl;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.edges.EdgeContainer;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;


/**
 * EditPart for Release instances.
 * 
 * @author Tammo
 */
public class FileDescriptorEditPart extends AbstractGraphicalEditPart
									implements PropertyChangeListener, NodeEditPart {

    private ReleaseFigure figure;
    private ChopboxAnchor anchor;
    private AbstractRootAsset root;
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies()
    {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new GraphicalNodeEditPolicyImpl());

		//installEditPolicy(EditPolicy.COMPONENT_ROLE, new ComponentEditPolicyImpl());
		//installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowLayoutEditPolicyImpl());
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    protected IFigure createFigure()
    {
        figure = new ReleaseFigure();
        figure.setTitle(((FileDescriptor)getModel()).getFilename());
        anchor = new ChopboxAnchor(figure);
        return figure;
    }

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();

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

    /**
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection)
    {
        return anchor;
    }

    /**
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
     */
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection)
    {
        return anchor;
    }

    /**
     * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getSourceConnectionAnchor(Request request)
    {
        return anchor;
    }

    /**
     * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
     */
    public ConnectionAnchor getTargetConnectionAnchor(Request request)
    {
        return anchor;
    }
    
	public FileDescriptor getFileDescriptor()
	{
	    return (FileDescriptor)getModel();
	}
	
    /**
	 * Updates the visuals. 
	 */
	protected void refreshVisuals() 
	{
		
		figure.setTitle((((FileDescriptor)getModel()).getFilename() == null) ? "" : getFileDescriptor().getFilename());
	}

	/**
     * @see org.eclipse.gef.EditPart#activate()
     */
    public void activate()
    {
        if (isActive() == false) {
            super.activate();
            getFileDescriptor().addPropertyChangeListener(this);
            root = getFileDescriptor().getParentAsset().getRoot();
            root.getEdgeContainer().addPropertyChangeListener(this);
        }
    }

	/**
     * @see org.eclipse.gef.EditPart#deactivate()
     */
    public void deactivate()
    {
        if (isActive()) {
            super.deactivate();
            getFileDescriptor().removePropertyChangeListener(this);
            if (root != null) {
                root.getEdgeContainer().removePropertyChangeListener(this);
            }
        }
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
     */
    protected List getModelSourceConnections()
    {
        AbstractRootAsset root = getFileDescriptor().getRoot();
        EdgeContainer ec = root.getEdgeContainer();
        return root.getEdgeContainer().getEdgesFrom(getFileDescriptor());
    }
    
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
     */
    protected List getModelTargetConnections()
    {
        AbstractRootAsset root = getFileDescriptor().getRoot();
        EdgeContainer ec = root.getEdgeContainer();
        return root.getEdgeContainer().getEdgesTo(getFileDescriptor());
    }
}
