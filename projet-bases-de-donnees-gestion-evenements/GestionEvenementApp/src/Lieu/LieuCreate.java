package Lieu;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LieuCreate extends JFrame {

    private JTextField nomField, adresseField, villeField, capaciteField, typeField;

    public LieuCreate() {
        setTitle("Ajouter un Lieu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBackground(violetPastel);

        panel.add(new JLabel("Nom du lieu :"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Adresse :"));
        adresseField = new JTextField();
        panel.add(adresseField);

        panel.add(new JLabel("Ville :"));
        villeField = new JTextField();
        panel.add(villeField);

        panel.add(new JLabel("Capacité :"));
        capaciteField = new JTextField();
        panel.add(capaciteField);

        panel.add(new JLabel("Type de lieu :"));
        typeField = new JTextField();
        panel.add(typeField);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(200, 180, 240));
        panel.add(saveButton);

        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(e -> enregistrerLieu());

        add(panel);
        setVisible(true);
    }

    private void enregistrerLieu() {
        String sql = "INSERT INTO LIEU (nom_lieu, adresse, ville, capacite, type_lieu) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomField.getText());
            stmt.setString(2, adresseField.getText());
            stmt.setString(3, villeField.getText());
            stmt.setInt(4, Integer.parseInt(capaciteField.getText()));
            stmt.setString(5, typeField.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Lieu ajouté avec succès !");
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La capacité doit être un nombre entier.");
        }
    }

    private void clearFields() {
        nomField.setText("");
        adresseField.setText("");
        villeField.setText("");
        capaciteField.setText("");
        typeField.setText("");
    }

    public static void main(String[] args) {
        new LieuCreate();
    }
}
