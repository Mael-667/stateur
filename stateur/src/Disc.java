import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Disc extends Thread {

    File[] fichiers;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<Long> al2 = new ArrayList<>();

    public Disc(File[] fichier){
        this.fichiers = fichier;
    }
    public void run(){
        for(File dosscsv: fichiers){
            if(dosscsv.isDirectory()){
                try{
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
                        long temps = date.getTimeInMillis();
                        al.add(msg.toString());
                        al2.add(temps);
                    }
                }catch(Exception e){
                            
                }
            }
        }
    }
}