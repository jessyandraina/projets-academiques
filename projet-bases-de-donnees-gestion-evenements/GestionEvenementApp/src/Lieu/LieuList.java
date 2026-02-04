package Lieu;

import connexion.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LieuList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public LieuList() {
        setTitle("Liste des Lieux");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        String[] colonnes = { "ID", "Nom", "Adresse", "Ville", "Capacité", "Type" };
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);

        table.setBackground(violetPastel);
        table.setGridColor(Color.GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(violetPastel);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.setBackground(new Color(200, 180, 240));
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(e -> chargerLieux());
        getRootPane().setDefaultButton(refreshButton);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(violetPastel);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(refreshButton, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        chargerLieux();
        setVisible(true);
    }

    private void chargerLieux() {
        tableModel.setRowCount(0);
        String sql = "SELECT * FROM LIEU";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] ligne = {
                        rs.getInt("id_lieu"),
                        rs.getString("nom_lieu"),
                        rs.getString("adresse"),
                        rs.getString("ville"),
                        rs.getInt("capacite"),
                        rs.getString("type_lieu")
                };
                tableModel.addRow(ligne);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new LieuList();
    }
}
