﻿/*
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
 */

package kobold.client.serveradmin;

import java.net.URL;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.secure.SecureXmlRpcClient;

import kobold.common.controller.IKoboldServerAdministration;
import kobold.common.io.RepositoryDescriptor;

/**
 * This class is used to administrate a Kobold server. It implements
 * IKoboldServerAdministration and acts as if it actually was the
 * Kobold server.
 *  
 * If you want to call a Kobold server's administration methods, instanciate
 * this class and call setServerURL(). Further calls of 
 * IKoboldServerAdministration-methods will be delegated to the set server
 * (if its reachable).
 * 
 * @author contan
 */
public class KoboldServerAdministrator implements IKoboldServerAdministration {
    /**
     * This member is used for logging operations
     * 
     * @see org.apache.commons.logging.LogFactory
     * @see org.apache.commons.logging.Log
     */
    public static final Log logger = 
                        LogFactory.getLog(KoboldServerAdministrator.class);

    /**
     * This member is used to communicate with Kobold servers. It is 
     * instanciated every time setServer() is called and set to null
     * if setServer() fails.
     * 
     * @see org.apache.xmlrpc.secure.SecureXmlRpcClient
     */
    private SecureXmlRpcClient client = null;
        
    /**
     * this method must be called before any of the IKoboldServerAdministration-
     * methods. it checks if the specified server is reachable and the password
     * is valid.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param url the Kobold server's url
     * 
     * @return IKoboldServerAdministration.RETURN_OK, if the check was 
     *         successful, IKoboldServerAdministration.RETURN_FAIL if an error
     *         occured while executing the method on the server (e.g. wrong 
     *         password), IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE 
     *         if the method could not be called on the server. 
     */
    public String setServer(String adminPassword, URL url){
        try{
            logger.debug("creating new SecureXmlRpcClient instance with " +
                         url.toString());
        	client = new SecureXmlRpcClient(url);
        }
        catch(Exception e){
            client = null;
            logger.error("Caught exception while instanciating new " + 
                         "SecureXmlRpcClient with url " + url.toString() +
                         "Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }
        
        return checkAdministrability(adminPassword);
    }
    
   /**
     * this method is used to check if the called Kobold server is reachable 
     * and can be administrated with the passed password
     *  
     * @param adminPassword the Kobold server's administration password
     * 
     * @return IKoboldServerAdministration.RETURN_OK, if the check was 
     *         successful, IKoboldServerAdministration.RETURN_FAIL if an error
     *         occured while executing the method on the server, 
     *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the
     *         method could not be called on the server.
     */
    public String checkAdministrability(String adminPassword){
        /*
         * 1.) check if the server's URL has been successfully set. if not, 
         *     signal that the server cannot be reached.
         */
        if (client == null){
            logger.warn("checkAdministrability() was called w/o prior " +
                        "setting the server.");
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }
        
        // 2.) Call checkAdministrability() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        try{
            logger.debug("Trying to call checkAdministrability on the server.");
        	return (String) client.execute("checkAdministrability", v);
        }
        catch(Exception e){
            logger.error("Caught exception while calling " +
                         "checkAdministrability() on the server. Exception: " + 
                         e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }        
    }
    
    /**
     * call this method if you want to create a new productline on the Kobold
     * server. Note that if there already exists a productline with the name
     * 'nameOfProductline, the method will exit with an error. 
     * 
     * @param adminPassword the Kobold server's administration password
     * @param nameOfProductline the new productline's desired name
     * @param repositoryDescriptor description of the repository for the new
     *                             productline
     * 
     * @return IKoboldServerAdministration.RETURN_OK, if the check was 
     *         successful, IKoboldServerAdministration.RETURN_FAIL if an error
     *         occured while executing the method on the server, 
     *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the
     *         method could not be called on the server.
     */
    public String newProductline(String adminPassword, 
                                 String nameOfProductline, 
                                 RepositoryDescriptor repositoryDescriptor){
        
        // 1.) check if the server is reachable.
        {
        	String ret = checkAdministrability(adminPassword); 
        	if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                logger.error("Could not create new productline. " +
                             "checkAdministrability() failed.");
        		return ret;
        	}
        }
        
        // 2.) call newProductline() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        v.add(repositoryDescriptor);
        
        try{
            logger.debug("Trying to call newProductline() on the server.");
            return (String) client.execute("newProductline", v);
        }
        catch(Exception e){
            logger.error("Caught exception while calling newProductline() on " +
                         "the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    }
    
    /**
     * to assure data consistency of products that are realted to a certain
     * productline, productlines cannot be entirely deleted. nonetheless this 
     * method provides you with the possibility to mark an existing productline
     * as invalid. 
     *  
     * @param adminPassword the Kobold server's administration password
     * @param nameOfProductline name of the productline that is to be 
     *                          invalidated
     * 
     * @return IKoboldServerAdministration.RETURN_OK, if the check was 
     *         successful, IKoboldServerAdministration.RETURN_FAIL if an error
     *         occured while executing the method on the server, 
     *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the
     *         method could not be called on the server.
     */
    public String invalidateProductline(String adminPassword, 
                                        String nameOfProductline){

        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                logger.error("Could not invalidate productline. " +
                             "checkAdministrability() failed.");
                return ret;
            }
        }

        // 2.) call invalidateProductline() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        
        try{
            logger.debug("Trying to call invalidateProductline on the server."); 
            return (String) client.execute("invalidateProductline", v);
        }
        catch(Exception e){
            logger.error("Caught exception while calling " +
                         "invalidateProductline() on the server. Exception: " + 
                         e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    } 
    
    /**
     * This method assigns a user to a productline (users that are assigned
     * directly to productlines act as the productline's ple). Possible already
     * assigned user will be automatically unassigned.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param nameOfProductline name of the productline that should be assigned
     *                          a new PLE
     * @param nameOfUser name of the user that should be assigned to the 
     *                   specified productline
     * 
     * @return IKoboldServerAdministration.RETURN_OK, if the check was 
     *         successful, IKoboldServerAdministration.RETURN_FAIL if an error
     *         occured while executing the method on the server, 
     *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the
     *         method could not be called on the server.
     */
    public String assignPle(String adminPassword,
                            String nameOfProductline,
                            String nameOfUser){

        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                logger.error("Could not create assign ple. " +
                             "checkAdministrability() failed.");
                return ret;
            }
        }

        // 2.) call assignPle() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        v.add(nameOfUser);
        
        try{
            logger.debug("Trying to call assignPle() on the server.");
            return (String) client.execute("assignPle", v);
        }
        catch(Exception e){
            logger.error("Caught exception while calling assignPle() on " +
                         "the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    }
    
    /**
     * This methods unassigns a user (ple) from a productline. Since there can
     * only be one assigned ple per productline at the same time, the specified
     * productline will have no more assigned ple after a successful call of
     * this method.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param nameOfProductline name of the productline that should "loose"
     *                          the specified user
     * @param nameOfUser username specifiing which user should be unassigned 
     * 
     * @return IKoboldServerAdministration.RETURN_OK, if the check was 
     *         successful, IKoboldServerAdministration.RETURN_FAIL if an error
     *         occured while executing the method on the server, 
     *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the
     *         method could not be called on the server. 
     */
    public String unassignPle(String adminPassword,
                               String nameOfProductline){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                logger.error("Could not unassign ple. checkAdministrability()" +
                             " failed.");
                return ret;
            }
        }

        // 2.) call unassignPle() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        
        try{
            logger.debug("Trying to call unassignPle() on the server.");
            return (String) client.execute("unassignPle", v);
        }
        catch(Exception e){
            logger.error("Caught exception while calling unassignPle() on " +
                         "the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    } 
}

