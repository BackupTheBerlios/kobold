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
 * $Id: VariantNode.java,v 1.7 2004/04/28 13:06:20 vanto Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * VariantNode
 * 
 * @author Tammo van Lessen
 */
public class VariantNode extends AbstractNode 
{
	public static final String TYPE = "http://kobold.berlios.de/types#variant";
	
	private List components = new ArrayList();
	private String version;
	private String name;
	//List of scripts
	private Vector scripts;
	private int status;

	/**
	 */
	public VariantNode(String id) 
	{
		super(id, TYPE);
	}

	/**
	 * @return
	 */
	public ComponentNode[] getComponents() {
		return (ComponentNode[])components.toArray(new ComponentNode[0]);
	}

	/**
	 * @param
	 */
	public void addComponent() {
		
	}

	/**
	 * @param
	 */
	public void removeComponent() {
		
	}


	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public Vector getScripts() {
		return scripts;
	}

	/**
	 * @param
	 */
	public void addScript() {
		
	}

	/**
	 * @param
	 */
	public void removeScript() {
		
	}


	/**
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param i
	 */
	public void setStatus(int i) {
		status = i;
	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

}
