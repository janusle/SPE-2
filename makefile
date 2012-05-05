all: iterativeServer concurrentServer req_client MultiServer MultiClient

MultiServer:
	javac ./server/MultiServer.java ./tools/tools.java

MultiClient:
	javac ./client/MultiClient.java ./tools/tools.java

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
