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
 * $Id: ConfigureAssetAction.java,v 1.5 2004/11/05 10:32:31 grosseml Exp $
 *
 */
package kobold.client.plam.action;

import org.apache.log4j.Logger;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.editor.dialog.AssetConfigurationDialog;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.MetaNode;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Shell;


/**
 * Provides an action to configure an asset (for use in RoleTree).
 * 
 * @author Tammo
 */
public class ConfigureAssetAction extends Action
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ConfigureAssetAction.class);

    private Shell shell;
    private AbstractAsset selection;
    
    public ConfigureAssetAction(Shell shell)
    {
        this.shell = shell;
		setText("Configure Asset...");
		setToolTipText("Show asset configuration dialog.");
		setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif"));

    }
    
    public void run()
    {
		if (selection != null) {
	        AssetConfigurationDialog acd = new AssetConfigurationDialog(shell, selection);
	        acd.open();
		}
    }
    
    public void handleSelectionChanged(SelectionChangedEvent event)
    {
        IStructuredSelection sel = (IStructuredSelection)event.getSelection();
        if (sel.size() == 1 && sel.getFirstElement() instanceof AbstractAsset
                && !(sel.getFirstElement() instanceof MetaNode)
                && !(sel.getFirstElement() instanceof FileDescriptor)) {
            setEnabled(true);
            selection = (AbstractAsset)sel.getFirstElement();
        } else {
            setEnabled(false);
            selection = null;
        }
    }

}
