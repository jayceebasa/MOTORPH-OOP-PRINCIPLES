package GUI.employee;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.*;
import javax.swing.border.*;

import Classes.Compensation;
import Classes.GovernmentIdentification;
import GUI.LoginPage;
import UtilityClasses.JsonFileHandler;
import UtilityClasses.SalaryCalculator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class EmployeeDashboard extends JFrame {
	// Color scheme
	private Color primaryColor = new Color(25, 118, 210);  // Material blue
	private Color accentColor = new Color(230, 230, 230);  // Light gray
	private Color backgroundColor = new Color(250, 250, 250);  // Nearly white
	private Color textColor = new Color(33, 33, 33);  // Dark gray for text
	private Color buttonTextColor = Color.WHITE;

	// Existing class variables
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	private Double totalAllowance;
	private String selectedMonth = LocalDate.now().getMonth().toString();
	private AtomicInteger hoursRenderedNum = new AtomicInteger(0);
	private AtomicInteger absentsNum = new AtomicInteger(0);
	private AtomicInteger latesNum = new AtomicInteger(0);
	private AtomicInteger presentsNum = new AtomicInteger(0);
	private DecimalFormat numberFormat = new DecimalFormat("#,##0.00");

	// UI Components
	private JPanel mainPanel;
	private JLabel welcomeLabel;
	private JButton computeButton;
	private JComboBox<String> monthDropdown;
	private JButton submitLeaveRequestButton;
	private JButton logoutButton;
	private JScrollPane contentScrollPane;

	// Salary Computation Labels
	private JLabel grossSalaryValue;
	private JLabel hoursRenderedValue;
	private JLabel sssDeductionsValue;
	private JLabel philhealthDeductionsValue;
	private JLabel pagibigDeductionsValue;
	private JLabel totalDeductionsValue;
	private JLabel taxableSalaryValue;
	private JLabel withHoldingTaxValue;
	private JLabel salaryAfterTaxValue;
	private JLabel netSalaryValue;

	public EmployeeDashboard(GovernmentIdentification employeeGI, Compensation employeeComp) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;

		this.totalAllowance = employeeComp.getRiceSubsidy() +
				employeeComp.getPhoneAllowance() +
				employeeComp.getClothingAllowance();

		initComponents();
	}

	private void initComponents() {
		// Set basic frame properties
		setTitle("MotorPH Payroll System | Employee Dashboard");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(1000, 900));

		// Main panel setup
		mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(backgroundColor);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Setup header panel
		JPanel headerPanel = createHeaderPanel();
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		// Setup content panel
		contentScrollPane = new JScrollPane();
		contentScrollPane.setBorder(null);
		mainPanel.add(contentScrollPane, BorderLayout.CENTER);

		// Create main content panel
		JPanel contentPanel = createContentPanel();
		contentScrollPane.setViewportView(contentPanel);

		// Initialize salary labels
		initializeSalaryLabels();

		// Set content pane
		setContentPane(mainPanel);

		// Size and position frame
		pack();
		setLocationRelativeTo(null);
	}

	private void initializeSalaryLabels() {
		// Create or reset labels
		if (hoursRenderedValue == null)
			hoursRenderedValue = new JLabel();
		if (grossSalaryValue == null)
			grossSalaryValue = new JLabel();

		// Add similar checks and initializations for all labels
		if (sssDeductionsValue == null)
			sssDeductionsValue = new JLabel();
		if (philhealthDeductionsValue == null)
			philhealthDeductionsValue = new JLabel();
		if (pagibigDeductionsValue == null)
			pagibigDeductionsValue = new JLabel();
		if (totalDeductionsValue == null)
			totalDeductionsValue = new JLabel();
		if (taxableSalaryValue == null)
			taxableSalaryValue = new JLabel();
		if (withHoldingTaxValue == null)
			withHoldingTaxValue = new JLabel();
		if (salaryAfterTaxValue == null)
			salaryAfterTaxValue = new JLabel();
		if (netSalaryValue == null)
			netSalaryValue = new JLabel();

		// Set font and appearance
		Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
		Color textColor = new Color(33, 33, 33);  // Dark gray for text

		JLabel[] labels = {
				hoursRenderedValue, grossSalaryValue,
				sssDeductionsValue, philhealthDeductionsValue,
				pagibigDeductionsValue, totalDeductionsValue,
				taxableSalaryValue, withHoldingTaxValue,
				salaryAfterTaxValue, netSalaryValue
		};

		for (JLabel label : labels) {
			label.setFont(labelFont);
			label.setForeground(textColor);
		}
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(backgroundColor);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		// Welcome label
		welcomeLabel = new JLabel("Welcome, " + employeeGI.getLastName());
		welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		welcomeLabel.setForeground(primaryColor);

		// Action buttons panel
		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		actionPanel.setBackground(backgroundColor);

		// Compute button
		computeButton = createStyledButton("Compute", primaryColor);
		computeButton.addActionListener(e -> {
			try {
				computeButtonActionPerformed(null);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		});

		// Month dropdown
		monthDropdown = new JComboBox<>(new String[]{
				"January", "February", "March", "April", "May", "June",
				"July", "August", "September", "October", "November", "December"
		});
		monthDropdown.setPreferredSize(new Dimension(150, 35));
		monthDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		monthDropdown.addActionListener(this::monthDropdownActionPerformed);

		// Leave request button
		submitLeaveRequestButton = createStyledButton("Submit Leave Request", primaryColor);
		submitLeaveRequestButton.addActionListener(e -> backToEmployeeListButtonActionPerformed(null));

		// Logout button
		logoutButton = createStyledButton("Log Out", new Color(211, 47, 47)); // Red color
		logoutButton.addActionListener(e -> logoutButtonActionPerformed(null));

		// Add components to action panel
		actionPanel.add(submitLeaveRequestButton);
		actionPanel.add(monthDropdown);
		actionPanel.add(computeButton);
		actionPanel.add(logoutButton);

		// Populate header panel
		headerPanel.add(welcomeLabel, BorderLayout.WEST);
		headerPanel.add(actionPanel, BorderLayout.EAST);

		return headerPanel;
	}

	private JPanel createContentPanel() {
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBackground(backgroundColor);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		// Gross Salary Computation Panel
		JPanel grossSalaryPanel = createInfoPanel("Gross Salary Computation", createGrossSalaryContent());
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		contentPanel.add(grossSalaryPanel, gbc);

		// Allowances Panel
		JPanel allowancesPanel = createInfoPanel("Allowances", createAllowancesContent());
		gbc.gridx = 1;
		contentPanel.add(allowancesPanel, gbc);

		// Net Salary Computation Panel
		JPanel netSalaryPanel = createInfoPanel("Net Salary Computation", createNetSalaryContent());
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		contentPanel.add(netSalaryPanel, gbc);

		// Personal and Employment Panels
		JPanel personalInfoPanel = createInfoPanel("Personal Information", createPersonalInfoContent());
		JPanel employmentInfoPanel = createInfoPanel("Employment Information", createEmploymentInfoContent());
		JPanel governmentIDsPanel = createInfoPanel("Government IDs", createGovernmentIDsContent());

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		contentPanel.add(personalInfoPanel, gbc);

		gbc.gridx = 1;
		contentPanel.add(employmentInfoPanel, gbc);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		contentPanel.add(governmentIDsPanel, gbc);

		return contentPanel;
	}

	private JPanel createGrossSalaryContent() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBackground(backgroundColor);

		// Ensure hoursRenderedValue is initialized
		if (hoursRenderedValue == null) {
			hoursRenderedValue = new JLabel(" ");
		}

		// Create label for hourly rate, ensuring it's not null
		JLabel hourlyRateLabel = new JLabel(numberFormat.format(employeeComp.getHourlyRate()));

		// Create label for gross salary, ensuring it's not null
		if (grossSalaryValue == null) {
			grossSalaryValue = new JLabel(" ");
		}

		addLabelPair(panel, "Hours Rendered", hoursRenderedValue);
		addLabelPair(panel, "Hourly Rate", hourlyRateLabel);
		addLabelPair(panel, "Gross Salary", grossSalaryValue);

		return panel;
	}

	private JPanel createAllowancesContent() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBackground(backgroundColor);

		addLabelPair(panel, "Rice Subsidy",
				new JLabel(numberFormat.format(employeeComp.getRiceSubsidy())));
		addLabelPair(panel, "Phone Allowance",
				new JLabel(numberFormat.format(employeeComp.getPhoneAllowance())));
		addLabelPair(panel, "Clothing Allowance",
				new JLabel(numberFormat.format(employeeComp.getClothingAllowance())));
		addLabelPair(panel, "Total Allowance",
				new JLabel(numberFormat.format(totalAllowance)));

		return panel;
	}

	private JPanel createNetSalaryContent() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBackground(backgroundColor);

		// Ensure labels are initialized with empty strings
		if (sssDeductionsValue == null)
			sssDeductionsValue = new JLabel("");
		if (philhealthDeductionsValue == null)
			philhealthDeductionsValue = new JLabel("");
		if (pagibigDeductionsValue == null)
			pagibigDeductionsValue = new JLabel("");
		if (totalDeductionsValue == null)
			totalDeductionsValue = new JLabel("");
		if (taxableSalaryValue == null)
			taxableSalaryValue = new JLabel("");
		if (withHoldingTaxValue == null)
			withHoldingTaxValue = new JLabel("");
		if (salaryAfterTaxValue == null)
			salaryAfterTaxValue = new JLabel("");
		if (netSalaryValue == null)
			netSalaryValue = new JLabel("");

		// Set font and color for labels
		Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
		Color textColor = new Color(33, 33, 33);  // Dark gray for text

		JLabel[] labels = {
				sssDeductionsValue, philhealthDeductionsValue,
				pagibigDeductionsValue, totalDeductionsValue,
				taxableSalaryValue, withHoldingTaxValue,
				salaryAfterTaxValue, netSalaryValue
		};

		for (JLabel label : labels) {
			label.setFont(labelFont);
			label.setForeground(textColor);
		}

		// Add labels to the panel
		addLabelPair(panel, "SSS Deduction", sssDeductionsValue);
		addLabelPair(panel, "PhilHealth Deduction", philhealthDeductionsValue);
		addLabelPair(panel, "Pag-IBIG Deduction", pagibigDeductionsValue);
		addLabelPair(panel, "Total Deductions", totalDeductionsValue);
		addLabelPair(panel, "Taxable Salary", taxableSalaryValue);
		addLabelPair(panel, "Withholding Tax", withHoldingTaxValue);
		addLabelPair(panel, "Salary After Tax", salaryAfterTaxValue);
		addLabelPair(panel, "Net Salary", netSalaryValue);

		return panel;
	}

	private JPanel createPersonalInfoContent() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBackground(backgroundColor);

		addLabelPair(panel, "First Name",
				new JLabel(employeeGI.getFirstName()));
		addLabelPair(panel, "Last Name",
				new JLabel(employeeGI.getLastName()));
		addLabelPair(panel, "Birthday",
				new JLabel(employeeGI.getBirthday()));
		addLabelPair(panel, "Phone Number",
				new JLabel(employeeGI.getPhoneNumber()));

		return panel;
	}

	private JPanel createEmploymentInfoContent() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBackground(backgroundColor);

		addLabelPair(panel, "Status",
				new JLabel(employeeGI.getStatus()));
		addLabelPair(panel, "Position",
				new JLabel(employeeGI.getPosition()));
		addLabelPair(panel, "Immediate Supervisor",
				new JLabel(employeeGI.getImmediateSupervisor()));
		addLabelPair(panel, "Hourly Rate",
				new JLabel(numberFormat.format(employeeComp.getHourlyRate())));

		return panel;
	}

	private JPanel createGovernmentIDsContent() {
		JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
		panel.setBackground(backgroundColor);

		addLabelPair(panel, "SSS Number",
				new JLabel(employeeGI.getSSSNumber()));
		addLabelPair(panel, "PhilHealth Number",
				new JLabel(employeeGI.getPhilHealthNumber()));
		addLabelPair(panel, "Pag-IBIG Number",
				new JLabel(employeeGI.getPagibigNumber()));
		addLabelPair(panel, "TIN Number",
				new JLabel(employeeGI.getTinNumber()));

		return panel;
	}

	private void addLabelPair(JPanel panel, String label, JLabel valueLabel) {
		// Null check to prevent NullPointerException
		if (valueLabel == null) {
			valueLabel = new JLabel(" ");
		}

		JLabel labelComponent = new JLabel(label);

		labelComponent.setFont(new Font("Segoe UI", Font.BOLD, 14));
		labelComponent.setForeground(textColor);

		valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		valueLabel.setForeground(textColor);

		panel.add(labelComponent);
		panel.add(valueLabel);
	}

	private JPanel createInfoPanel(String title, JPanel contentPanel) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(accentColor),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)
		));

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
		panel.add(contentPanel, BorderLayout.CENTER);

		return panel;
	}

	private JButton createStyledButton(String text, Color backgroundColor) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(150, 35));
		button.setBackground(backgroundColor);
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
				button.setBackground(backgroundColor);
			}
		});

		return button;
	}



	// Action Methods
	private void computeButtonActionPerformed(ActionEvent evt) throws IOException {
		try {
			// Ensure labels are not null and initialized
			if (sssDeductionsValue == null)
				sssDeductionsValue = new JLabel();
			if (philhealthDeductionsValue == null)
				philhealthDeductionsValue = new JLabel();
			if (pagibigDeductionsValue == null)
				pagibigDeductionsValue = new JLabel();
			if (totalDeductionsValue == null)
				totalDeductionsValue = new JLabel();
			if (taxableSalaryValue == null)
				taxableSalaryValue = new JLabel();
			if (withHoldingTaxValue == null)
				withHoldingTaxValue = new JLabel();
			if (salaryAfterTaxValue == null)
				salaryAfterTaxValue = new JLabel();
			if (netSalaryValue == null)
				netSalaryValue = new JLabel();

			// Get the selected month from the dropdown
			Month selectedMonthEnum = Month.valueOf(selectedMonth);

			// Compute for the hoursRendered
			JsonArray attendanceJsonArray = JsonFileHandler.getAttendanceJSON();

			// Reset tracking variables
			hoursRenderedNum.set(0);
			presentsNum.set(0);
			latesNum.set(0);
			absentsNum.set(0);

			// Loop through attendance records
			for (JsonElement element : attendanceJsonArray) {
				JsonObject attendanceJson = element.getAsJsonObject();
				String employeeNum = attendanceJson.get("employeeNum").getAsString();
				LocalDateTime timeIn = SalaryCalculator.getTimeInOrOut(attendanceJson, "time_in");

				// Check if this record is for the current employee and selected month
				if (employeeNum.equals(employeeGI.getEmployeeNumber())
						&& timeIn.getMonth().equals(selectedMonthEnum)) {

					// Calculate and accumulate hours
					SalaryCalculator.getAttendance(
							attendanceJson,
							selectedMonthEnum,
							presentsNum,
							latesNum,
							absentsNum,
							hoursRenderedNum
					);
				}
			}

			// Get hours rendered
			int hoursRendered = hoursRenderedNum.get();

			// Validate hours rendered
			if (hoursRendered <= 0) {
				JOptionPane.showMessageDialog(this,
						"No hours worked for " + selectedMonth,
						"No Attendance",
						JOptionPane.INFORMATION_MESSAGE);

				// Clear all salary computation labels
				clearSalaryLabels();
				return;
			}

			// Compute for the Gross Salary
			double hourlyRate = employeeComp.getHourlyRate();
			double grossSalary = hoursRendered * hourlyRate;

			// Update Gross Salary Computation labels
			hoursRenderedValue.setText(String.valueOf(hoursRendered));
			grossSalaryValue.setText(numberFormat.format(grossSalary));

			// Compute for deductions
			double sssDeduction = SalaryCalculator.getSSS(grossSalary);
			double philhealthDeduction = SalaryCalculator.getPhilHealth(grossSalary);
			double pagibigDeduction = SalaryCalculator.getPagibig(grossSalary);
			double totalDeductions = sssDeduction + philhealthDeduction + pagibigDeduction;

			// Update deduction labels
			sssDeductionsValue.setText(numberFormat.format(sssDeduction));
			philhealthDeductionsValue.setText(numberFormat.format(philhealthDeduction));
			pagibigDeductionsValue.setText(numberFormat.format(pagibigDeduction));
			totalDeductionsValue.setText(numberFormat.format(totalDeductions));

			// Compute for Taxable Salary
			double taxableSalary = grossSalary - totalDeductions;
			taxableSalaryValue.setText(numberFormat.format(taxableSalary));

			// Compute for Withholding Tax
			double withholdingTax = SalaryCalculator.getWithholding(taxableSalary);
			withHoldingTaxValue.setText(numberFormat.format(withholdingTax));

			// Compute for Salary After Tax
			double salaryAfterTax = taxableSalary - withholdingTax;
			salaryAfterTaxValue.setText(numberFormat.format(salaryAfterTax));

			// Compute for Net Salary
			double netSalary = salaryAfterTax + totalAllowance;
			netSalaryValue.setText(numberFormat.format(netSalary));

			// Detailed logging
			System.out.println("\nSalary Computation Details:");
			System.out.println("Gross Salary: " + grossSalary);
			System.out.println("SSS Deduction: " + sssDeduction);
			System.out.println("PhilHealth Deduction: " + philhealthDeduction);
			System.out.println("Pag-IBIG Deduction: " + pagibigDeduction);
			System.out.println("Total Deductions: " + totalDeductions);
			System.out.println("Taxable Salary: " + taxableSalary);
			System.out.println("Withholding Tax: " + withholdingTax);
			System.out.println("Salary After Tax: " + salaryAfterTax);
			System.out.println("Total Allowance: " + totalAllowance);
			System.out.println("Net Salary: " + netSalary);

			// Force UI update
			SwingUtilities.invokeLater(() -> {
				contentScrollPane.revalidate();
				contentScrollPane.repaint();

				// Ensure the panel is updated
				if (contentScrollPane.getViewport().getView() != null) {
					contentScrollPane.getViewport().getView().revalidate();
					contentScrollPane.getViewport().getView().repaint();
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"An error occurred while calculating salary:\n" + e.getMessage(),
					"Calculation Error",
					JOptionPane.ERROR_MESSAGE);

			// Clear labels in case of error
			clearSalaryLabels();
		}
	}

	private void clearSalaryLabels() {
		// Clear all salary computation labels
		hoursRenderedValue.setText("");
		grossSalaryValue.setText("");
		sssDeductionsValue.setText("");
		philhealthDeductionsValue.setText("");
		pagibigDeductionsValue.setText("");
		totalDeductionsValue.setText("");
		taxableSalaryValue.setText("");
		withHoldingTaxValue.setText("");
		salaryAfterTaxValue.setText("");
		netSalaryValue.setText("");
	}

	private void monthDropdownActionPerformed(ActionEvent evt) {
		// Reset values for hours rendered
		resetSummaryValues();

		// Get the selected item
		selectedMonth = ((String) monthDropdown.getSelectedItem()).toUpperCase();
	}

	private void backToEmployeeListButtonActionPerformed(ActionEvent evt) {
		dispose();
		new LeaveRequestPage(employeeGI, employeeComp).setVisible(true);
	}

	private void logoutButtonActionPerformed(ActionEvent evt) {
		dispose();
		new LoginPage().setVisible(true);
	}

	// Utility Methods
	private void loadAttendanceRecordsFromJsonFile(JsonArray jsonArray) {
		try {
			// Reset hours rendered before calculation
			hoursRenderedNum.set(0);
			presentsNum.set(0);
			latesNum.set(0);
			absentsNum.set(0);

			// Loop through each element in the array
			for (JsonElement element : jsonArray) {
				JsonObject attendanceJson = element.getAsJsonObject();
				String employeeNum = attendanceJson.get("employeeNum").getAsString();
				LocalDateTime month = SalaryCalculator.getTimeInOrOut(attendanceJson, "time_in");

				// Check if this record is for the current employee and selected month
				if (employeeNum.equals(employeeGI.getEmployeeNumber())
						&& month.getMonth().equals(Month.valueOf(selectedMonth))) {

					// Calculate and accumulate hours
					SalaryCalculator.getAttendance(
							attendanceJson,
							Month.valueOf(selectedMonth),
							presentsNum,
							latesNum,
							absentsNum,
							hoursRenderedNum
					);
				}
			}

			// Validate hours rendered
			if (hoursRenderedNum.get() == 0) {
				JOptionPane.showMessageDialog(this,
						"No attendance records found for " + selectedMonth,
						"No Data",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"Error loading attendance records: " + e.getMessage(),
					"Attendance Error",
					JOptionPane.ERROR_MESSAGE);

			// Reset hours to zero if there's an error
			hoursRenderedNum.set(0);
		}
	}

	public Double computeGrossSalary() {
		return hoursRenderedNum.get() * employeeComp.getHourlyRate();
	}

	public void resetSummaryValues() {
		this.absentsNum = new AtomicInteger(0);
		this.hoursRenderedNum = new AtomicInteger(0);
		this.latesNum = new AtomicInteger(0);
		this.presentsNum = new AtomicInteger(0);
	}

	public static String getAttendance(JsonObject json, Month currentMonth, AtomicInteger presentsNum,
									   AtomicInteger latesNum, AtomicInteger absentsNum, AtomicInteger hoursRenderedNum) {
		try {
			long attendance = 0;

			LocalDateTime dateTime1 = SalaryCalculator.getTimeInOrOut(json, "time_in");
			LocalDateTime dateTime2 = SalaryCalculator.getTimeInOrOut(json, "time_out");

			long duration = java.time.Duration.between(dateTime1, dateTime2).toHours();
			long mins = java.time.Duration.between(dateTime1, dateTime2).toMinutes();

			// If the record is for the current month
			if (dateTime1.getMonth().equals(currentMonth)) {
				// Detailed logging of each record
				System.out.println("----------------------------");
				System.out.println("Date: " + json.get("date").getAsString());
				System.out.println("Time In: " + json.get("time_in").getAsString());
				System.out.println("Time Out: " + json.get("time_out").getAsString());
				System.out.println("Duration (hours): " + duration);
				System.out.println("Duration (minutes): " + mins);

				// Condition for full day work (8.5 to 9 hours)
				if (mins > 529 && mins < 540) {
					presentsNum.incrementAndGet();
					attendance = duration + 1;
					System.out.println("Full day work: " + attendance + " hours");
				} else {
					// Check if employee is present
					checkIfPresent(mins, presentsNum);
					attendance = duration;
					System.out.println("Partial day work: " + attendance + " hours");
				}
			}

			// Check for late arrivals
			if (mins < 530 && mins > 0) {
				latesNum.incrementAndGet();
				System.out.println("Late arrival");
			}

			// Check for absent
			if (mins == 0) {
				absentsNum.incrementAndGet();
				System.out.println("Absent");
			}

			// Accumulate hours
			hoursRenderedNum.addAndGet((int) attendance);

			System.out.println("Current Total Hours: " + hoursRenderedNum.get());
			System.out.println("----------------------------");

			return String.valueOf(attendance);
		} catch (Exception e) {
			System.err.println("Error in getAttendance method:");
			e.printStackTrace();
			return "0";
		}
	}

	private static void checkIfPresent(long mins, AtomicInteger presentsNum) {
		if (mins != 0) {
			presentsNum.incrementAndGet();
		}
	}
}