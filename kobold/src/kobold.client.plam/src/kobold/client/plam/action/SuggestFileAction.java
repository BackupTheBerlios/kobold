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
 * $Id: SuggestFileAction.java,v 1.4 2004/08/24 18:46:24 garbeam Exp $
 *
 */
package kobold.client.plam.action;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.workflow.CoreGroupDialog;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Shell;

/**
 * @author dometine
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SuggestFileAction extends Action {
	
	private Shell shell;
    private AbstractAsset selection;
    
    public SuggestFileAction(Shell shell) 
    {
        this.shell = shell;
		setText("Core Group Suggestion");
		setToolTipText("Suggest Asset To Core Group");
		setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/wflow.gif"));

    }
    
    public void run()
    {
    	WorkflowMessage msg = new WorkflowMessage("Core Group Suggestion");
		try {
			CoreGroupDialog cgDialog = new CoreGroupDialog(shell, selection);
			cgDialog.open();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    
    
    public void handleSelectionChanged(SelectionChangedEvent event)
    {
        IStructuredSelection sel = (IStructuredSelection)event.getSelection();
        if (sel.size() == 1 && sel.getFirstElement() instanceof AbstractAsset
                && !(sel.getFirstElement() instanceof MetaNode)) {
            setEnabled(true);
            selection = (AbstractAsset)sel.getFirstElement();
        } else {
            setEnabled(false);
            selection = null;
        }
    }
}
