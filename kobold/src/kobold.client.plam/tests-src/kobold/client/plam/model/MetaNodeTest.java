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
 *MiG20.09.2004
 */
package kobold.client.plam.model;

import org.apache.commons.id.uuid.state.InMemoryStateImpl;

import junit.framework.TestCase;

/**
 * @author MiG
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class MetaNodeTest extends TestCase {

	public MetaNodeTest(String arg0) {
		super(arg0);
        System.setProperty("org.apache.commons.id.uuid.state.State", InMemoryStateImpl.class.getName());

	}
	
	private MetaNode CreateNode(){
		MetaNode mn = new MetaNode(".AND");
		mn.setEdgeType("edgeType");
		mn.setDescription("Beschreibung");
		mn.setName("Name");
		mn.setResource("Resource");
		
		return mn;
	}
	
	public void SerializeTest(){
		MetaNode desNode = new MetaNode(".AND");	//Node for deserializing
		
		//Creating and filling a node
		MetaNode serNode = CreateNode();
		
		//serializing the testnode and deserializing the desNode
		desNode.deserialize(serNode.serialize());
		
		//testing the properties
		
		//description
		assertTrue(desNode.getDescription().equals(serNode.getDescription()));
		
		//name
		assertTrue(desNode.getName().equals(serNode.getName()));
		
		//Resource
		assertTrue(desNode.getResource().equals(serNode.getResource()));
		
	}
}


