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
 * $Id: IReleaseContainer.java,v 1.2 2004/11/05 10:32:32 grosseml Exp $
 *
 */
package kobold.client.plam.model;

import org.apache.log4j.Logger;

import java.util.List;


/**
 * @author Tammo
 */
public interface IReleaseContainer
{
	/**
	 * Logger for this class
	 
	private static final Logger logger = Logger
			.getLogger(IReleaseContainer.class);
*/
	/**
	 * Adds a release and has to set its parent to this asset.
	 *
	 * @param release to add
	 */
    void addRelease(Release release);

	/**
	 * Adds a release at the given index and has to set its 
	 * parent to this asset.
	 *
	 * @param release to add
	 */
    void addRelease(Release release, int index);

    /**
	 * Removes a release and has to set its parent to null.
	 *
	 * @param release to remove
	 */
    void removeRelease(Release release);
    
    /**
     * Returns an unmodifiable list of releases.
     * 
     * @return list of releases
     */
    List getReleases();
}
