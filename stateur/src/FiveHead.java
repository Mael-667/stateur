import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.lang.StringBuilder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.nio.file.FileSystems;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public  class FiveHead implements HttpHandler {
    public void handle(HttpExchange t) throws IOException{
        InputStream is = t.getRequestBody();
        t.sendResponseHeaders(200, 0);
        OutputStream os = t.getResponseBody();
        String response = "oui";

        //System.out.println(new String(is.readAllBytes()));

        String[] req = Algo.arrayer(new String(is.readAllBytes(), "UTF-8"));
        /*for(int j = 0; j < req.length; j++){
            System.out.println(req[j]);
        }*/
        switch(req[0]){
            case("disc"):
                response = disc(req);
                break;
            case("yt"):
                response = yt(req);
                break;
            case("explorer"):
                response = FileExplorer.explorer(req[1]);
                break;
            default:
                response = "ouiettoi";                
                break;
        }
        os.write(response.getBytes("UTF-16"));
        os.close();
    } 


    StatDisc sd;
    private String disc(String[] arg){
        switch(arg[1]){
            case("stat"):
                sd = new StatDisc(arg[2]);
                String json = "{\"emotes\": "+Algo.array2string(sd.liste2emotelist)+
                                ",\"lettres\": "+Algo.array2string(sd.liste2lettrelist)+
                                "}";
                return json;
        }
        return "bruh";
    }



    StatYt stat;
    private String yt(String[] arg){
        switch(arg[1]){
            case("stat"):
                stat = new StatYt(arg[2]);
                String json = "{\"chaine\": "+stat.listechaine+
                              ",\"video\": "+stat.listevid+
                              ",\"ttul\": "+stat.listevidmaschaine+
                              "}";
                return json;
        }
        return null;
    }

    
}
