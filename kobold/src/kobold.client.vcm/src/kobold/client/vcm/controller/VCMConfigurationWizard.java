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

package kobold.client.vcm.controller;

import kobold.client.vcm.KoboldVCMPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.team.core.RepositoryProvider;
import org.eclipse.team.ui.IConfigurationWizard;
import org.eclipse.ui.IWorkbench;

/**
 * @see Wizard
 */
public class VCMConfigurationWizard extends Wizard implements IConfigurationWizard {
	private IProject currentProject;
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
			if (currentProject != null) {
				RepositoryProvider.map(currentProject,KoboldVCMPlugin.PROVIDER_ID);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return true;
	}

	/**
	 * @see Wizard#init
	 */
	public void init(IWorkbench workbench, IProject project)  {
		try
			{
				this.currentProject = project;
			}
			catch (Exception e)
			{
				System.out.println("lala");
			}
//		workbench.getActiveWorkbenchWindow().getWorkbench().
	}
}
