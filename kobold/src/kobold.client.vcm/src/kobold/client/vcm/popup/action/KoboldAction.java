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
 * Created on 27.05.2004
 * by schneipk KoboldAction.java
 * @author schneipk
 */
package kobold.client.vcm.popup.action;


import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.product.Product;
import kobold.client.plam.model.productline.Productline;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.team.core.TeamException;
import org.eclipse.team.internal.ui.actions.TeamAction;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;

/**
 * @author schneipk
 *
 *  This class holds the basic functionality needed by all Kobold Actions
 *  it implements the slectionChanged method and stores the selected Objects
 *  in their respective Variables
 */
public class KoboldAction extends TeamAction implements IObjectActionDelegate {

	protected Productline selectedProductLine = null;
	protected Product selectedProduct = null;
	protected Variant selectedVariant = null;
	protected AbstractAsset testAssets[] = null;
	
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ui.actions.TeamAction#isEnabled()
	 */
	/**
	 * In Iteration II this function should always return true as tere is no check
	 * wether the User/Context is allowed to call this method
	 */
	protected boolean isEnabled() throws TeamException {
		return true;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		IStructuredSelection structSelection =((IStructuredSelection)selection);
		
		for (int i = 0; i < structSelection.size(); i++) {
			if (i == 0) {
				testAssets = new AbstractAsset[structSelection.size()];
			}
			if (structSelection.getFirstElement() instanceof AbstractAsset) {
				testAssets[i] = (AbstractAsset)structSelection.getFirstElement();
			}
			if (structSelection.getFirstElement() instanceof Productline) {
				selectedProductLine = (Productline)structSelection.getFirstElement();
			}
			if (structSelection.getFirstElement() instanceof Product) {
				selectedProduct = (Product)structSelection.getFirstElement();
			}
			if (structSelection.getFirstElement() instanceof Variant) {
				selectedVariant = (Variant)structSelection.getFirstElement();
			}
			
		}

		
	}

}
