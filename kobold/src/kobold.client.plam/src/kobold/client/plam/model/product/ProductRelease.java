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
 * $Id: ProductRelease.java,v 1.4 2004/10/21 21:32:40 martinplies Exp $
 *
 */

package kobold.client.plam.model.product;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import kobold.client.plam.model.AbstractAsset;
import kobold.client.plam.model.Release;

import org.dom4j.Element;

/**
 * This class represents a product release. 
 */
public class ProductRelease extends AbstractAsset
{

	private DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSZ");
	private static final String GXL_TYPE = "http://kobold.berlios.de/types#ProductReleases";
	// container
	private List releases;
	private Date creationDate;
	
	/**
	 * Basic constructor.
	 * @param creationDate the origin date when this release
	 * 		  has been created.	
	 */
	public ProductRelease (Date creationDate) {
	    super();
		this.creationDate = creationDate;
	}
	
	/**
	 * DOM constructor.
	 * @param parent
	 * @param element
	 */
	public ProductRelease (AbstractAsset parent, Element element) {
	    super();
	    setParent(parent);
		deserialize(element);
	}
	
	/**
	 * Serializes the product.
	 * @see kobold.client.plam.model.product.Product#serialize()
	 */
	public Element serialize() {
		Element productRelease = super.serialize();
		if (creationDate != null) {
			productRelease.addAttribute("created", dateFormat.format(creationDate));
		}
		
		Element releasesElement = productRelease.addElement("releases");
		if (releases != null)
		for (Iterator it = releases.iterator(); it.hasNext(); ) {
			Release release = (Release) it.next();
			releasesElement.add(release.serialize());
		}
		
		return productRelease;
	}

	/**
	 * Deserializes this product.
	 * @param element
	 */
	public void deserialize(Element element) {
		super.deserialize(element);
		if (element.attributeValue("created") != null) {
			try {
				creationDate = dateFormat.parse(element.attributeValue("created"));
			} catch (ParseException e) {}
		}
		// FIXME: Releases deserialisieren: �ber Id die Referenz auf das Release aus 
		// einem Instancepool holen!
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public List getReleases() {
		return releases;
	}

	/**
	 * @see kobold.client.plam.model.AbstractAsset#getType()
	 */
	public String getType() {
		return AbstractAsset.PRODUCT_RELEASE;
	}

    /**
     * @see kobold.client.plam.model.AbstractAsset#getGXLChildren()
     */
    public List getGXLChildren() {
        return this.releases;
    }

    /**
     * @see kobold.client.plam.model.AbstractAsset#getGXLType()
     */
    public String getGXLType() {
        return GXL_TYPE;
    }

}
