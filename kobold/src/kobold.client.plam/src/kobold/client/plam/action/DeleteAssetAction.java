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
 * $Id: DeleteAssetAction.java,v 1.6 2004/09/23 13:43:19 vanto Exp $
 *
 */
package kobold.client.plam.action;

import kobold.client.plam.editor.command.DeleteAssetCommand;
import kobold.client.plam.editor.dialog.DeleteDeprecatedDialog;
import kobold.client.plam.model.AbstractAsset;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * @author pliesmn
 *
 * Excute actions of DeleteAction and DeleteCommand.
 */
public class DeleteAssetAction extends ActionDelegate {
	
//    private ISelection selection;
    private AbstractAsset selection;
    
    public void run(IAction action)
    
    { 
    	DeleteAssetCommand deleteCommand = new DeleteAssetCommand();
    	deleteCommand.setAsset(selection);  

    	Shell shell = Display.getDefault().getActiveShell();
  		DeleteDeprecatedDialog dialog = new DeleteDeprecatedDialog(shell, deleteCommand); 
  		dialog.open();
  		
//    	DeleteAssetCommand deleteCommand = new DeleteAssetCommand();
//        deleteCommand.setSelection(selection);
//        Shell shell = Display.getDefault().getActiveShell();
//		DeleteDeprecatedDialog dialog = new DeleteDeprecatedDialog(shell, deleteCommand); 
//		dialog.open();
	
    }
     
    /**
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
       IStructuredSelection sel = (StructuredSelection)selection;
       
       boolean enable = true;
       this.selection = null;
       
       

//       for (Iterator it = sel.iterator(); it.hasNext();) {
//       	   if (!(it.next() instanceof AbstractAsset)) {
//       	   		enable = false;
//       	   }
//       }
       if (sel.getFirstElement() instanceof AbstractAsset) {
        this.selection = (AbstractAsset)sel.getFirstElement();
        enable = true;
		}
	      
//       if (enable) {
//           this.selection = sel;
//       }
       if (action != null) {
           action.setEnabled(enable);
       }
    }

}
