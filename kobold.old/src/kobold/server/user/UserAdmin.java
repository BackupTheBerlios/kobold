package kobold.server.user;

import java.lang.String;

/**
 * this class stores user data on the server and provides authentification
 * services for user interaction with the Server (sessionIDs)
 *
 * @author Armin Cont
 */
public class UserAdmin {

/**
 * adds a new user 
 *
 * @param username String containing the new user's username
 */
public void addUser(String username) {
  }

/**
 * changes the stored information for the user specified in info
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
 * adds a Role to a user (rigth user is specified by the Role-Object)
 */
public void addRole(Role r) {
  }

/**
 * removes the geiven Role
 */
public void removeRole(Role r) {
  }

/**
 * removes the specified user
 */
public void removeUser(String user) {
  }

/**
 * creates a sessionID for the specified user (login)
 */
public String createSessionID(String username, String password) {
  return null;
  }

/**
 * releases the given sessionID (logout)
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
