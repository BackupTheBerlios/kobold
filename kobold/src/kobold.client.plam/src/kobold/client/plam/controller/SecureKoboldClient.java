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
 * $Id: SecureKoboldClient.java,v 1.32 2004/08/02 14:36:03 vanto Exp $
 *
 */
package kobold.client.plam.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import kobold.common.controller.IKoboldServer;
import kobold.common.controller.RPCMessageTransformer;
import kobold.common.data.AbstractKoboldMessage;
import kobold.common.data.Productline;
import kobold.common.data.User;
import kobold.common.data.UserContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcClientException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.secure.SecureXmlRpcClient;
import org.dom4j.Element;

import sun.misc.BASE64Decoder;

/**
 */
/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SecureKoboldClient implements IKoboldServer {

	private static final Log log =
		LogFactory.getLog("kobold.client.controller.SecureKoboldClient");
	private static SecureKoboldClient instance = null;
	
	private Map clientByUrlPool = new HashMap();

	
	public static SecureKoboldClient getInstance() {
	    if (instance == null) {
	        instance = new SecureKoboldClient();
	    }
	    return instance;
	       
	}

	private SecureXmlRpcClient getClientByUrl(URL url) {
	    SecureXmlRpcClient client = (SecureXmlRpcClient) clientByUrlPool.get(url);
	    if (client == null) {
	        client = new SecureXmlRpcClient(url);
	        clientByUrlPool.put(url, client);
	    }
	    return client;
	}
	
	/**
	 * {@see kobold.common.controller.IKoboldServer#login(String, String)}
	 */
	public UserContext login(URL url, String userName, String password) {
		Vector v = new Vector();
		v.add(userName);
		v.add(password);
		try {
			Object result = getClientByUrl(url).execute("login", v);
			if (!((String)result).equals(IKoboldServer.NO_RESULT)) {
				Element element = RPCMessageTransformer.decode((String)result);
				UserContext uc = new UserContext(element);
				uc.setServerUrl(url);
				return uc;
			}
		} catch (Exception exception) {
			log.error("login()", exception);
		}
		return null;
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#logout(UserContext)}
	 */
	public void logout(UserContext userContext) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("logout", v);
		} catch (Exception exception) {
			log.error("logout()", exception);
		}
	}

	/**
	 * {@see kobold.common.controller.IKoboldServer#addUser(UserContext, String, String, String)}
	 */
	public void addUser(UserContext userContext, String userName,
									String password, String realName)
	{
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(userName);
		v.add(password);
		v.add(realName);
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("addUser", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}
	
	/**
	 * {@see kobold.common.controller.IKoboldServer#changeUser(UserContext userContext, String userName, String realName)}
	 */
	public void changeUser(UserContext userContext, String userName, String realName)
	{
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(userName);
		v.add(realName);
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("changeUser",v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}
	
	/**
	 * @see kobold.common.controller.IKoboldServer#getProductline(kobold.common.data.UserContext, java.lang.String)
	 */
	public Productline getProductline(UserContext userContext, String plId) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(plId);
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("getProductline", v);
			if (!((String)result).equals(IKoboldServer.NO_RESULT)) {
				System.err.println(result);
				System.err.println(RPCMessageTransformer.decode((String)result));
				return new Productline(RPCMessageTransformer.decode((String)result));
			}
		} catch (Exception exception) {
			log.error("getProductline()", exception);
		}
		return null;
	}
	
	/**
	 * @see kobold.common.controller.IKoboldServer#sendMessage(kobold.common.data.UserContext, kobold.common.data.AbstractKoboldMessage)
	 */
	public void sendMessage(UserContext userContext, AbstractKoboldMessage koboldMessage) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(RPCMessageTransformer.encode(koboldMessage.serialize()));
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("sendMessage", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#fetchMessage(kobold.common.data.UserContext)
	 */
	public AbstractKoboldMessage fetchMessage(UserContext userContext) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("fetchMessage", v);
			if (!((String)result).equals(IKoboldServer.NO_RESULT)) {
				return AbstractKoboldMessage.createMessage(RPCMessageTransformer.decode((String)result));
			}
		} catch (Exception exception) {
			log.error(exception);
		}
		return null;
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#invalidateMessage(kobold.common.data.UserContext, kobold.common.data.AbstractKoboldMessage)
	 */
	public void invalidateMessage(UserContext userContext, AbstractKoboldMessage koboldMessage) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(RPCMessageTransformer.encode(koboldMessage.serialize()));
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("invalidateMessage", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#getProductlineNames(kobold.common.data.UserContext)
	 */
	public Vector getProductlineNames(UserContext userContext) {
	    Vector result = null;
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		try {
			result = (Vector) getClientByUrl(userContext.getServerUrl()).execute("getProductlineNames", v);
			log.info(result);
		} catch (Exception exception) {
			log.error(exception);
		}
		return result;
	}
	
	/**
	 * @see kobold.common.controller.IKoboldServer#getAllUsers(kobold.common.data.UserContext)
	 */
	public Vector getAllUsers(UserContext userContext) {
	    Vector result = new Vector();
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		try {
			Vector vector = (Vector) getClientByUrl(userContext.getServerUrl()).execute("getAllUser", v);
			
			for (Iterator iterator = vector.iterator(); iterator.hasNext();) {
			    User user = new User(RPCMessageTransformer.decode((String) iterator.next()));
			    result.add(user);
			}
		    log.info(result);
		} catch (Exception exception) {
			log.error("getAllUsers()", exception);
		}
		return result;
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#removeUser(kobold.common.data.UserContext, kobold.common.data.User)
	 */
	public void removeUser(UserContext userContext, User user) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(RPCMessageTransformer.encode(user.serialize()));
		try {
			Object result = (Object) getClientByUrl(userContext.getServerUrl()).execute("removeUser", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	/**
	 * @see kobold.common.controller.IKoboldServer#updateProductline(kobold.common.data.UserContext, kobold.common.data.Productline)
	 */
	public void updateProductline(UserContext userContext, Productline productline) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(RPCMessageTransformer.encode(productline.serialize()));
		try {
			Object result = (Object) getClientByUrl(userContext.getServerUrl()).execute("updateProductline", v);
		} catch (Exception exception) {
			log.error(exception);
		}
	}

	private class AdaptedSecureXmlRpcClient extends SecureXmlRpcClient {
		
		/**
		 */
		public AdaptedSecureXmlRpcClient(URL url) {
			super(url);
		}
		
		/**
		 * @see org.apache.xmlrpc.XmlRpcHandler#execute(java.lang.String, java.util.Vector)
		 */
		public Object execute(String arg0, Vector arg1)
		throws XmlRpcException, IOException {
			String response = (String)super.execute(arg0, arg1);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(new BASE64Decoder().decodeBuffer(response)));
			try {
				return ois.readObject();
			} catch (ClassNotFoundException e) {
				log.error("Could not read data stream", e);
				throw new XmlRpcClientException("Unkown class", e);
			}
		}
	}


    /**
     * @see kobold.common.controller.IKoboldServer#updateUserFullName(kobold.common.data.UserContext, kobold.common.data.User, java.lang.String)
     */
    public void updateUserFullName(UserContext userContext, User user, String password) {
  
    	//changeUser(userContext, user.getUsername(), user.getFullname());
        Vector v = new Vector();
        v.add(RPCMessageTransformer.encode(userContext.serialize()));
        v.add(RPCMessageTransformer.encode(user.serialize()));
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("updateUserFullname",v);
		} catch (Exception exception) {
			log.error(exception);
		}        
    }

    /**
     * @see kobold.common.controller.IKoboldServer#updateUserPassword(kobold.common.data.UserContext, kobold.common.data.User, java.lang.String, java.lang.String)
     */
    public void updateUserPassword(UserContext userContext, User user, String oldPassword, String newPassword) {
		Vector v = new Vector();
		v.add(RPCMessageTransformer.encode(userContext.serialize()));
		v.add(RPCMessageTransformer.encode(user.serialize()));
		v.add(oldPassword);
		v.add(newPassword);
		try {
			Object result = getClientByUrl(userContext.getServerUrl()).execute("updateUserPassword",v);
		} catch (Exception exception) {
			log.error(exception);
		}
     }

}

