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
 * $Id: RelationNode.java,v 1.4 2004/04/21 15:57:09 memyselfandi Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import net.sourceforge.gxl.GXLNode;
import java.net.URI;

/**
 * RelationNode
 * 
 * Provides an abstract class to define more complex dependencies between
 * components.
 * 
 * @author Tammo van Lessen
 */
public abstract class RelationNode extends GXLNode 
{
	String startNode = null;
	String endNode = null;
	/**
	 */
	public RelationNode(String id) 
	{
		super(id);
		
		URI type = null;
		try
		{
			type = new URI("RelationNode");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		setType(type);
	}

}
