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
 * $Id: SecureKoboldClient.java,v 1.3 2004/05/13 15:15:34 garbeam Exp $
 *
 */
package kobold.client.plam.controller;

import java.util.List;
import java.util.Vector;

import kobold.common.controller.ServerInterface;
import kobold.common.data.KoboldMessage;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.Role;
import kobold.common.data.User;
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
	public SecureKoboldClient(String url) throws Exception {
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
	 * @param user the new user, it is not allowed to create a user with
	 * 		      more permissions than the user defined by userContext.
	 */
	public void addUser(UserContext userContext, User newUser) {
		

	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#getProductline(kobold.common.data.UserContext, java.lang.String)
	 */
	public Productline getProductline(UserContext userContext, String plName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#getProduct(kobold.common.data.UserContext, java.lang.String)
	 */
	public Product getProduct(UserContext userContext, String productName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#addProduct(kobold.common.data.UserContext, kobold.common.data.Product)
	 */
	public void addProduct(UserContext userContext, Product product) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#addRole(kobold.common.data.UserContext, kobold.common.data.User, kobold.common.data.Role)
	 */
	public void addRole(UserContext userContext, User user, Role role) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#removeRole(kobold.common.data.UserContext, kobold.common.data.User, kobold.common.data.Role)
	 */
	public void removeRole(UserContext userContext, User user, Role role) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#applyProductlineModfiications(kobold.common.data.UserContext, kobold.common.data.Productline)
	 */
	public void applyProductlineModfiications(UserContext userContext, Productline productline) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#applyProductModfiications(kobold.common.data.UserContext, kobold.common.data.Product)
	 */
	public void applyProductModfiications(UserContext userContext, Product product) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#removeUser(kobold.common.data.UserContext, kobold.common.data.User)
	 */
	public void removeUser(UserContext userContext, User user) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#sendMessage(kobold.common.data.UserContext, kobold.common.data.KoboldMessage)
	 */
	public void sendMessage(UserContext userContext, KoboldMessage koboldMessage) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#fetchMessage(kobold.common.data.UserContext)
	 */
	public KoboldMessage fetchMessage(UserContext userContext) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.common.controller.ServerInterface#invalidateMessage(kobold.common.data.UserContext, kobold.common.data.KoboldMessage)
	 */
	public KoboldMessage invalidateMessage(UserContext userContext, KoboldMessage koboldMessage) {
		// TODO Auto-generated method stub
		return null;
	}


}
