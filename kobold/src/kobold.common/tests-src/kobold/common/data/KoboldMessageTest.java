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
 * $Id: KoboldMessageTest.java,v 1.1 2004/05/14 13:04:31 vanto Exp $
 *
 */
package kobold.common.data;

import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

/**
 * @author Tammo
 *
 */
public class KoboldMessageTest extends TestCase {

	/**
	 * Constructor for KoboldMessageTest.
	 */
	public KoboldMessageTest() 
	{
		super("Kobold Message Test");
	}

	public void testSerialize() 
	{
		KoboldMessage msg = new KoboldMessage();
		msg.setDate(new Date());
		msg.setMessageText("Das ist ein Test");
		msg.setPriority("high");
		msg.setReceiver("vanto");
		msg.setSender("tammo");
		msg.setSubject("Test");
		System.out.println(msg);
		
		Element ser = msg.serialize();
		
		try {
			XMLWriter w = new XMLWriter(System.out);
			w.write(ser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		KoboldMessage msg2 = new KoboldMessage(ser);
		System.out.println(msg2);
		Assert.assertEquals(msg.getId(), msg2.getId());
		Assert.assertEquals(msg.getMessageText(), msg2.getMessageText());
		Assert.assertEquals(msg.getPriority(), msg2.getPriority());
		Assert.assertEquals(msg.getReceiver(), msg2.getReceiver());
		Assert.assertEquals(msg.getSender(), msg2.getSender());
		Assert.assertEquals(msg.getSubject(), msg2.getSubject());
		Assert.assertEquals(msg.getId(), msg2.getId());
	}

}
