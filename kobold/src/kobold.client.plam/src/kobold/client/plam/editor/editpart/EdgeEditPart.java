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
 * $Id: EdgeEditPart.java,v 1.5 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor.editpart;

import org.apache.log4j.Logger;

import kobold.client.plam.editor.policy.ConnectionEditPolicyImpl;
import kobold.client.plam.model.edges.Edge;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;


/**
 * @author Tammo
 */
public class EdgeEditPart extends AbstractConnectionEditPart 
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(EdgeEditPart.class);

    protected IFigure createFigure()
	{
		PolylineConnection fig = new PolylineConnection();
		PolygonDecoration dec = new PolygonDecoration();
		fig.setTargetDecoration(dec);

		return fig;
	}
	
    protected void refreshVisuals()
	{
		Edge edge = (Edge)getModel();
		PolylineConnection fig = (PolylineConnection)getConnectionFigure();
		if (edge.getType().equals(Edge.INCLUDE)) {
		    fig.setForegroundColor(ColorConstants.green);
		} else if (edge.getType().equals(Edge.EXCLUDE)) {
		    fig.setForegroundColor(ColorConstants.red);
		} else {
		    fig.setForegroundColor(ColorConstants.lightGray);
		}
	}
    
    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    protected void createEditPolicies()
    {
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicyImpl());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
		//installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new BendpointEditPolicyImpl());
    }
}
