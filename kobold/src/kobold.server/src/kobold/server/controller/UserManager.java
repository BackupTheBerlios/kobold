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
 * $Id: UserManager.java,v 1.23 2004/09/23 13:43:14 vanto Exp $
 *
 */
package kobold.server.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import kobold.common.data.Asset;
import kobold.server.data.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * This singleton class manages the users registered on the server.
 */
public class UserManager {

    private static final Log log = LogFactory.getLog(UserManager.class);

	private HashMap users = null;
	private String userStore = "users.xml";
	
	static private UserManager instance;
	 
	static public UserManager getInstance() {
		 if (instance == null ) {
		 	 instance = new UserManager();
		 }
		 return instance;
	}
    
	/**
	 * Basic constructor of this singleton.
     * TODO: remove the dummy-call before delivery
	 */
	private UserManager() {
		users = new HashMap();
		this.userStore =
			System.getProperty("kobold.server.storePath") +
			System.getProperty("kobold.server.userStore");
		deserialize();
		// DEBUG
		dummyUsers();
	}
	
	/**
	 * Adds a new user. Please note that adding will be refused if there has
     * already been registered a user with the same username as the one to add.
	 *
	 * @param user the User object that is to be added
     * @return true, if the new User could successfully be added, false 
     *         otherwise (if the new User's username was already registered)
	 */
	public boolean addUser(User user) {
		Object o = users.put(user.getUserName(), user);
        
        if (o != null){
            // obviously a user with the same name as the one to add has already
            // been registered => undo the change and signal error
            users.put(user.getUserName(), o);
            serialize();
            return false;
        }
        else{
            serialize();
        	return true;
        }
	}

	/**
	 * Returns a User by its username.
	 *
	 * @param username the name of the User to be returned
     * @return the User object associated with the passed username or null, if
     *         the passed username has not been registered
	 */
	public User getUser(String username) {
		return (User) users.get(username);
	}

	/**
	 * Removes the passed user. Note that a user will be removed even if it is 
     * still assigned to an asset (in that case all assignements of this user 
     * will be silently removed to avoid data inconsistencies within the Kobold 
     * system).
     * 
     * Use 'isAssignedToAsset() if you like to check a user for assignements. 
     * 
     * @see removeUserByName()
     * 
     * @param user User object containing the username of the User object that 
     *        is to be removed
     * @return the removed User object or null, if the passed User object's 
     *         username hasn't been registered
	 */
	public User removeUser(User user) {
		return removeUserByName(user.getUserName());
	}

    /**
     * Removes the user with the passed username. Note that a user will be
     * removed even if it is still assigned to an asset (in that case all 
     * assignements of this user will be silently removed to avoid data
     * inconsistencies within the Kobold system).
     * 
     * Use 'isAssignedToAsset() if you like to check a user for assignements. 
     * 
     * @param username username of the user that should be removed
     * @return the removed User object or null, if the passed username hasn't 
     *         been registered
     */
    public User removeUserByName(String username){
        if (isAssignedToAsset(username)){
            unassignFromAllAssets(username);
        }
        
        User ret = (User) users.remove(username);
        serialize();
        return ret;
    }
    
    /**
     * This method checks if the user specified by the passed username is still
     * assigned to at least one assets.
     * 
     * @param username name of the user to check for removability
     * @return true if the specified user is still assigned to at least one 
     *         asset, false otherwise or if the specified user doesn't exist 
     */
    public boolean isAssignedToAsset(String username){
        // 1.) check if the passed username is registered
        if (getUser(username) == null){
            return false;
        }
        
        // 2.) check if the user is still assigned to any asset
        Iterator it = ProductlineManager.getInstance().getAllRegisteredAssets().iterator();
        while(it.hasNext()){
            if (((Asset)it.next()).isMaintainer(username)){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * The following method unassigns the user specified by 'username from all
     * assets.
     * 
     * @param username name of user that should be unassigned from all assets
     */
    public void unassignFromAllAssets(String username){
        // 1.) stop if the passed user doesn't exist
        if (getUser(username) == null){
            return;
        }
        
        // 2.) unassign from all Assets
        kobold.common.data.User user = getUser(username).getSecureRepresentation();
        Iterator it = ProductlineManager.getInstance().getAllRegisteredAssets().iterator();
        while(it.hasNext()){
            ((Asset)it.next()).removeMaintainer(user);
        }
        
        // 3.) persist changes
        ProductlineManager.getInstance().serialize();
    }
    
	/**
	 * Serializes all users with its roles to the file specified
	 * by 'userStore.
	 */
	public void serialize() {
		serialize(this.userStore);
	}
	
	/**
	 * Serializes all users with its roles to the file specified
	 * by path.
	 * 
	 * @param path the file to serialize all users.
	 */
	public void serialize(String path) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("kobold");

		Element users = root.addElement("users");

		for (Iterator it = this.users.values().iterator(); it.hasNext();) {
			User user = (User) it.next();
            users.add(user.serialize());
		}

		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(path));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			Log log = LogFactory.getLog("kobold.server.controller.UserAdmin");
			log.error(e);
		}

	}

	/**
	 * Deserializes all users from the file specified by 'userStore.
	 */
	public void deserialize() {
		deserialize(this.userStore);
	}
	
	/**
	 * Deserializes all users from the specified file.
     * 
     * @param path path to the file form which to deserialize
	 */
	public void deserialize(String path) {
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(path);
			Element users = document.getRootElement().element("users");
			
			for (Iterator iterator = users.elementIterator("user"); iterator.hasNext(); ) {
			    Element element = (Element) iterator.next();
			    User user = new User(element);
			    this.users.put(user.getUserName(), user);
			}
		} catch (DocumentException e) {
			log.error(e);
		}
	}
	
	/**
	 * Returns a list of {@see kobold.common.data.User} users.
     * 
     * @return List of {@see kobold.common.data.User} users representing all the
     *         Users currently registered on the server
	 */
	public List getAllUsers() {
	    List result = new ArrayList();
	    
	    for (Iterator iterator = users.values().iterator(); iterator.hasNext(); ) {
            result.add(((User)iterator.next()).getSecureRepresentation());
	    }
	    
	    return result;
	}
	
	// DEBUG
	private void dummyUsers() {
	    addUser(new User("garbeam", "Anselm", "garbeam"));
	    addUser(new User("vanto", "Tammo", "vanto"));
	    addUser(new User("schneipk", "Patrick", "schneipk"));
	}
}
