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
 * $Id: ScriptDescriptor.java,v 1.11 2004/11/17 11:55:27 garbeam Exp $
 *
 */
package kobold.common.io;

import org.apache.log4j.Logger;

import java.net.URI;

import kobold.common.data.ISerializable;
import kobold.common.data.IdManager;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

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
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ScriptDescriptor.class);

	public static final String BASE_URI = "http://kobold.berlios.de/scripts#";
	
	public final static String VCM_UPDATE = "update";
	public final static String VCM_COMMIT = "commit";
	public final static String VCM_ADD = "add";
	public final static String VCM_REMOVE = "remove";
	
	private String id;
	// absolute path to the specific script
	private String path;
	private String name;
	private String vcmActionType;
    
	public ScriptDescriptor(Element sd) {
	    deserialize(sd);
	}
	
    public ScriptDescriptor(String name, String vcmActionType) 
    {
		id = IdManager.nextId(name);
		this.name = name;
		this.vcmActionType = vcmActionType;
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
        sd.addAttribute("vcmtype", vcmActionType);
        return sd;
    }

    /**
     * @see kobold.common.data.ISerializable#deserialize(org.dom4j.Element)
     */
    public void deserialize(Element element) {
        id = element.attributeValue("id");
        name = element.attributeValue("name");
        path = element.attributeValue("path");
        vcmActionType = element.attributeValue("vcmtype");
    }
    
    public String getVcmActionType() {
        return vcmActionType;
    }
    
    public void setVcmActionType(String vcmActionType) {
        this.vcmActionType = vcmActionType;
    }
}
