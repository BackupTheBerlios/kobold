/*
 * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
 *                    Martin Plies, Michael Grosse, Necati Aydin,
 *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package kobold.client.vcm.popup.action;

import kobold.client.vcm.communication.KoboldPolicy;
import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


/**
 * @author pschneid
 *
 */
public class CheckoutAction extends KoboldAction {
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		KoboldRepositoryAccessOperations repoAccess = new KoboldRepositoryAccessOperations();
		IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		try
		{
			repoAccess.checkout(testAssets,IResource.DEPTH_INFINITE,progress);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		Shell shell = new Shell();
		MessageDialog.openInformation(
			shell,
			"Kobold VCM Plug-in",
			"preupdate (precheckout) was executed.");
//		repoAccess.checkout();
		MessageDialog.openInformation(
				shell,
				"Kobold VCM Plug-in",
				"update (checkout) was executed.");
//		repoAccess.postcheckout();
		MessageDialog.openInformation(
				shell,
				"Kobold VCM Plug-in",
				"postupdate (postcheckout) was executed.");
	}

}
