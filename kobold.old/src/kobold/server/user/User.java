package kobold.server.user;


/**
 *
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
 *
 * adds a new Role to this user's role list. NOTE: if the passed Role
 * object is not associated with this user (that is the Role's user-
 * attribute equals the user-object's username), addRole() is ignored.
 * 
 * @param r the Role to add
 */
public void addRole(Role r) {
  }

/**
 *
 * removes the passed Role form the user's role list. if the passed
 * role is not part of the user's role list this method has no effect.
 *
 * @param r the role to remove
 */
public void removeRole(Role r) {
  }
}
