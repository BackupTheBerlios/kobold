/* * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller, *                    Martin Plies, Michael Grosse, Necati Aydin, *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen *  * Permission is hereby granted, free of charge, to any person obtaining a * copy of this software and associated documentation files (the "Software"), * to deal in the Software without restriction, including without limitation * the rights to use, copy, modify, merge, publish, distribute, sublicense, * and/or sell copies of the Software, and to permit persons to whom the * Software is furnished to do so, subject to the following conditions: *  * The above copyright notice and this permission notice shall be included * in all copies or substantial portions of the Software. *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR * OTHER DEALINGS IN THE SOFTWARE. *  *  */package kobold.client.serveradmin;import java.io.FileInputStream;import java.net.URL;import java.util.Properties;import kobold.client.serveradmin.inquisitors.NewProductlineInquisitor;import kobold.client.serveradmin.inquisitors.NewUserInquisitor;import kobold.client.serveradmin.inquisitors.SetServerInquisitor;import kobold.client.serveradmin.tools.InquisitorQuestion;import kobold.common.controller.IKoboldServerAdministration;import org.apache.commons.logging.Log;import org.apache.commons.logging.LogFactory;/** * This class acts as a frontend for KoboldServerAdministration objects.  * Its purpose is to communicates directly with users (console style io)  * who intend to administrate Kobold servers. *  * According to the input provided by the user, this class uses a  * KoboldServerAministartor object to call the Kobold server's  * IKoboldServerAdministartion methods.  *  * This SAT is started by simply passing CT control to its main()-method. *  * @see kobold.client.serveradmin.KoboldServerAdministrator * @see kobold.common.controller.IKoboldServerAdministration *  * @author contan */public class ServerAdministrationTool {
    /**     * If set to true this member prevents all logging operations of this     * class ignoring any other options.     */    public static final boolean forceNoLogging = true;    /**     * If set to true a field of randomly changing characters is displayed into      * which echoing of user's input occurs when password masking is needed.     */    public static final boolean useSecurePasswordMasking = false;        /**     * This member is used for logging operations     *      * @see org.apache.commons.logging.LogFactory     * @see org.apache.commons.logging.Log     */    public static final Log logger = forceNoLogging ? null : LogFactory.getLog(ServerAdministrationTool.class);
    /**     * This member is used to administrate Kobold servers. It implements     * IKoboldServerAdministration so that a KoboldServerAdministrator instance     * can be accessed as if it actually was a Kobold server (calls of     * IKoboldServerAdministration-methods are delegated to the actual server).     *      * @see kobold.client.serveradmin.KoboldServerAdministrator     */    private static KoboldServerAdministrator serverAdmin = null;        /**     * This member stores the password for the current Kobold server.     * It is set during the execution of main().     */    private static java.lang.String currentPassword = "";
    /**     * This member stores the current Kobold server's URL.     * It is set during the execution of main().     */    private static java.lang.String currentURL = "https://localhost:23232";
    /**     * the following constant Strings are used to define the syntax     * of commands which can be input by SAT-users.     */    private static final String SAT_CMD_NEW_PRODUCTLINE = "newpl";    private static final String SAT_CMD_INVAL_PRODUCTLINE = "rmpl";    private static final String SAT_CMD_ADD_PLE = "assignple";    private static final String SAT_CMD_INVAL_PLE = "unassignple";    private static final String SAT_CMD_SHOW_CMD_SUMMARY = "help";    private static final String SAT_CMD_ABOUT = "about";    private static final String SAT_CMD_TERMINATE_SAT = "exit";    private static final String SAT_CMD_SET_SERVER = "setserver";    private static final String SAT_CMD_GET_PLE_LIST = "plelist";    private static final String SAT_CMD_GET_PRODUCTLINES = "pllist";    private static final String SAT_CMD_GET_USER_LIST = "userlist";    private static final String SAT_CMD_ADD_USER = "newuser";    private static final String SAT_CMD_REMOVE_USER = "rmuser";    /**     * the following fields are used for user interactions       */    private static NewProductlineInquisitor newProductlineInquisitor =         new NewProductlineInquisitor();    private static NewUserInquisitor newUserInquisitor =         new NewUserInquisitor();    private static SetServerInquisitor setServerInquisitor =         new SetServerInquisitor();
/*--------------------------------------------------------------------------------     The following section containes all controller methods of this class.--------------------------------------------------------------------------------   */        /**     * This method acts as entry point for the Kobold Server Administration      * Tool. The user is asked to provide the URL of the Kobold server to      * administrate and to provide a valid admin-password (see method      * startup()). The provided data is evaluated (see method checkServer())      * and -if this succeeds- the user can proceed administrating the Kobold      * server.     *      * @param args - all passed parameters are ignored!     */    public static void main(String[] args) {        debOut("entering ServerAdministartionTool");        stdOut("\t\t * Kobold Server Administration Tool - v3.2 *\n\n");        // 0.) if cmdarg is set, use it as configfilename        if (args.length > 0) {            System.setProperty("kobold.sat.configFile", args[0]);        }        // 1.) perform startup ops -> see startup()        performStartup();                // 2.) now show the promt until the user quits        while(executePrompt());
        // 3.) Terminate SAT        infOut("\nLeaving SAT...");    } // end main()
    /**     * This method performs the standard startup operations. It is called by     * main() and tries to access the server specified by 'currentURL and      * 'currentPassword.     */    private static void performStartup(){        debOut("performing startup operations");        try {            Properties props = new Properties(System.getProperties());            props.load(new FileInputStream(System.getProperty("kobold.sat.configFile")));            System.setProperties(props);        } catch (Exception e) {            errOut("Could not find client configuration",e);        }        String ret = checkKoboldServer();        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            infOut("Logged on " + currentURL + "\n\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_FAIL)){            infOut("Could reach " + currentURL + " but could not log on.\n" +                   "Check your password.\n\n");        }        else{            infOut("Could not reach " + currentURL + ". The server is either " +                    "down or the newtwork connection \nhas failed or the set " +                   "url is wrong.\n\n");        }                if (useSecurePasswordMasking) {            InquisitorQuestion.secureMasking = true;        }        
        stdOut("Type '" + SAT_CMD_SHOW_CMD_SUMMARY + "' to show a summary of " +               "available commands and to learn more about this program's " +               "purpose.\n");    }//performStartup()
    /**     * Validates the accessibility of the server specified by 'currentURL with      * the password 'currentPassword.     *      * @return IKoboldServerAdministration.RETURN_OK if the server was reachable     *         and accepted the password,IKoboldServerAdministration.RETURN_FAIL     *         if the server was reachable but did not accept the password,     *         IKoboldServerAdministration.SERVER_UNREACHABLE if the server     *         could not even bee reached.     */    private static String checkKoboldServer(){        if (serverAdmin == null){            URL url;            try{                url = new URL (currentURL);            }            catch(Exception e){                /*                  * since 'currentURL obviously contains a bad url, the server                 * is unreachable                 */                return IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE;            }
            serverAdmin = new KoboldServerAdministrator();            serverAdmin.setServer(currentPassword, url);         }                return serverAdmin.checkAdministrability(currentPassword);    } // checkKoboldServer    /*--------------------------------------------------------------------------------    The following section contains all the member functions that execute the    commands that are fetched by executePrompt().-------------------------------------------------------------------------------- */       /**     * This function askes the user to input the name of the productline that     * should loose its PLE and tries to unassign it (by calling 'serverAdmin).      * It is called by executePrompt().     */    private static void removePLE(){        //1.) get the neccessary data        debOut("removePLE(): asking user for details");                //show the pls        if (!getProductlines()) {            return;        }                stdOut("Productline name to unassign a PLE: ");        String plsname = readInput();                //show        getPles(plsname);                stdOut("User to remove from" + plsname + ": ");        String username = readInput();                // 2.) Try to perform the necessary operations on the server        debOut("removePLE(): trying to call server");        try{            String ret = serverAdmin.unassignPle(currentPassword,                                                  plsname,                                                  username);
            // 3.) inform the user about the results            showResults(ret);        }        catch(Exception e){            errOut("A fatal error occured while accessing the server.\n",                   e);        }    }
    /**     * This function askes the user to input the names of the productline that     * should get a new PLE and the name of the user that is to be assigned      * new PLE and tries to assign it (by calling 'serverAdmin). It is called      * by executePrompt().     */    private static void addPLE(){        //1.) get the neccessary data        debOut("addPLE() : asking the user for details");                //show        if (!getProductlines()) {            return;        }                stdOut("Productline to add a PLE: ");        String plsname = readInput();                //show        if (!getRegisteredUsers()) {            return;        }                stdOut("User to add as PLE to " +                plsname + ": ");        String username = readInput();
        // 2.) Try to perform the necessary operations on the server        debOut("trying to assign " + username + " to " + plsname);        try{            String ret = serverAdmin.assignPle(currentPassword,                                                plsname,                                                username);                        // 3.) inform the user about the results            showResults(ret);        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   " to make " + username + " " + plsname + "'s new PLE.",                   e);        }    }        /**     * This function askes the user to input the name of the productline that     * should be invalidated and tries to invalidate it (by calling      * 'serverAdmin). It is called by executePrompt().     */    private static void removeProductline(){        //1.) get the neccessary data        debOut("removeProductline() : asking the user for details");                //show        if (!getProductlines()) {            return;        }                stdOut("Productline name to remove: ");        String plsname = readInput();                // 2.) Try to perform the necessary operations on the server        debOut("Trying to invalidate " + plsname);        try{            String ret = serverAdmin.invalidateProductline(currentPassword,                                                           plsname);            // 3.) inform the user about the results            showResults(ret);        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to remove the productline " + plsname + ".\n", e);            return;        }    }        /**     * This function askes the user to input the name of the new productline      * and tries to create it (by calling 'serverAdmin). It is called by      * executePrompt().     */    private static void createProductline(){        //1.) get the neccessary data        debOut("createProsuctline() : asking user for details");                if (!getProductlines()) {            return;        }        if (!newProductlineInquisitor.performInquisition()) {            // user wants to cancel action -> leave            return;        }                // 2.) Try to perform the necessary operations on the server        debOut("trying to create new productline \"" +                 newProductlineInquisitor.getName() + "\"");        try{            String ret = serverAdmin.newProductline(currentPassword,                     newProductlineInquisitor.getName(),                     newProductlineInquisitor.getResource(),                    newProductlineInquisitor.getRepositoryDescriptor());
            // 3.) inform the user about the results            showResults(ret);        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to create the new productline " +                    newProductlineInquisitor.getName() + ".\n", e);            return;        }    }// end createPoductline()    /**     * The following method lets the user add a new Kobold user to the server.      */    private static void addKoboldUser(){        // 1.) get the necessary data                if (!getRegisteredUsers()) {            return;        }                        if (!newUserInquisitor.performInquisition()) {            // user cancelled action -> abort            return;        }                // 2.) Try to perform the necessary operations on the server        debOut("trying to create new user \"" + newUserInquisitor.getUsername()                 + "\"");        try{            String ret = serverAdmin.addKoboldUser(currentPassword,                     newUserInquisitor.getUsername(),                    newUserInquisitor.getFullName(),                    newUserInquisitor.getPassword());                        // 3.) inform the user about the results            showResults(ret);        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to create the new user " + newUserInquisitor.getUsername() +                    ".\n", e);            return;        }    }        /**     * The following method removes a user from the server. A check is performed     * to see if the specified user is still assigned to any assets. In that     * case the (SAT-)user is warned and asked to explicitely confirm the      * removal.      */    private static void removeKoboldUser(){        // 1.) get the necessary username    	    	if (!getRegisteredUsers()) {            return;        }    	        stdOut("User name: ");        String username = readInput();                // 2.) check for assignements        try{            String ret = serverAdmin.checkUserAssignements(currentPassword,                                                           username);                        if (ret.equals(IKoboldServerAdministration.RETURN_USER_ASSIGNED)){                infOut("User \"" + username + "\" is still assigned to at " +                       "least one asset.\n");                                if (!readYesNo("Would you like to remove it anyway ")){                    return;                }            }            else if (ret.equals(IKoboldServerAdministration.RETURN_USER_NOT_ASSIGNED)){                infOut("User\"" + username +"\" is not assigned to any asset.\n");            }            else {                // an error occured => inform the user and stop                showResults(ret);                return;            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to remove the user " + username + ".\n", e);            return;        }                // 3.) remove the user        try{            String ret = serverAdmin.removeKoboldUser(currentPassword, username);                        // 4.) inform the user about the results            showResults(ret);        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to remove the user " + username + ".\n", e);            return;        }    }        /**     * This method asks the user to provide the name of a registered productline     * and tries to obtain and print a list of all users who are assigned      * maintainers of the specified productline.     *      * @param productlineName Naame of the productline whose ple list should be     *        printed or null if the user should be asked to provide the pl's     *        name     */    private static void getPles(String productlineName){        //1.) get the neccessary data        debOut("getPles() : asking user for details");                String plsname = productlineName;                if (productlineName == null) {        	stdOut("Productline name: ");        	plsname = readInput();        }                // 2.) Try to perform the necessary operations on the server        debOut("trying to get plelist for \"" + plsname + "\"");        try{            String ret = serverAdmin.getPles(currentPassword, plsname);                        // 3.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_FAIL) ||                ret.equals(IKoboldServerAdministration.RETURN_WRONG_PASSWORD) ||                ret.equals(IKoboldServerAdministration.RETURN_PRODUCTLINE_MISSING) ||                ret.equals(IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE)){                showResults(ret);            }            else{                stdOut("PLE list of \"" + plsname + "\":\n" + ret + "\n");            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to get the ple list for " + plsname + ".\n", e);            return;        }            }        /**     * This method tries to get and print a list of the names of all the     * productlines that are currently registered on the server.     *      * @return true, if the operation completed successfully, false otherwise      */    private static boolean getProductlines(){        // 1.) Try to perform the necessary operations on the server        debOut("trying to get productline list from server");        try{            String ret = serverAdmin.getProductlines(currentPassword);                        // 2.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_FAIL) ||                ret.equals(IKoboldServerAdministration.RETURN_WRONG_PASSWORD) ||                ret.equals(IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE)){                showResults(ret);                return false;            }            else{                stdOut("Productline list of \"" + currentURL + "\":\n" + ret +                        "\n");                return true;            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to get the productline list.\n", e);            return false;        }            }        /**     * This method tries to obtain and print a list of all the useres who are     * currently registered on the server.     *      * @return true, if the operation completed successfully, false otherwise     */    private static boolean getRegisteredUsers(){        // 1.) Try to perform the necessary operations on the server        debOut("trying to get userlist from server");        try{            String ret = serverAdmin.getRegisteredUsers(currentPassword);                        // 2.) inform the user about the results            if (ret.equals(IKoboldServerAdministration.RETURN_FAIL) ||                ret.equals(IKoboldServerAdministration.RETURN_WRONG_PASSWORD) ||                ret.equals(IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE)){                showResults(ret);                return false;            }            else{                stdOut("Registered users on \"" + currentURL + "\":\n" + ret +                        "\n");                return true;            }        }        catch(Exception e){            errOut("A fatal error occured while accessing the Kobold server " +                   "to get the userlist.\n", e);            return false;        }                    }        /**     * This function is called by setServer() after the server's url was      * changed by the user to evaluate the new url and to give the user a     * feedback.     */    private static void changeServerUrl(){        // 1.) verify syntactic correctness of 'currentURL        if (!checkUrlCorrectness()){            /*             * 'currentURL contains an invalid url - inform the user and             * quit this method.             */            infOut("Could not change the server's url. \"" + currentURL +                   "\" is not a valid url.\n");            return;        }
        // 2.) check server        serverAdmin = null;        String ret = checkKoboldServer();
        // 3.) inform the user about the results        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            infOut("Url changed to " + currentURL + ". Server was reachable " +                   "and accepted your current password.\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_WRONG_PASSWORD)){            infOut("Url changed to " + currentURL + ". Server was reachable " +                   "but didn't accept your current password.\n");        }        else{            infOut("Url has been changed but no Kobold server responded at " +                   currentURL + ".\n");        }    }//changeServerUrl()       /**     * This function is called by setServer() if the administration password     * was changed by the user to evaluate the new password and to give the     * user a feedback.     */    private static void changePassword(){        // 1.) check server        serverAdmin = null;        String ret = checkKoboldServer();
        // 2.) inform the user about the results        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            infOut("Password changed. Server was reachable and accepted your " +                   "new password.\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_WRONG_PASSWORD)){            infOut("Password changed. Server was reachable but didn't accept " +                   "your current password.\n");        }        else{            infOut("Password has been changed but no Kobold server responded " +                   "at " + currentURL + ".\n");        }    }//changePassword        /*     * This function gives the user an overview of the current server settings     * (url, password) along with the possibility to change them.     */    private static void setServer() {        if (!setServerInquisitor.performInquisition()) {            // user decided to cancel action            return;        }                if (!currentURL.equals(setServerInquisitor.getUrl())) {            // url has changed            currentURL = setServerInquisitor.getUrl();            changeServerUrl();        }                if (!currentPassword.equals(setServerInquisitor.getPassword())) {            // password has changed            currentPassword = setServerInquisitor.getPassword();            changePassword();        }    }    /**     * This method prints a summary of available commands for the SAT on the      * output stream. It is called by provideSATPrompt(), when the user inputs      * 'help'.     */    private static void showCommandSummary(){        // 1.) print program's purpose        stdOut("This program's purpose is to give the administrator of a " +               "Kobold server an lightweight, easy to use\ntool to create/" +               "invalidate productlines, to assign/unsassign productline " +               "engineers\nand to create/remove users.\n\n");
        // 2.) print command summary        stdOut("Available commands:\n\t" +                SAT_CMD_ABOUT + " - shows information about this program's " +                "version and licence\n\t" +               SAT_CMD_ADD_PLE + " - sets a productline's new PLE\n\t" +               SAT_CMD_TERMINATE_SAT + " - terminates the SAT\n\t" +               SAT_CMD_SHOW_CMD_SUMMARY + " - shows this help\n\t" +               SAT_CMD_NEW_PRODUCTLINE + " - creates a new productline\n\t" +               SAT_CMD_ADD_USER + " - creates a new user\n\t" +               SAT_CMD_GET_PRODUCTLINES + " - shows a list of all productlines"+                   " registered on the server\n\t" +               SAT_CMD_GET_PLE_LIST + " - shows a list of all assigned ples " +                   "for a productline\n\t" +               SAT_CMD_INVAL_PRODUCTLINE + " - removes a productline\n\t" +               SAT_CMD_REMOVE_USER + " - removes an user\n\t" +               SAT_CMD_INVAL_PLE + " - removes a productline's PLE\n\t" +               SAT_CMD_SET_SERVER + " - changes the current server " +               "properties\n\t" +               SAT_CMD_GET_USER_LIST + " - shows a list of all registered " +                                       "users on the server\n\n");                // 3.) print current "server stats"
        stdOut("Current administrated Kobold server is at \"" + currentURL +                "\"\n");        String ret = checkKoboldServer();        if (ret.equals(IKoboldServerAdministration.RETURN_OK)){            stdOut("It is reachable and accepting your password.\n");        }        else if (ret.equals(IKoboldServerAdministration.RETURN_WRONG_PASSWORD)){            stdOut("It is reachable but not accepting your password. Please " +                   "change it.\n");        }        else{            stdOut("Unfortunately the server is currently not reachable. " +                   "Please check your url and/or your network connection.\n");        }    }    
    /**     * This methods prints information about this program's version and licence.      */    private static void showAbout(){        stdOut("Kobold Server Administration Tool\n\nVersion 3.2\n\n" +               "Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina " +                "Druckenmueller,\n                   Martin Plies, Michael " +                "Grosse, Necati Aydin,\n                   Oliver Rendgen, " +                "Patrick Schneider, Tammo van Lessen\n\nPermission is hereby " +                "granted, free of charge, to any person obtaining\na copy of " +                "this software and associated documentation files (the " +                "\"Software\"),\nto deal in the Software without " +                "restriction, including without limitation\nthe rights to " +                "use, copy, modify, merge, publish, distribute, sublicense," +                "\nand/or sell copies of the Software, and to permit persons " +                "to whom the\nSoftware is furnished to do so, subject to the " +                "following conditions:\n\nThe above copyright notice and " +                "this permission notice shall be included\nin all copies or " +                "substantial portions of the Software.\n\nTHE SOFTWARE IS " +                "PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS " +                "OR\nIMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF " +                "MERCHANTABILITY,\nFITNESS FOR A PARTICULAR PURPOSE AND " +                "NONINFRINGEMENT. IN NO EVENT SHALL\nTHE AUTHORS OR " +                "COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR " +                "OTHER\nLIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR " +                "OTHERWISE,\nARISING FROM, OUT OF OR IN CONNECTION WITH THE " +                "SOFTWARE OR THE USE OR\nOTHER DEALINGS IN THE SOFTWARE.\n");     }// end showAbout()
    /**     * This method provides the user with a command prompt, if     * the startup ops of the main()-method could be executed     * successfully. The user's input is collected and the according     * methods are called.     *      * @return true, if the user does not wish to exit the SAT, false otherwise     */    private static boolean executePrompt(){        stdOut("\n> ");        String input = readInput();                /*         * Evaluate input         */        if (input == ""){            return true;        }        else if (input.startsWith(SAT_CMD_TERMINATE_SAT)){            return false;        }        else if (input.startsWith(SAT_CMD_SHOW_CMD_SUMMARY)){            showCommandSummary();            return true;        }        else if (input.startsWith(SAT_CMD_ABOUT)){            showAbout();            return true;        }        else if (input.startsWith(SAT_CMD_NEW_PRODUCTLINE)){            createProductline();            return true;        }        else if (input.startsWith(SAT_CMD_INVAL_PRODUCTLINE)){            removeProductline();            return true;        }        else if (input.startsWith(SAT_CMD_ADD_PLE)){            addPLE();            return true;        }        else if (input.startsWith(SAT_CMD_INVAL_PLE)){            removePLE();            return true;        }        else if (input.startsWith(SAT_CMD_SET_SERVER)){            setServer();            return true;        }        else if (input.startsWith(SAT_CMD_GET_PLE_LIST)){            getPles(null);            return true;        }        else if (input.startsWith(SAT_CMD_GET_PRODUCTLINES)){            getProductlines();            return true;        }        else if (input.startsWith(SAT_CMD_GET_USER_LIST)){            getRegisteredUsers();            return true;        }        else if (input.startsWith(SAT_CMD_ADD_USER)){            addKoboldUser();            return true;        }        else if (input.startsWith(SAT_CMD_REMOVE_USER)){            removeKoboldUser();            return true;        }                else{            stdOut("Unknown command or wrong syntax.\n");            return true;        }    }// end executePrompt()
/*--------------------------------------------------------------------------------    The following section contains all the helper functions of this class.-------------------------------------------------------------------------------- */           /**     * This helper method prints a result message according to the passed     * string which must be one of the IKoboldServerAdministration return      * strings.     */    private static void showResults(String result){        if (result.equals(IKoboldServerAdministration.RETURN_OK)){            stdOut("Operation completed successfully.\n");        }        else if (result.equals(IKoboldServerAdministration.RETURN_FAIL)){            stdOut("Operation failed: An unknown error occured on the server.\n");        }        else if (result.equals(IKoboldServerAdministration.RETURN_NAME_ALREADY_REGISTERED)){            stdOut("Operation failed: The name you passed has already been " +                    "registered; please choose another one.\n");        }        else if (result.equals(IKoboldServerAdministration.RETURN_PRODUCTLINE_MISSING)){            stdOut("Operation failed: The productline you specified does not " +                   "exist.\n");        }        else if (result.equals(IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE)){            stdOut("Operation failed: The Kobold server at \"" + currentURL +                    "\" was unreachable.\n");        }        else if (result.equals(IKoboldServerAdministration.RETURN_USER_MISSING)){            stdOut("Operation failed: The user you specified does not exist.\n");        }        else if (result.equals(IKoboldServerAdministration.RETURN_WRONG_PASSWORD)){            stdOut("Operation failed: The server didn't accept your admin " +                    "password.\n");        }        else {            stdOut("Operation failed: The server at \"" + currentURL +                    "\" doesn't seem to be a Kobold server.\n");        }    }        /**     * This helper function reads up to 500 bytes of input data and     * returns the read input as a String.     *      * @return String containing the user's input - empty String if an error      *         occured     */    private static String readInput(){        try        {            byte inp[] = new byte [500];             int read = System.in.read(inp);            byte sinp[] = new byte [read-1];            for (int ax = 0; ax < (read-1); ax++){                sinp[ax] = inp[ax];            }            String ret = new String (sinp);            ret.replaceAll("\r", ""); // to avoid unwanted new lines under MS-Windows            return ret;        }        catch(Exception e)        {            errOut("An error occured while accessing the input stream.\n", e);                        return new String(""); // since op failed return empty string        }            } // readInput()    private static String readSecureInput(String prompt){        kobold.client.serveradmin.tools.MaskingThread maskingThread =             new kobold.client.serveradmin.tools.MaskingThread(prompt);        Thread thread = new Thread(maskingThread);        thread.start();        String input = readInput();        thread.stop();        return input;    }//readSecureInput()
    /**     * This helper method read's a single byte from the input stream as long as      * the user does not input y or n.     * @param question - String that will be printed on the output stream,      *        followed by "(y/n)"     * @return true, if the posed question was answered with yes, false      *         otherwise     */    private static boolean readYesNo(String question) throws Exception{        stdOut(question);            /*         * as long as the user does not input 'y', 'Y', 'n' or 'N' a byte         * is read form the input stream         */        while(true){            stdOut("(y/n)");            String inp = readInput();
            if (inp.charAt(0) == 'y' || inp.charAt(0) == 'Y'){                return true;            }            else if (inp.charAt(0) == 'n' || inp.charAt(0) == 'N'){                return false;            }                   } // end while    } // readYesNo()
    /**     * the following helper function prints the passed error message and     * (error-) logs it along with the passed exception's trace (if e != null).      * A '\n' is inserted between the error message and the exception's trace.     *      * @param message - the custom error message     * @param e - Exception whose trace is to be printed, or null      */    private static void errOut(String message, Exception e){        if (!forceNoLogging){        	System.err.print(message);        	logger.error(message + "\nException thrown: " + e.toString());        }    }// end errOut()
    /**     * This helper function prints and (info-) logs the passed message     * @param message String containing the message to print and log     */    private static void infOut(String message){        stdOut(message);        if (!forceNoLogging){        	logger.info(message);        }    }// end infOut()
    /**     * This helper function logs a debug message     * @param message String containing the message to log     */    private static void debOut(String message){        if (!forceNoLogging){        	logger.debug(message);        }    }// end debOut()
    /**     * This helper fuction prints a String that should be read by the user.     */    private static void stdOut(String message){        System.out.print(message);    }// end stdOut()
    /**     * This helper method checks if 'currentURL can be transformed into an     * URL-object without causing a malformed url exception (that is if      * 'currentURL contains a syntactically correct url).     *      * @return true, if the 'currentURL can be transformed, false otherwise     */    private static boolean checkUrlCorrectness(){        try{            URL url = new URL(currentURL);        }        catch(Exception e){            return false;        }                return true;    } // end checkUrlCorrectness()}// class ServerAdministartionTool