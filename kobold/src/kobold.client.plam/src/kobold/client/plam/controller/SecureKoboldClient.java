/*
 * Created on 21.04.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package kobold.client.plam.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.secure.SecureXmlRpcClient;

/**
 * @author garbeam
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SecureKoboldClient extends SecureXmlRpcClient {

	/**
	 * @param arg0
	 */
	public SecureKoboldClient(URL arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.xmlrpc.XmlRpcHandler#execute(java.lang.String, java.util.Vector)
	 */
	public Object execute(String arg0, Vector arg1)
		throws XmlRpcException, IOException {
		// TODO Auto-generated method stub
		return super.execute(arg0, arg1);
	}

}
