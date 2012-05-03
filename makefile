all: iterativeServer concurrentServer req_client MultiServer

MultiServer:
	javac ./server/MultiServer.java ./server/tools.java

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

