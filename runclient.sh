#!/bin/bash


if [ $# == 0 ];then

    echo "Invalid argument"
    exit

fi

if [ $1 == multi ];then
    java -cp tools/* client.MultiClient 
else
    for i in {1..20}
    do
      java -Djavax.net.ssl.trustStore=mySrvKeystore client.client&
    done
fi
