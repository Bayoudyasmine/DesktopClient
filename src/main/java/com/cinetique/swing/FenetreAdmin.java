package com.cinetique.swing;

import com.cinetique.model.CD;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class FenetreAdmin extends JFrame {
    private AdminController adminController;
    private JButton ajouterBtn;
    private JButton modifierBtn;
    private JButton supprimerBtn;
    private JList<String> cdList;
    private DefaultListModel<String> listModel;

    public FenetreAdmin() {
        try {
            adminController = new AdminController();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur de connexion au serveur EJB : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Gestion des CDs");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Création de la liste et du modèle de liste
        listModel = new DefaultListModel<>();
        cdList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(cdList);

        // Création des boutons
        ajouterBtn = new JButton("Ajouter CD");
        modifierBtn = new JButton("Modifier CD");
        supprimerBtn = new JButton("Supprimer CD");

        // Panel de boutons
        JPanel panel = new JPanel();
        panel.add(ajouterBtn);
        panel.add(modifierBtn);
        panel.add(supprimerBtn);

        // Ajout des composants à la fenêtre
        add(scrollPane, "Center");
        add(panel, "South");

        // Rafraîchissement initial de la liste
        rafraichirListe();

        // Gestion de l'ajout d'un nouveau CD
        ajouterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CD nouveauCD = new CD();
                String titre = JOptionPane.showInputDialog("Titre du CD :");
                if (titre == null || titre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Le titre du CD ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String artiste = JOptionPane.showInputDialog("Artiste du CD :");
                if (artiste == null || artiste.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "L'artiste du CD ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nouveauCD.setTitre(titre);
                nouveauCD.setArtiste(artiste);
                nouveauCD.setDisponible(true);
                adminController.ajouterCD(nouveauCD);
                rafraichirListe();
            }
        });

        // Gestion de la modification d'un CD existant
        modifierBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cdList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un CD à modifier", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String titre = JOptionPane.showInputDialog("Nouveau titre du CD :");
                if (titre == null || titre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Le titre ne peut pas être vide", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<CD> cds = adminController.getAllCDs();
                CD selectedCD = cds.get(selectedIndex);
                selectedCD.setTitre(titre);
                adminController.modifierCD(selectedCD);
                rafraichirListe();
            }
        });

        // Gestion de la suppression d'un CD
        supprimerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = cdList.getSelectedIndex();
                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(null, "Veuillez sélectionner un CD à supprimer", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<CD> cds = adminController.getAllCDs();
                CD selectedCD = cds.get(selectedIndex);
                adminController.supprimerCD(selectedCD.getId());
                rafraichirListe();
            }
        });
    }

    // Méthode pour rafraîchir la liste des CDs affichée
    private void rafraichirListe() {
        List<CD> cds = adminController.getAllCDs();
        listModel.clear();
        listModel.addAll(cds.stream().map(cd -> cd.getTitre() + " - " + cd.getArtiste()).collect(Collectors.toList()));
    }

    // Méthode principale pour exécuter l'application Swing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FenetreAdmin().setVisible(true));
    }
}
