/*
 * Created on 23.06.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package kobold.client.plam.useractions;

import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.controller.SecureKoboldClient;
import kobold.client.plam.controller.ServerHelper;
import kobold.common.data.User;
import kobold.common.data.UserContext;

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
		
        UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());	
		SecureKoboldClient.getInstance().addUser(ctx,userName,password,realName);
		
		return true;
		
	}
	
	public void removeUser(String userName){
        User user = new User("Username","mismatch"); //the user to be removed
		
        UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
        SecureKoboldClient.getInstance().removeUser(ctx, getOneUser(userName));
	}
	
	public void changePassword(String newPassword, String confirmPassword){
		if(newPassword.equals(confirmPassword)){
		    UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
		    SecureKoboldClient.getInstance().updateUserPassword(ctx, getOneUser(ctx.getUserName()), confirmPassword, newPassword);
		}
	}
	
	public void updateFullName(String userName, String newName, String password){
		
	    UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());        
        User user = getOneUser(userName);
        
        user.setFullname(newName);
        
        SecureKoboldClient.getInstance().updateUserFullName(ctx, user , password);
		
	}
	

	
	
	/*
	 * returns one User
	 */
	private User getOneUser(String userName){
        User user = null;
		
        UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
        List userList = SecureKoboldClient.getInstance().getAllUsers(ctx);
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


