package de.sranko_informatik.si_ListContentOfCompressedFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

public class ListDirectoryRecursive {

    public static boolean isJarFile(File file) throws IOException {
        if (!isZipFile(file)) {
            return false;
        }
        ZipFile zip = new ZipFile(file);
        boolean manifest = zip.getEntry("META-INF/MANIFEST.MF") != null;
        zip.close();
        return manifest;
    }

    public static boolean isZipFile(File file) throws IOException {
        if(file.isDirectory()) {
            return false;
        }
        if(!file.canRead()) {
            throw new IOException("Cannot read file "+file.getAbsolutePath());
        }
        if(file.length() < 4) {
            return false;
        }
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        int test = in.readInt();
        in.close();
        return test == 0x504b0304;
    }

    public List<File> listDirectory(String dirPath, int level) {
        List<File> jarFiles = new ArrayList<>();
        File dir = new File(dirPath);
        File[] firstLevelFiles = dir.listFiles();
        if (firstLevelFiles != null && firstLevelFiles.length > 0) {
            for (File aFile : firstLevelFiles) {
                for (int i = 0; i < level; i++) {
                    //System.out.print("\t");
                }
                if (aFile.isDirectory()) {
                    //System.out.println("[" + aFile.getName() + "]");
                    listDirectory(aFile.getAbsolutePath(), level + 1);
                } else {
                    try {

                        if (isJarFile(aFile)) {
                            //System.out.println("> ".concat(aFile.getAbsolutePath()));
                            jarFiles.add(aFile);
                        }
                    } catch (IOException e) {
                        //System.out.println("!!!! ".concat(aFile.getAbsolutePath()));
                        e.printStackTrace();
                    }

                    //System.out.println(aFile.getName());
                }
            }
        }
        return jarFiles;
    }
}