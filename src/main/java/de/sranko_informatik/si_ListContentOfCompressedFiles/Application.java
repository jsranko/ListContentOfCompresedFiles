
package de.sranko_informatik.si_ListContentOfCompressedFiles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Application {

    public static void main(String[] args) throws IOException {

        CommandLine cmd = init(args);

        String dirToList = cmd.getOptionValue("pfad");
        String outputFilePath = cmd.getOptionValue("output");
        String className = cmd.getOptionValue("search");
        String workDir = cmd.getOptionValue("workDir");

        ListDirectoryRecursive test = new ListDirectoryRecursive();
        //String dirToList = System.getProperty("user.home") + File.separator + "Documents";

        System.out.println("Jar Dateien werden gesucht ...");
        List<File> jarFiles = new ArrayList<>();;

        test.listDirectory(dirToList, 0, jarFiles);

        System.out.println("Jar Dateien werden analysiert ...");
        List<fruehlingDatei> fdFiles = new ArrayList<fruehlingDatei>();

        List<File> jarFilesLvl2 = new ArrayList<>();
        if (jarFiles.size() != 0){
            System.out.println("Level 1 -> ".concat(Integer.toString(jarFiles.size())).concat(" files ..."));
            for (File jarFile : jarFiles) {
                fdFiles.add(JarDir.processJarFile(workDir, jarFilesLvl2, jarFile, className));
            }
            fdFiles.removeIf(Objects::isNull);
        }

        List<File> jarFilesLvl3 = new ArrayList<>();
        if (jarFilesLvl2.size() != 0){
            System.out.println("Level 2 -> ".concat(Integer.toString(jarFilesLvl2.size())).concat(" files ..."));
            for (File jarFile : jarFilesLvl2) {
                fdFiles.add(JarDir.processJarFile(workDir, jarFilesLvl3, jarFile, className));
            }
            fdFiles.removeIf(Objects::isNull);
        }

        List<File> jarFilesLvl4 = new ArrayList<>();
        if (jarFilesLvl3.size() != 0){
            System.out.println("Level 3 -> ".concat(Integer.toString(jarFilesLvl3.size())).concat(" files ..."));
            for (File jarFile : jarFilesLvl3) {
                fdFiles.add(JarDir.processJarFile(workDir, jarFilesLvl4, jarFile, className));
            }
            fdFiles.removeIf(Objects::isNull);
        }

        List<File> jarFilesLvl5 = new ArrayList<>();
        if (jarFilesLvl4.size() != 0){
            System.out.println("Level 4 -> ".concat(Integer.toString(jarFilesLvl4.size())).concat(" files ..."));
            for (File jarFile : jarFilesLvl4) {
                fdFiles.add(JarDir.processJarFile(workDir, jarFilesLvl5, jarFile, className));
            }
            fdFiles.removeIf(Objects::isNull);
            System.out.println("Done. Level 5 -> ".concat(Integer.toString(jarFilesLvl4.size())).concat(" files ..."));
        }

        System.out.println("Es sind ".concat(Integer.toString(fdFiles.size())).concat(" jar Dateien mit:").concat(className).concat(" gefunden worden."));

        if (outputFilePath != null) {
            System.out.println("Output Datei ".concat(outputFilePath).concat(" wird erstellt ..."));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = new FileWriter(outputFilePath);
            gson.toJson(fdFiles, writer);
            writer.close();
            //gson.toJson(fdFiles, new FileWriter(outputFilePath));
        }

        File wDir = null;
        if (workDir != null) {
            System.out.println("Workdirectory wird gelöscht ...");
            wDir = new File(workDir);
            FileUtils.deleteDirectory(wDir);
        }

        System.out.println("Fertig!");
    }

    static CommandLine init(String[] args) {

        Options options = new Options();

        Option input = new Option("p", "pfad", true, "Path to scann");
        input.setRequired(true);
        options.addOption(input);

        Option workDir = new Option("d", "workDir", true, "Working directory");
        workDir.setRequired(true);
        options.addOption(workDir);

        Option output = new Option("o", "output", true, "output file");
        options.addOption(output);

        Option className = new Option("s", "search", true, "search class name");
        options.addOption(className);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;//not a good practice, it serves it purpose

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ListContentOfCompressedFile", options);

            System.exit(1);
        }
        return cmd;
    }

}
