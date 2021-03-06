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
 * $Id: WorkflowMessage.java,v 1.28 2004/11/05 10:50:56 grosseml Exp $
 *
 */
package kobold.common.data;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

/**
 
 * @author garbeam
 * @author vanto
 */
public class WorkflowMessage extends AbstractKoboldMessage {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(WorkflowMessage.class);
	
	public static final String TYPE = "workflow";
	public static final String DATA_VALUE_TRUE = "TRUE";
	public static final String DATA_VALUE_FALSE= "FALSE";
	private String workflowType = new String();
	private String comment = "";
	private Set parents = new HashSet();
	private List controlItems = new LinkedList(); // ArrayList performs much better
	private HashMap workflowData = new HashMap();
	private int step;


	/**
	 * Creates a Workflow Message in the 'workflow' id namespace
	 */
	public WorkflowMessage(String workflowType)
	{
		super(TYPE);
		this.workflowType = workflowType;
	}
	
	/**
	 * Creates a Workflow Message and deserializes its data from xml
	 * 
	 * @param data a &lt;message&gt; element
	 */
	public WorkflowMessage(Element data)
	{
		super(TYPE);
	    deserialize(data);
	}
	
	public void addParentId(String id)
	{
		parents.add(id);
	}
	
	public String[] getParentIds()
	{
		return (String[])parents.toArray(new String[0]);	
	}
	
	public void addWorkflowControl(WorkflowItem item)
	{
		controlItems.add(item);
	}
	
	public WorkflowItem[] getWorkflowControls()
	{
		return (WorkflowItem[])controlItems.toArray(new WorkflowItem[0]);
	}
	
	public String getComment() {
		return comment;
	}


	/**
	 * Sets the Workflow ID. 
	 * This ID describes, which Workflow should be applied by Drools

	 * @return workflow id
	 */
	public String getWorkflowType() {
		return workflowType;
	}

	/**
	 * @param string
	 */
	public void setComment(String string) {
		comment = string;
	}

	/**
	 * Sets the Workflow ID. 
	 * This ID describes, which Workflow should be applied by Drools
	 */
	public void setWorkflowType(String type) {
		workflowType = type;
	}
	
	public Map getWorkflowData() {
		return workflowData;
	}	
	
	public void putWorkflowData(String key, String value) {
		this.workflowData.put(key, value);
	}	
	
	public String getWorkflowDate(String key) {
		return (String) this.workflowData.get(key);
	}

	/**
	 * @see kobold.common.data.KoboldMessage#deserialize(org.dom4j.Element)
	 */
	public void deserialize(Element data) 
	{
		super.deserialize(data);
		// TODO check why those slutty variables dont get initialized.
		//parents = new HashSet();
		//controlItems = new LinkedList();
		
		workflowType = data.elementTextTrim("workflow-id");
		comment = data.elementTextTrim("comment");
		step = Integer.valueOf(data.elementTextTrim("step")).intValue();

		Element history = data.element("history");
		Iterator it = history.elementIterator("parent");
		
		while (it.hasNext()) {
			Element p = (Element)it.next();
			parents.add(p.getTextTrim());
		}
		
		Element controls = data.element("controls");
		it = controls.elementIterator("control");

		while (it.hasNext()) {
			Element c = (Element)it.next();
			controlItems.add(new WorkflowItem(c));
		}
		
		Element answers = data.element("results");
		it = answers.elementIterator();
		
		while (it.hasNext()) {
			Element a = (Element) it.next();
			this.putWorkflowData(a.getName(), a.getTextTrim());
		}

	}

	public String getType() 
	{
		return WorkflowMessage.TYPE;
	}

	/**
	 * @see kobold.common.data.KoboldMessage#serialize()
	 */
	public Element serialize() 
	{
		Element el = super.serialize();
		el.addElement("workflow-id").setText(workflowType);
		el.addElement("comment").addCDATA(comment);
		el.addElement("step").setText(String.valueOf(step));
		
		Element hist = el.addElement("history");
		Iterator it = parents.iterator();
		while (it.hasNext()) {
			String id = (String)it.next();
			hist.addElement("parent").setText(id);
		}
		
		Element controls = el.addElement("controls");
		it = controlItems.iterator();
		while (it.hasNext()) {
			WorkflowItem control = (WorkflowItem)it.next();
			controls.add(control.serialize());
		}
		
		// store answers
		Element answers = el.addElement("results");
		it = workflowData.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			answers.addElement(key).setText((String) workflowData.get(key));
		}
		return el;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\t[wf-id:    " + getWorkflowType() + "]\n");
		sb.append("\t[parents:  " + getParentIds().length + "]\n");
		sb.append("\t[controls: " + getWorkflowControls().length + "]\n");

		return super.toString() + sb.toString(); 
	}
	/**
	 * @return Returns the step.
	 */
	public int getStep() {
		return step;
	}
	/**
	 * @param step The step to set.
	 */
	public void setStep(int step) {
		this.step = step;
	}
}   
   

