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
 * $Id: GXLExportAction.java,v 1.5 2004/07/23 22:27:11 vanto Exp $
 *
 */
package kobold.client.plam.editor.action;

import kobold.client.plam.editor.editpart.AbstractAssetEditPart;
import kobold.client.plam.editor.editpart.ReleaseEditPart;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.wizard.GXLExportDialog;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Provides an Action for the ArchitectureEditor and exports the selected asset as gxl.
 * 
 * @author vanto
 */
public class GXLExportAction extends SelectionAction {
    public static final String ID = "kobold.client.plam.editor.action.GXLExportAction"; 
    
    public GXLExportAction(IWorkbenchPart part) 
    {
        super(part);
        setId(ID);
        setText("Export");
        setLazyEnablementCalculation(true);
        setEnabled(true);
    }

    public void run() 
    {
        Shell shell = getWorkbenchPart().getSite().getShell();
        AbstractAsset asset = (AbstractAsset) ((EditPart) getSelectedObjects()
                .get(0)).getModel();
        GXLExportDialog ged = new GXLExportDialog(shell, asset);
        ged.open();
    }

    protected boolean calculateEnabled() 
    {
        if (getSelectedObjects().size() != 1) {
            return false;
        }
        
        Object selection = getSelectedObjects().get(0);
        if ((selection instanceof AbstractAssetEditPart) 
            && !(selection instanceof ReleaseEditPart)) {
            
            return true;
        }
        
        return false;
    }

}