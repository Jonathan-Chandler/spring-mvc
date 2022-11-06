#!/bin/bash

PASSWORD=guest123

#BASE_DIR=/home/jonathan/work/java/rabbitmq-config/testkey
BASE_DIR=/home
SERVER_DIR=$BASE_DIR/server
CLIENT_DIR=$BASE_DIR/client
TEST_CA_DIR=$BASE_DIR/testca
mkdir -p $CLIENT_DIR
mkdir -p $BASE_DIR/private
mkdir -p $TEST_CA_DIR/certs

echo 01 > $TEST_CA_DIR/serial
touch $TEST_CA_DIR/index.txt

set -eu

echo "test dir"
#
# Prepare the certificate authority (self-signed).
#
cd $TEST_CA_DIR

# Create a self-signed certificate that will serve a certificate authority (CA).
# The private key is located under "private".
openssl req -x509 -config $BASE_DIR/openssl.cnf -newkey rsa:2048 -days 365 -out cacert.pem -outform PEM -subj /CN=MyTestCA/ -nodes

# Encode our certificate with DER.
openssl x509 -in cacert.pem -out cacert.cer -outform DER


echo "server dir"

#
# Prepare the server's stuff.
#
cd $SERVER_DIR

# Generate a private RSA key.
openssl genrsa -out key.pem 2048

# Generate a certificate from our private key.
openssl req -new -key key.pem -out req.pem -outform PEM -subj /CN=$(hostname)/O=server/ -nodes

echo "test dir"
# Sign the certificate with our CA.
cd $TEST_CA_DIR
openssl ca -config $BASE_DIR/openssl.cnf -in $SERVER_DIR/req.pem -out $SERVER_DIR/cert.pem -notext -batch -extensions server_ca_extensions

echo "server dir"
# Create a key store that will contain our certificate.
cd $SERVER_DIR
openssl pkcs12 -export -out keycert.p12 -in cert.pem -inkey key.pem -passout pass:$PASSWORD

echo "client"
cd $CLIENT_DIR
# Generate a private RSA key.
openssl genrsa -out key.pem 2048

# Generate a certificate from our private key.
openssl req -new -key key.pem -out req.pem -outform PEM -subj /CN=$(hostname)/O=client/ -nodes

echo "test dir"
# Sign the certificate with our CA.
cd $TEST_CA_DIR
openssl ca -config $BASE_DIR/openssl.cnf -in $CLIENT_DIR/req.pem -out $CLIENT_DIR/cert.pem -notext -batch -extensions client_ca_extensions

echo "client"
# Create a key store that will contain our certificate.
cd $CLIENT_DIR
openssl pkcs12 -export -out key-store.p12 -in cert.pem -inkey key.pem -passout pass:$PASSWORD

# Create a trust store that will contain the certificate of our CA.
openssl pkcs12 -export -out trust-store.p12 -in $TEST_CA_DIR/cacert.pem -inkey $BASE_DIR/private/cakey.pem -passout pass:$PASSWORD

echo "server"
ls -al $SERVER_DIR
echo "client"
ls -al $CLIENT_DIR
echo "private"
ls -al $BASE_DIR/private
echo "testca"
ls -al $TEST_CA_DIR
