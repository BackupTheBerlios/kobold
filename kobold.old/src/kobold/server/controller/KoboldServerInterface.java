package kobold.server.controller;

import java.lang.String;

/**
 * this class acts as an interface for Kobold-clients
 *
 * @author Armin Cont
 */
public class KoboldServerInterface {

/**
 * a Kobold-Client has to call this method before making any
 * other requests 
 *
 * @see kobold.util.data.LoginInfo
 */
public LoginInfo login(LoginInfo li) {
  return null;
  }

/**
 * called by Kobold-Clients to logout (the passed session-ID
 * gets invalid)
 */
public void logout(String sessionID) {
  }

/**
 * Posts a KoboldMessage on the server. The interface delivers
 * the passed message to the proper Objects on the server.
 *
 * @see kobold.util.data.KoboldMessage
 */
public void sendMessage(String sessionID, KoboldMessage msg) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.addProductline(java.lang.String)
 */
public void addProductline(String sessionID, String name) {
  }

/**
 * @see kobold.server.user.UserAdmin.addUser(java.lang.String)
 */
public void addUser(String sessionID, String username) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.getProductlineList()
 */
public list getProductlineList(String sessionID) {
  return null;
  }

/**
 * @see kobold.server.model.ProductlineAdmin.getProductlineInfo(java.lang.String)
 */
public ProductlineInfo getProductlineInfo(String sessionID, String pl) {
  return null;
  }

/**
 * @see kobold.server.model.ProductlineAdmin.getProductInfo(java.lang.String)
 */
public ProductInfo getProductInfo(String sessionID, String product) {
  return null;
  }

/**
 * @see kobold.server.model.ProductlineAdmin.addProduct(java.lang.String, java.lang.String)
 */
public void addProduct(String sessionID, String name, String pl) {
  }

/**
 * @see kobold.server.user.UserAdmin.getUserInfo(java.lang.String)
 */
public UserInfo getUserInfo(String sessionID, String username) {
  return null;
  }

/**
 * @see kobold.server.model.ProductlineAdmin.addRole(kobold.util.data.Role)
 * @see kobold.server.user.UserAdmin.addRole(kobold.util.data.Role)
 */
public void addRole(String sessionID, Role r) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.applyProductlineInfo(kobold.util.data.ProductlineInfo)
 */
public void applyProductlineInfo(String sessionID, ProductlineInfo pi) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.applyProductInfo(kobold.util.data.ProductInfo)
 */
public void applyProductInfo(String sessionID, ProductInfo pi) {
  }

/**
 * @see kobold.server.user.UserAdmin.applyUserInfo(kobold.util.data.UserInfo)
 */
public void applyUserInfo(String sessionID, UserInfo ui) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.removeProductline(java.lang.String)
 */
public void removeProductline(String sessionID, String pl) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.removeProduct(java.lang.String)
 */
public void removeProduct(String sessionID, void arg1, String product) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.removeRole(kobold.util.data.Role)
 * @see kobold.server.user.UserAdmin.removeRole(kobold.util.data.Role)
 */
public void removeRole(Role r) {
  }

/**
 * @see kobold.server.user.UserAdmin.removeUser(java.lang.String)
 */
public void removeUser(String sessionID, String username) {
  }
}
