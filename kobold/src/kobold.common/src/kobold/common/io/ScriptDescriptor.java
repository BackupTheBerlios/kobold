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
 * $Id: ScriptDescriptor.java,v 1.3 2004/04/28 16:24:18 vanto Exp $
 *
 */
package kobold.common.io;

import java.net.URI;

import kobold.common.data.IdManager;

/**
 * Base descriptor class for invocation of scripts before and after
 * VCM actions or whatever actions you may invent someday.
 * Provides information about location of script(s)
 * (executable command), order of execution, referenced files, etc.
 *
 * @author garbeam
 */
public class ScriptDescriptor 
{
	public static final String BASE_URI = "http://kobold.berlios.de/scripts#";
	
	private String id;
    
    public ScriptDescriptor(String name) 
    {
		id = IdManager.getInstance().getScriptId(name);
    }
    
    public URI getId()
    {
    	return URI.create(BASE_URI + id);
    }
    	
}
