/*
 * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
 *                    Martin Plies, Michael Grosse, Necati Aydin,
 *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * $Id: WrapperProvider.java,v 1.5 2004/11/05 10:50:58 grosseml Exp $
 */

package kobold.client.vcm.controller;

import org.apache.log4j.Logger;

import kobold.client.vcm.KoboldVCMPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.team.core.RepositoryProvider;

/**
 * WrapperProvider.
 *
 * Base class of the Kobold  wrapper to provide an Eclipse Team
 * plugin for repository access.
 *
 * @author garbeam
 */
public class WrapperProvider extends RepositoryProvider {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(WrapperProvider.class);


    /**
     * Default constructor.
     */
    public WrapperProvider() {
        super();
    }

    /**
     * Configures the provider for the given project.
     */
    public void configureProject() {
    	
    }

    /**
     * Answer the id of this provider instance.
     */
    public String getID() {
		return KoboldVCMPlugin.PROVIDER_ID;

    }

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.IProjectNature#deconfigure()
	 */
	public void deconfigure() throws CoreException {
		// TODO Auto-generated method stub
		
	}
}
