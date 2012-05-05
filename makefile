JC = javac

all: iterativeServer concurrentServer req_client MultiServer MultiClient 

tools:
	$(JC) ./tools/clientInfo.java 
	$(JC) ./tools/TimeInfo.java

MultiServer: tools
	$(JC) ./server/MultiServer.java ./tools/clientInfo.java

MultiClient: tools
	$(JC) ./client/MultiClient.java ./tools/clientInfo.java ./tools/TimeInfo.java

iterativeServer:
	$(JC) ./server/iterativeServer.java

concurrentServer:
	$(JC) ./server/concurrentServer.java

req_client:
	$(JC) ./client/client.java


genkey:
	./genkey.sh

clean:
	-rm ./server/*.class
	-rm ./client/*.class
	-rm ./tools/*.class
