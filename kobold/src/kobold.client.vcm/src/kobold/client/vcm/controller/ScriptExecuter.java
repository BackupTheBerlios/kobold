/*
 * Created on 31.08.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package kobold.client.vcm.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.internal.ccvs.core.util.Util;
import org.eclipse.team.internal.core.streams.PollingInputStream;
import org.eclipse.team.internal.core.streams.TimeoutInputStream;

/**
 * @author Tammo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ScriptExecuter
{
	private String[] command;

	// incoming from remote host
	InputStream inputStream;

	// outgoing to remote host
	OutputStream outputStream;
	
	// Process spawn to run the command
	Process process;

	protected ScriptExecuter(String[] command) 
	{
	    this.command = command;
	}
	
	/**
	 * Closes the connection.
	 */
	public void close() throws IOException {
		try {
			if (inputStream != null) inputStream.close();
		} finally {
			inputStream = null;
			try {
				if (outputStream != null) outputStream.close();
			} finally {
				outputStream = null;
				if (process != null) process.destroy();
			}
		}
	}
	
	/**
	 * Returns the <code>InputStream</code> used to read data from the
	 * server.
	 */
	public InputStream getInputStream() {
		return inputStream;
	}
	
	/**
	 * Returns the <code>OutputStream</code> used to send data to the
	 * server.
	 */
	public OutputStream getOutputStream() {
		return outputStream;
	}
	
	/**
	 * Opens the connection and invokes cvs in server mode.
	 *
	 * @see Connection.open()
	 */
	public String open(IProgressMonitor monitor) throws IOException {
		boolean connected = false;
		try {
			process = Util.createProcess(command, monitor);

			inputStream = new PollingInputStream(new TimeoutInputStream(process.getInputStream(),
					8192 /*bufferSize*/, 1000 /*readTimeout*/, -1 /*closeTimeout*/), 60, monitor);

			// XXX need to do something more useful with stderr
			// discard the input to prevent the process from hanging due to a full pipe
			Thread othread = new StringInputThread(inputStream);
			Thread ethread = new DiscardInputThread(process.getErrorStream());
			connected = true;
			othread.start();
			ethread.start();
			process.waitFor();
			return ((StringInputThread)othread).getResult();
		} catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
			if (! connected) {
				try {
					close();
				} finally {
					// Ignore any exceptions during close
				}
			}
		}
	}

	private static class DiscardInputThread extends Thread {
		private InputStream in;
		public DiscardInputThread(InputStream in) {
			this.in = in;
		}
		public void run() {
			try {
				try {
					while (in.read() != -1);
				} finally {
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}

	private static class StringInputThread extends Thread {
		private InputStream in;
		private StringBuffer result = new StringBuffer();
		public StringInputThread(InputStream in) {
			this.in = in;
		}
		public void run() {
		    try {
		        try {
		            int r;
		            while ((r = in.read()) != -1) {
		                result.append((char)r);
		            }
		        } finally {
		            in.close();
		        }
		    } catch (IOException e) {
		    }
		}
		
		public String getResult()
		{
		    return result.toString();
		}
	}

}
