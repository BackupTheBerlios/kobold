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
 * $Id: DeleteCommand.java,v 1.7 2004/08/27 00:36:17 martinplies Exp $
 *
 */
package kobold.client.plam.editor.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.DeleteAsset;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.AbstractStatus;
import kobold.client.plam.model.DeprecatedStatus;
import kobold.client.plam.model.IComponentContainer;
import kobold.client.plam.model.IReleaseContainer;
import kobold.client.plam.model.IVariantContainer;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.edges.EdgeContainer;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;


/**
 * @author Tammo
 */
public class DeleteCommand extends Command
{
    private AbstractAsset asset;
    private int action;
    private DeleteAsset delAsset;
    
    public DeleteCommand()
    {
        super("delete command");
        delAsset = new DeleteAsset(); 
        
    }
    
    public void setAsset(AbstractAsset asset)
    {
         delAsset.setAsset(asset);
    }
    
    public void execute()
    {
        if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), 
            	"Deleted or Deprecated", 
            	"Are you sure to delete this asset? Press \"Yes\" to delete or \"No\" to mark the asset deprecated.")) {
            action = DeleteAsset.DELETE;
        } else {
            action = DeleteAsset.DEPRECATED;
        }
        
        delAsset.execute(action);
    }
    
    public void undo() {
        delAsset.undo();
    }


}
