package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import controleur.TransacController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class transac extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    private JTextField searchField, searchField2, searchField3;
    private JComboBox<String> filterComboBox;
    private DefaultTableModel tableModel;

    private accueil FenetreAccueil;
    private bien FenetreBien;
    private client FenetreClient;
    private agent FenetreAgent;
    private transac FenetreTransaction;
    private rdv FenetreRdv;
    private contrat FenetreContrat;

    private JTable transacTable;

    public transac() {
        setTitle("Espace Transactions");
        setSize(1830, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(BACKGROUND_COLOR);

        add(createMenuPanel(), BorderLayout.WEST);
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        loadTransacsFromDatabase();
        setVisible(true);
    }

    // -----------------------------------------------------------
    // MENU (Même design que bien)
    // -----------------------------------------------------------
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(260, getHeight()));

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
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setIconTextGap(15);
            btn.setBackground(new Color(22, 45, 104));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

            final int index = i;
            btn.addActionListener(e -> openWindow(index));

            // hover style
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(35, 70, 150));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(22, 45, 104));
                }
            });

            panel.add(btn);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    private void openWindow(int index) {
        this.dispose();
        switch (index) {
            case 0 -> new accueil().setVisible(true);
            case 1 -> new bien().setVisible(true);
            case 2 -> new client().setVisible(true);
            case 3 -> new agent().setVisible(true);
            case 4 -> new transac().setVisible(true);
            case 5 -> new rdv().setVisible(true);
            case 6 -> new contrat().setVisible(true);
        }
    }

    // -----------------------------------------------------------
    // HEADER (Même design que bien)
    // -----------------------------------------------------------
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(getWidth(), 80));

        JLabel title = new JLabel("Espace Transactions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(0, 20, 0, 0));

        header.add(title, BorderLayout.WEST);
        return header;
    }

    // -----------------------------------------------------------
    // MAIN PANEL (Search + Table)
    // -----------------------------------------------------------
    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(BACKGROUND_COLOR);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        searchPanel.setBackground(BACKGROUND_COLOR);

        searchPanel.add(new JLabel("Propriétaire :"));
        searchField = createInputField();
        searchPanel.add(searchField);

        searchPanel.add(new JLabel("Preneur :"));
        searchField2 = createInputField();
        searchPanel.add(searchField2);

        searchPanel.add(new JLabel("Agent :"));
        searchField3 = createInputField();
        searchPanel.add(searchField3);

        searchPanel.add(new JLabel("Type :"));
        filterComboBox = new JComboBox<>(new String[]{"Tous", "Location", "Vente"});
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterComboBox.setPreferredSize(new Dimension(150, 32));
        searchPanel.add(filterComboBox);

        JButton btnSearch = new JButton("Rechercher");
        stylePrimaryButton(btnSearch);
        searchPanel.add(btnSearch);

        main.add(searchPanel, BorderLayout.NORTH);

        // TABLE
        String[] cols = { "ID", "ID Bien", "Propriétaire", "Preneur",
                          "Type", "Montant", "Date", "Agent" };

        tableModel = new DefaultTableModel(cols, 0);
        transacTable = new JTable(tableModel);

        transacTable.setRowHeight(40);
        transacTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        transacTable.setGridColor(Color.LIGHT_GRAY);

        JTableHeader header = transacTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setDefaultRenderer(new TableCellRenderer() {
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel lbl = new JLabel(value.toString());
                lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                Color[] colors = {
                    new Color(166,234,208), new Color(141,228,250),
                    new Color(234,175,225), new Color(246,199,170),
                    new Color(200,255,200), new Color(255,230,180),
                    new Color(180,200,255), new Color(166,234,208)
                };

                lbl.setOpaque(true);
                lbl.setBackground(colors[column]);
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(transacTable);
        scroll.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.add(scroll, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> handleSearch());
        filterComboBox.addActionListener(e -> handleFilter());

        JPanel btnPanel = createButtonPanel();
        main.add(btnPanel, BorderLayout.SOUTH);

        return main;
    }

    private JTextField createInputField() {
        JTextField tf = new JTextField(15);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setPreferredSize(new Dimension(180, 32));
        tf.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));
        return tf;
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setBackground(new Color(141, 228, 250));
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(150, 32));
        btn.setFocusPainted(false);
    }

    // -----------------------------------------------------------
    // LOAD DB
    // -----------------------------------------------------------
    private void loadTransacsFromDatabase() {
        TransacController controller = new TransacController();
        List<String[]> list = controller.getAllTransactions();
        tableModel.setRowCount(0);
        for (String[] row : list) {
            tableModel.addRow(row);
        }
    }

    // -----------------------------------------------------------
    // FILTER + SEARCH
    // -----------------------------------------------------------
    private void handleSearch() {
        String owner = searchField.getText().trim().toLowerCase();
        String preneur = searchField2.getText().trim().toLowerCase();
        String agent = searchField3.getText().trim().toLowerCase();

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        transacTable.setRowSorter(sorter);

        List<RowFilter<Object,Object>> filters = new ArrayList<>();

        if (!owner.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + owner, 2));
        if (!preneur.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + preneur, 3));
        if (!agent.isEmpty()) filters.add(RowFilter.regexFilter("(?i)" + agent, 7));

        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
    }

    private void handleFilter() {
        String filter = filterComboBox.getSelectedItem().toString();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        transacTable.setRowSorter(sorter);

        if (!filter.equals("Tous"))
            sorter.setRowFilter(RowFilter.regexFilter(filter, 4));
        else
            sorter.setRowFilter(null);
    }

    // -----------------------------------------------------------
    // CRUD BUTTON PANEL
    // -----------------------------------------------------------
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(BACKGROUND_COLOR);

        JButton modify = new JButton("Modifier");
        JButton delete = new JButton("Supprimer");

        styleActionButton(modify, new Color(166,234,208));
        styleActionButton(delete, new Color(234,175,175));

        modify.addActionListener(e -> handleModify());
        delete.addActionListener(e -> handleDelete());

        panel.add(modify);
        panel.add(delete);

        return panel;
    }

    private void styleActionButton(JButton btn, Color bg) {
        btn.setPreferredSize(new Dimension(180, 38));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
    }

    // -----------------------------------------------------------
    // DELETE
    // -----------------------------------------------------------
    private void handleDelete() {
        int row = transacTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une transaction à supprimer.");
            return;
        }
        tableModel.removeRow(row);
        JOptionPane.showMessageDialog(this, "Transaction supprimée.");
    }

    // -----------------------------------------------------------
    // MODIFY
    // -----------------------------------------------------------
    private void handleModify() {
        int row = transacTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une transaction.");
            return;
        }

        String id = tableModel.getValueAt(row, 0).toString();
        String type = tableModel.getValueAt(row, 4).toString();
        String montant = tableModel.getValueAt(row, 5).toString();
        String date = tableModel.getValueAt(row, 6).toString();

        JDialog dialog = new JDialog(this, "Modifier transaction", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8,8,8,8);

        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Location", "Vente"});
        typeBox.setSelectedItem(type);

        JTextField montantField = new JTextField(montant);
        JTextField dateField = new JTextField(date);

        addFormRow(form, gbc, "Type :", typeBox);
        addFormRow(form, gbc, "Montant :", montantField);
        addFormRow(form, gbc, "Date :", dateField);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Annuler");
        JButton save = new JButton("Enregistrer");

        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> {
            try {
                String nt = typeBox.getSelectedItem().toString();
                float nm = Float.parseFloat(montantField.getText());
                String nd = dateField.getText();

                TransacController controller = new TransacController();

                boolean ok = controller.modifierTransac(Integer.parseInt(id), nt, nm, nd);

                if (ok) {
                    tableModel.setValueAt(nt, row, 4);
                    tableModel.setValueAt(nm, row, 5);
                    tableModel.setValueAt(nd, row, 6);
                    JOptionPane.showMessageDialog(dialog, "Modification réussie.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Erreur lors de la modification.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Vérifiez les valeurs.");
            }
        });

        btns.add(cancel);
        btns.add(save);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btns, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }
}
