/*
 * Created on 21.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.server;

import org.apache.xmlrpc.secure.SecureWebServer;

/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SecureKoboldWebServer {
	// the xml-rpc webserver
	 static SecureWebServer server;

	 /**
	  * main method
	  */
	 public static void main (String args[]) throws Exception {
	 if (args.length < 1) {
		 System.err.println ("Usage: java Server port");
		 System.exit (1);
	 }
	 int port = 0;
	 try {
		 port = Integer.parseInt (args[0]);
	 } catch (NumberFormatException nonumber) {
		 System.err.println ("Invalid port number: "+args[0]);
		 System.exit (1);
	 }

	 server = new SecureWebServer (port);
	 // add an instance of this class as default handler
	 server.addHandler ("$default", new SecureKoboldWebServer());
	 server.start ();
	 System.err.println ("Listening on port "+port);
	 }


	 /**
	  *  This is method that is invoked via XML-RPC. The server looks
	  *  up the method via Java Reflection API. The alternative approach
	  *  would have been to implement the XmlRpcHandler interface and
	  *  define an execute() method to handle incoming calls.
	  */
	 public String echo (String input) {
	 return input;
	 }
	
}
