import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;


public class Algo {
    




    public static String[] arrayer(String ar){
        return ar.split(",");
    }






    public static void indexer(HashMap<String, Integer> list, String index){
        int bruh = 1;
        if(list.containsKey(index)){
            bruh += list.get(index);
            list.remove(index);
        }
        list.put(index, bruh);
    }




    public static StringBuilder onreprendici(StringBuilder bru, int len, String file){
        int boucle = 2;
        StringBuilder bruh = new StringBuilder(bru);
        while(boucle > 0){
            if(file.charAt(len-1) == '>' && file.charAt(len-2) == 'a' && file.charAt(len-3) == '/'){
                boucle--;
            }
            bruh.append(file.charAt(len));
            len++;
        }
    
        return bruh;
    }



    public static String array2string(ArrayList<Entry<String, Integer>> liste){
        StringBuilder sb = new StringBuilder("[");
        for(int i = 0; i < liste.size(); i++){
            Entry<String, Integer> v = liste.get(i);
            String value = v.getKey().replaceAll("\\\\", "\\\\\\\\");
            value = value.replaceAll("\n", "");
            sb.append("[\"")
              .append(Integer.toString(v.getValue()))
              .append("\",\"")
              .append(value.replaceAll("\"", "\\\\\""))
              .append("\"]");
            if(i != liste.size()-1){
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }




    public static <T extends Number> ArrayList<Entry<String, T>> hashmap2arraylist(HashMap<String, T> map){
        ArrayList<Entry<String, T>> ans = new ArrayList<>();
        Set<Entry<String, T>> entryset = map.entrySet();
        Iterator<Entry<String, T>> wow = entryset.iterator();
        while(wow.hasNext()){
            Entry<String, T> wtf =  wow.next();
            ans.add(wtf);
        }

        return ans;
    }





    /**
     * Note de : <strong>moi</strong>
     * <p>g et d sont des entrées de l'arraylist, j'ai surchargé toutes les méthodes pr 
     * pouvoir trier autre chose que des entrymap
     * <p>si g > d == true trifusion ajoutera g
     * <p>bruh en gros si ça return true g sera ajouté donc emoji muscle
     */
    @FunctionalInterface
    public interface Comparaison<T> {
        boolean comp(T g, T d);
    }

    /*public static ArrayList<Entry<String, Integer>> triv2(ArrayList<Entry<String, Integer>> list){
        ArrayList<Entry<String, Integer>> ans = defusion(list, (g, d) -> g.getValue() > d.getValue());
        return ans;
    }*/

    /**
     * (g, d) -> g.getValue() > d.getValue()
     * 
     */
    public static <T> ArrayList<T> triv2(ArrayList<T> list, Comparaison<T> c){
        long mtn = Calendar.getInstance().getTimeInMillis();
        ArrayList<T> ans = defusion(list, c);
        System.out.println("temps dexecution de tri ultime II " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms et il a trié "+ ans.size() +" objets 😎💪✌🏾");
        return ans;
    }

    private static <T> ArrayList<T> fusion(ArrayList<T> g, ArrayList<T> d, Comparaison<T> c){
        int len = g.size() + d.size();
        ArrayList<T> ans = new ArrayList<>(len);
        int ig = 0;
        int id = 0;
        for(int i = 0; i < len; i++){
            if(ig == g.size()){
                ans.add(d.get(id));
                id++;
            } else if(id == d.size()){
                ans.add(g.get(ig));
                ig++;
            } else if(c.comp(g.get(ig), d.get(id))) {
                ans.add(g.get(ig));
                ig++;
            } else {
                ans.add(d.get(id));
                id++;
            }
        }
        return ans;
    }

    private static <T> ArrayList<T> defusion(ArrayList<T> liste, Comparaison<T> c){
        if(liste.size() == 1){
            return liste;
        } else {
            ArrayList<T> g = new ArrayList<>(liste.subList(0, liste.size()/2));
            ArrayList<T> d = new ArrayList<>(liste.subList(liste.size()/2, liste.size()));
            return fusion(defusion(g, c), defusion(d, c), c);
        }
    }











    @SuppressWarnings("unchecked")
    public static <T> T[] triv3(ArrayList<T> list, Comparaison<T> c, int nbThread){
        long mtn = Calendar.getInstance().getTimeInMillis();

        Object[] liste = list.toArray();
        Algo.Tri<?>[] tArr =  new Tri<?>[nbThread];
        for(int i = 0; i < nbThread; i++){
            int range = liste.length/nbThread;
            int max = i == nbThread-1 ? liste.length : i*range+range;
            tArr[i] = new Algo().new Tri<>(c, Arrays.copyOfRange(liste, i*range, max));
            tArr[i].setPriority(7);
            tArr[i].start();
        }
        Thread.currentThread();
        Object[][] rep = new Object[nbThread][];
        for(int i = 0; i < nbThread; i++){
            while(tArr[i].isAlive()){
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            rep[i] = tArr[i].liste;
        }
        T[] ans = (T[]) tArr[0].merge(rep);
        System.out.println("temps dexecution de tri ultime IV " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms et il a trié "+ ans.length +" objets 😎💪✌🏾");
        return ans;
    }

    @SuppressWarnings("unchecked")
    public class Tri<T> extends Thread{
        Comparaison<T> c;
        Object[] liste;
        public void run(){
            liste = defusion(liste);
        }
        public Tri(Comparaison<T> c, Object[] liste){
            this.c = c;
            this.liste = liste;
        }

        public Object[] merge(Object[][] list){
            ArrayList<Object[]> ans = new ArrayList<>(Arrays.asList(list));
            while(ans.size() != 1){
                ArrayList<Object[]> temp = new ArrayList<>(ans.size()/2+1);
                for(var i = 0; ans.size() - i > 1; i++){
                    temp.add(fusion(ans.get(i), ans.get(++i)));
                }
                ans = temp;
            }
            return ans.get(0);
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

















    public static ArrayList<Entry<String, Integer>> triv2(ArrayList<Entry<String, Integer>> list){
        long mtn = Calendar.getInstance().getTimeInMillis();

        ArrayList<Entry<String, Integer>> ans = defusion(list);
    
        System.out.println("temps dexecution de tri ultime " + (Calendar.getInstance().getTimeInMillis() - mtn) + "ms et il a trié "+ ans.size() +" objets 😎💪✌🏾");
        return ans;
    }

    private static ArrayList<Entry<String, Integer>> fusion(ArrayList<Entry<String, Integer>> g, ArrayList<Entry<String, Integer>> d){
        int len = g.size() + d.size();
        ArrayList<Entry<String, Integer>> ans = new ArrayList<>(len);
        int ig = 0;
        int id = 0;
        for(int i = 0; i < len; i++){
            if(ig == g.size()){
                ans.add(d.get(id));
                id++;
            } else if(id == d.size()){
                ans.add(g.get(ig));
                ig++;
            } else if( g.get(ig).getValue() < d.get(id).getValue()) {
                ans.add(d.get(id));
                id++;
            } else {
                ans.add(g.get(ig));
                ig++;
            }
        }
        return ans;
    }

    private static ArrayList<Entry<String, Integer>> defusion(ArrayList<Entry<String, Integer>> liste){
        if(liste.size() == 1){
            return liste;
        } else {
            ArrayList<Entry<String, Integer>> g = new ArrayList<>(liste.subList(0, liste.size()/2));
            ArrayList<Entry<String, Integer>> d = new ArrayList<>(liste.subList(liste.size()/2, liste.size()));
            return fusion(defusion(g), defusion(d));
        }
    }








}
