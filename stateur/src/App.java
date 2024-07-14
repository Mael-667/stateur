import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.text.DateFormat;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.swing.filechooser.FileSystemView;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        //test();
        //aya(10000000);
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new MyHandler());
        server.createContext("/stat", new FiveHead());
        server.setExecutor(null); // creates a default executor
        server.start();
        //Algo.Comparaison bruh = (g , d) -> g > d;
        //System.out.println(bruh.comp(6, 7));
        //ProcessBuilder bruh = new ProcessBuilder("cmd.exe", "/c", "%LOCALAPPDATA%\\Google\\Chrome\\Application\\chrome.exe --app=http://localhost:8000");
        //bruh.start();


        //StatYt stat = new StatYt("C:\\Users\\Mael\\Documents\\YouTube et YouTube Music\\historique\\watch-history.html");
        /*SortedMap<String, Charset> ruh = Charset.availableCharsets();
        for(Entry<String, Charset> oui: ruh.entrySet()){
            System.out.println(oui.getKey());
        }*/
        //StatDisc stat = new StatDisc("C:\\Users\\Mael\\Downloads\\package\\messages");
        
                /*try {
                    BufferedInputStream fi = new BufferedInputStream(new FileInputStream("src/bruh.txt"));
                    int v;
                    StringBuilder sb = new StringBuilder();
                    while((v = fi.read()) != -1){
                        sb.append((char) v);
                        System.out.println(v);
                    }
                    /*for(int i = 0; i < 7; i++){
                        sb.append((char) fi.read());
                    }
                    System.out.println(sb.toString());
                    fi.close();
                } catch (Exception e){
                    e.printStackTrace();
                }*/


    }

    private static void test(){
        StringBuilder sb = new StringBuilder();
        try{
        for(var k = 2; k < 129; k*=2){
            ArrayList<Long> moy = new ArrayList<>();
            for(var i = 1; i < 32; i*=2){
                for(var a = 0; a < 10; a++){
                    var aya = i*50000;
                    ArrayList<Double> bru = new ArrayList<>();
                    for(int j = 0; j < aya; j++){
                        bru.add((Double) (Math.random() * (aya*2)));
                    }
                    long mtn = Calendar.getInstance().getTimeInMillis();
                    Algo.triv3(bru, (g, d) -> g > d, k);
                    moy.add(Calendar.getInstance().getTimeInMillis() - mtn);
                }
            }
            long m = 0;
            for(Long e: moy){
                m+=e;
            }
            sb.append(k).append(" : ").append(m/moy.size()).append("ms").append("\n");
        }
        } catch(Exception e) {
            
        }
        System.out.println(sb.toString());
    }


    private static void aya(int nbItem){
        int aya = nbItem;
        int[] test = new int[aya];
        for(int i = 0; i < aya; i++){
            test[i] = (int) (Math.random() * (aya*2));
        }
        test = triv2(test);
        ArrayList<Double> bru = new ArrayList<>(aya);
        for(int i = 0; i < aya; i++){
            bru.add((Double) (Math.random() * (aya*2)));
        }
        //triv2(bru);
        //Algo.triv2(bru, (g, d) -> g > d);
        triv3(bru, (g, d) -> g > d);
        Algo.triv3(bru, (g, d) -> g > d, 16);
        boolean stopsvp = true;
        for(int i = 0; i < bru.size(); i++){
            try {
                if(bru.get(i) > bru.get(i-1)){
                    stopsvp = false;
                }
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
        System.out.println(bru.size());
        System.out.println(stopsvp);

    }













    public static ArrayList<Double> triv2(ArrayList<Double> list){
        long mtn = Calendar.getInstance().getTimeInMillis();

        ArrayList<Double> ans = defusion(list);
    
        System.out.println("temps dexecution de tri ultime " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms et il a trié "+ ans.size() +" objets 😎💪✌🏾");
        return ans;
    }

    private static ArrayList<Double> fusion(ArrayList<Double> g, ArrayList<Double> d){
        int len = g.size() + d.size();
        ArrayList<Double> ans = new ArrayList<>(len);
        int ig = 0;
        int id = 0;
        for(int i = 0; i < len; i++){
            if(ig == g.size()){
                ans.add(d.get(id));
                id++;
            } else if(id == d.size()){
                ans.add(g.get(ig));
                ig++;
            } else if( g.get(ig) < d.get(id)) {
                ans.add(d.get(id));
                id++;
            } else {
                ans.add(g.get(ig));
                ig++;
            }
        }
        return ans;
    }

    private static ArrayList<Double> defusion(ArrayList<Double> liste){
        if(liste.size() == 1){
            return liste;
        } else {
            ArrayList<Double> g = new ArrayList<>(liste.subList(0, liste.size()/2));
            ArrayList<Double> d = new ArrayList<>(liste.subList(liste.size()/2, liste.size()));
            return fusion(defusion(g), defusion(d));
        }
    }








    @FunctionalInterface
    public interface Comparaison<T> {
        boolean comp(T g, T d);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] triv3(ArrayList<T> list, Comparaison<T> c){
        long mtn = Calendar.getInstance().getTimeInMillis();
        Tri<T> bruh = new App().new Tri<>(c);
        T[] ans = (T[]) bruh.defusion(list.toArray());
        System.out.println("temps dexecution de tri ultime III " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms et il a trié "+ ans.length +" objets 😎💪✌🏾");
        return ans;
    }

    @SuppressWarnings("unchecked")
    public class Tri<T>{
        Comparaison<T> c;
        public Tri(Comparaison<T> c){
            this.c = c;
        }
        private Object[] fusion(Object[] g, Object[] d){
            int len = g.length + d.length;
            Object[] ans = new Object[len];
            int ig = 0;
            int id = 0;
            for(int i = 0; i < len; i++){
                if(ig == g.length){
                    ans[i] = d[id];
                    id++;
                } else if(id == d.length){
                    ans[i] = g[ig];
                    ig++;
                } else if(c.comp((T) g[ig], (T) d[id])) {
                    ans[i] = g[ig];
                    ig++;
                } else {
                    ans[i] = d[id];
                    id++;
                }
            }
            return ans;
        }
    
        public Object[] defusion(Object[] liste){
            if(liste.length == 1){
                return liste;
            } else {
                Object[] g = Arrays.copyOfRange(liste, 0, liste.length/2);
                Object[] d = Arrays.copyOfRange(liste, liste.length/2, liste.length);
                return fusion(defusion(g), defusion(d));
            }
        }
    }








    private static int[] triv2(int[] list){
        long mtn = Calendar.getInstance().getTimeInMillis();

        int[] ans = defusion(list);
    
        System.out.println("temps dexecution de tri ultime X " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms et il a trié "+ ans.length +" objets 😎💪✌🏾");
        return ans;
    }

    private static int[] fusion(int[] g, int[] d){
        int[] ans = new int[g.length + d.length];
        int len = g.length + d.length;
        int ig = 0;
        int id = 0;
            for(int i = 0; i < len; i++){
                if(g.length == ig){
                    ans[i] = d[id];
                    id++;
                } else if(d.length == id){
                    ans[i] = g[ig];
                    ig++;
                } else if( g[ig] < d[id]) {
                    ans[i] = d[id];
                    id++;
                } else {
                    ans[i] = g[ig];
                    ig++;
                }
            }
        return ans;
    }

    private static int[] defusion(int[] liste){
        if(liste.length == 1){
            return liste;
        } else {
            int[] g = Arrays.copyOfRange(liste, 0, liste.length/2);
            int[] d = Arrays.copyOfRange(liste, liste.length/2, liste.length);
            return fusion(defusion(g), defusion(d));
        }
    }








    

}
