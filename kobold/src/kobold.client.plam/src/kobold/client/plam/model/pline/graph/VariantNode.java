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
 * $Id: VariantNode.java,v 1.9 2004/04/28 16:23:56 vanto Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * VariantNode
 * 
 * @author Tammo van Lessen
 */
public class VariantNode extends AbstractNode 
{
	public static final String TYPE = "http://kobold.berlios.de/types#variant";

	private List versions = new ArrayList();
	private List components = new ArrayList();

	/**
	 */
	public VariantNode(String name) 
	{
		super(name, TYPE);
	}

	/**
	 */
	public ComponentNode[] getComponents() 
	{
		return (ComponentNode[]) components.toArray(new ComponentNode[0]);
	}

	/**
	 */
	public void addComponent(ComponentNode component) 
	{
		components.add(component);
		add(component);
	}

	/**
	 */
	public void removeComponent(ComponentNode component) 
	{
		components.remove(component);
		remove(component);
	}

	/**
	 */
	public VersionNode[] getVersions() 
	{
		return (VersionNode[]) versions.toArray(new VersionNode[0]);
	}

	/**
	 */
	public void addVersion(VersionNode version) 
	{
		versions.add(version);
		add(version);
	}

	/**
	 */
	public void removeVersion(VersionNode version) 
	{
		version.remove(version);
		remove(version);
	}

}
