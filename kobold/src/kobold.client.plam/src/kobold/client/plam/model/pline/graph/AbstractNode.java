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
 * $Id: AbstractNode.java,v 1.7 2004/04/28 16:23:56 vanto Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import java.net.URI;

import kobold.common.data.IdManager;
import kobold.common.io.ScriptDescriptor;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLLocator;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * AbstractNode
 *
 * Provides methods and fields to annotate Nodes with meta information.
 *
 * @author Tammo van Lessen
 */
public abstract class AbstractNode extends GXLNode
 {

	protected static final Log logger = LogFactory.getLog(AbstractNode.class);
	private Dimension dimension;
	private Point x, y;

	private GXLGraph graph = new GXLGraph(IdManager.getInstance().getModelId("container"));

	/**
	 * @param id
	 */
	public AbstractNode(String name, String type) 
	{
		super(IdManager.getInstance().getModelId(name));
		add(graph);
		try {
			setType(new URI(type));
		} catch (Exception e) {
			logger.info("Wrong node type uri specified", e);
		}
	}

	protected void add(AbstractNode node) 
	{
		graph.add(node);
	}
	
	protected void remove(AbstractNode node)
	{
		graph.remove(node);
	}
	
	/**
	*/
	public void setScriptDescriptor(ScriptDescriptor desc)
	 {
	 	setAttr("scriptdesc", new GXLLocator(desc.getId()));
	 }

	/**
	
	 */
	public String getDescription() 
	{
		return ((GXLString) getAttr("description").getValue()).getValue();
	}

	/**
	 * This method returns the dimension of the graphical object, or null if it is not set
	 * @return
	 */
	public Dimension getDimension() 
	{
		return dimension;
	}

	/**
	 */
	public String getName() 
	{
		return ((GXLString) getAttr("name").getValue()).getValue();
	}

	/**
	 * @return
	 */
	public String getOwner() 
	{
		return ((GXLString) getAttr("owner").getValue()).getValue();
	}

	/**
	 */
	public ScriptDescriptor getScriptDescriptor() 
	{
		return null;
	}

	/**
	 */
	public int getStatus() 
	{
		return ((GXLInt) getAttr("status").getValue()).getIntValue();
	}

	/**
	 * This method returns the x-Axis point of the graphical object, or null if it is not set
	 * @return
	 */
	public Point getX() 
	{
		return x;
	}

	/**
	 * This method returns the y-Axis point of the graphical object, or null if it is not set
	 * @return
	 */
	public Point getY() 
	{
		return y;
	}

	/**
	 */
	public void setDescription(String desc) 
	{
		setAttr("description", new GXLString(desc));
	}

	/**
	 * This method sets the dimension of the graphical object
	 * @param dimension
	 */
	public void setDimension(Dimension dimension) 
	{
		this.dimension = dimension;
	}

	/**
	 */
	public void setName(String name) 
	{
		setAttr("name", new GXLString(name));
	}

	/**
	 */
	public void setOwner(String owner) 
	{
		setAttr("owner", new GXLString(owner));
	}

	/**
	 */
	public void setStatus(int status) 
	{
		setAttr("status", new GXLInt(status));
	}

	/**
	 * This method sets the x-Axis point of the graphical object
	 * @param point
	 */
	public void setX(Point point) 
	{
		x = point;
	}

	/**
	 * This method sets the y-Axis point of the graphical object
	 * @param point
	 */
	public void setY(Point point) 
	{
		y = point;
	}
}
