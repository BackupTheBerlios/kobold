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
 * $Id: WorkflowMessage.java,v 1.14 2004/05/15 02:11:01 vanto Exp $
 *
 */
package kobold.common.data;
import java.util.*;

import org.dom4j.Element;

/**
 * @author garbeam
 * @author vanto
 */
public class WorkflowMessage extends KoboldMessage {
	
	protected static final String TYPE = "workflow";
	private String workflowId;
	private String comment = "";
	private Set parents = new HashSet();
	private List controlItems = new LinkedList();
	private HashMap answer; 


	public WorkflowMessage()
	{
		super("wmesg");
	}
	
	public WorkflowMessage(Element data)
	{
		super(data);
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
	public String getWorkflowId() {
		return workflowId;
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
	public void setWorkflowId(String id) {
		workflowId = id;
	}

	/**
	 * @return
	 */
	public HashMap getAnswer() {
		return answer;
	}

	/**
	 * @param map
	 */
	public void setAnswer(HashMap map) {
		answer = map;
	}

	/**
	 * @see kobold.common.data.KoboldMessage#deserialize(org.dom4j.Element)
	 */
	protected void deserialize(Element data) 
	{
		super.deserialize(data);
	}

	protected String getType() 
	{
		return WorkflowMessage.TYPE;
	}

	/**
	 * @see kobold.common.data.KoboldMessage#serialize()
	 */
	public Element serialize() 
	{
		Element el = super.serialize();
		el.addElement("workflow-id").setText(workflowId);
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

}   
   

