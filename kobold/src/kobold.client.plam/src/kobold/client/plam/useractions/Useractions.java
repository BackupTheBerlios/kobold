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
        User user = new User("Username","mismatch"); //the user to be removed
		
		SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();		
        UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();

        client.removeUser(currUser,getOneUser(userName));
	}
	
	public void changePassword(String newPassword, String confirmPassword){
		if(newPassword.equals(confirmPassword)){
			SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();
			UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
			client.updateUserPassword(currUser,getOneUser(currUser.getUserName()), confirmPassword,newPassword);
		}
	}
	
	public void updateFullName(String userName, String newName, String password){
		
		SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();		
        UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
        
        User user = getOneUser(userName);
        
        user.setFullname(newName);
        
        client.updateUserFullName(currUser, user , password);
		
	}
	
	
	/*
	 * returns one User
	 */
	private User getOneUser(String userName){
        User user = new User("Username","mismatch"); //the user to be removed
		
		SecureKoboldClient client = KoboldPLAMPlugin.getCurrentClient();		
        UserContext currUser = KoboldPLAMPlugin.getCurrentProjectNature().getUserContext();
        java.util.List userList = client.getAllUsers(currUser);
        for (int i = 0; i<userList.size();i++)
        {
        	User tempUser = (User)userList.get(i);
        	if(tempUser.getUsername().equals(userName))
        	{
        		user = tempUser;
        	}
        }
        return user;
	}
	
}


