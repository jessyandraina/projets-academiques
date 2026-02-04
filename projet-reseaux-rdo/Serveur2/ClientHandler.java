import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private static Map<String, String> jetons = new HashMap<>(); // ip client -> jeton
    private static Map<String, List<String>> jetonsRedirection = new HashMap<>(); // jeton -> [client, fichier]
    private boolean estEnregistre = false;
    private String adresseClient;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.adresseClient = clientSocket.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        System.out.println("Connexion de " + adresseClient);

        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);
        ) {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message recu de " + adresseClient + " : " + message);

                if (message.equals("EXIT")) {
                    out.println("EXIT|BYE");
                    break;
                }

                if (message.startsWith("TEMP-JETON|")) {
                    String[] parts = message.split("\\|");
                    if (parts.length == 4) {
                        String jeton = parts[1];
                        String ipClient = parts[2];
                        String fichier = parts[3];
                        jetonsRedirection.put(jeton, Arrays.asList(ipClient, fichier));
                        System.out.println("Jeton temporaire recu pour " + fichier + " depuis " + ipClient);
                    }
                    continue;
                }

                if (!estEnregistre && !message.startsWith("REGISTER|")) {
                    out.println("NOT_REGISTERED_YET");
                    continue;
                }

                if (message.startsWith("REGISTER|")) {
                    String[] parts = message.split("\\|");
                    if (parts.length != 2 || !message.endsWith("|")) {
                        out.println("REGISTER|INVALID_FORMAT");
                        continue;
                    }
                    String ipEnvoyee = parts[1];
                    String ipReelle = clientSocket.getInetAddress().getHostAddress();

                    if (jetons.containsKey(adresseClient)) {
                        out.println("REGISTER|ALREADY_REGISTERED");
                    } else if (!ipEnvoyee.equals(ipReelle)) {
                        out.println("REGISTER|INVALID_IP");
                    } else {
                        String jeton = genererJeton();
                        jetons.put(adresseClient, jeton);
                        estEnregistre = true;
                        out.println("REGISTERED|" + jeton + "|");
                        System.out.println("Jeton attribue a " + adresseClient + " : " + jeton);
                    }
                    continue;
                }

                if (message.startsWith("LS|")) {
                    String[] parts = message.split("\\|");
                    if (parts.length != 2 || !message.endsWith("|")) {
                        out.println("LS|INVALID_FORMAT");
                        continue;
                    }
                    String jetonRecu = parts[1];
                    if (!jetonValide(jetonRecu)) {
                        out.println("LS|UNAUTHORIZED");
                        continue;
                    }
                    StringBuilder sb = new StringBuilder("LS|").append(ServerMain.fileList.size()).append("|");
                    for (String nomFichier : ServerMain.fileList.keySet()) {
                        sb.append(nomFichier).append("|");
                    }
                    out.println(sb.toString());
                    continue;
                }

                if (message.startsWith("WRITE|")) {
                    String[] parts = message.split("\\|");
                    if (parts.length < 3) {
                        out.println("WRITE|INVALID_FORMAT");
                        continue;
                    }
                    String jeton = parts[1];
                    String nomFichier = parts[2];
                    if (!jetonValide(jeton)) {
                        out.println("WRITE|UNAUTHORIZED");
                        continue;
                    }
                    if (ServerMain.fileList.containsKey(nomFichier)) {
                        out.println("WRITE|FILE_EXISTS");
                        String decision = in.readLine();
                        if (decision != null && decision.equals("REPLACE|OUI")) {
                            out.println("WRITE|BEGIN");
                            recevoirFichier(in, nomFichier);
                        } else {
                            out.println("WRITE|CANCELLED");
                        }
                    } else {
                        out.println("WRITE|BEGIN");
                        recevoirFichier(in, nomFichier);
                    }
                    continue;
                }

                if (message.startsWith("FILE|")) {
                    continue;
                }

                if (message.startsWith("READ|")) {
                    String[] parts = message.split("\\|");
                    if (parts.length < 3) {
                        out.println("READ|INVALID_FORMAT");
                        continue;
                    }
                    String jetonRecu = parts[1];
                    String nomFichier = parts[2];

                    if (!jetonValide(jetonRecu)) {
                        if (!jetonsRedirection.containsKey(jetonRecu)) {
                            out.println("READ|UNAUTHORIZED");
                            continue;
                        } else {
                            List<String> info = jetonsRedirection.get(jetonRecu);
                            if (!info.get(0).equals(adresseClient) || !info.get(1).equals(nomFichier)) {
                                out.println("READ|UNAUTHORIZED");
                                continue;
                            }
                            envoyerFichier(out, nomFichier);
                            jetonsRedirection.remove(jetonRecu);
                            continue;
                        }
                    }

                    if (ServerMain.fileList.containsKey(nomFichier)) {
                        if (ServerMain.fileList.get(nomFichier).equals("local")) {
                            envoyerFichier(out, nomFichier);
                        } else {
                            String[] ipPort = ServerMain.fileList.get(nomFichier).split(":");
                            String ip = ipPort[0];
                            String port = ipPort[1];
                            if (ServerMain.pingPeer(ip, Integer.parseInt(port))) {
                                String jetonTemp = genererJeton();
                                jetonsRedirection.put(jetonTemp, Arrays.asList(adresseClient, nomFichier));

                                try (Socket peerSocket = new Socket(ip, Integer.parseInt(port));
                                     PrintWriter peerOut = new PrintWriter(new OutputStreamWriter(peerSocket.getOutputStream(), "UTF-8"), true)) {
                                    String jetonMessage = "TEMP-JETON|" + jetonTemp + "|" + adresseClient + "|" + nomFichier + "|";
                                    peerOut.println(jetonMessage);
                                } catch (IOException e) {
                                    System.out.println("Erreur en notifiant le serveur distant : " + e.getMessage());
                                }

                                out.println("READ-REDIRECT|" + ip + "|" + port + "|" + jetonTemp);
                            } else {
                                out.println("READ|UNAVAILABLE");
                            }
                        }
                    } else {
                        out.println("READ|NOT_FOUND");
                    }
                    continue;
                }

                out.println("ERREUR|Message inconnu");
            }
        } catch (IOException e) {
            System.out.println("Erreur avec le client : " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Deconnexion de " + adresseClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void recevoirFichier(BufferedReader in, String nomFichier) throws IOException {
        String ligne;
        StringBuilder contenu = new StringBuilder();

        while ((ligne = in.readLine()) != null) {
            if (ligne.startsWith("FILE|")) {
                String[] parts = ligne.split("\\|", 5);
                String base64 = parts[4];
                byte[] donnees = Base64.getDecoder().decode(base64);
                contenu.append(new String(donnees, "UTF-8"));

                if (Integer.parseInt(parts[3]) == 1) break;
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nomFichier), "UTF-8"))) {
            writer.write(contenu.toString());
        }

        ServerMain.fileList.put(nomFichier, "local");
        updateFileList();
        System.out.println("Fichier '" + nomFichier + "' recu et enregistre.");
    }

    private void envoyerFichier(PrintWriter out, String nomFichier) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(nomFichier));
        StringBuilder contenu = new StringBuilder();
        String ligne;
        while ((ligne = reader.readLine()) != null) {
            contenu.append(ligne).append("\n");
        }
        reader.close();

        String texte = contenu.toString();
        int offset = 0;
        int index = 0;
        int total = texte.length();

        while (index < total) {
            int end = Math.min(index + 500, total);
            String fragment = texte.substring(index, end);

            if (fragment.length() < 500) {
                fragment += " ".repeat(500 - fragment.length());
            }

            String base64 = Base64.getEncoder().encodeToString(fragment.getBytes("UTF-8"));
            int isLast = (end == total) ? 1 : 0;

            String message = "FILE|" + nomFichier + "|" + offset + "|" + isLast + "|" + base64;
            out.println(message);
            offset++;
            index += 500;
        }
    }

    private boolean jetonValide(String jeton) {
        return jetons.containsValue(jeton);
    }

    private String genererJeton() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
        }
        return sb.toString();
    }

    private void updateFileList() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("files_list.txt"), "UTF-8"))) {
            for (Map.Entry<String, String> entry : ServerMain.fileList.entrySet()) {
                String nom = entry.getKey();
                String valeur = entry.getValue();
                if (valeur.equals("local")) {
                    writer.write(nom + "\n");
                } else {
                    writer.write(nom + " " + valeur + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la mise a jour de files_list.txt : " + e.getMessage());
        }
    }
}
