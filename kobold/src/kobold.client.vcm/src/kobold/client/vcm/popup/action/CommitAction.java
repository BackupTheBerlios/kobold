package kobold.client.vcm.popup.action;

import kobold.client.vcm.controller.KoboldRepositoryAccessOperations;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CommitAction implements IObjectActionDelegate {

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
		Shell shell = new Shell();
		

		KoboldRepositoryAccessOperations ao = new KoboldRepositoryAccessOperations();
		IProgressMonitor monitor;
//		selection
		try {
			ao.precheckout(currentSelection,0);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IResource) {
			currentSelection = (IResource)selection;
		}
		
	}

}
