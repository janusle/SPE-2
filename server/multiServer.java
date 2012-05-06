package server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.server.UID;
//import java.text.SimpleDateFormat;
import tools.*;

class NoSuchClientException extends Exception{};


public class MultiServer{

    private final int PORT = 40302;
    private ArrayList<clientInfo> addresses = new ArrayList<clientInfo>();
    private Timer timer = null;

    public static void main(String []args){

        new MultiServer().run(); //runing server;
         
    }

    class Sender extends TimerTask{
        
        /*
        private String getCurrentTime(){

            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
            return formatter.format( currentDate.getTime() );

        }
        */

        public void run() {
          
            if( addresses.size() > 0 ){ 
              
              clientInfo ci;
              InetAddress ip;
              int port;
              TimeInfo timeinfo = new TimeInfo( addresses );
    
              for(int i=0; i<addresses.size(); i++ ){

                    try{

                        ci = addresses.get(i); // get first client
                        ip = ci.getAddress();
                        port = ci.getPort();
                        Socket s = new Socket( ip, port );
                        ObjectOutputStream out = new ObjectOutputStream( s.getOutputStream() );
                        //String time = this.getCurrentTime();               
                        //timeinfo.time = time;
                        out.writeObject( timeinfo );
                        out.close();
                        s.close();
                        System.out.println("Time is sent to client " + i + " " + ip.getHostAddress() + ":" + port +"\n");
                        System.out.println( timeinfo.point );
                        return;
                    }
                    catch(Exception e)
                    {  
                        System.err.println("Fail to send time to client " + i ); 
                      

                        if( timeinfo != null && timeinfo.point + 1 < timeinfo.addresses.size() )
                           timeinfo.point++;

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

                   if( line.equals("subscribe") ){

                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();


                       String id = new UID().toString();

                       clientInfo ci = new clientInfo( ia, port, id);
                       /*if ( ( index = addresses.indexOf( ci ) ) == -1 ){*/
              
                           
                           addresses.add( ci );
                           out.println( id );//confirm subscription

                           System.out.println("New subscriber from " + ia.getHostAddress() + ":" + port +
                                              " client id " + id );
                           
                           if ( this.timer == null ){
                              timer = new Timer();
                              timer.schedule( new Sender(), 5 * 1000, 5 * 1000 );
                              System.out.println("Timer is set");
                           }

                       /*
                       }
                       else{ //duplicated subscribe
                           System.err.println("subscriber " + ia.getHostAddress() + " is already in the list");

                       }
                       */

                   }
                   else if ( line.indexOf("unsubscribe") != -1 ){
                       
                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();
                       String []tokens = line.split(":"); 
                       String id = null; 

                       if( tokens.length == 2 ){
                        
                           try{
                              id = tokens[1]; //get client id

                              index = getClientIndex( id );

                              addresses.remove( index );

                              out.println("OK");

                              System.out.println( "Client " + index + " is removed" );
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
