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
 *MiG05.08.2004
 */
package kobold.client.plam.action;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.editor.dialog.ChangePasswordDialog;
import kobold.client.plam.editor.dialog.UpdateFullNameDialog;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

/**
 * @author MiG
 * 
 * Action for updating the Full Name of an user
 */
public class UpdateFullNameAction extends Action{
	private Shell shell;
	
	public UpdateFullNameAction(Shell shell2){
		shell = shell2;
		setText("Update Full Name");
		setToolTipText("Change your full name");
		setImageDescriptor(KoboldPLAMPlugin.getImageDescriptor("icons/user.gif"));

	}
	
    public void run()
    {
		UpdateFullNameDialog ufnd = new UpdateFullNameDialog(shell);
		ufnd.open();
    }


}
