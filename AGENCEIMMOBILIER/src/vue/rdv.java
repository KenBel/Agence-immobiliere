package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

import controleur.RdvController;

public class rdv extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    private JTextField searchField, searchField2, searchField3;
    private JTable rdvTable;
    private DefaultTableModel tableModel;

    private accueil FenetreAccueil;
    private bien FenetreBien;
    private client FenetreClient;
    private agent FenetreAgent;
    private transac FenetreTransaction;
    private rdv FenetreRdv;
    private contrat FenetreContrat;

    public rdv() {

        setTitle("Agence immobilière - Espace Rendez-vous");
        setSize(1830, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel menuPanel = createMenuPanel();
        menuPanel.setPreferredSize(new Dimension(260, getHeight()));
        add(menuPanel, BorderLayout.WEST);

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadRdvsFromDatabase();

        setVisible(true);
    }

    // ---------------------------------------------------------
    // MENU — Identique à BIEN
    // ---------------------------------------------------------
    private JPanel createMenuPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel logo = new JLabel(new ImageIcon("C:\\Users\\Kenza\\Downloads\\agence.png"));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel title = new JLabel("Agence immobilière");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(logo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(title);
        panel.add(Box.createVerticalStrut(30));

        String[] items = {
                "Tableau de bord", "Recherche de biens", "Clients",
                "Agents", "Transactions", "Rendez-vous", "Contrats"
        };

        String[] icons = {
                "C:\\Users\\Kenza\\Downloads\\acc.png",
                "C:\\Users\\Kenza\\Downloads\\bien.png",
                "C:\\Users\\Kenza\\Downloads\\client.png",
                "C:\\Users\\Kenza\\Downloads\\employe.png",
                "C:\\Users\\Kenza\\Downloads\\transac.png",
                "C:\\Users\\Kenza\\Downloads\\rdv.png",
                "C:\\Users\\Kenza\\Downloads\\contrat.png"
        };

        for (int i = 0; i < items.length; i++) {

            JButton btn = new JButton(items[i], new ImageIcon(icons[i]));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            btn.setBackground(new Color(22, 45, 104));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            btn.setFocusPainted(false);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(35, 70, 150));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(22, 45, 104));
                }
            });

            final int index = i;
            btn.addActionListener(e -> openWindow(index));

            panel.add(btn);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    private void openWindow(int i) {

        this.dispose();

        switch (i) {
            case 0: new accueil().setVisible(true); break;
            case 1: new bien().setVisible(true); break;
            case 2: new client().setVisible(true); break;
            case 3: new agent().setVisible(true); break;
            case 4: new transac().setVisible(true); break;
            case 5: new rdv().setVisible(true); break;
            case 6: new contrat().setVisible(true); break;
        }
    }

    // ---------------------------------------------------------
    // HEADER — Identique à BIEN
    // ---------------------------------------------------------
    private JPanel createHeaderPanel() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel title = new JLabel("Espace Rendez-vous Immobiliers");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 20, 0, 0));

        header.add(title, BorderLayout.WEST);

        return header;
    }

    // ---------------------------------------------------------
    // MAIN — Search + Table
    // ---------------------------------------------------------
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JLabel l1 = new JLabel("Propriétaire :");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(l1);

        searchField = createInput();
        searchPanel.add(searchField);

        JLabel l2 = new JLabel("Preneur :");
        l2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(l2);

        searchField2 = createInput();
        searchPanel.add(searchField2);

        JLabel l3 = new JLabel("Agent :");
        l3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(l3);

        searchField3 = createInput();
        searchPanel.add(searchField3);

        JButton searchBtn = new JButton("Rechercher");
        stylePrimaryButton(searchBtn);
        searchPanel.add(searchBtn);

        main.add(searchPanel, BorderLayout.NORTH);

        // TABLE
        String[] cols = { "ID", "ID Bien", "Propriétaire", "Preneur", "Date", "Heure", "Agent" };
        tableModel = new DefaultTableModel(cols, 0);

        rdvTable = new JTable(tableModel);
        rdvTable.setRowHeight(40);
        rdvTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rdvTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader head = rdvTable.getTableHeader();
        head.setFont(new Font("Segoe UI", Font.BOLD, 14));
        head.setBackground(new Color(225, 230, 245));

        JScrollPane sp = new JScrollPane(rdvTable);
        sp.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.add(sp, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> handleSearch());

        main.add(createButtonPanel(), BorderLayout.SOUTH);

        return main;
    }

    private JTextField createInput() {
        JTextField t = new JTextField();
        t.setPreferredSize(new Dimension(220, 32));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return t;
    }

    private void stylePrimaryButton(JButton b) {
        b.setPreferredSize(new Dimension(160, 32));
        b.setBackground(new Color(141, 228, 250));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
    }

    // ---------------------------------------------------------
    // CRUD BUTTONS
    // ---------------------------------------------------------
    private JPanel createButtonPanel() {

        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        p.setBackground(BACKGROUND_COLOR);

        JButton modify = new JButton("Modifier");
        JButton delete = new JButton("Supprimer");

        styleAction(modify, new Color(166, 234, 208));
        styleAction(delete, new Color(234, 175, 175));

        p.add(modify);
        p.add(delete);

        modify.addActionListener(e -> handleModify());
        delete.addActionListener(e -> handleDelete());

        return p;
    }

    private void styleAction(JButton btn, Color bg) {
        btn.setPreferredSize(new Dimension(180, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
    }

    // ---------------------------------------------------------
    // LOGIQUE (NON MODIFIÉE)
    // ---------------------------------------------------------
    private void loadRdvsFromDatabase() {
        RdvController c = new RdvController();
        List<String[]> rows = c.getAllRdvs();

        tableModel.setRowCount(0);
        for (String[] r : rows) tableModel.addRow(r);
    }

    private void handleSearch() {

        String p = searchField.getText().toLowerCase();
        String pr = searchField2.getText().toLowerCase();
        String ag = searchField3.getText().toLowerCase();

        TableRowSorter sorter = new TableRowSorter<>(tableModel);
        rdvTable.setRowSorter(sorter);

        List<RowFilter<Object,Object>> filters = new ArrayList<>();

        if (!p.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + p, 2));
        if (!pr.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + pr, 3));
        if (!ag.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + ag, 6));

        if (!filters.isEmpty()) sorter.setRowFilter(RowFilter.andFilter(filters));
        else sorter.setRowFilter(null);
    }

    private void handleDelete() {

        int row = rdvTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un rendez-vous.");
            return;
        }

        tableModel.removeRow(row);
        JOptionPane.showMessageDialog(this, "Rendez-vous supprimé.");
    }

    private void handleModify() {

        int row = rdvTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un rendez-vous à modifier.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ===== Récupération des valeurs existantes =====
        int idV = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String ancienIdBien = tableModel.getValueAt(row, 1).toString();
        String ancienProprietaire = tableModel.getValueAt(row, 2).toString();
        String ancienPreneur = tableModel.getValueAt(row, 3).toString();
        String ancienneDate = tableModel.getValueAt(row, 4).toString();
        String ancienneHeure = tableModel.getValueAt(row, 5).toString().substring(0,5); // HH:MM
        String ancienAgent = tableModel.getValueAt(row, 6).toString();

        // ===== Création du popup =====
        JDialog dialog = new JDialog(this, "Modifier un rendez-vous", true);
        dialog.setSize(500, 430);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // ===== Champs à modifier =====
        JTextField propField = new JTextField(ancienProprietaire);
        JTextField preneurField = new JTextField(ancienPreneur);
        JTextField agentField = new JTextField(ancienAgent);

        JTextField dateField = new JTextField(ancienneDate);
        JTextField heureField = new JTextField(ancienneHeure);

        JTextField adresseBienField = new JTextField(ancienIdBien); // remplacé plus tard par adresse

        // On affiche l'ID du bien mais tu peux choisir d'afficher une adresse directement si tu veux
        adresseBienField.setText(ancienIdBien);

        // ===== Ajout dans formulaire =====
        addFormRow(form, gbc, "Propriétaire :", propField);
        addFormRow(form, gbc, "Preneur :", preneurField);
        addFormRow(form, gbc, "Agent :", agentField);
        addFormRow(form, gbc, "Date :", dateField);
        addFormRow(form, gbc, "Heure (HH:MM) :", heureField);
        addFormRow(form, gbc, "ID Bien :", adresseBienField);

        // ===== Boutons =====
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.setBackground(new Color(240, 240, 240));

        JButton cancel = new JButton("Annuler");
        cancel.setBackground(new Color(220, 53, 69));
        cancel.setForeground(Color.WHITE);

        JButton save = new JButton("Enregistrer");
        save.setBackground(new Color(40, 167, 69));
        save.setForeground(Color.WHITE);

        btns.add(cancel);
        btns.add(save);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btns, BorderLayout.SOUTH);

        cancel.addActionListener(e -> dialog.dispose());

        // -------------------------------------------------------
        // ACTION : SAUVEGARDE
        // -------------------------------------------------------
        save.addActionListener(e -> {

            String newProp = propField.getText().trim();
            String newPreneur = preneurField.getText().trim();
            String newAgent = agentField.getText().trim();
            String newDate = dateField.getText().trim();
            String newHeure = heureField.getText().trim();
            String newIdBienTxt = adresseBienField.getText().trim();

            if (newProp.isEmpty() || newPreneur.isEmpty() || newAgent.isEmpty() ||
                    newDate.isEmpty() || newHeure.isEmpty() || newIdBienTxt.isEmpty()) {

                JOptionPane.showMessageDialog(dialog,
                        "Tous les champs doivent être remplis.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            RdvController controller = new RdvController();

            int idClientPreneur = controller.getIdClientByNom(newPreneur);
            int idAgent = controller.getIdAgentByNom(newAgent);
            int idBien = Integer.parseInt(newIdBienTxt);

            if (idClientPreneur == -1 || idAgent == -1) {
                JOptionPane.showMessageDialog(dialog,
                        "Erreur : Agent ou Client introuvable.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean ok = controller.modifierRdv(
                    idV,
                    newDate,
                    newHeure,
                    idClientPreneur,
                    idAgent,
                    idBien
            );

            if (ok) {

                tableModel.setValueAt(newIdBienTxt, row, 1);
                tableModel.setValueAt(newProp, row, 2);
                tableModel.setValueAt(newPreneur, row, 3);
                tableModel.setValueAt(newDate, row, 4);
                tableModel.setValueAt(newHeure + ":00", row, 5);
                tableModel.setValueAt(newAgent, row, 6);

                JOptionPane.showMessageDialog(dialog, "Rendez-vous modifié avec succès.");
                dialog.dispose();

            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Erreur lors de la modification.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }
    
    private void addFormRow(JPanel panel, GridBagConstraints gbc, String labelText, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.2;

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.8;
        panel.add(component, gbc);
    }


}
