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
 * $Id: VersionNode.java,v 1.5 2004/04/28 15:17:08 vanto Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import net.sourceforge.gxl.GXLString;

/**
 * VersionNode
 * 
 * @author Tammo van Lessen
 */
public class VersionNode extends AbstractNode 
{
	public static final String TYPE = "http://kobold.berlios.de/types#version";

	/**
	 */
	public VersionNode(String name) 
	{
		super(name, TYPE);
	}

	/**
	 * Returns the version of this version
	 */
	public String getVersionId() 
	{
		return ((GXLString) getAttr("versionId").getValue()).getValue();
	}

	/**
	 * Sets the status to the new status
	 */
	public void setStatus(String status) 
	{
		setAttr("status", new GXLString(status));
	}

	/**
	 * Sets the versionNumber to the new version
	 */
	public void setVersionId(String version) 
	{
		setAttr("versionId", new GXLString(version));
	}

}
