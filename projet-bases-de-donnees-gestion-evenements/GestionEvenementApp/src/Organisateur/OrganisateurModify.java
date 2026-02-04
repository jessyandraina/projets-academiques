package Organisateur;

import connexion.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganisateurModify extends JFrame {

    private JTextField idField, nomField, prenomField, courrielField, telephoneField, organismeField;
    private JButton loadButton, updateButton;

    public OrganisateurModify() {
        setTitle("Modifier un Organisateur");
        setSize(450, 400);
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
        panel.add(new JLabel("ID Organisateur :"), gbc);

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
        ajouterChamp(panel, gbc, 2, "Prénom :", prenomField = new JTextField());
        ajouterChamp(panel, gbc, 3, "Courriel :", courrielField = new JTextField());
        ajouterChamp(panel, gbc, 4, "Téléphone :", telephoneField = new JTextField());
        ajouterChamp(panel, gbc, 5, "Organisme :", organismeField = new JTextField());

        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        updateButton = new JButton("Mettre à jour");
        updateButton.setBackground(new Color(200, 180, 240));
        panel.add(updateButton, gbc);

        getRootPane().setDefaultButton(updateButton);

        loadButton.addActionListener(e -> chargerOrganisateur());
        updateButton.addActionListener(e -> mettreAJourOrganisateur());

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

    private void chargerOrganisateur() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "SELECT * FROM ORGANISATEUR WHERE id_organisateur = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    nomField.setText(rs.getString("nom_organisateur"));
                    prenomField.setText(rs.getString("prenom_organisateur"));
                    courrielField.setText(rs.getString("courriel_organisateur"));
                    telephoneField.setText(rs.getString("telephone_organisateur"));
                    organismeField.setText(rs.getString("organisme"));
                } else {
                    JOptionPane.showMessageDialog(this, "Aucun organisateur trouvé.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    private void mettreAJourOrganisateur() {
        try {
            int id = Integer.parseInt(idField.getText());
            String sql = "UPDATE ORGANISATEUR SET nom_organisateur = ?, prenom_organisateur = ?, courriel_organisateur = ?, telephone_organisateur = ?, organisme = ? WHERE id_organisateur = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, nomField.getText());
                stmt.setString(2, prenomField.getText());
                stmt.setString(3, courrielField.getText());
                stmt.setString(4, telephoneField.getText());
                stmt.setString(5, organismeField.getText());
                stmt.setInt(6, id);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Organisateur mis à jour avec succès.");
                } else {
                    JOptionPane.showMessageDialog(this, "Mise à jour échouée.");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new OrganisateurModify();
    }
}
