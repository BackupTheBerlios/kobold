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
 * $Id: ExclusionEdge.java,v 1.3 2004/04/21 15:05:08 rendgeor Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import net.sourceforge.gxl.GXLEdge;
import net.sourceforge.gxl.GXLGraphElement;
import java.net.URI;
/**
 * ExclusionEdge
 * 
 * @author Tammo van Lessen
 */
public class ExclusionEdge extends GXLEdge 
{
	/**
	 */
	public ExclusionEdge(GXLGraphElement arg0, GXLGraphElement arg1) 
	{
		super(arg0, arg1);
		URI type = null;
				try
				{
					type = new URI("ExclusionEdge");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		
				setType(type);

		// TODO Auto-generated constructor stub
	}

}
