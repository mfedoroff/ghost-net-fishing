# Anleitung: MySQL-Dump importieren und Benutzer anlegen

Diese Anleitung erklärt, wie die MySQL-Dump-Datei `database.mysql` für die Datenbank `ghostnetfishing` importiert werden kann. 
Für die erfolgreiche Benutzung der Anwendung muss ebenfalls der Benutzer `ghostnetuser` mit den entsprechenden Berechtigungen anlegt werden.

Die `database.mysql` ist im Hauptverzeichnis dieses Projekts zu finden.

## Voraussetzungen
- MySQL ist installiert (verwendete Version: `9.3.0 homebrew`)
- Zugriff auf die MySQL-Server-Konsole
- Die MySQL-Anmeldeinformationen für den Administrator (root) müssen verfügbar sein.

## Installationsschritte
### 1. MySQL-Dump importieren

1. **Kommandozeile öffnen und folgenden Befehl eingeben, um sich als root-Benutzer anzumelden:**
    ```
    mysql -u root -p
    ```
2. **Datenbank `ghostnetfishing` erstellen (falls noch nicht vorhanden):**
    ``` sql
    CREATE DATABASE ghostnetfishing;
    ```
3. **Datenbankdump einlesen:**  
    Um den Datenbankdump einzulsen muss zunächst der MySQL Client mit dem folgenden Befehl beendet werden:
   ``` sql
   Exit;
    ```
    Nun kann mir folgendem Befehl die Dump-Datei eingelesen werden. Der Pfad muss entsprechend angepasst werden.  
    ```
    mysql -u root -p ghostnetfishing < /Pfad/zur/database.sql    
    ```
### 2. Benutzer `ghostnetuser` anlegen und Berechtigungen zuweisen

1. **Erneutes starten des MySQL-Clients:**
    ```
    mysql -u root -p
    ```
2. **Benutzer erstellen und entsprechende Berechtigungen setzen:**
    ``` sql
    CREATE USER 'ghostnetuser'@'localhost' IDENTIFIED BY 'ghostnetsql';
    GRANT ALL PRIVILEGES ON ghostnetfishing.* TO 'ghostnetuser'@'localhost';
    ```
3. **Berechtigungen wirksam setzen:**
    ``` sql
    FLUSH PRIVILEGES;
    ```
## Hinweise
Bei dem vorliegenden Projekt handelt es sich um eine Fallstudie und dient lediglich als Prototyp.
Ein Produktiveinsatz ist nicht gedacht.  

Für einen vollständigen Test der Anwendung ist ein Login innerhalb der Anwendung notwendig. 
Im Folgenden sind die Zugangsdaten aus dem Datensatz aufgelistet:

| Benutzer     | Passwort |
|--------------|----------|
| m.mustermann | 123      |