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

/**
 * This Inquisitor class is used to manage the set server SAT command.
 * 
 * @see kobold.client.serveradmin.tools.BasicInquisitor
 * 
 * @author contan
 */
public class SetServerInquisitor extends BasicInquisitor {
    private static final String SERVER_IP = "server ip\t";
    private static final String SERVER_PORT = "server port";
    private static final String PASSWORD = "password\t";
    
	public SetServerInquisitor() {
        containingAction = "set server";
        addQuestion("1", SERVER_IP, "localhost", false);
        addQuestion("2", SERVER_PORT, "23232", false);
        addQuestion("3", PASSWORD, "", true);
    }
    
    public String getIp() {
        return getAnswer(SERVER_IP);
    }
    
    public String getPort() {
        return getAnswer(SERVER_PORT);
    }
    
    public String getPassword() {
        return getAnswer(PASSWORD);
    }
    
    public String getUrl() {
        return "https://" + getIp() + ":" + getPort();
    }
}
