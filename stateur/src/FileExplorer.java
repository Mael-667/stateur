import java.io.File;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class FileExplorer {
    public static String explorer(String lien){
        try{
            FileSystemView bruh = FileSystemView.getFileSystemView();
            File wd; 
            if(lien.equals("default")){ //pov j'ai fait lien == "bruh" et ça m'a 🤞 (ptet a cause du changemnt de charset)
                wd = bruh.getDefaultDirectory();
            } else {
                wd = new File(lien);
            }
            //System.out.println(bruh.getSystemDisplayName(bruh.getDefaultDirectory()));
            //recupération et envoi des fichiers enfants
            //creer nouvelle icone a partir de image(fichier) si non flemme (siu = new imageicon(oui[i].path))
            //j'ai eu lidée en voyant une icone que g crée avoir sa minia pov
            /*ImageIcon siu;
                if(oui[i].getName().endsWith(".jpg")){
                    siu = new ImageIcon(oui[i].getPath());
                } else {
                    siu = (ImageIcon) bruh.getSystemIcon(oui[i]);
                } */
            //java.lang.ClassCastException: class sun.awt.image.ToolkitImage cannot be cast to class java.awt.image.BufferedImage (sun.awt.image.ToolkitImage and java.awt.image.BufferedImage are in module java.desktop of loader 'bootstrap')
            StringBuilder sb = new StringBuilder();
            File[] oui = bruh.getFiles(wd, false);
            sb.append("['");
            sb.append(wd.getAbsolutePath().replace("\\", "\\\\"));
            sb.append("',");
            for(int i = 0; i < oui.length; i++){
                ImageIcon siu = (ImageIcon) bruh.getSystemIcon(oui[i]);
                BufferedImage crvr = (BufferedImage) siu.getImage();
                Raster ayo = crvr.getData(); //AYAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
                StringBuilder icon = new StringBuilder("[");
                for(int h = 0; h < ayo.getHeight(); h++){
                    icon.append("[");
                    for(int w = 0; w < ayo.getWidth(); w++){
                        int[] rgba = new int[4];
                        ayo.getPixel(h, w, rgba);
                        icon.append(Arrays.toString(rgba));
                        if(w != ayo.getWidth()-1){
                            icon.append(",");
                        }
                    }
                    icon.append("]");
                    if(h != ayo.getHeight()-1){
                        icon.append(",");
                    }
                }
                icon.append("]");
                /*int br = oui[i].getPath().length();
                if(oui[i].getPath().indexOf("YouTube") != -1){
                    System.out.println(oui[i].getPath().codePointAt(br-6));
                    return oui[i].getPath();
                }*/

                String json = "{\"path\": \""+oui[i].getPath().replace("\\", "\\\\")+
                            "\",\"name\": \""+oui[i].getName()+
                            "\",\"directory\": \""+oui[i].isDirectory()+
                            "\",\"icon\": \""+icon.toString()+
                            "\",\"width\": \""+ayo.getWidth()+
                            "\",\"height\": \""+ayo.getHeight()+"\"}";
                sb.append(json);
                if(i != oui.length-1){
                    sb.append(",");
                }
            }
            sb.append("]");
            return sb.toString();
        } catch(Exception e){
            e.printStackTrace();
            return "ayo";
        }
    }
}
