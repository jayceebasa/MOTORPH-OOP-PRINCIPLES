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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import Classes.Compensation;
import Classes.GovernmentIdentification;
import Classes.LeaveRequest;
import GUI.employee.EmployeeDashboard;
import UtilityClasses.JsonFileHandler;

@SuppressWarnings("serial")
public class LeaveRequestDetailsPage extends JFrame {

	// Design Constants
	private static final Color PRIMARY_COLOR = new Color(25, 118, 210);  // Material blue
	private static final Color ACCENT_COLOR = new Color(230, 230, 230);  // Light gray
	private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Nearly white
	private static final Color TEXT_COLOR = new Color(33, 33, 33);  // Dark gray for text
	private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
	private static final Color PANEL_BACKGROUND = Color.WHITE;
	private static final Color SUCCESS_COLOR = new Color(76, 175, 80);  // Green
	private static final Color ERROR_COLOR = new Color(211, 47, 47);  // Red

	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);
	private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
	private static final Font VALUE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

	private javax.swing.JLabel endDateField;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextArea notesField;
	private javax.swing.JLabel leaveRequestLabel;
	private javax.swing.JLabel notesLabel;
	private javax.swing.JLabel startDateField;
	private javax.swing.JButton submitButton;
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton rejectButton;
	private javax.swing.JLabel typeOfLeaveDropdown;
	private javax.swing.JLabel typeOfLeaveLabel;
	private JPanel mainPanel;
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;
	private LeaveRequest leaveRequest;

	public LeaveRequestDetailsPage(GovernmentIdentification employeeGI, Compensation employeeComp, LeaveRequest leaveRequest) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;
		this.leaveRequest = leaveRequest;
		initComponents();
	}

	@SuppressWarnings("unchecked")
	private void initComponents() {
		// Create main components
		leaveRequestLabel = new javax.swing.JLabel();
		typeOfLeaveLabel = new javax.swing.JLabel();
		typeOfLeaveDropdown = new javax.swing.JLabel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		startDateField = new javax.swing.JLabel();
		endDateField = new javax.swing.JLabel();
		notesLabel = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		notesField = new javax.swing.JTextArea();
		submitButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		rejectButton = new javax.swing.JButton();
		mainPanel = new JPanel();

		// Set frame properties
		setTitle("MotorPH Payroll System | Leave Request Details");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(BACKGROUND_COLOR);

		// Style main panel
		mainPanel.setBackground(PANEL_BACKGROUND);
		mainPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(ACCENT_COLOR),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		// Style components
		leaveRequestLabel.setFont(TITLE_FONT);
		leaveRequestLabel.setForeground(PRIMARY_COLOR);
		leaveRequestLabel.setText("Leave Request Details");
		leaveRequestLabel.setHorizontalAlignment(SwingConstants.CENTER);

		styleLabel(typeOfLeaveLabel, "Type of Leave");

		typeOfLeaveDropdown.setFont(VALUE_FONT);
		typeOfLeaveDropdown.setText(leaveRequest.getLeaveType());

		styleLabel(jLabel1, "Start Date");

		styleLabel(jLabel2, "End Date");

		startDateField.setFont(VALUE_FONT);
		startDateField.setPreferredSize(new java.awt.Dimension(126, 22));

		try {
			startDateField.setText(new SimpleDateFormat("EEE MMM dd, yyyy")
					.format(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(leaveRequest.getStartDate())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		endDateField.setFont(VALUE_FONT);
		endDateField.setPreferredSize(new java.awt.Dimension(126, 22));

		try {
			endDateField.setText(new SimpleDateFormat("EEE MMM dd, yyyy")
					.format(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(leaveRequest.getEndDate())));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		styleLabel(notesLabel, "Notes");

		notesField.setColumns(20);
		notesField.setRows(5);
		notesField.setText(leaveRequest.getNotes());
		notesField.setEditable(false);
		notesField.setFont(VALUE_FONT);
		notesField.setLineWrap(true);
		notesField.setWrapStyleWord(true);
		notesField.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(ACCENT_COLOR),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		jScrollPane1.setViewportView(notesField);
		jScrollPane1.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));

		// Style buttons
		styleButton(submitButton, "Approve", SUCCESS_COLOR);
		styleButton(cancelButton, "Cancel", PRIMARY_COLOR);
		styleButton(rejectButton, "Reject", ERROR_COLOR);

		submitButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				approveButtonActionPerformed(evt);
			}
		});

		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		rejectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					rejectButtonActionPerformed(evt);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// Layout the components on the main panel
		javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(
				mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(mainPanelLayout.createSequentialGroup()
								.addGap(20, 20, 20)
								.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
										.addComponent(leaveRequestLabel)
										.addGroup(mainPanelLayout.createSequentialGroup()
												.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(typeOfLeaveLabel)
														.addComponent(jLabel1)
														.addComponent(jLabel2)
														.addComponent(notesLabel))
												.addGap(30, 30, 30)
												.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(typeOfLeaveDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addGroup(mainPanelLayout.createSequentialGroup()
												.addComponent(submitButton)
												.addGap(20, 20, 20)
												.addComponent(cancelButton)
												.addGap(20, 20, 20)
												.addComponent(rejectButton)))
								.addContainerGap(20, Short.MAX_VALUE))
		);
		mainPanelLayout.setVerticalGroup(
				mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(mainPanelLayout.createSequentialGroup()
								.addGap(20, 20, 20)
								.addComponent(leaveRequestLabel)
								.addGap(30, 30, 30)
								.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(typeOfLeaveLabel)
										.addComponent(typeOfLeaveDropdown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(20, 20, 20)
								.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel1)
										.addComponent(startDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(20, 20, 20)
								.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel2)
										.addComponent(endDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(20, 20, 20)
								.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(notesLabel)
										.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(30, 30, 30)
								.addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(submitButton)
										.addComponent(cancelButton)
										.addComponent(rejectButton))
								.addContainerGap(20, Short.MAX_VALUE))
		);

		// Create layout for frame
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGap(20, 20, 20)
								.addComponent(mainPanel)
								.addGap(20, 20, 20))
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addGap(20, 20, 20)
								.addComponent(mainPanel)
								.addGap(20, 20, 20))
		);

		pack();
		setLocationRelativeTo(null);
	}

	// Helper method to style labels
	private void styleLabel(JLabel label, String text) {
		label.setText(text);
		label.setFont(LABEL_FONT);
		label.setForeground(TEXT_COLOR);
	}

	// Helper method to style buttons
	private void styleButton(JButton button, String text, Color bgColor) {
		button.setText(text);
		button.setFont(BUTTON_FONT);
		button.setBackground(bgColor);
		button.setForeground(BUTTON_TEXT_COLOR);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(120, 40));

		// Add hover effect
		final Color originalColor = bgColor;
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(originalColor.darker());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(originalColor);
			}
		});
	}

	private void rejectButtonActionPerformed(java.awt.event.ActionEvent evt) throws IOException {
		// Get the leave request JSON array
		JsonArray jsonArrayLeaveRequest = JsonFileHandler.getLeaveRequestJSON();

		// Get the properties we need to find the correct leave request
		String employeeNumToUpdate = leaveRequest.getEmployeeNum();
		String startDate = leaveRequest.getStartDate();
		String endDate = leaveRequest.getEndDate();
		String notes = leaveRequest.getNotes();
		String leaveType = leaveRequest.getLeaveType();
		String id = leaveRequest.getId();

		// Update the status to "Rejected"
		boolean updated = updateLeaveRequestStatus(jsonArrayLeaveRequest, id, employeeNumToUpdate,
				startDate, endDate, notes, leaveType, "Rejected");

		if (updated) {
			// Convert updated JsonArray to string
			String updatedJson = jsonArrayLeaveRequest.toString();

			// Write the updated JSON back to file
			JsonFileHandler.writeJsonFile(updatedJson, JsonFileHandler.getLeaveRequestJsonPath());

			// Show success message
			JOptionPane.showMessageDialog(this,
					"Leave request has been rejected.",
					"Status Updated",
					JOptionPane.INFORMATION_MESSAGE);

			// Return to leave request list
			navigateToLeaveRequestList();
		} else {
			// Show error message if update failed
			JOptionPane.showMessageDialog(this,
					"Failed to update leave request status. Please try again.",
					"Update Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void approveButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			// Get the leave request JSON array
			JsonArray jsonArrayLeaveRequest = JsonFileHandler.getLeaveRequestJSON();

			// Get the properties we need to find the correct leave request
			String employeeNumToUpdate = leaveRequest.getEmployeeNum();
			String startDate = leaveRequest.getStartDate();
			String endDate = leaveRequest.getEndDate();
			String notes = leaveRequest.getNotes();
			String leaveType = leaveRequest.getLeaveType();
			String id = leaveRequest.getId();

			// Update the status to "Approved"
			boolean updated = updateLeaveRequestStatus(jsonArrayLeaveRequest, id, employeeNumToUpdate,
					startDate, endDate, notes, leaveType, "Approved");

			if (updated) {
				// Convert updated JsonArray to string
				String updatedJson = jsonArrayLeaveRequest.toString();

				// Write the updated JSON back to file
				JsonFileHandler.writeJsonFile(updatedJson, JsonFileHandler.getLeaveRequestJsonPath());

				// Show success message
				JOptionPane.showMessageDialog(this,
						"Leave request has been approved.",
						"Status Updated",
						JOptionPane.INFORMATION_MESSAGE);

				// Return to leave request list
				navigateToLeaveRequestList();
			} else {
				// Show error message if update failed
				JOptionPane.showMessageDialog(this,
						"Failed to update leave request status. Please try again.",
						"Update Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this,
					"An error occurred: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		navigateToLeaveRequestList();
	}

	// Helper method to navigate back to leave request list
	private void navigateToLeaveRequestList() {
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

	// Updated method to find and update a leave request status
	private boolean updateLeaveRequestStatus(
			JsonArray jsonArray, String id, String employeeNum,
			String startDate, String endDate, String notes,
			String leaveType, String newStatus) {

		// Log the values we're looking for (for debugging)
		System.out.println("Looking for leave request with:");
		System.out.println("ID: " + id);
		System.out.println("Employee #: " + employeeNum);
		System.out.println("Start Date: " + startDate);
		System.out.println("End Date: " + endDate);

		// First try to find by ID (most reliable)
		if (id != null && !id.isEmpty()) {
			for (JsonElement element : jsonArray) {
				if (!element.isJsonObject()) continue;

				JsonObject requestObject = element.getAsJsonObject();
				if (requestObject.has("id") && requestObject.get("id").getAsString().equals(id)) {
					// Found the leave request by ID
					requestObject.addProperty("approved", newStatus);
					return true;
				}
			}
		}

		// If ID search failed, try to find by other properties
		for (JsonElement element : jsonArray) {
			if (!element.isJsonObject()) continue;

			JsonObject requestObject = element.getAsJsonObject();
			boolean matchesEmployeeNum = requestObject.has("employeeNum") &&
					requestObject.get("employeeNum").getAsString().equals(employeeNum);

			boolean matchesStartDate = requestObject.has("startDate") &&
					requestObject.get("startDate").getAsString().equals(startDate);

			boolean matchesEndDate = requestObject.has("endDate") &&
					requestObject.get("endDate").getAsString().equals(endDate);

			// Only require employee number and dates to match for unique identification
			if (matchesEmployeeNum && matchesStartDate && matchesEndDate) {
				// Found the leave request by properties
				System.out.println("Found matching leave request - updating status to: " + newStatus);
				requestObject.addProperty("approved", newStatus);
				return true;
			}
		}

		System.out.println("No matching leave request found!");
		return false;
	}
}