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
 * $Id: UserManager.java,v 1.2 2004/08/25 10:00:27 garbeam Exp $
 *
 */
package kobold.client.plam.controller;

import java.util.List;

import kobold.client.plam.KoboldPLAMPlugin;
import kobold.client.plam.KoboldProject;
import kobold.common.data.User;
import kobold.common.data.UserContext;

/**
 * @author grosseml
 *	This class combines all useractions to provide an abstract
 *	layer.
 * 
 */
public class UserManager {
	
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
		
		KoboldProject kp = KoboldPLAMPlugin.getCurrentKoboldProject();
		kp.updateUserPool();
		
		return true;
		
	}
	
	public void removeUser(String userName){
        User user = new User("Username","mismatch"); //the user to be removed
		
        UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
        SecureKoboldClient.getInstance().removeUser(ctx, getUser(userName));
		
		KoboldProject kp = KoboldPLAMPlugin.getCurrentKoboldProject();
		kp.updateUserPool();
	}
	
	public void changePassword(String oldPassword, String newPassword){
		    UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());
		    SecureKoboldClient.getInstance().updateUserPassword(ctx, getUser(ctx.getUserName()), oldPassword, newPassword);
		    //changing the password at the client
		    KoboldProject tmpProj = KoboldPLAMPlugin.getCurrentKoboldProject();
		    tmpProj.setPassword(newPassword);
	}
	
	public void updateFullName(String userName, String newName, String password){
		
	    UserContext ctx = ServerHelper.getUserContext(KoboldPLAMPlugin.getCurrentKoboldProject());        
        User user = getUser(userName);
        
        user.setFullname(newName);
        
        SecureKoboldClient.getInstance().updateUserFullName(ctx, user , password);

        KoboldProject kp = KoboldPLAMPlugin.getCurrentKoboldProject();
		kp.updateUserPool();
		
	}
	

	
	
	/*
	 * returns one User
	 */
	private User getUser(String userName){
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


