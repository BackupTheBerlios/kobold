#!/bin/sh
#

echo "Creating private/public key pair ..."

keytool -genkey \
        -dname "cn=localhost, ou=Kobold, o=Werkbold, c=DE" \
        -alias koboldbase \
        -keypass kobold1 \
        -keystore keystore \
        -storepass kobold1 \
        -validity 123

echo "Creating certificate ..."

keytool -export \
        -alias koboldbase \
        -keystore keystore \
        -keypass kobold1 \
        -storepass kobold1 \
        -rfc \
        -file koboldbase.cer

echo "Import cert into truststore ..."

keytool -import \
        -alias koboldbase \
        -file koboldbase.cer \
        -keystore truststore \
        -storepass kobold1
