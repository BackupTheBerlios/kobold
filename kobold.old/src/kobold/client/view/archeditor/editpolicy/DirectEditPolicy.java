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
 * $Id: DirectEditPolicy.java,v 1.1 2004/04/01 22:32:15 vanto Exp $
 *
 */
package kobold.client.view.archeditor.editpolicy;

import kobold.client.view.archeditor.command.RenameModelCommand;
import kobold.client.view.archeditor.figure.VariantFigure;
import kobold.client.view.archeditor.model.AbstractModel;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;

/**
 * DirectEditPolicy
 * 
 * @author Tammo van Lessen
 */
public class DirectEditPolicy
    extends org.eclipse.gef.editpolicies.DirectEditPolicy {

    /**
     * @see org.eclipse.gef.editpolicies.DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
     */
    protected Command getDirectEditCommand(DirectEditRequest request) {
        RenameModelCommand cmd = new RenameModelCommand();
        cmd.setModel((AbstractModel)getHost().getModel());
        cmd.setOldName(((AbstractModel)getHost().getModel()).getName());
        cmd.setName((String)request.getCellEditor().getValue());
        return cmd;
    }

    /**
     * @see org.eclipse.gef.editpolicies.DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
     */
    protected void showCurrentEditValue(DirectEditRequest request) {
        String value = (String)request.getCellEditor().getValue();
        ((VariantFigure)getHostFigure()).getLabel().setText(value);
    }

}
