/*
 * Created on 24.06.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.editor.action;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.Release;
import kobold.client.plam.wizard.GXLExportDialog;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author MiG
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class GXLExportAction extends SelectionAction {
    public static final String ID = "kobold.client.plam.editor.action.GXLExportAction"; 
    public GXLExportAction(IWorkbenchPart part) {
        super(part);
        setId(ID);
        setText("Export");
        setLazyEnablementCalculation(true);
        setEnabled(true);
    }

    public void run() {
        Shell shell = getWorkbenchPart().getSite().getShell();
        AbstractAsset asset = (AbstractAsset) ((EditPart) getSelectedObjects()
                .get(0)).getModel();
        GXLExportDialog ged = new GXLExportDialog(shell, asset);
        ged.open();
    }

    protected boolean calculateEnabled() {
        if ((getSelectedObjects().size() == 1)
                && (getSelectedObjects().get(0) instanceof EditPart)) {
            AbstractAsset aa = (AbstractAsset)((EditPart)getSelectedObjects().get(0)).getModel();
            if (!(aa instanceof Release)) {
                return true;
            }
        }
        return false;
    }

}