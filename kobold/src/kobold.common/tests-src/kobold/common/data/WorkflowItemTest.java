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
 * $Id: WorkflowItemTest.java,v 1.1 2004/05/14 18:46:27 vanto Exp $
 *
 */
package kobold.common.data;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author Tammo
 */
public class WorkflowItemTest extends TestCase {

	/**
	 * Constructor for KoboldMessageTest.
	 */
	public WorkflowItemTest() 
	{
		super("Workflow Item Test");
	}
	
	public void testSerialize() 
	{
		WorkflowItem wi = new WorkflowItem("true", "desc", WorkflowItem.CHECK);
		Element ser = wi.serialize();
		
		try {
			XMLWriter w = new XMLWriter(System.out, OutputFormat.createPrettyPrint());
			w.write(ser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		WorkflowItem wi2 = new WorkflowItem(ser);
		
		Assert.assertEquals(wi.getDescription(), wi2.getDescription());
		Assert.assertEquals(wi.getType(), wi2.getType());
		Assert.assertEquals(wi.getValue(), wi2.getValue());
		Assert.assertEquals(wi.getChildren().length, wi2.getChildren().length);
		Assert.assertEquals(wi.getChildren().length, 0);
		
		WorkflowItem wiC = new WorkflowItem(null, "desc", WorkflowItem.CONTAINER);
		wiC.addChild(wi2);
		
		ser = wiC.serialize();

		try {
			XMLWriter w = new XMLWriter(System.out, OutputFormat.createPrettyPrint());
			w.write(ser);
		} catch (Exception e) {
			e.printStackTrace();
		}

		WorkflowItem wi3 = new WorkflowItem(ser);

		Assert.assertEquals(wiC.getDescription(), wi3.getDescription());
		Assert.assertEquals(wiC.getType(), wi3.getType());
		Assert.assertEquals(wiC.getValue(), wi3.getValue());
		Assert.assertEquals(wiC.getChildren().length, wi3.getChildren().length);
		Assert.assertEquals(wiC.getChildren().length, 1);

		WorkflowItem wi4 = wi3.getChildren()[0];
		Assert.assertEquals(wi.getDescription(), wi4.getDescription());
		Assert.assertEquals(wi.getType(), wi4.getType());
		Assert.assertEquals(wi.getValue(), wi4.getValue());
		Assert.assertEquals(wi.getChildren().length, wi4.getChildren().length);
		Assert.assertEquals(wi.getChildren().length, 0);


	}

}
