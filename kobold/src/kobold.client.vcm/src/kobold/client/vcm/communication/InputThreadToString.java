/*
 * Created on 29.07.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
/*
package kobold.client.vcm.communication;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.ui.console.MessageConsoleStream;


// XXX need to do something more useful with stderr
// discard the input to prevent the process from hanging due to a full pipe
public class InputThreadToString extends Thread {
	private InputStream in;
	private byte[] readLineBuffer = new byte[256];

	public void setInputStream(InputStream in) {
		this.in = in;
	}
	public void run(String string) {
		
		try {
			try {
				if (in != null) {
				int r,index = 0;
//				System.out.println(in.toString()+" :"+in.available());
				while ((r = in.read()) != -1 ) {
					readLineBuffer = append(readLineBuffer, index++, (byte) r);
				}
			
				string =  (new String(readLineBuffer, 0, index));
			}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static byte[] append(byte[] buffer, int index, byte b) {
		if (index >= buffer.length) {
			byte[] newBuffer= new byte[index * 2];
			System.arraycopy(buffer, 0, newBuffer, 0, buffer.length);
			buffer= newBuffer;
		}
		buffer[index]= b;
		return buffer;
	}
}
*/
