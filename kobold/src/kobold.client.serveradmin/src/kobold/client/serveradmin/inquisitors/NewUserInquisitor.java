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
 * This Inquisitor class is used to manage the new user SAT command.
 * 
 * @see kobold.client.serveradmin.tools.BasicInquisitor
 * 
 * @author contan
 */
public class NewUserInquisitor extends BasicInquisitor {
    private static final String USERNAME = "username";
    private static final String FULLNAME = "fullname";
    private static final String PASSWORD = "password";
    
    private void setHelpMessage() {
        helpMessage = "This action is used to create a new user on the " +
        "currently associated Kobold\nserver. A Kobold user \"consists\" of " +
        "her/his username (or loginname), her/his\nfull (or real) name and " +
        "the initial password (which should be changed when\nthe new user " +
        "logs in for the first time).";
    }
    
    public NewUserInquisitor() {
        containingAction = "new user";
        addQuestion("1", USERNAME, "unknown", false);
        addQuestion("2", FULLNAME, "nobody", false);
        addQuestion("3", PASSWORD, "", true);
        
        setHelpMessage();
    }
    
    public String getUsername(){
        return getAnswer(USERNAME);
    }
    public String getFullName() {
        return getAnswer(FULLNAME);
    }
    public String getPassword() {
        return getAnswer(PASSWORD);
    }
}
