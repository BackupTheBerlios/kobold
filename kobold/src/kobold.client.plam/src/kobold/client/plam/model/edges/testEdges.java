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
 * $Id: testEdges.java,v 1.5 2004/11/05 10:32:32 grosseml Exp $
 */

package kobold.client.plam.model.edges;

import org.apache.log4j.Logger;

import kobold.client.plam.model.productline.Component;

/**
 * @author pliesmn
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class testEdges {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(testEdges.class);

    public testEdges() {}
    public static void main(String[] args) {
        testEdges t = new testEdges();
        t.test1();
    }
    
    void test1() {}
    
    static void o(boolean b, String s){
      if (b) {	if (logger.isDebugEnabled()) {
		logger.debug("o(boolean, String) - OK Test: " + s);
	}}
      else {	if (logger.isDebugEnabled()) {
		logger.debug("o(boolean, String) - Failur Test: " + s);
	}};
    }
    
    
    
}
