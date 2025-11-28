package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controleur.BienController;
import controleur.ClientController;

public class AjoutClient extends JFrame {

	private static final Color BACKGROUND_COLOR = new Color(124, 134, 150);
	private static final Color BUTTON_COLOR = new Color(60, 94, 179);

	private JTextField nomField;
	private JTextField prenomField;
	private JTextField emailField;
	private JTextField telephoneField;
	private JTextField adresseField;
	private JComboBox<String> statusComboBox;
	private final DefaultTableModel tableModel;
	private final ClientController controller;

	public AjoutClient(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
		this.controller = new ClientController();
		setTitle("Ajouter un client");
		setSize(700, 650);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBackground(BACKGROUND_COLOR);

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 20));
		mainPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		logoPanel.setBackground(BACKGROUND_COLOR);
		JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\Kenza\\Downloads\\agence.png"));
		JLabel titleLabel = new JLabel("Ajouter un client");
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
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel[] labels = { new JLabel("Nom:"), new JLabel("Prénom:"), new JLabel("Email:"), new JLabel("Téléphone:"),
				new JLabel("Adresse:") };

		JTextField[] textFields = { nomField = new JTextField(20), prenomField = new JTextField(20),
				emailField = new JTextField(20), telephoneField = new JTextField(20),
				adresseField = new JTextField(20) };

		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			labels[i].setFont(new Font("Arial", Font.BOLD, 20));
			labels[i].setPreferredSize(new Dimension(150, 40));
			panel.add(labels[i], gbc);

			gbc.gridx = 1;
			textFields[i].setFont(new Font("Arial", Font.PLAIN, 18));
			textFields[i].setPreferredSize(new Dimension(350, 40));
			textFields[i].setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
			panel.add(textFields[i], gbc);
		}

		nomField.addFocusListener(new java.awt.event.FocusAdapter() {
			@Override
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

		gbc.gridx = 0;
		gbc.gridy = labels.length;
		JLabel statusLabel = new JLabel("Statut:");
		statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
		panel.add(statusLabel, gbc);

		gbc.gridx = 1;
		statusComboBox = new JComboBox<>(new String[] { "Proprietaire", "Acheteur", "Locataire" });
		statusComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
		statusComboBox.setPreferredSize(new Dimension(350, 40));
		panel.add(statusComboBox, gbc);

		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 20));
		panel.setBackground(BACKGROUND_COLOR);

		JButton addButton = new JButton("Ajouter");
		styleButton(addButton);
		addButton.addActionListener(new AddButtonListener());

		JButton returnButton = new JButton("Retourner");
		styleButton(returnButton);
		returnButton.addActionListener(e -> dispose());

		panel.add(addButton);
		panel.add(returnButton);

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
		public void actionPerformed(ActionEvent e) {
			String nom = nomField.getText().trim();
			String prenom = prenomField.getText().trim();
			String email = emailField.getText().trim();
			String telephone = telephoneField.getText().trim();
			String adresse = adresseField.getText().trim();
			String statut = (String) statusComboBox.getSelectedItem();

			if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || telephone.isEmpty() || adresse.isEmpty()) {
				JOptionPane.showMessageDialog(AjoutClient.this, "Tous les champs doivent être remplis.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
				JOptionPane.showMessageDialog(AjoutClient.this, "Veuillez entrer un email valide.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!telephone.matches("\\d{10}")) {
				JOptionPane.showMessageDialog(AjoutClient.this,
						"Le numéro de téléphone doit contenir exactement 10 chiffres.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			controleur.ClientController controller = new controleur.ClientController();
			boolean success = controller.ajouterClient(nom, prenom, email, telephone, adresse, statut);

			if (success) {
				JOptionPane.showMessageDialog(AjoutClient.this, "Client ajouté avec succès !");
				tableModel.addRow(new Object[] { nom, prenom, email, telephone, adresse, statut });
				dispose();
			} else {
				JOptionPane.showMessageDialog(AjoutClient.this, "Erreur lors de l'ajout du client.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
