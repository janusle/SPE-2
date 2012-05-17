package server;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;



/*
 * process the request
 */
class processRequest implements Runnable {
 
   private SSLSocket s; 
   
   public processRequest(SSLSocket s){

       this.s = s;

   }

   private String getCurrentTime(){

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
        return formatter.format( currentDate.getTime() );

    }


   public void run(){

        long threadId = Thread.currentThread().getId();
        System.out.println( "Thread " + threadId + " started" );
        try{

                BufferedReader in = new BufferedReader(new InputStreamReader( s.getInputStream() ) );
                PrintWriter out = new PrintWriter( s.getOutputStream(), true);
                String line = null;
                if ( ( line = in.readLine() ) != null ){

                    if ( line.equals( "What is the time?" ) ){
                        System.out.println("Request from " + s.getInetAddress().getHostAddress() );
                        String currentTime = this.getCurrentTime();                    
                        out.println( currentTime ); 
                    }
                    else{
                        System.out.println("Invalid request from " + s.getInetAddress().getHostAddress() );
                    }

                }
                out.close();
                in.close();
                s.close();

        }
        catch(SSLHandshakeException sse){
                System.out.println( "Unknown certificate, ignore" );

        }
        catch(Exception e){

                System.out.println( e.getMessage() );
                
        }

        System.out.println( "Thread " + threadId + " finished\n" );
 

    }




}



public class concurrentServer {

    private int port;
    private int count = 0;
    private final int max = 10 + 1;

    public static void main(String[] args) {

        concurrentServer cs = new concurrentServer(40302);
        cs.run();

    }


    public concurrentServer( int port ){

        this.port = port;

    }


    public void run(){

        SSLServerSocket ss = null;

        try{

            SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            ss = (SSLServerSocket)ssf.createServerSocket( this.port );
            System.out.println("Server started...");

        }
        catch(Exception e){

            System.out.println( e.getMessage() );
            System.exit(-1);
        }


        while (true){ 
            
            if ( Thread.activeCount() > this.max ){ //check client number
               continue;
            }

            try{

                SSLSocket s = (SSLSocket) ss.accept();
                processRequest pr = new processRequest(s);
                Thread t = new Thread( pr );
                t.start();

             }
            catch(SSLHandshakeException sse){
                System.out.println( "Unknown certificate, ignore" );

            }
            catch(Exception e){

                System.out.println( e.getMessage() );
                System.exit(-1);
            }
        }    

    }


}
