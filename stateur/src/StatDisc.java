import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.lang.StringBuilder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class StatDisc {

    private ArrayList<String> message = new ArrayList<>();
    private HashMap<String, Long> temps = new HashMap<>();
    private HashMap<String, Integer> liste2mot = new HashMap<>();
    private HashMap<String, Integer> liste2emote = new HashMap<>();
    private HashMap<String, Integer> liste2lettre = new HashMap<>();
    ArrayList<Entry<String, Long>> messagentlist = new ArrayList<>();
    ArrayList<Entry<String, Integer>> liste2motlist = new ArrayList<>();
    ArrayList<Entry<String, Integer>> liste2emotelist = new ArrayList<>();
    ArrayList<Entry<String, Integer>> liste2lettrelist = new ArrayList<>();
    int messages;
    int mots;
    int emotes;
    int lettres;
    long[][][] heatmap = new long[7][24][6];

    public StatDisc(String fichier){
        statv2(fichier);
        //System.out.println(stat(fichier));
        //System.out.println(statv2(fichier));
    }

    private long statv2(String doss){
        try{
            File dossier = new File(doss);
            File[] fichiers = dossier.listFiles();
            

            long mtn = Calendar.getInstance().getTimeInMillis();

            int thread = 16;
            Disc[] bruh = new Disc[thread];
            for(int i = 0; i < thread; i++){
                int range = fichiers.length/thread;
                bruh[i] = new Disc(Arrays.copyOfRange(fichiers, i*range, i*range+range));
                //bruh[i].setPriority(Thread.MAX_PRIORITY);
                bruh[i].start();
            }
            Thread.currentThread();
            int oui = 0;
            for(int i = 0; i < thread; i++){
                while(bruh[i].isAlive()){
                    Thread.sleep(15);
                }
                for(int j = 0; j < bruh[i].al.size(); j++){
                    message.add(bruh[i].al.get(j));
                    temps.put(Integer.toString(oui), bruh[i].al2.get(j));
                    stateur(bruh[i].al.get(j));
                    mapchaleur(heatmap, bruh[i].al2.get(j));
                    oui++;
                }
            }
            //penser a reserver la place des arraylist
            //System.out.println("temps d'execution de l'algo de moi " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms 😳 et il a traité "+len+" caracteres");
            //System.out.println(urllist.size());
            Algo.triv3(Algo.hashmap2arraylist(temps), (g, d) -> g.getValue() > d.getValue(), 8);
            App.triv3(Algo.hashmap2arraylist(temps), (g, d) -> g.getValue() > d.getValue());
            messagentlist = Algo.triv2(Algo.hashmap2arraylist(temps), (g, d) -> g.getValue() > d.getValue());
            liste2motlist = Algo.triv2(Algo.hashmap2arraylist(liste2mot));
            liste2emotelist = Algo.triv2(Algo.hashmap2arraylist(liste2emote));
            liste2lettrelist = Algo.triv2(Algo.hashmap2arraylist(liste2lettre));
            return (Calendar.getInstance().getTimeInMillis() - mtn);

        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    public long stat(String doss){
        try{
            int non = 0;
            File dossier = new File(doss);
            File[] fichiers = dossier.listFiles();
            long mtn = Calendar.getInstance().getTimeInMillis();
            for(File dosscsv: fichiers){
                if(dosscsv.isDirectory()){
                    FileInputStream fi = new FileInputStream(new File(dosscsv, "messages.csv"));
                    String csv = new String(fi.readAllBytes(), "UTF-8");
                    Pattern p = Pattern.compile("\n(?=[0-9]{18}).*");
                    Matcher m = p.matcher(csv);
                    while(m.find()){            
                        String[] info = m.group().split(",");     
                                                         
                        StringBuilder msg = new StringBuilder();
                        int max = info.length == 3 ? 3 : info.length-1;      
                        for(int i = 2; i < max; i++){        
                            msg.append(info[i]);
                        }
                        Calendar date = Calendar.getInstance();
                        date.set(Integer.parseInt(info[1].substring(0, 4))
                                ,Integer.parseInt(info[1].substring(5, 7))
                                ,Integer.parseInt(info[1].substring(8,10))
                                ,Integer.parseInt(info[1].substring(11, 13))
                                ,Integer.parseInt(info[1].substring(14, 16)),
                                Integer.parseInt(info[1].substring(17,19)));
                        long time = date.getTimeInMillis();
                        //System.out.println((long) temps*1000);
                        String messagent = msg.toString();
                        message.add(messagent);
                        temps.put(Integer.toString(non), time);
                        stateur(messagent);
                        mapchaleur(heatmap, time);
                        non++;
                    }
                }
            }
            //System.out.println(non); 
            //System.out.println(message.size());
            messagentlist = Algo.triv2(Algo.hashmap2arraylist(temps), (g, d) -> g.getValue() > d.getValue());
            liste2motlist = Algo.triv2(Algo.hashmap2arraylist(liste2mot));
            liste2emotelist = Algo.triv2(Algo.hashmap2arraylist(liste2emote));
            liste2lettrelist = Algo.triv2(Algo.hashmap2arraylist(liste2lettre));
            return (Calendar.getInstance().getTimeInMillis() - mtn);

        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }

    private void stateur(String msg){
        //System.out.println("calcul de tout et 2moi !! !!! (c long psk jsp si je dois save le résultat ou pas !!!!)");
        messages++;
        for(String v: msg.split(" ")){
            if(v != ""){
                mots+=1;
                Algo.indexer(liste2mot, v);
                Pattern p = Pattern.compile(":\\w*:");
                Matcher m = p.matcher(v);
                while(m.find()){
                    emotes+=1;
                    Algo.indexer(liste2emote, m.group());
                }
            }
        }
        for(String v: msg.split("")){
            Algo.indexer(liste2lettre, v);     
        }
        lettres+=msg.length();
    }

    private void mapchaleur(long[][][] liste, long milli){
        Calendar date = Calendar.getInstance();
        //System.out.println(date.getTimeInMillis());
        final int jour = date.get(Calendar.DAY_OF_WEEK);
        liste[jour-1][date.get(Calendar.HOUR_OF_DAY)][date.get(Calendar.MINUTE) / 10] += 1;
    }


}
