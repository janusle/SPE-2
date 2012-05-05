#!/bin/bash

if [ $# == 0 ];then
 
  echo "Invalid argument"
  exit

fi

if [ $1 == ir ];then
  java -Djavax.net.ssl.keyStore=mySrvKeystore -Djavax.net.ssl.keyStorePassword=123456 server.iterativeServer
elif [ $1 == cc ];then
  java -Djavax.net.ssl.keyStore=mySrvKeystore -Djavax.net.ssl.keyStorePassword=123456 server.concurrentServer
elif [ $1 == multi ];then
  java server.MultiServer
else
  echo "Invalid argument"
fi
