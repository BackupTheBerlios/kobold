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
 * $Id: UserAdmin.java,v 1.2 2004/04/08 13:53:26 garbeam Exp $
 *
 */


package kobold.server.user;

import java.lang.String;

/**
 * This class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs).
 *
 * @author Armin Cont
 */
public class UserAdmin {

    /**
     * Adds a new user.
     *
     * @param username String containing the new user's username
     */
    public void addUser(String username) {
    }

    /**
     * Changes the stored information for the user specified in info.
     *
     * @param info userinfo that describes the new user info
     */
    public void applyUserInfo(UserInfo info) {
    }

    /**
     * @return the user's information
     */
    public UserInfo getUserInfo(String user) {
        return null;
    }

    /**
     * Adds a Role to a user (rigth user is specified by the Role-Object).
     */
    public void addRole(Role r) {
    }

    /**
     * Removes the geiven Role.
     */
    public void removeRole(Role r) {
    }

    /**
     * Removes the specified user.
     */
    public void removeUser(String user) {
    }

    /**
     * Creates a sessionID for the specified user (login).
     */
    public String createSessionID(String username, String password) {
        return null;
    }

    /**
     * Releases the given sessionID (logout).
     */
    public void releaseSessionID(String sessionID) {
    }

    /**
     * @return the user that is associated with the given sessionID
     */
    public String getUserFromSessionID(String sessionID) {
        return null;
    }
}
