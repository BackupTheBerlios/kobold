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
 * $Id: XYLayoutEditPolicy.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.editpolicy;

import kobold.client.view.archeditor.command.CreateComponentCommand;
import kobold.client.view.archeditor.command.SetConstraintCommand;
import kobold.client.view.archeditor.model.ArchitectureModel;
import kobold.client.view.archeditor.model.ComponentModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

/**
 * XYLayoutEditPolicy
 * 
 * @author Tammo van Lessen
 */
public class XYLayoutEditPolicy
    extends org.eclipse.gef.editpolicies.XYLayoutEditPolicy {

    /**
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart, java.lang.Object)
     */
    protected Command createAddCommand(EditPart child, Object constraint) {
        return null;
    }

    /**
     * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChangeConstraintCommand(org.eclipse.gef.EditPart, java.lang.Object)
     */
    protected Command createChangeConstraintCommand(
        EditPart child,
        Object constraint) {
            SetConstraintCommand locationCommand = new SetConstraintCommand();

            locationCommand.setEditPart(child);
            locationCommand.setRect((Rectangle) constraint);

            // System.out.println("HelloXYLayoutEditPolicy.createChangeConstraintCommand() return command="+locationCommand);
            return locationCommand;
    }

    /**
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(CreateRequest request) {
        Command create = null;
        String type = (String)request.getNewObjectType();
        if ((getHost().getModel() instanceof ArchitectureModel)
            && (type.equals("component"))) {
            ArchitectureModel arch = (ArchitectureModel)getHost().getModel();
            //ComponentModel comp = new ComponentModel("New");
            ComponentModel comp = (ComponentModel)request.getNewObject();
            create = new CreateComponentCommand(arch, comp);
        }
        
        return create;
    }

    /**
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
     */
    protected Command getDeleteDependantCommand(Request request) {
        // TODO
        return null;
    }

    /**
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(EditPart)
     */
    protected EditPolicy createChildEditPolicy(EditPart child)
    {
        // return new NonResizableEditPolicy();
        return new ResizableEditPolicy();
    }
    
}
