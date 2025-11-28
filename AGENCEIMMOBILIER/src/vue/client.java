package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import controleur.ClientController;

public class client extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    private JTextField searchField, searchField2;
    private JComboBox<String> filterComboBox;
    private JTable clientTable;
    private DefaultTableModel tableModel;

    public client() {

        setTitle("Agence immobilière - Espace Clients");
        setSize(1830, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(createMenuPanel(), BorderLayout.WEST);
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadClientsFromDatabase();

        setVisible(true);
    }

    // ---------------------------------------------------------------------
    // MENU IDENTIQUE À BIEN ET AGENT
    // ---------------------------------------------------------------------
    private JPanel createMenuPanel() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(260, getHeight()));
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

            JButton btn = new JButton(items[i]);
            btn.setIcon(new ImageIcon(icons[i]));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            btn.setBackground(new Color(22, 45, 104));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            btn.setFocusPainted(false);

            // Hover
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
    // HEADER IDENTIQUE À BIEN ET AGENT
    // ---------------------------------------------------------------------
    private JPanel createHeaderPanel() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel title = new JLabel("Espace Clients");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 20, 0, 0));

        JButton addBtn = new JButton("Ajouter un client");
        addBtn.setIcon(new ImageIcon("C:\\Users\\Kenza\\Downloads\\plus.png"));
        addBtn.setBackground(new Color(166, 234, 208));
        addBtn.setForeground(Color.BLACK);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setPreferredSize(new Dimension(200, 40));
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> new AjoutClient(tableModel));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        right.setBackground(PRIMARY_COLOR);
        right.add(addBtn);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ---------------------------------------------------------------------
    // MAIN PANEL (SEARCH + TABLE)
    // ---------------------------------------------------------------------
    private JPanel createMainPanel() {

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JLabel lblNom = new JLabel("Nom :");
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblNom);

        searchField = new JTextField(20);
        styleInput(searchField);
        searchPanel.add(searchField);

        JLabel lblPrenom = new JLabel("Prénom :");
        lblPrenom.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblPrenom);

        searchField2 = new JTextField(20);
        styleInput(searchField2);
        searchPanel.add(searchField2);

        JLabel lblFilter = new JLabel("Statut :");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(lblFilter);

        filterComboBox = new JComboBox<>(new String[]{"Tous", "Proprietaire", "Acheteur", "Locataire"});
        filterComboBox.setPreferredSize(new Dimension(200, 32));
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchPanel.add(filterComboBox);

        JButton btnSearch = new JButton("Rechercher");
        stylePrimaryButton(btnSearch);
        searchPanel.add(btnSearch);

        main.add(searchPanel, BorderLayout.NORTH);

        // TABLEAU MODERNISÉ
        String[] cols = {"Id", "Nom", "Prénom", "Adresse", "Téléphone", "Email", "Statut"};
        tableModel = new DefaultTableModel(cols, 0);
        clientTable = new JTable(tableModel);

        clientTable.setRowHeight(40);
        clientTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        clientTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = clientTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(225, 230, 245));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(clientTable);
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
    // BOUTONS CRUD IDENTIQUES À BIEN
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
    private void loadClientsFromDatabase() {
        ClientController controller = new ClientController();
        List<String[]> clients = controller.getAllClients();
        tableModel.setRowCount(0);
        for (String[] c : clients)
            tableModel.addRow(c);
    }

    private void handleSearch() {
        String nom = searchField.getText().toLowerCase();
        String prenom = searchField2.getText().toLowerCase();

        TableRowSorter sorter = new TableRowSorter<>(tableModel);
        clientTable.setRowSorter(sorter);

        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        if (!nom.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + nom, 1));
        if (!prenom.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + prenom, 2));

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private void handleFilter() {
        String filter = (String) filterComboBox.getSelectedItem();
        TableRowSorter sorter = new TableRowSorter<>(tableModel);
        clientTable.setRowSorter(sorter);

        if (!"Tous".equals(filter)) sorter.setRowFilter(RowFilter.regexFilter(filter, 6));
        else sorter.setRowFilter(null);
    }

    private void handleDelete() {
        int selectedRow = clientTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un client à supprimer.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Êtes-vous sûr de vouloir supprimer ce client ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        int idClient = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

        ClientController controller = new ClientController();
        
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
	
	    // Récupération des données dans l'ordre exact du tableau
	    int idClient = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
	    String ancienNom = tableModel.getValueAt(selectedRow, 1).toString();
	    String ancienPrenom = tableModel.getValueAt(selectedRow, 2).toString();
	    String ancienEmail = tableModel.getValueAt(selectedRow, 3).toString();
	    String ancienTel = tableModel.getValueAt(selectedRow, 4).toString();
	    String ancienAdresse = tableModel.getValueAt(selectedRow, 5).toString();
	    String ancienStatut = tableModel.getValueAt(selectedRow, 6).toString();
	
	    // Fenêtre popup
	    JDialog dialog = new JDialog(this, "Modifier un client", true);
	    dialog.setSize(500, 420);
	    dialog.setLocationRelativeTo(this);
	
	    JPanel mainPanel = new JPanel(new GridBagLayout());
	    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	    mainPanel.setBackground(new Color(240, 240, 240));
	
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    gbc.insets = new Insets(8, 8, 8, 8);
	
	    // Champs remplis
	    JTextField nomField = createStyledTextField(ancienNom);
	    JTextField prenomField = createStyledTextField(ancienPrenom);
	    JTextField emailField = createStyledTextField(ancienEmail);
	    JTextField telField = createStyledTextField(ancienTel);
	    JTextField adresseField = createStyledTextField(ancienAdresse);
	
	    JComboBox<String> statutCombo = new JComboBox<>(
	            new String[]{"Proprietaire", "Acheteur", "Locataire"});
	    statutCombo.setSelectedItem(ancienStatut);
	
	    // Ajout dans le GridBagLayout
	    addLabelAndComponent(mainPanel, gbc, "Nom :", nomField);
	    addLabelAndComponent(mainPanel, gbc, "Prénom :", prenomField);
	    addLabelAndComponent(mainPanel, gbc, "Email :", emailField);
	    addLabelAndComponent(mainPanel, gbc, "Téléphone :", telField);
	    addLabelAndComponent(mainPanel, gbc, "Adresse :", adresseField);
	    addLabelAndComponent(mainPanel, gbc, "Statut :", statutCombo);
	
	    // Boutons
	    JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    btnPanel.setBackground(new Color(240, 240, 240));
	
	    JButton cancel = createStyledButton("Annuler", new Color(220, 53, 69));
	    JButton save = createStyledButton("Enregistrer", new Color(40, 167, 69));
	
	    btnPanel.add(cancel);
	    btnPanel.add(save);
	
	    dialog.setLayout(new BorderLayout());
	    dialog.add(mainPanel, BorderLayout.CENTER);
	    dialog.add(btnPanel, BorderLayout.SOUTH);
	
	    cancel.addActionListener(e -> dialog.dispose());
	
	    save.addActionListener(e -> {
	
	        String newNom = nomField.getText().trim();
	        String newPrenom = prenomField.getText().trim();
	        String newEmail = emailField.getText().trim();
	        String newTel = telField.getText().trim();
	        String newAdresse = adresseField.getText().trim();
	        String newStatut = statutCombo.getSelectedItem().toString();
	
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

    private JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

}
