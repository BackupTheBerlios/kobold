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
 * $Id: VCMDecorator.java,v 1.3 2004/08/26 16:58:16 martinplies Exp $
 */
package kobold.client.vcm;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

import java.lang.Object;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.jface.viewers.ILabelDecorator;

/**
 * @see ILabelDecorator
 */
public class VCMDecorator extends LabelProvider
		implements
			ILightweightLabelDecorator,
			IResourceChangeListener
{
	/**
	 *
	 */
	public VCMDecorator() {
		System.out.println("lalal");
	}

	/**
	 * @see ILabelDecorator#addListener
	 */
	public void addListener(ILabelProviderListener listener)  {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang.Object, org.eclipse.jface.viewers.IDecoration)
	 */
	public void decorate(Object element, IDecoration decoration)
	{
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	public void resourceChanged(IResourceChangeEvent event)
	{
        IResourceDelta[] deltas = new IResourceDelta[] { event.getDelta()};
        List changes = new ArrayList();

        collectChangedFiles(changes, deltas);

        if (!changes.isEmpty()) {
            postLabelEvent(
                new LabelProviderChangedEvent(this, changes.toArray()));
        }
	}
	
    private void collectChangedFiles(
            Collection collector,
            IResourceDelta[] deltas) {

            for (int i = 0; i < deltas.length; i++) {
                if ((deltas[i].getKind() & IResourceDelta.CHANGED) != 0) {
                    IResource resource = deltas[i].getResource();
                    if (resource instanceof IResource) {
                       
                            collector.add(resource);
                    }
                }
                collectChangedFiles(
                    collector,
                    deltas[i].getAffectedChildren(IResourceDelta.CHANGED));
            }
        }
    
    /**
     * Post the label event to the UI thread.
     *
     * @param event The event to post.
     */
    private void postLabelEvent(final LabelProviderChangedEvent event) {
        Display.getDefault().asyncExec(new Runnable() {
            public void run() {
                fireLabelProviderChanged(event);
            }
        });
    }


}
