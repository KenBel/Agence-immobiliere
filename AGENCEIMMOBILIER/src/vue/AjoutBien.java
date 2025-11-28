package vue;

import controleur.BienController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AjoutBien extends JFrame {

	private static final Color BACKGROUND_COLOR = new Color(124, 134, 150);
	private static final Color BUTTON_COLOR = new Color(60, 94, 179);

	private JComboBox<String> typeComboBox;
	private JComboBox<String> etatComboBox;
	private JComboBox<String> statutComboBox;
	private JTextField adresseField;
	private JTextField surfaceField;
	private JTextField piecesField;
	private JTextField prixField;
	private JTextField proprietaireField;

	private final DefaultTableModel tableModel;
	private final BienController controller;

	public AjoutBien(DefaultTableModel tableModel) {
		this.tableModel = tableModel;
		this.controller = new BienController();

		setTitle("Ajouter un bien immobilier");
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
		JLabel titleLabel = new JLabel("Ajouter un bien immobilier");
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

		String[] labels = { "Type de bien :", "Etat du bien :", "Adresse :", "Surface (m²) :", "Nombre de pièces :",
				"Prix :", "Statut :", "Propriétaire :" };

		String[] types = { "Maison", "Appartement", "Terrain", "Local commercial" };
		String[] etats = { "Disponible", "Non disponible" };
		String[] statuts = { "À vendre", "À louer" };

		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;

			JLabel label = new JLabel(labels[i]);
			label.setFont(new Font("Arial", Font.BOLD, 20));
			panel.add(label, gbc);

			gbc.gridx = 1;
			gbc.gridwidth = GridBagConstraints.REMAINDER;

			switch (i) {
			case 0:
				typeComboBox = new JComboBox<>(types);
				styleComboBox(typeComboBox);
				panel.add(typeComboBox, gbc);
				break;
			case 1:
				etatComboBox = new JComboBox<>(etats);
				styleComboBox(etatComboBox);
				panel.add(etatComboBox, gbc);
				break;
			case 6:
				statutComboBox = new JComboBox<>(statuts);
				styleComboBox(statutComboBox);
				panel.add(statutComboBox, gbc);
				break;
			case 7:
				proprietaireField = createTextField();
				panel.add(proprietaireField, gbc);
				break;
			default:
				JTextField textField = createTextField();
				if (i == 2)
					adresseField = textField;
				if (i == 3)
					surfaceField = textField;
				if (i == 4)
					piecesField = textField;
				if (i == 5)
					prixField = textField;
				panel.add(textField, gbc);
			}
		}

		return panel;
	}

	private void styleComboBox(JComboBox<String> comboBox) {
		comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
		comboBox.setPreferredSize(new Dimension(350, 40));
		comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
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
		public void actionPerformed(ActionEvent e) {
			try {
				String type = (String) typeComboBox.getSelectedItem();
				String etat = (String) etatComboBox.getSelectedItem();
				String adresse = adresseField.getText().trim();
				double surface = Double.parseDouble(surfaceField.getText().trim());
				int pieces = Integer.parseInt(piecesField.getText().trim());
				double prix = Double.parseDouble(prixField.getText().trim());
				String statut = (String) statutComboBox.getSelectedItem();
				String proprietaire = proprietaireField.getText().trim();

				if (adresse.isEmpty()) {
					throw new IllegalArgumentException("L'adresse ne peut pas être vide.");
				}

				boolean success = controller.ajouterBien(type, etat, adresse, String.valueOf(surface),
						String.valueOf(pieces), String.valueOf(prix), statut, proprietaire);

				if (success) {
					JOptionPane.showMessageDialog(AjoutBien.this, "Bien ajouté avec succès !");
					tableModel
							.addRow(new Object[] { type, adresse, surface, prix, etat, statut, proprietaire });
					dispose();
				} else {
					JOptionPane.showMessageDialog(AjoutBien.this,
							"Erreur lors de l'ajout du bien dans la base de données.", "Erreur",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(AjoutBien.this, "Veuillez entrer des valeurs numériques valides.",
						"Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (IllegalArgumentException ex) {
				JOptionPane.showMessageDialog(AjoutBien.this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
