package kobold.common.data;

import org.dom4j.Element;

/**
 * this is the base class for RoleP, -PE and -PLE. a Role-Object is always
 * associated to a specific user and stores information about the user's
 * permissions that are related to that Role
 *
 * @author Armin Cont
 */
public class Role {

	protected static final short ROLE_PLE = 1;
	protected static final short ROLE_PE = 2;
	protected static final short ROLE_P = 2;

	private String permissions;
	private short type;

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

	/**
	 * Serializes this object.
	 * 
	 * @param roles DOM parent element to attach this role.
	 */
	public void serialize(Element roles) {
		String roleType = null;
		switch (type) {
			case 1 :
				roleType = "PLE";
				break;
			case 2 :
				roleType = "PE";
				break;
			case 3 :
				roleType = "P";
				break;
		}
		Element role = roles.addElement("role").addText(roleType);
	}
}
