package kobold.client.vcm.popup.action;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ui.actions.TeamAction;

/**
 */
public class DisconnectAction extends TeamAction{
	
	/**
	 */
	public DisconnectAction() {

	}
	public void run(IAction action) {
		IProject projects[] = getSelectedProjects();
		try {
			for (int i = 0; i < projects.length; i++) {
				RepositoryProvider.unmap(projects[i]);
			}
		} catch (TeamException e) {
			ErrorDialog.openError(getShell(), "Cannot disconnect", null, e.getStatus()); //$NON-NLS-1$
		} 
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ui.actions.TeamAction#isEnabled()
	 */
	protected boolean isEnabled() throws TeamException {
		// TODO Auto-generated method stub
		return true;
	}
}
