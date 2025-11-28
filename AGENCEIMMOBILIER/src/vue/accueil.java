package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Map;

import controleur.BienController;
import controleur.AccueilController;

public class accueil extends JFrame {

    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BACKGROUND_COLOR = new Color(240, 244, 248);

    // Nouveau logo (à décaler un peu à droite)
    private static final String LOGO_URL = "https://static.vecteezy.com/system/resources/thumbnails/023/495/220/small/real-estate-logo-design-png.png";

    private static final String ICON_DASHBOARD_URL = "";
    private static final String ICON_BIEN_URL = "";
    private static final String ICON_CLIENT_URL = "";
    private static final String ICON_AGENT_URL = "";
    private static final String ICON_TRANSAC_URL = "";
    private static final String ICON_RDV_URL = "";
    private static final String ICON_CONTRAT_URL = "";

    private static final String ICON_QUICK_PLUS = "https://cdn-icons-png.flaticon.com/512/1828/1828817.png";
    private static final String ICON_QUICK_CLIENT = "https://cdn-icons-png.flaticon.com/512/747/747376.png";
    private static final String ICON_QUICK_RDV = "https://cdn-icons-png.flaticon.com/512/2921/2921226.png";
    private static final String ICON_QUICK_CONTRAT = "https://cdn-icons-png.flaticon.com/512/3209/3209280.png";

    private static final String ICON_USER = "https://cdn-icons-png.flaticon.com/512/847/847969.png";

    private DefaultTableModel tableModel;
    private String adresse = "";

    public accueil() {

        setTitle("Agence immobilière");
        setSize(1830, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createMenuPanel(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private ImageIcon loadIcon(String url, int w, int h) {
        try {
            ImageIcon icon = new ImageIcon(new URL(url));
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    private JPanel createMenuPanel() {

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(260, getHeight()));
        panel.setBackground(PRIMARY_COLOR);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel logo = new JLabel(loadIcon(LOGO_URL, 90, 90));
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        logo.setBorder(new EmptyBorder(0, 20, 0, 0)); // ➤ Logo un peu à droite

        JLabel title = new JLabel("Agence immobilière");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(new EmptyBorder(5, 20, 0, 0)); // ➤ aligné avec le logo

        panel.add(logo);
        panel.add(Box.createVerticalStrut(10));
        panel.add(title);
        panel.add(Box.createVerticalStrut(30));

        String[] items = {
                "Tableau de bord", "Recherche de biens", "Clients",
                "Agents", "Transactions", "Rendez-vous", "Contrats"
        };

        String[] icons = {
                ICON_DASHBOARD_URL, ICON_BIEN_URL, ICON_CLIENT_URL,
                ICON_AGENT_URL, ICON_TRANSAC_URL, ICON_RDV_URL, ICON_CONTRAT_URL
        };

        for (int i = 0; i < items.length; i++) {

            JButton btn = new JButton(items[i], loadIcon(icons[i], 22, 22));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setIconTextGap(15);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            btn.setBackground(new Color(22, 45, 104));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            btn.setFocusPainted(false);

            final int index = i;
            btn.addActionListener(e -> openWindow(index));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setBackground(new Color(35, 70, 150));
                }
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setBackground(new Color(22, 45, 104));
                }
            });

            panel.add(btn);
            panel.add(Box.createVerticalStrut(10));
        }

        return panel;
    }

    // HEADER collé au menu
    private JPanel createHeaderPanel() {

        JPanel header = new JPanel(new BorderLayout());
        header.setPreferredSize(new Dimension(getWidth(), 100));
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(null); // ➤ Collé au menu sans marge

        JLabel title = new JLabel("Bienvenue dans votre espace agence");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(0, 20, 0, 0)); // léger offset

        JButton profile = new JButton(loadIcon(ICON_USER, 30, 30));
        profile.setBackground(new Color(255, 255, 255, 40));
        profile.setPreferredSize(new Dimension(50, 50));
        profile.setBorderPainted(false);
        profile.setFocusPainted(false);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 25, 25));
        right.setOpaque(false);
        right.add(profile);

        header.add(title, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);

        return header;
    }

    private JPanel createQuickActionsPanel() {

        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(20, 20, 0, 20));

        String[] labels = { "Nouveau bien", "Nouveau client", "Planifier RDV", "Nouveau contrat" };
        String[] icons = { ICON_QUICK_PLUS, ICON_QUICK_CLIENT, ICON_QUICK_RDV, ICON_QUICK_CONTRAT };

        Color[] colors = {
                new Color(166, 234, 208),
                new Color(141, 228, 250),
                new Color(234, 175, 225),
                new Color(246, 199, 170)
        };

        for (int i = 0; i < 4; i++) {
            JButton b = new JButton(labels[i], loadIcon(icons[i], 22, 22));
            b.setBackground(colors[i]);
            b.setFont(new Font("Segoe UI", Font.BOLD, 15));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            int finalI = i;
            b.addActionListener(e -> {
                if (finalI == 0) new AjoutBien(tableModel);
                if (finalI == 1) new AjoutClient(tableModel);
                if (finalI == 2) {
                    int idBien = new BienController().getIdBienByAdresse(adresse);
                    new AjoutRdv(idBien);
                }
                if (finalI == 3) {
                    int idBien = new BienController().getIdBienByAdresse(adresse);
                    new AjoutContrat(idBien);
                }
            });

            panel.add(b);
        }

        return panel;
    }

    private JPanel createMainPanel() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(createHeaderPanel(), gbc);

        JLabel title = new JLabel("Tableau de bord");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(PRIMARY_COLOR);
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(title, gbc);

        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(createQuickActionsPanel(), gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(createStatsPanel(), gbc);

        gbc.gridx = 1;
        panel.add(createActivitiesPanel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(createAppointmentsPanel(), gbc);

        return panel;
    }

    // AGRANDIR BOX STATS comme avant
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                "Statistiques du mois"
        );
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        titledBorder.setTitleColor(PRIMARY_COLOR);
        panel.setBorder(titledBorder);

        AccueilController bienController = new AccueilController();
        Map<String, Integer> stats = bienController.getMonthlyStats();

        String[] statLabels = {
                stats.getOrDefault("Biens", 0) + " Biens en gestion",
                stats.getOrDefault("Ventes", 0) + " Transactions finalisées",
                stats.getOrDefault("Clients", 0) + " Nouveaux clients",
                stats.getOrDefault("Contrats", 0) + " Contrats"
        };

        panel.setPreferredSize(new Dimension(400, 200));
        panel.setMinimumSize(new Dimension(400, 300));
        panel.setMaximumSize(new Dimension(400, 300));

        Color[] colors = {
                new Color(166, 234, 208),
                new Color(141, 228, 250),
                new Color(234, 175, 225),
                new Color(246, 199, 170)
        };

        for (int i = 0; i < statLabels.length; i++) {
            JLabel statLabel = new JLabel(statLabels[i], SwingConstants.CENTER);
            statLabel.setForeground(PRIMARY_COLOR);
            statLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            statLabel.setPreferredSize(new Dimension(120, 50));
            statLabel.setBackground(colors[i]);
            statLabel.setOpaque(true);
            panel.add(statLabel);
        }

        return panel;
    }
    
    private JPanel createActivitiesPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                ""
        ));
        return p;
    }

    private JPanel createAppointmentsPanel() {

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        TitledBorder t = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                "Rendez-vous à venir"
        );
        t.setTitleFont(new Font("Segoe UI", Font.BOLD, 18));
        t.setTitleColor(PRIMARY_COLOR);
        p.setBorder(t);

        String[] cols = { "Date", "Client", "Bien", "Agent" };
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        AccueilController controller = new AccueilController();
        List<String[]> rdvs = controller.getUpcomingAppointments();

        for (String[] r : rdvs) model.addRow(r);

        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setBackground(new Color(230, 235, 245));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane sp = new JScrollPane(table);
        p.add(sp, BorderLayout.CENTER);

        return p;
    }

    private void openWindow(int i) {
        dispose();
        switch (i) {
            case 0 -> new accueil().setVisible(true);
            case 1 -> new bien().setVisible(true);
            case 2 -> new client().setVisible(true);
            case 3 -> new agent().setVisible(true);
            case 4 -> new transac().setVisible(true);
            case 5 -> new rdv().setVisible(true);
            case 6 -> new contrat().setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new accueil().setVisible(true));
    }
}
