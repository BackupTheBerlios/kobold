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
 * $Id: SecureKoboldClient.java,v 1.6 2004/05/15 16:18:16 vanto Exp $
 *
 */
package kobold.client.plam.controller;

import java.net.URL;
import java.util.List;
import java.util.Vector;

import kobold.common.controller.ServerInterface;
import kobold.common.data.KoboldMessage;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.Role;
import kobold.common.data.UserContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.secure.SecureXmlRpcClient;

/**
 * @author garbeam
 */
public class SecureKoboldClient implements ServerInterface {

	private static final Log log =
		LogFactory.getLog("kobold.client.controller.SecureKoboldClient");

	// the xml-rpc client
	private SecureXmlRpcClient client;

	/**
	 *  Constructor
	 */
	public SecureKoboldClient(URL url) {
		client = new SecureXmlRpcClient(url);
	}

	/**
	 * Login handler.
	 * @param userName the username.
	 * @param password the plain text password.
	 * @return UserContext, if the userName and password
	 * 			  is valid. 
	 */
	public UserContext login(String userName, String password) {
		Vector v = new Vector();
		v.add(userName);
		v.add(password);
		try {
			Object result = client.execute("login", v);
			return (UserContext)result;
		} catch (Exception exception) {
			log.error(exception);
		}
		return null;
	}

	/**
	 * Logout handler.
	 * Invalidates the given user context.
	 * @param userContext the user context.
	 */
	public void logout(UserContext userContext) {
		Vector v = new Vector();
		v.add(userContext);
		try {
			Object result = client.execute("logout", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Fetches all roles for the given user context from the server.
	 * @param userContext the user context.
	 * @return List of Roles.
	 */
	public List getRoles(UserContext userContext) {
		Vector v = new Vector();
		v.add(userContext);
		try {
			Object result = client.execute("getRoles", v);
			return (List)result;
		} catch (Exception exception) {
			log.error(exception);
		}
		return null;
	}

	/**
	 * Adds an new user to the server.
	 * @param userContext the user context of the valid creator of the
	 * 			  new user (if the new user is a P, than the userContext
	 * 			  must be at least a PE).
	 * @param userName the user name.
	 * @param password the password.
	 * @param realName the real name.
	 */
	public void addUser(UserContext userContext, String userName,
									String password, String realName)
	{
		Vector v = new Vector();
		v.add(userContext);
		v.add(userName);
		v.add(password);
		v.add(realName);
		try {
			Object result = client.execute("addUser", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Fetches a productline by its name.
	 * @param userContext the user context.
	 * @param plName the name of the productline.
	 * @return the product line.
	 */
	public Productline getProductline(UserContext userContext, String plName) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(plName);
		try {
			Object result = client.execute("getProductline", v);
			return (Productline)result;
		} catch (Exception exception) {
			log.error(exception);
		}
		return null;
	}

	/**
	 * Fetches a product by its name.
	 * @param userContext the user context.
	 * @param productName the name of the productline.
	 * @return the product line.
	 */
	public Product getProduct(UserContext userContext, String productName) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(productName);
		try {
			Object result = client.execute("getProduct", v);
			return (Product)result;
		} catch (Exception exception) {
			log.error(exception);
		}
		return null;
	}

	/**
	 * Adds a new product.
	 * @param userContext the user context.
	 * @param product the product.
	 */
	public void addProduct(UserContext userContext, Product product) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(product);
		try {
			Object result = client.execute("addProduct", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Adds a new role.
	 * @param userContext the user context.
	 * @param userName the specified user.
	 * @param role the new role.
	 */
	public void addRole(UserContext userContext, String userName, Role role) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(userName);
		v.add(role);
		try {
			Object result = client.execute("addRole", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Removes the role from the user.
	 * @param userContext the user context.
	 * @param userName the specified user.
	 * @param role the new role.
	 */
	public void removeRole(UserContext userContext, String userName, Role role) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(userName);
		v.add(role);
		try {
			Object result = client.execute("removeRole", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Applies modifications to the given Productline.
	 * @param userContext the user context.
	 * @param productline the productline. 
	 */
	public void applyProductlineModifications(UserContext userContext, Productline productline) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(productline);
		try {
			Object result = client.execute("applyProductlineModifications", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Applies modifications to the given Product.
	 * @param userContext the user context.
	 * @param product the product. 
	 */
	public void applyProductModifications(UserContext userContext, Product product) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(product);
		try {
			Object result = client.execute("applyProductModifications", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Removes the specified user.
	 * @param userContext the user context.
	 * @param userName the user to remove.
	 */
	public void removeUser(UserContext userContext, String userName) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(userName);
		try {
			Object result = client.execute("removeUser", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Sends a KoboldMessage or WorkflowMessage.
	 * 
	 * @param userContext the user context.
	 * @param koboldMessage the message.
	 */
	public void sendMessage(UserContext userContext, KoboldMessage koboldMessage) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(koboldMessage);
		try {
			Object result = client.execute("sendMessage", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * Fetches a single KoboldMessage. Should be put to a queue.
	 * Note: to remove the message from Servers message queue,
	 * it has to be invalidated using invalidateMessage!
	 * 
	 * @param userContext the user context.
	 */
	public KoboldMessage fetchMessage(UserContext userContext) {
		Vector v = new Vector();
		v.add(userContext);
		try {
			Object result = client.execute("fetchMessage", v);
			return (KoboldMessage)result;
		} catch (Exception exception) {
			log.error(exception);
		}
		return null;
	}

	/**
	 * Invalidates the specified message. This method will remove the message
	 * from Servers message queue.
	 * 
	 * @param userContext the user context.
	 * @param koboldMessage the message.
	 */
	public void invalidateMessage(UserContext userContext, KoboldMessage koboldMessage) {
		Vector v = new Vector();
		v.add(userContext);
		v.add(koboldMessage);
		try {
			Object result = client.execute("invalidateMessage", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}
}