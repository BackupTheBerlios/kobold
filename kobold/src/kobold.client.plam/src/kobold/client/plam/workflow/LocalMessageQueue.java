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
 * $Id: LocalMessageQueue.java,v 1.3 2004/05/15 21:56:04 vanto Exp $
 *
 */
package kobold.client.plam.workflow;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kobold.common.data.KoboldMessage;
import kobold.common.data.WorkflowItem;
import kobold.common.data.WorkflowMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Stores/Manages all local kobold messages
 * 
 * @author Tammo
 */
public class LocalMessageQueue  {

	private static final Log logger = LogFactory.getLog(LocalMessageQueue.class);
	private static final String MARKER_ID = "kobold.client.plam.messagemarker";
	
	private IFile queueFile;
	
	//TODO: Sorted!
	private Map messages = new HashMap();//TreeMap(new DateComparator());
	private Map markerIdByMessage = new HashMap();
	
	
	public LocalMessageQueue(IFile queueFile)
	{
		this.queueFile = queueFile;
		update();
		
		/*//TODO: Remove test stuff
		WorkflowMessage msg = new WorkflowMessage();
		msg.setWorkflowId("testwf");
		msg.setSender("vanto");
		msg.setReceiver("vanto");
		msg.setSubject("Test workflow");
		msg.setMessageText("Das ist die Erkl�rung");
		msg.addWorkflowControl(new WorkflowItem("false", "Kobold ist doof", WorkflowItem.CHECK));
		msg.addWorkflowControl(new WorkflowItem("true", "Kobold ist toll", WorkflowItem.CHECK));
		addMarker(msg);
		messages.put("test", msg);*/
	}
	
	public synchronized void addMessage(KoboldMessage msg)
	{
		messages.put(msg.getId(), msg);
		store();
		addMarker(msg);
	}

	public synchronized void removeMessage(KoboldMessage msg)
	{
		messages.remove(msg.getId());
		store();
		removeMarker(msg);
	}
	
	public KoboldMessage[] getMessages()
	{
		return (KoboldMessage[])messages.values().toArray(new KoboldMessage[0]);
	}
	
	public KoboldMessage getMessageById(String id)
	{
		KoboldMessage msg = (KoboldMessage)messages.get(id); 
		return msg;
	}
	
	public void update()
	{
		if (!queueFile.exists()) {
			logger.debug("update: file doesn't exist");
			return;
		}
			
		try {
			InputStream in = queueFile.getContents();
			SAXReader reader = new SAXReader();
			Document document = reader.read(in);
		    
			if (document.getRootElement() != null 
				&& document.getRootElement().getName() != "kobold-messages") {
				logger.debug("update: wrong file structure");
				return; 
			}
			
			messages.clear();
			clearMarkers();
			
			Iterator it = document.getRootElement().elementIterator("message");
			while (it.hasNext()) {
				Element msgEl = (Element)it.next();
				KoboldMessage message = KoboldMessage.createMessage(msgEl);
				messages.put(message.getId(), message);
				logger.info("add msg: "+message);
				addMarker(message);
			}

			in.close();
		} catch (CoreException e) {
			logger.debug("Error while reading PLAM config.", e);
		} catch (DocumentException e) {
			logger.debug("Error while parsing PLAM config.", e);
		} catch (IOException e) {
		}
	}
	
	public void store()
	{
		try {
			Document document = DocumentHelper.createDocument();
			Element root = document.addElement("kobold-messages");
			
			Iterator it = messages.values().iterator();
			
			while (it.hasNext()) {
				KoboldMessage msg = (KoboldMessage)it.next();
				root.add(msg.serialize()); 
			}
			
			
			XMLWriter writer = new XMLWriter(new FileWriter(queueFile.getLocation().toOSString()),
						OutputFormat.createPrettyPrint());
			writer.write(document);
			writer.close();
			// TODO notify eclipse that file has been changed.
			
		} catch (IOException e) {
			logger.debug("Error while writing PLAM config.", e);
		}
	}

	private void addMarker(KoboldMessage msg)
	{
		logger.info("add marker: " + msg);
		IMarker marker;
		try {
			marker = queueFile.createMarker(MARKER_ID);
			marker.setAttribute("msgid", msg.getId());
			marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_NORMAL);
			marker.setAttribute(IMarker.MESSAGE, msg.getSubject());
			marker.setAttribute(IMarker.USER_EDITABLE, false);
			markerIdByMessage.put(msg, new Long(marker.getId()));

		} catch (CoreException e) {
			logger.warn("Error during marker operation", e);
		}
		

	}

	private void removeMarker(KoboldMessage msg)
	{
		logger.info("remove marker: " + msg);
		Long id = (Long)messages.get(msg);
		if (id != null) {
			try {
				IMarker marker = queueFile.findMarker(id.longValue());
				marker.delete();
			} catch (CoreException e) {
				logger.warn("Error during marker operation", e);
			}	
		}
	}

	private void clearMarkers()
	{
		try {
			IMarker[] markers = queueFile.findMarkers(MARKER_ID, true, IResource.DEPTH_ZERO);
			for (int i = 0; i < markers.length; i++) {
				markers[i].delete();
			}
			markerIdByMessage.clear();
		} catch (CoreException e) {
			logger.warn("Error during marker operation", e);
		}	
	}
	
	/**
	 * Sorts Kobold Messages by Date
	 */
	public class DateComparator implements Comparator{

		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(Object o1, Object o2) {
			if (o1 instanceof KoboldMessage && o2 instanceof KoboldMessage) {
				return ((KoboldMessage)o1).getDate().compareTo(((KoboldMessage)o2).getDate());
			} else return 1;
		}

	}

	/**
	 */
	public void activate() {
		update();
	}

	/**
	 */
	public void deactivate() {
		clearMarkers();		
	}
}
