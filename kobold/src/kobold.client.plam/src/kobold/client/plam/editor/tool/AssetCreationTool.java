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
 * $Id: AssetCreationTool.java,v 1.2 2004/08/06 01:30:02 vanto Exp $
 *
 */
package kobold.client.plam.editor.tool;

import kobold.client.plam.editor.dialog.AssetConfigurationDialog;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.jface.dialogs.Dialog;


/**
 * Provides an Asset Creation Tool for the Graphical Editor.
 * 
 * @author Tammo
 */
public class AssetCreationTool extends CreationTool
{

    /**
     * Constructs a new AbstractCreationTool with the given factory.
     * @param aFactory the creation factory
     */
    public AssetCreationTool(CreationFactory factory) {
    	super(factory);
    }
    
    /**
     * Creates the asset and shows the configuration dialog. If the dialog
     * gets canceled, an undo will be performed to get the previous state.
     * 
     * @see org.eclipse.gef.tools.CreationTool#performCreation(int)
     */
    protected void performCreation(int button)
    {
        Command thisCommand = getCurrentCommand();
        super.performCreation(button);
        
    	final Object model = getCreateRequest().getNewObject();
    	if ((thisCommand.canExecute()) && (model != null) && (model instanceof AbstractAsset) && !(model instanceof MetaNode)) {
    	    AssetConfigurationDialog dlg = new AssetConfigurationDialog(getCurrentViewer().getControl().getShell(), (AbstractAsset)model);
    	    CommandStack cs = getDomain().getCommandStack();
    	    if (dlg.open() == Dialog.CANCEL && cs.canUndo()
    	            && cs.getUndoCommand() == thisCommand) {
    	        cs.undo();
    	    }
    	}
    }
}
