package Inscription;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class InscriptionCreate extends JFrame {

    private JComboBox<String> participantBox, evenementBox;
    private JTextField dateField, modePaiementField, montantField, statutField;

    private HashMap<String, Integer> participantMap = new HashMap<>();
    private HashMap<String, Integer> evenementMap = new HashMap<>();

    public InscriptionCreate() {
        setTitle("Ajouter une Inscription");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
        panel.setBackground(violetPastel);

        panel.add(new JLabel("Participant :"));
        participantBox = new JComboBox<>();
        panel.add(participantBox);

        panel.add(new JLabel("Événement :"));
        evenementBox = new JComboBox<>();
        panel.add(evenementBox);

        panel.add(new JLabel("Date d'inscription (YYYY-MM-DD) :"));
        dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Mode de paiement :"));
        modePaiementField = new JTextField();
        panel.add(modePaiementField);

        panel.add(new JLabel("Montant payé :"));
        montantField = new JTextField();
        panel.add(montantField);

        panel.add(new JLabel("Statut :"));
        statutField = new JTextField();
        panel.add(statutField);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(200, 180, 240));
        panel.add(saveButton);

        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(e -> enregistrerInscription());

        add(panel);
        chargerParticipants();
        chargerEvenements();
        setVisible(true);
    }

    private void chargerParticipants() {
        String sql = "SELECT id_participant, nom_participant, prenom_participant FROM PARTICIPANT";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_participant");
                String nomComplet = rs.getString("nom_participant") + " " + rs.getString("prenom_participant");
                participantBox.addItem(nomComplet);
                participantMap.put(nomComplet, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement participants : " + e.getMessage());
        }
    }

    private void chargerEvenements() {
        String sql = "SELECT id_evenement, nom_evenement FROM EVENEMENT";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_evenement");
                String nom = rs.getString("nom_evenement");
                evenementBox.addItem(nom);
                evenementMap.put(nom, id);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement événements : " + e.getMessage());
        }
    }

    private void enregistrerInscription() {
        try {
            int idParticipant = participantMap.get((String) participantBox.getSelectedItem());
            int idEvenement = evenementMap.get((String) evenementBox.getSelectedItem());

            String sql = "INSERT INTO INSCRIPTION (id_participant, id_evenement, date_inscription, mode_paiement, montant_paye, statut) VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idParticipant);
                stmt.setInt(2, idEvenement);
                stmt.setString(3, dateField.getText());
                stmt.setString(4, modePaiementField.getText());
                stmt.setDouble(5, Double.parseDouble(montantField.getText()));
                stmt.setString(6, statutField.getText());

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Inscription ajoutée avec succès !");
                clearFields();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Montant payé doit être un nombre valide.");
        }
    }

    private void clearFields() {
        dateField.setText("");
        modePaiementField.setText("");
        montantField.setText("");
        statutField.setText("");
    }

    public static void main(String[] args) {
        new InscriptionCreate();
    }
}
