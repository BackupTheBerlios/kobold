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

import kobold.client.plam.model.AbstractRootAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.IFileDescriptorContainer;
import kobold.client.plam.model.ModelStorage;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.communication.KoboldPolicy;
import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;
import kobold.client.vcm.controller.StatusUpdater;
import kobold.common.io.RepositoryDescriptor;

import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.team.core.TeamException;
import org.eclipse.ui.IActionDelegate;

public class ImportAction extends KoboldAction {

	/**
	 * Constructor for Action1.
	 */
	public ImportAction() {
		super();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
		public void run(IAction action) {
			KoboldRepositoryAccessOperations repoAccess = new KoboldRepositoryAccessOperations();
			IProgressMonitor progress = KoboldPolicy.monitorFor(null);
//			KoboldVCMPlugin
//			((Productline)testAssets[0]).getLocalPath();//serializeProductline("C:\\Temp",true);
			//IFolder if1 = ((Productline)testAssets[0]).getKoboldProject().getPath();
			//String ip = if1.getFullPath().toOSString();
//			ModelStorage.storeModel(((Productline)testAssets[0]));
			StatusUpdater st = new StatusUpdater();
			
			st.updateFileDescriptors(((Variant)testAssets[0]));
//			RepositoryDescriptor rdsc = ((Productline)testAssets[0]).getRepositoryDescriptor();
			boolean test = testAssets[0] instanceof AbstractRootAsset;
			

			
			try
			{
//				repoAccess.precheckin(testAssets,IResource.DEPTH_INFINITE,progress,true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			try
			{
//				repoAccess.checkin(testAssets,IResource.DEPTH_INFINITE,progress);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
//			repoAccess.postcheckin();
//			MessageDialog.openInformation(
//					shell,
//					"Kobold VCM Plug-in",
//					"postImport was executed.");
		}


	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ui.actions.TeamAction#isEnabled()
	 */
	protected boolean isEnabled() throws TeamException
	{
		
		return true;
	}
}

