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
 * $Id: ContainerEditPolicy.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 *
 */
package kobold.client.plam.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * ContainerEditPolicy
 * 
 * @author Tammo van Lessen
 * @version $Id: ContainerEditPolicy.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 */
public class ContainerEditPolicy
    extends org.eclipse.gef.editpolicies.ContainerEditPolicy {

    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(CreateRequest request) {
        // TODO
        return null;
    }

    /**
     * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    /*protected Command getCreateCommand(CreateRequest request) {
        Command create = null;
        String type = (String)request.getNewObjectType();
        if ((getHost().getModel() instanceof VariantNode) 
                   && (type.equals("component"))) {

            VariantNode variant = (VariantNode)getHost().getModel();
            ComponentNode comp = (ComponentNode)request.getNewObject();

            create = new CreateComponentCommand(variant, comp);
        } else if ((getHost().getModel() instanceof ComponentNode)
                   && (type.equals("variant"))) {

            ComponentNode component = (ComponentNode)getHost().getModel();
            VariantNode variant = (VariantNode)request.getNewObject();

            create = new CreateVariantCommand(component, variant);
        } else {
            return null;
        } 

        return create;
    }*/
}
