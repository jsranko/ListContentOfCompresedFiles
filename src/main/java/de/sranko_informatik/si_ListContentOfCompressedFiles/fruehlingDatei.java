package de.sranko_informatik.si_ListContentOfCompressedFiles;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fruehlingDatei {
    private String name;
    private String pfad;
    private List<String> inhalt;

    public fruehlingDatei(String name, String pfad) {
        this.name = name;
        this.pfad = pfad;
        this.inhalt = new ArrayList<String>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPfad() {
        return pfad;
    }

    public void setPfad(String pfad) {
        this.pfad = pfad;
    }

    public List<String> getInhalt() {
        return inhalt;
    }

    public void setInhalt(List<String> inhalt) {
        this.inhalt = inhalt;
    }

    public void addInhaltEntry(String entry) {
        this.inhalt.add(entry);
    }

    public boolean isInhaltEmpty (){
        return this.inhalt.isEmpty();
    }

}
