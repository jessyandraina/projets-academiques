import java.io.*;
import java.net.*;
import java.util.*;

public class ServerMain {
    public static Map<String, String> fileList = new HashMap<>(); // nomFichier -> "local" ou "ip:port"

    public static void main(String[] args) {
        int port = 4000; // par defaut
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur en ecoute sur le port " + port);

            chargerFilesList();
            chargerPeersList();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket);
                handler.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du lancement du serveur : " + e.getMessage());
        }
    }

    private static void chargerFilesList() {
        File fichier = new File("files_list.txt");
        if (!fichier.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.trim().split(" ");
                if (parts.length == 1) {
                    fileList.put(parts[0], "local");
                } else if (parts.length == 2) {
                    fileList.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des fichiers : " + e.getMessage());
        }
    }

    private static void chargerPeersList() {
        File fichier = new File("peers_list.txt");
        if (!fichier.exists()) return;

        System.out.println("Verification des pairs...");
        try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                String[] parts = ligne.trim().split(":");
                if (parts.length == 2) {
                    String ip = parts[0];
                    int port = Integer.parseInt(parts[1]);
                    boolean actif = pingPeer(ip, port);
                    System.out.println((actif ? "Actif" : "Inactif") + " : " + ip + ":" + port);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des pairs : " + e.getMessage());
        }
    }

    public static boolean pingPeer(String ip, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(ip, port), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
