all: iterativeServer concurrentServer req_client

iterativeServer:
	javac ./server/iterativeServer.java

concurrentServer:
	javac ./server/concurrentServer.java

req_client:
	javac ./client/client.java

genkey:
	./genkey.sh

clean:
	-rm ./server/*.class
	-rm ./client/*.class

