package kobold.client.vcm.controller;

import kobold.client.vcm.KoboldVCMPlugin;

import org.eclipse.ui.IWorkbench;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.ui.IConfigurationWizard;

/**
 * @see Wizard
 */
public class VCMConfigurationWizard extends Wizard implements IConfigurationWizard {
	private IProject project;
	/**
	 *
	 */
	public VCMConfigurationWizard() {
	}

	/**
	 * @see Wizard#performFinish
	 */
	public boolean performFinish()  {
		try {
			RepositoryProvider.map(project,KoboldVCMPlugin.getPROVIDER_ID());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return true;
	}

	/**
	 * @see Wizard#init
	 */
	public void init(IWorkbench workbench, IProject project)  {
		this.project = project;
//		workbench.getActiveWorkbenchWindow().getWorkbench().
	}
}
