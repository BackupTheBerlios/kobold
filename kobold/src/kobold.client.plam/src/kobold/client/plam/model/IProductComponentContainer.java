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
 * $Id: IProductComponentContainer.java,v 1.2 2004/10/13 22:43:44 martinplies Exp $
 *
 */

package kobold.client.plam.model;

import java.util.List;

import kobold.client.plam.model.product.ProductComponent;
import kobold.client.plam.model.product.RelatedComponent;
import kobold.client.plam.model.product.SpecificComponent;


/**
 * @author pliesmn
 */
public interface IProductComponentContainer  {
   
    
    /**
	 * Removes a ProductComponent and has to set its parent to null.
	 *
	 * @param ProductComponet to remove
	 */
    public void removeProductComponent(ProductComponent comp);
    
    
    /**
	 * Adds a ProductComponent and has to set its 
	 * parent to this asset.
	 *
	 * @param ProductComponent to add
	 */
    void addProductComponent(ProductComponent comp);
    
    /**
	 * Adds a SpecificComponent at the given index and has to set its 
	 * parent to this asset.
	 *
	 * @param SpecificComponent to add
	 */
    public void addSpecificComponent(SpecificComponent component, int index);
    
    /**
	 * Adds a RelatedComponent at the given index and has to set its 
	 * parent to this asset.
	 *
	 * @param RelatedComponent to add
	 */
    public void addRelatedComponent(RelatedComponent component, int index);
    
    /**
     * 
     * @return Returns all SpecificComponents
     */
    public List getSpecificComponents();
       
    
    /**
     * 
     * @return Returns all RelatedComponents
     */
    public List getRelatedComponents();
    
    /**
     * 
     * @return Returns all ProductComponents
     */
    public List getProductComponents();
    
    
    public String getName();
    
    
    public AbstractRootAsset getRoot();


}
