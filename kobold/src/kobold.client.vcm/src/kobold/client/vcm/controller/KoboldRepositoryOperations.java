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
 * 
 */

package kobold.client.vcm.controller;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.Release;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;

/**
 * @author schneipk
 ** This interface extends the eclipse simple Access operations and implements
 *	the changes needed to perform operations before and after the normal VCM operations
 */
public interface KoboldRepositoryOperations {
	
    /**
     * Adds all assets to version control.
     * @param assets
     * @param progress
     * @throws TeamException
     */
	public void add(AbstractAsset[] assets, IProgressMonitor progress)
		throws TeamException;
	
	/**
	 * Commits all changes of assets.
	 * @param assets
	 * @param progress
	 * @param msg
	 * @throws TeamException
	 */
	public void commit(AbstractAsset[] assets, IProgressMonitor progress,
	        		   String msg)
		throws TeamException; 
	
	/**
	 * Checks out several assets.
	 * @param progress
	 * @param tag if <code>null</code> no revision is used.
	 * @param isPl
	 * @throws TeamException
	 */
	public void checkout(AbstractAsset[] assets, IProgressMonitor progress,
	        			 String tag, boolean isPl) throws TeamException;

	/**
	 * Imports all assets.
	 * @param resources
	 * @param progress
	 * @param isPl
	 * @throws TeamException
	 */
	public void importing(AbstractAsset[] resources, IProgressMonitor progress,
	        			  String msg, boolean isPl) throws TeamException;
	
	/**
	 * Updates all assets.
	 * @param assets
	 * @param progress
	 * @param tag if <code>null</code> no revision is used.
	 * @throws TeamException
	 */
	public void update(AbstractAsset[] assets, IProgressMonitor progress, String tag)
		throws TeamException;
	
	/**
	 * Removes the assets from version control, does not remove them physically.
	 * @param assets
	 * @param progress
	 * @throws TeamException
	 */
	public void remove(AbstractAsset[] assets, IProgressMonitor progress)
		throws TeamException;
	
	
}
