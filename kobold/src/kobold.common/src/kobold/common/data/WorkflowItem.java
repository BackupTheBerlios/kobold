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
 * $Id: WorkflowItem.java,v 1.9 2004/11/05 01:50:54 martinplies Exp $
 *
 */
package kobold.common.data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author pliesmn
 *
 * A WorkflowItem is a Checkbutton, a Radiobutton or a  single-Line-TextWindow
 * The data, to display a WorkFlowItem is stored here.  
 * 
 */
public class WorkflowItem {

	public final static String TEXT = "text"; 
	public final static String CHECK = "check";
	public final static String RADIO = "radio";
	public final static String CONTAINER = "container";
	
	private String description;
	private String value; // The Name of the Checkbutton or Textwindow or the value of the Radiobutton 
	private String type;
	private List children = new LinkedList();
	
	public WorkflowItem(Element data)
	{
		deserialize(data);
	}

	public WorkflowItem(String value, String description, String type) {
		this.value = value;
		this.description = description;
		this.type = type;
	}

	
	public void addChild(WorkflowItem control)
	{
		if (!type.equals(WorkflowItem.CONTAINER))
			throw new IllegalArgumentException("illegal add");
		children.add(control);	
	}
	
	
	public void addChildren(WorkflowItem[] control)
	{
		if (!type.equals(WorkflowItem.CONTAINER))
			throw new IllegalArgumentException("illegal add");
		for (int itemNr =0; itemNr <  control.length; itemNr++)
		  children.add(control[itemNr]);	
	}
	
	public WorkflowItem[] getChildren()
	{
		return (WorkflowItem[])children.toArray(new WorkflowItem[0]);	
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue(){
		return value;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public Element serialize()
	{
		Element element = DocumentHelper.createElement("control");
		element.addElement("type").setText(type);
		element.addElement("description").setText(description);
		element.addElement("value").setText(value);
		
		if (type.equals(WorkflowItem.CONTAINER)) {
			Element childrenEl = element.addElement("children");
			Iterator it = children.iterator();
			while (it.hasNext()) {
				WorkflowItem wfi = (WorkflowItem)it.next();
				childrenEl.add(wfi.serialize());
			}			
		}		
		return element;
	}
	
	protected void deserialize(Element data)
	{
		type = data.elementTextTrim("type");
		value = data.elementTextTrim("value");
		description = data.elementTextTrim("description");
		
		Element childrenEl = data.element("children");
		if (type.equals(WorkflowItem.CONTAINER) && childrenEl != null) {
			children.clear();
			Iterator it = childrenEl.elementIterator("control");
			while (it.hasNext()) {
				Element control = (Element)it.next();
				children.add(new WorkflowItem(control));	
			}
		}
	}

	public void setValue(String value) 
	{
		this.value = value;
	}
	
}
