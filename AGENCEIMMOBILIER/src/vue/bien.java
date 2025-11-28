package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableRowSorter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.util.*;

import controleur.BienController;

public class bien extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JTable propertyTable;
    private DefaultTableModel tableModel;

    private accueil FenetreAccueil;
    private client FenetreClient;
    private agent FenetreAgent;
    private transac FenetreTransaction;
    private rdv FenetreRdv;
    private contrat FenetreContrat;

    public bien() {
        setTitle("Agence immobilière - Espace Biens");
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

        loadBienFromDatabase();
        setVisible(true);
    }

    // ---------------------------------------------------------------------
    // MENU IDENTIQUE À AGENT
    // ---------------------------------------------------------------------
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\Kenza\\Downloads\\agence.png"));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("Agence immobilière");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(logoLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(titleLabel);
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

            JButton button = new JButton(items[i]);
            button.setIcon(new ImageIcon(icons[i]));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setIconTextGap(15);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);

            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            button.setBackground(new Color(22, 45, 104));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            button.setFocusPainted(false);

            // Hover (comme agent)
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(35, 70, 150));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(22, 45, 104));
                }
            });

            final int index = i;
            button.addActionListener(e -> openWindow(index));

            panel.add(button);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    private void openWindow(int index) {

        this.dispose();

        switch (index) {
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
    // HEADER IDENTIQUE À AGENT
    // ---------------------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel title = new JLabel("Espace Biens Immobiliers");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 20, 0, 0));

        JButton addButton = new JButton("Ajouter un bien");
        addButton.setIcon(new ImageIcon("C:\\Users\\Kenza\\Downloads\\plus.png"));
        addButton.setBackground(new Color(166, 234, 208));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setFocusPainted(false);
        addButton.setPreferredSize(new Dimension(200, 40));
        addButton.addActionListener(e -> new AjoutBien(tableModel));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        right.setBackground(PRIMARY_COLOR);
        right.add(addButton);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ---------------------------------------------------------------------
    // MAIN PANEL + SEARCH BAR + TABLEAU
    // ---------------------------------------------------------------------
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND_COLOR);

        // Barre de recherche LIKE agent
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JLabel lblSearch = new JLabel("Rechercher :");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblSearch);

        searchField = new JTextField(20);
        styleInput(searchField);
        searchPanel.add(searchField);

        JLabel lblType = new JLabel("Type de bien :");
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblType);

        filterComboBox = new JComboBox<>(new String[]{
            "Tous", "Maison", "Appartement", "Terrain", "Local commercial"
        });
        filterComboBox.setPreferredSize(new Dimension(200, 32));
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchPanel.add(filterComboBox);

        JButton btnSearch = new JButton("Rechercher");
        stylePrimaryButton(btnSearch);
        searchPanel.add(btnSearch);

        main.add(searchPanel, BorderLayout.NORTH);

        // TABLEAU MODERNISÉ
        String[] cols = { "Id", "Type", "Adresse", "Surface", "Prix", "Statut", "Disponibilité", "Propriétaire" };

        tableModel = new DefaultTableModel(cols, 0);
        propertyTable = new JTable(tableModel);

        propertyTable.setRowHeight(40);
        propertyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        propertyTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = propertyTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(225, 230, 245));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(propertyTable);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.add(scrollPane, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> handleSearch());
        filterComboBox.addActionListener(e -> handleFilter());

        JPanel btnPanel = createButtonPanel();
        main.add(btnPanel, BorderLayout.SOUTH);

        return main;
    }

    private void styleInput(JTextField field) {
        field.setPreferredSize(new Dimension(240, 32));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setPreferredSize(new Dimension(150, 32));
        btn.setBackground(new Color(141, 228, 250));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
    }

    // ---------------------------------------------------------------------
    // BOUTONS CRUD IDENTIQUES À AGENT
    // ---------------------------------------------------------------------
    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        p.setBackground(BACKGROUND_COLOR);

        JButton modify = new JButton("Modifier");
        JButton delete = new JButton("Supprimer");
        JButton visit = new JButton("Visite");
        JButton transac = new JButton("Transaction");
        JButton contrat = new JButton("Contrat");

        styleAction(modify, new Color(166, 234, 208));
        styleAction(delete, new Color(234, 175, 175));
        styleAction(visit, new Color(141, 228, 250));
        styleAction(transac, new Color(234, 175, 225));
        styleAction(contrat, new Color(246, 199, 170));

        p.add(modify);
        p.add(delete);
        p.add(visit);
        p.add(transac);
        p.add(contrat);

        modify.addActionListener(e -> handleModify());
        delete.addActionListener(e -> handleDelete());
        visit.addActionListener(e -> handleVisit());
        transac.addActionListener(e -> handleTransac());
        contrat.addActionListener(e -> handleContrat());

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
    private void loadBienFromDatabase() {
        BienController controller = new BienController();
        java.util.List<String[]> biens = controller.getAllBiens();
        for (String[] bien : biens) tableModel.addRow(bien);
    }

    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        propertyTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTerm));
    }

    private void handleFilter() {
        String filter = (String) filterComboBox.getSelectedItem();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        propertyTable.setRowSorter(sorter);

        if (!"Tous".equals(filter)) {
            sorter.setRowFilter(RowFilter.regexFilter(filter, 1));
        } else {
            sorter.setRowFilter(null);
        }
    }

    private void handleDelete() {
        int selectedRow = propertyTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un bien à supprimer.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer ce bien ?",
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String type = (String) tableModel.getValueAt(selectedRow, 0);
            String adresse = (String) tableModel.getValueAt(selectedRow, 1);

            BienController controller = new BienController();
            boolean isDeleted = controller.supprimerBien(type, adresse);

            if (isDeleted) {
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this,
                        "Bien supprimé avec succès.",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de la suppression du bien.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

private void handleModify() {

    int selectedRow = clientTable.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un client à modifier.",
                "Avertissement",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Récupération des valeurs correctes depuis la table
    int idClient = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
    String ancienNom = tableModel.getValueAt(selectedRow, 1).toString();
    String ancienPrenom = tableModel.getValueAt(selectedRow, 2).toString();
    String ancienEmail = tableModel.getValueAt(selectedRow, 3).toString();
    String ancienTel = tableModel.getValueAt(selectedRow, 4).toString();
    String ancienAdresse = tableModel.getValueAt(selectedRow, 5).toString();
    String ancienStatut = tableModel.getValueAt(selectedRow, 6).toString();

    // Popup
    JDialog dialog = new JDialog(this, "Modifier un client", true);
    dialog.setSize(500, 420);
    dialog.setLocationRelativeTo(this);

    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    mainPanel.setBackground(new Color(240, 240, 240));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(8, 8, 8, 8);

    // Champs pré-remplis
    JTextField nomField = createStyledTextField(ancienNom);
    JTextField prenomField = createStyledTextField(ancienPrenom);
    JTextField emailField = createStyledTextField(ancienEmail);
    JTextField telField = createStyledTextField(ancienTel);
    JTextField adresseField = createStyledTextField(ancienAdresse);

    JComboBox<String> statutComboBox = new JComboBox<>(
            new String[]{"Proprietaire", "Acheteur", "Locataire"});
    statutComboBox.setSelectedItem(ancienStatut);

    // Ajout des éléments
    addLabelAndComponent(mainPanel, gbc, "Nom :", nomField);
    addLabelAndComponent(mainPanel, gbc, "Prénom :", prenomField);
    addLabelAndComponent(mainPanel, gbc, "Email :", emailField);
    addLabelAndComponent(mainPanel, gbc, "Téléphone :", telField);
    addLabelAndComponent(mainPanel, gbc, "Adresse :", adresseField);
    addLabelAndComponent(mainPanel, gbc, "Statut :", statutComboBox);

    // Boutons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(new Color(240, 240, 240));

    JButton cancelButton = createStyledButton("Annuler", new Color(220, 53, 69));
    JButton saveButton = createStyledButton("Enregistrer", new Color(40, 167, 69));

    buttonPanel.add(cancelButton);
    buttonPanel.add(saveButton);

    dialog.setLayout(new BorderLayout());
    dialog.add(mainPanel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);

    cancelButton.addActionListener(e -> dialog.dispose());

    saveButton.addActionListener(e -> {
        String newNom = nomField.getText().trim();
        String newPrenom = prenomField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newTel = telField.getText().trim();
        String newAdresse = adresseField.getText().trim();
        String newStatut = statutComboBox.getSelectedItem().toString();

        if (newNom.isEmpty() || newPrenom.isEmpty() || newEmail.isEmpty()
                || newTel.isEmpty() || newAdresse.isEmpty()) {

            JOptionPane.showMessageDialog(dialog,
                    "Tous les champs doivent être remplis.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClientController controller = new ClientController();
        boolean ok = controller.modifierClient(
                idClient, newNom, newPrenom, newEmail, newTel, newAdresse, newStatut);

        if (ok) {
            tableModel.setValueAt(newNom, selectedRow, 1);
            tableModel.setValueAt(newPrenom, selectedRow, 2);
            tableModel.setValueAt(newEmail, selectedRow, 3);
            tableModel.setValueAt(newTel, selectedRow, 4);
            tableModel.setValueAt(newAdresse, selectedRow, 5);
            tableModel.setValueAt(newStatut, selectedRow, 6);

            JOptionPane.showMessageDialog(dialog, "Client modifié avec succès.");
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


    private void handleVisit() {
        int selectedRow = propertyTable.getSelectedRow();

        if (selectedRow != -1) {
            String adresse = (String) tableModel.getValueAt(selectedRow, 2);
            BienController bienController = new BienController();
            int idBien = bienController.getIdBienByAdresse(adresse);

            if (idBien != -1) {
                new AjoutRdv(idBien);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Aucun bien trouvé pour cette adresse.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un bien pour la visite.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleTransac() {
        int selectedRow = propertyTable.getSelectedRow();

        if (selectedRow != -1) {
            String adresse = (String) tableModel.getValueAt(selectedRow, 1);
            BienController bienController = new BienController();
            int idBien = bienController.getIdBienByAdresse(adresse);

            new AjoutTransac(idBien);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un bien pour la transaction.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleContrat() {
        int selectedRow = propertyTable.getSelectedRow();

        if (selectedRow != -1) {
            String type = (String) tableModel.getValueAt(selectedRow, 0);
            String adresse = (String) tableModel.getValueAt(selectedRow, 1);

            BienController bienController = new BienController();
            int idBien = bienController.getIdBienByAdresse(adresse);

            new AjoutContrat(idBien);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un bien pour le contrat.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 30));
        return button;
    }

    private void addLabelAndComponent(JPanel panel,
                                      GridBagConstraints gbc,
                                      String labelText,
                                      JComponent component) {

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.weightx = 0.1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.9;
        panel.add(component, gbc);
    }
}
