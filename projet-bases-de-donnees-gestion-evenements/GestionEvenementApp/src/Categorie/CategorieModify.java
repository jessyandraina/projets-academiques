package Categorie;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategorieModify extends JFrame {

    private JTextField idField, nomField, descriptionField, publicField, couleurField;
    private JButton loadButton, updateButton;

    public CategorieModify() {
        setTitle("Modifier une Catégorie");
        setSize(500, 350);
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
        panel.add(new JLabel("ID Catégorie :"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        idField = new JTextField(10);
        panel.add(idField, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        loadButton = new JButton("Charger");
        loadButton.setBackground(new Color(200, 180, 240));
        panel.add(loadButton, gbc);

        ajouterChamp(panel, gbc, 1, "Nom :", nomField = new JTextField());
        ajouterChamp(panel, gbc, 2, "Description :", descriptionField = new JTextField());
        ajouterChamp(panel, gbc, 3, "Public cible :", publicField = new JTextField());
        ajouterChamp(panel, gbc, 4, "Couleur :", couleurField = new JTextField());

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        updateButton = new JButton("Mettre à jour");
        updateButton.setBackground(new Color(200, 180, 240));
        panel.add(updateButton, gbc);

        getRootPane().setDefaultButton(updateButton);

        loadButton.addActionListener(e -> chargerCategorie());
        updateButton.addActionListener(e -> mettreAJourCategorie());

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

    private void chargerCategorie() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "SELECT * FROM CATEGORIE WHERE id_categorie = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    nomField.setText(rs.getString("nom_categorie"));
                    descriptionField.setText(rs.getString("description"));
                    publicField.setText(rs.getString("public_cible"));
                    couleurField.setText(rs.getString("couleur_affichage"));
                } else {
                    JOptionPane.showMessageDialog(this, "Aucune catégorie trouvée.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void mettreAJourCategorie() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "UPDATE CATEGORIE SET nom_categorie = ?, description = ?, public_cible = ?, couleur_affichage = ? WHERE id_categorie = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nomField.getText());
                stmt.setString(2, descriptionField.getText());
                stmt.setString(3, publicField.getText());
                stmt.setString(4, couleurField.getText());
                stmt.setInt(5, id);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Catégorie mise à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Mise à jour échouée.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new CategorieModify();
    }
}
