package Inscription;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class InscriptionModify extends JFrame {

    private JComboBox<String> participantBox, evenementBox;
    private JTextField dateField, modePaiementField, montantField, statutField;
    private JButton loadButton, updateButton;

    private HashMap<String, Integer> participantMap = new HashMap<>();
    private HashMap<String, Integer> evenementMap = new HashMap<>();

    public InscriptionModify() {
        setTitle("Modifier une Inscription");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
        panel.setBackground(violetPastel);

        panel.add(new JLabel("Participant :"));
        participantBox = new JComboBox<>();
        panel.add(participantBox);

        panel.add(new JLabel("Événement :"));
        evenementBox = new JComboBox<>();
        panel.add(evenementBox);

        loadButton = new JButton("Charger");
        loadButton.setBackground(new Color(200, 180, 240));
        panel.add(loadButton);

        panel.add(new JLabel("Date d'inscription :"));
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

        updateButton = new JButton("Mettre à jour");
        updateButton.setBackground(new Color(200, 180, 240));
        panel.add(updateButton);

        getRootPane().setDefaultButton(updateButton);

        loadButton.addActionListener(e -> chargerInscription());
        updateButton.addActionListener(e -> mettreAJourInscription());

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

    private void chargerInscription() {
        try {
            int idParticipant = participantMap.get((String) participantBox.getSelectedItem());
            int idEvenement = evenementMap.get((String) evenementBox.getSelectedItem());

            String sql = "SELECT * FROM INSCRIPTION WHERE id_participant = ? AND id_evenement = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, idParticipant);
                stmt.setInt(2, idEvenement);

                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    dateField.setText(rs.getString("date_inscription"));
                    modePaiementField.setText(rs.getString("mode_paiement"));
                    montantField.setText(String.valueOf(rs.getDouble("montant_paye")));
                    statutField.setText(rs.getString("statut"));
                } else {
                    JOptionPane.showMessageDialog(this, "Aucune inscription trouvée.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void mettreAJourInscription() {
        try {
            int idParticipant = participantMap.get((String) participantBox.getSelectedItem());
            int idEvenement = evenementMap.get((String) evenementBox.getSelectedItem());

            String sql = "UPDATE INSCRIPTION SET date_inscription = ?, mode_paiement = ?, montant_paye = ?, statut = ? "
                    +
                    "WHERE id_participant = ? AND id_evenement = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, dateField.getText());
                stmt.setString(2, modePaiementField.getText());
                stmt.setDouble(3, Double.parseDouble(montantField.getText()));
                stmt.setString(4, statutField.getText());
                stmt.setInt(5, idParticipant);
                stmt.setInt(6, idEvenement);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Inscription mise à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Mise à jour échouée.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new InscriptionModify();
    }
}
