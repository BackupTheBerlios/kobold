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
 * $Id: deleteAction.java,v 1.1 2004/08/27 00:36:17 martinplies Exp $
 *
 */
package kobold.client.plam.action;

import kobold.client.plam.DeleteAsset;
import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.workflow.CoreGroupDialog;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author pliesmn
 *
 * Excute actions of DeleteAction and DeleteCommand.
 */
public class deleteAction extends Action {
	
	private Shell shell;
    private AbstractAsset selection;
    private DeleteAsset delAsset = new DeleteAsset();
    
    public deleteAction(Shell shell) 
    {
        this.shell = shell;
		setText("delete asset");
    }
    
    public void run()
    {        
        if (MessageDialog.openQuestion(Display.getDefault().getActiveShell(), 
            	"Deleted or Deprecated", 
            	"Are you sure to delete this asset? Press \"Yes\" to delete or \"No\" to mark the asset deprecated.")) {
            delAsset.execute(DeleteAsset.DELETE);
        } else {
            delAsset.execute(DeleteAsset.DEPRECATED);
        }
    }    
    
    public void handleSelectionChanged(SelectionChangedEvent event)
    {
        IStructuredSelection sel = (IStructuredSelection)event.getSelection();
        if (sel.size() == 1 && sel.getFirstElement() instanceof AbstractAsset){
            setEnabled(true);
            delAsset.setAsset((AbstractAsset)sel.getFirstElement());
        } else {
            setEnabled(false);
            delAsset.setAsset(null);
        }
    }
}
