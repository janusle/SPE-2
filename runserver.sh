#!/bin/bash

if [ $# == 0 ];then
 
  echo "Invalid argument"
  exit

fi

if [ $1 == ir ];then
  java -Djavax.net.ssl.keyStore=mySrvKeystore -Djavax.net.ssl.keyStorePassword=123456 iterative_server
elif [ $1 == cc ];then
  java -Djavax.net.ssl.keyStore=mySrvKeystore -Djavax.net.ssl.keyStorePassword=123456 concurrentServer
else
  echo "Invalid argument"
fi
