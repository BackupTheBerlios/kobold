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
 * $Id: SecureKoboldWebServer.java,v 1.24 2004/06/23 11:48:08 neccaino Exp $
 *
 */
package kobold.server;

import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import kobold.common.controller.IKoboldServer;
import kobold.common.controller.RPCMessageTransformer;
import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.Role;
import kobold.common.data.RoleP;
import kobold.common.data.RolePE;
import kobold.common.data.User;
import kobold.common.data.UserContext;
import kobold.common.data.WorkflowMessage;
import kobold.server.controller.MessageManager;
import kobold.server.controller.ProductManager;
import kobold.server.controller.SessionManager;
import kobold.server.controller.UserManager;
import kobold.server.workflow.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.secure.SecureWebServer;

/**
 * @author garbeam, contan
 *
 * Implements the server interface and delegates all RPCs to its
 * appropriate methods.
 */
public class SecureKoboldWebServer implements IKoboldServer, XmlRpcHandler {
	
	private static final Log logger = LogFactory.getLog(SecureKoboldWebServer.class);

	// the xml-rpc webserver
	private static SecureWebServer server;

	/**
	 * main method
	 */
	public static void main(String args[]) throws Exception {
		if (args.length < 1) {
			System.err.println("Usage: java SecureKoboldWebServer port");
			System.exit(1);
		}
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException nonumber) {
			System.err.println("Invalid port number: " + args[0]);
			System.exit(1);
		}

		Properties props = new Properties(System.getProperties());
		props.load(new FileInputStream("server.properties"));
		System.setProperties(props);
		
		server = new SecureWebServer(port);
		// add an instance of this class as default handler
		server.addHandler("$default", new SecureKoboldWebServer());
		server.start();
		System.err.println("Listening on port " + port);
	}

	/**
	 * Basic delegation of RPC to the specific handler method.
	 * @see org.apache.xmlrpc.XmlRpcHandler#execute(java.lang.String, java.util.Vector)
	 */
	public Object execute(String methodName, Vector arguments)
		throws Exception
	{
		try {
			if (methodName.equals("login")) {
				UserContext userContext = login((String)arguments.elementAt(0), (String)arguments.elementAt(1));
				if (userContext !=  null) {
					return RPCMessageTransformer.encode(userContext.serialize());
				}
			}
			else if (methodName.equals("logout")) {
			    UserContext uc = new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0)));
			    logout(uc);
			}
			else if (methodName.equals("getRoles")) {
				Vector result = new Vector();
				Vector roles = getRoles(new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0))));
				if (roles.size() > 0) {
					for (Iterator it = roles.iterator(); it.hasNext(); ) {
						Role role = (Role) it.next();
						result.add(RPCMessageTransformer.encode(role.serialize()));
					}
					return result;
				}
			}
			else if (methodName.equals("addUser")) {
				addUser(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
						  (String)arguments.elementAt(1),
						  (String)arguments.elementAt(2),
						  (String)arguments.elementAt(3));
			}
			else if (methodName.equals("getProductline")) {
				UserContext uc = new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				if (uc != null) {
					return RPCMessageTransformer.encode(getProductline(uc,
											  (String)arguments.elementAt(1)).serialize());
				}
			}
			else if (methodName.equals("getProduct")) {
				Product product = getProduct(
					new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
					(String)arguments.elementAt(1));
					
				if (product != null) {
					return RPCMessageTransformer.encode(product.serialize());
				}
			}
			else if (methodName.equals("addProduct")) {
				addProduct(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
							  new Product(RPCMessageTransformer.decode((String)arguments.elementAt(1))));
			}
			else if (methodName.equals("addRole")) {
				addRole(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
						 (String)arguments.elementAt(1), 
						 Role.createRole(RPCMessageTransformer.decode((String)arguments.elementAt(2))));
			}
			else if (methodName.equals("removeRole")) {
				removeRole(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
							   (String)arguments.elementAt(1),
								Role.createRole(RPCMessageTransformer.decode((String)arguments.elementAt(2))));
			}
			else if (methodName.equals("applyProductlineModifications")) {
				applyProductlineModifications(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
														new Productline(RPCMessageTransformer.decode((String)arguments.elementAt(1))));
			}
			else if (methodName.equals("applyProductModifications")) {
				applyProductModifications(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
													new Product(RPCMessageTransformer.decode((String)arguments.elementAt(1))));
			}
			else if (methodName.equals("removeUser")) {
				removeUser(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
								(String)arguments.elementAt(1));
			}
			else if (methodName.equals("sendMessage")) {
				sendMessage(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
								 AbstractKoboldMessage.createMessage(RPCMessageTransformer.decode((String)arguments.elementAt(1))));
			}
			else if (methodName.equals("fetchMessage")) {
				AbstractKoboldMessage abstractKoboldMessage =
					fetchMessage(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))));
				if (abstractKoboldMessage != null) {
					return RPCMessageTransformer.encode(abstractKoboldMessage.serialize());
				}
			}
			else if (methodName.equals("invalidateMessage")) {
				invalidateMessage(new UserContext(RPCMessageTransformer.decode((String)arguments.elementAt(0))),
										AbstractKoboldMessage.createMessage(RPCMessageTransformer.decode((String)arguments.elementAt(1))));
			}
			else if (methodName.equals("validateSATAccessibility")){
				return validateSATAccessibility((String)arguments.elementAt(0));
			}
		} catch (Exception e) {
			logger.info("Exception during execute()", e);
			throw e;	
		}
		return IKoboldServer.NO_RESULT;
	}
	

	/**
	 * Login handler.
	 * @param userName the username.
	 * @param password the plain text password.
	 * @return UserContext, if the userName and password
	 * 			  is valid. 
	 */
	public UserContext login(String userName, String password) {
		return SessionManager.getInstance().login(userName, password);
	}


	/**
	 * Logout handler.
	 * Invalidates the given user context.
	 * @param userContext the user context.
	 */
	public void logout(UserContext userContext) {
		SessionManager.getInstance().logout(userContext);
	}

	/**
	 * Fetches all roles for the given user context from the server.
	 * @param userContext the user context.
	 * @return List of Roles.
	 */
	public Vector getRoles(UserContext userContext) {
		UserManager manager = UserManager.getInstance();
		return manager.getUser(userContext.getUserName()).getRoles(); 
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
		User user = new User();
		user.setUserName(userName);
		user.setRealName(realName);
		user.setPassword(password);
		UserManager manager = UserManager.getInstance();
		manager.addUser(user);
	}
	
	/**
	 * This method changes all userdate from a given user except his password
	 * for changing the password see method changepassword
	 * @param userContext	the user context
	 * @param userName		the user name
	 * @param realName		the real name
	 */
	public void changeUser(UserContext userContext, String userName,
		 String realName)
	{
		UserManager manager = UserManager.getInstance();
		User user = manager.getUser(userName);
		user.setUserName(userName);
		user.setRealName(realName);
	}

	/**
	 * Fetches a productline by its name.
	 * @param userContext the user context.
	 * @param plName the name of the productline.
	 * @return the product line.
	 */
	public Productline getProductline(UserContext userContext, String plName) {
		ProductManager productManager = ProductManager.getInstance();
		return productManager.getProductLine(plName);
	}

	/**
	 * Fetches a product by its name.
	 * @param userContext the user context.
	 * @param productName the name of the productline.
	 * @return the product line.
	 */
	public Product getProduct(UserContext userContext, String productName) {
		ProductManager productManager = ProductManager.getInstance(); 
		return productManager.getProduct(productName);
	}

	/**
	 * Adds a new product.
	 * @param userContext the user context.
	 * @param product the product.
	 */
	public void addProduct(UserContext userContext, Product product) {
		ProductManager productManager = ProductManager.getInstance();
		UserManager userManager = UserManager.getInstance();
		if (userManager.isPLE(userContext.getUserName())) {
			productManager.addProduct(product);
		}
	}

	/**
	* Adds a new role.
	* @ param userContext the user context.
	* @ param userName the specified user.
	* @ param role the new role.
	*/
	public void addRole(UserContext userContext, String userName, Role role) {
		ProductManager productManager = ProductManager.getInstance();
		UserManager userManager = UserManager.getInstance();
		String name = userContext.getUserName();

		if (((role instanceof RolePE) && userManager.isPLE(name))
			|| ((role instanceof RoleP) && userManager.isPE(name))) {
			User user = userManager.getUser(userName);
			if (user != null) {
				user.addRole(role);
			}
		}

	}

	/**
	 * Removes the role from the user.
	 * @param userContext the user context.
	 * @param userName the specified user.
	 * @param role the new role.
	 */
	public void removeRole(UserContext userContext, String userName, Role role) {
		ProductManager productManager = ProductManager.getInstance();
		UserManager userManager = UserManager.getInstance();
		String name = userContext.getUserName();

		if (((role instanceof RolePE) && userManager.isPLE(name))
			|| ((role instanceof RoleP) && userManager.isPE(name))) {
			User user = userManager.getUser(userName);
			if (user != null) {
				user.removeRole(role);
			}
		}
	}

	/**
	 * Applies modifications to the given Productline.
	 * @param userContext the user context.
	 * @param productline the productline. 
	 */
	public void applyProductlineModifications(UserContext userContext, Productline productline) {
		UserManager userManager = UserManager.getInstance();
		if (userManager.isPLE(userContext.getUserName())) {
		 		ProductManager productManager = ProductManager.getInstance();
		  		Productline pline = productManager.getProductLine(productline.getName());
		  		productManager.removeProductLine(pline);
		  		productManager.addProductLine(productline);
		}
	}

	/**
	 * Applies modifications to the given Product.
	 * @param userContext the user context.
	 * @param product the product. 
	 */
	public void applyProductModifications(UserContext userContext, Product product) {
		UserManager userManager = UserManager.getInstance();
		if (userManager.isPLE(userContext.getUserName()) ||
		    userManager.isPE(userContext.getUserName()))
		{
				ProductManager productManager = ProductManager.getInstance();
				Product p = productManager.getProduct(product.getName());
				productManager.removeProduct(p);
				productManager.addProduct(product);
		}
	}

	/**
	 * Removes the specified user.
	 * @param userContext the user context.
	 * @param userName the user to remove.
	 */
	public void removeUser(UserContext userContext, String userName) {
		
		if (userContext.getUserName() != userName) {
			UserManager manager = UserManager.getInstance();
			if (manager.isPLE(userName)) {
				return;
			}
			else if ((manager.isPE(userName) && manager.isPLE(userContext.getUserName())) ||
						(!manager.isPE(userName) && manager.isPE(userContext.getUserName())))
			{
				User user = manager.getUser(userName);
				manager.removeUser(user);
			}
		}
	}

	/**
	 * Sends a KoboldMessage or WorkflowMessage.
	 * 
	 * @param userContext the user context.
	 * @param koboldMessage the message.
	 */
	public void sendMessage(UserContext userContext, AbstractKoboldMessage koboldMessage) {
		if (koboldMessage instanceof WorkflowMessage) {
			WorkflowEngine.applWorkflow((WorkflowMessage)koboldMessage);
		}
		else {
			MessageManager.getInstance().sendMessage(userContext, koboldMessage);
		}
	}


	/**
	 * Fetches a single KoboldMessage. Should be put to a queue.
	 * Note: to remove the message from Servers message queue,
	 * it has to be invalidated using invalidateMessage!
	 * 
	 * @param userContext the user context.
	 */
	public AbstractKoboldMessage fetchMessage(UserContext userContext) {
		return MessageManager.getInstance().fetchMessage(userContext);
	}

	/**
	 * Invalidates the specified message. This method will remove the message
	 * from Servers message queue.
	 * 
	 * @param userContext the user context.
	 * @param koboldMessage the message.
	 */
	public void invalidateMessage(UserContext userContext, AbstractKoboldMessage koboldMessage) {
		MessageManager.getInstance().invalidateMessage(userContext,
																					koboldMessage);
	}
	
	public String validateSATAccessibility(String adminPassword){
		return this.NO_RESULT; // until fully implemented
	}
}