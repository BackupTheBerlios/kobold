/*
 * Created on 17.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.client.plam.action;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.workflow.CoreGroupDialog;
import kobold.common.data.WorkflowMessage;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

/**
 * @author dometine
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SuggestFileAction extends Action{
	
	private Shell shell;
    
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
			CoreGroupDialog cgDialog = new CoreGroupDialog(shell);
			cgDialog.open();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
