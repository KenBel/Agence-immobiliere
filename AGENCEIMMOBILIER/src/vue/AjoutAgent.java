package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controleur.AgentController;

public class AjoutAgent extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(124, 134, 150);
    private static final Color BUTTON_COLOR = new Color(60, 94, 179);

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField emailField;
    private JTextField telephoneField;
    private JTextField adresseField;
    private JTextField nbrAffaireField;
    private JTextField salaireField;
    private JTextField nomUtilisateurField;
    private JTextField motDePasseField;

    private DefaultTableModel tableModel;
    private AgentController controller;

    public AjoutAgent(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        this.controller = new AgentController();
        setTitle("Ajouter un agent");
        setSize(700, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(BACKGROUND_COLOR);

        JLabel logoLabel = new JLabel(new ImageIcon("C:/Users/Kenza/Downloads/agence.png"));
        JLabel titleLabel = new JLabel("Ajouter un agent");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);

        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        String[] labels = { "Nom :", "Prénom :", "Email :", "Téléphone :", "Adresse :", "Nombre d'affaires :",
                "Salaire :", "Nom d'utilisateur :", "Mot de passe :" };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;

            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(label, gbc);

            gbc.gridx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            JTextField textField = createTextField();
            switch (i) {
                case 0 -> nomField = textField;
                case 1 -> prenomField = textField;
                case 2 -> emailField = textField;
                case 3 -> telephoneField = textField;
                case 4 -> adresseField = textField;
                case 5 -> nbrAffaireField = textField;
                case 6 -> salaireField = textField;
                case 7 -> nomUtilisateurField = textField;
                case 8 -> motDePasseField = textField;
            }
            panel.add(textField, gbc);
        }

        nomField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                String nom = nomField.getText().trim();
                nomField.setText(nom.toUpperCase());
            }
        });

        prenomField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                String prenom = prenomField.getText().trim();
                if (!prenom.isEmpty()) {
                    prenomField.setText(prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase());
                }
            }
        });

        return panel;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setFont(new Font("Arial", Font.PLAIN, 18));
        textField.setPreferredSize(new Dimension(350, 40));
        textField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        textField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        return textField;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panel.setBackground(BACKGROUND_COLOR);

        JButton addButton = new JButton("Ajouter");
        styleButton(addButton);
        addButton.addActionListener(new AddButtonListener());

        JButton cancelButton = new JButton("Annuler");
        styleButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());

        panel.add(addButton);
        panel.add(cancelButton);

        return panel;
    }

    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(250, 50));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 20));
    }

    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String nom = nomField.getText().trim();
                String prenom = prenomField.getText().trim();
                String email = emailField.getText().trim();
                String telephone = telephoneField.getText().trim();
                String adresse = adresseField.getText().trim();
                String nbrAffaire = nbrAffaireField.getText().trim();
                String salaire = salaireField.getText().trim();
                String nomUtilisateur = nomUtilisateurField.getText().trim();
                String motDePasse = motDePasseField.getText().trim();

                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || adresse.isEmpty()
                        || nbrAffaire.isEmpty() || salaire.isEmpty() || nomUtilisateur.isEmpty()
                        || motDePasse.isEmpty()) {
                    throw new IllegalArgumentException("Tous les champs doivent être remplis.");
                }

                if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                    throw new IllegalArgumentException("Veuillez entrer un email valide.");
                }

                if (!telephone.matches("\\d{10}")) {
                    throw new IllegalArgumentException("Le numéro de téléphone doit contenir exactement 10 chiffres.");
                }

                controleur.AgentController controller = new controleur.AgentController();
                boolean success = controller.ajouterAgent(nom, prenom, email, telephone, adresse, nbrAffaire, salaire,
                        nomUtilisateur, motDePasse);

                if (success) {
                    JOptionPane.showMessageDialog(AjoutAgent.this, "Agent ajouté avec succès !");
                    tableModel.addRow(new Object[] {nom, prenom, email, telephone, adresse, nbrAffaire, salaire, nomUtilisateur});
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(AjoutAgent.this, "Erreur lors de l'ajout de l'agent.", "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(AjoutAgent.this, ex.getMessage(), "Erreur de validation", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
