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
 * 
 */
package kobold.client.serveradmin.tools;

import java.util.Random;

/*
 * This class extends the basic MaskingThread class to provide *really* secure 
 * password masking. The current line is not only overwritten with prompt and
 * blanks but also filled with a short string of random characters that are
 * randomly changing. Although mapping of pressed keys cannot be prevented, it
 * is absolutely impossible to tell which of the appearing characters is a
 * mapped one because mapping ocurs within the random character field.  
 */
public class RandomEchoMaskingThread extends MaskingThread {
    Random r; // pseudo random number generator
    String randomEchoString; // the current random character string
    
    public RandomEchoMaskingThread(String prompt) {
        super(prompt);
        
        // Initialize randomEchoString with 8 characters between 33 and 126 
        r = new Random();
        char[] res = new char [8];
        for (int n = 0; n < 8; n++) {
        	res[n] = (char) (((int)(r.nextDouble() * 93)) + 33);
        }
        randomEchoString = new String(res);
    }
    
    /*
     * @see class head comment
     */
    protected void mask() throws Exception{
    	sleep(10);
        
        // 1.) modify one of the characters of randomEchoString
        int modPosition = (int) (r.nextDouble() * 8);
        char[] res = new char [8];
        randomEchoString.getChars(0, 8, res, 0);
        res[modPosition] = (char) (((int)(r.nextDouble() * 93)) + 33);
        randomEchoString = new String (res);
        
        // 2.) mask the line 
        System.out.print("\r" + prompt + randomEchoString);
        System.out.flush();
        
        // 3.) set cursor to random position within the random echo section
        int curPosition = (int) (r.nextDouble() * 7);
        System.out.print("\r" + prompt);
        randomEchoString.getChars(0, 8, res, 0);
        for (int n = 0; n < curPosition; n++) {
            System.out.print(res[n]);
        }
        System.out.flush();
    }
}
