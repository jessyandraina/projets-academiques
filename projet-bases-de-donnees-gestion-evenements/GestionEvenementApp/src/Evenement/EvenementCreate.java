package Evenement;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EvenementCreate extends JFrame {

    private JTextField nomField, dateField, heureField, typeField, tarifField, lieuField, organisateurField,
            categorieField;

    public EvenementCreate() {
        setTitle("Création d'un Événement");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        setLayout(new GridLayout(9, 2, 5, 5));

        add(new JLabel("Nom de l'événement :"));
        nomField = new JTextField();
        add(nomField);

        add(new JLabel("Date (YYYY-MM-DD) :"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Heure (HH:MM:SS) :"));
        heureField = new JTextField();
        add(heureField);

        add(new JLabel("Type d'événement :"));
        typeField = new JTextField();
        add(typeField);

        add(new JLabel("Tarif :"));
        tarifField = new JTextField();
        add(tarifField);

        add(new JLabel("ID Lieu :"));
        lieuField = new JTextField();
        add(lieuField);

        add(new JLabel("ID Organisateur :"));
        organisateurField = new JTextField();
        add(organisateurField);

        add(new JLabel("ID Catégorie :"));
        categorieField = new JTextField();
        add(categorieField);

        JButton saveButton = new JButton("Enregistrer");
        saveButton.setBackground(new Color(200, 180, 240));
        add(saveButton);
        add(new JLabel(""));

        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrerEvenement();
            }
        });

        setVisible(true);
    }

    private void enregistrerEvenement() {
        try {
            String nom = nomField.getText();
            String date = dateField.getText();
            String heure = heureField.getText();
            String type = typeField.getText();
            double tarif = Double.parseDouble(tarifField.getText());
            int idLieu = Integer.parseInt(lieuField.getText());
            int idOrganisateur = Integer.parseInt(organisateurField.getText());
            int idCategorie = Integer.parseInt(categorieField.getText());

            String sql = "INSERT INTO EVENEMENT (id_evenement, nom_evenement, date_evenement, heure, type_evenement, tarif, id_lieu, id_organisateur, id_categorie) "
                    +
                    "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nom);
                stmt.setString(2, date);
                stmt.setString(3, heure);
                stmt.setString(4, type);
                stmt.setDouble(5, tarif);
                stmt.setInt(6, idLieu);
                stmt.setInt(7, idOrganisateur);
                stmt.setInt(8, idCategorie);

                stmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Événement ajouté avec succès !");
                clearFields();

            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vérifiez les formats des champs numériques !");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    private void clearFields() {
        nomField.setText("");
        dateField.setText("");
        heureField.setText("");
        typeField.setText("");
        tarifField.setText("");
        lieuField.setText("");
        organisateurField.setText("");
        categorieField.setText("");
    }

    public static void main(String[] args) {
        new EvenementCreate();
    }
}
