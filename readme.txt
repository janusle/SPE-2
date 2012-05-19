1. Compile the program

   make

2. Run the program
 
2.1 run program of part 1.1 

   # generate key
   ./genkey.sh  
   # the password should be 123456 

   # run iterative server
   ./runserver.sh iterative
   
   # run client; The script will run client 20 times 
   ./runclient.sh iterative 
   
   # Note: The creation of SSL connection is very slow so it takes quite a while to get the time.

2.2 run program of part 1.2

   # generate key
   ./genkey.sh
   # the password should be 123456 

   # run concurrent server
   ./runserver.sh concurrent
   
   # run client; The script will run client 20 times
   ./runclient.sh concurrent
   
   # Note: The creation of SSL connection is very slow so it takes quite a while to get the time.

2.3 run program of part 2.1

   # run server
   ./runserver.sh multi

   # run client; You can run the script several times 
   # When cilent starts, it will send a request to subscribe time immediately 
   ./runclient.sh multi

   # Unsubscribe the time
   Just type 'unssb' command. The client will send unsubscription request to the server.


3. Clean the *.class files

   make clean



Note: Please run all the servers on yallara becuase client assumes the time servers are on yallara
