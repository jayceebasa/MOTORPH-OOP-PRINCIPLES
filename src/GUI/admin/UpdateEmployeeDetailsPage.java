package GUI.admin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Classes.Compensation;
import Classes.GovernmentIdentification;
import UtilityClasses.CustomTooltip;
import UtilityClasses.DataValidators;
import UtilityClasses.JsonFileHandler;

public class UpdateEmployeeDetailsPage extends JFrame {

	// Design Constants
	private static final Color PRIMARY_COLOR = new Color(25, 118, 210);  // Material blue
	private static final Color ACCENT_COLOR = new Color(230, 230, 230);  // Light gray
	private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Nearly white
	private static final Color TEXT_COLOR = new Color(33, 33, 33);  // Dark gray for text
	private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
	private static final Color SUCCESS_COLOR = new Color(76, 175, 80); // Green for success actions
	private static final Color ERROR_COLOR = new Color(211, 47, 47);  // Red for errors

	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 18);
	private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
	private static final Font FIELD_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

	// Main panels
	private JPanel mainPanel;
	private JPanel personalInfoPanel;
	private JPanel govtIDsPanel;
	private JPanel employmentInfoPanel;
	private JPanel compensationPanel;

	// Labels and Fields for Personal Information
	private JLabel titleLabel;
	private JLabel personalInfoSectionLabel;
	private JLabel employeeNumberLabel;
	private JTextField employeeNumberField;
	private JLabel firstNameLabel;
	private JTextField firstNameField;
	private JLabel lastNameLabel;
	private JTextField lastNameField;
	private JLabel birthdayLabel;
	private JTextField birthdayField;
	private JLabel addressLabel;
	private JTextField addressField;
	private JLabel phoneNumberLabel;
	private JTextField phoneNumberField;

	// Labels and Fields for Government IDs
	private JLabel govtIDsSectionLabel;
	private JLabel sssLabel;
	private JTextField sssField;
	private JLabel philhealthLabel;
	private JTextField philhealthField;
	private JLabel tinLabel;
	private JTextField tinField;
	private JLabel pagibigLabel;
	private JTextField pagibigField;

	// Labels and Fields for Employment Information
	private JLabel employmentInfoSectionLabel;
	private JLabel statusLabel;
	private JTextField statusField;
	private JLabel positionLabel;
	private JTextField positionField;
	private JLabel immediateSupervisorLabel;
	private JTextField immediateSupervisorField;

	// Labels and Fields for Compensation
	private JLabel compensationSectionLabel;
	private JLabel basicSalaryLabel;
	private JTextField basicSalaryField;
	private JLabel riceSubsidyLabel;
	private JTextField riceSubsidyField;
	private JLabel phoneAllowanceLabel;
	private JTextField phoneAllowanceField;
	private JLabel clothingAllowanceLabel;
	private JTextField clothingAllowanceField;
	private JLabel grossSemiMonthlyRateLabel;
	private JTextField grossSemiMonthlyRateField;
	private JLabel hourlyRateLabel;
	private JTextField hourlyRateField;

	// Buttons
	private JButton goBackButton;
	private JButton confirmButton;

	// Employee data objects
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	private DecimalFormat decimalFormatter = new DecimalFormat();
	
	
	public UpdateEmployeeDetailsPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;
		initComponents();
		loadEmployeeData();
	}

	private void initComponents() {
		// Set frame properties
		setTitle("MotorPH Payroll System | Update Employee Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(BACKGROUND_COLOR);
		setSize(1000, 800);
		setLocationRelativeTo(null);

		// Create main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(null); // Using absolute positioning for more precise control
		mainPanel.setBackground(BACKGROUND_COLOR);

		// Create title
		titleLabel = new JLabel("Update Employee Details");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(PRIMARY_COLOR);
		titleLabel.setBounds(30, 20, 400, 30);

		// Create navigation buttons
		goBackButton = createButton("Go Back", PRIMARY_COLOR, 30, 70, 150, 40);
		goBackButton.addActionListener(e -> {
			try {
				goBackToEmployeeListButtonActionPerformed();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		confirmButton = createButton("Save Changes", SUCCESS_COLOR, 820, 70, 150, 40);
		confirmButton.addActionListener(e -> {
			try {
				confirmButtonActionPerformed();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		// Create sections
		createPersonalInfoSection();
		createGovernmentIDsSection();
		createEmploymentInfoSection();
		createCompensationSection();

		// Add all components to main panel
		mainPanel.add(titleLabel);
		mainPanel.add(goBackButton);
		mainPanel.add(confirmButton);

		// Add main panel to frame
		add(mainPanel);
	}

	private void loadEmployeeData() {
		// We need to separate tooltip setup from data loading to avoid the error
		// First set the data values without adding tooltip listeners
		employeeNumberField.setText(employeeGI.getEmployeeNumber());
		firstNameField.setText(employeeGI.getFirstName());
		lastNameField.setText(employeeGI.getLastName());
		birthdayField.setText(employeeGI.getBirthday());
		addressField.setText(employeeGI.getAddress());
		phoneNumberField.setText(employeeGI.getPhoneNumber());

		sssField.setText(employeeGI.getSSSNumber());
		philhealthField.setText(employeeGI.getPhilHealthNumber());
		tinField.setText(employeeGI.getTinNumber());
		pagibigField.setText(employeeGI.getPagibigNumber());

		statusField.setText(employeeGI.getStatus());
		positionField.setText(employeeGI.getPosition());
		immediateSupervisorField.setText(employeeGI.getImmediateSupervisor());

		basicSalaryField.setText(Double.toString(employeeComp.getBasicSalary()));
		riceSubsidyField.setText(Double.toString(employeeComp.getRiceSubsidy()));
		phoneAllowanceField.setText(Double.toString(employeeComp.getPhoneAllowance()));
		clothingAllowanceField.setText(Double.toString(employeeComp.getClothingAllowance()));
		grossSemiMonthlyRateField.setText(Double.toString(employeeComp.getGrossSemiMonthlyRate()));
		hourlyRateField.setText(Double.toString(employeeComp.getHourlyRate()));

		// Now schedule the tooltip setup for later, after the component is visible
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Add tooltips after the UI is fully rendered
				addTooltip(birthdayField, "Format: MM/dd/yyyy");
				addTooltip(phoneNumberField, "Format: xxx-xxx-xxxx");
				addTooltip(sssField, "Format: xx-xxxxxxx-x");
				addTooltip(philhealthField, "Max Length: 12");
				addTooltip(tinField, "Format: xxx-xxx-xxx-xxx");
				addTooltip(pagibigField, "Max Length: 12");
			}
		});
	}

	private void createPersonalInfoSection() {
		// Create personal information panel
		personalInfoPanel = createPanelWithTitle("Personal Information", 30, 130, 450, 280);

		// Create labels and fields
		int labelX = 20;
		int fieldX = 180;
		int startY = 50;
		int spacing = 35;

		personalInfoSectionLabel = createSectionLabel("Personal Information", 20, 15);

		employeeNumberLabel = createLabel("Employee Number:", labelX, startY);
		employeeNumberField = createTextField(fieldX, startY, 230, false);

		firstNameLabel = createLabel("First Name:", labelX, startY + spacing);
		firstNameField = createTextField(fieldX, startY + spacing, 230, true);

		lastNameLabel = createLabel("Last Name:", labelX, startY + spacing * 2);
		lastNameField = createTextField(fieldX, startY + spacing * 2, 230, true);

		birthdayLabel = createLabel("Birthday:", labelX, startY + spacing * 3);
		birthdayField = createTextField(fieldX, startY + spacing * 3, 230, true);
		addTooltip(birthdayField, "Format: MM/dd/yyyy");

		addressLabel = createLabel("Address:", labelX, startY + spacing * 4);
		addressField = createTextField(fieldX, startY + spacing * 4, 230, true);

		phoneNumberLabel = createLabel("Phone Number:", labelX, startY + spacing * 5);
		phoneNumberField = createTextField(fieldX, startY + spacing * 5, 230, true);
		addTooltip(phoneNumberField, "Format: xxx-xxx-xxxx");

		// Add components to panel
		personalInfoPanel.add(personalInfoSectionLabel);
		personalInfoPanel.add(employeeNumberLabel);
		personalInfoPanel.add(employeeNumberField);
		personalInfoPanel.add(firstNameLabel);
		personalInfoPanel.add(firstNameField);
		personalInfoPanel.add(lastNameLabel);
		personalInfoPanel.add(lastNameField);
		personalInfoPanel.add(birthdayLabel);
		personalInfoPanel.add(birthdayField);
		personalInfoPanel.add(addressLabel);
		personalInfoPanel.add(addressField);
		personalInfoPanel.add(phoneNumberLabel);
		personalInfoPanel.add(phoneNumberField);

		// Add panel to main panel
		mainPanel.add(personalInfoPanel);
	}

	private void createGovernmentIDsSection() {
		// Create government IDs panel
		govtIDsPanel = createPanelWithTitle("Government IDs", 520, 130, 450, 280);

		// Create labels and fields
		int labelX = 20;
		int fieldX = 180;
		int startY = 50;
		int spacing = 35;

		govtIDsSectionLabel = createSectionLabel("Government IDs", 20, 15);

		sssLabel = createLabel("SSS Number:", labelX, startY);
		sssField = createTextField(fieldX, startY, 230, true);
		addTooltip(sssField, "Format: xx-xxxxxxx-x");

		philhealthLabel = createLabel("PhilHealth Number:", labelX, startY + spacing);
		philhealthField = createTextField(fieldX, startY + spacing, 230, true);
		addTooltip(philhealthField, "Max Length: 12");

		tinLabel = createLabel("TIN Number:", labelX, startY + spacing * 2);
		tinField = createTextField(fieldX, startY + spacing * 2, 230, true);
		addTooltip(tinField, "Format: xxx-xxx-xxx-xxx");

		pagibigLabel = createLabel("Pag-ibig Number:", labelX, startY + spacing * 3);
		pagibigField = createTextField(fieldX, startY + spacing * 3, 230, true);
		addTooltip(pagibigField, "Max Length: 12");

		// Add components to panel
		govtIDsPanel.add(govtIDsSectionLabel);
		govtIDsPanel.add(sssLabel);
		govtIDsPanel.add(sssField);
		govtIDsPanel.add(philhealthLabel);
		govtIDsPanel.add(philhealthField);
		govtIDsPanel.add(tinLabel);
		govtIDsPanel.add(tinField);
		govtIDsPanel.add(pagibigLabel);
		govtIDsPanel.add(pagibigField);

		// Add panel to main panel
		mainPanel.add(govtIDsPanel);
	}

	private void createEmploymentInfoSection() {
		// Create employment information panel
		employmentInfoPanel = createPanelWithTitle("Employment Information", 30, 440, 450, 280);

		// Create labels and fields
		int labelX = 20;
		int fieldX = 180;
		int startY = 50;
		int spacing = 35;

		employmentInfoSectionLabel = createSectionLabel("Employment Information", 20, 15);

		statusLabel = createLabel("Status:", labelX, startY);
		statusField = createTextField(fieldX, startY, 230, true);

		positionLabel = createLabel("Position:", labelX, startY + spacing);
		positionField = createTextField(fieldX, startY + spacing, 230, true);

		immediateSupervisorLabel = createLabel("Supervisor:", labelX, startY + spacing * 2);
		immediateSupervisorField = createTextField(fieldX, startY + spacing * 2, 230, true);

		// Add components to panel
		employmentInfoPanel.add(employmentInfoSectionLabel);
		employmentInfoPanel.add(statusLabel);
		employmentInfoPanel.add(statusField);
		employmentInfoPanel.add(positionLabel);
		employmentInfoPanel.add(positionField);
		employmentInfoPanel.add(immediateSupervisorLabel);
		employmentInfoPanel.add(immediateSupervisorField);

		// Add panel to main panel
		mainPanel.add(employmentInfoPanel);
	}

	private void createCompensationSection() {
		// Create compensation panel
		compensationPanel = createPanelWithTitle("Compensation", 520, 440, 450, 280);

		// Create labels and fields
		int labelX = 20;
		int fieldX = 180;
		int startY = 50;
		int spacing = 35;

		compensationSectionLabel = createSectionLabel("Compensation", 20, 15);

		basicSalaryLabel = createLabel("Basic Salary:", labelX, startY);
		basicSalaryField = createTextField(fieldX, startY, 230, true);

		riceSubsidyLabel = createLabel("Rice Subsidy:", labelX, startY + spacing);
		riceSubsidyField = createTextField(fieldX, startY + spacing, 230, true);

		phoneAllowanceLabel = createLabel("Phone Allowance:", labelX, startY + spacing * 2);
		phoneAllowanceField = createTextField(fieldX, startY + spacing * 2, 230, true);

		clothingAllowanceLabel = createLabel("Clothing Allowance:", labelX, startY + spacing * 3);
		clothingAllowanceField = createTextField(fieldX, startY + spacing * 3, 230, true);

		grossSemiMonthlyRateLabel = createLabel("Semi-Monthly Rate:", labelX, startY + spacing * 4);
		grossSemiMonthlyRateField = createTextField(fieldX, startY + spacing * 4, 230, true);

		hourlyRateLabel = createLabel("Hourly Rate:", labelX, startY + spacing * 5);
		hourlyRateField = createTextField(fieldX, startY + spacing * 5, 230, true);

		// Add components to panel
		compensationPanel.add(compensationSectionLabel);
		compensationPanel.add(basicSalaryLabel);
		compensationPanel.add(basicSalaryField);
		compensationPanel.add(riceSubsidyLabel);
		compensationPanel.add(riceSubsidyField);
		compensationPanel.add(phoneAllowanceLabel);
		compensationPanel.add(phoneAllowanceField);
		compensationPanel.add(clothingAllowanceLabel);
		compensationPanel.add(clothingAllowanceField);
		compensationPanel.add(grossSemiMonthlyRateLabel);
		compensationPanel.add(grossSemiMonthlyRateField);
		compensationPanel.add(hourlyRateLabel);
		compensationPanel.add(hourlyRateField);

		// Add panel to main panel
		mainPanel.add(compensationPanel);
	}

	// Helper methods for creating UI components
	private JPanel createPanelWithTitle(String title, int x, int y, int width, int height) {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2d.setColor(getBackground());
				g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
			}
		};
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
		panel.setLayout(null);
		panel.setBounds(x, y, width, height);
		return panel;
	}

	private JLabel createSectionLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setFont(SECTION_FONT);
		label.setForeground(PRIMARY_COLOR);
		label.setBounds(x, y, 300, 30);
		return label;
	}

	private JLabel createLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setFont(LABEL_FONT);
		label.setForeground(TEXT_COLOR);
		label.setBounds(x, y, 150, 25);
		return label;
	}

	private JTextField createTextField(int x, int y, int width, boolean editable) {
		JTextField field = new JTextField();
		field.setFont(FIELD_FONT);
		field.setBounds(x, y, width, 30);
		field.setEditable(editable);
		field.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(ACCENT_COLOR),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		// Gray out disabled fields
		if (!editable) {
			field.setBackground(new Color(240, 240, 240));
		}

		return field;
	}

	private JButton createButton(String text, Color bgColor, int x, int y, int width, int height) {
		JButton button = new JButton(text);
		button.setFont(BUTTON_FONT);
		button.setBackground(bgColor);
		button.setForeground(BUTTON_TEXT_COLOR);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setBounds(x, y, width, height);

		// Add hover effect
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(bgColor.darker());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(bgColor);
			}
		});

		return button;
	}

	private void addTooltip(final JTextField field, final String tooltipText) {
		// Only add tooltip functionality after the component is visible
		field.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				// Only show tooltip if the component is visible on screen
				if (field.isShowing()) {
					CustomTooltip.showCustomTooltip(field, tooltipText);
				}
			}
		});

		field.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				// Only show tooltip if the component is visible on screen
				if (field.isShowing()) {
					CustomTooltip.showCustomTooltip(field, tooltipText);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				CustomTooltip.hideCustomTooltip();
			}
		});
	}

	private void confirmButtonActionPerformed() throws IOException {
		// Validate fields
		StringBuilder errorMessage = new StringBuilder();

		// Get all input fields
		JTextField[] stringOnlyFields = {
				firstNameField, lastNameField, positionField,
				immediateSupervisorField, statusField
		};

		JTextField[] numericFields = {
				basicSalaryField, riceSubsidyField, phoneAllowanceField,
				clothingAllowanceField, grossSemiMonthlyRateField, hourlyRateField,
				pagibigField, philhealthField
		};

		JTextField[] allFields = {
				firstNameField, lastNameField, birthdayField, addressField,
				phoneNumberField, sssField, philhealthField, tinField, pagibigField,
				statusField, positionField, immediateSupervisorField, basicSalaryField,
				riceSubsidyField, phoneAllowanceField, clothingAllowanceField,
				grossSemiMonthlyRateField, hourlyRateField
		};

		// Check if fields are empty
		if (Arrays.stream(allFields).anyMatch(field -> field.getText().trim().isEmpty())) {
			errorMessage.append("Please fill in all fields.\n");
		}

		// Check if text fields contain only text
		if (Arrays.stream(stringOnlyFields)
				.anyMatch(field -> !DataValidators.isPureString(field.getText()))) {
			errorMessage.append("Name, position, supervisor and status fields should contain only letters.\n");
		}

		// Check if numeric fields contain only numbers
		if (Arrays.stream(numericFields)
				.anyMatch(field -> !DataValidators.isNumeric(field.getText()))) {
			errorMessage.append("Salary and allowance fields should contain only numbers.\n");
		}

		// Check date format
		if (!DataValidators.isValidDate(birthdayField.getText())) {
			errorMessage.append("Please enter a valid date in MM/dd/yyyy format.\n");
		}

		// Check formatting of government IDs
		if (!DataValidators.isSSSFormattedCorrectly(sssField.getText())) {
			errorMessage.append("SSS number format should be xx-xxxxxxx-x.\n");
		}

		if (!DataValidators.isPhoneNumberFormattedCorrectly(phoneNumberField.getText())) {
			errorMessage.append("Phone number format should be xxx-xxx-xxxx.\n");
		}

		if (!DataValidators.isTINFormattedCorrectly(tinField.getText())) {
			errorMessage.append("TIN format should be xxx-xxx-xxx-xxx.\n");
		}

		if (!DataValidators.isProperLength(pagibigField.getText()) ||
				!DataValidators.isProperLength(philhealthField.getText())) {
			errorMessage.append("Pag-ibig and PhilHealth numbers should not exceed 12 characters.\n");
		}

		// If there are errors, show them and return
		if (errorMessage.length() > 0) {
			JOptionPane.showMessageDialog(this, errorMessage.toString(), "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Update employee data in JSON files
		JsonArray employeesArray = JsonFileHandler.getEmployeesJSON();
		JsonArray loginCredentialsArray = JsonFileHandler.getLoginCredentialsJSON();

		if (updateEmployeeEntry(employeesArray) &&
				updateLoginCredentialsEntry(loginCredentialsArray)) {

			// Save changes to files
			JsonFileHandler.writeJsonFile(employeesArray.toString(), JsonFileHandler.getEmployeesJsonPath());
			JsonFileHandler.writeJsonFile(loginCredentialsArray.toString(), JsonFileHandler.getLoginCredentialsJsonPath());

			// Show success message
			JOptionPane.showMessageDialog(this,
					"Employee data updated successfully!",
					"Success",
					JOptionPane.INFORMATION_MESSAGE);

			// Return to employee list
			goBackToEmployeeListButtonActionPerformed();
		}
	}

	private boolean updateEmployeeEntry(JsonArray jsonArray) {
		for (JsonElement element : jsonArray) {
			if (!element.isJsonObject()) {
				continue;
			}

			JsonObject employeeObject = element.getAsJsonObject();
			if (employeeObject.has("employeeNum") &&
					employeeObject.get("employeeNum").getAsString().equals(employeeNumberField.getText())) {

				// Update personal information
				employeeObject.addProperty("first_name", firstNameField.getText());
				employeeObject.addProperty("last_name", lastNameField.getText());
				employeeObject.addProperty("birthday", birthdayField.getText());
				employeeObject.addProperty("address", addressField.getText());
				employeeObject.addProperty("phone_number", phoneNumberField.getText());

				// Update government IDs
				employeeObject.addProperty("SSS", sssField.getText());
				employeeObject.addProperty("Philhealth", Long.parseLong(philhealthField.getText()));
				employeeObject.addProperty("TIN", tinField.getText());
				employeeObject.addProperty("Pag-ibig", Long.parseLong(pagibigField.getText()));

				// Update employment information
				employeeObject.addProperty("Status", statusField.getText());
				employeeObject.addProperty("Position", positionField.getText());
				employeeObject.addProperty("immediate_supervisor", immediateSupervisorField.getText());

				// Update compensation
				employeeObject.addProperty("basic_salary", Double.parseDouble(basicSalaryField.getText()));
				employeeObject.addProperty("rice_subsidy", Double.parseDouble(riceSubsidyField.getText()));
				employeeObject.addProperty("phone_allowance", Double.parseDouble(phoneAllowanceField.getText()));
				employeeObject.addProperty("clothing_allowance", Double.parseDouble(clothingAllowanceField.getText()));
				employeeObject.addProperty("gross_semi-monthly_rate", Double.parseDouble(grossSemiMonthlyRateField.getText()));
				employeeObject.addProperty("hourly_rate", Double.parseDouble(hourlyRateField.getText()));

				return true;
			}
		}

		return false;
	}

	private boolean updateLoginCredentialsEntry(JsonArray jsonArray) {
		for (JsonElement element : jsonArray) {
			if (!element.isJsonObject()) {
				continue;
			}

			JsonObject credentialsObject = element.getAsJsonObject();
			if (credentialsObject.has("employeeNum") &&
					credentialsObject.get("employeeNum").getAsString().equals(employeeNumberField.getText())) {

				// Update username based on first and last name
				credentialsObject.addProperty("username",
						(firstNameField.getText() + "." + lastNameField.getText()).toLowerCase());

				return true;
			}
		}

		return false;
	}

	private void goBackToEmployeeListButtonActionPerformed() throws IOException {
		dispose();
		new EmployeeListPage().setVisible(true);
	}
}