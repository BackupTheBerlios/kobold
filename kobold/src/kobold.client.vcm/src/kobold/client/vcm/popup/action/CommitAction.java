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

import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ui.actions.TeamAction;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CommitAction extends TeamAction implements IObjectActionDelegate {

	//The selected Object
	IResource currentSelection = null;
	/**
	 * Constructor for Action1.
	 */
	public CommitAction() {
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
//				repoAccess.precheckin(currentSelection,IResource.DEPTH_INFINITE,null);
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}
			Shell shell = new Shell();
			MessageDialog.openInformation(
				shell,
				"Kobold VCM Plug-in",
				"prcommit (precheckin) was executed.");
//			repoAccess.checkin();
			MessageDialog.openInformation(
					shell,
					"Kobold VCM Plug-in",
					"update (checkin) was executed.");
//			repoAccess.postcheckin();
			MessageDialog.openInformation(
					shell,
					"Kobold VCM Plug-in",
					"postupdate (postcheckin) was executed.");
		}
//			run(new WorkspaceModifyOperation() {
//				public void execute(IProgressMonitor monitor) throws InterruptedException {
//					try {
//						Map table = CommitAction.this.getProviderMapping();
//						monitor.beginTask(null, table.size() * 1000);
//						monitor.setTaskName(KoboldPolicy.bind("GetAction.working")); //$NON-NLS-1$
//						for (Iterator iter = table.keySet().iterator(); iter.hasNext();) {
//							IProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1000);
//							WrapperProvider provider = (WrapperProvider) iter.next();
//							List list = (List) table.get(provider);
//							IResource[] providerResources = (IResource[]) list.toArray(new IResource[list.size()]);
////							provider.getOperations().get(providerResources, IResource.DEPTH_INFINITE, isOverwriteOutgoing(), subMonitor);
//						}
//					} finally {
//						monitor.done();
//					}
//				}
//			}, KoboldPolicy.bind("GetAction.problemMessage"), TeamAction.PROGRESS_DIALOG); //$NON-NLS-1$		
//
//		KoboldRepositoryAccessOperations repoAccess = new KoboldRepositoryAccessOperations();
//		IProgressMonitor monitor;
////		selection
//		try {
//			
//			repoAccess.precheckin(currentSelection,IResource.DEPTH_INFINITE,null);
//			repoAccess.checkin();
//			repoAccess.postcheckin();
////			ao.precheckin(currentSelection,0);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
	

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IResource) {
			currentSelection = (IResource)selection;
		}
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ui.actions.TeamAction#isEnabled()
	 */
	protected boolean isEnabled() throws TeamException
	{
		
		return true;
	}
}
