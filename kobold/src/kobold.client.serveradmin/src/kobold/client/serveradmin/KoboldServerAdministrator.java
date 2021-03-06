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
 *This class is used to administrate a Kobold server. It implements
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
     * If set to true, this member prevents all logging operations of this
     * class ignoring any other options.
     */
    public static final boolean forceNoLogging = true;

    /**
     * This member is used for logging operations
     * 
     * @see org.apache.commons.logging.LogFactory
     * @see org.apache.commons.logging.Log
     */
    public static final Log logger = forceNoLogging ? null : LogFactory.getLog(KoboldServerAdministrator.class);

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
            debOut("creating new SecureXmlRpcClient instance with " +
                         url.toString());
        	client = new SecureXmlRpcClient(url);
        }
        catch(Exception e){
            client = null;
            errOut("Caught exception while instanciating new " + 
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
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }
        
        // 2.) Call checkAdministrability() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        try{
            debOut("Trying to call checkAdministrability on the server.");
        	return (String) client.execute("checkAdministrability", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling " +
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
     * @param resource the atomar top-level directory name where this productline
     *        is located 
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
                                 String resource,
                                 RepositoryDescriptor repositoryDescriptor){
        
        // 1.) check if the server is reachable.
        {
        	String ret = checkAdministrability(adminPassword); 
        	if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not create new productline. " +
                             "checkAdministrability() failed.");
        		return ret;
        	}
        }
        
        // 2.) call newProductline() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        v.add(resource);
        v.add(repositoryDescriptor.getHost());
        v.add(repositoryDescriptor.getPath());
        v.add(repositoryDescriptor.getProtocol());
        v.add(repositoryDescriptor.getRoot());
        v.add(repositoryDescriptor.getType());

        try{
            debOut("Trying to call newProductline() on the server.");
            return (String) client.execute("newProductline", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling newProductline() on " +
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
                errOut("Could not invalidate productline. " +
                             "checkAdministrability() failed.");
                return ret;
            }
        }

        // 2.) call invalidateProductline() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        
        try{
            debOut("Trying to call invalidateProductline on the server."); 
            return (String) client.execute("invalidateProductline", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling " +
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
                errOut("Could not create assign ple. " +
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
            debOut("Trying to call assignPle() on the server.");
            return (String) client.execute("assignPle", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling assignPle() on " +
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
                              String nameOfProductline,
                              String nameOfUser){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not unassign ple. checkAdministrability()" +
                             " failed.");
                return ret;
            }
        }

        // 2.) call unassignPle() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        v.add(nameOfUser);
        
        try{
            debOut("Trying to call unassignPle() on the server.");
            return (String) client.execute("unassignPle", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling unassignPle() on " +
                         "the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    }
    
    /**
     * This method returns the usernames of all users that are assigned to
     * the specified productline.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param nameOfProductline name of the productline whose maintainers' names
     *        should be returned
     * @return String containing all assigned usernames (seperated by '\n') or
     *         one of the IKoboldServerAdministration error strings
     */
    public String getPles(String adminPassword, String nameOfProductline){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not get ple list. checkAdministrability()" +
                             " failed.");
                return ret;
            }
        }

        // 2.) call getPles() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(nameOfProductline);
        
        try{
            debOut("Trying to call getPles() on the server.");
            return (String) client.execute("getPles", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling getPles() on " +
                         "the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                        
    }
    
    /**
     * This method returns the names of all productlines that are currently 
     * registered on the called Kobold server.
     *  
     * @param adminPassword the Kobold server's administration password
     * @return String containing all the registered productlines' names
     *         (seperated by '\n')  or one of the IKoboldServerAdministration 
     *         error strings
     */
    public String getProductlines(String adminPassword){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not get productline names. check" +
                             "Administrability() failed.");
                return ret;
            }
        }

        // 2.) call getProductlines() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        
        try{
            debOut("Trying to call getProductlines() on the server.");
            return (String) client.execute("getProductlines", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling hetProductlines() " +
                         "on the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                

    }
    
    /**
     * This method returns the usernames of all registered users.
     * 
     * @param adminPassword the Kobold server's administration password
     * @return String containing all registered usernames (seperated by '\n')
     *         or one of the IKoboldServerAdministration error strings
     */
    public String getRegisteredUsers(String adminPassword){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not get usernames. check" +
                             "Administrability() failed.");
                return ret;
            }
        }

        // 2.) call getRegisteredUsers() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        
        try{
            debOut("Trying to call getRegisteredUsers() on the server.");
            return (String) client.execute("getGeneralUsers", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling getRegisteredUsers()" +
                         " on the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    }
    
    /**
     * This method creates a new kobold user and adds it to the server.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param username the new user�s username
     * @param fullname the new user's full name
     * @param password the password for the new user
     * @return RETURN_OK if the new user could be created successfully, 
     *         RETURN_FAIL if the server encountered an error (e.g. if the
     *         passed username has already been assigned) or 
     *         RETURN_SERVER_UNREACHABLE if the Kobold server could not be
     *         reached
     */
    public String addKoboldUser(String adminPassword, 
                                String username, 
                                String fullname,
                                String password){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not add new user. check" +
                             "Administrability() failed.");
                return ret;
            }
        }

        // 2.) call addKoboldUser() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(username);
        v.add(fullname);
        v.add(password);
        
        try{
            debOut("Trying to call addKoboldUser() on the server.");
            return (String) client.execute("addKoboldUser", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling addKoboldUser()" +
                         " on the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                    
    }
    
    /**
     * This method removes a registered user from the Kobold server. Note that
     * a user will be removed even if its still assigned to assets.
     * 
     * @param adminPassword the Kobold server's administration password
     * @param username username of the user that should be removed
     * @return RETURN_OK if the user could be removed successfully,
     *         RETURN_FAIL if the server encountered an error (e.g. if the
     *         passed username has not been registered) or
     *         RETURN_SERVER_UNREACHABLE if the Kobold server could not be
     *         reached
     */
    public String removeKoboldUser(String adminPassword, String username){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not remove user. check" +
                             "Administrability() failed.");
                return ret;
            }
        }

        // 2.) call removeKoboldUser() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(username);
        
        try{
            debOut("Trying to call removeKoboldUser() on the server.");
            return (String) client.execute("removeKoboldUser", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling removeKoboldUser()" +
                         " on the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    }
    
    /**
     * @param adminPassword the Kobold server's administration password
     * @param username name of the user that should be checked for assignements
     * @return RETURN_USER_ASSIGNED if the specified user is still assigned to
     *         at least one asset, RETURN_USER_NOT_ASSIGNED if the specified
     *         user is not assigned to any asset, RETURN_FAIL if the server
     *         encountered an error, RETURN_SERVER_UNREACHABLE if the Kobold
     *         server was unreachable 
     */
    public String checkUserAssignements(String adminPassword, String username){
        // 1.) check if the server is reachable.
        {
            String ret = checkAdministrability(adminPassword); 
            if (!ret.equals(IKoboldServerAdministration.RETURN_OK)){
                errOut("Could not check user assignements. check" +
                             "Administrability() failed.");
                return ret;
            }
        }

        // 2.) call checkUserAssignements() on the server
        Vector v = new Vector();
        v.add(adminPassword);
        v.add(username);
        
        try{
            debOut("Trying to call checkUserAssignements() on the server.");
            return (String) client.execute("checkUserAssignements", v);
        }
        catch(Exception e){
            errOut("Caught exception while calling checkUserAssignements()" +
                         " on the server. Exception: " + e.toString());
            return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;
        }                
    }
    
    /**
     * This helper function logs a debug message
     * @param message String containing the message to log
     */
    private static void debOut(String message){
        if (!forceNoLogging){
            logger.debug(message);
        }
    }// end debOut()
    
    private static void errOut(String message){
        if (!forceNoLogging){
            System.err.print(message);
            logger.error(message);
        }
    }// end errOut()
}