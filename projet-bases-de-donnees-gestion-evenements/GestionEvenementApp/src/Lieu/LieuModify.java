package Lieu;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LieuModify extends JFrame {

    private JTextField idField, nomField, adresseField, villeField, capaciteField, typeField;
    private JButton loadButton, updateButton;

    public LieuModify() {
        setTitle("Modifier un Lieu");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(violetPastel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID Lieu :"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        idField = new JTextField(10);
        panel.add(idField, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        loadButton = new JButton("Charger");
        loadButton.setBackground(new Color(200, 180, 240));
        panel.add(loadButton, gbc);

        ajouterChamp(panel, gbc, 1, "Nom du lieu :", nomField = new JTextField());
        ajouterChamp(panel, gbc, 2, "Adresse :", adresseField = new JTextField());
        ajouterChamp(panel, gbc, 3, "Ville :", villeField = new JTextField());
        ajouterChamp(panel, gbc, 4, "Capacité :", capaciteField = new JTextField());
        ajouterChamp(panel, gbc, 5, "Type de lieu :", typeField = new JTextField());

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        updateButton = new JButton("Mettre à jour");
        updateButton.setBackground(new Color(200, 180, 240));
        panel.add(updateButton, gbc);

        getRootPane().setDefaultButton(updateButton);

        loadButton.addActionListener(e -> chargerLieu());
        updateButton.addActionListener(e -> mettreAJourLieu());

        add(panel);
        setVisible(true);
    }

    private void ajouterChamp(JPanel panel, GridBagConstraints gbc, int ligne, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = ligne;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(field, gbc);
        gbc.gridwidth = 1;
    }

    private void chargerLieu() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "SELECT * FROM LIEU WHERE id_lieu = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    nomField.setText(rs.getString("nom_lieu"));
                    adresseField.setText(rs.getString("adresse"));
                    villeField.setText(rs.getString("ville"));
                    capaciteField.setText(String.valueOf(rs.getInt("capacite")));
                    typeField.setText(rs.getString("type_lieu"));
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun lieu trouvé.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void mettreAJourLieu() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "UPDATE LIEU SET nom_lieu = ?, adresse = ?, ville = ?, capacite = ?, type_lieu = ? WHERE id_lieu = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nomField.getText());
                stmt.setString(2, adresseField.getText());
                stmt.setString(3, villeField.getText());
                stmt.setInt(4, Integer.parseInt(capaciteField.getText()));
                stmt.setString(5, typeField.getText());
                stmt.setInt(6, id);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Lieu mis à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Mise à jour échouée.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LieuModify();
    }
}
