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
 * $Id: IKoboldServer.java,v 1.10 2004/06/24 08:45:23 grosseml Exp $
 *
 */

package kobold.common.controller;

import java.util.Vector;

import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.Role;
import kobold.common.data.UserContext;
import kobold.common.io.RepositoryDescriptor;

/**
 * This class acts as an interface between kobold clients and a
 * kobold server.
 */
public interface IKoboldServer {
	
	public static final String NO_RESULT = "NO_RESULT";

	/**
	 * Login handler.
	 * @param userName the username.
	 * @param password the plain text password.
	 * @return UserContext, if the userName and password
	 * 			  is valid. 
	 */
    public UserContext login(String userName, String password);

	/**
	 * Logout handler.
	 * Invalidates the given user context.
	 * @param userContext the user context.
	 */
    public void logout(UserContext userContext);

	/**
     * Fetches all roles for the given user context from the server.
	 * @param userContext the user context.
	 * @return List of Roles.
	 */
	public Vector getRoles(UserContext userContext);
	
	/**
	 * Adds an new user to the server.
	 * @param userContext the user context of the valid creator of the
	 * 			  new user (if the new user is a P, than the userContext
	 * 			  must be at least a PE).
	 * @param userName the user name.
	 * @param password the password.
	 * @param realName the real name.
	 */
	public void addUser(UserContext userContext,
									String userName, String password,
									String realName);
		
    /**
     * Fetches a productline by its name.
     * @param userContext the user context.
     * @param plName the name of the productline.
     * @return the product line.
     */
    public Productline getProductline(UserContext userContext, String plName);

    /**
     * Fetches a product by its name.
     * @param userContext the user context.
     * @param productName the name of the productline.
     * @return the product line.
     */
    public Product getProduct(UserContext userContext, String productName);

    /**
     * Adds a new product.
     * @param userContext the user context.
     * @param product the product.
     */
    public void addProduct(UserContext userContext, kobold.common.model.product.Product product);

    /**
     * Adds a new role.
     * @param userContext the user context.
     * @param userName the specified user.
     * @param role the new role.
     */
    public void addRole(UserContext userContext, String userName, Role role);

	/**
	 * Removes the role from the user.
	 * @param userContext the user context.
	 * @param userName the specified user.
	 * @param role the new role.
	 */
	public void removeRole(UserContext userContext, String userName, Role role);

	/**
	 * Applies modifications to the given Productline.
	 * @param userContext the user context.
	 * @param productline the productline. 
	 */
	public void applyProductlineModifications(UserContext userContext,
																 Productline productline);
    
	/**
	 * Applies modifications to the given Product.
	 * @param userContext the user context.
	 * @param product the product. 
	 */
	public void applyProductModifications(UserContext userContext,
			kobold.common.model.product.Product product);
    
    /**
     * Removes the specified user.
     * @param userContext the user context.
     * @param user the user to remove.
     */
    public void removeUser(UserContext userContext, String userName);

	/**
	 * Sends a KoboldMessage or WorkflowMessage.
	 * 
	 * @param userContext the user context.
	 * @param koboldMessage the message.
	 */
	public void sendMessage(UserContext userContext,
												AbstractKoboldMessage koboldMessage);
	
	
	/**
	 * Fetches a single KoboldMessage. Should be put to a queue.
	 * Note: to remove the message from Servers message queue,
	 * it has to be invalidated using invalidateMessage!
	 * 
	 * @param userContext the user context.
	 */
	public AbstractKoboldMessage fetchMessage(UserContext userContext);
	

	/**
	 * Invalidates the specified message. This method will remove the message
	 * from Servers message queue.
	 * 
	 * @param userContext the user context.
	 * @param koboldMessage the message.
	 */
	public void invalidateMessage(UserContext userContext,
												  AbstractKoboldMessage koboldMessage);
	
	/**
	 * This method is used by SAT-Clients to validate the accessibility of
	 * the Kobold server with the passed password.
	 *  
	 * @param adminPassword server administration password
	 * @return IKoboldServer::NO_RESULT if the server is not
	 *				   accessible that way, "" otherwise
	 */
	public String validateSATAccessibility(String adminPassword); 
	
	/**
	 * this method is used by SAT-Clients to create a new productline
	 * on the KoboldServer
	 * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @return IKoboldServer::NO_RESULT if the server is not
	 *				   accessible that way, "" otherwise 
	 */
	public String satCreateNewProductline(String adminPassword, String plname, RepositoryDescriptor rd);
	
	/**
	 * this method is used by SAT-Clients to remove a productline
	 * on the KoboldServer
	 * 
	 * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @return IKoboldServer::NO_RESULT if an error occured,"" otherwise 
	 */
	public String satRemoveProductline(String adminPassword, String plname);
	
	/**
	 * this method is used by SAT-Clients to set a productline's new PLE
	 * 
	 * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @param username name of the user who should become plsname's
	 *                 new PLE
	 * @return IKoboldServer::NO_RESULT if an error occured,"" otherwise 
	 */
	public String satAddPLE(String adminPassword, String plname, String username);
	
	/**
	 * this method is used by SAT-Clients to invalidate a productline's 
	 * PLE
	 * 
	 * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @return IKoboldServer::NO_RESULT if an error occured,"" otherwise 
	 */
	public String satRemovePLE(String adminPassword, String plname);
}