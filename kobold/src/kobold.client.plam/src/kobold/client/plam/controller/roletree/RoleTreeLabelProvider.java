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
 * $Id: RoleTreeLabelProvider.java,v 1.3 2004/05/12 15:04:14 neco Exp $
 *
 */
package kobold.client.plam.controller.roletree;

import org.eclipse.jface.viewers.LabelProvider;
import kobold.common.data.*;

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

	public String getText(Object element) {
		if (element instanceof User) {
			return ((User)element).getUserName();
		} else if (element instanceof RolePLE) {
			return "PLE";
		} else if (element instanceof RolePE) {
			return "PE";
		} else if (element instanceof RoleP) {
			return "P";
		} else if (element instanceof Productline) {
			return ((Productline)element).getName();
		} else if (element instanceof Product) {
			return ((Product)element).getName();
		} else {
			throw unknownElement(element);
		}
	}
	protected RuntimeException unknownElement(Object element) {
		return new RuntimeException("Unknown type of element in tree of type " 
				+ element.getClass().getName());
	}
}
