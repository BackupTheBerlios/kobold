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
 * $Id: ComposeEditPolicy.java,v 1.5 2004/08/04 08:50:12 vanto Exp $
 *
 */
package kobold.client.plam.editor.policy;

import kobold.client.plam.editor.ArchitectureEditor;
import kobold.client.plam.editor.KoboldGraphicalViewer;
import kobold.client.plam.editor.command.ProductComposerCommand;
import kobold.client.plam.editor.tool.ProductComposer;
import kobold.client.plam.editor.tool.ProductComposerRequest;
import kobold.client.plam.editor.tool.ProductComposerTool;
import kobold.client.plam.model.AbstractAsset;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;


/**
 * An EditPolicy for the product composing usecase.
 * 
 * @author Tammo
 */
public class ComposeEditPolicy extends AbstractEditPolicy
{

    public Command getCommand(Request request)
    {
    	if (ProductComposerTool.REQ_COMPOSE.equals(request.getType())) {
    	    return getProductComposerCommand(((ProductComposerRequest)request).isControlKeyPressed());
    	} 
    	return null;
    }
    
    protected Command getProductComposerCommand(boolean isCtrlKeyPressed)
    {
        ArchitectureEditor ae = (ArchitectureEditor)((DefaultEditDomain)getHost().getViewer().getEditDomain()).getEditorPart();
        ProductComposer comp = ((KoboldGraphicalViewer)ae.getAdapter(GraphicalViewer.class)).getProductComposer();
        return new ProductComposerCommand((AbstractAsset)getHost().getModel(), comp, isCtrlKeyPressed);
    }
    
    public EditPart getTargetEditPart(Request request)
    {
    	if (ProductComposerTool.REQ_COMPOSE.equals(request.getType())) {
    			return getHost();
    	}
    	return null;
    }
}
