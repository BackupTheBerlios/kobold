#!/bin/sh
#
# You've to ask Anselm to get the valid certificate for using the Kobold server.

echo "Creating private/public key pair ..."

keytool -genkey \
        -dname "cn=<hostname is private>, ou=Kobold, o=Werkbold, c=DE" \
        -alias koboldbase \
        -keypass <password> \
        -keystore keystore \
        -storepass <password> \
        -validity 60                

echo "Creating certificate ..."

keytool -export \
        -alias koboldbase \
        -keystore keystore \
        -keypass <password> \
        -storepass <password> \
        -rfc \
        -file koboldbase.cer

echo "Import cert into truststore ..."

keytool -import \
        -alias koboldbase \
        -file koboldbase.cer \
        -keystore truststore \
        -storepass <password>
