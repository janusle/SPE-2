import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class iterative_server {

    private int port;


    public static void main(String[] args) {

        iterative_server is = new iterative_server(9999);
        is.run();

    }

    private String getCurrentTime(){

        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
        return formatter.format( currentDate.getTime() );

    }

    public iterative_server( int port ){

        this.port = port;

    }


    public void run(){

        SSLServerSocket ss = null;
        try{

            SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            ss = (SSLServerSocket)ssf.createServerSocket( this.port );
            System.out.println("Server start...");
        }
        catch(Exception e){

            System.out.println( e.getMessage() );
            System.exit(-1);
        }

        BufferedReader in = null;
        PrintWriter out = null;
        SSLSocket s = null;

        while (true){ 
            try{

                s = (SSLSocket) ss.accept();
                in = new BufferedReader(new InputStreamReader( s.getInputStream() ) );
                out = new PrintWriter( s.getOutputStream(), true);
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
                System.exit(-1);
            }
        }    

    }


}
