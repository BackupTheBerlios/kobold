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
 * $Id: KoboldTemplateTransferDropTargetListener.java,v 1.4 2004/08/05 12:57:04 rendgeor Exp $
 *
 */
package kobold.client.plam.editor;

import kobold.client.plam.editor.dialog.AssetConfigurationDialog;
import kobold.client.plam.editor.model.KoboldAssetFactory;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.dnd.DND;


/**
 * This listener handles asset templates that are dropped onto
 * the logic editor.
 */
public class KoboldTemplateTransferDropTargetListener extends
        TemplateTransferDropTargetListener
{

    /**
     * Creates a new TransferDropTargetListener with the given viewer.
     * 
     * @param viewer
     */
    public KoboldTemplateTransferDropTargetListener(EditPartViewer viewer) 
    {
    	super(viewer);
    }
    
    /**
     * Creates a new Asset using the KoboldAssetFactory.
     * 
     * @see org.eclipse.gef.dnd.TemplateTransferDropTargetListener#getFactory(java.lang.Object)
     */
    protected CreationFactory getFactory(Object template) 
    {
    	if (template instanceof String) {
    		return new KoboldAssetFactory((String)template);
    	}
    	return null;
    }

    /**
     * Handles the asset drop and shows the asset dialog only if the command is valid.
     * 
     * For MetaNodes no dialog will be displayed. 
     * 
     * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
     */
    protected void handleDrop()
    {
    	updateTargetRequest();
    	updateTargetEditPart();
    	
    	if (getTargetEditPart() != null) {
    		Command command = getCommand();
    		if (command != null && command.canExecute()) {
    			getViewer().getEditDomain().getCommandStack().execute(command);
    			
    			final Object model = getCreateRequest().getNewObject();
    			if ((model != null) && (model instanceof AbstractAsset) 
    			        && !(model instanceof MetaNode)) {
    			    CommandStack cs = getViewer().getEditDomain().getCommandStack();
    			    
    			    AssetConfigurationDialog dlg = new AssetConfigurationDialog(getViewer().getControl().getShell(), (AbstractAsset)model);
    			    if (dlg.open() == Dialog.CANCEL && cs.canUndo()
    			            && cs.getUndoCommand() == command) {
    			        cs.undo();
    			        
    			    }
    			}
    		} else {
    			getCurrentEvent().detail = DND.DROP_NONE;
    		}
    	} else {
    		getCurrentEvent().detail = DND.DROP_NONE;
    	}
    	selectAddedObject();
    }
    
    /* CPL */
    private void selectAddedObject() {
    	Object model = getCreateRequest().getNewObject();
    	if (model == null)
    		return;
    	EditPartViewer viewer = getViewer();
    	viewer.getControl().forceFocus();
    	Object editpart = viewer.getEditPartRegistry().get(model);
    	if (editpart instanceof EditPart) {
    		//Force a layout first.
    		getViewer().flush();
    		viewer.select((EditPart)editpart);
    	}
    }
}
