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
 * $Id: User.java,v 1.2 2004/04/08 13:53:26 garbeam Exp $
 *
 */


package kobold.server.user;

/**
 * This class contains all information about a user that has to be stored
 * on the Kobold Server (password, roles...).
 *
 * @author Armin Cont
 */
public class User {

    private UserInfo info;

    /**
     *
     * Use to get the stored USerInfo
     * @return the stored UserInfo
     */
    public UserInfo getInfo() {
        return null;
    }

    /**
     *
     * Overwrites the stored UserInfo with "info". If you intend to change
     * a user's data, first get the actual USerInfo by calling getInfo()
     * then perform your changes on that object and pass it to this method.
     *
     * @param info the new UserInfo
     */
    public void applyUserInfo(UserInfo info) {
    }

    /**
     * Adds a new Role to this user's role list. NOTE: if the passed Role
     * object is not associated with this user (that is the Role's user-
     * attribute equals the user-object's username), addRole() is ignored.
     * 
     * @param r the Role to add
     */
    public void addRole(Role r) {
    }

    /**
     * Removes the passed Role form the user's role list. if the passed
     * role is not part of the user's role list this method has no effect.
     *
     * @param r the role to remove
     */
    public void removeRole(Role r) {
    }
}
