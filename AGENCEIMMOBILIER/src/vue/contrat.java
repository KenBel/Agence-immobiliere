package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import controleur.ContratController;

public class contrat extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    private JTextField searchField, searchField2;
    private JComboBox<String> filterComboBox;
    private JTable contratTable;
    private DefaultTableModel tableModel;

    private accueil FenetreAccueil;
    private bien FenetreBien;
    private client FenetreClient;
    private agent FenetreAgent;
    private transac FenetreTransaction;
    private rdv FenetreRdv;

    public contrat() {

        setTitle("Agence immobilière - Espace Contrats");
        setSize(1830, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel menuPanel = createMenuPanel();
        menuPanel.setPreferredSize(new Dimension(260, getHeight()));
        add(menuPanel, BorderLayout.WEST);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        loadContratFromDatabase();

        setVisible(true);
    }

    // ---------------------------------------------------------------------
    // MENU — Identique à BIEN / AGENT
    // ---------------------------------------------------------------------
    private JPanel createMenuPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

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

    // ---------------------------------------------------------------------
    // HEADER — Identique à BIEN
    // ---------------------------------------------------------------------
    private JPanel createHeaderPanel() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel title = new JLabel("Espace Contrats Immobiliers");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 20, 0, 0));

        JButton addButton = new JButton("Ajouter un contrat");
        addButton.setIcon(new ImageIcon("C:\\Users\\Kenza\\Downloads\\plus.png"));
        addButton.setBackground(new Color(166, 234, 208));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.setFocusPainted(false);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        rightPanel.setBackground(PRIMARY_COLOR);
        rightPanel.add(addButton);

        header.add(title, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    // ---------------------------------------------------------------------
    // MAIN PANEL — Table + Search
    // ---------------------------------------------------------------------
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JLabel lblP = new JLabel("Propriétaire :");
        lblP.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblP);

        searchField = new JTextField();
        styleInput(searchField);
        searchPanel.add(searchField);

        JLabel lblN = new JLabel("Preneur :");
        lblN.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblN);

        searchField2 = new JTextField();
        styleInput(searchField2);
        searchPanel.add(searchField2);

        JLabel lblF = new JLabel("Type :");
        lblF.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblF);

        filterComboBox = new JComboBox<>(new String[]{"Tous", "Location", "Vente"});
        filterComboBox.setPreferredSize(new Dimension(160, 32));
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchPanel.add(filterComboBox);

        JButton btnSearch = new JButton("Rechercher");
        stylePrimaryButton(btnSearch);
        searchPanel.add(btnSearch);

        main.add(searchPanel, BorderLayout.NORTH);

        // TABLE
        String[] cols = {"Id", "Id Bien", "Propriétaire", "Preneur", "Type", "Prix", "Date"};

        tableModel = new DefaultTableModel(cols, 0);
        contratTable = new JTable(tableModel);
        contratTable.setRowHeight(40);
        contratTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contratTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = contratTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(225, 230, 245));

        JScrollPane pane = new JScrollPane(contratTable);
        pane.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.add(pane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> handleSearch());
        filterComboBox.addActionListener(e -> handleFilter());

        JPanel bottom = createButtonPanel();
        main.add(bottom, BorderLayout.SOUTH);

        return main;
    }

    private void styleInput(JTextField t) {
        t.setPreferredSize(new Dimension(220, 32));
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void stylePrimaryButton(JButton b) {
        b.setPreferredSize(new Dimension(150, 32));
        b.setBackground(new Color(141, 228, 250));
        b.setForeground(Color.BLACK);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
    }

    // ---------------------------------------------------------------------
    // CRUD Buttons (Modifier / Supprimer)
    // ---------------------------------------------------------------------
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

    // ---------------------------------------------------------------------
    // LOGIQUE ORIGINALE (NON MODIFIÉE)
    // ---------------------------------------------------------------------
    private void loadContratFromDatabase() {
        ContratController controller = new ContratController();
        List<String[]> contrats = controller.getAllContrats();
        tableModel.setRowCount(0);
        for (String[] c : contrats) tableModel.addRow(c);
    }

    private void handleSearch() {

        String p = searchField.getText().toLowerCase();
        String pr = searchField2.getText().toLowerCase();

        TableRowSorter sorter = new TableRowSorter<>(tableModel);
        contratTable.setRowSorter(sorter);

        List<RowFilter<Object,Object>> filters = new ArrayList<>();

        if (!p.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + p, 2));
        if (!pr.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + pr, 3));

        if (!filters.isEmpty()) sorter.setRowFilter(RowFilter.andFilter(filters));
        else sorter.setRowFilter(null);
    }

    private void handleFilter() {

        String f = (String) filterComboBox.getSelectedItem();

        TableRowSorter sorter = new TableRowSorter<>(tableModel);
        contratTable.setRowSorter(sorter);

        if (!"Tous".equals(f)) sorter.setRowFilter(RowFilter.regexFilter(f, 4));
        else sorter.setRowFilter(null);
    }

    private void handleDelete() {

        int row = contratTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un contrat.");
            return;
        }
        tableModel.removeRow(row);
        JOptionPane.showMessageDialog(this, "Contrat supprimé.");
    }

    private void handleModify() {

        int row = contratTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un contrat à modifier.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // =======================
        // Récupération des données
        // =======================
        int idContrat = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String ancienType = tableModel.getValueAt(row, 4).toString();
        String ancienPrix = tableModel.getValueAt(row, 5).toString();
        String ancienneDate = tableModel.getValueAt(row, 6).toString();

        // =======================
        // Création de la fenêtre
        // =======================
        JDialog dialog = new JDialog(this, "Modifier un contrat", true);
        dialog.setSize(500, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Champs
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Location", "Vente"});
        typeBox.setSelectedItem(ancienType);

        JTextField prixField = new JTextField(ancienPrix);
        JTextField dateField = new JTextField(ancienneDate); // Format yyyy-MM-dd

        addFormRow(form, gbc, "Type du contrat :", typeBox);
        addFormRow(form, gbc, "Prix :", prixField);
        addFormRow(form, gbc, "Date (yyyy-MM-dd) :", dateField);

        // =======================
        // Boutons
        // =======================
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

        // =======================
        // LOGIQUE DE MODIFICATION
        // =======================
        save.addActionListener(e -> {
            try {
                String newType = typeBox.getSelectedItem().toString();
                float newPrix = Float.parseFloat(prixField.getText().trim());
                String newDate = dateField.getText().trim();

                if (newDate.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog,
                            "La date ne doit pas être vide.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ContratController controller = new ContratController();

                boolean ok = controller.modifierContrat(idContrat, newType, newPrix, newDate);

                if (ok) {
                    // Mise à jour dans le tableau
                    tableModel.setValueAt(newType, row, 4);
                    tableModel.setValueAt(newPrix, row, 5);
                    tableModel.setValueAt(newDate, row, 6);

                    JOptionPane.showMessageDialog(dialog, "Contrat modifié avec succès !");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "Erreur lors de la modification.",
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Vérifiez les valeurs saisies.",
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
