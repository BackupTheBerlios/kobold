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
 * $Id: SecureKoboldWebServer.java,v 1.4 2004/05/03 22:57:07 garbeam Exp $
 *
 */
package kobold.server;

import java.util.List;
import java.util.Vector;

import kobold.common.data.KoboldMessage;
import kobold.common.data.Product;
import kobold.common.data.Productline;
import kobold.common.data.Role;
import kobold.common.data.UserContext;
import kobold.server.controller.ServerInterface;

import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.secure.SecureWebServer;

/**
 * @author garbeam
 *
 * Implements the server interface and delegates all RPCs to its
 * appropriate methods.
 */
public class SecureKoboldWebServer implements ServerInterface, XmlRpcHandler {
	// the xml-rpc webserver
	static SecureWebServer server;

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
		if (methodName == "login") {
			return login((String)arguments.elementAt(0), (String)arguments.elementAt(1));
		}
		else if (methodName == "logout") {
			logout((UserContext)arguments.elementAt(0));
		}
	
	
		return null;
	}

	/**
	 * Logins the specified user and returns UserContext if everything
	 * works well.
	 * 
	 * @see kobold.server.controller.ServerInterface#login(java.lang.String, java.lang.String)
	 */
	public UserContext login(String userName, String password) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#logout(kobold.common.data.UserContext)
	 */
	public void logout(UserContext userContext) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getRoles(kobold.common.data.UserContext)
	 */
	public List getRoles(UserContext userContext) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addProductline(java.lang.String, java.lang.String)
	 */
	public void addProductline(String sessionID, String name) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addUser(java.lang.String, java.lang.String)
	 */
	public void addUser(String sessionID, String username) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getProductlineList(java.lang.String)
	 */
	public List getProductlineList(String sessionID) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getProductlineInfo(java.lang.String, java.lang.String)
	 */
	public Productline getProductlineInfo(String sessionID, String pl) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getProductInfo(java.lang.String, java.lang.String)
	 */
	public Product getProductInfo(String sessionID, String product) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addProduct(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addProduct(String sessionID, String name, String pl) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getUserContext(java.lang.String, java.lang.String)
	 */
	public UserContext getUserContext(String sessionID, String username) {
		// TODO Auto-generated method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addRole(java.lang.String, kobold.common.data.Role)
	 */
	public void addRole(String sessionID, Role r) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#applyPLInfo(java.lang.String, kobold.common.data.PLInfo)
	 */
	public void applyPLInfo(String sessionID, Productline pi) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#applyProductInfo(java.lang.String, kobold.common.data.PInfo)
	 */
	public void applyProductInfo(String sessionID, Product pi) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#applyUserInfo(java.lang.String, kobold.common.data.UserContext)
	 */
	public void applyUserInfo(String sessionID, UserContext uc) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeProductline(java.lang.String, java.lang.String)
	 */
	public void removeProductline(String sessionID, String pl) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeProduct(java.lang.String, java.lang.String)
	 */
	public void removeProduct(String sessionID, String product) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeRole(kobold.common.data.Role)
	 */
	public void removeRole(Role r) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeUser(java.lang.String, java.lang.String)
	 */
	public void removeUser(String sessionID, String username) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#commitMessage(kobold.common.data.UserContext, kobold.common.data.KoboldMessage)
	 */
	public void commitMessage(UserContext uc, KoboldMessage koboldMessage) {
		// TODO Auto-generated method stub
		
	}


	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#fetchMessage(kobold.common.data.UserContext)
	 */
	public KoboldMessage fetchMessage(UserContext uc) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
