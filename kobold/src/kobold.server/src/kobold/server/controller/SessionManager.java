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
 * $Id: SessionManager.java,v 1.1 2004/05/15 01:24:17 garbeam Exp $
 *
 */
package kobold.server.controller;

import java.util.HashMap;

import kobold.common.data.IdManager;
import kobold.common.data.User;
import kobold.common.data.UserContext;
/**
 * This class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs). It's a
 * singleton class.
 *
 * @author garbeam
 */
public class SessionManager {

	HashMap sessions;
	
	static private SessionManager instance;
	 
	static public SessionManager getInstance() {
		 if (instance == null ) {
		 	 instance = new SessionManager();
		 }
		 return instance;
	}
	
	/**
	 * Basic constructor of this singleton.
	 * @param path
	 */
	private SessionManager() {
		sessions = new HashMap();
	}
	
	/**
	 * Login handler.
	 * 
	 * @param userName the user name.
	 * @param password the password for the session.
	 */
	public UserContext login(String userName, String password) {

		IdManager idManager = IdManager.getInstance();
		UserManager userManager = UserManager.getInstance();

		User user = userManager.getUser(userName);
		if ((user != null) && (password.equals(user.getPassword()))) {
			String sessionId = idManager.getSessionId(userName);
			UserContext userContext = user.getInitialUserContext(sessionId);
			sessions.put(userName, userContext);
			MessageManager.getInstance().registerQueue(userContext);
			return userContext;
		}
		return null;
	}
	
	/**
	 * Logout handler.
	 * Invalidates the given user context.
	 * @param userContext the user context.
	 */
	public void logout(UserContext userContext) {
		MessageManager.getInstance().unregisterQueue(userContext);
		sessions.remove(userContext.getUserName());
		userContext.setSessionId(null);
	}

	/**
	 * @param userName the user name.
	 * @return current UserContext for userName.
	 */
	public UserContext getUserContextForUserName(String userName) {
		return (UserContext)sessions.get(userName);
	}
}