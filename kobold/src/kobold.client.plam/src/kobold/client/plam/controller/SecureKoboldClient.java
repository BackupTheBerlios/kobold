/*
 * Created on 21.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.client.plam.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.xmlrpc.secure.SecureXmlRpcClient;

/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SecureKoboldClient {

	// the xml-rpc client
	  SecureXmlRpcClient client;


	  /**
	   * main method
	   */
	  public static void main (String args[]) throws Exception {
	  if (args.length < 1) {
		  System.err.println ("Usage: java Client URL");
	  } else {
		  SecureKoboldClient client = new SecureKoboldClient(args[0]);
		  client.run ();
	  }
	  }

	  /**
	   *  Constructor
	   */
	  public SecureKoboldClient (String url) throws Exception {
	  client = new SecureXmlRpcClient (url);
	  }

	  /**
	   * Read from standard input and make an asynchronous XML-RPC call.
	   */
	  public void run () throws IOException {
	  String token = null;
	  BufferedReader d = new BufferedReader(
		  new InputStreamReader(System.in));
	  System.err.println ("Enter some text and hit <return>");
		  System.err.println ("Input will be sent to "+client.getURL ());
	  while ((token = d.readLine()) != null) {
			  System.err.println ("sending: "+token);
		  Vector v = new Vector ();
		  v.add (token);
		  try {
				  Object result = client.execute ("echo", v);
			  System.err.println ("received: "+result);
		  } catch (Exception exception) {
			  System.err.println ("Error: "+exception);
		  }
	  }
	  }
	
}
