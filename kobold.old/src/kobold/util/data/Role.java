package kobold.util.data;

import java.lang.String;

/**
 * this is the base class for RoleP, -PE and -PLE. a Role-Object is always
 * associated to a specific user and stores information about the user's
 * permissions that are related to that Role
 *
 * @author Armin Cont
 */
public class Role {

/**
 * name of associated user
 */
private String user;

/**
 * String that describes the related permissions
 */
private String permissions;
  
/**
 * @return the associated user's name
 */
public String getUser() {
  return null;
  }

/**
 * associates the Roleobject with an user
 *  
 * @param user the user to associate
 */
public void setUser(String user) {
  }

/**
 * @return permissions related to this Role
 */
public String getPermissions() {
  return null;
  }

/**
 * sets the permissions for this Role
 * 
 * @param perm - the permissions
 */
public void setPermissions(String perm) {
  }
}
