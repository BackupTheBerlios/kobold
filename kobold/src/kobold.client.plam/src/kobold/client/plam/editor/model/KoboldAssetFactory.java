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
 * $Id: KoboldAssetFactory.java,v 1.8 2004/08/05 12:57:04 rendgeor Exp $
 *
 */
package kobold.client.plam.editor.model;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.MetaNode;
import kobold.client.plam.model.Release;
import kobold.client.plam.model.productline.Component;
import kobold.client.plam.model.productline.Variant;

import org.eclipse.gef.requests.CreationFactory;


/**
 * @author Tammo
 */
public class KoboldAssetFactory implements CreationFactory
{
	//iterators for the different assets
	public static int iterC = 0;
	public static int iterV = 0;
	public static int iterR = 0;
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
    	//TODO: check for unique names, instead don't create asset
  	
        if (id.equals(AbstractAsset.COMPONENT)) {
        	iterC++;
        	Component c = new Component();
            c.setName(id+iterC);
            return c;
        } else if (id.equals(AbstractAsset.VARIANT)) {
        	iterV++;
            Variant c = new Variant();
            c.setName(id+iterV);
            return c;
        } else if (id.equals(AbstractAsset.RELEASE)) {
        	iterR++;
            Release c = new Release();
            c.setName(id+iterR);
            return c;
        } else if (id.equals(MetaNode.AND)) {
            return new MetaNode(MetaNode.AND);
        } else if (id.equals(MetaNode.OR)) {
            return new MetaNode(MetaNode.OR);
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
