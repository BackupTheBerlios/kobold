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
 * $Id: ModelFactory.java,v 1.1 2004/05/06 16:58:21 vanto Exp $
 *
 */
package kobold.client.plam.model;

import kobold.client.plam.model.pline.graph.ComponentNode;
import kobold.client.plam.model.pline.graph.VariantNode;
import kobold.client.plam.model.pline.graph.VersionNode;
import kobold.common.io.ScriptDescriptor;

/**
 * ModelFactory
 * 
 * @author Tammo van Lessen
 */
public class ModelFactory {

	public static Model createProductLineModel(ProductLine line)
	{
		return null;
	}
	
	public static Model createProductModel(Product product)
	{
		return null;
	}
	
	public static Model createTestModel()
	{
		Model model = new Model(new ProductLine());
		ComponentNode componentNode = new ComponentNode("Compiler");
		ComponentNode c2 = new ComponentNode("Compiler");
		c2.setDescription("Testdescription");
		c2.setOwner("Tammo");
		
		VariantNode var1 = new VariantNode("V1");
		VariantNode var2 = new VariantNode("V2");
		
		ComponentNode c2c = new ComponentNode("Something");
		VariantNode var3 = new VariantNode("V1");
		c2.addVariant(var1);
		c2.addVariant(var2);
		var2.addComponent(c2c);
		c2c.addVariant(var3);
	
		VersionNode version = new VersionNode("1.0");
		var2.addVersion(version);
		version.setScriptDescriptor(new ScriptDescriptor("MyScript"));
		
		model.addComponent(componentNode);
		model.addComponent(c2);
		
		return model;
	}
}
