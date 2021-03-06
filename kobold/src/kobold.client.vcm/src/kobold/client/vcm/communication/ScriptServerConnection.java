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
 * $Id: ScriptServerConnection.java,v 1.58 2004/11/25 14:59:20 garbeam Exp $
 */
package kobold.client.vcm.communication;

import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.internal.ccvs.core.IServerConnection;
import org.eclipse.team.internal.ccvs.core.connection.CVSAuthenticationException;
import org.eclipse.team.internal.ccvs.core.util.Util;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.internal.Workbench;

/**
 * @author schneipk
 */
public class ScriptServerConnection implements IServerConnection
{
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(ScriptServerConnection.class);

	// The variable for verifiying the connection is establisched
	private static boolean connected = false;
	
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
	
	// The return Value of the process
	int returnValue = -1;
	
	public static final char NEWLINE= 0xA;
	
	
	public static ScriptServerConnection getConnection() {
	    ScriptServerConnection result = new ScriptServerConnection();
	    return result;
	}
	
	/** 
	 * The protected default constructor for this class
	 */
	protected ScriptServerConnection() {


	}
	
	
	/**
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

			MessageConsoleStream stream2 = null;
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
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#open(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void open(IProgressMonitor monitor,String[] command) throws IOException,
			CVSAuthenticationException
	{

		try {
			process = Util.createProcess(command, monitor);

			MessageConsoleStream stream1,stream2 = null;
			MessageConsole console= new MessageConsole("Kobold VCM Console",null);
			IConsoleManager conMgr = ConsolePlugin.getDefault().getConsoleManager();
			IConsole cons[] = conMgr.getConsoles();
			for (int i = 0; i < cons.length; i++)
            {
			    IConsole currentConsole = cons[i];
			    if (currentConsole.getName().equals("Kobold VCM Console"))
                {
                    console = (MessageConsole)cons[i];
                    stream2 = console.newMessageStream();
                    stream2.println("******************new Action********************");
                    
                } else
                {
                    conMgr.addConsoles(
            				new IConsole[] {console});
                    stream2 = console.newMessageStream();
                }
            }
			if (stream2 == null)
            {
                conMgr.addConsoles(
        				new IConsole[] {console});
                stream2 = console.newMessageStream();
            }
			connected = true;
			inputThread = new InputThreadToConsole(process/*.getInputStream()*/, stream2);
			errorThread = new InputThreadToConsole(process/*.getInputStream()*/, stream2);
			((InputThreadToConsole)errorThread).setErrProccessing(true);
			try
            {
			    errorThread.start();
			    Workbench.getInstance().getProgressService().run(true,false,(InputThreadToConsole)inputThread) ;
				process.waitFor();
				returnValue =  process.exitValue();
				if (returnValue != 0)
                {
				    /* To prevent confusion in end presentation
					MessageDialog.openError(new Shell(),"Error","An error occured while excecuting a VCM script, \n" +
					"please check the message console for further details!");
					*/
                }
				if (logger.isDebugEnabled()) {
					logger.debug("open(IProgressMonitor, String[])"
							+ returnValue);
				}
            } catch (Exception e)
            {
				logger.error("open(IProgressMonitor, String[])", e);
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
	
	/**
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
	
	/**
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#getInputStream()
	 */
	public InputStream getInputStream()
	{
		return inputStream;
	}
	
	/**
	 * @see org.eclipse.team.internal.ccvs.core.IServerConnection#getOutputStream()
	 */
	public OutputStream getOutputStream()
	{
		return outputStream;
	}
	
	private static class InputThreadToConsole extends Thread implements IRunnableWithProgress{
		/**
		 * Logger for this class
		 */
		private static final Logger logger = Logger
				.getLogger(InputThreadToConsole.class);

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
		public void setErrProccessing(boolean apply)
		{
		    if (apply)
            {
                this.in = this.errStream;
            }
		}
		public void run(){
		    int index = 0, r = 0, s = 0, i = 0, lineCount = 50;
            try
            {
                if(monitor != null)monitor.beginTask("VCM Action....",1000000);
                	else{
                	    monitor = KoboldPolicy.monitorFor(null);
                	}
//                while (in.available() == 0 && s < 50)
//                {
//                    sleep(5);   
//                    s++;
//                }
                while ( lineCount == 50 || i < 50)
                {
                    monitor.worked(1);

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
                    monitor.worked(1);
                    if (index != 0)
                    {
                        stream.println(new String(readLineBuffer, 0, index));
                    }
                    
//					if (logger.isDebugEnabled()) {
//						logger.debug("run()"
//								+ new String(readLineBuffer, 0, index));
//					}
                    this.readLineBuffer = new byte[512];
                    index = 0;
                    
                    if (in.available() == 0  && lineCount != 50)
                    {
                        monitor.worked(3);
                        i++;
                    }
                    try
                    {
                        lineCount =  proc.exitValue();
                    } catch (Exception e)
                    {
                        // Don't care
                    }
                }
                monitor.done();
            } catch (Exception e)
            {
				logger.error("run()", e);
                monitor.done();
            }

		}
		public void run3(){
		    int index = 0, r = 0, s = 0, i = 0, lineCount = 250;
            try
            {
                if(monitor != null)monitor.beginTask("VCM Action....",1000000);
                
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
						if (logger.isDebugEnabled()) {
							logger.debug("run3()"
									+ new String(readLineBuffer, 0, index));
						}

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
				logger.error("run3()", e);
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
						logger.error("run2()", e);
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
				logger.error("run2()", e);
            } finally
            {
                try
                {
                    in.close();
                } catch (IOException e1)
                {
					logger.error("run2()", e1);
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
	 * @return returnValue Returns the return Value of the process.
	 */
    public int getReturnValue()
    {
        return returnValue;
    }
}
