package kobold.server.model;

import java.lang.String;

/**
 * this class admins the model of productlines and products on the server
 *
 * @author Armin Cont
 */
public class ProductlineAdmin {

/**
 * this method adds a new (empty) productline to the model
 *
 * @param productline the name of the new productline
 */
public void addProductline(String productline) {
  }

/**
 * this method adds a new (empty) product to the given productline
 *
 * @param product the new product's name
 * @param productline name of the productline to add the new product to
 */
public void addProduct(String product, String productline) {
  }

/**
 * adds (and applies) the passed Roleobject to the model
 * (e.g. if r is a PE-Role the method looks for the product
 * that is given by the Role object and changes its associated
 * PE-Role to the new Role)
 *
 * @param r the Role to add
 */
public void addRole(Role r) {
  }

/**
 * @return info to the passed productline
 */
public ProductlineInfo getProductlineInfo(String productline) {
  return null;
  }

/**
 * @return info to the passed product
 */
public ProductInfo getProductInfo(String product) {
  return null;
  }

/**
 * changes the info stored by the productline-object that is specified
 * by the passed ProductlineInfo according to its contents
 * 
 * @see ProductlineInfo
 */
public void applyProductlineInfo(Productlineinfo info) {
  }

/**
 * changes the info stored by the product-object that is specified
 * by the passed ProductInfo according to its contents
 * 
 * @see ProductInfo
 */
public void applyProductInfo(ProductInfo info) {
  }

/**
 * removes the passed productline
 */
public void removeProductline(String pl) {
  }

/**
 * removes the passed product
 */
public void removeProduct(String product) {
  }

/**
 * @return a list containing all stored productlines' names 
 */
public list getProductlineList() {
  return null;
  }

/**
 * removes a Role from the model (e.g. removal of a programmer 
 * form a product)
 */
public void removeRole(Role r) {
  }
}
