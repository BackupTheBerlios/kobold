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
 * $Id: ConfigureAssetAction.java,v 1.7 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.editor.action;

import org.apache.log4j.Logger;

import kobold.client.plam.editor.dialog.AssetConfigurationDialog;
import kobold.client.plam.editor.editpart.AbstractAssetEditPart;
import kobold.client.plam.editor.editpart.FileDescriptorEditPart;
import kobold.client.plam.editor.editpart.MetaEditPart;
import kobold.client.plam.editor.editpart.ProductlineEditPart;
import kobold.client.plam.model.AbstractAsset;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;


/**
 * Provides an action for selected assets and invokes  the configuration dialog.
 * 
 * @author Tammo
 */
public class ConfigureAssetAction extends SelectionAction
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ConfigureAssetAction.class);

    public static final String ID = "kobold.client.plam.editor.action.ConfigureAssetAction";
    
    /**
     * @param part
     */
    public ConfigureAssetAction(IWorkbenchPart part)
    {
        super(part);
        setId(ID);
        setText("Configure...");
        setLazyEnablementCalculation(true);
        setEnabled(true);
    }

    public void run() {
        Shell shell = getWorkbenchPart().getSite().getShell();
        AbstractAsset asset = (AbstractAsset) ((EditPart) getSelectedObjects()
                .get(0)).getModel();
        AssetConfigurationDialog acd = new AssetConfigurationDialog(shell, asset);
        acd.open();
    }

    /**
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    protected boolean calculateEnabled()
    {
        if ((getSelectedObjects().size() == 1)
                && ((getSelectedObjects().get(0) instanceof AbstractAssetEditPart)
                || (getSelectedObjects().get(0) instanceof ProductlineEditPart))
                && !(getSelectedObjects().get(0) instanceof MetaEditPart)
                && !(getSelectedObjects().get(0) instanceof FileDescriptorEditPart)) {

            return true;
        }
        return false;
    }

}
