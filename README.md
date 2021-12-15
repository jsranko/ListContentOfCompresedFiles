# List content of compressed files
Java Programm sucht rekursive  Zeichenkette innerhalb ZIP/JAR Dateien.

## Install

### Set PATH variable

Extend the environment variable PATH, so that the OpenSource packages do not have to enter qualified:

```
export PATH=/QOpenSys/pkgs/bin:$PATH
```

### Install git

Opensource package **git** must be installed. For installation execute the following command:
```
yum install git maven
```

### Clone project
A local copy of the project must be created:
```
git clone https://github.com/jsranko/ListContentOfCompresedFiles.git
```

### Build project

```
cd listcontentofcompresedfiles
mvn clean package assembly:single
```

## Example 
### Use case 1
Es sollten ausgewählte Verzeichnisse in root analysiert werden.

root als aktuelles Verzeichnis setzen:
```
cd /
```
Nur für Verzeichnisse ein Analyse-Script generieren:
```
ls -ld */ | awk '{print substr($9, 1, length($9)-1)}' | awk '{str = sprintf("%s -> java -cp /OpenSource/listcontentofcompresedfiles/target/ListContentOfCompresedFiles-jar-with-dependencies.jar de/sranko_informatik/si_ListContentOfCompressedFiles/Application --scann /%s --class JndiLookup.class --output ~/DependencyTree_%s.json", $1, $1, $1); print str}'
```
Eine mögliche Ausgabe:
```
home -> java -cp /OpenSource/listcontentofcompresedfiles/target/ListContentOfCompresedFiles-jar-with-dependencies.jar de/sranko_informatik/si_ListContentOfCompressedFiles/Application --scann /home --class log4j --workDir ~/LOG4J_Analyse/wd/ --output ~/LOG4J_Analyse/dir_home.json
QOpenSys -> java -cp /OpenSource/listcontentofcompresedfiles/target/ListContentOfCompresedFiles-jar-with-dependencies.jar de/sranko_informatik/si_ListContentOfCompressedFiles/Application --scann /QOpenSys --class log4j --workDir ~/LOG4J_Analyse/wd/ --output ~/LOG4J_Analyse/dir_QOpenSys.json
QIBM -> java -cp /OpenSource/listcontentofcompresedfiles/target/ListContentOfCompresedFiles-jar-with-dependencies.jar de/sranko_informatik/si_ListContentOfCompressedFiles/Application --scann /QIBM --class log4j --workDir ~/LOG4J_Analyse/wd/ --output ~/LOG4J_Analyse/dir_QIBM.json
```
Wenn die Aufgabenstellung ist, home-Verzeichnis zu analysieren, dann muss der Java-Befehl ausgeführt werden:
```
java -cp /OpenSource/listcontentofcompresedfiles/target/ListContentOfCompresedFiles-jar-with-dependencies.jar \
     de/sranko_informatik/si_ListContentOfCompressedFiles/Application \
     --pfad /home \
     --search JndiLookup.class \
     --workDir ~/LOG4J_Analyse/wd/ \ 
     --output ~/LOG4J_Analyse/dir_home.json
```
Sollte ein Treffer gefunden werden, wird folgende Ausgabe generiert:
```
[
  {
    "name": "log4j-core-2.16.0.jar",
    "pfad": "/home/testjusr/.m2/log4j-core-2.16.0.jar",
    "inhalt": [
      "org/apache/logging/log4j/core/lookup/JndiLookup.class"
    ]
  }
```
