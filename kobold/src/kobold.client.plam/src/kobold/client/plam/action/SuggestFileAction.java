/*
 * Created on 17.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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
public class SuggestFileAction extends Action{
	
	private Shell shell;
    private AbstractAsset selection;
    
    public SuggestFileAction(Shell shell) 
    {
        this.shell = shell;
		setText("Suggest file for core group");
		setToolTipText("Suggest a file to be added to a core group");
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
