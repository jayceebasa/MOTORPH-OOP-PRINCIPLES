package GUI.admin;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.border.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import GUI.LoginPage;
import UtilityClasses.JsonFileHandler;

public class DashboardPage extends JFrame {

	// UI Components (keeping the same components)
	private JLabel employeeId = new JLabel("Employee ID:");
	private JLabel lastName = new JLabel("Last Name:");
	private JLabel lastNameValue = new JLabel("");
	private JLabel firstName = new JLabel("First Name:");
	private JLabel firstNameValue = new JLabel("");
	private JLabel birthday = new JLabel("Birthday:");
	private JLabel birthdayValue = new JLabel("");
	private JLabel address = new JLabel("Address:");
	private JLabel addressValue = new JLabel("");
	private JLabel phoneNumber = new JLabel("Phone Number:");
	private JLabel phoneNumberValue = new JLabel("");
	private JLabel sssNumber = new JLabel("SSS Number:");
	private JLabel sssNumberValue = new JLabel("");
	private JLabel philhealthNumber = new JLabel("PhilHealth Number:");
	private JLabel philhealthNumberValue = new JLabel("");
	private JLabel tinNumber = new JLabel("TIN Number:");
	private JLabel tinNumberValue = new JLabel("");
	private JLabel pagibigNumber = new JLabel("Pag-IBIG Number:");
	private JLabel pagibigNumberValue = new JLabel("");
	private JLabel status = new JLabel("Status:");
	private JLabel statusValue = new JLabel("");
	private JLabel position = new JLabel("Position:");
	private JLabel positionValue = new JLabel("");
	private JLabel immediateSupervisor = new JLabel("Immediate Supervisor:");
	private JLabel immediateSupervisorValue = new JLabel("");
	private JLabel hourlyRate = new JLabel("Hourly Rate:");
	private JLabel hourlyRateValue = new JLabel("");

	// Interactibles
	private JTextField employeeIdField = new JTextField(30);
	private JButton searchButton = new JButton("Search");
	private JButton computeButton = new JButton("Compute Salary");
	private JButton employeeListButton = new JButton("Employee List");
	private JButton leaveRequestButton = new JButton("Leave Requests");
	private JButton logoutButton = new JButton("Log Out");
	private JLabel[] labels = { lastNameValue, firstNameValue, birthdayValue, addressValue, phoneNumberValue,
			sssNumberValue, philhealthNumberValue, tinNumberValue, pagibigNumberValue, statusValue, positionValue,
			immediateSupervisorValue, hourlyRateValue };
	private String[] stringifiedLabels = { "employeeNum", "last_name", "first_name", "birthday", "address",
			"phone_number", "SSS", "Philhealth", "TIN", "Pag-ibig", "Status", "Position", "immediate_supervisor",
			"hourly_rate" };

	// Employee data objects
	GovernmentIdentification employeeGI = new GovernmentIdentification(employeeIdField.getText());
	Compensation employeeComp = new Compensation(employeeIdField.getText());

	// Panels
	private JPanel mainPanel;
	private JPanel headerPanel;
	private JPanel searchPanel;
	private JPanel actionsPanel;
	private JPanel personalInfoPanel;
	private JPanel employmentInfoPanel;
	private JPanel governmentIDsPanel;
	private JPanel addressPanel;
	private JPanel contentPanel;

	// Color scheme
	private Color primaryColor = new Color(25, 118, 210);  // Material blue
	private Color accentColor = new Color(230, 230, 230);  // Light gray
	private Color backgroundColor = new Color(250, 250, 250);  // Nearly white
	private Color textColor = new Color(33, 33, 33);  // Dark gray for text
	private Color buttonTextColor = Color.WHITE;

	public DashboardPage() {
		initComponents();
	}

	private void initComponents() {
		// Set basic frame properties
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("MotorPH Payroll System | Dashboard");
		setMinimumSize(new Dimension(1000, 900));

		// Initialize and configure panels
		mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(backgroundColor);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		setupHeaderPanel();
		setupSearchPanel();
		setupActionsPanel();
		setupContentPanel();

		// Add panels to main panel
		mainPanel.add(headerPanel, BorderLayout.NORTH);
		mainPanel.add(contentPanel, BorderLayout.CENTER);

		// Add main panel to frame
		setContentPane(mainPanel);

		// Size and position frame
		pack();
		setLocationRelativeTo(null);
	}

	private void setupHeaderPanel() {
		headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(backgroundColor);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		// Title label
		JLabel titleLabel = new JLabel("MotorPH Employee Dashboard");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titleLabel.setForeground(primaryColor);

		// Logout button
		styleButton(logoutButton, new Dimension(120, 30));
		logoutButton.setBackground(new Color(211, 47, 47)); // Red color for logout

		// Add components to header
		headerPanel.add(titleLabel, BorderLayout.WEST);
		headerPanel.add(logoutButton, BorderLayout.EAST);

		// Add action listener to logout button
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				logoutButtonActionPerformed(evt);
			}
		});
	}

	private void setupSearchPanel() {
		searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		searchPanel.setBackground(backgroundColor);

		// Style components
		employeeId.setFont(new Font("Segoe UI", Font.BOLD, 14));
		employeeId.setForeground(textColor);

		employeeIdField.setPreferredSize(new Dimension(200, 35));
		employeeIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		employeeIdField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(accentColor),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		styleButton(searchButton, new Dimension(100, 35));
		styleButton(computeButton, new Dimension(150, 35));
		computeButton.setEnabled(false);

		// Add components to panel
		searchPanel.add(employeeId);
		searchPanel.add(employeeIdField);
		searchPanel.add(searchButton);
		searchPanel.add(computeButton);

		// Add action listeners
		employeeIdField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				employeeIdFieldActionPerformed(evt);
			}
		});

		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				searchButtonActionPerformed(evt);
			}
		});

		computeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				computeButtonActionPerformed(evt);
			}
		});
	}

	private void setupActionsPanel() {
		actionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		actionsPanel.setBackground(backgroundColor);

		// Style buttons
		styleButton(employeeListButton, new Dimension(150, 35));
		styleButton(leaveRequestButton, new Dimension(150, 35));

		// Add buttons to panel
		actionsPanel.add(employeeListButton);
		actionsPanel.add(leaveRequestButton);

		// Add action listeners
		employeeListButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				employeeListButtonActionPerformed(evt);
			}
		});

		leaveRequestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					leaveRequestButtonActionPerformed(evt);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void setupContentPanel() {
		contentPanel = new JPanel(new BorderLayout(10, 10));
		contentPanel.setBackground(backgroundColor);

		// Add search and actions panel at the top of content area
		JPanel topContentPanel = new JPanel(new GridLayout(2, 1, 0, 10));
		topContentPanel.setBackground(backgroundColor);
		topContentPanel.add(searchPanel);
		topContentPanel.add(actionsPanel);
		contentPanel.add(topContentPanel, BorderLayout.NORTH);

		// Set up the information panels
		JPanel infoPanel = new JPanel(new GridBagLayout());
		infoPanel.setBackground(backgroundColor);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.weightx = 1.0;

		// Create and set up information panels
		setupPersonalInfoPanel();
		setupEmploymentInfoPanel();
		setupGovernmentIDsPanel();
		setupAddressPanel();

		// First row: personal and employment info panels
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 1.0;
		infoPanel.add(personalInfoPanel, gbc);

		gbc.gridx = 1;
		infoPanel.add(employmentInfoPanel, gbc);

		// Second row: government IDs panel spans two columns
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		infoPanel.add(governmentIDsPanel, gbc);

		// Third row: address panel spans two columns
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weighty = 0.5;
		infoPanel.add(addressPanel, gbc);

		// Add the information panels to the content area
		contentPanel.add(infoPanel, BorderLayout.CENTER);
	}

	private void setupPersonalInfoPanel() {
		personalInfoPanel = createInfoPanel("Personal Information");

		// Create a grid layout for the labels
		JPanel labelsPanel = new JPanel(new GridLayout(0, 1, 5, 10));
		labelsPanel.setBackground(backgroundColor);
		labelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Add field-value pairs
		addFieldValuePair(labelsPanel, firstName, firstNameValue);
		addFieldValuePair(labelsPanel, lastName, lastNameValue);
		addFieldValuePair(labelsPanel, birthday, birthdayValue);
		addFieldValuePair(labelsPanel, phoneNumber, phoneNumberValue);

		personalInfoPanel.add(labelsPanel, BorderLayout.CENTER);
	}

	private void setupEmploymentInfoPanel() {
		employmentInfoPanel = createInfoPanel("Employment Information");

		JPanel labelsPanel = new JPanel(new GridLayout(0, 1, 5, 10));
		labelsPanel.setBackground(backgroundColor);
		labelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		addFieldValuePair(labelsPanel, status, statusValue);
		addFieldValuePair(labelsPanel, position, positionValue);
		addFieldValuePair(labelsPanel, immediateSupervisor, immediateSupervisorValue);
		addFieldValuePair(labelsPanel, hourlyRate, hourlyRateValue);

		employmentInfoPanel.add(labelsPanel, BorderLayout.CENTER);
	}

	private void setupGovernmentIDsPanel() {
		governmentIDsPanel = createInfoPanel("Government IDs");

		JPanel labelsPanel = new JPanel(new GridLayout(2, 2, 20, 10));
		labelsPanel.setBackground(backgroundColor);
		labelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JPanel sssPanel = new JPanel(new GridLayout(0, 1));
		sssPanel.setBackground(backgroundColor);
		addFieldValuePair(sssPanel, sssNumber, sssNumberValue);

		JPanel philhealthPanel = new JPanel(new GridLayout(0, 1));
		philhealthPanel.setBackground(backgroundColor);
		addFieldValuePair(philhealthPanel, philhealthNumber, philhealthNumberValue);

		JPanel pagibigPanel = new JPanel(new GridLayout(0, 1));
		pagibigPanel.setBackground(backgroundColor);
		addFieldValuePair(pagibigPanel, pagibigNumber, pagibigNumberValue);

		JPanel tinPanel = new JPanel(new GridLayout(0, 1));
		tinPanel.setBackground(backgroundColor);
		addFieldValuePair(tinPanel, tinNumber, tinNumberValue);

		labelsPanel.add(sssPanel);
		labelsPanel.add(philhealthPanel);
		labelsPanel.add(pagibigPanel);
		labelsPanel.add(tinPanel);

		governmentIDsPanel.add(labelsPanel, BorderLayout.CENTER);
	}

	private void setupAddressPanel() {
		addressPanel = createInfoPanel("Address");

		JPanel labelsPanel = new JPanel(new BorderLayout());
		labelsPanel.setBackground(backgroundColor);
		labelsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		address.setFont(new Font("Segoe UI", Font.BOLD, 14));
		address.setForeground(textColor);

		addressValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		addressValue.setForeground(textColor);

		labelsPanel.add(address, BorderLayout.NORTH);
		labelsPanel.add(addressValue, BorderLayout.CENTER);

		addressPanel.add(labelsPanel, BorderLayout.CENTER);
	}

	private JPanel createInfoPanel(String title) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(accentColor),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// Create title panel with gradient background
		JPanel titlePanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gp = new GradientPaint(0, 0, primaryColor, getWidth(), 0,
						new Color(primaryColor.getRed(), primaryColor.getGreen(), primaryColor.getBlue(), 200));
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		titlePanel.setPreferredSize(new Dimension(0, 40));
		titlePanel.setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel(title);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		titlePanel.add(titleLabel, BorderLayout.CENTER);

		panel.add(titlePanel, BorderLayout.NORTH);

		return panel;
	}

	private void addFieldValuePair(JPanel panel, JLabel fieldLabel, JLabel valueLabel) {
		fieldLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
		fieldLabel.setForeground(textColor);

		valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		valueLabel.setForeground(textColor);

		panel.add(fieldLabel);
		panel.add(valueLabel);
	}

	private void styleButton(JButton button, Dimension size) {
		button.setPreferredSize(size);
		button.setBackground(primaryColor);
		button.setForeground(buttonTextColor);
		button.setFont(new Font("Segoe UI", Font.BOLD, 12));
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
				if (button == logoutButton) {
					button.setBackground(new Color(211, 47, 47));
				}
			}
		});
	}

	// Keep all the original action methods to maintain functionality
	private void employeeIdFieldActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			JsonObject employeeData = JsonFileHandler.nameIterator(JsonFileHandler.getEmployeesJSON(), "employeeNum",
					employeeIdField.getText());
			setLabelValues(employeeData);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			JsonObject employeeData = JsonFileHandler.nameIterator(JsonFileHandler.getEmployeesJSON(), "employeeNum",
					employeeIdField.getText());
			setLabelValues(employeeData);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void computeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		openCalculator(employeeComp);
	}

	private void employeeListButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				dispose();
				new EmployeeListPage().setVisible(true);
			}
		});
	}

	private void leaveRequestButtonActionPerformed(java.awt.event.ActionEvent evt) throws FileNotFoundException {
		FileReader reader = new FileReader(JsonFileHandler.getLeaveRequestJsonPath());
		JsonElement jsonElement = JsonParser.parseReader(reader);

		if (!jsonElement.isJsonArray()) {
			JOptionPane.showMessageDialog(this, "No leave requests found.", "Empty Data",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				dispose();
				try {
					new LeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				dispose();
				new LoginPage().setVisible(true);
			}
		});
	}

	public Boolean checkForEmployee(JsonObject employeeData) throws IOException {
		if (employeeIdField.getText().equals("")) {
			JOptionPane.showMessageDialog(new JFrame(""), "Please provide an employee number.", "No input detected",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (employeeData == null) {
			JOptionPane.showMessageDialog(new JFrame(""), "No user found.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	public void setLabelValues(JsonObject employeeData) throws IOException {
		if (!checkForEmployee(employeeData)) {
			return;
		}

		JsonFileHandler.labelAssigner(employeeData, stringifiedLabels, labels);
		EmployeeInformation.setEmployeeInformationObject(employeeIdField.getText(), employeeGI, employeeComp);
		computeButton.setEnabled(true);
	}

	public void openCalculator(Compensation employeeComp) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new CalculatorPage(employeeComp).setVisible(true);
			}
		});
	}

	public void setEmployeeInformationObject(JsonObject employeeData) {
		Gson gson = new Gson();
		GovernmentIdentification employeeGovInfo = gson.fromJson(employeeData, GovernmentIdentification.class);
		Compensation employeeCompInfo = gson.fromJson(employeeData, Compensation.class);

		employeeGI.setSSSNumber(employeeGovInfo.getSSSNumber());
		employeeGI.setPhilHealthNumber(employeeGovInfo.getPhilHealthNumber());
		employeeGI.setPagibigNumber(employeeGovInfo.getPagibigNumber());
		employeeGI.setTinNumber(employeeGovInfo.getTinNumber());

		employeeComp.setBasicSalary(employeeCompInfo.getBasicSalary());
		employeeComp.setClothingAllowance(employeeCompInfo.getClothingAllowance());
		employeeComp.setGrossSemiMonthlyRate(employeeCompInfo.getGrossSemiMonthlyRate());
		employeeComp.setPhoneAllowance(employeeCompInfo.getPhoneAllowance());
		employeeComp.setRiceSubsidy(employeeCompInfo.getRiceSubsidy());
		employeeComp.setHourlyRate(employeeCompInfo.getHourlyRate());
	}
}