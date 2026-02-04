package Evenement;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EvenementModify extends JFrame {

    private JTextField idField, nomField, dateField, heureField, typeField, tarifField, lieuField, organisateurField,
            categorieField;
    private JButton loadButton, updateButton;

    public EvenementModify() {
        setTitle("Modifier un Événement");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(violetPastel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ligne 1 : ID + Charger
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID de l'Événement :"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        idField = new JTextField(15); // Champ plus large
        panel.add(idField, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        loadButton = new JButton("Charger");
        loadButton.setBackground(new Color(200, 180, 240));
        panel.add(loadButton, gbc);

        // Champs du formulaire
        ajouterChamp(panel, gbc, 1, "Nom :", nomField = new JTextField());
        ajouterChamp(panel, gbc, 2, "Date (YYYY-MM-DD) :", dateField = new JTextField());
        ajouterChamp(panel, gbc, 3, "Heure (HH:MM:SS) :", heureField = new JTextField());
        ajouterChamp(panel, gbc, 4, "Type :", typeField = new JTextField());
        ajouterChamp(panel, gbc, 5, "Tarif :", tarifField = new JTextField());
        ajouterChamp(panel, gbc, 6, "ID Lieu :", lieuField = new JTextField());
        ajouterChamp(panel, gbc, 7, "ID Organisateur :", organisateurField = new JTextField());
        ajouterChamp(panel, gbc, 8, "ID Catégorie :", categorieField = new JTextField());

        // Bouton Mettre à jour
        gbc.gridx = 1;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        updateButton = new JButton("Mettre à jour");
        updateButton.setBackground(new Color(200, 180, 240));
        panel.add(updateButton, gbc);

        getRootPane().setDefaultButton(updateButton);

        add(panel);

        loadButton.addActionListener(e -> chargerEvenement());
        updateButton.addActionListener(e -> mettreAJourEvenement());

        setVisible(true);
    }

    private void ajouterChamp(JPanel panel, GridBagConstraints gbc, int ligne, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = ligne;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(field, gbc);
        gbc.gridwidth = 1;
    }

    private void chargerEvenement() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "SELECT * FROM EVENEMENT WHERE id_evenement = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    nomField.setText(rs.getString("nom_evenement"));
                    dateField.setText(rs.getString("date_evenement"));
                    heureField.setText(rs.getString("heure"));
                    typeField.setText(rs.getString("type_evenement"));
                    tarifField.setText(String.valueOf(rs.getDouble("tarif")));
                    lieuField.setText(String.valueOf(rs.getInt("id_lieu")));
                    organisateurField.setText(String.valueOf(rs.getInt("id_organisateur")));
                    categorieField.setText(String.valueOf(rs.getInt("id_categorie")));
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun événement trouvé avec cet ID.");
                }

            }
        } catch (NumberFormatException | SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void mettreAJourEvenement() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "UPDATE EVENEMENT SET nom_evenement = ?, date_evenement = ?, heure = ?, type_evenement = ?, " +
                    "tarif = ?, id_lieu = ?, id_organisateur = ?, id_categorie = ? WHERE id_evenement = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nomField.getText());
                stmt.setString(2, dateField.getText());
                stmt.setString(3, heureField.getText());
                stmt.setString(4, typeField.getText());
                stmt.setDouble(5, Double.parseDouble(tarifField.getText()));
                stmt.setInt(6, Integer.parseInt(lieuField.getText()));
                stmt.setInt(7, Integer.parseInt(organisateurField.getText()));
                stmt.setInt(8, Integer.parseInt(categorieField.getText()));
                stmt.setInt(9, id);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Événement mis à jour !");
                } else {
                    JOptionPane.showMessageDialog(this, "Mise à jour échouée.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new EvenementModify();
    }
}
