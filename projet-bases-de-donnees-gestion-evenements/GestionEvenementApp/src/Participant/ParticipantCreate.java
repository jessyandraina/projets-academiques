package Participant;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParticipantCreate extends JFrame {

    private JTextField nomField, prenomField, courrielField, telephoneField, professionField, nbBilletField;

    public ParticipantCreate() {
        setTitle("Ajout d'un Participant");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Couleur violet pastel
        getContentPane().setBackground(new Color(230, 220, 250));

        setLayout(new GridLayout(8, 2, 5, 5));

        add(new JLabel("Nom :"));
        nomField = new JTextField();
        add(nomField);

        add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        add(prenomField);

        add(new JLabel("Courriel :"));
        courrielField = new JTextField();
        add(courrielField);

        add(new JLabel("Téléphone :"));
        telephoneField = new JTextField();
        add(telephoneField);

        add(new JLabel("Profession :"));
        professionField = new JTextField();
        add(professionField);

        add(new JLabel("Nombre de billets :"));
        nbBilletField = new JTextField();
        add(nbBilletField);

        JButton saveButton = new JButton("Enregistrer");
        add(saveButton);

        // Bouton vide pour l'alignement
        add(new JLabel(""));

        // Permet d'appuyer sur "Entrée" pour valider le formulaire
        getRootPane().setDefaultButton(saveButton);

        // Action du bouton
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrerParticipant();
            }
        });

        setVisible(true);
    }

    private void enregistrerParticipant() {
        try {
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String courriel = courrielField.getText();
            String telephone = telephoneField.getText();
            String profession = professionField.getText();
            int nbBillet = Integer.parseInt(nbBilletField.getText());

            String sql = "INSERT INTO PARTICIPANT (id_participant, nom_participant, prenom_participant, courriel_participant, telephone_participant, profession, nombre_billet) "
                    +
                    "VALUES (NULL, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                stmt.setString(3, courriel);
                stmt.setString(4, telephone);
                stmt.setString(5, profession);
                stmt.setInt(6, nbBillet);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Participant ajouté avec succès !");
                clearFields();

            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide pour le nombre de billets.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    private void clearFields() {
        nomField.setText("");
        prenomField.setText("");
        courrielField.setText("");
        telephoneField.setText("");
        professionField.setText("");
        nbBilletField.setText("");
    }

    public static void main(String[] args) {
        new ParticipantCreate();
    }
}
