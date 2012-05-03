import java.io.*;
import java.net.*;
import java.util.*;


class clientInfo{

    private InetAddress ia;
    private int port;

    public clientInfo(InetAddress ia, int port){

        this.ia = ia;
        this.port = port;

    }

    public InetAddress getAddress(){

        return ia;

    }

    public int getPort(){

       return port;
    }

    public boolean equals( clientInfo ci ){

        if( ia.equals( ci ) )
          return true;
        else
          return false;

    }

}

public class multiServer{

    private final int PORT = 40302;
    private ArrayList<clientInfo> addresses = new ArrayList<clientInfo>();

    public static void main(String []args){

    }


    public void run(){

          try{
           
             ServerSocket ss = new ServerSocket(PORT);
             while(true){
  
               Socket cs =  ss.accept();
               BufferedReader in = new BufferedReader( new InputStreamReader( cs.getInputStream() ) );
               String line;
               if ( ( line = in.readLine() ) != null ){

                   if( line.equals("subscribe") ){

                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();
                       clientInfo ci = new clientInfo( ia, port );
                        
                       if ( ( int index = addresses.indexOf( ci ) ) == -1 ){
                    
                           addresses.add( ci );
                           System.out.println("New subscriber from " + ia.getHostAddress() );

                       }
                       else{ //duplicated subscribe
                           System.err.println("subscriber " + ia.getHostAddress() + " is already in the list");

                       }

                   }
                   else if ( line.equals("unsubscribe") ){
                       
                       InetAddress ia = cs.getInetAddress();
                       int port = cs.getPort();
                       if ( ( int index = addresses.indexOf( new clientInfo( ia, port) )) != -1 ){ //remove client
                          addresses.remove( index );

                       }
                       else{ //invalid unsubscribe command cause client is not in the list

                          System.err.println("Invalid unsubscribe from " + ia.getHostAddress() );

                       }
                   }

               }

             }

          }
          catch( IOException e){
  
              System.err.println("Server error: " + e.getMessage() );
              System.exit(-1);
  
          }
  
    }



    

}
