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
 * $Id: ProductComposerTool.java,v 1.5 2004/08/05 20:42:31 vanto Exp $
 *
 */
package kobold.client.plam.editor.tool;

import kobold.client.plam.editor.ArchitectureEditor;
import kobold.client.plam.editor.KoboldGraphicalViewer;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.TargetingTool;


/**
 * Provides a tool to compose products out of the productline architecture.
 * 
 * @author Tammo
 */
public class ProductComposerTool extends TargetingTool
{

    public static final String REQ_COMPOSE = "compose";
    
    /**
     * Default constructor.  Sets the disabled cursors.
     */
    public ProductComposerTool() {
    	setDisabledCursor(SharedCursors.NO);
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
     */
    protected String getCommandName()
    {
        return REQ_COMPOSE;
    }

    /**
     * @see org.eclipse.gef.tools.AbstractTool#getDebugName()
     */
    protected String getDebugName()
    {
        return "Product Compose Tool";
    }

    protected Request createTargetRequest()
    {
    	ProductComposerRequest request = new ProductComposerRequest();
    	request.setType(getCommandName());
    	return request;
    }
    
    /**
     * Sets the modifiers , type and location of the target request (which is a 
     * {@link SelectionRequest}) and then calls {@link #updateHoverRequest()}.
     * @see org.eclipse.gef.tools.TargetingTool#updateTargetRequest()
     */
    protected void updateTargetRequest() {
    	ProductComposerRequest request = (ProductComposerRequest)getTargetRequest();
    	request.setControlKeyPressed(getCurrentInput().isControlKeyDown());
    	request.setType(getCommandName());
    	request.setLocation(getLocation());
    }
    
    /**
     * Updates the request and mouse target, gets the current command and asks to show
     * feedback.
     * @see org.eclipse.gef.tools.AbstractTool#handleMove()
     */
    protected boolean handleMove() {
    	updateTargetRequest();
    	updateTargetUnderMouse();
    	setCurrentCommand(getCommand());
    	//showTargetFeedback();
    	return true;
    }

    protected boolean handleButtonDown(int button)
    {
    	//resetHover();
    	EditPartViewer viewer = getCurrentViewer();
    	Point p = getLocation();

    	updateTargetRequest();

//    	((ProductComposerRequest)getTargetRequest()).setLastButtonPressed(button);
    	updateTargetUnderMouse();
    	EditPart editpart = getTargetEditPart();
    	if (editpart != null) {
    		lockTargetEditPart(editpart);
			Command command = getCommand();
			if (command != null)
				setCurrentCommand(command);

    		return true;
    	}
    	return false;
    }

    protected boolean handleButtonUp(int button)
    {
        if (getCurrentInput().isAnyButtonDown())
            return false;
        //((ProductComposerRequest)getTargetRequest()).setLastButtonPressed(0);
        setState(STATE_TERMINAL);
        unlockTargetEditPart();
        executeCurrentCommand();
        return true;
    }
    
    protected EditPartViewer.Conditional getTargetingConditional() {
    	return new EditPartViewer.Conditional() {
    		public boolean evaluate(EditPart editpart) {
    		    return editpart.isSelectable();
    		}
    	};
    }
    
    public void deactivate()
    {
        super.deactivate();
        
        ArchitectureEditor ae = (ArchitectureEditor)((DefaultEditDomain)getDomain()).getEditorPart();
        ((KoboldGraphicalViewer)ae.getAdapter(GraphicalViewer.class)).setComposing(false);
    }
    
    public void activate()
    {
        super.activate();
        ArchitectureEditor ae = (ArchitectureEditor)((DefaultEditDomain)getDomain()).getEditorPart();
        ((KoboldGraphicalViewer)ae.getAdapter(GraphicalViewer.class)).setComposing(true);
    }
}
