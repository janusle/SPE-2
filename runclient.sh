#!/bin/bash


for i in {1..20}
do
   java -Djavax.net.ssl.trustStore=mySrvKeystore client.client&
done

