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
 * grosseml 05.08.2004
 */
package kobold.client.plam.action;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.editor.dialog.ChangePasswordDialog;
import kobold.client.plam.editor.dialog.RemoveUserDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

/**
 * @author grosseml
 *
 * Provides the action to remove an user
 */
public class RemoveUserAction extends Action{
	private Shell shell;
	
	public RemoveUserAction(Shell shell2){
		shell = shell2;
		setText("Remove User");
		setToolTipText("Invalidates one user permanently");
		setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/user.gif"));
	}
	
    public void run()
    {
		RemoveUserDialog rud = new RemoveUserDialog(shell);
		rud.open();
    }


}
