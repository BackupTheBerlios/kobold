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
 * $Id: RoleTreeLabelProvider.java,v 1.14 2004/08/25 01:50:32 vanto Exp $
 *
 */
package kobold.client.plam.controller.roletree;

import java.util.Hashtable;
import java.util.Map;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.roletree.RoleTreeContentProvider.ArchitectureItem;
import kobold.client.plam.controller.roletree.RoleTreeContentProvider.TreeContainer;
import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.FileDescriptor;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;
import kobold.common.data.User;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * RoleTreeLabelProvider
 * 
 * This label provider maps an element of the architecture model to an 
 * image and text string used to display the element in the viewer's control.
 * 
 * @author Tammo van Lessen
 */
public class RoleTreeLabelProvider extends LabelProvider
{
	private Map imgCache = null;

	public String getText(Object element) {
		if (element instanceof User) {
			return ((User)element).getFullname();
		}  if (element instanceof Productline) {
			return ((Productline)element).getName();
		} else if (element instanceof Product) {
			return ((Product)element).getName();
		} else if (element instanceof IProject) {
			return ((IProject)element).getName();
		} else if (element instanceof TreeContainer) {
		    return ((TreeContainer)element).getName();
		} else if (element instanceof AbstractAsset) {
		    return ((AbstractAsset)element).getName();
		} else if (element instanceof FileDescriptor) {
		    FileDescriptor fd = (FileDescriptor) element;
		    String result = fd.getFilename();
		    if (fd.getRevision().length() > 0) {
		        result += "(" + fd.getRevision() + ")";
		    }
		    return result;
		} else if (element instanceof ArchitectureItem) {
		    return "Architecture";
		} else {
			return "unkown";
			//throw unknownElement(element);
		}
	}
	
	
	
	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type " 
				+ element.getClass().getName());
	}
	
	public Image getImage(Object element) {
		ImageDescriptor id = null;
		if (element instanceof IProject) {
			IWorkbenchAdapter wa = (IWorkbenchAdapter)((IProject)element).getAdapter(IWorkbenchAdapter.class);
			id = wa.getImageDescriptor(element);
		} else if (element instanceof User) {
			id = KoboldPLAMPlugin.getImageDescriptor("icons/user.gif");
		} else if (element instanceof Productline) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/pl.gif");
		} else if (element instanceof Component) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/component.gif");
		} else if (element instanceof Variant) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/variant.gif");
		} else if (element instanceof Release) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/release.gif");
		} else if (element instanceof TreeContainer) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/container.gif");
		} else if (element instanceof ArchitectureItem) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/kobold_persp.gif");
		} else if (element instanceof Product) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/product.gif");
		} else if (element instanceof RelatedComponent) {
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/relcomp.gif");
		} else if (element instanceof SpecificComponent){
		    id = KoboldPLAMPlugin.getImageDescriptor("icons/speccomp.gif");
		} else if (element instanceof FileDescriptor) {
		    FileDescriptor fd = (FileDescriptor) element;
		    if (fd.isDirectory()) {
    		    id = WorkbenchPlugin.getDefault().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FOLDER);
		    }
		    else {
    		    id = WorkbenchPlugin.getDefault().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
		    }
		}

		if (id == null) {
			return null;
		}
		
		// image cache
		if (imgCache == null) {
			imgCache = new Hashtable(10);
		}
		
		Image image = (Image)imgCache.get(id);

		if (image == null) {
			image = id.createImage();
			imgCache.put(id, image);
		}
		return image;
	}

}
