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
 * $Id: SecureKoboldWebServer.java,v 1.40 2004/07/05 15:59:53 garbeam Exp $
 *
 */
package kobold.server;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import kobold.common.controller.IKoboldServer;
import kobold.common.controller.RPCMessageTransformer;
import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.Component;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.RPCSpy;
import kobold.common.data.UserContext;
import kobold.common.data.WorkflowMessage;
import kobold.common.io.RepositoryDescriptor;
import kobold.server.controller.MessageManager;
import kobold.server.controller.ProductManager;
import kobold.server.controller.SessionManager;
import kobold.server.controller.UserManager;
import kobold.server.data.User;
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
		Vector sniffArgs = new Vector();
		try {
			if (methodName.equals("login")) {
				sniffArgs.add(new String((String)arguments.elementAt(0)));
				sniffArgs.add(new String((String)arguments.elementAt(1)));
				UserContext userContext = login((String)arguments.elementAt(0), (String)arguments.elementAt(1));
				if (userContext !=  null) {
					return RPCMessageTransformer.encode(userContext.serialize());
				}
			}
			else if (methodName.equals("logout")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				logout(uc);
			}
			else if (methodName.equals("addUser")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				sniffArgs.add(arguments.elementAt(1));
				sniffArgs.add(arguments.elementAt(2));
				sniffArgs.add(arguments.elementAt(3));
				
				addUser(uc, (String)arguments.elementAt(1),
						    (String)arguments.elementAt(2),
						    (String)arguments.elementAt(3));
			}
			else if (methodName.equals("getAllUsers")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				
				List users = getAllUsers(uc);
				List result = new ArrayList();
				for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
					User user = (User) iterator.next();
					result.add(RPCMessageTransformer.encode(user.serialize()));
				}
				return result;
			}
			else if (methodName.equals("removeUser")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				kobold.common.data.User user = new kobold.common.data.User(
						RPCMessageTransformer.decode((String)arguments.elementAt(1)));
				sniffArgs.add(uc);
				sniffArgs.add(user);
				
				removeUser(uc, user);
			}
			else if (methodName.equals("getProductlineNames")) {			
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);

				// no need to serialize string lists
				return getProductlineNames(uc);
			}
			else if (methodName.equals("getProductline")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				sniffArgs.add(arguments.elementAt(1));
				
				return RPCMessageTransformer.encode(
						getProductline(uc, (String)arguments.elementAt(1)).serialize());
			}
			else if (methodName.equals("updateProductline")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				Productline productline = new Productline(
						RPCMessageTransformer.decode((String)arguments.elementAt(1)));
				
				sniffArgs.add(uc);
				sniffArgs.add(productline);

				updateProductline(uc, productline);
			}
			else if (methodName.equals("updateProduct")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				String productlineName = (String)arguments.elementAt(1);
				Productline productline = ProductManager.getInstance().getProductLine(productlineName);
				Product product = new Product(productline,
						RPCMessageTransformer.decode((String)arguments.elementAt(2)));
				
				sniffArgs.add(uc);
				sniffArgs.add(product);

				updateProduct(uc, productlineName, product);
			}
			else if (methodName.equals("updateComponent")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				String productName = (String)arguments.elementAt(1);
				Product product = ProductManager.getInstance().getProduct(productName);
				Component component = new Component(product,
						RPCMessageTransformer.decode((String)arguments.elementAt(2)));
				
				sniffArgs.add(uc);
				sniffArgs.add(component);

				updateComponent(uc, productName, component);
			}
			else if (methodName.equals("sendMessage")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				AbstractKoboldMessage msg =
					AbstractKoboldMessage.createMessage(RPCMessageTransformer.decode((String)arguments.elementAt(1)));

				sniffArgs.add(uc);
				sniffArgs.add(msg);
				sendMessage(uc, msg);
			}
			else if (methodName.equals("fetchMessage")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				AbstractKoboldMessage msg = fetchMessage(uc);
				if (msg != null) {
					return RPCMessageTransformer.encode(msg.serialize());
				}
			}
			else if (methodName.equals("invalidateMessage")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				AbstractKoboldMessage msg =
					AbstractKoboldMessage.createMessage(RPCMessageTransformer.decode((String)arguments.elementAt(1)));

				sniffArgs.add(uc);
				sniffArgs.add(msg);
				
				invalidateMessage(uc, msg);
			
			}
			// TODO: separate to special interface
			else if (methodName.equals("validateSATAccessibility")){
				return validateSATAccessibility((String)arguments.elementAt(0));
			}
			else if (methodName.equals("satCreateNewProductline")){
				return satCreateNewProductline((String)arguments.elementAt(0),
														               (String)arguments.elementAt(1),
						                                               (RepositoryDescriptor)arguments.elementAt(2));
			}
			else if (methodName.equals("satRemoveProductline")){
				return satRemoveProductline((String)arguments.elementAt(0),
						                                          (String)arguments.elementAt(1));
			}
			else if (methodName.equals("satAddPLE")){
				return satAddPLE((String)arguments.elementAt(0),
						                        (String)arguments.elementAt(1),
						                        (String)arguments.elementAt(2));
			}
			else if (methodName.equals("satRemovePLE")){
				return satRemovePLE((String)arguments.elementAt(0),
						                               (String)arguments.elementAt(1));
			}
		} catch (Exception e) {
			logger.info("Exception during execute()", e);
			throw e;	
		}
		
		WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
		return IKoboldServer.NO_RESULT;
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#login(String, String)}
	 */
	public UserContext login(String userName, String password) {
		return SessionManager.getInstance().login(userName, password);
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#logout(UserContext)}
	 */
	public void logout(UserContext userContext) {
		SessionManager.getInstance().logout(userContext);
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#getAllUsers(kobold.common.data.UserContext)
	 */
	public List getAllUsers(UserContext userContext) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#updateUser(kobold.common.data.UserContext, kobold.common.data.User, java.lang.String)
	 */
	public void updateUser(UserContext userContext, kobold.common.data.User user, String password) {
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#removeUser(kobold.common.data.UserContext, kobold.common.data.User)
	 */
	public void removeUser(UserContext userContext, kobold.common.data.User user) {
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#addUser(UserContext, String, String, String)}
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
	 * {@see kobold.common.controller.IKoboldServer#getProductline(UserContext, String)}
	 */
	public Productline getProductline(UserContext userContext, String plName) {
		ProductManager productManager = ProductManager.getInstance();
		return productManager.getProductLine(plName);
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#updateProductline(UserContext, Productline)}
	 */
	public void updateProductline(UserContext userContext, Productline productline) {
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#getProductlineNames(kobold.common.data.UserContext)
	 */
	public List getProductlineNames(UserContext userContext) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#updateProduct(UserContext, String, Product)}
	 */
	public void updateProduct(UserContext userContext, String productlineName, Product product) {
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#updateProduct(UserContext, String, Product)}
	 */
	public void updateComponent(UserContext userContext, String productName, Component component) {
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#sendMessage(UserContext, AbstractKoboldMessage)}
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
	 * {@see kobold.common.controller.IKoboldServer#fetchMessage(UserContext)}
	 */
	public AbstractKoboldMessage fetchMessage(UserContext userContext) {
		return MessageManager.getInstance().fetchMessage(userContext);
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#invalidateMessage(UserContext, AbstractKoboldMessage)}
	 */
	public void invalidateMessage(UserContext userContext, AbstractKoboldMessage koboldMessage) {
		MessageManager.getInstance().invalidateMessage(userContext, koboldMessage);
	}
	
	/**
	 * This method is used by SAT-Clients to validate the accessibility of
	 * the Kobold server with the passed password.
	 * 
	 * NOTE: since there is no real administrator password functionality
	 * on the Kobold server after it2, every password is accepted!
	 *  
	 * @param adminPassword server administration password
	 * @return true, if the passed password is valid, false otherwise
	 */
	public String validateSATAccessibility(String adminPassword){
		return ""; // no password's needed, so check is successful
	}
	
	/**
	 * this method is used by SAT-Clients to create a new productline
	 * on the KoboldServer
	 * 
	 * NOTE: this member has not yet been fully implemented!
	 * (adminPassword is not checked -> it3)
	 * 
	 * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @return IKoboldServer::NO_RESULT if the server is not
	 *				   accessible that way, "" otherwise 
	 */
	public String satCreateNewProductline(String adminPassword, String plname, RepositoryDescriptor rd){
		try{
			ProductManager.getInstance().addProductLine(new Productline(plname, rd));
		}
		catch(Exception e){
			return IKoboldServer.NO_RESULT;
		}
		
		return ""; // return success
	}
	
	/**
	 * this method is used by SAT-Clients to remove a new productline
	 * on the KoboldServer
	 * 
	 * NOTE: this member has not yet been fully implemented!
	 * (adminPassword is not checked -> it3)
	 *
     * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @return IKoboldServer::NO_RESULT if an error occured,"" otherwise 
	 */
	public String satRemoveProductline(String adminPassword, String plname){
		try{
			ProductManager.getInstance().removeProductLine(new Productline(plname, null));
		}
		catch(Exception e){
			return IKoboldServer.NO_RESULT;
		}
		
		return ""; // return success
	}
	
	/**
	 * this method is used by SAT-Clients to set a productline's new PLE
     * 
	 * NOTE: this member has not yet been fully implemented!
	 * (stub has been inserted to avoid comp. errors)
	 * 
     * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @param username name of the user who should become plsname's
	 *                 new PLE
	 * @return IKoboldServer::NO_RESULT if an error occured,"" otherwise 
	 */
	public String satAddPLE(String adminPassword, String plname, String username){		
		return IKoboldServer.NO_RESULT;// until fully implemented
	}
	
	/**
	 * this method is used by SAT-Clients to invalidate a productline's 
	 * PLE
	 * 
	 * NOTE: this member has not yet been fully implemented!
	 * (stub has been inserted to avoid comp. errors)
	 * 
	 * @param adminPassword server administartion password
	 * @param plname name of the new productline
	 * @return IKoboldServer::NO_RESULT if an error occured,"" otherwise 
	 */
	public String satRemovePLE(String adminPassword, String plname){
		return IKoboldServer.NO_RESULT;// until fully implemented
	}

	

}