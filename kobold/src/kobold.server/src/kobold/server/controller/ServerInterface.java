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
 * $Id: ServerInterface.java,v 1.5 2004/05/03 22:57:07 garbeam Exp $
 *
 */

package kobold.server.controller;

import java.util.List;

import kobold.common.data.KoboldMessage;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.Role;
import kobold.common.data.UserContext;

/**
 * This class acts as an interface for Kobold-clients.
 *
 * @author Armin Cont
 */
public interface ServerInterface {

    /**
     * A Kobold-Client has to call this method before making any
     * other requests.
     * 
     * @param userName the username
     * @param password the password
     * @returns {@link kobold.common.data.UserContext}
     */
    public UserContext login(String userName, String password);

    /**
     * Logs out and invalidates the given userContext.
     */
    public void logout(UserContext userContext);


	/**
	 * 
	 * @param sessionID
	 * @param name
	 */
	public List getRoles(UserContext userContext);
	
        /**
     * @see kobold.server.model.ProductlineAdmin.addProductline(java.lang.String)
     */
    public void addProductline(String sessionID, String name);

    /**
     * @see kobold.server.user.UserAdmin.addUser(java.lang.String)
     */
    public void addUser(String sessionID, String username);

    /**
     * @see kobold.server.model.ProductlineAdmin.getProductlineList()
     */
    public List getProductlineList(String sessionID);

    /**
     * @see kobold.server.model.ProductlineAdmin.getProductlineInfo(java.lang.String)
     */
    public Productline getProductlineInfo(String sessionID, String pl);

    /**
     * @see kobold.server.model.ProductlineAdmin.getProductInfo(java.lang.String)
     */
    public Product getProductInfo(String sessionID, String product);

    /**
     * @see kobold.server.model.ProductlineAdmin.addProduct(java.lang.String, java.lang.String)
     */
    public void addProduct(String sessionID, String name, String pl);

    /**
     * @see kobold.server.user.UserAdmin.getUserInfo(java.lang.String)
     */
    public UserContext getUserContext(String sessionID, String username);

    /**
     * @see kobold.server.model.ProductlineAdmin.addRole(kobold.util.data.Role)
     * @see kobold.server.user.UserAdmin.addRole(kobold.util.data.Role)
     */
    public void addRole(String sessionID, Role r);

    /**
     * @see kobold.server.model.ProductlineAdmin.applyProductlineInfo(kobold.util.data.ProductlineInfo)
     */
    public void applyPLInfo(String sessionID, Productline pi);

    /**
     * @see kobold.server.model.ProductlineAdmin.applyProductInfo(kobold.util.data.ProductInfo)
     */
    public void applyProductInfo(String sessionID, Product pi);

    /**
     * @see kobold.server.user.UserAdmin.applyUserInfo(kobold.util.data.UserInfo)
     */
    public void applyUserInfo(String sessionID, UserContext uc);

    /**
     * @see kobold.server.model.ProductlineAdmin.removeProductline(java.lang.String)
     */
    public void removeProductline(String sessionID, String pl);

    /**
     * @see kobold.server.model.ProductlineAdmin.removeProduct(java.lang.String)
     */
    public void removeProduct(String sessionID, String product);

    /**
     * @see kobold.server.model.ProductlineAdmin.removeRole(kobold.util.data.Role)
     * @see kobold.server.user.UserAdmin.removeRole(kobold.util.data.Role)
     */
    public void removeRole(Role r);

    /**
     * @see kobold.server.user.UserAdmin.removeUser(java.lang.String)
     */
    public void removeUser(String sessionID, String username);

	/**
	 * Commits a single Message.
	 * 
	 * @param uc the UserContext, @see kobold.common.data.UserContext
	 * @param koboldMessage, @see kobold.common.data.KoboldMessage
	 */
	public void commitMessage(UserContext uc,
												KoboldMessage koboldMessage);
	
	/**
	 * Fetches a single Message.
	 * 
	 * @param uc the UserContext, @see kobold.common.data.UserContext
	 * @return of WorkflowMessages.
	 */
	public KoboldMessage fetchMessage(UserContext uc);
}
