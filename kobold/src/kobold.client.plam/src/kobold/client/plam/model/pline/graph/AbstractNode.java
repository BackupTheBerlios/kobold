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
 * $Id: AbstractNode.java,v 1.9 2004/05/14 00:30:14 vanto Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;

import kobold.common.data.IdManager;
import kobold.common.io.ScriptDescriptor;
import net.sourceforge.gxl.GXLAttr;
import net.sourceforge.gxl.GXLGraph;
import net.sourceforge.gxl.GXLInt;
import net.sourceforge.gxl.GXLLocator;
import net.sourceforge.gxl.GXLNode;
import net.sourceforge.gxl.GXLString;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * AbstractNode
 *
 * Provides methods and fields to annotate Nodes with meta information.
 *
 * @author Tammo van Lessen
 */
public abstract class AbstractNode extends GXLNode //implements IPropertySource
 {
	protected static final Log logger = LogFactory.getLog(AbstractNode.class);
	private Dimension dimension = new Dimension(100,50);
	private Point location = new Point(5,5);

	protected transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	private GXLGraph graph = new GXLGraph(IdManager.getInstance().getModelId("container"));

	protected static IPropertyDescriptor[] descriptors = null;
	public static final String
		ID_CHILDREN = "children", 	//$NON-NLS-1$
		ID_INPUTS = "inputs",	//$NON-NLS-1$
		ID_OUTPUTS = "outputs",	//$NON-NLS-1$
		ID_SIZE = "size",         //$NON-NLS-1$
		ID_LOCATION = "location"; //$NON-NLS-1$

	static{
		descriptors = new IPropertyDescriptor[]{
			new PropertyDescriptor(ID_SIZE, "Size"),
			new PropertyDescriptor(ID_LOCATION, "Location")
		};
	}

	/**
	 * @param id
	 */
	public AbstractNode(String name, String type) 
	{
		super(IdManager.getInstance().getModelId(name));
		add(graph);
		setName(name);
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
	public Dimension getSize() 
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
		GXLAttr attr = getAttr("scriptdesc"); 
		if (attr == null)
			return null;
			
		URI id = ((GXLLocator)attr.getValue()).getURI();
		return ScriptDescriptor.getById(id);
	}

	/**
	 */
	public int getStatus() 
	{
		return ((GXLInt) getAttr("status").getValue()).getIntValue();
	}

	/**
	 * This method returns the location of the graphical object, or null if it is not set
	 * @return
	 */
	public Point getLocation() 
	{
		return location;
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
	public void setSize(Dimension dimension) 
	{
		if (this.dimension.equals(dimension)) return;
		this.dimension = dimension;
		firePropertyChange(ID_SIZE, null, dimension);  //$NON-NLS-1$
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
	public void setLocation(Point point) 
	{
		if (location.equals(point)) return;
		location = point;
		firePropertyChange(ID_LOCATION, null, point);  //$NON-NLS-1$
	}

	public void addPropertyChangeListener(PropertyChangeListener l)
	{
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l)
	{
		listeners.removePropertyChangeListener(l);
	}

	protected void firePropertyChange(String prop, Object old, Object newValue){
		listeners.firePropertyChange(prop, old, newValue);
	}

	protected void fireStructureChange(String prop, Object child){
		listeners.firePropertyChange(prop, null, child);
	}

	//TODO implement IPropertySource
}
