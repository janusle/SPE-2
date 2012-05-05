package server;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import tools.*;

public class MultiServer{

    private final int PORT = 40302;
    private ArrayList<clientInfo> addresses = new ArrayList<clientInfo>();
    private Timer timer = null;

    public static void main(String []args){

        new MultiServer().run(); //runing server;
         
    }

    class Sender extends TimerTask{
   
        private String getCurrentTime(){

            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
            return formatter.format( currentDate.getTime() );

        }

        public void run() {
          
            if( addresses.size() > 0 ){ 
              
              clientInfo ci;
              InetAddress ip;
              int port;
              TimeInfo timeinfo = null;
    
              for(int i=0; i<addresses.size(); i++ ){

                    try{

                        ci = addresses.get(i); // get first client
                        ip = ci.getAddress();
                        port = ci.getPort();
                        Socket s = new Socket( ip, port );
                        ObjectOutputStream out = new ObjectOutputStream( s.getOutputStream() );
                        String time = this.getCurrentTime();               
                        timeinfo = new TimeInfo( addresses, time );
                        out.writeObject( timeinfo );
                        out.close();
                        s.close();
                        System.out.println("Time is sent to client " + i + " " + ip.getHostAddress() + ":" + port +"\n");
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
                       clientInfo ci = new clientInfo( ia, port );
                       if ( ( index = addresses.indexOf( ci ) ) == -1 ){
              
                           
                           addresses.add( ci );
                           System.out.println("New subscriber from " + ia.getHostAddress() );
                           out.println("OK"); //confirm subscription

                           if ( this.timer == null ){
                              timer = new Timer();
                              timer.schedule( new Sender(), 3 * 1000, 3 * 1000 );
                              System.out.println("Timer is set");
                           }


                       }
                       else{ //duplicated subscribe
                           System.err.println("subscriber " + ia.getHostAddress() + " is already in the list");

                       }

                   }
                   else if ( line.equals("unsubscribe") ){
                       
                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();
                       if ( ( index = addresses.indexOf( new clientInfo( ia, port) )) != -1 ){ //remove client
                          addresses.remove( index );

                       }
                       else{ //invalid unsubscribe command cause client is not in the list

                          System.err.println("Invalid unsubscribe from " + ia.getHostAddress() );

                       }
                   }
                   else{ // invalid request 
                     
                       InetAddress ia = cs.getInetAddress();
                       System.err.println("Invalid request from " + ia.getHostAddress() );

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
