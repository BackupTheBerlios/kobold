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
 * $Id: AbstractAsset.java,v 1.1 2004/05/15 21:57:02 vanto Exp $
 *
 */
package kobold.common.data;

import java.io.Serializable;

import org.dom4j.Element;

/**
 * @author Tammo
 *
 */
public abstract class AbstractAsset implements Serializable {

	public static final String PRODUCT = "product";
	public static final String PRODUCT_LINE = "productline";
	
	protected String name;

	/**
	 * Basic constructor.
	 * @param productLineName
	 */
	public AbstractAsset(String assetName) {
		this.name = assetName;
	}

	/**
	 * DOM constructor.
	 * @param element
	 */
	public AbstractAsset(Element element) {
		deserialize(element);
	}

	public String getName() {
		return name;
	}

	/**
	 * Returns the type of this Asset.
	 * Possible values are AbstractAsset.PRODUCT and AbstractAsset.PRODUCT_LINE
	 * 
	 * @return the type of this asset
	 */
	public abstract String getType();

	public abstract void serialize(Element root);
	protected abstract void deserialize(Element element);
}
