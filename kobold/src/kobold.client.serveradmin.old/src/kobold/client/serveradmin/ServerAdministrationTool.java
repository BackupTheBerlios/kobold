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
package kobold.client.serveradmin;

import java.net.URL;

import kobold.common.controller.IKoboldServer;
import kobold.client.plam.controller.SecureKoboldClient;

/**
 * This class implements an independet Kobold Client. It is intended as a simple to use,
 * lightweight Kobold server administration tool (SAT). To use the functionality pass CT 
 * control to this class' main()-method.
 * 
 * NOTE: This class is currently under construction and not fully implemented. 
 * (5/8 SAT-UC's completely implemented)
 * 
 * TODO: export the command strings (they shouldn't be hardcoded!!)
 *  
 * @author contan
 */
public class ServerAdministrationTool {

	/**
	 * This member stores the password for the current Kobold server.
	 * It is set during the execution of main().
	 */
	private static java.lang.String currentPassword = "";
	
	/**
	 * This member stores the current Kobold server's URL.
	 * It is set during the execution of main().
	 */
	private static java.lang.String currentURL = "";
	
	/**
	 * this member implements the IKoboldServer-Interface and provides 
	 * easy to use accessibility for any Kobold server
	 */
	private static SecureKoboldClient client = null;
	
	/**
	 * This method acts as entry point for the Kobold Server Administartion Tool.
	 * The user is asked to provide the URL of the Kobold server to administrate
	 * and to provide a valid admin-password (see method startup()). The
	 * provided data is evaluated and -if this succeeds- the user can proceed 
	 * administarting the Kobold server.
	 * 
	 * @param args - all passed parameters are ignored
	 */
	public static void main(String[] args) {
		System.out.print("\t\t\t\t * Kobold Server Administration Tool - v1.0 *\n\n");
		
		/*
		 * 1.) Perform startup ops, as long as startup operations fail or the 
		 *      user decides to quit. If an exception is thrown the execution of 
		 *      this program is terminated.
		 */
		try{
			while (!startup()){
				System.out.print("Could not access the Kobold Server at \"" + currentURL + "\".\n");
				if (!readYesNo("Do you wish to reset the server's URL/your password? ")){
					System.out.print("\nTerminating SAT...");
					return;
				}
			}		
			System.out.print("\nKobold server " + currentURL + " is awaiting your commands.\n");
			System.out.print("Please type 'help' to show a summary of available commands.");
		}
		catch(Exception e){
			System.err.print("A fatal error occured while performing startup operations.\n");
			System.err.print("Exception thrown: ");
			System.err.print(e.toString());
			System.err.print("\nTerminating SAT...");
			return;
		} // end startup ops
		
		/*
		 * 2.) since the startup was successful, show the promt until the user quits
		 */
		try{
			while(provideSATPrompt());
		}
		catch(Exception e){
			System.err.print("A fatal error occured while executing the SAT-Prompt.\n");
			System.err.print("Exception thrown: ");
			System.err.print(e.toString());
			System.err.print("\nTerminating SAT...");
			return;
		}
		
		/*
		 * 3.) Terminate SAT
		 */
		System.out.print("\n\nTerminating SAT...");
		return;
	} // main()
	
	/**
	 * This helper function provides the user with a command prompt, if
	 * the startup ops of the main()-method could be executed
	 * successfully. The user's input is collected and the according
	 * methods are called.
	 * 
	 * @return true, if the user does not wish to exit the SAT, false otherwise
	 */
	private static boolean provideSATPrompt(){
		System.out.print("\nadmin@" + currentURL + ":");
		String input = readInput();
		
		/*
		 * Evaluate input
		 */
		if (input == ""){
			return true;
		}
		else if (input.startsWith("exit")){
			return false;
		}
		else if (input.startsWith("help")){
			showCommandSummary();
			return true;
		}
		else if (input.startsWith("createpl")){
			createProductline();
			return true;
		}
		else if (input.startsWith("removepl")){
			removeProductline();
			return true;
		}
		else if (input.startsWith("addple")){
			addPLE();
			return true;
		}
		else if (input.startsWith("removeple")){
			removePLE();
			return true;
		}
		else{
			System.out.print("Unknown command or wrong syntax.\n");
			return true;
		}
	}// end provideSATPrompt()
	
	/**
	 * this helper method is called by provideSATPrompt() when the user
	 * inputs 'addple'. The user is asked to input the names of the user and 
	 * the productline that shall be linked together by the new PLE-Role.
	 */
	private static void removePLE(){
		System.out.print("Please provide the name of the productline whose PLE you want to invalidate:");
		String plsname = readInput();
		
		try{
			if ((new SecureKoboldClient(new URL(currentURL))).satRemovePLE(currentPassword, plsname) !=
					IKoboldServer.NO_RESULT){
				System.out.print(plsname + " got rid of its PLE.\n");
			}
			else{
				System.out.print("Could not of " + plsname + "'s PLE. Maybe incorrect spelling of productline's name?\n");
			}
		}
		catch(Exception e){
			System.err.print("A fatal error occured while accessing a Kobold server.\n");
			System.err.print("Exception thrown: ");
			System.err.print(e.toString());
		}
	}
	/**
	 * this helper method is called by provideSATPrompt() when the user
	 * inputs 'addple'. The user is asked to input the names of the user and 
	 * the productline that shall be linked together by the new PLE-Role.
	 */
    private static void addPLE(){
    	System.out.print("Please provide the name of the productline you want to get a new PLE:");
    	String plsname = readInput();
    	System.out.print("Please provide the name of the user that shall become " + plsname + "'s new PLE:");
    	String username = readInput();
    	
    	try{
    		if ((new SecureKoboldClient(new URL(currentURL))).satAddPLE(currentPassword, username, plsname) !=
    		    IKoboldServer.NO_RESULT){
    			System.out.print(username + " is now " + plsname + " new PLE.\n");
    		}
    		else{
    			System.out.print("Could not make " + username + " become " + plsname + "'s new PLE.\n" +
    					                    "Maybe the productline's or user's name's incorrect?\n");
    		}
    	}
    	catch(Exception e){
    		System.err.print("A fatal error occured while accessing a Kobold server.\n");
    		System.err.print("Exception thrown: ");
    		System.err.print(e.toString());
    	}
    }
    /**
	 * this helper method is called by provideSATPrompt() when the user
	 * inputs 'removepl'. The user is asked to input a name for the new 
	 * productline. 
	 */
    private static void removeProductline(){
    	System.out.print("Please provide the name of the productline to be invalidated:");
    	
    	String plsname = readInput();
    	
    	try{
    		if ((new SecureKoboldClient(new URL(currentURL))).satRemoveProductline(currentPassword, plsname) !=
    		     IKoboldServer.NO_RESULT){
    			System.out.print("Productline \"" + plsname + "\" successfully removed.\n");
    		}
    		else{
    			System.out.print("Productline \"" + plsname + "\" could not be removed." +
    			                            "Maybe \"" + plsname + "\"has already been removed?\n");
    		}
    	}
    	catch(Exception e){
    		System.err.print("A fatal error occured while accessing a Kobold server.\n");
    		System.err.print("Exception thrown: ");
    		System.err.print(e.toString());
    		return;
    	}
    }
	
	/**
	 * this helper method is called by provideSATPrompt() when the user
	 * inputs 'createpl'. The user is asked to input a name for the new 
	 * productline. 
	 */
	private static void createProductline(){
		System.out.print("Please provide a name for the new productline: ");

		String plsname = readInput();
		
		if (plsname == ""){
			System.out.print("Sorry, the name of a productline must not be empty!");
			return;
		}
		
		try{
			if ((new SecureKoboldClient(new URL(currentURL))).satCreateNewProductline(currentPassword, plsname) !=
				IKoboldServer.NO_RESULT){
				System.out.print("New productline \"" + plsname + "\" successfully created.\n");
			}
			else{
				System.out.print("New productline \"" + plsname + "\" could not be created due" +
						                    " to an error on the Server (maybe there is already a productline" +
						                    " with name " + plsname + "?\n");
			}								 
		}
		catch(Exception e){
			System.err.print("A fatal error occured while accessing a Kobold server.\n");
			System.err.print("Exception thrown: ");
			System.err.print(e.toString());
			return;
		}
	}// end createPoductline()
		
	/**
	 * this helper method prints a summary of available commands
	 * for the SAT on the output stream. It is called by provideSATPrompt(),
	 * when the user inputs 'help'.
	 */
	private static void showCommandSummary(){
		System.out.print("Available commands:\n" +
									"\texit - terminates the SAT\n" +
									"\thelp - shows this help\n");
	}
	
	/**
	 * This helper function reads up to 500 bytes of input data and
	 * returns the read input as a String.
	 * 
	 * @return String containing the user's input - empty String if an error occured
	 */
	private static String readInput(){
		byte inp[] = new byte [500]; // buffer to store user's input
		
		/*
		 * read input into 'inp
		 */
		try
		{
			System.in.read(inp);
		}
		catch(Exception e)
		{
			System.err.print("An error occured while accessing the input stream.\n");
			System.err.print("Exception thrown: ");
			System.err.print(e.toString());
			
			return new String(""); // since op failed return empty string
		} // end read input 
		
		return new String (inp); 
	} // readInput()
	
	/**
	 * This helper method read's a single byte from the input stream as long as the
	 * user does not input y or n.
	 * @param question - String that will be printed on the output stream, followed by "(y/n)"
	 * @return true, if the posed question was answered with yes, false otherwise
	 */
	private static boolean readYesNo(String question) throws Exception{
		System.out.print(question);
	
		/*
		 * as long as the user does not input 'y', 'Y', 'n' or 'N' a byte
		 * is read form the input stream
		 */
		while(true){
			System.out.print("(y/n)");
			String inp = readInput();
			
			if (inp.charAt(0) == 'y' || inp.charAt(0) == 'Y'){
				return true;
			}
			else if (inp.charAt(0) == 'n' || inp.charAt(0) == 'N'){
				return false;
			}			
		} // end while
	} // readYesNo()
	
	/**
	 * This function performs the necessary startup operations to set the SAT
	 * into an operational status. This method is called by main() - please refer
	 * to main() for further information.
	 * 
	 * @return true, if the startup operations were performed successfully (that is URL and
	 * password are right and the Server is reachable), false otherwise
	 */
	private static boolean startup(){
		/*
		 * 1.) set the Kobold Server's URL
		 */
		System.out.print("Please provide the URL of the Kobold server you want to aministrate:\n");	
		currentURL = readInput();
		System.out.print("Kobold Server URL set to \"" + currentURL + "\".\n");
	
		/*
		 * 2.) set the admin's password
		 */
		System.out.print("Please provide the password of " + currentURL + ":\n");
		currentPassword = readInput();

		/*
		 * 3.) Test the server's accessibility
		 */
		return checkKoboldServer();
	} // startup()
	
	/**
	 * Tries to access the server specified by 'currentURL with 'currentPassword using an instance
	 * of "SecureKoboldClient".
	 * 
	 * @return true, if the Server referenced by the passed url is a reachable Kobold Server, false otherwise.
	 */
	private static boolean checkKoboldServer()	{
		try{
			URL url = new URL(currentURL);
			client = new SecureKoboldClient(url);		
			return (client.validateSATAccessibility(currentPassword) != IKoboldServer.NO_RESULT);
		}
		catch(Exception e){
			System.err.print("A fatal error occured while accessing a Kobold server.\n");
			System.err.print("Exception thrown: ");
			System.err.print(e.toString());
			return false;
		}
	} // checkKoboldServer 
}// class ServerAdministartionTool