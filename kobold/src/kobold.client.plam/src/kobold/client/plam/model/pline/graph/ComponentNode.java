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
 * $Id: ComponentNode.java,v 1.8 2004/05/06 16:58:21 vanto Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ComponentNode
 * 
 * @author Tammo van Lessen
 */
public class ComponentNode extends AbstractNode 
{

	public static final String TYPE = "http://kobold.berlios.de/types#component";

	private List variants = new ArrayList();

	/**
	 */
	public ComponentNode(String name) 
	{
		super(name, TYPE);
	}

	/**
	 */
	public List getVariants() 
	{
		return Collections.unmodifiableList(variants);
	}

	/**
	 */
	public void addVariant(VariantNode variant) 
	{
		variants.add(variant);
		add(variant);
		
	}

	/**
	 */
	public void removeVariant(VariantNode variant) 
	{
		variants.remove(variant);
		remove(variant);
	}

}
