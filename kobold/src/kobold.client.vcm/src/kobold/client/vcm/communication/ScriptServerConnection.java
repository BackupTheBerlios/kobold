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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import kobold.client.vcm.KoboldVCMPlugin;
import kobold.client.vcm.preferences.VCMPreferencePage;
import kobold.common.io.RepositoryDescriptor;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.internal.ccvs.core.IServerConnection;
import org.eclipse.team.internal.ccvs.core.connection.CVSAuthenticationException;
import org.eclipse.team.internal.ccvs.core.util.Util;
import org.eclipse.team.internal.core.streams.PollingInputStream;
import org.eclipse.team.internal.core.streams.PollingOutputStream;
import org.eclipse.team.internal.core.streams.TimeoutInputStream;
import org.eclipse.team.internal.core.streams.TimeoutOutputStream;
import org.eclipse.team.internal.ui.actions.ProgressDialogRunnableContext;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.Workbench;



/**
 * @author schneipk
 *
 * 
 */
public class ScriptServerConnection implements IServerConnection
{
	// The default port for rsh
	private static final int DEFAULT_PORT = 9999;
	
	// The variable for verifiying the connection is establisched
	private static boolean connected = false;

	// cvs format for the repository without connection detail information(e.g. host:/home/cvs/repo)
	private String repositoryPath = "";
	
	private String localPath = "";
	// The user name for the VCM
	private String user;
	//	 The password for the VCM
	private String password;
	//	 The server for the VCM
	private String vcmHostLocation;
	// The buffer for reading the InputStreams
	private byte[] readLineBuffer = new byte[256];
	// The Repository Descriptor used by this connection
	private RepositoryDescriptor repositoryDescriptor = null;
	
	
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
	
	// The two Threads created for error and input reading
	private Thread errorThread, inputThread;
	public static final char NEWLINE= 0xA;
	
	
	
	/** 
	 * 
	 * @author schneipk
	 *
	 * The protected default constructor for this class
	 * @param location the location of the cvs repository @see ICVSRepositoryLocation
	 */
	public ScriptServerConnection(String repositoryPath) {

		if (repositoryPath != "noUser")
		{
			this.repositoryPath = repositoryPath;
			this.user = getUserName();
			this.password = getUserPassword();
		}

	
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

			MessageConsoleStream stream1,stream2 = null;
			MessageConsole console= new MessageConsole("Kobold Script Console",null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] {console});
			stream2 = console.newMessageStream();
			connected = true;
			
			inputThread = new InputThreadToConsole(process/*.getInputStream()*/, stream2);
			try
            {
			    Workbench.getInstance().getProgressService().run(true,false,(InputThreadToConsole)inputThread) ;
            } catch (Exception e)
            {
                // TODO: handle exception
            }
			
		} finally {
			if (! connected) {
				try {
					close();
				} finally {
					inputThread = null;
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
	public String open(String[] command, String returnStr) throws IOException,
	CVSAuthenticationException
{
		IProgressMonitor progress = KoboldPolicy.monitorFor(null);
		connected = false;
		try {
			process = Util.createProcess(command,progress);

//			inputStream = new PollingInputStream(new TimeoutInputStream(process
//					.getInputStream(), 8192 /* bufferSize */,
//					2900 /* readTimeout */, -1 /* closeTimeout */), 60, null);
//			outputStream = new PollingOutputStream(new TimeoutOutputStream(
//					process.getOutputStream(), 8192 /* buffersize */,
//					1000 /* writeTimeout */, 1000 /* closeTimeout */), 60,
//					null);

			
			// discard the input to prevent the process from hanging due to a
			// full pipe
			errStream = (process.getErrorStream());
			connected = true;
//			errorThread = new InputThreadToConsole(errStream,null);
			inputThread = new InputThreadToConsole(process/*.getInputStream()*/, null);
			((InputThreadToConsole)inputThread).setReturnString(returnStr);

			try
            {
			    Workbench.getInstance().getProgressService().run(true,false,(InputThreadToConsole)inputThread) ;
            } catch (Exception e)
            {
                // TODO: handle exception
            }
//			errorThread.run();
			return ((InputThreadToConsole)inputThread).getReturnString();
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
//	    Job job = new Job();
//	    Workbench.getInstance().getProgressService().showInDialog(new Shell(),)
//		vcmHostLocation = repositoryDescriptor.getHost();
//		repositoryPath = repositoryDescriptor.getPath();
		String[] actualCommand = new String[command.length + 3];
		actualCommand[0] = skriptName; 
		actualCommand[1] = command[0];
		actualCommand[2] = this.user;
		actualCommand[3] = this.password;
		for (int i = 1; i < command.length; i++) {
			actualCommand[i + 3] = command[i];
			
		}
		for (int i = 0; i < actualCommand.length; i++) {
			System.out.print(actualCommand[i]);
			System.out.print(" ** ");
		}
		System.out.println("** Stopp endegelände");
//		String[] command = ((CVSRepositoryLocation)location).getExtCommand(password);
//		String[] command = {"C:\\Temp\\test","marvin"};
		try {
			process = Util.createProcess(actualCommand, monitor);

//			inputStream = new PollingInputStream(new TimeoutInputStream(process.getInputStream(),
//					32768 /*16384 bufferSize*/, -1 /*readTimeout*/, -1 /*closeTimeout*/), 60, monitor);
//			outputStream = new PollingOutputStream(new TimeoutOutputStream(process.getOutputStream(),
//					16384 /*8192buffersize*/, 4000 /*writeTimeout*/, 4000 /*closeTimeout*/), 60, monitor);
			// 
			// discard the input to prevent the process from hanging due to a full pipe
	
			MessageConsoleStream stream1,stream2 = null;
			MessageConsole console= new MessageConsole("Kobold VCM Console",null);
			ConsolePlugin.getDefault().getConsoleManager().addConsoles(
				new IConsole[] {console});
			stream2 = console.newMessageStream();
			connected = true;
			
			inputThread = new InputThreadToConsole(process/*.getInputStream()*/, stream2);
			try
            {
			    Workbench.getInstance().getProgressService().run(true,false,(InputThreadToConsole)inputThread) ;
            } catch (Exception e)
            {
                // TODO: handle exception
            }
			

			//
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
		    if(inputStream!=null)
		    if(inputStream.available()==0)
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
				
				if (errorThread != null) 
					{
						errorThread = null;
					}
				if (inputThread != null) 
				{
				    inputThread.stop();
				    inputThread = null;
				}
				if (process != null) process = null;
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
/*
 * Deprecated , currently not in Use
 * 
 	public void readInputStreamsToConsole()
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
*/
	
	// 
	// discard the input to prevent the process from hanging due to a full pipe

	private static class InputThreadToConsole extends Thread implements IRunnableWithProgress{
		private BufferedInputStream in, errStream;
		private byte[] readLineBuffer = new byte[512];
		IProgressMonitor monitor = null;
		private Process proc = null;
		private MessageConsoleStream stream = null;
		String returnString = null;
		public InputThreadToConsole(Process proc,MessageConsoleStream stream ) {
			this.in = new BufferedInputStream(proc.getInputStream());
			this.stream = stream;
			this.errStream = new BufferedInputStream(proc.getErrorStream());
			this.proc = proc;
		}
		public void run(){
		    int index = 0, r = 0, s = 0, i = 0, lineCount = 250;
            try
            {
                if(monitor != null)monitor.beginTask("VCM Action....",100000);
                
                while (in.available() == 0 & errStream.available() == 0)
                {
                    sleep(5);       
                }
                lineCount=50000;
                while ( lineCount == 50000 || i < 250)
                {
                    monitor.worked(1);
                    try
                    {
                        lineCount =  proc.exitValue();
                    } catch (Exception e)
                    {
                        // Don't care
                    }
                    if (in.available() != 0)
                    {
                        while ((r = in.read()) != -1)
                        {
                            monitor.worked(1);
                            if (r == NEWLINE)break;
                            readLineBuffer = append(readLineBuffer, index++,
                                    (byte) r);
                            
                            if(in.available() == 0) break;
                        }
                    }
                    if (errStream.available() != 0)
                    {
                        while ((s = errStream.read()) != -1)
                        {
                            monitor.worked(1);
                            if (s == NEWLINE) break;
                            readLineBuffer = append(readLineBuffer, index++,
                                    (byte) s);
                            if(errStream.available() == 0) break;
                            
                        }
                    }
                    if (returnString != null)
                    {
                        if (returnString.equals(""))
                            returnString = new String(readLineBuffer, 0, index);
                        else{
                            if(index != 0)returnString = returnString.concat("\n");
                            returnString = returnString.concat(new String(
                                    readLineBuffer, 0, index));
                            	
                            
                        }
                        readLineBuffer = new byte[512];
                        index = 0;
                        monitor.worked(1);
                    } else
                    {
                        monitor.worked(1);
                        stream.print(new String(readLineBuffer, 0, index));
                        System.out
                                .print(new String(readLineBuffer, 0, index));

                        this.readLineBuffer = new byte[512];
                        index = 0;
                    }
                    if (in.available() == 0 && errStream.available() == 0 && lineCount != 50000)
                    {
                        monitor.worked(1);
                        i++;
                    }
                }
                monitor.done();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
		public void run2() {

				try
            {
                if (in != null)
                {
                    int r = 0, s = 0, exit = 0, index = 0;
                    //					sleep(2500);
//                    System.out.println(in.toString() + " :" + in.available()
//                            + errStream.available());//(in.available() != 0) &&
                    try
                    {
                        sleep(1050);
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    while (r != -1 | s != -1 | exit < 120)
                    {
                        //						int test = errStream.available();
                        if (errStream != null && errStream.available() != 0)
                        {
                            while ((s = errStream.read()) != -1)
                            {
                                if(s == NEWLINE) break;
                                readLineBuffer = append(readLineBuffer,
                                        index++, (byte) s);
                            }
                        } else
                            {
                            	s = -1;
                            	exit++;
                            }
                        //						int testIn = ;
                        if (in.available() != 0)
                        {
                            while (in.available() != 0 & (r = in.read()) != -1)
                            {
                                if(r == NEWLINE) break;
                                readLineBuffer = append(readLineBuffer,
                                        index++, (byte) r);
                            }

                        } else
                        {
                            r = -1;
                            exit++;
                        }
                        if (returnString != null)
                        {
                            if (returnString.equals(""))
                                returnString = new String(readLineBuffer, 0,
                                        index);
                            else
                                returnString = returnString.concat(new String(
                                        readLineBuffer, 0, index));
                            readLineBuffer = new byte[512];
                            index = 0;
                        }

                        else
                        {
                            stream.print(new String(readLineBuffer, 0, index));
                            readLineBuffer = new byte[512];
                            index = 0;
                        }

                    }
                    //					if(returnString != null)
                    //						returnString = new String(readLineBuffer, 0, index);
                    //					else{
                    //						stream.print(new String(readLineBuffer, 0, index));
                    //					}
                    //					stream.getConsole().

                }
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                try
                {
                    in.close();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
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
		
		/**
		 * @return Returns the returnString.
		 */
		public String getReturnString() {
			return returnString;
		}
		/**
		 * @param returnString The returnString to set.
		 */
		public void setReturnString(String returnString) {
			this.returnString = returnString;
		}
        /* (non-Javadoc)
         * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
         */
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
            this.monitor = monitor;
            run();
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
	
	/**
	 * Gets the userName
	 * @return the username
	 */
	private String getUserName ()
	{
		//gets the userName
		String uN = KoboldVCMPlugin.getDefault().getPreferenceStore().getString("User Name");
 
		if (uN.equals(""))
		{
		    
			uN = getPreference (VCMPreferencePage.KOBOLD_VCM_USER_STR);
			//@ FIXME Dangerouse ;)
			setUserName(uN);
			return uN;
		}
		return uN;


	}

	/**
	 * Sets the userName to the preferences
	 * @param userName, the userName to store
	 */
	protected void setUserName (String userName)
	{
		//set the default userName (initial)
	    if (userName != null)
        {
	        KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_USER_STR, userName);
        }
	    else
	    {
	        MessageDialog.openError(new Shell(),"VCM User not set","VCM User not set, please reconfigure!");
	    }
	}

	/**
	 * gets the stored userName
	 * @return the stored userName
	 */
	private String getUserPassword ()
	{
		//gets the userPassword
	    Preferences prefs = KoboldVCMPlugin.getDefault().getPluginPreferences();
		String uP = prefs.getString(VCMPreferencePage.KOBOLD_VCM_PWD_STR);
		
		if (prefs.getBoolean(VCMPreferencePage.KOBOLD_VCM_ASK_PWD) || prefs.getString(VCMPreferencePage.KOBOLD_VCM_PWD_STR).equals(""))
		{
			uP = getPreference (VCMPreferencePage.KOBOLD_VCM_PWD_STR);
			setUserPassword(uP);
			return uP;
		}
		return uP;
	}

	/**
	 * Sets the new userName
	 * @param userPassword, the userPassword to store
	 */
	private void setUserPassword (String userPassword)
	{
		KoboldVCMPlugin.getDefault().getPreferenceStore().setValue(VCMPreferencePage.KOBOLD_VCM_PWD_STR,userPassword);
	}

	/**
	 * Opens a input Dialog to enter the user-data
	 * @param type, the variableName to get of the user
	 * @return the input-value of the dialog
	 */
	private String getPreference (String type)
	{
	    String preferenceName = "";
	    if (type.equals(VCMPreferencePage.KOBOLD_VCM_PWD_STR))
        {
	        preferenceName = type;
            type = "VCM User Password"; 
        } 
	    if (type.equals(VCMPreferencePage.KOBOLD_VCM_USER_STR))
	    {
	        preferenceName = type;
	        type = "VCM User Name";
	    }
		InputDialog in = new InputDialog (new Shell(), "Please enter the " + type, "Please enter the " + type +":", null, null);
		//open the dialog
		in.open();
		return in.getValue ();
	}
	/**
	 * @param repositoryDescriptor The repositoryDescriptor to set.
	 */
	public void setRepositoryDescriptor(
			RepositoryDescriptor repositoryDescriptor) {
		this.repositoryDescriptor = repositoryDescriptor;
	}
}
