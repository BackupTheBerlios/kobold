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
 * $Id: MetaInfoAction.java,v 1.7 2004/11/05 10:32:31 grosseml Exp $
 *
 */
package kobold.client.plam.action;

import org.apache.log4j.Logger;

import kobold.client.plam.MetaInformation;
import kobold.client.plam.model.AbstractAsset;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionDelegate;


/**
 * @author Necati Aydin
 */
public class MetaInfoAction extends ActionDelegate
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MetaInfoAction.class);

    private AbstractAsset selectedAsset;
    
    public void run(IAction action)
    {
        if (selectedAsset != null) {
            Shell shell = Display.getDefault().getActiveShell(); 
            
            FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
    		saveDialog.setFileName("assetinfo.pdf");
    		String filePath = saveDialog.open();
    		if (filePath != null) {
    			MetaInformation metaInformation = new MetaInformation(new Path(filePath));
    			metaInformation.getMetaData(selectedAsset);
    		}
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
