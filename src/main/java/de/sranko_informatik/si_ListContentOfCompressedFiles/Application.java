
package de.sranko_informatik.si_ListContentOfCompressedFiles;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.cli.*;

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

        String dirToList = cmd.getOptionValue("scann");
        String outputFilePath = cmd.getOptionValue("output");
        String className = cmd.getOptionValue("class");

        ListDirectoryRecursive test = new ListDirectoryRecursive();
        //String dirToList = System.getProperty("user.home") + File.separator + "Documents";

        System.out.println("Jar Dateien werden gesucht ...");
        List<File> jarFiles = test.listDirectory(dirToList, 0);
        System.out.println("Es sind ".concat(Integer.toString(jarFiles.size())).concat(" jar Dateien gefunden worden."));

        System.out.println("Jar Dateien werden analysiert ...");
        List<fruehlingDatei> fdFiles = new ArrayList<fruehlingDatei>();

        for (File jarFile : jarFiles) {
            fdFiles.add(JarDir.processJarFile(jarFile, className));
        }        //while (fdFiles.remove(null));
        fdFiles.removeIf(Objects::isNull);
        System.out.println("Es sind ".concat(Integer.toString(fdFiles.size())).concat(" jar Dateien mit ").concat(className).concat(" gefunden worden."));

        if (outputFilePath != null) {
            System.out.println("Output Datei wird erstellt ...");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer writer = new FileWriter(outputFilePath);
            gson.toJson(fdFiles, writer);
            writer.close();
            //gson.toJson(fdFiles, new FileWriter(outputFilePath));
        }

        System.out.println("Fertig!");
    }

    static CommandLine init(String[] args) {

        Options options = new Options();

        Option input = new Option("s", "scann", true, "Path to scann");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        options.addOption(output);

        Option className = new Option("c", "class", true, "search class name");
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
