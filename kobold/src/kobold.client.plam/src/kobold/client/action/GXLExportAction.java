/*
 * Created on 24.06.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.action;

import kobold.client.plam.wizard.GXLExportDialog;
import kobold.common.model.AbstractAsset;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author MiG
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class GXLExportAction extends SelectionAction {

	public GXLExportAction(IWorkbenchPart part) {
		super(part);
		setId("kobold.client.action.GXLExportAction");
		setText("Export");
		setLazyEnablementCalculation(true);
		setEnabled(true);
	}
	
	public void run() {
		Shell shell = getWorkbenchPart().getSite().getShell();
		AbstractAsset asset = (AbstractAsset)((EditPart)getSelectedObjects().get(0)).getModel();
		System.out.println(asset);
		GXLExportDialog ged = new GXLExportDialog(shell, asset);
		ged.open();
	}

	protected boolean calculateEnabled() {
		if ((getSelectedObjects().size() == 1) 
			&& (getSelectedObjects().get(0) instanceof AbstractAsset)) {
			return true;
		}
		return true;
	}
}
