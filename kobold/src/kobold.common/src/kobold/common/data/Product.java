/*
 * Created on 18.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
 

package kobold.common.data;

import org.dom4j.Element;
/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Product {

	private String productName;
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public void serialize(Element productName) {
			Element product = productName.addElement("product").addText(this.productName);
		}

	/**
	 * @param productName
	 */
	private void deserialize(Element productName) {

		this.productName = productName.selectSingleNode("//product/productName").getText();
	}
	
	/**
	 * @return
	 */
	public String getProductName() {
		return productName;
	}

	/**
	 * @param string
	 */
	public void setProductName(String string) {
		productName = string;
	}

}
