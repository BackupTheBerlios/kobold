/*
 * Created on 16.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.common.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.CDATA;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class KoboldMessage {

	private static final Log logger = LogFactory.getLog(KoboldMessage.class);
	private DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSZ");

	private String sender;
	private String receiver;
	private String messageText;
	private Date date = new Date();
	private String priority;
	private String subject;
	private String id;

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
	 * @param type
	 */
	protected KoboldMessage(String type)
	{
		id = IdManager.getInstance().getMessageId(type);
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
		xmsg.addAttribute("id", id);
		xmsg.addAttribute("priority", priority);

		xmsg.addElement("sender").setText(sender);
		xmsg.addElement("receiver").setText(receiver);

		xmsg.addElement("date").setText(dateFormat.format(date));

		xmsg.addElement("subject").setText(subject);

		CDATA msg = DocumentHelper.createCDATA(messageText);
		xmsg.add(msg);
		
		return xmsg;
	}

	/**
	 * Deserializes message 
	 */
	private void deserialize(Element data) {
		id = data.attributeValue("id");
		priority = data.attributeValue("priority");
				
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
	public boolean equals(Object obj) {
		if (!(obj instanceof KoboldMessage)) 
			return false;
		return ((KoboldMessage)obj).getId().equals(getId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer(getClass().getName());
		sb.append("\t[id:       " + getId() + "]\n");
		sb.append("\t[sender:   " + getSender() + "]\n");
		sb.append("\t[receiver: " + getReceiver() + "]\n");
		sb.append("\t[subject:  " + getSubject() + "]\n");
		return sb.toString();
	}

}
