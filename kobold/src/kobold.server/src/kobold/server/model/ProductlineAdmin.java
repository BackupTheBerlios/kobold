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
 * $Id: ProductlineAdmin.java,v 1.2 2004/04/18 14:04:40 garbeam Exp $
 *
 */

package kobold.server.model;

import java.util.List;

import kobold.common.data.PInfo;
import kobold.common.data.PLInfo;
import kobold.common.data.Role;

/**
 * This class admins the model of productlines and products on the server.
 *
 * @author Armin Cont
 */
public class ProductlineAdmin {

    /**
     * This method adds a new (empty) productline to the model.
     *
     * @param productline the name of the new productline
     */
    public void addProductline(String productline) {
    }

    /**
     * This method adds a new (empty) product to the given productline.
     *
     * @param product the new product's name
     * @param productline name of the productline to add the new product to
     */
    public void addProduct(String product, String productline) {
    }

    /**
     * Adds (and applies) the passed Roleobject to the model.
     * (e.g. if r is a PE-Role the method looks for the product
     * that is given by the Role object and changes its associated
     * PE-Role to the new Role)
     *
     * @param r the Role to add
     */
    public void addRole(Role r) {
    }

    /**
     * @return info to the passed productline
     */
    public PLInfo getProductlineInfo(String productline) {
        return null;
    }

    /**
     * @return info to the passed product
     */
    public PInfo getProductInfo(String product) {
        return null;
    }

    /**
     * Changes the info stored by the productline-object that is specified
     * by the passed ProductlineInfo according to its contents.
     * 
     * @see ProductlineInfo
     */
    public void applyProductlineInfo(PLInfo info) {
    }

    /**
     * Changes the info stored by the product-object that is specified
     * by the passed ProductInfo according to its contents.
     * 
     * @see ProductInfo
     */
    public void applyProductInfo(PInfo info) {
    }

    /**
     * Removes the passed productline.
     */
    public void removeProductline(String pl) {
    }

    /**
     * Removes the passed product.
     */
    public void removeProduct(String product) {
    }

    /**
     * @return a list containing all stored productlines' names 
     */
    public List getProductlineList() {
        return null;
    }

    /**
     * Removes a Role from the model (e.g. removal of a programmer 
     * form a product)
     */
    public void removeRole(Role r) {
    }
}
