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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations;

/**
 * @author schneipk
 ** This interface extends the eclipse simple Access operations and implements
 *	the changes needed to perform operations before and after the normal VCM operations
 */
public interface KoboldRepositoryOperations extends SimpleAccessOperations {
	
	public void preCheckin(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void add(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void preAdd(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void postAdd(AbstractAsset[] assets, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void postCheckin(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void preCheckout(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void postcheckout(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;

	public void preImport(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void postImport(AbstractAsset[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void checkin(AbstractAsset[] resources, int depth, IProgressMonitor progress)throws TeamException; 
	
	public void checkout(AbstractAsset[] resources, int depth, IProgressMonitor progress) throws TeamException;
	
	public void importing(AbstractAsset[] resources, int depth, IProgressMonitor progress)
			throws TeamException;
	
	public void update(AbstractAsset[] resources, int depth, IProgressMonitor progress)
	throws TeamException;
	
	public void preUpdate(AbstractAsset[] resources, int depth, IProgressMonitor progress)
	throws TeamException;
	
	public void postUpdate(AbstractAsset[] resources, int depth, IProgressMonitor progress)
	throws TeamException;
}
