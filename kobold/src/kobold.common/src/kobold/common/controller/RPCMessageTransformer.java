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
 * $Id: RPCMessageTransformer.java,v 1.3 2004/08/03 14:49:17 vanto Exp $
 *
 */
package kobold.common.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Transforms kobold xml elements to base64 encoded strings and vice versa.
 * 
 * @author Tammo
 */
public class RPCMessageTransformer {

	protected static OutputFormat format = OutputFormat.createCompactFormat();
	private static final Log logger = LogFactory.getLog(RPCMessageTransformer.class);

	static {
		format.setEncoding("UTF-8");
	}

	/* not intended to instanciate */
	private RPCMessageTransformer() {}
	
	public static String encode(Element element)
	{
		return new BASE64Encoder().encode(element.asXML().getBytes());
	}
	
	public static Element decode(String data) throws RPCTransformerException
	{
		try {
			return DocumentHelper.parseText(new String(new BASE64Decoder().decodeBuffer(data))).getRootElement();
		} catch (Exception e) {
			throw new RPCMessageTransformer.RPCTransformerException("Could not decode rpc stream", e);
		}
	}
	
	public static class RPCTransformerException extends Exception
	{
		public RPCTransformerException(String msg, Throwable e)
		{
			super(msg, e);
		}
	}
}
