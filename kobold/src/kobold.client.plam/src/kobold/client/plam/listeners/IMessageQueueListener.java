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
 * $Id: IMessageQueueListener.java,v 1.3 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.listeners;

import org.apache.log4j.Logger;

import java.util.EventListener;

import kobold.common.data.AbstractKoboldMessage;

/**
 * An interface to be implemented by objects interested in add and remove events 
 * on the message queue.
 * 
 * @author Tammo
 */
public interface IMessageQueueListener extends EventListener {
	/**
	 * Logger for this class
	
	private static final Logger logger = Logger
			.getLogger(IMessageQueueListener.class);
 */
	void addMessage(AbstractKoboldMessage msg);
	void removeMessage(AbstractKoboldMessage msg);
	void rebuild();
}
