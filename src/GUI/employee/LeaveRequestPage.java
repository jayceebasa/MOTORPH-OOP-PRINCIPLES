package GUI.employee;

import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.swing.*;
import javax.swing.border.*;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import Classes.Compensation;
import Classes.GovernmentIdentification;
import Classes.LeaveRequest;
import UtilityClasses.JsonFileHandler;
import GUI.employee.EmployeeDashboard;
import GUI.employee.EmployeeLeaveRequestListPage;

public class LeaveRequestPage extends JFrame {
	// Color scheme
	private Color primaryColor = new Color(25, 118, 210);  // Material blue
	private Color accentColor = new Color(230, 230, 230);  // Light gray
	private Color backgroundColor = new Color(250, 250, 250);  // Nearly white
	private Color textColor = new Color(33, 33, 33);  // Dark gray for text
	private Color buttonTextColor = Color.WHITE;

	// UI Components
	private JLabel titleLabel;
	private JLabel typeOfLeaveLabel;
	private JComboBox<String> typeOfLeaveDropdown;
	private JLabel startDateLabel;
	private JLabel endDateLabel;
	private JDateChooser startDateField;
	private JDateChooser endDateField;
	private JLabel notesLabel;
	private JTextArea notesField;
	private JButton submitButton;
	private JButton cancelButton;
	private JButton viewRequestsButton;

	// Employee information
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;

	public LeaveRequestPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;

		initComponents();
	}

	private void initComponents() {
		// Set frame properties
		setTitle("MotorPH Payroll System | Submit Leave Request");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(600, 700));
		getContentPane().setBackground(backgroundColor);

		// Create main panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(backgroundColor);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Create header panel
		JPanel headerPanel = createHeaderPanel();
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		// Create form panel
		JPanel formPanel = createFormPanel();
		mainPanel.add(formPanel, BorderLayout.CENTER);

		// Create button panel
		JPanel buttonPanel = createButtonPanel();
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// Set content pane
		setContentPane(mainPanel);

		// Size and position frame
		pack();
		setLocationRelativeTo(null);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(backgroundColor);
		headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

		// Title label
		titleLabel = new JLabel("Leave Request Form");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titleLabel.setForeground(primaryColor);

		headerPanel.add(titleLabel, BorderLayout.WEST);

		return headerPanel;
	}

	private JPanel createFormPanel() {
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setBackground(backgroundColor);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		// Type of Leave
		typeOfLeaveLabel = createLabel("Type of Leave");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		formPanel.add(typeOfLeaveLabel, gbc);

		typeOfLeaveDropdown = new JComboBox<>(new String[]{
				"Sick Leave", "Vacation Leave", "Emergency Leave"
		});
		typeOfLeaveDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		typeOfLeaveDropdown.setPreferredSize(new Dimension(250, 35));
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		formPanel.add(typeOfLeaveDropdown, gbc);

		// Start Date
		startDateLabel = createLabel("Start Date");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.3;
		formPanel.add(startDateLabel, gbc);

		startDateField = new JDateChooser();
		configureJDateChooser(startDateField);
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		formPanel.add(startDateField, gbc);

		// End Date
		endDateLabel = createLabel("End Date");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.3;
		formPanel.add(endDateLabel, gbc);

		endDateField = new JDateChooser();
		configureJDateChooser(endDateField);
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		formPanel.add(endDateField, gbc);

		// Notes
		notesLabel = createLabel("Notes");
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.weightx = 0.3;
		formPanel.add(notesLabel, gbc);

		notesField = new JTextArea();
		notesField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		notesField.setLineWrap(true);
		notesField.setWrapStyleWord(true);
		JScrollPane notesScrollPane = new JScrollPane(notesField);
		notesScrollPane.setPreferredSize(new Dimension(250, 100));
		gbc.gridx = 1;
		gbc.weightx = 0.7;
		formPanel.add(notesScrollPane, gbc);

		return formPanel;
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
		buttonPanel.setBackground(backgroundColor);

		// Cancel button
		cancelButton = createStyledButton("Cancel", new Color(211, 47, 47)); // Red color
		cancelButton.addActionListener(e -> cancelButtonActionPerformed(null));

		// View Requests button
		viewRequestsButton = createStyledButton("View Requests", primaryColor);
		viewRequestsButton.addActionListener(e -> viewRequestsButtonActionPerformed(null));

		// Submit button
		submitButton = createStyledButton("Submit", new Color(76, 175, 80)); // Green color
		submitButton.addActionListener(e -> submitButtonActionPerformed(null));

		buttonPanel.add(cancelButton);
		buttonPanel.add(viewRequestsButton);
		buttonPanel.add(submitButton);

		return buttonPanel;
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
		label.setForeground(textColor);
		return label;
	}

	private void configureJDateChooser(JDateChooser dateChooser) {
		dateChooser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		dateChooser.setPreferredSize(new Dimension(250, 35));

		// Make date text field non-editable
		JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
		editor.setEditable(false);
		editor.setBackground(Color.WHITE);
		editor.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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

	private void submitButtonActionPerformed(ActionEvent evt) {
		// Validate input
		if (!validateInput()) {
			return;
		}

		// Add leave request
		addLeaveRequest();

		// Navigate back to employee dashboard
		navigateToEmployeeDashboard();
	}

	private boolean validateInput() {
		// Check if start and end dates are selected
		if (startDateField.getDate() == null || endDateField.getDate() == null) {
			showErrorDialog("Please select both start and end dates.");
			return false;
		}

		// Ensure start date is before or equal to end date
		if (startDateField.getDate().after(endDateField.getDate())) {
			showErrorDialog("Start date must be before or equal to end date.");
			return false;
		}

		return true;
	}

	private void showErrorDialog(String message) {
		JOptionPane.showMessageDialog(
				this,
				message,
				"Validation Error",
				JOptionPane.ERROR_MESSAGE
		);
	}

	private void addLeaveRequest() {
		try {
			// Create new leave request
			LeaveRequest leaveRequest = new LeaveRequest(employeeGI.getEmployeeNumber());

			// Set leave request details
			leaveRequest.setLastName(employeeGI.getLastName());
			leaveRequest.setFirstName(employeeGI.getFirstName());

			// Convert dates to string representation
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
			leaveRequest.setStartDate(dateFormat.format(startDateField.getDate()));
			leaveRequest.setEndDate(dateFormat.format(endDateField.getDate()));

			leaveRequest.setNotes(notesField.getText());
			leaveRequest.setLeaveType(typeOfLeaveDropdown.getSelectedItem().toString());
			leaveRequest.setApproved("Not Approved Yet");

			// Ensure a unique ID is generated
			leaveRequest.setId(UUID.randomUUID().toString());

			// Add the leave request
			JsonFileHandler.addLeaveRequest(leaveRequest);

			// Show success message
			JOptionPane.showMessageDialog(
					this,
					"Leave request submitted successfully.",
					"Success",
					JOptionPane.INFORMATION_MESSAGE
			);
		} catch (Exception e) {
			// Handle any errors during leave request submission
			e.printStackTrace();
			JOptionPane.showMessageDialog(
					this,
					"Error submitting leave request: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	private void navigateToEmployeeDashboard() {
		dispose();
		new EmployeeDashboard(employeeGI, employeeComp).setVisible(true);
	}

	private void cancelButtonActionPerformed(ActionEvent evt) {
		dispose();
		new EmployeeDashboard(employeeGI, employeeComp).setVisible(true);
	}

	private void viewRequestsButtonActionPerformed(ActionEvent evt) {
		dispose();
        new EmployeeLeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
    }
}