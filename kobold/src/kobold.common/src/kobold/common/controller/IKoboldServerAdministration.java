﻿/* * Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller, *                    Martin Plies, Michael Grosse, Necati Aydin, *                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen *  * Permission is hereby granted, free of charge, to any person obtaining a * copy of this software and associated documentation files (the "Software"), * to deal in the Software without restriction, including without limitation * the rights to use, copy, modify, merge, publish, distribute, sublicense, * and/or sell copies of the Software, and to permit persons to whom the * Software is furnished to do so, subject to the following conditions: *  * The above copyright notice and this permission notice shall be included * in all copies or substantial portions of the Software. *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR * OTHER DEALINGS IN THE SOFTWARE. */package kobold.common.controller;import kobold.common.io.RepositoryDescriptor;/** * Through this interface Kobold servers are accessed (on administration-level) * to create new (invalidate existing) productlines and to assign/unassign users  * to/from them. *   * @author contan */public interface IKoboldServerAdministration {        /**     * this method is used to check if the called Kobold server is reachable      * and can be administrated with the passed password     *       * @param adminPassword the Kobold server's administration password     *      * @return IKoboldServerAdministration.RETURN_OK, if the check was      *         successful, IKoboldServerAdministration.RETURN_FAIL if an error     *         occured while executing the method on the server,      *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the     *         method could not be called on the server.      */    public String checkAdministrability(String adminPassword);        /**     * call this method if you want to create a new productline on the Kobold     * server. Note that if there already exists a productline with the name     * 'nameOfProductline, the method will exit with an error.      *      * @param adminPassword the Kobold server's administration password     * @param nameOfProductline the new productline's desired name     * @param repositoryDescriptor description of the repository for the new     *                             productline     *      * @return IKoboldServerAdministration.RETURN_OK, if the check was      *         successful, IKoboldServerAdministration.RETURN_FAIL if an error     *         occured while executing the method on the server,      *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the     *         method could not be called on the server.      */    public String newProductline(String adminPassword,                                  String nameOfProductline,                                  RepositoryDescriptor repositoryDescriptor);        /**     * to assure data consistency of products that are realted to a certain     * productline, productlines cannot be entirely deleted. nonetheless this      * method provides you with the possibility to mark an existing productline     * as invalid.      *       * @param adminPassword the Kobold server's administration password     * @param nameOfProductline name of the productline that is to be      *                          invalidated     *      * @return IKoboldServerAdministration.RETURN_OK, if the check was      *         successful, IKoboldServerAdministration.RETURN_FAIL if an error     *         occured while executing the method on the server,      *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the     *         method could not be called on the server.      */    public String invalidateProductline(String adminPassword,                                         String nameOfProductline);         /**     * This method assigns a user to a productline (users that are assigned     * directly to productlines act as the productline's ple). Possible already     * assigned user will be automatically unassigned.     *      * @param adminPassword the Kobold server's administration password     * @param nameOfProductline name of the productline that should be assigned     *                          a new PLE     * @param nameOfUser name of the user that should be assigned to the      *                   specified productline     *      * @return IKoboldServerAdministration.RETURN_OK, if the check was      *         successful, IKoboldServerAdministration.RETURN_FAIL if an error     *         occured while executing the method on the server,      *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the     *         method could not be called on the server.      */    public String assignPle(String adminPassword,                            String nameOfProductline,                            String nameOfUser);        /**     * This methods unassigns a user (ple) from a productline. Since there can     * only be one assigned ple per productline at the same time, the specified     * productline will have no more assigned ple after a successful call of     * this method.     *      * @param adminPassword the Kobold server's administration password     * @param nameOfProductline name of the productline that should "loose"     *                          the specified user     * @param nameOfUser username specifiing which user should be unassigned      *      * @return IKoboldServerAdministration.RETURN_OK, if the check was      *         successful, IKoboldServerAdministration.RETURN_FAIL if an error     *         occured while executing the method on the server,      *         IKoboldServerAdministration.RETURN_SERVER_UNREACHABLE if the     *         method could not be called on the server.      */    public String unassignPle(String adminPassword, String nameOfProductline);        /**     * The following String will be returned by IKoboldServerAdministration-     * methods, if the called method completed without an error.     */    public static final String RETURN_OK = "OK";        /**     * The following String will be returned by IKoboldServerAdministration-     * methods, if the called method completed with an error.     */    public static final String RETURN_FAIL = "FAIL";        /**     * The following String will be returned by IKoboldServerAdministration-     * methods, if the corresponding method on the "real" Kobold server could      * not be executed (e.g. fail of RP call).     */    public static final String RETURN_SERVER_UNREACHABLE = "SERVERNOTREACHABLE";}
