package Participant;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipantModify extends JFrame {

    private JTextField idField, nomField, prenomField, courrielField, telephoneField, professionField, nbBilletField;
    private JButton updateButton;

    public ParticipantModify() {
        setTitle("Modifier un Participant");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        setLayout(new GridLayout(9, 2, 5, 5));

        add(new JLabel("ID Participant à modifier :"));
        idField = new JTextField();
        add(idField);

        JButton loadButton = new JButton("Charger");
        loadButton.setBackground(new Color(200, 180, 240));
        add(loadButton);
        add(new JLabel("")); // Espace vide

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

        updateButton = new JButton("Mettre à jour");
        updateButton.setBackground(new Color(200, 180, 240));
        add(updateButton);
        add(new JLabel(""));

        getRootPane().setDefaultButton(updateButton);

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerParticipant();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mettreAJourParticipant();
            }
        });

        setVisible(true);
    }

    private void chargerParticipant() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "SELECT * FROM PARTICIPANT WHERE id_participant = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    nomField.setText(rs.getString("nom_participant"));
                    prenomField.setText(rs.getString("prenom_participant"));
                    courrielField.setText(rs.getString("courriel_participant"));
                    telephoneField.setText(rs.getString("telephone_participant"));
                    professionField.setText(rs.getString("profession"));
                    nbBilletField.setText(String.valueOf(rs.getInt("nombre_billet")));
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun participant trouvé avec cet ID.");
                }

            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "L'ID doit être un nombre valide.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void mettreAJourParticipant() {
        try {
            int id = Integer.parseInt(idField.getText());
            String nom = nomField.getText();
            String prenom = prenomField.getText();
            String courriel = courrielField.getText();
            String telephone = telephoneField.getText();
            String profession = professionField.getText();
            int nbBillet = Integer.parseInt(nbBilletField.getText());

            String sql = "UPDATE PARTICIPANT SET nom_participant = ?, prenom_participant = ?, courriel_participant = ?, "
                    +
                    "telephone_participant = ?, profession = ?, nombre_billet = ? WHERE id_participant = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nom);
                stmt.setString(2, prenom);
                stmt.setString(3, courriel);
                stmt.setString(4, telephone);
                stmt.setString(5, profession);
                stmt.setInt(6, nbBillet);
                stmt.setInt(7, id);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Participant mis à jour avec succès !");
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun participant trouvé avec cet ID.");
                }

            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs valides.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new ParticipantModify();
    }
}
