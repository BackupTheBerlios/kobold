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

import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;
import kobold.client.vcm.controller.StatusUpdater;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.team.core.TeamException;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class AddAction extends KoboldAction {

	//The selected Object
	IResource currentSelection = null;
	/**
	 * Constructor for Action1.
	 */
	public AddAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
		public void run(IAction action) {
			KoboldRepositoryAccessOperations repoAccess = new KoboldRepositoryAccessOperations();
			try
			{
				//gets the stored userData
				
		//test
				Variant var = new Variant ("testVar");
				
				
				//update FD(s)
				StatusUpdater statUp = new StatusUpdater ();

				String[] command = {"perl", "/home/rendgeor/workspace/kobold.client.vcm/scripts/"+ 
						"stats.pl", "/home/rendgeor/workspace/kobold.client.vcm/"};
				
				statUp.processConnection(command, var);

		////		
				
//				((Component)testAssets[0])

				
				repoAccess.preCheckout(testAssets,IResource.DEPTH_INFINITE,null,true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
//			Shell shell = new Shell();
			
//				shell,
//				"Kobold VCM Plug-in",
//				"preAdd was executed.");
////			repoAccess.checkin();
//			MessageDialog.openInformation(
//					shell,
//					"Kobold VCM Plug-in",
//					"Add was executed.");
////			repoAccess.postcheckin();
//			MessageDialog.openInformation(
//					shell,
//					"Kobold VCM Plug-in",
//					"postadd was executed.");
		}


	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ui.actions.TeamAction#isEnabled()
	 */
	protected boolean isEnabled() throws TeamException
	{
		
		return true;
	}
}
