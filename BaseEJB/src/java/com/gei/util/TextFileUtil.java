/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gei.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author clay
 */
public class TextFileUtil {
    public static void writeTo(String pathandfilename, String data){
        String filename = parseFilename(pathandfilename)
                ,path = pathandfilename.replace(filename, "");
        writeTo(path, filename, data, false);
    }

    public static void writeTo(String path, String filename, String data, boolean append){
        File dir = new File(path);

        if (!dir.exists()){
            dir.mkdirs();
        }

        if (dir.exists()){
            FileWriter fw = null;
            BufferedWriter out = null;
            try {
                File file = new File(path + File.separator + filename);
                file.setExecutable(true);
                file.setReadable(true);
                file.setWritable(true);
                fw = new FileWriter(file, append);
                out = new BufferedWriter(fw);
                out.write(data);
                out.close();
            }
            catch (IOException ex){
                Logger.getLogger(TextFileUtil.class.getName()).log(Level.WARNING, ex.getMessage());
            }
            finally {
                try {fw.close();} catch (Exception ex) {}
            }
        }
    }

    private static String parsePath(String pathandfilename){
        String path = "";

        if (pathandfilename.contains("/")){
            path = pathandfilename.substring(0,pathandfilename.lastIndexOf("/"));
        }
        else if (pathandfilename.contains("\\")){
            path = pathandfilename.substring(0,pathandfilename.lastIndexOf("\\"));
        }

        return path;
    }

    private static String parseFilename(String pathandfilename){
        if (pathandfilename.lastIndexOf("/") != -1){
            pathandfilename = parseFilename(pathandfilename.substring(pathandfilename.lastIndexOf("/")+1));
        }
        else if (pathandfilename.lastIndexOf("\\") != -1){
            pathandfilename = parseFilename(pathandfilename.substring(pathandfilename.lastIndexOf("\\")+1));
        }

        return pathandfilename;
    }

    public static void appendTo(String pathandfilename, String data){
        String filename = parseFilename(pathandfilename)
                ,path = pathandfilename.replace(filename, "");
        writeTo(path, filename, data, true);
    }

    public static String readFrom(String pathandfilename){
        return readFrom(pathandfilename, StandardCharsets.UTF_8);
    }

    public static String readFrom(String pathandfilename, Charset encode)
    {
        String result = "";
        try {
            Path path = Paths.get(pathandfilename);
            Scanner scanner = new Scanner(path, encode.name());
            while (scanner.hasNextLine()){
                result += scanner.nextLine() + "\n";
            }
            scanner.close();
        }
        catch (IOException ex) {}

        if (result.length() > 0){
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
//    public static String readFrom(String pathandfilename, Charset encode)
//    {
//        int bufferSize = 1024 * 100;
//        byte[] result = null;
//        FileInputStream fileStrm = null;
//        FileChannel fileChannel = null;
//        MappedByteBuffer mapBuffer = null;
//        
//        try { 
//            fileStrm = new FileInputStream(pathandfilename);
//          fileChannel = fileStrm.getChannel();
//          Long fileSize = fileChannel.size();
//          result = new byte[fileSize.intValue()];      
//          mapBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0L, fileSize.longValue());
//          int numGet = 0;
//          int position = 0;
//          while( mapBuffer.hasRemaining( )) {
//            numGet = Math.min( mapBuffer.remaining( ), bufferSize );
//            mapBuffer.get(result, position, numGet );
//            position += numGet;
//          }
//          
//        }
//        catch(IOException ex){}
//        finally
//        {
//            if (mapBuffer != null)
//            {
//                try{mapBuffer.clear();}catch(Exception ex){}
//            }
//            if (fileChannel != null)
//            {
//                try{fileChannel.close();}catch(Exception ex){}
//            }
//            if (fileStrm != null)
//            {
//                try{fileStrm.close();}catch(IOException ex){}
//            }
//        }
//            
//        return new String(result);
//    }

    public static boolean delete(String pathandfilename){
        boolean success = false;
        File f = new File(pathandfilename);
        if (f.exists()){
            success = f.delete();
        }
        return success;
    }

    public static String getTempDir()
    {
        String tempFilePath = "";
        try
        {
            //create a temp file
            File temp = File.createTempFile("temp-file-name", ".tmp");

        //Get tempropary file path
            String absolutePath = temp.getAbsolutePath();
            tempFilePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
            temp.delete();
    	}
        catch(IOException e)
        {
//    		e.printStackTrace();
    	}

        return tempFilePath;
    }
    
//    public static void main(String[] args)
//    {
//        String[] strs = {"hello","world"};
//        String filename = "c:/tmp/test.txt";
//        for (String s : strs)
//        {
//            writeTo(filename, s);
//            System.out.printf("%s",readFrom(filename));
//        }
//        delete(filename);
//    }
}