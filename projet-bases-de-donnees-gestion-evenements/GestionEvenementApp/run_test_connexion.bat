@echo off
cd src

echo Compilation de toutes les classes...

javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" connexion\DatabaseConnection.java

:: Participants
javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" Participant\*.java

:: Evenements
javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" Evenement\*.java

:: Organisateurs
javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" Organisateur\*.java

:: Lieux
javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" Lieu\*.java

:: Categories
javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" Categorie\*.java

:: Inscriptions
javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" Inscription\*.java

:: Main Menu
javac -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" MainMenu.java

echo Lancement du Menu Principal...
java -cp ".;C:\JavaLibs\mysql-connector-java-8.0.33.jar" MainMenu


