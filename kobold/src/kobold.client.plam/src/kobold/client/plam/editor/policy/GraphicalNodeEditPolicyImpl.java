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
 * $Id: GraphicalNodeEditPolicyImpl.java,v 1.4 2004/07/27 16:23:11 vanto Exp $
 *
 */
package kobold.client.plam.editor.policy;

import kobold.client.plam.editor.command.ConnectionCommand;
import kobold.client.plam.editor.editpart.MetaEditPart;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.edges.INode;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * GraphicalNodeEditPolicy
 * 
 * @author Tammo van Lessen
 * @version $Id: GraphicalNodeEditPolicyImpl.java,v 1.4 2004/07/27 16:23:11 vanto Exp $
 */
public class GraphicalNodeEditPolicyImpl
    extends org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy {

    /**
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
     */
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        ConnectionCommand command = (ConnectionCommand)request.getStartCommand();

        // dont allow connection if there is already a connection between source and target
        // and of the same type.
        EdgeContainer ec = ((INode)getHost().getModel()).getRoot().getEdgeContainer();
        if (ec.containsEdge(command.getSourceNode(), (INode)getHost().getModel(), command.getType())) {
            return null;
        }

        // dont allow connection if source node is an ancestor of target
        AbstractAsset parent = (AbstractAsset)getHost().getModel();
        while (parent != null) {
            parent = parent.getParent();
            if (parent == command.getSourceNode()) {
                return null;
            }
        }

        // dont allow connection if target node is an ancestor of source
        parent = (AbstractAsset)command.getSourceNode();
        while (parent != null) {
            parent = parent.getParent();
            if (parent == (AbstractAsset)getHost().getModel()) {
                return null;
            }
        }
        
        command.setTargetNode((INode)getHost().getModel());

        return command;
    }

    /**
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
     */
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
    	
        if (getHost() instanceof MetaEditPart) {
            // dont allow edges from a meta node
    	    return null;
    	}
        
        ConnectionCommand command = new ConnectionCommand();
    	command.setSourceNode((INode)getHost().getModel());
        command.setType((String)request.getNewObjectType());
    	request.setStartCommand(command);
    	return command;
    }

    
    /**
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectTargetCommand(org.eclipse.gef.requests.ReconnectRequest)
     */
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        // TODO
        return null;
    }

    /**
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getReconnectSourceCommand(org.eclipse.gef.requests.ReconnectRequest)
     */
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        // TODO
        return null;
    }

}
