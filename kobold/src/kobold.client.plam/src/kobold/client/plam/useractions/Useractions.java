/*
 * Created on 23.06.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.useractions;

import kobold.client.plam.controller.*;
import kobold.common.data.*;
import kobold.client.plam.*;

/**
 * @author grosseml
 *	This class combines all useractions to provide an abstract
 *	layer.
 * 
 */
public class Useractions {
	
	/**
	 * If password and confirmpassword are equal the method creates
	 * a new User
	 * @param realName
	 * @param userName
	 * @param password
	 * @param confirmPassword
	 * @return boolean
	 */
	public boolean createUser(String realName, String userName,
			String password, String confirmPassword){
		if (!password.equals(confirmPassword)){
			return false;
		}
		
        SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();		
        UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
        
		client.addUser(currUser,userName,password,realName);
		
		return true;
		
	}
	
	public void removeUser(String userName){
        SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();		
        UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
        client.removeUser(currUser,userName);
	}
	
	public void changePassword(String password, String confirmPassword){
		if(password.equals(confirmPassword)){
			SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();
			UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
			client.changeUserPassword(currUser, password);
		}
	}
	
	/**
	 * Creates a new role and makes then the call to add a role
	 * @author grosseml
	 *
	 */
	public void addRole(String userName, String productName, String roleType){
		SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();
		UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
		
		Role newRole;
		
		if(roleType.equals("P")){
			newRole = new RoleP(productName);
		}
		else if (roleType.equals("PE")){
			newRole = new RolePE(productName);
		}
		else newRole = null;
		
		if (newRole!=null){
			client.addRole(currUser, userName, newRole);
		}
	}
	
	public void removeRole(String userName, String productName){
		SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();
		UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
		
		Role oldRole;
		
		oldRole = client.getProductRole(currUser, userName, productName);
		client.removeRole(currUser,userName,oldRole);
	}
	
}


