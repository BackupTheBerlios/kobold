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
 * $Id: AbstractNodeEditPart.java,v 1.2 2004/05/14 02:19:15 vanto Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import kobold.client.plam.editor.policy.ContainerEditPolicy;
import kobold.client.plam.editor.policy.GraphicalNodeEditPolicy;
import kobold.client.plam.editor.policy.XYLayoutEditPolicy;
import kobold.client.plam.model.pline.graph.AbstractNode;

import org.eclipse.draw2d.ConnectionAnchor;
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
 * @author Tammo
 */
public abstract class AbstractNodeEditPart extends AbstractGraphicalEditPart
	implements PropertyChangeListener, NodeEditPart {


	/**
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE,
			new GraphicalNodeEditPolicy());
		installEditPolicy(EditPolicy.CONTAINER_ROLE, new ContainerEditPolicy());
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new XYLayoutEditPolicy());
	}

	/**
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		System.out.println(evt.toString());
		if (AbstractNode.ID_CHILDREN.equals(prop))
			refreshChildren();
		else if (AbstractNode.ID_INPUTS.equals(prop))
			refreshTargetConnections();
		else if (AbstractNode.ID_OUTPUTS.equals(prop))
			refreshSourceConnections();
		else if (prop.equals(AbstractNode.ID_SIZE) || prop.equals(AbstractNode.ID_LOCATION))
			refreshVisuals();
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

	protected AbstractNode getNode()
	{
		return (AbstractNode)getModel();
	}
	
	/**
	 * Updates the visuals. 
	 */
	protected void refreshVisuals() {
		Point loc = getNode().getLocation();
		Dimension size= getNode().getSize();
		Rectangle r = new Rectangle(loc ,size);

		((GraphicalEditPart) getParent()).setLayoutConstraint(
			this,
			getFigure(),
			r);
	}
	

	/**
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	public void activate() {
		if (isActive() == false) {
			super.activate();
			((AbstractNode) getModel()).addPropertyChangeListener(this);
		}
	}

	/**
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	public void deactivate() {
		if (isActive()) {
		   super.deactivate();
		   ((AbstractNode) getModel()).removePropertyChangeListener(this);
		}
	}

}
