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
 * $Id: XYLayoutEditPolicyImpl.java,v 1.2 2004/08/23 14:36:58 vanto Exp $
 *
 */
package kobold.client.plam.editor.policy;

import kobold.client.plam.editor.command.SetConstraintCommand;
import kobold.client.plam.editor.editpart.MetaEditPart;
import kobold.client.plam.editor.model.IViewModelProvider;
import kobold.client.plam.editor.model.ViewModel;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
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
 * @version $Id: XYLayoutEditPolicyImpl.java,v 1.2 2004/08/23 14:36:58 vanto Exp $
 */
public class XYLayoutEditPolicyImpl 
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
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        SetConstraintCommand locationCommand = new SetConstraintCommand();
	    IViewModelProvider vmp = (IViewModelProvider)((DefaultEditDomain)getHost().getViewer().getEditDomain()).getEditorPart();
		ViewModel vp = vmp.getViewModelContainer().getViewModel((AbstractAsset)child.getModel());
		locationCommand.setViewModel(vp);

		// dont resize meta nodes
        if (child instanceof MetaEditPart) {
            ((Rectangle)constraint).setSize(-1, -1);
        }

		locationCommand.setLocation((Rectangle)constraint);
		return locationCommand;
    }

    /**
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
     */
    protected Command getCreateCommand(CreateRequest request) {
        SetConstraintCommand locationCommand = new SetConstraintCommand();

	    IViewModelProvider vmp = (IViewModelProvider)((DefaultEditDomain)getHost().getViewer().getEditDomain()).getEditorPart();
		ViewModel vp = vmp.getViewModelContainer().getViewModel((AbstractAsset)request.getNewObject());
		locationCommand.setViewModel(vp);
		Rectangle constraint = (Rectangle)getConstraintFor(request);

		// dont resize meta nodes
        if (request.getNewObject() instanceof MetaNode) {
            constraint.setSize(-1, -1);
        }

		locationCommand.setLocation(constraint);
		return locationCommand;
    }

    /**
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#getDeleteDependantCommand(org.eclipse.gef.Request)
     */
    protected Command getDeleteDependantCommand(Request request) {
        return null;
    }

    /**
     * @see org.eclipse.gef.editpolicies.LayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
     */
    protected EditPolicy createChildEditPolicy(EditPart child) {
		return new ResizableEditPolicy();
    }

}
