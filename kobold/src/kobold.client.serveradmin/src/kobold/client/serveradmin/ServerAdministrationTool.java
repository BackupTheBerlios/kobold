﻿/* * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller, *                    Martin Plies, Michael Grosse, Necati Aydin, *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen *  * Permission is hereby granted, free of charge, to any person obtaining a * copy of this software and associated documentation files (the "Software"), * to deal in the Software without restriction, including without limitation * the rights to use, copy, modify, merge, publish, distribute, sublicense, * and/or sell copies of the Software, and to permit persons to whom the * Software is furnished to do so, subject to the following conditions: *  * The above copyright notice and this permission notice shall be included * in all copies or substantial portions of the Software. *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR * OTHER DEALINGS IN THE SOFTWARE. *  *  */package kobold.client.serveradmin;import kobold.client.serveradmin.KoboldServerAdministrator;import kobold.common.controller.IKoboldServerAdministration;import kobold.common.io.RepositoryDescriptor;
import java.net.URL;import java.util.Properties;import java.io.FileInputStream;import org.apache.commons.logging.LogFactory;import org.apache.commons.logging.Log;/** * This class acts as a frontend for KoboldServerAdministration objects.  * Its purpose is to communicates directly with users (console style io)  * who intend to administrate Kobold servers. *  * According to the input provided by the user, this class uses a  * KoboldServerAministartor object to call the Kobold server's  * IKoboldServerAdministartion methods.  *  * This SAT is started by simply passing CT control to its main()-method. *  * @see kobold.client.serveradmin.KoboldServerAdministrator * @see kobold.common.controller.IKoboldServerAdministration *  * @author contan */public class ServerAdministrationTool {
    /**     * This member is used for logging operations     *      * @see org.apache.commons.logging.LogFactory     * @see org.apache.commons.logging.Log     */    public static final Log logger =                         LogFactory.getLog(ServerAdministrationTool.class);
    /**     * This member is used to administrate Kobold servers. It implements     * IKoboldServerAdministration so that a KoboldServerAdministrator instance     * can be accessed as if it actually was a Kobold server (calls of     * IKoboldServerAdministration-methods are delegated to the actual server).     *      * @see kobold.client.serveradmin.KoboldServerAdministrator     */    private static KoboldServerAdministrator serverAdmin = null;        /**     * This member stores the password for the current Kobold server.     * It is set during the execution of main().     */    private static java.lang.String currentPassword = "";
    /**     * This member stores the current Kobold server's URL.     * It is set during the execution of main().     */    private static java.lang.String currentURL = "https://localhost:23232";
    /**     * the following constant Strings are used to define the syntax     * of commands which can be input by SAT-users.     */    private static final String SAT_CMD_NEW_PRODUCTLINE = "newpl";    private static final String SAT_CMD_INVAL_PRODUCTLINE = "removepl";    private static final String SAT_CMD_ADD_PLE = "assignple";    private static final String SAT_CMD_INVAL_PLE = "unassignple";    private static final String SAT_CMD_SHOW_CMD_SUMMARY = "help";    private static final String SAT_CMD_ABOUT = "about";    private static final String SAT_CMD_TERMINATE_SAT = "exit";    private static final String SAT_CMD_SET_PASSWORD = "setpasswd";    private static final String SAT_CMD_SET_SERVER_URL = "seturl";    private static final String SAT_CMD_GET_PLE_LIST = "getplelist";    private static final String SAT_CMD_GET_PRODUCTLINES = "getpllist";    private static final String SAT_CMD_GET_USER_LIST = "getuserlist";
/*--------------------------------------------------------------------------------     The following section containes all controller methods of this class.--------------------------------------------------------------------------------   */        /**     * This method acts as entry point for the Kobold Server Administration      * Tool. The user is asked to provide the URL of the Kobold server to      * administrate and to provide a valid admin-password (see method      * startup()). The provided data is evaluated (see method checkServer())      * and -if this succeeds- the user can proceed administrating the Kobold      * server.     *      * @param args - all passed parameters are ignored!     */    public static void main(String[] args) {        debOut("entering ServerAdministartionTool");        stdOut("\t\t\t\t * Kobold Server Administration Tool - v2.0 *\n\n");        // 1.) perform startup ops -> see startup()        performStartup();
        // 2.) now show the promt until the user quits        while(executePrompt());
        // 3.) Terminate SAT        infOut("\nLeaving SAT...");    } // end main()
    /**     * This method performs the standard startup operations. It is called by     * main() and tries to access the server specified by 'currentURL and      * 'currentPassword.     */    private static void performStartup(){        debOut("performing startup operations");        try {            Properties props = new Properties(System.getProperties());            props.load(new FileInputStream(System.getProperty("kobold.sat.configFile")));            System.setProperties(props);        } catch (Exception e) {            errOut("Could not find client configuration",e);        }        String ret = checkKoboldServer();        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            infOut("Logged on " + currentURL + "\n\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){            infOut("Could reach " + currentURL + " but could not log on.\n" +                   "Check your password.\n\n");        }        else{            infOut("Could not reach " + currentURL + ". The server is either " +                    "down or the newtwork connection \nhas failed or the set " +                   "url is wrong.\n\n");        }
        stdOut("Type '" + SAT_CMD_SHOW_CMD_SUMMARY + "' to show a summary of " +               "available commands and to learn more about this program's " +               "purpose.\n");    }//performStartup()
    /**     * Validates the accessibility of the server specified by 'currentURL with      * the password 'currentPassword.     *      * @return IKoboldServerAdministration.RETURN_OK if the server was reachable     *         and accepted the password,IKoboldServerAdministration.RETURN_FAIL     *         if the server was reachable but did not accept the password,     *         IKoboldServerAdministration.SERVER_UNREACHABLE if the server     *         could not even bee reached.     */    private static String checkKoboldServer(){        if (serverAdmin == null){            URL url;            try{                url = new URL (currentURL);            }            catch(Exception e){                /*                  * since 'currentURL obviously contains a bad url, the server                 * is unreachable                 */                return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;            }
            serverAdmin = new KoboldServerAdministrator();            serverAdmin.setServer(currentPassword, url);         }                return serverAdmin.checkAdministrability(currentPassword);    } // checkKoboldServer    /*--------------------------------------------------------------------------------    The following section contains all the member functions that execute the    commands that are fetched by executePrompt().-------------------------------------------------------------------------------- */       /**     * This function askes the user to input the name of the productline that     * should loose its PLE and tries to unassign it (by calling 'serverAdmin).      * It is called by executePrompt().     */    private static void removePLE(){        //1.) get the neccessary data        debOut("removePLE() : asking user for details");                stdOut("Please provide the name of the productline whose PLE you " +               "want to unassign:");        String plsname = readInput();                stdOut("Please provide the name of the user that should no longer be " +               plsname + "'s PLE.");        String username = readInput();                // 2.) Try to perform the necessary operations on the server        debOut("removePLE(): trying to call server");        try{            String ret = serverAdmin.unassignPle(currentPassword,                                                  plsname,                                                  username);
            // 3.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_OK)){                infOut(plsname + " got rid of its PLE \"" + username + "\".\n");            }            else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){                infOut("The server could not get rid of " + plsname +                        "'s PLE \"" + username + "\".\nMaybe due to incorrect" +                        "spelling of productline's name?\n");            }            else{                infOut("Could not get rid of " + plsname + "'s PLE \"" +                        username + "\"; " + "the server was unreachable.\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the server.\n",                   e);        }    }
    /**     * This function askes the user to input the names of the productline that     * should get a new PLE and the name of the user that is to be assigned      * new PLE and tries to assign it (by calling 'serverAdmin). It is called      * by executePrompt().     */    private static void addPLE(){        //1.) get the neccessary data        debOut("addPLE() : asking the user for details");                stdOut("Please provide the name of the productline you want to get " +               "a new PLE:");        String plsname = readInput();                stdOut("Please provide the name of the user that is to become " +                plsname + "'s new PLE:");        String username = readInput();
        // 2.) Try to perform the necessary operations on the server        debOut("trying to assign " + username + " to " + plsname);        try{            String ret = serverAdmin.assignPle(currentPassword,                                                plsname,                                                username);                        // 3.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_OK)){                 infOut(username + " is now " + plsname + "'s new PLE.\n");            }            else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){                infOut("Could not make " + username + " become " + plsname +                        "'s new PLE.\nMaybe the productline's or the user's " +                       "name's spelling is incorrect?\n");            }            else{                infOut("Could not make " + username + " become " + plsname +                       "'s new PLE.\nThe server was unreachable.\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   " to make " + username + " " + plsname + "'s new PLE.",                   e);        }    }        /**     * This function askes the user to input the name of the productline that     * should be invalidated and tries to invalidate it (by calling      * 'serverAdmin). It is called by executePrompt().     */    private static void removeProductline(){        //1.) get the neccessary data        debOut("removeProductline() : asking the user for details");                stdOut("Please provide the name of the productline to be invalidated:");        String plsname = readInput();                // 2.) Try to perform the necessary operations on the server        debOut("Trying to invalidate " + plsname);        try{            String ret = serverAdmin.invalidateProductline(currentPassword,                                                           plsname);            // 3.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_OK)){                 infOut("Productline \"" + plsname + "\" " + "successfully " +                       "removed.\n");            }            else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){                infOut("Productline \"" + plsname + "\" could not be " +                       "invalidated. Maybe \"" + plsname + "\"has already " +                       "been invalidated?\n");            }            else{                infOut("Could not invalidate \"" + plsname + "\"; the server " +                       "was unreachable.\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to remove the productline " + plsname + ".\n", e);            return;        }    }        /**     * This function askes the user to input the name of the new productline      * and tries to create it (by calling 'serverAdmin). It is called by      * executePrompt().     */    private static void createProductline(){        //1.) get the neccessary data        debOut("createProsuctline() : asking user for details");
        stdOut("Please provide a name for the new productline: ");        String plsname = readInput();        stdOut("Please provide a resource (directory) name for the new productline: ");        String resname = readInput();       
        stdOut("Please define the repository type (cvs, svn, arch, ...): ");        String type = readInput();
        stdOut("Please define the protocol (ssh, pserver, svn, WebDAV, ...): ");        String protocol = readInput();
        stdOut("Please define the hostname (cvs.myhost.com): ");        String host = readInput();
        stdOut("Please define the repository root (/cvsroot/myrep/): ");        String root = readInput();
        stdOut("Please define the module path w/o repository root (myrep): ");        String path = readInput();
        RepositoryDescriptor rd = new RepositoryDescriptor(type,                                                           protocol,                                                           host,                                                           root,                                                           path);                               
        // 2.) Try to perform the necessary operations on the server        debOut("trying to create new productline \"" + plsname + "\"");        try{            String ret = serverAdmin.newProductline(currentPassword,                                                     plsname, resname,                                                    rd);
            // 3.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_OK)){                infOut("New productline \"" + plsname + "\" successfully " +                       "created.\n");            }            else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){                infOut("New productline \"" + plsname + "\" could not be " +                       "created due to an error on the Server \n(maybe there " +                        "is already a productline named " + plsname + "?\n");            }            else{                infOut("\"" + plsname + "\" could not be created. The server " +                       "was unreachable.\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to create the new productline " + plsname + ".\n", e);            return;        }    }// end createPoductline()    /**     * This method asks the user to provide the name of a registered productline     * and tries to obtain and print a list of all users who are assigned      * maintainers of the specified productline.     */    private static void getPles(){        //1.) get the neccessary data        debOut("getPles() : asking user for details");        stdOut("Please provide the name of the productline whose PLE list you" +               " want to display: ");        String plsname = readInput();                // 2.) Try to perform the necessary operations on the server        debOut("trying to get plelist for \"" + plsname + "\"");        try{            String ret = serverAdmin.getPles(currentPassword, plsname);                        // 3.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){                infOut("Could not get PLE list for productline \"" + plsname +                       "\". Maybe that productline does not exist?");            }            else if (ret.equals(IKoboldServerAdministration.                                RETURN_SERVER_UNREACHABLE)){                infOut("Could not get PLE list for productline \"" + plsname +                        "\". The server was unreachable.\n");            }            else{                stdOut("PLE list of \"" + plsname + "\":\n" + ret + "\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to get the ple list for " + plsname + ".\n", e);            return;        }            }        /**     * This method tries to get and print a list of the names of all the     * productlines that are currently registered on the server.      */    private static void getProductlines(){        // 1.) Try to perform the necessary operations on the server        debOut("trying to get productline list from server");        try{            String ret = serverAdmin.getProductlines(currentPassword);                        // 2.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){                infOut("Could not get productline list from server. Check " +                       "your password.\n");            }            else if (ret.equals(IKoboldServerAdministration.                                RETURN_SERVER_UNREACHABLE)){                infOut("Could not get productline list. The server was " +                       "unreachable.\n");            }            else{                stdOut("Productline list of \"" + currentURL + "\":\n" + ret +                        "\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to get the productline list.\n", e);            return;        }            }        /**     * This method tries to obtain and print a list of all the useres who are     * currently registered on the server.     */    private static void getRegisteredUsers(){        // 1.) Try to perform the necessary operations on the server        debOut("trying to get userlist from server");        try{            String ret = serverAdmin.getRegisteredUsers(currentPassword);                        // 2.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){                infOut("Could not get userlist from server. Check " +                       "your password.\n");            }            else if (ret.equals(IKoboldServerAdministration.                                RETURN_SERVER_UNREACHABLE)){                infOut("Could not get userlist. The server was unreachable.\n");            }            else{                stdOut("Registered users on \"" + currentURL + "\":\n" + ret +                        "\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to get the userlist.\n", e);            return;        }                    }        /**     * This function askes the user to provide a new url and changes      * 'currentURL (which specifies the server) accordingly. A server check     * is then performed to validate that the newly specified server is      * reachable and the user is informed about the results.     */    private static void changeServerUrl(){        // 1.) ask for necessary data        debOut("changeServerUrl() - asking the user for details.");
        stdOut("Please provide the new server's url : ");        currentURL = readInput();
        // 2.) verify syntactic correctness of 'currentURL        if (!checkUrlCorrectness()){            /*             * 'currentURL contains an invalid url - inform the user and             * quit this method.             */            infOut("Could not change the server's url. \"" + currentURL +                   "\" is not a valid url.\n");            return;        }
        // 3.) check server        serverAdmin = null;        String ret = checkKoboldServer();
        // 4.) inform the user about the results        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            infOut("Url changed to " + currentURL + ". Server was reachable " +                   "and accepted your current password.\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){            infOut("Url changed to " + currentURL + ". Server was reachable " +                   "but didn't accept your current password.\n");        }        else{            infOut("Url has been changed but no Kobold server responded at " +                   currentURL + ".\n");        }    }//changeServerUrl()       /**     * This function askes the user to provide a new password and changes      * 'currentPassword (which is used to access the server) accordingly.     * A server chack is performed to validate that the current server      * accepts the new password and the user is informed about the results.     */    private static void changePassword(){        // 1.) ask for necessary data        debOut("changePassword() - asking the user for details.");        stdOut("Please provide the new administrator password: ");        currentPassword = readInput();
        // 2.) check server        serverAdmin = null;        String ret = checkKoboldServer();
        // 3.) inform the user about the results        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            infOut("Password changed. Server was reachable and accepted your " +                   "new password.\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){            infOut("Password changed. Server was reachable but didn't accept " +                   "your current password.\n");        }        else{            infOut("Password has been changed but no Kobold server responded " +                   "at " + currentURL + ".\n");        }    }//changePassword    /**     * This method prints a summary of available commands for the SAT on the      * output stream. It is called by provideSATPrompt(), when the user inputs      * 'help'.     */    private static void showCommandSummary(){        // 1.) print program's purpose        stdOut("This program's purpose is to give the administrator of a " +               "Kobold server an lightweight, easy to use\ntool to create/" +               "invalidate productlines and to assign/unsassign productline " +               "engineers.\n\n");
        // 2.) print command summary        stdOut("Available commands:\n\t" +                SAT_CMD_TERMINATE_SAT + " - terminates the SAT\n\t" +               SAT_CMD_SHOW_CMD_SUMMARY + " - shows this help\n\t" +               SAT_CMD_ABOUT + " - shows information about this program's " +                               "version and licence\n\t" +               SAT_CMD_SET_SERVER_URL + " - changes the current URL\n\t" +               SAT_CMD_SET_PASSWORD + " - changes the password that is used " +                                      "to access the current server\n\t" +               SAT_CMD_NEW_PRODUCTLINE + " - creates a new productline\n\t" +               SAT_CMD_INVAL_PRODUCTLINE + " - removes a productline\n\t" +               SAT_CMD_ADD_PLE + " - sets a productline's new PLE\n\t" +               SAT_CMD_INVAL_PLE + " - removes a productline's PLE\n\t" +               SAT_CMD_GET_PRODUCTLINES + " - shows a list of all productlines"+                                      " that are registered on the server\n\t" +               SAT_CMD_GET_PLE_LIST + " - shows a list of all assigned ples " +                                      "for a productline\n\n");                // 3.) print current "server stats"
        stdOut("Current administrated Kobold server is at \"" + currentURL +                "\"\n");        String ret = checkKoboldServer();        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            stdOut("It is reachable and accepting the your password.\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){            stdOut("It is reachable but not accepting your password. Please " +                   "change it.\n");        }        else{            stdOut("Unfortunately the server is currently not reachable. " +                   "Please check your url and/or your network connection.\n");        }    }
    /**     * This methods prints information about this program's version and licence.      */    private static void showAbout(){        stdOut("Kobold Server Administration Tool\n\nVersion 2.0\n\n" +               "Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina " +                "Druckenmueller,\n                   Martin Plies, Michael " +                "Grosse, Necati Aydin,\n                   Oliver Rendgen, " +                "Patrick Schneider, Tammo van Lessen\n\nPermission is hereby " +                "granted, free of charge, to any person obtaining\na copy of " +                "this software and associated documentation files (the " +                "\"Software\"),\nto deal in the Software without " +                "restriction, including without limitation\nthe rights to " +                "use, copy, modify, merge, publish, distribute, sublicense," +                "\nand/or sell copies of the Software, and to permit persons " +                "to whom the\nSoftware is furnished to do so, subject to the " +                "following conditions:\n\nThe above copyright notice and " +                "this permission notice shall be included\nin all copies or " +                "substantial portions of the Software.\n\nTHE SOFTWARE IS " +                "PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS " +                "OR\nIMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF " +                "MERCHANTABILITY,\nFITNESS FOR A PARTICULAR PURPOSE AND " +                "NONINFRINGEMENT. IN NO EVENT SHALL\nTHE AUTHORS OR " +                "COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR " +                "OTHER\nLIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR " +                "OTHERWISE,\nARISING FROM, OUT OF OR IN CONNECTION WITH THE " +                "SOFTWARE OR THE USE OR\nOTHER DEALINGS IN THE SOFTWARE.\n");     }// end showAbout()
    /**     * This method provides the user with a command prompt, if     * the startup ops of the main()-method could be executed     * successfully. The user's input is collected and the according     * methods are called.     *      * @return true, if the user does not wish to exit the SAT, false otherwise     */    private static boolean executePrompt(){        stdOut("\n> ");        String input = readInput();                /*         * Evaluate input         */        if (input == ""){            return true;        }        else if (input.startsWith(SAT_CMD_TERMINATE_SAT)){            return false;        }        else if (input.startsWith(SAT_CMD_SHOW_CMD_SUMMARY)){            showCommandSummary();            return true;        }        else if (input.startsWith(SAT_CMD_ABOUT)){            showAbout();            return true;        }        else if (input.startsWith(SAT_CMD_NEW_PRODUCTLINE)){            createProductline();            return true;        }        else if (input.startsWith(SAT_CMD_INVAL_PRODUCTLINE)){            removeProductline();            return true;        }        else if (input.startsWith(SAT_CMD_ADD_PLE)){            addPLE();            return true;        }        else if (input.startsWith(SAT_CMD_INVAL_PLE)){            removePLE();            return true;        }        else if (input.startsWith(SAT_CMD_SET_SERVER_URL)){            changeServerUrl();            return true;        }        else if (input.startsWith(SAT_CMD_SET_PASSWORD)){            changePassword();            return true;        }        else if (input.startsWith(SAT_CMD_GET_PLE_LIST)){            getPles();            return true;        }        else if (input.startsWith(SAT_CMD_GET_PRODUCTLINES)){            getProductlines();            return true;        }        else if (input.startsWith(SAT_CMD_GET_USER_LIST)){            getRegisteredUsers();            return true;        }        else{            stdOut("Unknown command or wrong syntax.\n");            return true;        }    }// end executePrompt()
/*--------------------------------------------------------------------------------    The following section contains all the helper functions of this class.-------------------------------------------------------------------------------- */           /**     * This helper function reads up to 500 bytes of input data and     * returns the read input as a String.     *      * @return String containing the user's input - empty String if an error      *         occured     */    private static String readInput(){        try        {            byte inp[] = new byte [500];             int read = System.in.read(inp);            byte sinp[] = new byte [read-1];            for (int ax = 0; ax < (read-1); ax++){                sinp[ax] = inp[ax];            }            return new String (sinp);        }        catch(Exception e)        {            errOut("An error occured while accessing the input stream.\n", e);                        return new String(""); // since op failed return empty string        }            } // readInput()
    /**     * This helper method read's a single byte from the input stream as long as      * the user does not input y or n.     * @param question - String that will be printed on the output stream,      *        followed by "(y/n)"     * @return true, if the posed question was answered with yes, false      *         otherwise     */    private static boolean readYesNo(String question) throws Exception{        stdOut(question);            /*         * as long as the user does not input 'y', 'Y', 'n' or 'N' a byte         * is read form the input stream         */        while(true){            stdOut("(y/n)");            String inp = readInput();
            if (inp.charAt(0) == 'y' || inp.charAt(0) == 'Y'){                return true;            }            else if (inp.charAt(0) == 'n' || inp.charAt(0) == 'N'){                return false;            }                   } // end while    } // readYesNo()
    /**     * the following helper function prints the passed error message and     * (error-) logs it along with the passed exception's trace (if e != null).      * A '\n' is inserted between the error message and the exception's trace.     *      * @param message - the custom error message     * @param e - Exception whose trace is to be printed, or null      */    private static void errOut(String message, Exception e){        System.err.print(message);        logger.error(message + "\nException thrown: " + e.toString());    }// end errOut()
    /**     * This helper function prints and (info-) logs the passed message     * @param message String containing the message to print and log     */    private static void infOut(String message){        stdOut(message);        logger.info(message);    }// end infOut()
    /**     * This helper function logs a debug message     * @param message String containing the message to log     */    private static void debOut(String message){        logger.debug(message);    }// end debOut()
    /**     * This helper fuction prints a String that should be read by the user.     */    private static void stdOut(String message){        System.out.print(message);    }// end stdOut()
    /**     * This helper method checks if 'currentURL can be transformed into an     * URL-object without causing a malformed url exception (that is if      * 'currentURL contains a syntactically correct url).     *      * @return true, if the 'currentURL can be transformed, false otherwise     */    private static boolean checkUrlCorrectness(){        try{            URL url = new URL(currentURL);        }        catch(Exception e){            return false;        }                return true;    } // end checkUrlCorrectness()}// class ServerAdministartionTool