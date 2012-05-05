package client;
import java.io.*;
import java.net.*;
import java.util.*;


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

        //PrintWriter out = new Printwriter( csocket.getOutputStream(), true);
        ObjectInputStream in = new ObjectInputStream( s.getInputStream() );
        this.timeinfo = (TimeInfo)in.readObject();
        System.out.println( this.timeinfo.time );
        this.timeinfo.point++;  
       
        in.close();
        s.close();


    }

    public void run(){

      try{

         this.subscribe();
         while(true){

           this.getTime(); 

         }

      }catch(Exception e){

            System.err.println("Client error: " + e.getMessage() );
            System.exit(-1); 

      }



    }

}
