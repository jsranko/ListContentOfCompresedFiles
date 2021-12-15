package de.sranko_informatik.si_ListContentOfCompressedFiles;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;
import java.util.jar.*;

public class JarDir {

    public static void processEntry(JarEntry je, fruehlingDatei fdDatei, String className) {
        if (className != null & !je.getName().toUpperCase().contains(className.toUpperCase())){
            return;
        }
        fdDatei.addInhaltEntry(je.getName());
        //System.out.println(je.getName());
    }

    public static void processDirectory(JarFile jf, JarEntry entry, fruehlingDatei fdDatei, String className) throws IOException  {
        JarInputStream jis = new JarInputStream(jf.getInputStream(entry));
        JarEntry je;
        try {
            while ((je = jis.getNextJarEntry()) != null) {
                if (je.isDirectory()) {
                    processDirectory(jf, je, fdDatei, className);
                } else {
                    processEntry(je, fdDatei, className);
                }
            }
        } finally {
            jis.close();
        }
    }

    public static fruehlingDatei processJarFile(String workDir, List<File> jarFiles, File file, String className) {

        JarFile jf = null;
        try {
            jf = new JarFile(file.getAbsoluteFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

        fruehlingDatei fdDatei = new fruehlingDatei(file.getName(), file.getAbsolutePath());
        Enumeration<JarEntry> iter = jf.entries();
        while (iter.hasMoreElements()) {
            JarEntry entry = iter.nextElement();
            if(entry.isDirectory()){
                try {
                    processDirectory(jf, entry, fdDatei, className);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(entry.getName().toUpperCase().contains(".JAR")){
                try {
                    jarFiles.add(convertInputStreamToFile(workDir, entry.getName(), jf.getInputStream(entry)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(entry.getName().toUpperCase().contains(".ZIP")){
                try {
                    jarFiles.add(convertInputStreamToFile(workDir, entry.getName(), jf.getInputStream(entry)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                processEntry(entry, fdDatei, className);
            }

        }
        if (fdDatei.isInhaltEmpty()) {
            return null;
        } else {
            return fdDatei;
        }
    }

    private static File convertInputStreamToFile(String workDir, String name, InputStream inputStream) {

        File targetFile = null;
        try {

            File wDir = null;
            if (workDir != null) {
                wDir = new File(workDir);
                if (!wDir.exists()) {
                    wDir.mkdir();
                }
            }

            targetFile = new File(workDir.concat(File.separator).concat(name));

            FileUtils.copyInputStreamToFile(inputStream, targetFile);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return targetFile;
    }
}