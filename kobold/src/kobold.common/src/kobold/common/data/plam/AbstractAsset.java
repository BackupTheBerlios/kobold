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
 * $Id: AbstractAsset.java,v 1.2 2004/06/09 14:45:16 rendgeor Exp $
 *
 */
package kobold.common.data.plam;

import kobold.common.data.IdManager;
/**
 * @author Tammo
 *
 */
public abstract class AbstractAsset implements ISerializable {

	public static final String PRODUCT_LINE = "productline";
	public static final String PRODUCT = "product";
	public static final String CORE_ASSET = "coreAsset";
	public static final String COMPONENT = "component";
	public static final String VARIANT = "variant";
	public static final String VERSION = "version";
	public static final String FILE_DESCRIPTOR = "fileDescriptor";
	
	private String name;
	private String id;

	private int parent;
	
	public AbstractAsset() {

	}

	public AbstractAsset(String name) {
		this.name = name;
		this.id = IdManager.getInstance().getModelId(getType());
		//sets the parent
		setParent ();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the type of this Asset.
	 * Possible values are AbstractAsset.PRODUCT and AbstractAsset.PRODUCT_LINE
	 * 
	 * @return the type of this asset
	 */
	public abstract String getType();

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return Returns the parent.
	 */
	public int getParent() {
		return parent;
	}
	/**
	 * @param parent The parent to set.
	 */
	public void setParent() {
		/*
		 * TODO: setze this pointer
		 this.parent = this;
		 */
	}
	
	public /*int*/void getRoot(){
		while (getParent() != 0)
		{
			//TODO: pointer in java??
			/*
			int root = root.getParent();
		}
		return root*/;
		}
	}
}