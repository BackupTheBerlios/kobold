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

	private String name;
	
	/**
	 * Serializes the product.
	 * @see kobold.common.data.Product#serialize(org.dom4j.Element)
	 */
	public void serialize(Element productName) {
			Element product = productName.addElement("product").addText(this.name);
		}

	/**
	 * @param productName
	 */
	private void deserialize(Element productName) {

		this.name = productName.selectSingleNode("//product/productName").getText();
	}
	
	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param string
	 */
	public void setName(String name) {
		this.name = name;
	}

}
