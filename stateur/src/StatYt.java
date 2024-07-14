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
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;



public class StatYt {

    public String listevid;
    public String listechaine;
    public String listevidmaschaine;

    public StatYt(String fichier){
        stat(fichier);
    }

    private void test(String fichier){
        int len = 10;
        long[] v1 = new long[len];
        long[] v3 = new long[len];
        for(int i = 0; i < len; i++){
            v3[i] = statv3(fichier);
            v1[i] = stat(fichier);
        }
        long moyv1 = 0;
        long moyv3 = 0;
        for(int i = 0; i < len; i++){
            moyv1+=v1[i];
            moyv3+=v3[i];
        }
        System.out.println("moyenne v1 = "+ moyv1/v1.length);
        System.out.println("moyenne v3 = "+ moyv3/v3.length);
    }


    private long statv3(String fichier){

        ArrayList<Entry<String, Integer>> chaine = new ArrayList<>();
        ArrayList<Entry<String, Integer>> video = new ArrayList<>();
        ArrayList<Entry<String, Integer>> ttul = new ArrayList<>();
        HashMap<String, Integer> trueurllist = new HashMap<>();
        HashMap<String, Integer> urllist = new HashMap<>();
        try {
            long mtn = Calendar.getInstance().getTimeInMillis();
            FileInputStream fi = new FileInputStream(new File(fichier));
            String file = new String(fi.readAllBytes(), "UTF-8");

            int thread = 2;
            TestStat[] bruh = new TestStat[thread];
            for(int i = 0; i < thread; i++){
                int range = file.length()/thread;
                bruh[i] = new TestStat(i*range, i*range+range, file);
                bruh[i].setPriority(Thread.MAX_PRIORITY);
                bruh[i].start();
            }
            Thread.currentThread();
            for(int i = 0; i < thread; i++){
                while(bruh[i].isAlive()){
                    Thread.sleep(15);
                }
                bruh[i].urllist.forEach(v -> Algo.indexer(urllist, v));
                bruh[i].trueurllist.forEach(v -> Algo.indexer(trueurllist, v));
            }
            //System.out.println("temps d'execution de l'algo de moi " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms 😳 et il a traité "+len+" caracteres");
            //System.out.println(urllist.size());
            return (Calendar.getInstance().getTimeInMillis() - mtn);
            /*Set<Entry<String, Integer>> entryset = urllist.entrySet();
            Iterator<Entry<String, Integer>> wow = entryset.iterator();
            while(wow.hasNext()){
                Entry<String, Integer> wtf =  wow.next();
                if(wtf.getKey().indexOf("watch?v=") == -1){
                    chaine.add(wtf);
                } else {
                    video.add(wtf);
                }
            }
            video = Algo.triv2(video);
            chaine = Algo.triv2(chaine);

            Set<Entry<String, Integer>> entryset2 = trueurllist.entrySet();
            Iterator<Entry<String, Integer>> wow2 = entryset2.iterator();
            while(wow2.hasNext()){
                Entry<String, Integer> wtf =  wow2.next();
            ttul.add(wtf);
            }
            ttul = Algo.triv2(ttul);

            listevid = Algo.array2string(video);
            listechaine = Algo.array2string(chaine);
            listevidmaschaine = Algo.array2string(ttul);*/

        } catch(Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    private long stat(String fichier){

        HashMap<String, Integer> trueurllist = new HashMap<>();
        HashMap<String, Integer> urllist = new HashMap<>();
        ArrayList<Entry<String, Integer>> chaine = new ArrayList<>();
        ArrayList<Entry<String, Integer>> video = new ArrayList<>();
        ArrayList<Entry<String, Integer>> ttul = new ArrayList<>();

        FileInputStream fi;
                try {
                    long mtn = Calendar.getInstance().getTimeInMillis();
                    fi = new FileInputStream(new File(fichier));
                    String file = new String(fi.readAllBytes(), "UTF-8");
                    StringBuilder bruh = new StringBuilder();
                    int len = 0;
                    while(len != file.length()){
                        //le while est un piege pov ça se met en pause si ça trouve une string que je veux
                        //puis ça reprend apres avoir cc tout ce que je voulais
                        //ça reprend apres la string copiée
                        if(file.charAt(len) == '<' && file.charAt(len+1) == 'a'){ //g modifié le code pr me facilier la suite mais pr grab les liens tu remplaces "<" "a" par '"' '=' et t'adaptes les - ou + de len jsp
                            bruh = new StringBuilder();
                            while(!(file.charAt(len-1)== '>' && file.charAt(len-2) == 'a' && file.charAt(len-3) == '/')){
                                bruh.append(file.charAt(len));
                                len++;
                            }
                            //a partir de là j'ai soit une video soit une chaine
                            if(bruh.indexOf("myaccount.google") == -1){
                                if(bruh.indexOf("channel") == -1){
                                    //on reprend ici car ici une video car ya pas encore le channel justement
                                    //dcp apres on a video + chaine
                                    //c'est pour compter le nombre de vue par video et par chaine
                                    Algo.indexer(trueurllist, Algo.onreprendici(bruh, len, file).toString());
                                    //bruh est une ref a l'objet pas un doublon comme en js probablement
                                    //pr ça que ça marchait pas
                                    //dcp j'ai dupliqué bruh dans onreprendici               
                                }
                                Algo.indexer(urllist, bruh.toString());
                            }
                        }
                        len++;
                    }
                    //System.out.println("temps d'execution de l'algo de moi " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms 😳 et il a traité "+len+" caracteres");
                    //System.out.println(urllist.size());

                    Set<Entry<String, Integer>> entryset = urllist.entrySet();
                    Iterator<Entry<String, Integer>> wow = entryset.iterator();
                    while(wow.hasNext()){
                        Entry<String, Integer> wtf =  wow.next();
                        if(wtf.getKey().indexOf("watch?v=") == -1){
                            chaine.add(wtf);
                        } else {
                            video.add(wtf);
                        }
                    }
                    App.triv3(video, (g, d) -> g.getValue() > d.getValue());
                    Algo.triv2(video, (g, d) -> g.getValue() > d.getValue());
                    Algo.triv3(video, (g, d) -> g.getValue() > d.getValue(), 32);
                    video = Algo.triv2(video);
                    chaine = Algo.triv2(chaine);
                    ttul = Algo.triv2(Algo.hashmap2arraylist(trueurllist));

                    listevid = Algo.array2string(video);
                    listechaine = Algo.array2string(chaine);
                    listevidmaschaine = Algo.array2string(ttul);
                    return (Calendar.getInstance().getTimeInMillis() - mtn);
                    // return affichage(video, channel)
                    // return funfact(video)
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } 
                return 0;
    }
}
