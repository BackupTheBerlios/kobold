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
