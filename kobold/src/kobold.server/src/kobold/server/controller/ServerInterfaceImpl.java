/*
 * Created on 21.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.server.controller;

import java.util.List;

import kobold.common.data.KoboldMessage;
import kobold.common.data.PInfo;
import kobold.common.data.PLInfo;
import kobold.common.data.Role;
import kobold.common.data.UserContext;

/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ServerInterfaceImpl implements ServerInterface {

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#login(kobold.common.data.UserContext)
	 */
	public UserContext login(UserContext uc) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#logout(java.lang.String)
	 */
	public void logout(String sessionID) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addProductline(java.lang.String, java.lang.String)
	 */
	public void addProductline(String sessionID, String name) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addUser(java.lang.String, java.lang.String)
	 */
	public void addUser(String sessionID, String username) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getProductlineList(java.lang.String)
	 */
	public List getProductlineList(String sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getProductlineInfo(java.lang.String, java.lang.String)
	 */
	public PLInfo getProductlineInfo(String sessionID, String pl) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getProductInfo(java.lang.String, java.lang.String)
	 */
	public PInfo getProductInfo(String sessionID, String product) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addProduct(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void addProduct(String sessionID, String name, String pl) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#getUserContext(java.lang.String, java.lang.String)
	 */
	public UserContext getUserContext(String sessionID, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#addRole(java.lang.String, kobold.common.data.Role)
	 */
	public void addRole(String sessionID, Role r) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#applyPLInfo(java.lang.String, kobold.common.data.PLInfo)
	 */
	public void applyPLInfo(String sessionID, PLInfo pi) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#applyProductInfo(java.lang.String, kobold.common.data.PInfo)
	 */
	public void applyProductInfo(String sessionID, PInfo pi) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#applyUserInfo(java.lang.String, kobold.common.data.UserContext)
	 */
	public void applyUserInfo(String sessionID, UserContext uc) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeProductline(java.lang.String, java.lang.String)
	 */
	public void removeProductline(String sessionID, String pl) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeProduct(java.lang.String, java.lang.String)
	 */
	public void removeProduct(String sessionID, String product) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeRole(kobold.common.data.Role)
	 */
	public void removeRole(Role r) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#removeUser(java.lang.String, java.lang.String)
	 */
	public void removeUser(String sessionID, String username) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#commitMessage(kobold.common.data.UserContext, kobold.common.data.KoboldMessage)
	 */
	public void commitMessage(UserContext uc, KoboldMessage koboldMessage) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see kobold.server.controller.ServerInterface#fetchMessage(kobold.common.data.UserContext)
	 */
	public KoboldMessage fetchMessage(UserContext uc) {
		// TODO Auto-generated method stub
		return null;
	}

}
