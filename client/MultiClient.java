package client;
import java.io.*;
import java.net.*;
import java.util.*;
import tools.*;

public class MultiClient{


    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 40302;
    private ServerSocket ss = null;
    private int port;
    private TimeInfo timeinfo;

    public static void main(String []args){


        new MultiClient().run();

    }

    private void subscribe() throws Exception{

            
           Socket csocket = new Socket( SERVER_HOST, SERVER_PORT );
           this.port = csocket.getLocalPort(); //get local port, it will be used in the future 

           PrintWriter out = new PrintWriter( csocket.getOutputStream(), true);
           BufferedReader in = new BufferedReader( new InputStreamReader( csocket.getInputStream() ) );

           out.println("subscribe");

           String line;
           if( ( line = in.readLine() ) != null &&
                 line.equals("OK") ){
             

                    out.close();
                    in.close();
                    csocket.close();
                    
                    this.ss = new ServerSocket( this.port );
                    System.out.println("Time subscribed");

 
           }else{

                    throw new Exception("Failed to subscribe");
           }

    }

    private void getTime() throws Exception{

        Socket s = this.ss.accept();

        ObjectInputStream in = new ObjectInputStream( s.getInputStream() );
        this.timeinfo = (TimeInfo)in.readObject();
        System.out.println( this.timeinfo.time );
        
        in.close();
        s.close();


    }

    public void cast(){
         
          if( this.timeinfo.point + 1 < this.timeinfo.addresses.size() )
               this.timeinfo.point++;  
          else
               this.timeinfo.point = -1; // no more client


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

         this.subscribe();
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
