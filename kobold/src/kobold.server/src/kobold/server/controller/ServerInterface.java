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
 * $Id: ServerInterface.java,v 1.1 2004/04/16 11:28:03 garbeam Exp $
 *
 */

package kobold.server.controller;

import java.lang.String;

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
     * @see kobold.util.data.LoginInfo
     */
    public LoginInfo login(LoginInfo li);

    /**
     * Called by Kobold-Clients to logout (the passed session-ID
     * gets invalid).
     */
    public void logout(String sessionID);

    /**
     * Posts a KoboldMessage on the server. The interface delivers
     * the passed message to the proper Objects on the server.
     *
     * @see kobold.util.data.KoboldMessage
     */
    public void sendMessage(String sessionID, KoboldMessage msg);

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
    public ProductlineInfo getProductlineInfo(String sessionID, String pl);

    /**
     * @see kobold.server.model.ProductlineAdmin.getProductInfo(java.lang.String)
     */
    public ProductInfo getProductInfo(String sessionID, String product);

    /**
     * @see kobold.server.model.ProductlineAdmin.addProduct(java.lang.String, java.lang.String)
     */
    public void addProduct(String sessionID, String name, String pl);

    /**
     * @see kobold.server.user.UserAdmin.getUserInfo(java.lang.String)
     */
    public UserInfo getUserInfo(String sessionID, String username);

    /**
     * @see kobold.server.model.ProductlineAdmin.addRole(kobold.util.data.Role)
     * @see kobold.server.user.UserAdmin.addRole(kobold.util.data.Role)
     */
    public void addRole(String sessionID, Role r);

    /**
     * @see kobold.server.model.ProductlineAdmin.applyProductlineInfo(kobold.util.data.ProductlineInfo)
     */
    public void applyProductlineInfo(String sessionID, ProductlineInfo pi);

    /**
     * @see kobold.server.model.ProductlineAdmin.applyProductInfo(kobold.util.data.ProductInfo)
     */
    public void applyProductInfo(String sessionID, ProductInfo pi);

    /**
     * @see kobold.server.user.UserAdmin.applyUserInfo(kobold.util.data.UserInfo)
     */
    public void applyUserInfo(String sessionID, UserInfo ui);

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

}
