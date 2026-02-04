package Organisateur;

import connexion.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrganisateurList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public OrganisateurList() {
        setTitle("Liste des Organisateurs");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        String[] colonnes = { "ID", "Nom", "Prénom", "Courriel", "Téléphone", "Organisme" };
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);

        table.setBackground(violetPastel);
        table.setGridColor(Color.GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(violetPastel);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.setBackground(new Color(200, 180, 240));
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(e -> chargerOrganisateurs());
        getRootPane().setDefaultButton(refreshButton);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(violetPastel);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(refreshButton, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        chargerOrganisateurs();
        setVisible(true);
    }

    private void chargerOrganisateurs() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM ORGANISATEUR";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] ligne = {
                        rs.getInt("id_organisateur"),
                        rs.getString("nom_organisateur"),
                        rs.getString("prenom_organisateur"),
                        rs.getString("courriel_organisateur"),
                        rs.getString("telephone_organisateur"),
                        rs.getString("organisme")
                };
                tableModel.addRow(ligne);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur de chargement : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new OrganisateurList();
    }
}
