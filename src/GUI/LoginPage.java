package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import GUI.admin.DashboardPage;
import GUI.employee.EmployeeDashboard;

public class LoginPage extends JFrame {
	private JTextField usernameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JLabel usernameLabel = new JLabel("Username");
	private JLabel passwordLabel = new JLabel("Password");
	private JButton loginButton = new JButton("Login");
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;

	// Color scheme - matching DashboardPage
	private Color primaryColor = new Color(25, 118, 210);  // Material blue
	private Color accentColor = new Color(230, 230, 230);  // Light gray
	private Color backgroundColor = new Color(250, 250, 250);  // Nearly white
	private Color textColor = new Color(33, 33, 33);  // Dark gray for text
	private Color buttonTextColor = Color.WHITE;

	public LoginPage() {
		initComponents();
	}

	private void initComponents() {
		// Set up the JFrame
		setTitle("MotorPH Portal");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setMinimumSize(new Dimension(450, 400));

		// Main panel with BorderLayout
		JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
		mainPanel.setBackground(backgroundColor);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

		// Header panel
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(backgroundColor);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		// Add logo/title
		JLabel titleLabel = new JLabel("MotorPH Employee Portal");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titleLabel.setForeground(primaryColor);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		headerPanel.add(titleLabel, BorderLayout.CENTER);

		// Create login form panel
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(Color.WHITE);
		formPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(accentColor),
				BorderFactory.createEmptyBorder(25, 25, 25, 25)));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(8, 5, 8, 5);
		gbc.gridwidth = 1;
		gbc.weightx = 0.2;

		// Username label
		usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		usernameLabel.setForeground(textColor);
		gbc.gridx = 0;
		gbc.gridy = 0;
		formPanel.add(usernameLabel, gbc);

		// Username field
		styleTextField(usernameField);
		gbc.gridx = 1;
		gbc.weightx = 0.8;
		formPanel.add(usernameField, gbc);

		// Password label
		passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		passwordLabel.setForeground(textColor);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.2;
		formPanel.add(passwordLabel, gbc);

		// Password field
		styleTextField(passwordField);
		gbc.gridx = 1;
		gbc.weightx = 0.8;
		formPanel.add(passwordField, gbc);

		// Login button
		styleButton(loginButton, new Dimension(0, 40));
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 5, 8, 5);
		formPanel.add(loginButton, gbc);

		// Add panels to main panel
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Set content pane
		setContentPane(mainPanel);

		// Size and position frame
		pack();
		setLocationRelativeTo(null);

		// Add action listeners
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					checkLoginCredentials();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// Add action listeners to text fields
		JTextField[] fields = { usernameField, passwordField };
		for (JTextField field : fields) {
			field.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						checkLoginCredentials();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
		}
	}

	private void styleTextField(JTextField textField) {
		textField.setPreferredSize(new Dimension(200, 35));
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(accentColor),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
	}

	private void styleButton(JButton button, Dimension size) {
		button.setPreferredSize(size);
		button.setBackground(primaryColor);
		button.setForeground(buttonTextColor);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Add hover effect
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(button.getBackground().darker());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(primaryColor);
			}
		});
	}

	// Keep the original method to maintain functionality
	public void checkLoginCredentials() throws IOException {
		// Create an EmployeeInformation object with the login credentials
		EmployeeInformation userInfo = new EmployeeInformation(usernameField.getText(), new String(passwordField.getPassword()));

		// Show dialog if the user credentials provided are incorrect.
		if (!userInfo.getLoginStatus()) {
			JOptionPane.showMessageDialog(new JFrame(""), "User credentials incorrect.", "Login Failed",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		// Close the last page
		dispose();
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				if (userInfo.getIsAdmin()) {
					// Proceed to the next page once logged in
					// Create and display the form
					new DashboardPage().setVisible(true);
				} else {
					try {
						// Call constructor
						employeeGI = new GovernmentIdentification(userInfo.getEmployeeNumber());
						employeeComp = new Compensation(userInfo.getEmployeeNumber());

						// Set all the data for the logged in employee
						EmployeeInformation.setEmployeeInformationObject(userInfo.getEmployeeNumber(), employeeGI,
								employeeComp);

						// If user is an employee, go to employee dashboard page
						new EmployeeDashboard(employeeGI, employeeComp).setVisible(true);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}