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
 */
package kobold.client.serveradmin.inquisitors;

import kobold.client.serveradmin.tools.BasicInquisitor;
import kobold.common.io.RepositoryDescriptor;

/**
 * This Inquisitor class is used to manage the new productline SAT command.
 * 
 * @see kobold.client.serveradmin.tools.BasicInquisitor
 * 
 * @author contan
 */
public class NewProductlineInquisitor extends BasicInquisitor {
    private static final String PL_NAME = "productline name\t\t";
    private static final String PL_RES = "productline resource\t";
    private static final String REP_TYPE = "repository type\t\t";
    private static final String REP_PROTOCOL = "repository protocol\t";
    private static final String REP_HOST = "repository host\t\t";
    private static final String REP_ROOT = "repository root\t\t";
    private static final String REP_MPATH = "repository module path\t";
    
	public NewProductlineInquisitor() {
        containingAction = "new productline";
        addQuestion("1", PL_NAME, "unknown", false);
        addQuestion("2", PL_RES, "unknown", false);
        addQuestion("3", REP_TYPE, "cvs", false);
        addQuestion("4", REP_PROTOCOL, "pserver", false);
        addQuestion("5", REP_HOST, "cvs.werkbold.org", false);
        addQuestion("6", REP_ROOT, "/cvsroot/unknown/", false);
        addQuestion("7", REP_MPATH, "unknown", false);
    }
    
    public RepositoryDescriptor getRepositoryDescriptor() {
        String type = getAnswer(REP_TYPE);
        String protocol = getAnswer(REP_PROTOCOL);
        String host = getAnswer(REP_HOST);
        String root = getAnswer(REP_ROOT);
        String path = getAnswer(REP_MPATH);
        return new RepositoryDescriptor(type, protocol, host, root, path);                               
    }
    
    public String getName() {
        return getAnswer(PL_NAME);
    }
    
    public String getResource() {
        return getAnswer(PL_RES);
    }
}
