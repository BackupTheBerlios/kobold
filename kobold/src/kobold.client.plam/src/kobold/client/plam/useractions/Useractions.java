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
}


