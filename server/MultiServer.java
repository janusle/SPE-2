package server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.server.UID;
import tools.*;

class NoSuchClientException extends Exception{};


public class MultiServer{

    private final int PORT = 40302;
    private final int DELAY = 60000;
    private ArrayList<clientInfo> addresses = new ArrayList<clientInfo>();
    private Timer timer = null;

    public static void main(String []args){

        new MultiServer().run(); //runing server;
         
    }
    
    /* 
     * inner class sender for send time to the first client
     */
    class Sender extends TimerTask{

            public void run() {
          
            if( addresses.size() > 0 ){ //There is subscriber 
              
              clientInfo ci;
              InetAddress ip;
              int port;
              TimeInfo timeinfo = new TimeInfo( addresses );
    
              for(int i=0; i<addresses.size(); i++ ){

                    try{

                        ci = addresses.get(i); // get client
                        ip = ci.getAddress(); // get ip of client
                        port = ci.getPort(); // get port of cilent
                        Socket s = new Socket( ip, port );
                        ObjectOutputStream out = new ObjectOutputStream( s.getOutputStream() );
                        out.writeObject( timeinfo ); //send time infomation
                        out.close();
                        s.close();
                        System.out.println("Time is sent to " + i + "th(st/nd) client " + ip.getHostAddress() + ":" + port +"\n");
                        return;
                    }
                    catch(Exception e)
                    {  
                        System.err.println("Fail to send time to " + i + "th(st/nd) client" ); 
                      

                        if( timeinfo != null && timeinfo.point + 1 < timeinfo.addresses.size() )
                           timeinfo.point++; //incrument client point 

                        continue;
                    }

               }
               System.err.println("Failed to send time to client, will resend it one minute after");

            }
            else{ // no subscription

                System.out.println("No subscription, will try one minute after");

            }
       
            System.out.println("");
        }
    } 
    // end of inner class Sender 

    /*
     * get index in client list by client id
     */
    private int getClientIndex( String id ) throws NoSuchClientException{
         
        for(int i=0; i<addresses.size(); i++ ){

            if ( id.equals( addresses.get(i).getId() ) )
              return i;
        }
      
        throw new NoSuchClientException(); 

    }

    public void run(){

          try{
           
             ServerSocket ss = new ServerSocket(PORT);
             System.out.println("Server is started...");

             while(true){
  
               Socket cs =  ss.accept();
               BufferedReader in = new BufferedReader( new InputStreamReader( cs.getInputStream() ) );
               PrintWriter out = new PrintWriter( cs.getOutputStream(), true );
               String line;
               int index; 

               if ( ( line = in.readLine() ) != null ){

                   if( line.equals("subscribe") ){ // client ask for subscription

                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();


                       String id = new UID().toString(); //generate unique client id

                       clientInfo ci = new clientInfo( ia, port, id);
              
                           
                       addresses.add( ci ); // add the client info to list
                       out.println( id );//confirm subscription

                       System.out.println("New subscriber from " + ia.getHostAddress() + ":" + port +
                                              " client id " + id );
                           
                       if ( this.timer == null ){
                              timer = new Timer();
                              timer.schedule( new Sender(), DELAY, DELAY );
                              System.out.println("Timer is set");
                       }


                   }
                   else if ( line.indexOf("unsubscribe") != -1 ){ //client ask for unsubscription
                       
                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();
                       String []tokens = line.split("  ");  //split by two spaces
                       String id = null; 

                       if( tokens.length == 2 ){ //length should be two, the first is "unsubscribe" and the 2nd is its id
                        
                           try{
                              id = tokens[1]; //get client id

                              index = getClientIndex( id ); //get index of client in client list

                              addresses.remove( index ); //remove client from list

                              out.println("OK"); //confirmation

                              System.out.println( index + "th(st/nd) Client is removed" );
                           }
                           catch( NoSuchClientException nsce ){

                              System.err.println("Invalid client id, request from " + ia.getHostAddress() + ":" + port );

                           }
                           catch( IndexOutOfBoundsException iooe ){

                              System.err.println("Arraylist error:" + iooe.getMessage() );

                           }

                       }
                       else{
                              System.err.println("Invalid unsubscribe from " + ia.getHostAddress() + ":" + port );

                       }
                   }
                   else{ // invalid request 
                     
                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();
                       System.err.println("Invalid request from " + ia.getHostAddress() + ":" + port );

                   }
               }
          
               in.close();
               out.close();
               cs.close();
               
             }

          }
          catch( IOException e){
  
              System.err.println("Server error: " + e.getMessage() );
              System.exit(-1);
  
          }
  
    }



    

}
