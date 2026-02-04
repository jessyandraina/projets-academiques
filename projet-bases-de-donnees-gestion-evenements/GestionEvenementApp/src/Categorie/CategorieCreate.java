package Categorie;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategorieCreate extends JFrame {

    private JTextField nomField, descriptionField, publicField, couleurField;

    public CategorieCreate() {
        setTitle("Ajouter une Catégorie");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBackground(violetPastel);

        panel.add(new JLabel("Nom de la catégorie :"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Description :"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        panel.add(new JLabel("Public cible :"));
        publicField = new JTextField();
        panel.add(publicField);

        panel.add(new JLabel("Couleur d'affichage :"));
        couleurField = new JTextField();
        panel.add(couleurField);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(200, 180, 240));
        panel.add(saveButton);

        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(e -> enregistrerCategorie());

        add(panel);
        setVisible(true);
    }

    private void enregistrerCategorie() {
        String sql = "INSERT INTO CATEGORIE (nom_categorie, description, public_cible, couleur_affichage) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomField.getText());
            stmt.setString(2, descriptionField.getText());
            stmt.setString(3, publicField.getText());
            stmt.setString(4, couleurField.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Catégorie ajoutée avec succès !");
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomField.setText("");
        descriptionField.setText("");
        publicField.setText("");
        couleurField.setText("");
    }

    public static void main(String[] args) {
        new CategorieCreate();
    }
}
