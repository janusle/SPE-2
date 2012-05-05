package tools;
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


class TimeInfo{

    public ArrayList<clientInfo> addresses;
    public String time;
    public int point;

    public TimeInfo( ArrayList<clientInfo> addresses, String time){

        this.addresses = addresses;
        this.time = time;
        this.point = 0; 

    }


}
