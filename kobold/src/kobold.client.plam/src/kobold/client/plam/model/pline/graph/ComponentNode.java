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
 * $Id: ComponentNode.java,v 1.4 2004/04/27 15:49:00 rendgeor Exp $
 *
 */
package kobold.client.plam.model.pline.graph;

import java.net.URI;
import java.util.*;

/**
 * ComponentNode
 * 
 * @author Tammo van Lessen
 */
public class ComponentNode extends AbstractNode 
{
//List of variant
private Vector variants;
private String name;
//person who's responsible
private String responsibleEmployee;
private String description;
//List of scripts
private Vector scripts;
int status;
	/**
	 */
	public ComponentNode(String id) 
	{
		super(id);
		URI type = null;
				try
				{
					type = new URI("ComponentNode");
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
		
				setType(type);		
		// TODO Auto-generated constructor stub
	}


/**
 * @return
 */
public String getDescription() {
	return description;
}

/**
 * @return
 */
public String getName() {
	return name;
}

/**
 * @return
 */
public String getResponsibleEmployee() {
	return responsibleEmployee;
}

/**
 * @return
 */
public Vector getScripts() {
	return scripts;
}

/**
 * @param
 */
public void addScript() {
}

/**
 * @param
 */
public void removeScript() {
}

/**
 * @return
 */
public int getStatus() {
	return status;
}

/**
 * @return
 */
public Vector getVariants() {
	return variants;
}

/**
 * @param
 */
public void addVariant() {
}

/**
 * @param
 */
public void removeVariant() {
}

/**
 * @param string
 */
public void setDescription(String string) {
	description = string;
}

/**
 * @param string
 */
public void setName(String string) {
	name = string;
}

/**
 * @param string
 */
public void setResponsibleEmployee(String string) {
	responsibleEmployee = string;
}

/**
 * @param i
 */
public void setStatus(int i) {
	status = i;
}

}
