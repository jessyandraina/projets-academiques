package Inscription;

import connexion.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InscriptionList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public InscriptionList() {
        setTitle("Liste des Inscriptions");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color violetPastel = new Color(230, 220, 250);
        getContentPane().setBackground(violetPastel);

        String[] colonnes = { "Participant", "Événement", "Date", "Paiement", "Montant", "Statut" };
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);

        table.setBackground(violetPastel);
        table.setGridColor(Color.GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(violetPastel);

        JButton refreshButton = new JButton("Rafraîchir");
        refreshButton.setBackground(new Color(200, 180, 240));
        refreshButton.setFocusPainted(false);

        refreshButton.addActionListener(e -> chargerInscriptions());
        getRootPane().setDefaultButton(refreshButton);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(violetPastel);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(refreshButton, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        chargerInscriptions();
        setVisible(true);
    }

    private void chargerInscriptions() {
        tableModel.setRowCount(0);
        String sql = "SELECT P.nom_participant, P.prenom_participant, E.nom_evenement, I.date_inscription, I.mode_paiement, I.montant_paye, I.statut "
                +
                "FROM INSCRIPTION I " +
                "JOIN PARTICIPANT P ON I.id_participant = P.id_participant " +
                "JOIN EVENEMENT E ON I.id_evenement = E.id_evenement";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] ligne = {
                        rs.getString("nom_participant") + " " + rs.getString("prenom_participant"),
                        rs.getString("nom_evenement"),
                        rs.getString("date_inscription"),
                        rs.getString("mode_paiement"),
                        rs.getDouble("montant_paye"),
                        rs.getString("statut")
                };
                tableModel.addRow(ligne);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new InscriptionList();
    }
}
