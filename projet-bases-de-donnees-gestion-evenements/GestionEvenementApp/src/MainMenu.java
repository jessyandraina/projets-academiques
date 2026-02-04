import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("Gestion d'Événements - Menu Principal");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        JPanel panel = new JPanel(new GridLayout(1, 6, 10, 10));
        panel.setBackground(violetPastel);

        // Colonne Participants
        JPanel participantPanel = createSection("Participants",
                new String[] { "Créer", "Liste", "Modifier" },
                new String[] { "Participant.ParticipantCreate", "Participant.ParticipantList",
                        "Participant.ParticipantModify" });

        // Colonne Événements
        JPanel evenementPanel = createSection("Événements",
                new String[] { "Créer", "Liste", "Modifier" },
                new String[] { "Evenement.EvenementCreate", "Evenement.EvenementList", "Evenement.EvenementModify" });

        // Colonne Organisateurs
        JPanel organisateurPanel = createSection("Organisateurs",
                new String[] { "Créer", "Liste", "Modifier" },
                new String[] { "Organisateur.OrganisateurCreate", "Organisateur.OrganisateurList",
                        "Organisateur.OrganisateurModify" });

        // Colonne Lieux
        JPanel lieuPanel = createSection("Lieux",
                new String[] { "Créer", "Liste", "Modifier" },
                new String[] { "Lieu.LieuCreate", "Lieu.LieuList", "Lieu.LieuModify" });

        // Colonne Catégories
        JPanel categoriePanel = createSection("Catégories",
                new String[] { "Créer", "Liste", "Modifier" },
                new String[] { "Categorie.CategorieCreate", "Categorie.CategorieList", "Categorie.CategorieModify" });

        // Colonne Inscriptions
        JPanel inscriptionPanel = createSection("Inscriptions",
                new String[] { "Créer", "Liste", "Modifier" },
                new String[] { "Inscription.InscriptionCreate", "Inscription.InscriptionList",
                        "Inscription.InscriptionModify" });

        // Ajouter les colonnes au panel principal
        panel.add(participantPanel);
        panel.add(evenementPanel);
        panel.add(organisateurPanel);
        panel.add(lieuPanel);
        panel.add(categoriePanel);
        panel.add(inscriptionPanel);

        // Layout global
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        // Bouton Quitter en bas à droite
        JButton quitButton = new JButton("Quitter");
        quitButton.setBackground(new Color(200, 180, 240));
        quitButton.addActionListener(e -> System.exit(0));

        JPanel quitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        quitPanel.setBackground(violetPastel);
        quitPanel.add(quitButton);

        add(quitPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createSection(String titre, String[] boutons, String[] classes) {
        JPanel section = new JPanel();
        section.setLayout(new BoxLayout(section, BoxLayout.Y_AXIS));
        section.setBackground(new Color(230, 220, 250));

        JLabel label = new JLabel(titre, SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        section.add(label);

        Dimension buttonSize = new Dimension(120, 30); // Taille uniforme pour tous les boutons

        for (int i = 0; i < boutons.length; i++) {
            JButton button = new JButton(boutons[i]);
            button.setBackground(new Color(200, 180, 240));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            button.setMaximumSize(buttonSize);
            button.setPreferredSize(buttonSize);

            String className = classes[i];
            button.addActionListener(e -> {
                try {
                    Class.forName(className).getDeclaredMethod("main", String[].class).invoke(null,
                            (Object) new String[] {});
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                }
            });
            section.add(Box.createRigidArea(new Dimension(0, 5))); // Espace entre les boutons
            section.add(button);
        }

        return section;
    }

    public static void main(String[] args) {
        new MainMenu();
    }
}
