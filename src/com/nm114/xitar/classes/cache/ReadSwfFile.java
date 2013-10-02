/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nm114.xitar.classes.cache;

import com.nm114.xitar.classes.*;
import java.io.*;
import java.net.*;
import java.util.*;

import com.nm114.xitar.exception.*;

/**
 *
 * @author brgd
 */
public class ReadSwfFile extends ReadFile {
    //<editor-fold defaultstate="collapsed" desc="VARIABLES.">
    //<editor-fold defaultstate="collapsed" desc="STATIC variables.">
    private static int objCount = 0;
    private static final Set<String> suffix = new HashSet<String>();
    private static HashMap<String, byte[]> swfs = new HashMap<String, byte[]>();
    private static HashMap<String, String> filesInStage = new HashMap<String, String>();
    private static String absolutePath = null;
    //<editor-fold defaultstate="collapsed" desc="OBJECT variables. empty">
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="METHODS.">
    //<editor-fold defaultstate="collapsed" desc="STATIC methods.">
    public static synchronized ReadSwfFile getInstance() throws CreateObjectOutOfLimit {
        if (objCount < objLimit) {
            ++objCount;
            return new ReadSwfFile();
        } else {
            throw new CreateObjectOutOfLimit("Create ReadCssFile object Out of limit.");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="INITIAL methods.">
    private ReadSwfFile() {
        System.out.println("ReadSwfFile()");
        suffix.add("swf");

        String f0 = "error.swf;";
        String f1 = f0 + "login.swf;building.swf;";
        String f2 = f0 + "building.swf;board.swf;";


        filesInStage.put(Integer.toString(CONFIG.INIT_STAGE),f0);
        filesInStage.put(Integer.toString(CONFIG.LOGIN_STAGE), f1);
        filesInStage.put(Integer.toString(CONFIG.BUILDING_STAGE), f2);
        filesInStage.put(Integer.toString(CONFIG.ROOM_STAGE), f2);
        filesInStage.put(Integer.toString(CONFIG.DESK_STAGE), f2);

        URL location;
        String classLocation = ReadSwfFile.class.getName().replace('.', '/') + ".class";
        ClassLoader loader = ReadSwfFile.class.getClassLoader();
        location = loader.getResource(classLocation);
        String stl = location.toString();
        String t = stl.substring(stl.indexOf('/'), stl.lastIndexOf(classLocation) - 1);

        if (t.indexOf(":/") != -1) { //windows
            t = t.substring(t.indexOf('/') + 1);
        }
        absolutePath = t.substring(0, t.lastIndexOf('/') + 1) + "swf/";

        readContent();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SET methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GET methods.">
    public byte[] getContent(int seq,String filename) { //seq = access sequence

        byte[] swf = null;

        if(fileInSequence(seq,filename)){
            swf = swfs.get(filename);
        }
        return swf;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CAN methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Has methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="DELETE methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="ADD methods. empty">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="other methods.">

    public void readContent() {
        listPath(new File(absolutePath));
    }
    //

    private void listPath(File path) {
        File[] files = path.listFiles();
        for (int i = 0, n = files.length; i < n; i++) {
            String name = "";
            if (files[i].isDirectory()) {
                listPath(files[i]);
            } else {
                String t = files[i].getName();
                t = t.substring(t.lastIndexOf(".") + 1);
                if (suffix.contains(t.toLowerCase())) {
                    name = files[i].getName();
                    byte[] result = new byte[(int) files[i].length()];
                    try {
                        FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
                        in.read(result);
                    } catch (Exception ex) {
                        System.out.println("Exception caught: " + ex.getMessage());
                    }
                    swfs.put(name, result);
                }
            }
        }
    }

     private boolean fileInSequence(int seq, String filename) {
         if(CONFIG.DEBUG){
            System.out.println(seq + " - " + filename);
         }
         
         boolean match = false;

         String fn = (String)filesInStage.get(Integer.toString(seq));

         if(fn.contains(filename)){
            match = true;
         }
         return match;
     }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="INNER OBJECTS. empty">
//</editor-fold>
}
