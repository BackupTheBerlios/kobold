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
 * $Id: RefreshFileDescriptorsAction.java,v 1.1 2004/08/04 20:53:08 vanto Exp $
 *
 */
package kobold.client.plam.action;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.model.IFileDescriptorContainer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Shell;


/**
 * @author Tammo
 */
public class RefreshFileDescriptorsAction extends Action
{
    private Shell shell;
    private IFileDescriptorContainer selection;
    
    public RefreshFileDescriptorsAction(Shell shell)
    {
        this.shell = shell;
		setText("Refresh");
		setToolTipText("Refresh Resources");
		setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/refresh_msg.gif"));

    }
    
    public void run()
    {
		if (selection != null) {
		    selection.getRoot().getKoboldProject().refreshResources(selection);
		}
    }
    
    public void handleSelectionChanged(SelectionChangedEvent event)
    {
        IStructuredSelection sel = (IStructuredSelection)event.getSelection();
        if (sel.size() == 1 && sel.getFirstElement() instanceof IFileDescriptorContainer) {
            setEnabled(true);
            selection = (IFileDescriptorContainer)sel.getFirstElement();
        } else {
            setEnabled(false);
            selection = null;
        }
    }

}
