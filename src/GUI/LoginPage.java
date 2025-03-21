package GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Date;
import java.text.SimpleDateFormat;
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

	// Added for login validation
	private int loginAttempts = 0;
	private final int MAX_LOGIN_ATTEMPTS = 5;
	private Timer lockoutTimer;
	private JLabel errorMessageLabel = new JLabel("");

	// Color scheme - matching DashboardPage
	private Color primaryColor = new Color(25, 118, 210);  // Material blue
	private Color accentColor = new Color(230, 230, 230);  // Light gray
	private Color backgroundColor = new Color(250, 250, 250);  // Nearly white
	private Color textColor = new Color(33, 33, 33);  // Dark gray for text
	private Color buttonTextColor = Color.WHITE;
	private Color errorColor = new Color(211, 47, 47); // Error red color

	public LoginPage() {
		initComponents();
		setupUncaughtExceptionHandler();
	}

	private void setupUncaughtExceptionHandler() {
		// Set default exception handler for the AWT Event Dispatch Thread
		System.setProperty("sun.awt.exception.handler", LoginExceptionHandler.class.getName());

		// Also set a default handler for the current thread
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				handleLoginException(e);
			}
		});
	}

	// Custom exception handler class that will be used by the AWT Event Dispatch Thread
	public static class LoginExceptionHandler {
		public void handle(Throwable throwable) {
			// Get the current LoginPage instance
			for (Window window : Window.getWindows()) {
				if (window instanceof LoginPage && window.isVisible()) {
					((LoginPage) window).handleLoginException(throwable);
					break;
				}
			}
		}
	}

	// Handle login exceptions specifically
	public void handleLoginException(Throwable e) {
		if (e instanceof NullPointerException &&
				e.getMessage() != null &&
				e.getMessage().contains("Employee number cannot be null")) {

			// This is an authentication failure - show a friendly message
			SwingUtilities.invokeLater(() -> {
				// Increment login attempts
				loginAttempts++;

				// Update error message with attempts remaining
				int remainingAttempts = MAX_LOGIN_ATTEMPTS - loginAttempts;
				String message;

				if (remainingAttempts > 0) {
					message = "Wrong username or password. " +
							remainingAttempts + " attempt" + (remainingAttempts > 1 ? "s" : "") + " remaining.";
					errorMessageLabel.setText(message);

					// Show popup for invalid credentials
					JOptionPane.showMessageDialog(
							this,
							message,
							"Authentication Failed",
							JOptionPane.ERROR_MESSAGE
					);

					// Clear password field but keep username for retrying
					passwordField.setText("");
					// Highlight both fields to indicate error
					highlightFieldError(usernameField);
					highlightFieldError(passwordField);
					passwordField.requestFocus();

				} else {
					// Max attempts reached
					message = "Maximum login attempts exceeded. Account temporarily locked for 30 seconds.";
					errorMessageLabel.setText(message);
					loginButton.setEnabled(false);

					// Show popup for max attempts
					JOptionPane.showMessageDialog(
							this,
							message,
							"Account Locked",
							JOptionPane.ERROR_MESSAGE
					);

					// Set a timer to unlock after 30 seconds
					if (lockoutTimer != null && lockoutTimer.isRunning()) {
						lockoutTimer.stop();
					}

					lockoutTimer = new Timer(30000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							loginAttempts = 0;
							loginButton.setEnabled(true);
							errorMessageLabel.setText("Account unlocked. You may try again.");
							((Timer)e.getSource()).stop();
						}
					});
					lockoutTimer.setRepeats(false);
					lockoutTimer.start();
				}
			});
		} else {
			// For other exceptions, show generic error
			SwingUtilities.invokeLater(() -> {
				JOptionPane.showMessageDialog(
						this,
						"An unexpected error occurred: " + e.getMessage(),
						"System Error",
						JOptionPane.ERROR_MESSAGE
				);
			});
			e.printStackTrace();
		}
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

		// Error message label
		errorMessageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		errorMessageLabel.setForeground(errorColor);
		errorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		formPanel.add(errorMessageLabel, gbc);

		// Login button
		styleButton(loginButton, new Dimension(0, 40));
		gbc.gridx = 0;
		gbc.gridy = 3;
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
				validateAndLogin();
			}
		});

		// Add action listeners to text fields
		JTextField[] fields = { usernameField, passwordField };
		for (JTextField field : fields) {
			field.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					validateAndLogin();
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

	// Highlight field with error
	private void highlightFieldError(JTextField field) {
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(errorColor, 2),
				BorderFactory.createEmptyBorder(5, 10, 5, 10)));
	}

	// Reset all field styles
	private void resetFieldStyles() {
		styleTextField(usernameField);
		styleTextField(passwordField);
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

	// Validate form inputs before attempting login
	private boolean validateForm() {
		// Check for empty fields
		if (usernameField.getText().trim().isEmpty()) {
			errorMessageLabel.setText("Username cannot be empty");
			highlightFieldError(usernameField);
			usernameField.requestFocus();
			return false;
		}

		if (passwordField.getPassword().length == 0) {
			errorMessageLabel.setText("Password cannot be empty");
			highlightFieldError(passwordField);
			passwordField.requestFocus();
			return false;
		}

		// Add username format validation if needed
		// For example, you could check minimum length:
		if (usernameField.getText().trim().length() < 3) {
			errorMessageLabel.setText("Username must be at least 3 characters long");
			highlightFieldError(usernameField);
			usernameField.requestFocus();
			return false;
		}

		// Add password strength validation if needed
		// For example, you could check minimum length:
		if (passwordField.getPassword().length < 5) {
			errorMessageLabel.setText("Password must be at least 5 characters long");
			highlightFieldError(passwordField);
			passwordField.requestFocus();
			return false;
		}

		return true;
	}

	// New method to handle validation and login process
	private void validateAndLogin() {
		// Reset error message
		errorMessageLabel.setText("");

		// Reset any red border highlighting
		resetFieldStyles();

		// First validate the form
		if (!validateForm()) {
			// Show popup for validation errors
			JOptionPane.showMessageDialog(
					this,
					errorMessageLabel.getText(),
					"Validation Error",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		// Check if max attempts reached
		if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
			String message = "Maximum login attempts exceeded. Account temporarily locked for 30 seconds.";
			errorMessageLabel.setText(message);
			loginButton.setEnabled(false);

			// Show popup for max attempts
			JOptionPane.showMessageDialog(
					this,
					message,
					"Account Locked",
					JOptionPane.ERROR_MESSAGE
			);

			// Set a timer to unlock after 30 seconds
			if (lockoutTimer != null && lockoutTimer.isRunning()) {
				lockoutTimer.stop();
			}

			lockoutTimer = new Timer(30000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loginAttempts = 0;
					loginButton.setEnabled(true);
					errorMessageLabel.setText("Account unlocked. You may try again.");
					((Timer)e.getSource()).stop();
				}
			});
			lockoutTimer.setRepeats(false);
			lockoutTimer.start();
			return;
		}

		// Attempt to login - the NullPointerException will be caught by our handler
		try {
			// Log the attempt
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String timestamp = dateFormat.format(new Date());
			System.out.println("[" + timestamp + "] Login attempt for username: " + usernameField.getText());

			// This call may throw NullPointerException when employee number cannot be found
			// Our global exception handler will catch it
			EmployeeInformation userInfo = new EmployeeInformation(usernameField.getText(), new String(passwordField.getPassword()));

			// If we get here, login was successful
			if (userInfo.getLoginStatus()) {
				// Log successful login
				System.out.println("[" + timestamp + "] Authentication successful for username: " + usernameField.getText());

				// Login successful - show success message
				JOptionPane.showMessageDialog(
						this,
						"Login successful! Welcome to the MotorPH Portal.",
						"Login Successful",
						JOptionPane.INFORMATION_MESSAGE
				);

				// Proceed to appropriate dashboard
				dispose();
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						if (userInfo.getIsAdmin()) {
							// Proceed to the admin dashboard
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
								JOptionPane.showMessageDialog(
										null,
										"Error loading employee data: " + e.getMessage(),
										"System Error",
										JOptionPane.ERROR_MESSAGE
								);
								e.printStackTrace();
							}
						}
					}
				});
			} else {
				// Authentication failed but didn't throw exception
				// Handle as a normal failed login
				loginAttempts++;

				int remainingAttempts = MAX_LOGIN_ATTEMPTS - loginAttempts;
				String message = "Wrong username or password. " +
						remainingAttempts + " attempt" + (remainingAttempts > 1 ? "s" : "") + " remaining.";

				errorMessageLabel.setText(message);
				JOptionPane.showMessageDialog(
						this,
						message,
						"Authentication Failed",
						JOptionPane.ERROR_MESSAGE
				);

				passwordField.setText("");
				highlightFieldError(usernameField);
				highlightFieldError(passwordField);
				passwordField.requestFocus();
			}
		} catch (IOException e) {
			// Handle IO exceptions
			String message = "System error. Please try again later.";
			errorMessageLabel.setText(message);

			JOptionPane.showMessageDialog(
					this,
					message + "\nError details: " + e.getMessage(),
					"System Error",
					JOptionPane.ERROR_MESSAGE
			);

			e.printStackTrace();
		}
		// Note: NullPointerException is not caught here because it will be caught by
		// our global exception handler that shows a proper dialog
	}
}