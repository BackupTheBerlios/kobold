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
 * Created on 17.05.2004
 * by schneipk CVSSererConnection.java
 * @author schneipk
 */
package kobold.client.vcm.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.team.internal.ccvs.core.ICVSRepositoryLocation;
import org.eclipse.team.internal.ccvs.core.IServerConnection;
import org.eclipse.team.internal.ccvs.core.connection.CVSAuthenticationException;
import org.eclipse.team.internal.ccvs.core.util.Util;
import org.eclipse.team.internal.core.streams.PollingInputStream;
import org.eclipse.team.internal.core.streams.PollingOutputStream;
import org.eclipse.team.internal.core.streams.TimeoutInputStream;
import org.eclipse.team.internal.core.streams.TimeoutOutputStream;

/**
 * @author schneipk
 *
 * TODO comment
 */
public class CVSSererConnection implements IServerConnection
{
	// The default port for rsh
	private static final int DEFAULT_PORT = 9999;

	// cvs format for the repository (e.g. :extssh:user@host:/home/cvs/repo)
	private String location;
	private String password;

	// incoming from remote host
	private InputStream inputStream;

	// outgoing to remote host
	private OutputStream outputStream;
	
	// Process spawn to run the command
	private Process process;
	
	
	/** 
	 * 
	 * @author schneipk
	 *
	 * The protected default constructor for this class
	 * @param location the location of the cvs repository @see ICVSRepositoryLocation
	 * @param password the password of the current user
	 */
	public CVSSererConnection(String location, String password) {
		this.location = location;
		this.password = password;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#open(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void open(IProgressMonitor monitor) throws IOException,
			CVSAuthenticationException
	{
//		String[] command = ((CVSRepositoryLocation)location).getExtCommand(password);
		String[] command = {"C:\\Temp\\putty.exe","marvin"};
		boolean connected = false;
		try {
			process = Util.createProcess(command, monitor);

			inputStream = new PollingInputStream(new TimeoutInputStream(process.getInputStream(),
				8192 /*bufferSize*/, 1000 /*readTimeout*/, -1 /*closeTimeout*/), 60, monitor);
			outputStream = new PollingOutputStream(new TimeoutOutputStream(process.getOutputStream(),
				8192 /*buffersize*/, 1000 /*writeTimeout*/, 1000 /*closeTimeout*/), 60, monitor);

			// XXX need to do something more useful with stderr
			// discard the input to prevent the process from hanging due to a full pipe
			Thread thread = new DiscardInputThread(process.getErrorStream());
			connected = true;
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
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#open(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void open(IProgressMonitor monitor,String[] command1) throws IOException,
			CVSAuthenticationException
	{
//		String[] command = ((CVSRepositoryLocation)location).getExtCommand(password);
		String[] command = {"C:\\Temp\\putty.exe","marvin"};
		boolean connected = false;
		try {
			process = Util.createProcess(command, monitor);

			inputStream = new PollingInputStream(new TimeoutInputStream(process.getInputStream(),
				8192 /*bufferSize*/, 1000 /*readTimeout*/, -1 /*closeTimeout*/), 60, monitor);
			outputStream = new PollingOutputStream(new TimeoutOutputStream(process.getOutputStream(),
				8192 /*buffersize*/, 1000 /*writeTimeout*/, 1000 /*closeTimeout*/), 60, monitor);

			// XXX need to do something more useful with stderr
			// discard the input to prevent the process from hanging due to a full pipe
			Thread thread = new DiscardInputThread(process.getErrorStream());
			connected = true;
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
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#close()
	 */
	public void close() throws IOException
	{
		try
		{
			if (inputStream != null) inputStream.close();
		}
		finally
		{
			inputStream = null;
			try
			{
				if (outputStream != null) outputStream.close();
			}
			finally
			{
				outputStream = null;
				if (process != null) process.destroy();
			}
		} // TODO 
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#getInputStream()
	 */
	public InputStream getInputStream()
	{
		// TODO Auto-generated method stub
		return null;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#getOutputStream()
	 */
	public OutputStream getOutputStream()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// XXX need to do something more useful with stderr
	// discard the input to prevent the process from hanging due to a full pipe
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
}
