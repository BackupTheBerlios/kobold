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
 * $Id: ArchitectureEditorInputFactory.java,v 1.3 2004/09/01 02:58:22 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import kobold.client.plam.KoboldProject;
import kobold.client.plam.model.AbstractRootAsset;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;


/**
 * Restores a stored editor state.
 * @author Tammo
 */
public class ArchitectureEditorInputFactory implements IElementFactory
{

	/**
	 * Factory id. The workbench plug-in registers a factory by this name
	 * with the "org.eclipse.ui.elementFactories" extension point.
	 */
	private static final String ID_FACTORY = "kobold.client.plam.editor.ArchitectureEditorInputFactory"; //$NON-NLS-1$

	private static final String TAG_PROJECT = "project"; //$NON-NLS-1$
	private static final String TAG_ROOT_ID = "rootid";
	
	/**
	 * Creates a new factory.
	 */
	public ArchitectureEditorInputFactory() {
	}
	
	/**
	 * @see org.eclipse.ui.IElementFactory#createElement(org.eclipse.ui.IMemento)
	 */
	public IAdaptable createElement(IMemento memento) {
		String name = memento.getString(TAG_PROJECT);
		String rootid = memento.getString(TAG_ROOT_ID);
		if (name == null || rootid == null)
			return null;

		IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		if (p.isAccessible() && KoboldProject.hasKoboldNature(p)) {
			KoboldProject kp;
            try {
                kp = (KoboldProject)p.getNature(KoboldProject.NATURE_ID);
                //Productline pl = kp.getProductline();
                AbstractRootAsset ara = (AbstractRootAsset)kp.getProductline().getAssetById(rootid);
                return (ara != null) ? new ArchitectureEditorInput(ara) : null;
            } catch (CoreException e) {}
            
            return null;
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the element factory id for this class.
	 * 
	 * @return the element factory id
	 */
	public static String getFactoryId() 
	{
		return ID_FACTORY;
	}
	
	/**
	 * Saves the state of the given file editor input into the given memento.
	 *
	 * @param memento the storage area for element state
	 * @param input the file editor input
	 */
	public static void saveState(IMemento memento, ArchitectureEditorInput input) 
	{
	    memento.putString(TAG_PROJECT, input.getRootAsset().getKoboldProject().getProject().getName());
	    memento.putString(TAG_ROOT_ID, input.getRootAsset().getId());
	}

}
