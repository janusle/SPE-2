package client;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import tools.*;

public class MultiClient{


    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 40302;
    private ServerSocket ss = null;
    private int port;
    private TimeInfo timeinfo;
    private int clientId = -1;

    public static void main(String []args){


        new MultiClient().run();

    }


    private void unsubscribe() throws Exception{

        if ( clientId == -1 ){
     
            System.err.println("You haven't subscribe time yet!");
            return;

        }

        Socket csocket = new Socket( SERVER_HOST, SERVER_PORT );
      
        PrintWriter out = new PrintWriter( csocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader( csocket.getInputStream() ) );

        out.println("unsubscribe:" + clientId);

        String line;
        if( ( line = in.readLine() ) != null &&
              line.equals("OK") ){
             
            out.close();
            in.close();
            csocket.close();
          
            System.out.println("Time unsubscribed");

        }else{

            System.err.println("Failed to unsubscribe");
        }

    }


    private void subscribe() throws Exception{

            
           Socket csocket = new Socket( SERVER_HOST, SERVER_PORT );
           this.port = csocket.getLocalPort(); //get local port, it will be used in the future 

           PrintWriter out = new PrintWriter( csocket.getOutputStream(), true);
           BufferedReader in = new BufferedReader( new InputStreamReader( csocket.getInputStream() ) );

           out.println("subscribe");

           String line;
           if( ( line = in.readLine() ) != null ){
                  
                    clientId = Integer.parseInt( line ); //get client id

                    out.close();
                    in.close();
                    csocket.close();
                    
                    this.ss = new ServerSocket( this.port );
                    System.out.println("Time subscribed, client id is " + clientId);

 
           }else{

                    throw new Exception("Failed to subscribe");
           }

    }


    class SocketProcess implements Runnable{


        private void getTime() throws Exception{
    
            Socket s = ss.accept();
    
            ObjectInputStream in = new ObjectInputStream( s.getInputStream() );
            timeinfo = (TimeInfo)in.readObject();
            System.out.println( timeinfo.time );
            
            in.close();
            s.close();
    
    
        }
    
        public void cast(){
             
              if( timeinfo.point + 1 < timeinfo.addresses.size() )
                   timeinfo.point++;  
              else
                   timeinfo.point = -1; // no more client
    
    
              if( timeinfo.addresses.size() > 0 && timeinfo.point != -1 ){ 
           
                  clientInfo ci;
                  InetAddress ip;
                  int port;
    
                  
                  for(int i=timeinfo.point; i<timeinfo.addresses.size(); i++ ){
    
                        try{
    
                            ci = timeinfo.addresses.get(i); // get first client
                            ip = ci.getAddress();
                            port = ci.getPort();
                            Socket s = new Socket( ip, port );
                            ObjectOutputStream out = new ObjectOutputStream( s.getOutputStream() );
                            out.writeObject( timeinfo );
                            out.close();
                            s.close();
                            System.out.println("Time is sent to client " +  i  + " " + ip.getHostAddress() + ":" + port + "\n");
                            return;
                        }
                        catch(Exception e)
                        {  
                            System.err.println("Fail to send time to client " + i ); 
    
                            if( timeinfo.point + 1 < timeinfo.addresses.size() )
                               timeinfo.point++;
    
                            continue;
                        }
    
                   }
                   System.err.println("Failed to send time to clients, will resend after");
    
                }
                else{ // no subscription
    
                    if( timeinfo.point == -1 ) 
                      System.out.println("I am last client");
    
                }
                System.out.println("");
    
    
        }
    
        public void run(){
          
          try{
             
             System.out.println("I am start...");
    
             while(true){
    
               this.getTime(); 
               this.cast(); 
    
             }
    
          }catch(Exception e){
    
                System.err.println("Client error: " + e.getMessage() );
                System.exit(-1); 
    
          }
    
    
        }
    
    }
    
    public void run(){

      try{

         /*
         InputStream in = System.in;
         FileInputStream fi = (FileInputStream) in;
         FileChannel fc = fi.getChannel(); 
         */

         this.subscribe();
         Thread t = new Thread( new SocketProcess() );  
         t.start();
          
         while(true){

           Console cons;
           String cmd;
           if( ( cons = System.console() ) != null &&
                 ( cmd = cons.readLine() ) != null  ){

               if ( cmd.equals("ussb") )
                 this.unsubscribe();
               else{

                 System.out.println("Unknown cmd");
               }

           }
           //this.getTime(); 
           //this.cast(); 

         }

      }catch(Exception e){

            System.err.println("Client error: " + e.getMessage() );
            System.exit(-1); 

      }



    }

}
