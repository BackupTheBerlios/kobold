package kobold.server.model;

import java.lang.String;

/**
 * this class stores a productline's data on the server
 */
public class Productline {

private Productlineinfo info;

/**
 * @return the productline's informations
 * @see kobold.util.data.ProductlineInfo
 */
public ProductlineInfo getInfo() {
  return null;
  }

/**
 * @return information of the given product
 * @see kobold.util.data.ProductInfo 
 */
public ProductInfo getProductInfo(String product) {
  return null;
  }

/**
 * adds a new (empty) product to the productline
 *
 * @param product the new product's name
 */
public void addProduct(String product) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.applyProductlineInfo(kobold.util.data.ProductlineInfo)
 */
public void applyProductlineInfo(Productlineinfo info) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.applyProductInfo(kobold.util.data.ProductInfo)
 */
public void applyProductInfo(ProductInfo info) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.addRole(kobold.util.data.Role)
 */
public void addRole(Role r) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.removeRole(kobold.util.data.Role)
 */
public void removeRole(Role r) {
  }

/**
 * @see kobold.server.model.ProductlineAdmin.removeProduct(java.lang.String)
 */
public void removeProduct(String product) {
  }
}
