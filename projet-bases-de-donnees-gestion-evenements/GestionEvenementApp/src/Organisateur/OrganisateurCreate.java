package Organisateur;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrganisateurCreate extends JFrame {

    private JTextField nomField, prenomField, courrielField, telephoneField, organismeField;

    public OrganisateurCreate() {
        setTitle("Ajouter un Organisateur");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.setBackground(violetPastel);

        panel.add(new JLabel("Nom :"));
        nomField = new JTextField();
        panel.add(nomField);

        panel.add(new JLabel("Prénom :"));
        prenomField = new JTextField();
        panel.add(prenomField);

        panel.add(new JLabel("Courriel :"));
        courrielField = new JTextField();
        panel.add(courrielField);

        panel.add(new JLabel("Téléphone :"));
        telephoneField = new JTextField();
        panel.add(telephoneField);

        panel.add(new JLabel("Organisme :"));
        organismeField = new JTextField();
        panel.add(organismeField);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(200, 180, 240));
        panel.add(saveButton);

        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrerOrganisateur();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void enregistrerOrganisateur() {
        String sql = "INSERT INTO ORGANISATEUR (nom_organisateur, prenom_organisateur, courriel_organisateur, telephone_organisateur, organisme) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomField.getText());
            stmt.setString(2, prenomField.getText());
            stmt.setString(3, courrielField.getText());
            stmt.setString(4, telephoneField.getText());
            stmt.setString(5, organismeField.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Organisateur ajouté avec succès !");
            clearFields();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void clearFields() {
        nomField.setText("");
        prenomField.setText("");
        courrielField.setText("");
        telephoneField.setText("");
        organismeField.setText("");
    }

    public static void main(String[] args) {
        new OrganisateurCreate();
    }
}
