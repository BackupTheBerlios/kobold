/*
 * Copyright (c) 2003 - 2004 Necati Aydin, Armin Cont, 
 * Bettina Druckenmueller, Anselm Garbe, Michael Grosse, 
 * Tammo van Lessen,  Martin Plies, Oliver Rendgen, Patrick Schneider
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in 
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 *
 * $Id: WrapperInterface.java,v 1.3 2004/06/24 09:58:59 grosseml Exp $
 *
 */
package kobold.client.vcm.controller;

import java.util.List;

import kobold.common.data.UserContext;
import kobold.common.exception.VCMIOException;
import kobold.common.exception.VCMNotPermitException;
import kobold.common.exception.VCMScriptInvocationException;
import kobold.common.io.FileDescriptor;
import kobold.common.io.RepositoryDescriptor;
import kobold.common.io.ScriptDescriptor;

/**
 * WrapperInterface.
 *
 * Default interface for all  actions which are invoked by Kobold
 * or which are delegated by {@link kobold.client.controller.WrappedProvider}.
 *
 * @author garbeam
 */
public interface WrapperInterface {


    //// Standard  actions ////

    /**
     * Assigns Eclipse to import all data of the given working copy
     * path.
     *
     * @param userContext the user context of this action to perform.
     * @param directory path to the working copy which sould be
     *        imported.
     * @param repository the repository to use.
     * @param script the script descriptor for special script
     *        invocation.
     * @throws {@link kobold.util.exception.VCMNotPermitException} if
     *        the import is not permitted by the given user context.
     * @throws {@link kobold.util.exception.VCMIOException} if an IO
     *        error occured while trying to import the data.
     * @throws {@link kobold.util.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void imports(UserContext userContext,
                          FileDescriptor directory,
                          RepositoryDescriptor repository,
                          ScriptDescriptor script)
        throws VCMNotPermitException,
               VCMIOException,
               VCMScriptInvocationException;


    /**
     * Assigns Eclipse to commit all data of the given working copy
     * path.
     *
     * @param userContext the user context of this action to perform.
     * @param directory path to the working copy which sould be
     *        committed.
     * @param repository the repository to use.
     * @param script the script descriptor for special script
     *        invocation.
     * @throws {@link kobold.util.exception.VCMNotPermitException} if
     *        the commit is not permitted by the given user context.
     * @throws {@link kobold.util.exception.VCMIOException} if an IO
     *        error occured while trying to commit the data.
     * @throws {@link kobold.util.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void commit(UserContext userContext,
                          FileDescriptor directory,
                          RepositoryDescriptor repository,
                          ScriptDescriptor script)
        throws VCMNotPermitException,
               VCMIOException,
               VCMScriptInvocationException;


    /**
     * Assigns Eclipse to checkout all data of the given working copy
     * path.
     *
     * @param directory path to the working copy which sould be
     *        used.
     * @param repository the repository with module information to
     *        checkout from.
     * @param script the script descriptor for special script
     *        invocation.
     * @throws {@link kobold.util.exception.VCMIOException} if an IO
     *        error occured while trying to checkout the data.
     * @throws {@link kobold.util.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void checkout(FileDescriptor directory,
                            RepositoryDescriptor repository,
                            ScriptDescriptor script)
        throws VCMIOException,
               VCMScriptInvocationException;


    /**
     * Assigns Eclipse to update all data of the given working copy
     * path.
     *
     * @param directory path to the working copy which sould be
     *        used.
     * @param repository the repository with module information to
     *        updated from.
     * @param script the script descriptor for special script
     *        invocation.
     * @throws {@link kobold.util.exception.VCMIOException} if an IO
     *        error occured while trying to update the data.
     * @throws {@link kobold.util.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void update(FileDescriptor directory,
                            RepositoryDescriptor repository,
                            ScriptDescriptor script)
        throws VCMIOException,
               VCMScriptInvocationException;


    //// Special Kobold  actions. ////


    /**
     * @returns {@link java.util.List} meta information about the given
     * repository descriptor. Assigns Eclipse to return appropriate
     * information.
     * Note: If you need meta information only about a special file,
     *       you've to limit the repository descriptor to the specific
     *       file you're interested in.
     *
     * @param repository the repository with module information to
     *        get meta info from from.
     * @throws {@link kobold.util.exception.VCMIOException} if an IO
     *        error occured while trying to get meta information.
     */
    public List getMetaInfo(RepositoryDescriptor repository)
        throws VCMIOException;


    /**
     * @returns {@link java.util.List} delta of meta information about the
     * given input meta information and the given repository descriptor.
     * Assigns Eclipse to return appropriate information and generates
     * the delta afterwards.
     * Note: If you need delta meta information only about a special file,
     *       you've to limit the repository descriptor and your known
     *       meta information to the specific file you're interested in.
     *
     * @param knownMetaInfo {@link java.util.List} of known meta
     *        information about the repository.
     * @param repository the repository with module information to
     *        get delta meta info from from.
     * @throws {@link kobold.util.exception.VCMIOException} if an IO
     *        error occured while trying to get meta information.
     */
    public List getDeltaMetaInfo(List knownMetaInfo,
                                 RepositoryDescriptor repository)
        throws VCMIOException;

}
