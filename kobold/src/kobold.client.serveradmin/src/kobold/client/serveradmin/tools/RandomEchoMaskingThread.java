/*
 * Created on 17.09.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.serveradmin.tools;

import java.util.Random;

/**
 * @author Administrator
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RandomEchoMaskingThread extends MaskingThread {
    Random r;
    String randomEchoString;
    
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
