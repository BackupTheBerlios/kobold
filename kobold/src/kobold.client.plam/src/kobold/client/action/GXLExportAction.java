/*
 * Created on 28.06.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.action;

import kobold.client.plam.wizard.GXLExportDialog;
import kobold.common.model.AbstractAsset;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * @author meiner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GXLExportAction extends ActionDelegate {

    private AbstractAsset selectedAsset;
    /**
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        if (selectedAsset != null) {
            Shell shell = Display.getDefault().getActiveShell(); 
            GXLExportDialog ged = new GXLExportDialog(shell, selectedAsset);
            ged.open();
        }
    }
    
    /**
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
       IStructuredSelection structSel = (StructuredSelection)selection;
       boolean enable = false;
       selectedAsset = null;
       if (structSel.getFirstElement() instanceof AbstractAsset) {
           selectedAsset = (AbstractAsset)structSel.getFirstElement();
           enable = true;
       }
       
       if (action != null) {
           action.setEnabled(enable);
       }

    }
}
