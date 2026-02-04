package Evenement;

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

public class EvenementList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public EvenementList() {
        setTitle("Liste des Événements");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        // Définition des colonnes
        String[] colonnes = { "ID", "Nom", "Date", "Heure", "Type", "Tarif", "ID Lieu", "ID Organisateur",
                "ID Catégorie" };
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);

        table.setBackground(violetPastel);
        table.setGridColor(Color.GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(violetPastel);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.setBackground(new Color(200, 180, 240));
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerEvenements();
            }
        });

        getRootPane().setDefaultButton(refreshButton);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(violetPastel);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(refreshButton, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);

        chargerEvenements();

        setVisible(true);
    }

    private void chargerEvenements() {
        tableModel.setRowCount(0); // Nettoyer avant de recharger

        String sql = "SELECT * FROM EVENEMENT";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id_evenement"),
                        rs.getString("nom_evenement"),
                        rs.getDate("date_evenement"),
                        rs.getTime("heure"),
                        rs.getString("type_evenement"),
                        rs.getDouble("tarif"),
                        rs.getInt("id_lieu"),
                        rs.getInt("id_organisateur"),
                        rs.getInt("id_categorie")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new EvenementList();
    }
}
