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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.core.simpleAccess.SimpleAccessOperations;

/**
 * @author schneipk
 ** @ TODO shitty and not needed interface
 */
public interface KoboldRepositoryOperations extends SimpleAccessOperations {
	
	public void precheckin(IResource[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void postcheckin(IResource[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void precheckout(IResource[] resources, int depth, boolean performOperation) throws TeamException;
	
	public void postcheckout(IResource[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;

	public void preget(IResource[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
	
	public void postGet(IResource[] resources, int depth, IProgressMonitor progress, boolean performOperation) throws TeamException;
}
