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
 * $Id: Product.java,v 1.2 2004/04/08 13:53:26 garbeam Exp $
 *
 */

package kobold.server.model;

/**
 * This class stores a product's data on the server.
 */
public class Product {

    private ProductInfo info;

    /**
     * @see kobold.server.model.ProductlineAdmin.applyProductInfo(kobold.util.data.ProductInfo)
     */
    public void applyProductInfo(ProductInfo info) {
    }

    /**
     * @see kobold.server.model.ProductlineAdmin.addRole(kobold.util.data.Role)
     */
    public void addRole(Role r) {
    }

    /**
     * @return information about the product
     * @see kobold.util.data.ProductInfo 
     */
    public getInfo(ProductInfo arg1) {
        return null;  
    }

    /**
     * @see kobold.server.model.ProductlineAdmin.removeRole(kobold.util.data.Role)
     */
    public void removeRole(Role r) {
    }
}
