// Copyright (c) 2004 Armin Cont, Anselm R. Garbe, Bettina Druckenmueller,
//                    Martin Plies, Michael Grosse, Necati Aydin,
//                    Oliver Rendgen, Patrick Schneider, Tammo van Lessen
// 
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the "Software"),
// to deal in the Software without restriction, including without limitation
// the rights to use, copy, modify, merge, publish, distribute, sublicense,
// and/or sell copies of the Software, and to permit persons to whom the
// Software is furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
// THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
// ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
// OTHER DEALINGS IN THE SOFTWARE.
// 
// $Id: VCMWrapperInterface.java,v 1.1 2004/03/30 22:45:12 garbeam Exp $

package kobold.client.controller.vcm;

import java.util.List;

import kobold.exception.VCMNotPermitException;
import kobold.exception.VCMIoException;
import kobold.exception.VCMScriptInvocationException;
import kobold.io.FileDescriptor;
import kobold.io.RepositoryDescriptor;
import kobold.io.ScriptDescriptor;
import kobold.data.UserContext;

/**
 * VCMWrapperInterface.
 *
 * Default interface for all VCM actions which are invoked by Kobold
 * or which are delegated by {@link kobold.client.controller.WrappedVCMProvider}.
 *
 * @author garbeam
 */
public interface VCMWrapperInterface {


    //// Standard VCM actions ////

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
     * @throws {@link kobold.exception.VCMNotPermitException} if
     *        the import is not permitted by the given user context.
     * @throws {@link kobold.exception.VCMIoException} if an IO
     *        error occured while trying to import the data.
     * @throws {@link kobold.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void VCMImport(UserContext userContext,
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
     * @throws {@link kobold.exception.VCMNotPermitException} if
     *        the commit is not permitted by the given user context.
     * @throws {@link kobold.exception.VCMIoException} if an IO
     *        error occured while trying to commit the data.
     * @throws {@link kobold.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void VCMCommit(UserContext userContext,
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
     * @throws {@link kobold.exception.VCMIoException} if an IO
     *        error occured while trying to checkout the data.
     * @throws {@link kobold.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void VCMCheckout(FileDescriptor directory,
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
     * @throws {@link kobold.exception.VCMIoException} if an IO
     *        error occured while trying to update the data.
     * @throws {@link kobold.exception.VCMScriptInvocationException} if
     *        a script could not be invoced.
     */
    public void VCMUpdate(FileDescriptor directory,
                            RepositoryDescriptor repository,
                            ScriptDescriptor script)
        throws VCMIOException,
               VCMScriptInvocationException;


    //// Special Kobold VCM actions. ////


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
     * @throws {@link kobold.exception.VCMIoException} if an IO
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
     * @throws {@link kobold.exception.VCMIoException} if an IO
     *        error occured while trying to get meta information.
     */
    public List getDeltaMetaInfo(List knownMetaInfo,
                                 RepositoryDescriptor repository)
        throws VCMIOException;

}
