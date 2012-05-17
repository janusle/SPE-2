package tools;
import java.util.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;

public class TimeInfo implements Serializable {

    public ArrayList<clientInfo> addresses; //client list
    public String time; //time info
    public int point; // the index of current client in the client list


    private String getCurrentTime(){

            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd:MM:yyyy");
            return formatter.format( currentDate.getTime() );

    }


    public TimeInfo( ArrayList<clientInfo> addresses ){

        this.addresses = addresses;
        this.point = 0; 
        this.time = getCurrentTime();
    }

     

}
