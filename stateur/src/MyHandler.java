import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.lang.StringBuilder;
import java.nio.file.Path;
import java.nio.file.FileSystems;
import java.io.FileInputStream;
import java.lang.String;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MyHandler implements HttpHandler {
    public void handle(HttpExchange t){
        //System.out.println(t.getRequestURI());
        try {
            t.sendResponseHeaders(200, 0);
            OutputStream os = t.getResponseBody();
            String req = t.getRequestURI().toString();
            switch(req){
                case("/"):
                    req = "html.html";
            }
            Path path = FileSystems.getDefault().getPath("src", "site",req);
            //BufferedReader in = new BufferedReader(new FileReader(path.toFile()));
            //String bruh = t.getRequestURI().toString().replaceFirst("/", ""); //replace car l'uri c'est /bruh/bruh et java veut bruh/bruh
            //Path path = FileSystems.getDefault().getPath(bruh);
            FileInputStream in = new FileInputStream(path.toFile());
            int line = in.read();
            while(line != -1){
                os.write(line);
                line = in.read();
            }
            in.close();
            os.close();
        } catch (IOException e) {
            String response = "Petite erreur ici ou quoi";
            OutputStream os = t.getResponseBody();
            try {
                os.write(response.getBytes());
                os.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
