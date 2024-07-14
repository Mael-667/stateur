import java.io.BufferedReader;
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
import java.io.FileReader;

public class TestStat extends Thread {

    int len;
    int range;
    String file;
    ArrayList<String> urllist = new ArrayList<>();
    ArrayList<String> trueurllist = new ArrayList<>();

    public TestStat(int len, int range,String file){
        this.len = len;
        this.range = range;
        this.file = file;
    }

    public void run(){
        while(len < range || len != file.length()){
            if(file.charAt(len) == '<' && file.charAt(len+1) == 'a'){
                StringBuilder bruh = new StringBuilder();
                while(!(file.charAt(len-1)== '>' && file.charAt(len-2) == 'a' && file.charAt(len-3) == '/')){
                    bruh.append(file.charAt(len));
                    len++;
                }
                if(bruh.indexOf("myaccount.google") == -1){
                    if(bruh.indexOf("channel") == -1){
                        trueurllist.add(Algo.onreprendici(bruh, len, file).toString());
                    }
                    urllist.add(bruh.toString());
                }
            }
            len++;
        }
    }

    

    /*public static void indexer(HashMap<String, Integer> liste, String index){
        Map<String, Integer> list = Collections.synchronizedMap(liste);
        synchronized(list){
            int bruh = 1;
            if(list.containsKey(index)){
                bruh += list.get(index);
                list.remove(index);
            }
            list.put(index, bruh);
        }
    }*/



    public static class Bruh{
        private void statv2(String fichier){

            HashMap<String, Integer> trueurllist = new HashMap<>();
            HashMap<String, Integer> urllist = new HashMap<>();
            ArrayList<Entry<String, Integer>> chaine = new ArrayList<>();
            ArrayList<Entry<String, Integer>> video = new ArrayList<>();
            ArrayList<Entry<String, Integer>> ttul = new ArrayList<>();

            //InputStreamReader fi;
                    try {
                        long mtn = Calendar.getInstance().getTimeInMillis();
                        BufferedReader fi = new BufferedReader(new FileReader(fichier, Charset.forName("UTF-8")), 800);
                        int caract = 0;
                        while((caract = fi.read()) != -1){
                            if(caract == '<'){
                                if(fi.read() == 'a'){
                                    StringBuilder bruh = new StringBuilder("<a");
                                    while(!(bruh.charAt(bruh.length()-1)== '>' && bruh.charAt(bruh.length()-2) == 'a' && bruh.charAt(bruh.length()-3) == '/')){
                                        bruh.append((char)fi.read());
                                    }
                                    if(bruh.indexOf("myaccount.google") == -1){
                                        Algo.indexer(urllist, bruh.toString());
                                        if(bruh.indexOf("channel") == -1){
                                            fi.mark(700);
                                            int boucle = 2;
                                            while(boucle > 0){
                                                if(bruh.charAt(bruh.length()-1)== '>' && bruh.charAt(bruh.length()-2) == 'a' && bruh.charAt(bruh.length()-3) == '/'){
                                                    boucle--;
                                                }
                                                bruh.append((char) fi.read());
                                            }
                                            Algo.indexer(trueurllist, bruh.toString());
                                            fi.reset();
                                        }
                                    }
                                }
                            }
                        }
                        System.out.println("temps d'execution de l'algo de moi " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms 😳 et il a traité caracteres");
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
                        video = Algo.triv2(video);
                        chaine = Algo.triv2(chaine);

                        Set<Entry<String, Integer>> entryset2 = trueurllist.entrySet();
                        Iterator<Entry<String, Integer>> wow2 = entryset2.iterator();
                        while(wow2.hasNext()){
                            Entry<String, Integer> wtf =  wow2.next();
                            ttul.add(wtf);
                        }
                        ttul = Algo.triv2(ttul);
                        fi.close();

                        //System.out.println(video.get(0).getKey().indexOf("』"));
                        /*for(int l = 0; l < 10; l++){
                            System.out.println(chaine.get(l).getKey() + chaine.get(l).getValue());
                        }
                        for(int l = 0; l < 10; l++){
                            System.out.println(video.get(l).getKey() + video.get(l).getValue());
                        }*/




                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        }


    }
}
