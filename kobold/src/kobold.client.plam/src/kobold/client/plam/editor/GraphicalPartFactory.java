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
 * $Id: GraphicalPartFactory.java,v 1.9 2004/08/06 01:57:29 vanto Exp $
 *
 */
package kobold.client.plam.editor;

import kobold.client.plam.editor.editpart.ComponentEditPart;
import kobold.client.plam.editor.editpart.EdgeEditPart;
import kobold.client.plam.editor.editpart.MetaEditPart;
import kobold.client.plam.editor.editpart.ProductEditPart;
import kobold.client.plam.editor.editpart.ProductlineEditPart;
import kobold.client.plam.editor.editpart.RelatedComponentEditPart;
import kobold.client.plam.editor.editpart.ReleaseEditPart;
import kobold.client.plam.editor.editpart.SpecificComponentEditPart;
import kobold.client.plam.editor.editpart.VariantEditPart;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.edges.Edge;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * GraphicalPartFactory
 * 
 * @author Tammo
 */
public class GraphicalPartFactory implements EditPartFactory {

	/** 
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;

		if (model instanceof Productline) {
			editPart = new ProductlineEditPart();
		} else if (model instanceof Component) {
			editPart = new ComponentEditPart();
		} else if (model instanceof Variant) {
			editPart = new VariantEditPart();
		} else if (model instanceof Release) {
			editPart = new ReleaseEditPart();
		} else if (model instanceof MetaNode) {
		    editPart = new MetaEditPart();
		} else if (model instanceof Edge) {
		    editPart = new EdgeEditPart();
		}

		else if (model instanceof Product) {
		    editPart = new ProductEditPart();
		} else if (model instanceof SpecificComponent) {
		    editPart = new SpecificComponentEditPart();
		} else if (model instanceof RelatedComponent) {
		    editPart = new RelatedComponentEditPart();
		}
		
		if (editPart != null) {
			editPart.setModel(model);
		}

		return editPart;
	}

}
