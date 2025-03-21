package GUI.admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Classes.Compensation;
import Classes.GovernmentIdentification;
import UtilityClasses.JsonFileHandler;
import UtilityClasses.SalaryCalculator;

public class FullEmployeeDetailsPage extends JFrame {

	// Design Constants
	private static final Color PRIMARY_COLOR = new Color(25, 118, 210);  // Material blue
	private static final Color ACCENT_COLOR = new Color(230, 230, 230);  // Light gray
	private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Nearly white
	private static final Color TEXT_COLOR = new Color(33, 33, 33);  // Dark gray for text
	private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
	private static final Color PANEL_BACKGROUND = Color.WHITE;

	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font SECTION_FONT = new Font("Segoe UI", Font.BOLD, 18);
	private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
	private static final Font VALUE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

	// Main panel
	private JPanel mainPanel;

	// Section panels
	private JPanel personalInfoPanel;
	private JPanel employmentInfoPanel;
	private JPanel govtIDsPanel;
	private JPanel addressPanel;
	private JPanel salaryPanel;

	// Personal Info Fields
	private JLabel titleLabel;
	private JLabel firstNameLabel;
	private JLabel firstNameValue;
	private JLabel lastNameLabel;
	private JLabel lastNameValue;
	private JLabel birthdayLabel;
	private JLabel birthdayValue;
	private JLabel phoneNumberLabel;
	private JLabel phoneNumberValue;

	// Employment Info Fields
	private JLabel statusLabel;
	private JLabel statusValue;
	private JLabel positionLabel;
	private JLabel positionValue;
	private JLabel supervisorLabel;
	private JLabel supervisorValue;
	private JLabel hourlyRateLabel;
	private JLabel hourlyRateValue;

	// Government ID Fields
	private JLabel sssLabel;
	private JLabel sssValue;
	private JLabel philhealthLabel;
	private JLabel philhealthValue;
	private JLabel pagibigLabel;
	private JLabel pagibigValue;
	private JLabel tinLabel;
	private JLabel tinValue;

	// Address Field
	private JLabel addressLabel;
	private JLabel addressValue;

	// Salary Computation Fields
	private JLabel salaryLabel;
	private JComboBox<String> monthDropdown;
	private JButton computeButton;

	// Salary details panel fields
	private JLabel hoursRenderedLabel;
	private JLabel hoursRenderedValue;
	private JLabel grossSalaryLabel;
	private JLabel grossSalaryValue;
	private JLabel totalDeductionsLabel;
	private JLabel totalDeductionsValue;
	private JLabel netSalaryLabel;
	private JLabel netSalaryValue;

	// Navigation
	private JButton backButton;

	// Employee data
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	private Double totalAllowance;
	private String selectedMonth = LocalDate.now().getMonth().toString();
	private AtomicInteger hoursRenderedNum = new AtomicInteger(0);
	private AtomicInteger absentsNum = new AtomicInteger(0);
	private AtomicInteger latesNum = new AtomicInteger(0);
	private AtomicInteger presentsNum = new AtomicInteger(0);

	// Number formatters for different types of values
	private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
	private NumberFormat numberFormatter = NumberFormat.getNumberInstance();
	private DecimalFormat decimalFormatter = new DecimalFormat("#,##0.00");
	private DecimalFormat hourFormatter = new DecimalFormat("#,##0.0");

	public FullEmployeeDetailsPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;

		// Configure formatters
		numberFormatter.setMaximumFractionDigits(2);
		numberFormatter.setMinimumFractionDigits(2);

		// Set total allowance early on to avoid errors
		this.totalAllowance = employeeComp.getRiceSubsidy() + employeeComp.getPhoneAllowance()
				+ employeeComp.getClothingAllowance();

		initComponents();
	}

	private void initComponents() {
		// Set frame properties
		setTitle("MotorPH Payroll System | Employee Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(BACKGROUND_COLOR);
		setSize(1000, 800);

		// Create main panel
		mainPanel = new JPanel();
		mainPanel.setLayout(null); // Using absolute positioning
		mainPanel.setBackground(BACKGROUND_COLOR);

		// Create title and navigation
		titleLabel = new JLabel("Employee Details");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(PRIMARY_COLOR);
		titleLabel.setBounds(30, 20, 400, 30);

		backButton = createButton("Back to Employee List", PRIMARY_COLOR, 30, 70, 200, 40);
		backButton.addActionListener(e -> backToEmployeeListButtonActionPerformed());

		// Month dropdown and compute button for salary calculation
		String[] months = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
				"JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
		monthDropdown = new JComboBox<>(months);
		monthDropdown.setFont(BUTTON_FONT);
		monthDropdown.setBounds(650, 70, 180, 40);
		monthDropdown.setSelectedItem(selectedMonth);
		monthDropdown.addActionListener(e -> monthDropdownActionPerformed());

		computeButton = createButton("Calculate Salary", PRIMARY_COLOR, 850, 70, 120, 40);
		computeButton.addActionListener(e -> {
			try {
				computeButtonActionPerformed();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		// Create section panels
		createPersonalInfoPanel();
		createEmploymentInfoPanel();
		createGovernmentIDsPanel();
		createAddressPanel();
		createSalaryPanel();

		// Add components to main panel
		mainPanel.add(titleLabel);
		mainPanel.add(backButton);
		mainPanel.add(monthDropdown);
		mainPanel.add(computeButton);

		// Add main panel to frame
		add(mainPanel);

		// Center the frame on screen
		setLocationRelativeTo(null);
	}

	private void createPersonalInfoPanel() {
		// Create panel
		personalInfoPanel = createPanelWithTitle("Personal Information", 30, 130, 450, 220);

		// Create section title
		JLabel sectionTitle = createSectionLabel("Personal Information", 20, 15);
		personalInfoPanel.add(sectionTitle);

		// Create labels and values
		int labelX = 20;
		int valueX = 150;
		int startY = 60;
		int spacing = 35;

		firstNameLabel = createLabel("First Name:", labelX, startY);
		firstNameValue = createValueLabel(employeeGI.getFirstName(), valueX, startY);

		lastNameLabel = createLabel("Last Name:", labelX, startY + spacing);
		lastNameValue = createValueLabel(employeeGI.getLastName(), valueX, startY + spacing);

		birthdayLabel = createLabel("Birthday:", labelX, startY + spacing * 2);
		birthdayValue = createValueLabel(employeeGI.getBirthday(), valueX, startY + spacing * 2);

		phoneNumberLabel = createLabel("Phone Number:", labelX, startY + spacing * 3);
		phoneNumberValue = createValueLabel(employeeGI.getPhoneNumber(), valueX, startY + spacing * 3);

		// Add components to panel
		personalInfoPanel.add(firstNameLabel);
		personalInfoPanel.add(firstNameValue);
		personalInfoPanel.add(lastNameLabel);
		personalInfoPanel.add(lastNameValue);
		personalInfoPanel.add(birthdayLabel);
		personalInfoPanel.add(birthdayValue);
		personalInfoPanel.add(phoneNumberLabel);
		personalInfoPanel.add(phoneNumberValue);

		// Add panel to main panel
		mainPanel.add(personalInfoPanel);
	}

	private void createEmploymentInfoPanel() {
		// Create panel
		employmentInfoPanel = createPanelWithTitle("Employment Information", 520, 130, 450, 220);

		// Create section title
		JLabel sectionTitle = createSectionLabel("Employment Information", 20, 15);
		employmentInfoPanel.add(sectionTitle);

		// Create labels and values
		int labelX = 20;
		int valueX = 150;
		int startY = 60;
		int spacing = 35;

		statusLabel = createLabel("Status:", labelX, startY);
		statusValue = createValueLabel(employeeGI.getStatus(), valueX, startY);

		positionLabel = createLabel("Position:", labelX, startY + spacing);
		positionValue = createValueLabel(employeeGI.getPosition(), valueX, startY + spacing);

		supervisorLabel = createLabel("Supervisor:", labelX, startY + spacing * 2);
		supervisorValue = createValueLabel(employeeGI.getImmediateSupervisor(), valueX, startY + spacing * 2);

		hourlyRateLabel = createLabel("Hourly Rate:", labelX, startY + spacing * 3);
		// Format hourly rate with currency formatter
		hourlyRateValue = createValueLabel(currencyFormatter.format(employeeComp.getHourlyRate()), valueX, startY + spacing * 3);

		// Add components to panel
		employmentInfoPanel.add(statusLabel);
		employmentInfoPanel.add(statusValue);
		employmentInfoPanel.add(positionLabel);
		employmentInfoPanel.add(positionValue);
		employmentInfoPanel.add(supervisorLabel);
		employmentInfoPanel.add(supervisorValue);
		employmentInfoPanel.add(hourlyRateLabel);
		employmentInfoPanel.add(hourlyRateValue);

		// Add panel to main panel
		mainPanel.add(employmentInfoPanel);
	}

	private void createGovernmentIDsPanel() {
		// Create panel
		govtIDsPanel = createPanelWithTitle("Government IDs", 30, 370, 940, 160);

		// Create section title
		JLabel sectionTitle = createSectionLabel("Government IDs", 20, 15);
		govtIDsPanel.add(sectionTitle);

		// Create labels and values - arrange in 2x2 grid
		int col1X = 20;
		int col2X = 500;
		int valueOffset = 120;
		int row1Y = 60;
		int row2Y = 100;

		sssLabel = createLabel("SSS Number:", col1X, row1Y);
		sssValue = createValueLabel(employeeGI.getSSSNumber(), col1X + valueOffset, row1Y);

		philhealthLabel = createLabel("PhilHealth:", col2X, row1Y);
		philhealthValue = createValueLabel(formatIDNumber(employeeGI.getPhilHealthNumber()), col2X + valueOffset, row1Y);

		pagibigLabel = createLabel("Pag-IBIG:", col1X, row2Y);
		pagibigValue = createValueLabel(formatIDNumber(employeeGI.getPagibigNumber()), col1X + valueOffset, row2Y);

		tinLabel = createLabel("TIN Number:", col2X, row2Y);
		tinValue = createValueLabel(employeeGI.getTinNumber(), col2X + valueOffset, row2Y);

		// Add components to panel
		govtIDsPanel.add(sssLabel);
		govtIDsPanel.add(sssValue);
		govtIDsPanel.add(philhealthLabel);
		govtIDsPanel.add(philhealthValue);
		govtIDsPanel.add(pagibigLabel);
		govtIDsPanel.add(pagibigValue);
		govtIDsPanel.add(tinLabel);
		govtIDsPanel.add(tinValue);

		// Add panel to main panel
		mainPanel.add(govtIDsPanel);
	}

	private void createAddressPanel() {
		// Create panel
		addressPanel = createPanelWithTitle("Address", 30, 550, 940, 120);

		// Create section title
		JLabel sectionTitle = createSectionLabel("Address", 20, 15);
		addressPanel.add(sectionTitle);

		// Create address label and value
		addressLabel = createLabel("Address:", 20, 60);
		addressValue = createValueLabel(employeeGI.getAddress(), 120, 60);
		addressValue.setPreferredSize(new Dimension(800, 40));

		// Add components to panel
		addressPanel.add(addressLabel);
		addressPanel.add(addressValue);

		// Add panel to main panel
		mainPanel.add(addressPanel);
	}

	private void createSalaryPanel() {
		// Create panel
		salaryPanel = createPanelWithTitle("Monthly Salary Details", 30, 690, 940, 160);
		salaryPanel.setVisible(false); // Initially hidden until calculation is performed

		// Create section title with month indicator
		JLabel sectionTitle = createSectionLabel("Salary Details for " + selectedMonth, 20, 15);
		salaryPanel.add(sectionTitle);

		// Create labels and values for salary details
		int col1X = 20;
		int col2X = 500;
		int valueOffset = 150;
		int row1Y = 60;
		int row2Y = 100;

		hoursRenderedLabel = createLabel("Hours Rendered:", col1X, row1Y);
		hoursRenderedValue = createValueLabel("0.0", col1X + valueOffset, row1Y);

		grossSalaryLabel = createLabel("Gross Salary:", col2X, row1Y);
		grossSalaryValue = createValueLabel(currencyFormatter.format(0.0), col2X + valueOffset, row1Y);

		totalDeductionsLabel = createLabel("Total Deductions:", col1X, row2Y);
		totalDeductionsValue = createValueLabel(currencyFormatter.format(0.0), col1X + valueOffset, row2Y);

		netSalaryLabel = createLabel("Net Salary:", col2X, row2Y);
		netSalaryValue = createValueLabel(currencyFormatter.format(0.0), col2X + valueOffset, row2Y);

		// Style the net salary to stand out
		netSalaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		netSalaryValue.setFont(new Font("Segoe UI", Font.BOLD, 16));
		netSalaryValue.setForeground(PRIMARY_COLOR);

		// Add components to panel
		salaryPanel.add(sectionTitle);
		salaryPanel.add(hoursRenderedLabel);
		salaryPanel.add(hoursRenderedValue);
		salaryPanel.add(grossSalaryLabel);
		salaryPanel.add(grossSalaryValue);
		salaryPanel.add(totalDeductionsLabel);
		salaryPanel.add(totalDeductionsValue);
		salaryPanel.add(netSalaryLabel);
		salaryPanel.add(netSalaryValue);

		// Add panel to main panel
		mainPanel.add(salaryPanel);
	}

	// Helper methods
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
		panel.setBackground(PANEL_BACKGROUND);
		panel.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
		panel.setLayout(null);
		panel.setBounds(x, y, width, height);
		return panel;
	}

	private JLabel createSectionLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setFont(SECTION_FONT);
		label.setForeground(PRIMARY_COLOR);
		label.setBounds(x, y, 400, 30);
		return label;
	}

	private JLabel createLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setFont(LABEL_FONT);
		label.setForeground(TEXT_COLOR);
		label.setBounds(x, y, 130, 25);
		return label;
	}

	private JLabel createValueLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setFont(VALUE_FONT);
		label.setForeground(TEXT_COLOR);
		label.setBounds(x, y, 320, 25);
		return label;
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

	// Format long ID numbers for better readability
	private String formatIDNumber(String number) {
		try {
			// For numbers stored as strings
			long num = Long.parseLong(number);
			return NumberFormat.getNumberInstance(Locale.US).format(num);
		} catch (NumberFormatException e) {
			return number; // Return the original if it's not a number
		}
	}

	// Action Methods

	private void monthDropdownActionPerformed() {
		// Reset values for hours rendered
		resetSummaryValues();

		// Get the selected item
		selectedMonth = ((String) monthDropdown.getSelectedItem()).toUpperCase();

		// Update the title of the salary panel
		if (salaryPanel.isVisible()) {
			// Find the section title and update it
			Component[] components = salaryPanel.getComponents();
			for (Component component : components) {
				if (component instanceof JLabel
						&& ((JLabel)component).getText().startsWith("Salary Details for")) {
					((JLabel)component).setText("Salary Details for " + selectedMonth);
					break;
				}
			}

			// Recalculate for the new month
			try {
				computeButtonActionPerformed();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void computeButtonActionPerformed() throws IOException {
		// Reset values
		resetSummaryValues();

		// Calculate hours rendered from attendance data for the selected month
		calculateHoursRendered();

		// Update the hours rendered display with formatted value
		hoursRenderedValue.setText(hourFormatter.format(hoursRenderedNum.get()));

		// Calculate gross salary based on hours and hourly rate
		double hourlyRate = employeeComp.getHourlyRate();
		double hours = hoursRenderedNum.get();
		double grossSalary = hourlyRate * hours;

		// Calculate deductions
		double sssDeduction = SalaryCalculator.getSSS(grossSalary);
		double philhealthDeduction = SalaryCalculator.getPhilHealth(grossSalary);
		double pagibigDeduction = SalaryCalculator.getPagibig(grossSalary);
		double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;

		// Calculate taxable salary
		double taxableSalary = grossSalary - totalDeductions;

		// Calculate withholding tax
		double withholdingTax = SalaryCalculator.getWithholding(taxableSalary);

		// Calculate salary after tax
		double salaryAfterTax = taxableSalary - withholdingTax;

		// Add allowances
		double netSalary = salaryAfterTax + totalAllowance;

		// Update UI with formatted monetary values
		grossSalaryValue.setText(currencyFormatter.format(grossSalary));
		totalDeductionsValue.setText(currencyFormatter.format(totalDeductions + withholdingTax));
		netSalaryValue.setText(currencyFormatter.format(netSalary));

		// Make the salary panel visible
		if (!salaryPanel.isVisible()) {
			salaryPanel.setVisible(true);

			// Make sure the panel is visible by increasing the frame size if needed
			int newHeight = salaryPanel.getY() + salaryPanel.getHeight() + 50;
			if (getHeight() < newHeight) {
				setSize(getWidth(), newHeight);
			}
		}
	}

	private void calculateHoursRendered() throws IOException {
		// Load the JSON file as a JsonArray
		JsonArray jsonArray = JsonFileHandler.getAttendanceJSON(JsonFileHandler.getAttendanceJsonPath());

		// Loop through attendance records
		for (JsonElement element : jsonArray) {
			JsonObject attendanceJson = element.getAsJsonObject();
			String employeeNum = attendanceJson.get("employeeNum").getAsString();

			// Check if this record is for the current employee
			if (employeeNum.equals(employeeGI.getEmployeeNumber())) {
				// Parse the date to check if it matches the selected month
				LocalDateTime recordDateTime = SalaryCalculator.getTimeInOrOut(attendanceJson, "time_in");

				// If the month matches the selected month, count the hours
				if (recordDateTime.getMonth().equals(Month.valueOf(selectedMonth))) {
					// Calculate hours for this day and add to total
					String hoursRendered = SalaryCalculator.getAttendance(
							attendanceJson,
							Month.valueOf(selectedMonth),
							presentsNum,
							latesNum,
							absentsNum,
							hoursRenderedNum
					);
				}
			}
		}

		// If no hours found, show a message
		if (hoursRenderedNum.get() == 0) {
			JOptionPane.showMessageDialog(
					this,
					"No attendance records found for " + selectedMonth,
					"No Data",
					JOptionPane.INFORMATION_MESSAGE
			);
		}
	}

	private void backToEmployeeListButtonActionPerformed() {
		// Navigate back to the employee list
		java.awt.EventQueue.invokeLater(() -> {
			dispose();
			new EmployeeListPage().setVisible(true);
		});
	}

	// Utility Methods

	private void resetSummaryValues() {
		this.absentsNum = new AtomicInteger(0);
		this.hoursRenderedNum = new AtomicInteger(0);
		this.latesNum = new AtomicInteger(0);
		this.presentsNum = new AtomicInteger(0);
	}
}