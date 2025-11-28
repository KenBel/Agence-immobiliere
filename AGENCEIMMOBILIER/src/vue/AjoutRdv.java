package vue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import controleur.RdvController;
import controleur.TransacController;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AjoutRdv extends JFrame {

	private static final Color BACKGROUND_COLOR = new Color(124, 134, 150);
	private static final Color PRIMARY_COLOR = new Color(25, 53, 122);
	private static final Color BUTTON_COLOR = new Color(60, 94, 179);

	private JTextField proprietaireNomField;
	private JTextField proprietairePrenomField;
	private JTextField adrField;
	private JComboBox<String> typeComboBox;
	private JTextField trancheField;
	private JTextField dateField;
	private JTextField agentField;
	private JSpinner timeSpinner;
	private int idBien;

	public AjoutRdv(int idBien) {
		this.idBien = idBien;
		setTitle("Ajouter une transaction");
		setSize(700, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBackground(BACKGROUND_COLOR);

		JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
		mainPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

		JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		logoPanel.setBackground(BACKGROUND_COLOR);
		JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\Kenza\\Downloads\\agence.png"));
		JLabel titleLabel = new JLabel("Nouvelle visite");
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
		proprietaireNomField = new JTextField(20);
		proprietairePrenomField = new JTextField(20);
		proprietaireNomField.setFont(new Font("Arial", Font.BOLD, 15));
		proprietaireNomField.setPreferredSize(new Dimension(150, 30));
		proprietairePrenomField.setFont(new Font("Arial", Font.BOLD, 15));
		proprietairePrenomField.setPreferredSize(new Dimension(150, 30));
		addLabelAndField(panel, gbc, "Nom:", proprietaireNomField);
		addLabelAndField(panel, gbc, "Prénom:", proprietairePrenomField);

		adrField = new JTextField(20);
		adrField.setFont(new Font("Arial", Font.BOLD, 15));
		adrField.setPreferredSize(new Dimension(150, 30));

		typeComboBox = new JComboBox<>(new String[] { "Location", "Vente" });
		typeComboBox.setFont(new Font("Arial", Font.BOLD, 15));
		typeComboBox.setPreferredSize(new Dimension(150, 30));
		addLabelAndField(panel, gbc, "Type de la visite :", typeComboBox);

		dateField = new JTextField(20);
		dateField.setFont(new Font("Arial", Font.BOLD, 15));
		dateField.setPreferredSize(new Dimension(150, 30));
		addLabelAndField(panel, gbc, "Date (jj/mm/aaaa):", dateField);

		panel.add(new JLabel("Heure:"), gbc);
		SpinnerDateModel timeModel = new SpinnerDateModel();
		timeSpinner = new JSpinner(timeModel);
		timeSpinner.setFont(new Font("Arial", Font.BOLD, 15));
		timeSpinner.setPreferredSize(new Dimension(150, 30));
		JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
		timeSpinner.setEditor(timeEditor);
		addLabelAndField(panel, gbc, "Heure:", timeSpinner);

		agentField = new JTextField(20);
		agentField.setFont(new Font("Arial", Font.BOLD, 15));
		agentField.setPreferredSize(new Dimension(150, 30));
		addLabelAndField(panel, gbc, "Agent:", agentField);

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
		addButton.addActionListener(e -> ajouterRdv());

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

	private void ajouterRdv() {
	    try {
	        String clientNom = proprietaireNomField.getText().trim();
	        String agentNom = agentField.getText().trim();
	        String dateV = dateField.getText().trim();
	        String heureV = new SimpleDateFormat("HH:mm").format(timeSpinner.getValue());

	        RdvController controller = new RdvController();
	        int idClient = controller.getIdClientByNom(clientNom);
	        int idAgent = controller.getIdAgentByNom(agentNom);

	        System.out.println("Client: " + clientNom + ", Agent: " + agentNom + ", idBien: " + idBien);

	        if (idClient == -1 || idAgent == -1 || idBien == -1) {
	            JOptionPane.showMessageDialog(this, "Client, agent ou bien introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        boolean success = controller.ajouterRdv(dateV, heureV, idClient, idAgent, idBien);
	        if (success) {
	            JOptionPane.showMessageDialog(this, "Visite ajoutée avec succès !");
	            dispose();
	        } else {
	            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la visite.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Veuillez vérifier les champs saisis.", "Erreur", JOptionPane.ERROR_MESSAGE);
	        ex.printStackTrace();
	    }
	}


}
