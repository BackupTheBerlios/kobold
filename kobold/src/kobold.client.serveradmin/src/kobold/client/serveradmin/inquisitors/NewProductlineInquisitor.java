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
	public NewProductlineInquisitor() {
        containingAction = "new productline";
        addQuestion("1", "productline name\t\t", "unknown", false);
        addQuestion("2", "productline resource\t", "unknown", false);
        addQuestion("3", "repository type\t\t", "cvs", false);
        addQuestion("4", "repository protocol\t", "pserver", false);
        addQuestion("5", "repository host\t\t", "cvs.werkbold.org", false);
        addQuestion("6", "repository root\t\t", "/cvsroot/unknown/", false);
        addQuestion("7", "repository module path\t", "unknown", false);
    }
    
    public RepositoryDescriptor getRepositoryDescriptor() {
        String type = getAnswer("repository type\t\t");;
        String protocol = getAnswer("repository protocol\t");
        String host = getAnswer("repository host\t\t");
        String root = getAnswer("repository root\t\t");
        String path = getAnswer("repository module path\t");
        return new RepositoryDescriptor(type, protocol, host, root, path);                               
    }
    
    public String getName() {
        return getAnswer("productline name\t\t");
    }
    
    public String getResource() {
        return getAnswer("productline resource\t");
    }
}
