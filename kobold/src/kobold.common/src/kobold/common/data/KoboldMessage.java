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
 * $Id: KoboldMessage.java,v 1.9 2004/05/16 02:28:17 vanto Exp $
 *
 */
package kobold.common.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @author garbeam
 */
public class KoboldMessage {

	private static final Log logger = LogFactory.getLog(KoboldMessage.class);
	private DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSZ");
	
	/** this field must be changed if you subclass this class */
	public static final String TYPE = "kobold";
	public static final String STATE_UN_FETCHED = "UN_FETCHED"; 
	public static final String STATE_FETCHED = "FETCHED";
	public static final String STATE_INVALID = "INVALID";
	
	public static final String PRIORITY_HIGH = "high";
	public static final String PRIORITY_NORMAL = "normal";
	public static final String PRIORITY_LOW = "low";
	
	private String sender;
	private String receiver;
	private String messageText;
	private Date date = new Date();
	private String priority;
	private String subject;
	private String id;
	private String state = STATE_UN_FETCHED;

	/**
	 * Creates a new Kobold Message with a new unique id. (type = kmesg).
	 */
	public KoboldMessage()
	{
		this("kmesg");
	}
	
	/**
	 * Unmarshals a Kobold Message.
	 * @param data
	 */
	public KoboldMessage(Element data)
	{
		deserialize(data);
	}
	
	/**
	 * Creates a new Kobold Message.
	 * Use this constructor to use a seperate id pool for the given type.
	 * @param idtype
	 */
	protected KoboldMessage(String idtype)
	{
		id = IdManager.getInstance().getMessageId(idtype);
	}

	/**
	 * Returns the state.
	 * @return state.
	 */
	public String getState() {
		return state;	
	}
	
	/**
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @return
	 */
	public String getId() {
		return id;
	}

	public String getType() 
	{
		return KoboldMessage.TYPE;
	}
	
	/**
	 * @return
	 */
	public String getMessageText() {
		return messageText;
	}

	/**
	 * @return
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @return
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * @return
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param string
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @param i
	 */
	protected void setId(String id) {
		this.id = id;
	}

	/**
	 * @param string
	 */
	public void setMessageText(String string) {
		messageText = string;
	}

	/**
	 * @param string
	 */
	public void setPriority(String string) {
		priority = string;
	}

	/**
	 * @param string
	 */
	public void setReceiver(String string) {
		receiver = string;
	}

	/**
	 * @param string
	 */
	public void setSender(String string) {
		sender = string;
	}

	/**
	 * @param string
	 */
	public void setSubject(String string) {
		subject = string;
	}

	/**
	 * Serializes this object to an xml element and adds it to the given root.
	 * @param root
	 */
	public Element serialize() {
		Element xmsg = DocumentHelper.createElement("message");
		xmsg.addAttribute("type", getType());
		xmsg.addAttribute("id", id);
		xmsg.addAttribute("priority", priority);
		xmsg.addAttribute("state", state);
		
		xmsg.addElement("sender").setText(sender);
		xmsg.addElement("receiver").setText(receiver);

		xmsg.addElement("date").setText(dateFormat.format(date));

		xmsg.addElement("subject").setText(subject);
		xmsg.addCDATA(messageText);
		
		return xmsg;
	}

	/**
	 * Deserializes message 
	 */
	protected void deserialize(Element data) {
		id = data.attributeValue("id");
		priority = data.attributeValue("priority");
		state = data.attributeValue("state");
				
		sender = data.elementTextTrim("sender");
		receiver = data.elementTextTrim("receiver");
		
		// fall back on parse error
		date = new Date();
		try {
			date = dateFormat.parse(data.elementTextTrim("date"));
		} catch (ParseException e) {
			logger.debug("Could not parse date.", e);
		}

		subject = data.elementTextTrim("subject");

		messageText = data.getTextTrim();
	}


	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) 
	{
		if (!(obj instanceof KoboldMessage)) 
			return false;
		return ((KoboldMessage)obj).getId().equals(getId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return getId().hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append("\n\t[id:       " + getId() + "]\n");
		sb.append("\t[state:   "  + getState() + "]\n");
		sb.append("\t[sender:   " + getSender() + "]\n");
		sb.append("\t[receiver: " + getReceiver() + "]\n");
		sb.append("\t[subject:  " + getSubject() + "]\n");
		return sb.toString();
	}

	/**
	 * Factorymethod to create a Kobold-/Workflow-instance from an dom4j element.
	 * Checks the type attribute to select the right class, returns null if type
	 * attribute is not set or has wrong data.
	 * 
	 * @param el
	 * @return
	 */
	public static KoboldMessage createMessage(Element el)
	{
		String type = el.attributeValue("type");
		if (type == null)
			return null;
		
		if (type.equals(KoboldMessage.TYPE)) {
			return new KoboldMessage(el);
		}
		else if (type.equals(WorkflowMessage.TYPE)) {
			return new WorkflowMessage(el);
		}
		else return null;	
	}

	/**
	 * Sets the state.
	 * @param state the state.
	 */
	public void setState(String state) {
		this.state = state;
	}
}
