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
 * $Id: KoboldAssetFactory.java,v 1.1 2004/06/23 02:26:23 vanto Exp $
 *
 */
package kobold.client.plam.editor.model;

import kobold.common.model.AbstractAsset;
import kobold.common.model.Release;
import kobold.common.model.productline.Component;
import kobold.common.model.productline.Variant;

import org.eclipse.gef.requests.CreationFactory;


/**
 * @author Tammo
 */
public class KoboldAssetFactory implements CreationFactory
{
    private String id;
    
    public KoboldAssetFactory(String id) 
    {
        this.id = id;
    }
    
    /**
     * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
     */
    public Object getNewObject()
    {
        if (id.equals(AbstractAsset.COMPONENT)) {
            return new Component("dummy");
        } else if (id.equals(AbstractAsset.VARIANT)) {
            return new Variant("dummy");
        } else if (id.equals(AbstractAsset.RELEASE)) {
            return new Release("dummy");
        } else {
            return null;
        }
    }

    /**
     * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
     */
    public Object getObjectType()
    {
        return id;
    }

}
