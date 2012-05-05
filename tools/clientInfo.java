package tools;
import java.net.*;
import java.io.Serializable;

public class clientInfo implements Serializable{

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



