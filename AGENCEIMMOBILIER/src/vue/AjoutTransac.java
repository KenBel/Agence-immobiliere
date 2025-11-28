package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controleur.TransacController;

public class AjoutTransac extends JFrame {

    private static final Color BACKGROUND_COLOR = new Color(124, 134, 150);
    private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
    private static final Color BUTTON_COLOR = new Color(60, 94, 179);

    private JTextField proprietaireNomField;
    private JTextField proprietairePrenomField;
    private JTextField preneurNomField;
    private JTextField preneurPrenomField;
    private JComboBox<String> typeComboBox;
    private JTextField trancheField;
    private JTextField dateField;
    private JTextField agentField;
    private int idBien;

    public AjoutTransac(int idBien) {
        this.idBien = idBien;
        setTitle("Ajouter une transaction");
        setSize(700, 625);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(BACKGROUND_COLOR);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(BACKGROUND_COLOR);
        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\Kenza\\Downloads\\agence.png"));
        JLabel titleLabel = new JLabel("Nouvelle Transaction");
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);

        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        addSectionLabel(panel, gbc, "Client");
        preneurNomField = new JTextField(20);
        preneurPrenomField = new JTextField(20);
        preneurNomField.setFont(new Font("Arial", Font.BOLD, 15));
        preneurNomField.setPreferredSize(new Dimension(150, 30));
        preneurPrenomField.setFont(new Font("Arial", Font.BOLD, 15));
        preneurPrenomField.setPreferredSize(new Dimension(150, 30));
        addLabelAndField(panel, gbc, "Nom:", preneurNomField);
        addLabelAndField(panel, gbc, "Prénom:", preneurPrenomField);

        typeComboBox = new JComboBox<>(new String[]{"Location", "Vente"});
        typeComboBox.setFont(new Font("Arial", Font.BOLD, 15));
        typeComboBox.setPreferredSize(new Dimension(150, 30));
        addLabelAndField(panel, gbc, "Type de Transaction:", typeComboBox);

        trancheField = new JTextField(20);
        trancheField.setFont(new Font("Arial", Font.BOLD, 15));
        trancheField.setPreferredSize(new Dimension(150, 30));
        addLabelAndField(panel, gbc, "Tranche payée :", trancheField);

        dateField = new JTextField(20);
        dateField.setFont(new Font("Arial", Font.BOLD, 15));
        dateField.setPreferredSize(new Dimension(150, 30));
        addLabelAndField(panel, gbc, "Date (aaaa-mm-jj):", dateField);

        agentField = new JTextField(20);
        agentField.setFont(new Font("Arial", Font.BOLD, 15));
        agentField.setPreferredSize(new Dimension(150, 30));
        addLabelAndField(panel, gbc, "Agent:", agentField);
        
        preneurNomField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				String nom = preneurNomField.getText().trim();
				preneurNomField.setText(nom.toUpperCase());
			}
		});

        preneurPrenomField.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent e) {
				String prenom = preneurPrenomField.getText().trim();
				if (!prenom.isEmpty()) {
					preneurPrenomField.setText(prenom.substring(0, 1).toUpperCase() + prenom.substring(1).toLowerCase());
				}
			}});

        return panel;
    }

    private void addSectionLabel(JPanel panel, GridBagConstraints gbc, String text) {
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(PRIMARY_COLOR);
        panel.add(label, gbc);
        gbc.gridy++;
        gbc.gridwidth = 1;
    }

    private void addLabelAndField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
        panel.setBackground(BACKGROUND_COLOR);

        JButton addButton = new JButton("Ajouter");
        styleButton(addButton);
        addButton.addActionListener(e -> ajouterTransaction());

        JButton returnButton = new JButton("Retourner");
        styleButton(returnButton);
        returnButton.addActionListener(e -> dispose());

        panel.add(addButton);
        panel.add(returnButton);

        return panel;
    }


    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(250, 40));
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 20));
    }

    private void ajouterTransaction() {
        try {
            String clientNom = preneurNomField.getText().trim();
            String agentNom = agentField.getText().trim();
            String dateT = dateField.getText().trim();
            String typeT = (String) typeComboBox.getSelectedItem();
            float montantT = Float.parseFloat(trancheField.getText().trim());

            TransacController controller = new TransacController();
            int idClient = controller.getIdClientByNom(clientNom);
            int idAgent = controller.getIdAgentByNom(agentNom);

            boolean success = controller.ajouterTransaction(dateT, typeT, montantT, idClient, idAgent, idBien);

            if (success) {
                JOptionPane.showMessageDialog(this, "Transaction ajoutée avec succès !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la transaction.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez vérifier les champs numériques.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
