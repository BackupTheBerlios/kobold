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
 * $Id: SecureKoboldWebServer.java,v 1.81 2004/09/23 13:43:14 vanto Exp $
 *
 */
package kobold.server;

import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import kobold.common.controller.IKoboldServer;
import kobold.common.controller.IKoboldServerAdministration;
import kobold.common.controller.RPCMessageTransformer;
import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.Productline;
import kobold.common.data.RPCSpy;
import kobold.common.data.User;
import kobold.common.data.UserContext;
import kobold.common.data.WorkflowMessage;
import kobold.common.io.RepositoryDescriptor;
import kobold.server.controller.MessageManager;
import kobold.server.controller.ProductlineManager;
import kobold.server.controller.SessionManager;
import kobold.server.controller.UserManager;
import kobold.server.workflow.WorkflowEngine;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.secure.SecureWebServer;

/**
 * @author garbeam, contan
 *
 * This class is the Kobold server's entry point (main()) and the target of
 * all remote procedure calls that originate from Kobold clients. Those calls
 * are recieved by the (XmlRpcHandler-derived) method execute() and from there
 * delegated to their appropriate methods (which are derived from their 
 * respective Interfaces).
 * 
 * @see kobold.common.controller.IKoboldServer
 * @see kobold.common.controller.IKoboldServerAdministration
 * @see org.apache.xmlrpc.XmlRpcHandler 
 */
public class SecureKoboldWebServer implements IKoboldServer, 
                                              XmlRpcHandler,
                                              IKoboldServerAdministration{
	
	private static final Log logger = LogFactory.getLog(SecureKoboldWebServer.class);
	private static String adminPassword = "";

	// the xml-rpc webserver
	private static SecureWebServer server;
	
	/**
	 * main method
	 */
	public static void main(String args[]) throws Exception {
		String configFile = null;
	    if (args.length < 1) {
			System.err.println("Usage: java SecureKoboldWebServer port [configfile] [administrator password]");
			System.exit(1);
		}

	    if (args.length > 1) {
		    configFile = args[1];
		}

		if (args.length > 2) {
		    adminPassword = args[2];
		}

		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (NumberFormatException nonumber) {
			System.err.println("Invalid port number: " + args[0]);
			System.exit(1);
		}
		
		BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("[%p] %C - %F - %m%n")));
		
        // set UUID node manager class
        System.setProperty("org.apache.commons.id.uuid.state.State", InMemoryStateImpl.class.getName());
        
        if (configFile == null) {
            configFile = System.getProperty("kobold.server.configFile");
        }
        
        if (configFile == null) {
            System.err.println("No config file found. Exiting.");
            System.exit(1);
        }
        
		try {
		    Properties props = new Properties(System.getProperties());
		    props.load(new FileInputStream(configFile));
		    System.setProperties(props);
        } catch (Exception e) {
			logger.error("Could not find client configuration",e);
        }
		
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
            /*******************************************************************
             *                    IKoboldServer delegation
             ******************************************************************/
			if (methodName.equals("login")) {
				sniffArgs.add(new String((String)arguments.elementAt(0)));
				sniffArgs.add(new String((String)arguments.elementAt(1)));
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				UserContext userContext = login(null, (String)arguments.elementAt(0), (String)arguments.elementAt(1));
				if (userContext !=  null) {
					return RPCMessageTransformer.encode(userContext.serialize());
				}
			}
			else if (methodName.equals("logout")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				logout(uc);
			}
			else if (methodName.equals("addUser")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				sniffArgs.add(arguments.elementAt(1));
				sniffArgs.add(arguments.elementAt(2));
				sniffArgs.add(arguments.elementAt(3));
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				addUser(uc, (String)arguments.elementAt(1),
						    (String)arguments.elementAt(2),
						    (String)arguments.elementAt(3));
			}
			else if (methodName.equals("getAllUsers")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));

			    List users = UserManager.getInstance().getAllUsers();
			    Vector result = new Vector();
			    
			    for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
			        result.add(RPCMessageTransformer.encode(((User)iterator.next()).serialize()));
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
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				removeUser(uc, user);
			}
			else if (methodName.equals("updateUserPassword")) {
				UserContext uc = new UserContext (
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				kobold.common.data.User user = new kobold.common.data.User(
						RPCMessageTransformer.decode((String)arguments.elementAt(1)));
				String oldPassword = (String)arguments.elementAt(2);
				String newPassword = (String)arguments.elementAt(3);
				sniffArgs.add(uc);
				sniffArgs.add(user);
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				updateUserPassword(uc, user, oldPassword, newPassword);
			}
			else if (methodName.equals("updateUserFullName")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				kobold.common.data.User user = new kobold.common.data.User(
						RPCMessageTransformer.decode((String)arguments.elementAt(1)));
				String password = (String)arguments.elementAt(2);
				sniffArgs.add(uc);
				sniffArgs.add(user);
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				updateUserFullName(uc, user, password);
				
			}
			else if (methodName.equals("getProductlineNames")) {			
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				// no need to serialize string lists
				return getProductlineNames(uc);
			}
			else if (methodName.equals("getProductline")) {
				UserContext uc = new UserContext(
						RPCMessageTransformer.decode((String)arguments.elementAt(0)));
				sniffArgs.add(uc);
				sniffArgs.add(arguments.elementAt(1));
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
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
				WorkflowEngine.applRPCSpy(new RPCSpy(new String(methodName), sniffArgs));
				updateProductline(uc, productline);
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
			/*******************************************************************
			 *           IKoboldServerAdministration delegation
			 ******************************************************************/
            else if (methodName.equals("checkAdministrability")){
                try{
                   logger.info("Recieved a client's request to administrate me.\n");
                   // admin method so leave without noticing the WorkflowEngine
                   return checkAdministrability((String)arguments.elementAt(0));
                }
                catch(Exception e){
                   logger.info("Exception during execute()", e);
                   return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("newProductline")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    
                    //1.) unpack the repository desscriptor
                    RepositoryDescriptor rd = new RepositoryDescriptor();
                    rd.setHost((String)arguments.elementAt(3));
                    rd.setPath((String)arguments.elementAt(4));
                    rd.setProtocol((String)arguments.elementAt(5));
                    rd.setRoot((String)arguments.elementAt(6));
                    rd.setType((String)arguments.elementAt(7));
                    
                    //2.) call newProductline and leave without noticing the WorkflowEngine
                    return newProductline((String)arguments.elementAt(0),
                            			  (String)arguments.elementAt(1),
                                          (String)arguments.elementAt(2),
                                          rd);
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("invalidateProductline")){
                try{
                   logger.info("Recieved admincall \"" + methodName + "\"\n");
                   // admin method so leave without noticing the WorkflowEngine
                   return invalidateProductline((String)arguments.elementAt(0),
                                                (String)arguments.elementAt(1));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("assignPle")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return assignPle((String)arguments.elementAt(0),
                                     (String)arguments.elementAt(1),
                                     (String)arguments.elementAt(2));
                }
                catch(Exception e){
                   logger.info("Exception during execute()", e);
                   return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("unassignPle")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return unassignPle((String)arguments.elementAt(0),
                                       (String)arguments.elementAt(1),
                                       (String)arguments.elementAt(2));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("getPles")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return getPles((String)arguments.elementAt(0),
                                   (String)arguments.elementAt(1));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("getProductlines")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return getProductlines((String)arguments.elementAt(0));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("getGeneralUsers")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return getRegisteredUsers((String)arguments.elementAt(0));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }
            }
            else if (methodName.equals("addKoboldUser")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return addKoboldUser((String)arguments.elementAt(0),
                                         (String)arguments.elementAt(1),
                                         (String)arguments.elementAt(2),
                                         (String)arguments.elementAt(3));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }            	
            }
            else if (methodName.equals("removeKoboldUser")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return removeKoboldUser((String)arguments.elementAt(0),
                                            (String)arguments.elementAt(1));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }               
            }
            else if (methodName.equals("checkUserAssignements")){
                try{
                    logger.info("Recieved admincall \"" + methodName + "\"\n");
                    // admin method so leave without noticing the WorkflowEngine
                    return checkUserAssignements((String)arguments.elementAt(0),
                                                 (String)arguments.elementAt(1));
                }
                catch(Exception e){
                    logger.info("Exception during execute()", e);
                    return IKoboldServerAdministration.RETURN_FAIL;
                }               
            }
		} catch (Exception e) {
			logger.info("Exception during execute()", e);
			throw e;	
		}
		return IKoboldServer.NO_RESULT;
	}// end execute()

/*------------------------------------------------------------------------------
    The following section contains the IKoboldServer-methods' implementation 
    which are called by 'execute().
------------------------------------------------------------------------------*/
	/**
	 * {@see kobold.common.controller.IKoboldServer#login(String, String)}
	 */
	public UserContext login(URL url, String userName, String password) {
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
	public Vector getAllUsers(UserContext userContext) {
	    List users = UserManager.getInstance().getAllUsers();
	    Vector result = new Vector();
	    
	    for (Iterator iterator = users.iterator(); iterator.hasNext(); ) {
	        result.add((User)iterator.next());
//	        kobold.server.data.User user = (kobold.server.data.User) iterator.next();
//	        result.add(new User(user.getUserName(), user.getFullName()).serialize());
	    }
	    
		return result;
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#updateUserFullName(kobold.common.data.UserContext, kobold.common.data.User, java.lang.String)
	 */
	public void updateUserFullName(UserContext userContext, User user, String password) {
	    UserManager manager = UserManager.getInstance();
	    kobold.server.data.User serverUser = manager.getUser(userContext.getUserName());
	    if (userContext.getUserName().equals(user.getUsername())
	        && serverUser.getUserName().equals(userContext.getUserName())
	        && password.equals(serverUser.getPassword())) 
	    {
	        serverUser.setFullName(user.getFullname());
	    }
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#updateUserPassword(kobold.common.data.UserContext, kobold.common.data.User, java.lang.String, java.lang.String)
	 */
	public void updateUserPassword(UserContext userContext, User user,
	        	                   String oldPassword, String newPassword)
	{
	    UserManager manager = UserManager.getInstance();
	    kobold.server.data.User serverUser = manager.getUser(userContext.getUserName());
	    if (userContext.getUserName().equals(user.getUsername())
	        && serverUser.getUserName().equals(userContext.getUserName())
	        && oldPassword.equals(serverUser.getPassword())) 
	    {
	        serverUser.setPassword(newPassword);
	    }
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#removeUser(kobold.common.data.UserContext, kobold.common.data.User)
	 */
	public void removeUser(UserContext userContext, User user) {
	    UserManager manager = UserManager.getInstance();
	    kobold.server.data.User serverUser = manager.getUser(user.getUsername());
	    UserManager.getInstance().removeUser(serverUser);
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#addUser(UserContext, String, String, String)}
	 */
	public void addUser(UserContext userContext, String userName,
						String password, String fullName)
	{
		UserManager.getInstance().addUser(
		        new kobold.server.data.User(userName, fullName, password));
	}
	
	/**
	 * {@see kobold.common.controller.IKoboldServer#getProductline(UserContext, String)}
	 */
	public Productline getProductline(UserContext userContext, String plId) {
	    logger.debug("gathering productline with uc: " + userContext.getUserName()
	                 + "pl id:" + plId);
		ProductlineManager productlineManager = ProductlineManager.getInstance();
		return productlineManager.getProductline(plId);
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#updateProductline(UserContext, Productline)}
	 */
	public void updateProductline(UserContext userContext, Productline productline) {
	    ProductlineManager productlineManager = ProductlineManager.getInstance();
	    productlineManager.removeProductline(productline.getId());
	    productlineManager.addProductline(productline);
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#getProductlineNames(kobold.common.data.UserContext)
	 */
	public Vector getProductlineNames(UserContext userContext) {
	    ProductlineManager productlineManager = ProductlineManager.getInstance();
	    return productlineManager.getProductlineNames();
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#sendMessage(UserContext, AbstractKoboldMessage)}
	 */
	public void sendMessage(UserContext userContext, AbstractKoboldMessage koboldMessage) {
		if (koboldMessage instanceof WorkflowMessage) {
			WorkflowEngine.applWorkflow((WorkflowMessage)koboldMessage);
		}
		else {
			MessageManager.getInstance().sendMessage(koboldMessage);
		}
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#fetchMessage(UserContext)}
	 */
	public AbstractKoboldMessage fetchMessage(UserContext userContext) {
		return MessageManager.getInstance().fetchMessage(userContext.getUserName());
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#invalidateMessage(UserContext, AbstractKoboldMessage)}
	 */
	public void invalidateMessage(UserContext userContext, AbstractKoboldMessage koboldMessage) {
		MessageManager.getInstance().invalidateMessage(userContext.getUserName(), koboldMessage);
	}
	
	
/*------------------------------------------------------------------------------
     The following section contains the IKoboldServerAdministration-methods'
     implementation which are called by 'execute().
------------------------------------------------------------------------------*/
	/**
	 * this method is used to check if the called Kobold server is reachable 
	 * and can be administrated with the passed password
	 *  
	 * @param adminPassword the Kobold server's administration password
	 * 
	 * @return IKoboldServerAdministration.RETURN_OK, if the check was 
	 *         successful, IKoboldServerAdministration.RETURN_FAIL if the passed
     *         adminPassword is wrong
	 */
	public String checkAdministrability(String adminPassword){
	    return (adminPassword.equals(SecureKoboldWebServer.adminPassword)) ?
		       IKoboldServerAdministration.RETURN_OK :
		       IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
	}
	
	/**
	 * call this method if you want to create a new productline on the Kobold
	 * server. Note that if there already exists a productline with the name
	 * 'nameOfProductline, the method will exit with an error. 
	 * 
	 * @param adminPassword the Kobold server's administration password
	 * @param nameOfProductline the new productline's desired name
	 * @param resource name of file or directory.
	 * @param repositoryDescriptor description of the repository for the new
	 *                             productline
	 * 
	 * @return IKoboldServerAdministration.RETURN_OK, if the check was 
	 *         successful, IKoboldServerAdministration.RETURN_FAIL if an error
	 *         occured while executing the method on the server. 
	 */
	public String newProductline(String adminPassword, 
			String nameOfProductline, 
			String resource,
			RepositoryDescriptor repositoryDescriptor){
		// 1.) check the password
		if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
			return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
		}
		
		// 2.) create the new pl
		ProductlineManager plm = ProductlineManager.getInstance();
		Productline pl = new Productline(nameOfProductline, resource,
				    					 repositoryDescriptor);
		return plm.addProductline(pl) ? RETURN_OK : RETURN_NAME_ALREADY_REGISTERED;
	}
	
	/**
	 * This method removes the productline with the passed name from the server.
     *   
	 * @param adminPassword the Kobold server's administration password
	 * @param nameOfProductline name of the productline to remove
	 * 
	 * @return IKoboldServerAdministration.RETURN_OK, if the productline could 
	 *         be removed, IKoboldServerAdministration.RETURN_FAIL if not
	 */
	public String invalidateProductline(String adminPassword, 
			String nameOfProductline){
		// 1.) check the password
		if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
			return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
		}
        
		// 2.) remove the productline
      	return ProductlineManager.getInstance().removeProductlineByName(nameOfProductline) ? RETURN_OK : RETURN_PRODUCTLINE_MISSING;
	} 
	
	/**
	 * This method assigns a user to a productline (users that are assigned
	 * directly to productlines act as the productline's ple). 
	 * 
	 * @param adminPassword the Kobold server's administration password
	 * @param nameOfProductline name of the productline that should be assigned
	 *                          a new PLE
	 * @param nameOfUser name of the user that should be assigned to the 
	 *                   specified productline
	 * 
	 * @return IKoboldServerAdministration.RETURN_OK, if the passed user could
     *         be assigned, IKoboldServerAdministration.RETURN_FAIL otherwise
	 */
	public String assignPle(String adminPassword,
			String nameOfProductline,
			String nameOfUser){
		// 1.) check the password
		if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
			return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
		}
		
		// 2.) get the user's server representation by its name 
		ProductlineManager plm = ProductlineManager.getInstance();
		UserManager um = UserManager.getInstance();
		kobold.server.data.User suser = um.getUser(nameOfUser);
        
        // 3.) stop if the user doesn't exist
        if (suser == null){
            return IKoboldServerAdministration.RETURN_USER_MISSING;
        }
		
		// 4.) convert the user-object to its client-representation
		User cuser = suser.getSecureRepresentation();

        // 5.) stop if the productline does not exist
        Productline pl = plm.getProductlineByName(nameOfProductline);
        
        if (pl == null){
            return IKoboldServerAdministration.RETURN_PRODUCTLINE_MISSING;
        }

        // 6.) stop if the user has already been assigned
        if (pl.isMaintainer(nameOfUser)){
            return IKoboldServerAdministration.RETURN_OK;
        }
        
		// 7.) assign the user
		pl.addMaintainer(cuser);
        ProductlineManager.getInstance().serialize();
		
		return IKoboldServerAdministration.RETURN_OK;
	}
	
	/**
	 * This methods unassigns a user (ple) from a productline. 
	 * 
	 * @param adminPassword the Kobold server's administration password
	 * @param nameOfProductline name of the productline that should "loose"
	 *                          the specified user
	 * @param nameOfUser username specifiing which user should be unassigned 
	 * 
	 * @return IKoboldServerAdministration.RETURN_OK, if the user could be 
     *         unassigned, IKoboldServerAdministration.RETURN_FAIL otherwise
	 */
	public String unassignPle(String adminPassword, 
                              String nameOfProductline,
                              String nameOfUser){
		// 1.) check the password
		if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
			return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
		}
		
        // 2.) get the user's server representation by its name 
        ProductlineManager plm = ProductlineManager.getInstance();
        UserManager um = UserManager.getInstance();
        kobold.server.data.User suser = um.getUser(nameOfUser);
        
        // 3.) stop if the user doesn't exist
        if (suser == null){
            return IKoboldServerAdministration.RETURN_USER_MISSING;
        }
        
        // 4.) convert the user-object to its client-representation
        User cuser = suser.getSecureRepresentation();
        
        // 5.) stop if the productline doesn't exist
        Productline pl = plm.getProductlineByName(nameOfProductline);
        
        if (pl == null){
            return IKoboldServerAdministration.RETURN_PRODUCTLINE_MISSING;
        }

        // 6.) stop if the user has not yet been assigned to the productline
        if (!pl.isMaintainer(nameOfUser)){
            return IKoboldServerAdministration.RETURN_OK;
        }
        
        // 7.) unassign the user
        pl.removeMaintainer(cuser);
        ProductlineManager.getInstance().serialize();
        
		return IKoboldServerAdministration.RETURN_OK;
	}
    
    /**
     * This method returns the usernames of all users that are assigned to
     * the specified productline.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param nameOfProductline name of the productline whose maintainers' names
     *        should be returned
     * @return String containing all assigned usernames (seperated by '\n') or
     *         one of the IKoboldServerAdministration error strings
     */
    public String getPles(String adminPassword, String nameOfProductline){
        // 1.) check the password
        if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
            return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
        }
        
        // 2.) check if productline exists
        ProductlineManager plm = ProductlineManager.getInstance();
        Productline pl = plm.getProductlineByName(nameOfProductline);
        
        if (pl == null){
            return IKoboldServerAdministration.RETURN_PRODUCTLINE_MISSING;
        }
        
        // 3.) get maintainer list and convert it to string
        List ml = pl.getMaintainers();
        Iterator it = ml.iterator();
        String ret = "";
        
        if (!it.hasNext()){
            ret += "no users assigned\n";
        }
        else while(it.hasNext()){
            ret += ((User)it.next()).getUsername() + "\n";
        }
        
        return ret; 
    }
    
    /**
     * This method returns the names of all productlines that are currently 
     * registered on the called Kobold server.
     *  
     * @param adminPassword the Kobold server's administration password
     * @return String containing all the registered productlines' names
     *         (seperated by '\n')  or one of the IKoboldServerAdministration 
     *         error strings
     */
    public String getProductlines(String adminPassword){
        // 1.) check the password
        if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
            return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
        }
        
        // 2.) get productline vector, remove id strings and convert it to string
        Vector v = ProductlineManager.getInstance().getProductlineNames();
        Iterator it = v.iterator();
        String ret = "";

        if (!it.hasNext()){
            ret += "no productlines registered\n";
        }
        else while(it.hasNext()){
            it.next(); // removes id string of next productline
            ret += it.next() + "\n";
        }
                
        return ret; 
    }
    
    /**
     * This method returns the usernames of all registered users.
     * 
     * @param adminPassword the Kobold server's administration password
     * @return String containing all registered usernames (seperated by '\n')
     *         or one of the IKoboldServerAdministration error strings
     */
    public String getRegisteredUsers(String adminPassword){
        // 1.) check the password
        if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
            return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
        }
        
        // 2.) get userlist and convert it to string
        UserManager um = UserManager.getInstance();
        List ul = um.getAllUsers();
        Iterator it = ul.iterator();
        String ret = "";
        
        if (!it.hasNext()){
            ret += "no users registered\n";
        }
        else while(it.hasNext()){
            ret += ((kobold.common.data.User)it.next()).getUsername() + "\n";
        }
        
        return ret;
    }
    
    /**
     * This method creates a new kobold user and adds it to the server.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param username the new user�s username
     * @param fullname the new user's full name
     * @param password the password for the new user
     * @return RETURN_OK if the new user could be created successfully, 
     *         RETURN_FAIL if the server encountered an error (e.g. if the
     *         passed username has already been assigned) or 
     *         RETURN_SERVER_UNREACHABLE if the Kobold server could not be
     *         reached
     */
    public String addKoboldUser(String adminPassword, 
                                String username, 
                                String fullname,
                                String password){
        // 1.) check the password
        if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
            return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
        }
        
        // 2.) create and add user
        kobold.server.data.User user = new kobold.server.data.User(username,
                fullname,
                password);

        if (UserManager.getInstance().addUser(user)){
            return RETURN_OK;
        }
        else{
            return RETURN_NAME_ALREADY_REGISTERED;
        }
    }
    
    /**
     * This method removes a registered user from the Kobold server
     * 
     * NOTE: this method will remove the specified User even if it has been
     * assigned to specific assets.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param username username of the user that should be removed
     * @return RETURN_OK if the user could be removed successfully,
     *         RETURN_FAIL if the server encountered an error (e.g. if the
     *         passed username has not been registered) or
     *         RETURN_SERVER_UNREACHABLE if the Kobold server could not be
     *         reached
     */
    public String removeKoboldUser(String adminPassword, String username){
        // 1.) check the password
        if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
            return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
        }
        
        // 2.) remove the user
        if (UserManager.getInstance().removeUserByName(username) == null){
            // the passed username has not been registered
            return RETURN_USER_MISSING;
        }
        else{
            return RETURN_OK;
        }
    }
    
    /**
     * @param adminPassword the Kobold server's administration password
     * @param username name of the user that should be checked for assignements
     * @return RETURN_USER_ASSIGNED if the specified user is still assigned to
     *         at least one asset, RETURN_USER_NOT_ASSIGNED if the specified
     *         user is not assigned to any asset, RETURN_FAIL if the passed
     *         adminPassword has not been accepted
     */
    public String checkUserAssignements(String adminPassword, String username){
        // 1.) check the password
        if (!checkAdministrability(adminPassword).equals(RETURN_OK)){
            return IKoboldServerAdministration.RETURN_WRONG_PASSWORD;
        }

        // 2.) check for assignements
        return UserManager.getInstance().isAssignedToAsset(username) ?
                RETURN_USER_ASSIGNED : RETURN_USER_NOT_ASSIGNED;
    }    
}