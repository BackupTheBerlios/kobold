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
 * $Id: DeprecatedStatus.java,v 1.3 2004/10/21 21:32:41 martinplies Exp $
 *
 */
package kobold.client.plam.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;


/**
 * @author Tammo
 */
public class DeprecatedStatus extends AbstractStatus
{
    /**
     * @see kobold.client.plam.model.AbstractStatus#getId()
     */
    public String getId()
    {
        return DEPRECATED_ID;
    }

    /**
     * @see kobold.client.plam.model.AbstractStatus#getName()
     */
    public String getName()
    {
        return "deprecated";
    }

    /**
     * @see kobold.client.plam.model.AbstractStatus#getDescription()
     */
    public String getDescription()
    {
        return "{0} is deprecated. Please choose an alternative.";
    }

    /**
     * @see kobold.client.plam.model.AbstractStatus#getIcon()
     */
    public ImageDescriptor getIcon()
    {
        ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
        return images.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
    }
}
