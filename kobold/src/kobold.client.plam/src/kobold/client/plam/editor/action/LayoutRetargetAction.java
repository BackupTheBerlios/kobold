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
 * $Id: LayoutRetargetAction.java,v 1.2 2004/09/23 10:39:45 vanto Exp $
 *
 */
package kobold.client.plam.editor.action;

import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.internal.WorkbenchImages;


/**
 * @author Tammo
 */
public class LayoutRetargetAction extends LabelRetargetAction
{

    /**
     * @param actionID
     * @param text
     */
    public LayoutRetargetAction()
    {
    	super(LayoutAction.ID, "Layout");
    	setImageDescriptor(WorkbenchImages.getImageDescriptor(
								ISharedImages.IMG_DEF_VIEW));
    	setToolTipText("Layout Connected Graph");
    }

}
