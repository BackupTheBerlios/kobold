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
 * $Id: Model.java,v 1.3 2004/04/28 16:23:56 vanto Exp $
 *
 */
package kobold.client.plam.model;

import java.io.IOException;

import kobold.client.plam.model.pline.graph.ComponentNode;
import kobold.client.plam.model.pline.graph.VariantNode;
import kobold.client.plam.model.pline.graph.VersionNode;
import kobold.common.io.ScriptDescriptor;
import net.sourceforge.gxl.GXLDocument;
import net.sourceforge.gxl.GXLGraph;

/**
 * @author schneipk
 *
 * This Class provides the functionality to  create the architectural Model for either a product,
 * a product line 
 * 
 */
public class Model {

	/**
	 * This Constructor creates a new Model
	 */
	public Model(AbstractAsset type) {
		
		if (type instanceof ProductLine)
		{
			buildProductLine((ProductLine)type);
		}
		if ( type instanceof Product)
		{
			buildProduct((Product)type);
		}
		if( type instanceof CoreAsset)
		{
			buildCoreAsset((CoreAsset)type );
		}
			

//		switch (type) {
//			case 0 :
//				buildProductLine(name);
//				break;
//				
//			case 1 : 
//				buildProduct(name);
//				break;
//				
//			case 2 :
//				buildComponent(name);
//				break;
//
//			default :
//			buildProductLine(name);
//			/* @TODO Errorhandling 
//			 * 
//			 */
//			}


	}

	private void buildProductLine(ProductLine pline)
	{
		
	}
		
	private void buildProduct(Product prod)
	{
		
	}
	
	private void buildCoreAsset(CoreAsset ca)
	{
		
	}
	
	// test stuff
	public static void main(String[] args) 
	{
		//Model model = new Model(new ProductLine());
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
		
		GXLGraph graph = new GXLGraph("koboldgraph");
		GXLDocument doc = new GXLDocument();
		graph.add(componentNode);
		graph.add(c2);
		
		doc.getDocumentElement().add(graph);
		
		try {
			doc.write(System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
