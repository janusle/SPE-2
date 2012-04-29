all: iterativeServer concurrentServer client

iterativeServer:
	javac iterativeServer.java

concurrentServer:
	javac concurrentServer.java

client:
	javac client.java

clean:
	-rm *.class


