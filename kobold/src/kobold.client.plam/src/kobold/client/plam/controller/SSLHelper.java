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
 * $Id: SSLHelper.java,v 1.2 2004/08/03 13:00:39 garbeam Exp $
 *
 */
package kobold.client.plam.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import kobold.client.plam.KoboldPLAMPlugin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Provides static helper functions to manage SSL keystore and certificates.
 * @author garbeam
 */
public class SSLHelper
{
    public static final Log logger = LogFactory.getLog(SSLHelper.class);
    public static final String PROTOKOL_HANDLER = "java.protocol.handler.pkgs";
    public static final String JAVA_DEBUG = "javax.net.debug";
    public static final String KEY_STORE = "javax.net.ssl.keyStore";
    public static final String KEY_STORE_PASSWORD = "javax.net.ssl.keyStorePassword";
    public static final String TRUST_STORE = "javax.net.ssl.trustStore";
    public static final String TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";

    public static KeyStore getKeyStore() {
        IPreferenceStore preferenceStore = KoboldPLAMPlugin.getDefault().getPreferenceStore();
        KeyStore result = null;
        try {
            result = KeyStore.getInstance("JKS");
                result.load(new FileInputStream(preferenceStore.getString(SSLHelper.KEY_STORE)),
                            preferenceStore.getString(SSLHelper.KEY_STORE_PASSWORD).toCharArray());
        } catch (KeyStoreException e) {
            logger.fatal("Key store cannot be instantiated", e);
        } catch (NoSuchAlgorithmException e) {
            logger.fatal("Key store algorithm not supported", e);
        } catch (CertificateException e) {
            logger.fatal("Key store certificate corrupted", e);
        } catch (FileNotFoundException e) {
            logger.fatal("Key store file not found", e);
        } catch (IOException e) {
            logger.fatal("Key store I/O error", e);
        }

        return result;
    }
    
    public static Certificate getCertificateForPlainText(String plainText) {
        
        BufferedInputStream bis = new BufferedInputStream(
                new ByteArrayInputStream(plainText.getBytes()));

        CertificateFactory cf;
        Certificate cert = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                cert = cf.generateCertificate(bis);
            }

        } catch (CertificateException e) {
            logger.error("Cannot generate X.509 certificate", e);
        } catch (IOException e) {
            logger.error("Cannot read plain text certificate", e);
        }
        
        return cert;
    }
    
}
