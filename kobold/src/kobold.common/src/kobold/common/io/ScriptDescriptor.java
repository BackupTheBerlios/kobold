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
 * $Id: ScriptDescriptor.java,v 1.6 2004/08/27 16:28:03 garbeam Exp $
 *
 */
package kobold.common.io;

import java.net.URI;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import kobold.common.data.ISerializable;
import kobold.common.data.IdManager;

/**
 * Base descriptor class for invocation of scripts before and after
 * VCM actions or whatever actions you may invent someday.
 * Provides information about location of script(s)
 * (executable command), order of execution, referenced files, etc.
 *
 * @author garbeam
 */
public class ScriptDescriptor  implements ISerializable
{
	public static final String BASE_URI = "http://kobold.berlios.de/scripts#";
	
	private String id;
	// absolute path to the specific script
	private String path;
	private String name;
    
	public ScriptDescriptor(Element sd) {
	    deserialize(sd);
	}
	
    public ScriptDescriptor(String name) 
    {
		id = IdManager.nextId(name);
		this.name = name;
    }
    
    private void setId(URI id)
    {
    	this.id = id.getFragment();
    }
    
    public URI getId()
    {
    	return URI.create(BASE_URI + id);
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see kobold.common.data.ISerializable#serialize()
     */
    public Element serialize() {
        Element sd = DocumentHelper.createElement("script-descriptor");
        sd.addAttribute("id", id);
        sd.addAttribute("name", name);
        sd.addAttribute("path", path);
        
        return sd;
    }

    /**
     * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
     */
    public void deserialize(Element element) {
        id = element.attributeValue("id");
        name = element.attributeValue("name");
        path = element.attributeValue("path");
    }
}
