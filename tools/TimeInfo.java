package tools;
import java.util.*;
import java.io.Serializable;

public class TimeInfo implements Serializable {

    public ArrayList<clientInfo> addresses;
    public String time;
    public int point;

    public TimeInfo( ArrayList<clientInfo> addresses, String time){

        this.addresses = addresses;
        this.time = time;
        this.point = 0; 

    }


}
