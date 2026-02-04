package Participant;

import connexion.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ParticipantList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public ParticipantList() {
        setTitle("Liste des Participants");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Définition de la couleur violet pastel
        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        // Définir les colonnes du tableau
        String[] colonnes = { "ID", "Nom", "Prénom", "Courriel", "Téléphone", "Profession", "Nombre de billets" };
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);

        // Mettre le fond de la table en violet pastel
        table.setBackground(violetPastel);
        table.setGridColor(Color.GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(violetPastel);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.setBackground(new Color(200, 180, 240)); // Un violet un peu plus foncé pour le bouton
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerParticipants();
            }
        });

        getRootPane().setDefaultButton(refreshButton);

        // Layout personnalisé pour garder le fond visible
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(violetPastel);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(refreshButton, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        chargerParticipants();

        setVisible(true);
    }

    private void chargerParticipants() {
        tableModel.setRowCount(0); // Nettoyer le tableau avant de recharger

        String sql = "SELECT * FROM PARTICIPANT";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_participant"),
                        rs.getString("nom_participant"),
                        rs.getString("prenom_participant"),
                        rs.getString("courriel_participant"),
                        rs.getString("telephone_participant"),
                        rs.getString("profession"),
                        rs.getInt("nombre_billet")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ParticipantList();
    }
}
