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
 * $Id: WorkflowMessage.java,v 1.21 2004/05/18 14:11:22 martinplies Exp $
 *
 */
package kobold.common.data;
import java.util.*;

import org.dom4j.Element;

/**
 * @author garbeam
 * @author vanto
 */
public class WorkflowMessage extends AbstractKoboldMessage {
	
	public static final String TYPE = "workflow";
	public static final String DATA_VALUE_TRUE = "TRUE";
	public static final String DATA_VALUE_FALSE= "FALSE";
	private String workflowType = new String();
	private String comment = "";
	private Set parents = new HashSet();
	private List controlItems = new LinkedList();
	private HashMap workflowData = new HashMap();
	private int step;


	/**
	 * Creates a Workflow Message in the 'wmesg' id namespace
	 */
	public WorkflowMessage()
	{
		super("wmesg");
	}
	
	/**
	 * Creates a Workflow Message and deserializes its data from xml
	 * 
	 * @param data a &lt;message&gt; element
	 */
	public WorkflowMessage(Element data)
	{
		this();
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
	
	/**
	 * @return
	 */
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

	/**
	 * @return
	 */
	public HashMap getWorkflowData() {
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
		
		// TODO store answers
		/*Element answers = el.addElement("results");
		it = answer.*/
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
   

