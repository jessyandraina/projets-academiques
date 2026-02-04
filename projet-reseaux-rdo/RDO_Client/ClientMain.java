import java.io.*;
import java.net.*;
import java.util.*;
import java.util.Base64;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        boolean estEnregistre = false;

        afficherPeersActifs();

        try {
            System.out.print("Entrez l'adresse IP du serveur : ");
            String serverIp = scanner.nextLine();

            System.out.print("Entrez le port du serveur : ");
            int serverPort = Integer.parseInt(scanner.nextLine());

            InetAddress serverAddress = InetAddress.getByName(serverIp);
            socket = new Socket(serverAddress, serverPort);

            System.out.println("Connecte au serveur a " + serverIp + ":" + serverPort);
            System.out.println("Veuillez vous enregistrer avant toute commande.");
            System.out.println(">>> REGISTER|votre_ip|  ou EXIT");

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

            while (true) {
                System.out.print(">>> ");
                String message = scanner.nextLine().trim();
                out.println(message);
                out.flush();

                if (message.equals("EXIT")) {
                    String response = in.readLine();
                    if (response != null && response.equals("EXIT|BYE")) {
                        System.out.println("Deconnexion reussie.");
                        break;
                    } else {
                        System.out.println("Reponse inattendue du serveur : " + response);
                        break;
                    }
                } else if (message.startsWith("REGISTER|")) {
                    String response = in.readLine();
                    if (response != null) {
                        if (response.startsWith("REGISTERED|")) {
                            estEnregistre = true;
                            System.out.println(response);
                            System.out.println("ENTER YOUR REQUEST (LS / WRITE / READ / EXIT)");
                        } else if (response.equals("REGISTER|ALREADY_REGISTERED")) {
                            estEnregistre = true;
                            System.out.println("Already REGISTERED");
                            System.out.println("ENTER YOUR REQUEST (LS / WRITE / READ / EXIT)");
                        } else {
                            System.out.println("Reponse du serveur : " + response);
                        }
                    } else {
                        System.out.println("Le serveur n'a pas repondu.");
                        break;
                    }
                } else if (!estEnregistre) {
                    String response = in.readLine();
                    if (response != null && response.equals("NOT_REGISTERED_YET")) {
                        System.out.println("Not Registered yet, please REGISTER or EXIT");
                    } else {
                        System.out.println("Reponse du serveur : " + response);
                    }
                } else if (message.startsWith("WRITE|")) {
                    String[] parts = message.split("\\|");
                    if (parts.length >= 3) {
                        String jeton = parts[1];
                        String nomFichier = parts[2];

                        File fichierLocal = new File(nomFichier);
                        if (!fichierLocal.exists()) {
                            System.out.println("FILE NOT FOUND, PLEASE ENTER AN EXISTING FILE");
                            System.out.println("voici la liste des fichiers du cote client:");

                            File currentDir = new File(".");
                            File[] fichiers = currentDir.listFiles();
                            if (fichiers != null) {
                                System.out.print("FILES_CLIENT|");
                                for (File f : fichiers) {
                                    if (f.isFile() && f.getName().endsWith(".txt")) {
                                        System.out.print(f.getName() + "|");
                                    }
                                }
                                System.out.println();
                            }
                            continue;
                        }

                        out.println(message);
                        out.flush();
                        String confirmation = in.readLine();

                        if ("WRITE|FILE_EXISTS".equals(confirmation)) {
                            System.out.println("Le fichier existe deja, voulez-vous le remplacer ? 1-oui, 2-non");
                            System.out.print(">>> ");
                            String choix = scanner.nextLine().trim();

                            if (choix.equals("1")) {
                                out.println("REPLACE|OUI");
                                out.flush();
                                String reponseRemplacement = in.readLine();
                                if ("WRITE|BEGIN".equals(reponseRemplacement)) {
                                    try {
                                        sendFileFragments(nomFichier, out);
                                    } catch (Exception e) {
                                        System.out.println("Erreur lors de l'envoi du fichier : " + e.getMessage());
                                    }
                                }
                            } else {
                                out.println("REPLACE|NON");
                                out.flush();
                                System.out.println("ENTER YOUR REQUEST (LS / WRITE / READ / EXIT)");
                            }
                        } else if ("WRITE|BEGIN".equals(confirmation)) {
                            try {
                                sendFileFragments(nomFichier, out);
                            } catch (Exception e) {
                                System.out.println("Erreur lors de l'envoi du fichier : " + e.getMessage());
                            }
                        } else {
                            System.out.println("Reponse du serveur : " + confirmation);
                        }
                    } else {
                        System.out.println("Format invalide. Utilisez : WRITE|jeton|nom_fichier|");
                    }
                } else if (message.startsWith("READ|")) {
                    try {
                        lireEtSauvegarderFichier(in);
                    } catch (Exception e) {
                        System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                    }
                } else {
                    String response = in.readLine();
                    if (response != null) {
                        if (response.startsWith("READ-REDIRECT|")) {
                            String[] parts = response.split("\\|");
                            if (parts.length == 4) {
                                String ip = parts[1];
                                int port = Integer.parseInt(parts[2]);
                                String jeton = parts[3];

                                System.out.println("Reponse du serveur : " + response);
                                System.out.println("Un READ-REDIRECT a ete emis. Veuillez reessayer avec le serveur distant.");

                                System.out.print(">>> ");
                                String nouvelleCommande = scanner.nextLine();
                                if (!nouvelleCommande.startsWith("READ|")) {
                                    System.out.println("Commande invalide apres redirection. Attendu : READ|jeton|nom_fichier|");
                                    continue;
                                }

                                try (Socket redirSocket = new Socket(ip, port)) {
                                    PrintWriter outRedir = new PrintWriter(new OutputStreamWriter(redirSocket.getOutputStream(), "UTF-8"), true);
                                    BufferedReader inRedir = new BufferedReader(new InputStreamReader(redirSocket.getInputStream(), "UTF-8"));

                                    outRedir.println(nouvelleCommande);
                                    outRedir.flush();

                                    try {
                                        lireEtSauvegarderFichier(inRedir);
                                    } catch (Exception e) {
                                        System.out.println("Erreur lors de la lecture du fichier : " + e.getMessage());
                                    }

                                } catch (IOException e) {
                                    System.out.println("Erreur lors de la connexion au serveur distant : " + e.getMessage());
                                }
                            }
                        } else {
                            System.out.println("Reponse du serveur : " + response);
                            System.out.println("ENTER YOUR REQUEST (LS / WRITE / READ / EXIT)");
                        }
                    } else {
                        System.out.println("Le serveur n'a pas repondu.");
                        break;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Erreur : Impossible de se connecter au serveur.");
            System.out.println("Detail : " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Le port doit etre un nombre entier.");
        } finally {
            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture du socket : " + e.getMessage());
            }
        }
    }

    private static void sendFileFragments(String nomFichier, PrintWriter out) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(nomFichier));
        StringBuilder contenu = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            contenu.append(line).append("\n");
        }
        reader.close();

        String texte = contenu.toString();
        int offset = 0;
        int index = 0;
        int total = texte.length();

        while (index < total) {
            int end = Math.min(index + 500, total);
            String fragmentTexte = texte.substring(index, end);

            if (fragmentTexte.length() < 500) {
                fragmentTexte += " ".repeat(500 - fragmentTexte.length());
            }

            String encodedFragment = Base64.getEncoder().encodeToString(fragmentTexte.getBytes("UTF-8"));
            int isLast = (end == total) ? 1 : 0;

            String message = "FILE|" + nomFichier + "|" + offset + "|" + isLast + "|" + encodedFragment;
            System.out.println(message);

            out.println(message);
            out.flush();

            index += 500;
            offset++;
        }

        System.out.println("Fichier envoye en fragments de 500 caracteres (encodes en base64).\nENTER YOUR REQUEST (LS / WRITE / READ / EXIT)");
        Thread.sleep(300);
    }

    private static void lireEtSauvegarderFichier(BufferedReader in) throws Exception {
        Map<Integer, String> fragments = new TreeMap<>();
        String nomFichier = null;
        boolean dernierFragmentRecu = false;

        while (!dernierFragmentRecu) {
            String ligne = in.readLine();
            if (ligne == null) break;

            if (!ligne.startsWith("FILE|")) {
                System.out.println("Reponse inattendue : " + ligne);
                return;
            }

            String[] parties = ligne.split("\\|", 5);
            if (parties.length < 5) {
                System.out.println("Fragment mal formaté : " + ligne);
                return;
            }

            nomFichier = parties[1];
            int offset = Integer.parseInt(parties[2]);
            int isLast = Integer.parseInt(parties[3]);
            String contenuBase64 = parties[4];

            fragments.put(offset, contenuBase64);

            if (isLast == 1) {
                dernierFragmentRecu = true;
            }
        }

        StringBuilder contenuComplet = new StringBuilder();
        for (String fragment : fragments.values()) {
            byte[] data = Base64.getDecoder().decode(fragment);
            contenuComplet.append(new String(data, "UTF-8"));
        }

        File dossier = new File("client_files");
        if (!dossier.exists()) dossier.mkdir();

        FileWriter writer = new FileWriter("client_files/" + nomFichier);
        writer.write(contenuComplet.toString());
        writer.close();

        System.out.println("Fichier '" + nomFichier + "' sauvegarde dans client_files/");
        System.out.println("ENTER YOUR REQUEST (LS / WRITE / READ / EXIT)");
    }

    private static void afficherPeersActifs() {
        try {
            File peerFile = new File("peers_list.txt");
            if (!peerFile.exists()) return;

            List<String> actifs = new ArrayList<>();
            List<String> inactifs = new ArrayList<>();

            BufferedReader reader = new BufferedReader(new FileReader(peerFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(":");
                if (parts.length == 2) {
                    String ip = parts[0];
                    int port = Integer.parseInt(parts[1]);

                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(ip, port), 500);
                        actifs.add(ip + ":" + port);
                    } catch (IOException e) {
                        inactifs.add(ip + ":" + port);
                    }
                }
            }
            reader.close();

            System.out.println("Peers actifs : " + actifs);
            System.out.println("Peers inactifs : " + inactifs);

        } catch (Exception e) {
            System.out.println("Erreur lors du ping des peers : " + e.getMessage());
        }
    }
}
