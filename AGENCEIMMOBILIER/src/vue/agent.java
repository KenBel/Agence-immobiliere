package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.TableRowSorter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import controleur.AgentController;

public class agent extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    private JTextField searchField, searchField2;
    private JTable agentTable;
    private DefaultTableModel tableModel;

    private accueil FenetreAccueil;
    private bien FenetreBien;
    private client FenetreClient;
    private transac FenetreTransaction;
    private rdv FenetreRdv;
    private contrat FenetreContrat;

    public agent() {
        setTitle("Agence immobilière - Espace agents");
        setSize(1830, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);

        // =========== MENU IDENTIQUE À BIEN ===========
        JPanel menuPanel = createMenuPanel();
        menuPanel.setPreferredSize(new Dimension(260, getHeight()));
        add(menuPanel, BorderLayout.WEST);

        // =========== HEADER ===========
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // =========== MAIN ===========
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        loadAgentFromDatabase();
        setVisible(true);
    }

    // ============================================================
    // MENU IDENTIQUE À BIEN.JAVA
    // ============================================================
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
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setIconTextGap(15);

            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            button.setBackground(new Color(22, 45, 104));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            button.setFocusPainted(false);

            // HOVER IDENTIQUE À BIEN
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

    // ============================================================
    // HEADER
    // ============================================================
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel title = new JLabel("Espace agents");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(20, 20, 0, 0));

        JButton addButton = new JButton("Ajouter un agent");
        addButton.setIcon(new ImageIcon("C:\\Users\\Kenza\\Downloads\\plus.png"));
        addButton.setBackground(new Color(166, 234, 208));
        addButton.setForeground(Color.BLACK);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addButton.setPreferredSize(new Dimension(220, 40));
        addButton.setFocusPainted(false);
        addButton.addActionListener(e -> new AjoutAgent(tableModel));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        right.setBackground(PRIMARY_COLOR);
        right.add(addButton);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    // ============================================================
    // MAIN PANEL
    // ============================================================
    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 20));
        searchPanel.setBackground(BACKGROUND_COLOR);

        JLabel nameLabel = new JLabel("Nom :");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(nameLabel);

        searchField = new JTextField(20);
        styleSearchField(searchField);
        searchPanel.add(searchField);

        JLabel firstNameLabel = new JLabel("Prénom :");
        firstNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchPanel.add(firstNameLabel);

        searchField2 = new JTextField(20);
        styleSearchField(searchField2);
        searchPanel.add(searchField2);

        JButton searchButton = new JButton("Rechercher");
        searchButton.setPreferredSize(new Dimension(150, 32));
        searchButton.setBackground(new Color(141, 228, 250));
        searchButton.setForeground(Color.BLACK);
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchButton.addActionListener(e -> handleSearch());
        searchPanel.add(searchButton);

        main.add(searchPanel, BorderLayout.NORTH);

        String[] cols = { "Nom", "Prénom", "Email", "Téléphone", "Adresse", "Affaires" };
        tableModel = new DefaultTableModel(cols, 0);
        agentTable = new JTable(tableModel);

        agentTable.setRowHeight(40);
        agentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = agentTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(agentTable);
        scrollPane.setBorder(new EmptyBorder(20, 20, 20, 20));

        main.add(scrollPane, BorderLayout.CENTER);
        main.add(createButtonPanel(), BorderLayout.SOUTH);

        return main;
    }

    private void styleSearchField(JTextField field) {
        field.setPreferredSize(new Dimension(240, 32));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200,200,200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    // ============================================================
    // BOUTONS CRUD
    // ============================================================
    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        p.setBackground(BACKGROUND_COLOR);

        JButton modify = new JButton("Modifier");
        JButton delete = new JButton("Supprimer");

        styleActionButton(modify, new Color(166, 234, 208));
        styleActionButton(delete, new Color(234, 175, 175));

        p.add(modify);
        p.add(delete);

        modify.addActionListener(e -> handleModify());
        delete.addActionListener(e -> handleDelete());

        return p;
    }

    private void styleActionButton(JButton btn, Color bg) {
        btn.setPreferredSize(new Dimension(180, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
    }

    // ============================================================
    // LOGIQUE (inchangée)
    // ============================================================
    private void loadAgentFromDatabase() {
        AgentController controller = new AgentController();
        List<String[]> agents = controller.getAllAgents();
        for (String[] agent : agents) tableModel.addRow(agent);
    }

    private void handleDelete() {
        int row = agentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un agent.");
            return;
        }
        tableModel.removeRow(row);
        JOptionPane.showMessageDialog(this, "Agent supprimé.");
    }

    private void handleModify() {

        int selectedRow = agentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un agent à modifier.",
                    "Avertissement",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Récupération des données existantes
        String ancienNom = tableModel.getValueAt(selectedRow, 0).toString();
        String ancienPrenom = tableModel.getValueAt(selectedRow, 1).toString();
        String ancienEmail = tableModel.getValueAt(selectedRow, 2).toString();  // clé pour WHERE
        String ancienTel = tableModel.getValueAt(selectedRow, 3).toString();
        String ancienAdresse = tableModel.getValueAt(selectedRow, 4).toString();
        String ancienNbrAff = tableModel.getValueAt(selectedRow, 5).toString();

        JDialog dialog = new JDialog(this, "Modifier agent", true);
        dialog.setSize(480, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        form.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Champs
        JTextField nomField = new JTextField(ancienNom);
        JTextField prenomField = new JTextField(ancienPrenom);
        JTextField emailField = new JTextField(ancienEmail);
        JTextField telField = new JTextField(ancienTel);
        JTextField adresseField = new JTextField(ancienAdresse);
        JTextField nbrAffField = new JTextField(ancienNbrAff);

        // Ajout dans Form
        addFormRow(form, gbc, "Nom :", nomField);
        addFormRow(form, gbc, "Prénom :", prenomField);
        addFormRow(form, gbc, "Email :", emailField);
        addFormRow(form, gbc, "Téléphone :", telField);
        addFormRow(form, gbc, "Adresse :", adresseField);
        addFormRow(form, gbc, "Affaires :", nbrAffField);

        // Boutons
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

        save.addActionListener(e -> {

            String newNom = nomField.getText().trim();
            String newPrenom = prenomField.getText().trim();
            String newEmail = emailField.getText().trim();
            String newTel = telField.getText().trim();
            String newAdresse = adresseField.getText().trim();
            String newNbrAff = nbrAffField.getText().trim();

            if (newNom.isEmpty() || newPrenom.isEmpty() || newEmail.isEmpty()
                    || newTel.isEmpty() || newAdresse.isEmpty() || newNbrAff.isEmpty()) {

                JOptionPane.showMessageDialog(dialog,
                        "Tous les champs doivent être remplis.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            AgentController controller = new AgentController();
            boolean ok = controller.modifierAgent(
                    newNom, newPrenom, newEmail, newTel, newAdresse, newNbrAff
            );

            if (ok) {

                // Mise à jour du tableau
                tableModel.setValueAt(newNom, selectedRow, 0);
                tableModel.setValueAt(newPrenom, selectedRow, 1);
                tableModel.setValueAt(newEmail, selectedRow, 2);
                tableModel.setValueAt(newTel, selectedRow, 3);
                tableModel.setValueAt(newAdresse, selectedRow, 4);
                tableModel.setValueAt(newNbrAff, selectedRow, 5);

                JOptionPane.showMessageDialog(dialog, "Agent modifié avec succès.");
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


    private void handleSearch() {
        String n = searchField.getText().toLowerCase();
        String p = searchField2.getText().toLowerCase();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        agentTable.setRowSorter(sorter);

        List<RowFilter<Object,Object>> filters = new ArrayList<>();
        if (!n.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + n, 0));
        if (!p.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + p, 1));

        sorter.setRowFilter(RowFilter.andFilter(filters));
    }
}
