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
import org.eclipse.team.internal.ccvs.core.IServerConnection;
import org.eclipse.team.internal.ccvs.core.connection.CVSAuthenticationException;
import org.eclipse.team.internal.ccvs.core.util.Util;
import org.eclipse.team.internal.core.streams.PollingInputStream;
import org.eclipse.team.internal.core.streams.PollingOutputStream;
import org.eclipse.team.internal.core.streams.TimeoutInputStream;
import org.eclipse.team.internal.core.streams.TimeoutOutputStream;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

/**
 * @author schneipk
 *
 * TODO comment
 */
public class CVSSererConnection implements IServerConnection
{
	// The default port for rsh
	private static final int DEFAULT_PORT = 9999;
	
	// The variable for verifiying the connection is establisched
	private static boolean connected = false;

	// cvs format for the repository without connection detail information(e.g. host:/home/cvs/repo)
	private String repositoryLocation = "";
	private String localPath = "";
	// The user name for the VCM
	private String user;
	private String password;

	// The buffer for reading the InputStreams
	private byte[] readLineBuffer = new byte[512];
	
	
	// incoming from remote host
	private InputStream inputStream;

	// incoming errorstream from process
	private InputStream errStream;
	
	// outgoing to remote host
	private OutputStream outputStream;
	
	// Process spawn to run the command
	private Process process;
	 
	// The absolute path and name of the skript to be excecuted
	private String skriptName = null;
	
	
	
	
	/** 
	 * 
	 * @author schneipk
	 *
	 * The protected default constructor for this class
	 * @param location the location of the cvs repository @see ICVSRepositoryLocation
	 * @param password the password of the current user
	 */
	public CVSSererConnection(String location, String user, String password) {
		this.repositoryLocation = location;
		this.password = password;
		this.user = user;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#open(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void open(IProgressMonitor monitor) throws IOException,
			CVSAuthenticationException
	{

		connected = false;
		try {
			String[] command = new String[1];
			if (skriptName != null) 
			{
				command[0] = skriptName;
			}
			process = Util.createProcess(command, monitor);

			inputStream = new PollingInputStream(new TimeoutInputStream(process.getInputStream(),
				8192 /*bufferSize*/, 2900 /*readTimeout*/, -1 /*closeTimeout*/), 60, monitor);
			outputStream = new PollingOutputStream(new TimeoutOutputStream(process.getOutputStream(),
				8192 /*buffersize*/, 1000 /*writeTimeout*/, 1000 /*closeTimeout*/), 60, monitor);

			// XXX need to do something more useful with stderr
			// discard the input to prevent the process from hanging due to a full pipe
			errStream = (process.getErrorStream());
			connected = true;
			ConsolePlugin.getDefault();
			MessageConsoleStream stream = null;
			MessageConsole console= new MessageConsole("Kobold VCM Console",null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] {console});
			stream = console.newMessageStream();
			Thread inputThread = new InputThreadToConsole(process.getInputStream(), stream);
			Thread errorThread = new InputThreadToConsole(process.getErrorStream(), stream);
//			readInpuStreamsToConsole();
			inputThread.run();
			errorThread.run();
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
	/**
	 * This method allows the caller to run a given command as a
	 * runtime excecution call
	 * @param command
	 * @throws IOException
	 * @throws CVSAuthenticationException
	 */
	public void open(String[] command) throws IOException,
	CVSAuthenticationException
{

		connected = false;
		try {
			if (skriptName != null) {
				command[0] = skriptName;
			}
			process = Util.createProcess(command,null);

			inputStream = new PollingInputStream(new TimeoutInputStream(process
					.getInputStream(), 8192 /* bufferSize */,
					2900 /* readTimeout */, -1 /* closeTimeout */), 60, null);
			outputStream = new PollingOutputStream(new TimeoutOutputStream(
					process.getOutputStream(), 8192 /* buffersize */,
					1000 /* writeTimeout */, 1000 /* closeTimeout */), 60,
					null);

			// XXX need to do something more useful with stderr
			// discard the input to prevent the process from hanging due to a
			// full pipe
			errStream = (process.getErrorStream());
			connected = true;

		} finally {
			if (!connected) {
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
	public void open(IProgressMonitor monitor,String[] command) throws IOException,
			CVSAuthenticationException
	{
//		String[] command = ((CVSRepositoryLocation)location).getExtCommand(password);
//		String[] command = {"C:\\Temp\\test","marvin"};
		try {
			process = Util.createProcess(command, monitor);

			inputStream = new PollingInputStream(new TimeoutInputStream(process.getInputStream(),
				8192 /*bufferSize*/, 1000 /*readTimeout*/, -1 /*closeTimeout*/), 60, monitor);
			outputStream = new PollingOutputStream(new TimeoutOutputStream(process.getOutputStream(),
				8192 /*buffersize*/, 1000 /*writeTimeout*/, 1000 /*closeTimeout*/), 60, monitor);
			// XXX need to do something more useful with stderr
			// discard the input to prevent the process from hanging due to a full pipe
	
			MessageConsoleStream stream = null;
			MessageConsole console= new MessageConsole("Kobold VCM Console",null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] {console});
			stream = console.newMessageStream();
			connected = true;
			Thread inputThread = new InputThreadToConsole(process.getInputStream(), stream);
			Thread errorThread = new InputThreadToConsole(process.getErrorStream(), stream);
//			readInpuStreamsToConsole();
			inputThread.run();
			errorThread.run();
//			readInpuStreamsToConsole();
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
		} 
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#getInputStream()
	 */
	public InputStream getInputStream()
	{
		return inputStream;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#getOutputStream()
	 */
	public OutputStream getOutputStream()
	{
		return outputStream;
	}
	public void readInpuStreamsToConsole()
	{
		if (this.connected) 
		{
			ConsolePlugin.getDefault();
			MessageConsole console= new MessageConsole("Kobold VCM Console",null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] {console});
			MessageConsoleStream stream = console.newMessageStream();
			InputStream err = this.errStream;// (InputStream)process.getErrorStream();
			InputStream pis = this.inputStream; //(InputStream)process.getInputStream();
//			OutputStream os1 = this.outputStream;//process.getOutputStream();
//			stream.print("TESCHD");
			int index = 0;
			int r = 0;
			try	{				
				if (err != null) {
					while ((err.available() != 0) && (r = err.read()) != -1 ) {
						readLineBuffer = append(readLineBuffer, index++, (byte) r);
					}
				}
				if(pis != null)
				{
					while ((pis.available() != 0) && (r = pis.read()) != -1 ) {
						readLineBuffer = append(readLineBuffer, index++, (byte) r);
					}
				}

				
				stream.print(new String(readLineBuffer, 0, index));
			}
			catch(Exception e)
			{
				// problem reading the inputStreams of the process
				e.printStackTrace();
			}
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
	
	// XXX need to do something more useful with stderr
	// discard the input to prevent the process from hanging due to a full pipe
	private static class InputThreadToConsole extends Thread {
		private InputStream in;
		private byte[] readLineBuffer = new byte[256];
		private MessageConsoleStream stream = null;
		public InputThreadToConsole(InputStream in,MessageConsoleStream stream ) {
			this.in = in;
			this.stream = stream;
		}
		public void run() {
			
			try {
				try {
					if (in != null) {
					int r,index = 0;
					System.out.println(in.toString()+" :"+in.available());
					while ((in.available() != 0) && (r = in.read()) != -1 ) {
						readLineBuffer = append(readLineBuffer, index++, (byte) r);
					}
				
					stream.print(new String(readLineBuffer, 0, index));
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
	/**
	 * @return Returns the errStream.
	 */
	public InputStream getErrStream() {
		return errStream;
	}
	/**
	 * @param skriptName The Skript Name to be executed.
	 */
	public void setSkriptName(String skriptName) {
		this.skriptName = skriptName;
	}
	/**
	 * @param localPath The localPath to set.
	 */
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}
}
