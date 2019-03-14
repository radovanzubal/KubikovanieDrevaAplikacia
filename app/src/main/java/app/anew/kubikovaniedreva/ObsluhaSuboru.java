package app.anew.kubikovaniedreva;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ObsluhaSuboru {

    public static String nacitajUlozene(String fileName,Context con){
        String nacitane="";
        try {
            File f = new File(con.getFilesDir(), fileName);
            char[] mns = new char[(int)f.length()];
            FileReader fr = new FileReader(f);
            fr.read(mns);
            fr.close();
            nacitane += String.valueOf(mns);
        }catch(Exception e){e.printStackTrace();}
        return nacitane;
    }
    public static ArrayList<String> readByLine(Context con,String nazov){
        BufferedReader reader;
        ArrayList<String> arr=new ArrayList<>(0);
        try{
           // final InputStream file = getAssets().open("text.txt");
            File f = new File(con.getFilesDir(), nazov);
            FileInputStream is = new FileInputStream(f);
            reader = new BufferedReader(new InputStreamReader(is));
            String line = reader.readLine();
            while(line != null&&!line.equals("")){
                arr.add(line);
                line = reader.readLine();
            }
        } catch(Exception ioe){
            ioe.printStackTrace();
        }
        return arr;
    }
    public static void zapisDoSuboru(String ulozText,String nazovFile,int mode,Context con){
        try {
            FileOutputStream outputStream = con.openFileOutput(nazovFile,mode);
            outputStream.write(ulozText.getBytes());
            outputStream.close();
        }catch(Exception e){e.printStackTrace();}
    }
}
