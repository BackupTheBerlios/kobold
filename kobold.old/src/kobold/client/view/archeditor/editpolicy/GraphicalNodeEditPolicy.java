/*
 * gef-talk
 * TODO
 * Created on 21.02.2004
 *
 */
package kobold.client.view.archeditor.editpolicy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

/**
 * GraphicalNodeEditPolicy
 * 
 * @author Tammo van Lessen
 * @version $Id: GraphicalNodeEditPolicy.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 */
public class GraphicalNodeEditPolicy
    extends org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy {

    /**
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCompleteCommand(org.eclipse.gef.requests.CreateConnectionRequest)
     */
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        // TODO
        return null;
    }

    /**
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#getConnectionCreateCommand(org.eclipse.gef.requests.CreateConnectionRequest)
     */
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        // TODO
        return null;
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
