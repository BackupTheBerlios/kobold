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

/**
 * This helper class is used by the BasicInquisitor class to store information
 * about questions and to read input for that question from the user.
 * 
 * @see kobold.client.serveradmin.tools.BasicInquisitor
 * 
 * @author contan
 */
public class InquisitorQuestion {
    public boolean maskAnswer;
    public String shortcut;
    public String prompt;
    public String answer;
    
    public InquisitorQuestion(String shortcut, String prompt, String answer, boolean maskAnswer) {
        this.shortcut = shortcut;
        this.prompt = prompt;
        this.answer = answer;
        this.maskAnswer = maskAnswer;
    }
    
    /*
     * Poses the represented question, reads the user's answer and stores it.
     * If maskAnswer is set to true the MaskingThread is used to mask the user's
     * input.
     * 
     * @see kobold.client.serveradmin.tools.MaskingThread
     */
    public void poseQuestion(){
        System.out.print("new " + prompt.replaceAll("\t", "") + ": ");
        
        if (maskAnswer) {
            RandomEchoMaskingThread maskingThread = new RandomEchoMaskingThread("new " + prompt + ": ");
            
            Thread t = new Thread(maskingThread);
            t.start();
            String oldanswer = answer;
            answer = "";
            try {
                char c = (char) System.in.read();
            
                while(c != '\n') {
                    if (c != '\r') {
                        answer += c;
                    }
                    c = (char) System.in.read();
                }
            }
            catch(Exception e) {
                answer = oldanswer;
            }
            t.stop();
        }
        else {
            String oldanswer = answer;
            answer = "";
            try {
                char c = (char) System.in.read();
                while(c != '\n') {
                    if (c != '\r') {
                    	answer += c;
                    }
                    c = (char) System.in.read();
                }
            }
            catch(Exception e) {
                answer = oldanswer;
            }
        }
    }//poseQuestion()

}
