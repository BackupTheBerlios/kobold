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
 * $Id: IdManager.java,v 1.1 2004/04/28 15:41:22 vanto Exp $
 *
 */
package kobold.common.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tammo van Lessen
 */
public class IdManager 
{

	private static final IdManager instance = new IdManager();
	private Map idByModelName = new HashMap();
	private Map idByScriptName = new HashMap();
	
	private IdManager() {}
	
	public static IdManager getInstance() 
	{
		return instance;
	}

	public String getModelId(String name) 
	{
		return createId(idByModelName, name);
	}

	public String getScriptId(String name) 
	{
		return createId(idByScriptName, name);
	}

	private String createId(Map idPool, String name)
	{
		Integer count = (Integer) idPool.get(name);
		if (count == null) {
			idPool.put(name, new Integer(1));
			return name;
		}
		String id = name + count.toString();
		idPool.put(name, new Integer(count.intValue() + 1));

		return id;
	}

}
