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

package kobold.client.serveradmin.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

/**
 * This is the base class of all inquisitor classes that are used to perfrom
 * user interaction for most of SAT's functions. BasicInquisition objects store
 * several questions along with their answers and provide the possibiloty to  
 * perform automated user interrogation.
 * 
 * @see kobold.client.serveradmin.inquisitors.* 
 * 
 * @author contan
 */
public class BasicInquisitor {
    /*
     * Stores all the questions for this Inquisitor.
     * @see kobold.client.serveradmin.tools.InquisitorQuestion
     */
    private HashMap questions = new HashMap();
    private Vector qv = new Vector();
    
    /*
     * @see printOptions()
     */
    protected String containingAction;
    
    public BasicInquisitor() {
        containingAction = "";
    }

    protected void addQuestion(String shortcut, 
                               String prompt, 
                               String answer, 
                               boolean maskAnswer) {
        questions.put(shortcut, new InquisitorQuestion(shortcut, prompt, answer, 
                maskAnswer));
        qv.add(questions.get(shortcut));
    }
    
    /**
     * This function prints a menu of available values (questions) that can be
     * filled (answered) and their current contents (answer), if it is not 
     * masked.
     */
    private void printOptions() {
        // 1.) print listing head
        String listingHead = "Available Options for \"" + containingAction + "\":";
        System.out.print(listingHead + "\n");
        for (int n = 0; n < listingHead.length(); n++) {
            System.out.print('-');
        }
        System.out.print("\n\n");
        
        // 2.) print options
        for (Iterator it = qv.iterator(); it.hasNext();) {
            InquisitorQuestion q = (InquisitorQuestion) it.next();
            System.out.print("\t(" + q.shortcut + ") - change " + q.prompt);
            if (!q.maskAnswer) {
                System.out.print(" [" + q.answer + "]");
            }
            else {
                System.out.print(" [");
                for (int n = 0; n < q.answer.length(); n++) {
                    System.out.print("*");
                }
                System.out.print("]");
            }
            System.out.print("\n");
        }
        
        // 3.) print standard options
        System.out.print("\n\t(p) - perform action");
        System.out.print("\n\t(c) - cancel action\n\n");
    }
    
    /*
     * Performs the interrogation. A menu is presented (@see printOptions())
     * showing the questions that can be answered. User input is collected and
     * the according action is performed (posal of question or return).
     * 
     * @return false, if the user wants to quit the current action, false 
     * otherwise 
     */
    public boolean performInquisition() {
        printOptions();
    	while(true) {
            // 1.) get user's option choice
            System.out.print("Your choice: ");
            String input = "";
            try {
            	input += (char) System.in.read();
            }
            catch(Exception e) {
                System.out.print("\n");
                return false;
            }

            // 2.) option choice collected -> empty input stream
            try {
            	for (char c = 0; c != '\n'; c = (char)System.in.read());
            }
            catch(Exception e) {
                System.out.print("\n");
                return false;
            }

            // 3.) check if the user has finished
            if (input.equals("c")) {
                System.out.print("\n");
                return false;
            }
            else if (input.equals("p")) {
                System.out.print("\n");
                return true;
            }
            
            // 4.) check if user's input is valid and perform action
            InquisitorQuestion q = (InquisitorQuestion) questions.get(input);
            
            if (q == null) {                
                // invalid input, ask again
                continue;
            }
            else {
                // input is valid -> pose question
                System.out.print("\n");
                q.poseQuestion();
                
                // prepare next iteration
                System.out.print("\n");
                printOptions();
            }
        }//while(true)
    }//performInquisition()
    
    /**
     * Returns the stored answer for the question with the passed prompt 
     * @param prompt prompt of the question whose answer should be returned
     * @return null if no question with the passed prompt exists, the answer
     *         otherwise
     */
    protected String getAnswer(String prompt) {
        for (Iterator it = qv.iterator(); it.hasNext();) {
            InquisitorQuestion q = (InquisitorQuestion) it.next();
            if (q.prompt.equals(prompt)) {
                return q.answer;
            }
        }
        return null;
    }
}
