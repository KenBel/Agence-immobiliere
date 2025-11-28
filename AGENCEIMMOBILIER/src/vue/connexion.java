package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import controleur.ConnexionController;
import java.net.URL;


public class connexion extends JFrame {

	private static final String BACKGROUND_IMAGE_PATH = "https://img.batiweb.com/repo-images/article/26396/tour-gratte-ciel.jpg";
	private static final String LOGO_IMAGE_PATH = "https://static.vecteezy.com/system/resources/thumbnails/023/495/220/small/real-estate-logo-design-png.png";

	public connexion() {
		setTitle("Connexion");
		setSize(1830, 820);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel backgroundPanel = new JPanel() {
			private Image backgroundImage;
			{
				try {
					backgroundImage = ImageIO.read(new URL(BACKGROUND_IMAGE_PATH));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (backgroundImage != null) {
					g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
				}
			}
		};
		backgroundPanel.setLayout(new GridBagLayout());

		JPanel mainPanel = new JPanel() {
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(158, 200, 255, 150));
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
			}
		};
		mainPanel.setOpaque(false);
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
		mainPanel.setMaximumSize(new Dimension(800, 500));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		try {
		    Image logoImage = ImageIO.read(new URL(LOGO_IMAGE_PATH)); // Charge l'image depuis l'URL
		    Image scaledImage = logoImage.getScaledInstance(135, 135, Image.SCALE_SMOOTH); // Redimensionnement
		    JLabel logoLabel = new JLabel(new ImageIcon(scaledImage)); 
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 1;
			mainPanel.add(logoLabel, gbc);
		} catch (IOException e) {
			e.printStackTrace();
		}

		JLabel titleLabel = new JLabel("Espace connexion");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridwidth = 1;
		mainPanel.add(titleLabel, gbc);

		JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
		usernameLabel.setFont(new Font("Arial", Font.BOLD, 20));
		usernameLabel.setForeground(Color.BLACK);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.gridwidth = 1;
		mainPanel.add(usernameLabel, gbc);

		JTextField usernameField = createStyledTextField("Nom d'utilisateur");
		usernameField.setFont(new Font("Arial", Font.BOLD, 15));
		usernameField.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(usernameField, gbc);

		JLabel passwordLabel = new JLabel("Mot de passe :");
		passwordLabel.setFont(new Font("Arial", Font.BOLD, 20));
		passwordLabel.setForeground(Color.BLACK);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.EAST;
		mainPanel.add(passwordLabel, gbc);

		JPasswordField passwordField = createStyledPasswordField("Mot de passe");
		passwordField.setFont(new Font("Arial", Font.BOLD, 15));
		passwordField.setPreferredSize(new Dimension(150, 30));
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(passwordField, gbc);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setOpaque(false);
		JButton loginButton = createStyledButton("Se connecter");
		JButton cancelButton = createStyledButton("Annuler");
		buttonPanel.add(loginButton);
		buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPanel.add(cancelButton);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(30, 10, 10, 10);
		mainPanel.add(buttonPanel, gbc);

		cancelButton.addActionListener(e -> System.exit(0));
		loginButton.addActionListener(e -> {
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());

			ConnexionController connexionController = new ConnexionController();

			if (connexionController.verifierUtilisateur(username, password)) {
				accueil accueilWindow = new accueil();
				accueilWindow.setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect.", "Erreur",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		backgroundPanel.add(mainPanel);

		setContentPane(backgroundPanel);

		setVisible(true);
	}

	private JTextField createStyledTextField(String placeholder) {
		JTextField textField = new JTextField(20);
		textField.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		textField.setText(placeholder);
		textField.setForeground(Color.GRAY);
		textField.addFocusListener(new FocusAdapter() {

			public void focusGained(FocusEvent e) {
				if (textField.getText().equals(placeholder)) {
					textField.setText("");
					textField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (textField.getText().isEmpty()) {
					textField.setForeground(Color.GRAY);
					textField.setText(placeholder);
				}
			}
		});
		return textField;
	}

	private JPasswordField createStyledPasswordField(String placeholder) {
		JPasswordField passwordField = new JPasswordField(20);
		passwordField
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		passwordField.setEchoChar((char) 0);
		passwordField.setText(placeholder);
		passwordField.setForeground(Color.GRAY);
		passwordField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (new String(passwordField.getPassword()).equals(placeholder)) {
					passwordField.setText("");
					passwordField.setEchoChar('•');
					passwordField.setForeground(Color.BLACK);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (passwordField.getPassword().length == 0) {
					passwordField.setEchoChar((char) 0);
					passwordField.setForeground(Color.GRAY);
					passwordField.setText(placeholder);
				}
			}
		});
		return passwordField;
	}

	private JButton createStyledButton(String text) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(250, 50));
		button.setBackground(new Color(60, 94, 179));
		button.setForeground(Color.BLACK);
		button.setFocusPainted(false);
		button.setFont(new Font("Arial", Font.BOLD, 20));
		return button;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(connexion::new);
	}
}
