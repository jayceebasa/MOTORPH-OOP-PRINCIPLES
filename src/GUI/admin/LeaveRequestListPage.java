package GUI.admin;

import javax.swing.table.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonWriter;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
import Classes.LeaveRequest;
import GUI.employee.EmployeeLeaveRequestListPage;
import UtilityClasses.JsonFileHandler;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class LeaveRequestListPage extends JFrame {

	// Design Constants
	private static final Color PRIMARY_COLOR = new Color(25, 118, 210);  // Material blue
	private static final Color ACCENT_COLOR = new Color(230, 230, 230);  // Light gray
	private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);  // Nearly white
	private static final Color TEXT_COLOR = new Color(33, 33, 33);  // Dark gray for text
	private static final Color BUTTON_TEXT_COLOR = Color.WHITE;
	private static final Color SUCCESS_COLOR = new Color(76, 175, 80);  // Green
	private static final Color ERROR_COLOR = new Color(211, 47, 47);  // Red

	private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
	private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
	private static final Font BODY_FONT = new Font("Segoe UI", Font.PLAIN, 14);
	private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

	private JScrollPane jScrollPane1;
	private JButton goBackButton;
	private JTable jTable1;
	private int numberOfColumns = 9;
	private JButton addEmployeeButton;
	private JButton deleteEmployeeButton;
	private int selectedRow;
	private String employeeNum;
	private JLabel titleLabel;

	// Instantiate two of the user's important information
	GovernmentIdentification employeeGI;
	Compensation employeeComp;
	LeaveRequest leaveRequest;

	public LeaveRequestListPage(GovernmentIdentification employeeGI, Compensation employeeComp) throws ParseException {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;
		initComponents();
		loadEmployeeData();
	}

	private void initComponents() {

		// Set JFrame
		setTitle("MotorPH Payroll System | Leave Requests");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(BACKGROUND_COLOR);

		// Create title label
		titleLabel = new JLabel("Leave Requests");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(PRIMARY_COLOR);

		// Instantiate Table
		jTable1 = new JTable();

		// Instantiate Go Back Button
		goBackButton = new JButton();
		styleButton(goBackButton, "Go Back to Dashboard", PRIMARY_COLOR);
		goBackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				goBackButtonActionPerformed(evt);
			}
		});

		// Create an empty default table model
		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Employee Number",
				"Last Name", "First Name", "Start Date", "End Date", "Status", "Leave Type", "", "" }) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Return the appropriate class for the last column (column with buttons)
				return (columnIndex == getColumnCount() - 1) || (columnIndex == getColumnCount() - 2) ? JButton.class
						: Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// Allow editing only for the last column
				return column == getColumnCount() - 1 || column == getColumnCount() - 2;
			}
		};

		// Modify Table Row Height
		jTable1 = new JTable(model);
		jTable1.setRowHeight(40);
		jTable1.setFont(BODY_FONT);
		jTable1.setGridColor(ACCENT_COLOR);
		jTable1.setShowGrid(true);
		jTable1.setSelectionBackground(new Color(232, 240, 254)); // Light blue highlight
		jTable1.setSelectionForeground(TEXT_COLOR);
		jTable1.setBorder(BorderFactory.createEmptyBorder());

		// Modify the width of the first column
		TableColumn firstColumn = jTable1.getColumnModel().getColumn(0);
		firstColumn.setMinWidth(0);
		firstColumn.setMaxWidth(0);

		// Modify the width of the second column
		TableColumn secondColumn = jTable1.getColumnModel().getColumn(1);
		secondColumn.setPreferredWidth(90); // Set your preferred width here

		// Modify the width of the last column
		TableColumn lastColumn = jTable1.getColumnModel().getColumn(numberOfColumns);
		lastColumn.setPreferredWidth(70); // Set your preferred width here

		// Modify the width of the last column
		TableColumn deleteColumn = jTable1.getColumnModel().getColumn(numberOfColumns - 1);
		deleteColumn.setPreferredWidth(70); // Set your preferred width here

		// Set a custom renderer and editor for the Delete button column
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 1)
				.setCellRenderer(new ButtonRenderer("Delete", ERROR_COLOR));
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 1)
				.setCellEditor(new ButtonEditor(1, "Delete", "DeleteDialogPane", ERROR_COLOR));

		// Set a custom renderer and editor for the View Request column
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 2)
				.setCellRenderer(new ButtonRenderer("View", PRIMARY_COLOR));
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 2)
				.setCellEditor(new ButtonEditor(1, "View", "LeaveRequestDetailsPage", PRIMARY_COLOR));

		// Set custom renderer for the header cells to make them bold
		JTableHeader header = jTable1.getTableHeader();
		header.setBackground(ACCENT_COLOR);
		header.setForeground(TEXT_COLOR);
		header.setFont(HEADER_FONT);
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
		header.setBorder(BorderFactory.createEmptyBorder());

		jScrollPane1 = new JScrollPane(jTable1);
		jScrollPane1.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
		jScrollPane1.getViewport().setBackground(Color.WHITE);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(titleLabel)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup().addComponent(goBackButton).addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addContainerGap(13, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(13, Short.MAX_VALUE)
						.addComponent(titleLabel)
						.addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(goBackButton))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		pack();

		// Make the window appear in the middle
		setLocationRelativeTo(null);
	}

	// Helper method to style buttons consistently
	private void styleButton(JButton button, String text, Color bgColor) {
		button.setText(text);
		button.setFont(BUTTON_FONT);
		button.setBackground(bgColor);
		button.setForeground(BUTTON_TEXT_COLOR);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setCursor(new Cursor(Cursor.HAND_CURSOR));
		button.setPreferredSize(new Dimension(180, 40));

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

	private String convertToValidDateFormat(String inputDate) {
		try {
			// Try multiple date formats
			SimpleDateFormat[] inputFormats = {
					new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"),  // Default toString() format
					new SimpleDateFormat("yyyy-MM-dd"),  // Already correct format
					new SimpleDateFormat("MM/dd/yyyy"),  // Common US format
					new SimpleDateFormat("dd-MM-yyyy")   // Another common format
			};

			for (SimpleDateFormat format : inputFormats) {
				try {
					Date parsedDate = format.parse(inputDate);
					// Convert to YYYY-MM-DD format
					return new SimpleDateFormat("yyyy-MM-dd").format(parsedDate);
				} catch (ParseException e) {
					// Try next format
					continue;
				}
			}

			// If no format works, throw an exception
			throw new ParseException("Unable to parse date: " + inputDate, 0);
		} catch (Exception e) {
			// Log the error or handle it appropriately
			e.printStackTrace();
			return null;  // or return a default date
		}
	}

	private void loadEmployeeData() throws ParseException {
		try {
			// Read the JSON file and parse it using GSON
			FileReader reader = new FileReader(JsonFileHandler.getLeaveRequestJsonPath());
			JsonElement jsonElement = JsonParser.parseReader(reader);

			// Check if the parsed JSON is an array
			if (!jsonElement.isJsonArray()) {
				return;
			}

			JsonArray jsonArray = jsonElement.getAsJsonArray();

			// Check if the JSON array is empty
			if (jsonArray.size() == 0) {
				// Handle the case when the array is empty by creating an empty JSON array
				jsonArray = new JsonArray();
			}

			// Iterate through the JSON array and add data to the table model
			DefaultTableModel model = (DefaultTableModel) ((JTable) jScrollPane1.getViewport().getView()).getModel();
			Gson gson = new Gson();

			// Auto increment employeeNum for record creation
			employeeNum = String.valueOf(jsonArray.size() > 0
					? jsonArray.get(jsonArray.size() - 1).getAsJsonObject().get("employeeNum").getAsInt() + 1
					: 1);

			// Loop through the JSON array
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

				// Directly access the JSON object properties to ensure we get all data
				String id = jsonObject.has("id") ? jsonObject.get("id").getAsString() : "";
				String empNum = jsonObject.has("employeeNum") ? jsonObject.get("employeeNum").getAsString() : "";
				String lastName = jsonObject.has("lastName") ? jsonObject.get("lastName").getAsString() : "";
				String firstName = jsonObject.has("firstName") ? jsonObject.get("firstName").getAsString() : "";

				// Convert start and end dates to valid format
				String startDate = jsonObject.has("startDate") ?
						convertToValidDateFormat(jsonObject.get("startDate").getAsString()) : "";
				String endDate = jsonObject.has("endDate") ?
						convertToValidDateFormat(jsonObject.get("endDate").getAsString()) : "";

				String status = jsonObject.has("approved") ? jsonObject.get("approved").getAsString() : "Pending";
				String leaveType = "";

				// Handle leave_type which may be stored differently
				if (jsonObject.has("leave_type")) {
					leaveType = jsonObject.get("leave_type").getAsString();
				} else if (jsonObject.has("leaveType")) {
					leaveType = jsonObject.get("leaveType").getAsString();
				}

				// If lastName or firstName is null, try to get them from employees.json
				if (lastName == null || lastName.isEmpty() || firstName == null || firstName.isEmpty()) {
					try {
						JsonArray employeesArray = JsonFileHandler.getEmployeesJSON();
						for (JsonElement empElement : employeesArray) {
							JsonObject empObj = empElement.getAsJsonObject();
							if (empObj.get("employeeNum").getAsString().equals(empNum)) {
								if ((lastName == null || lastName.isEmpty()) && empObj.has("last_name")) {
									lastName = empObj.get("last_name").getAsString();
								}
								if ((firstName == null || firstName.isEmpty()) && empObj.has("first_name")) {
									firstName = empObj.get("first_name").getAsString();
								}
								break;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				// Format dates for display
				String formattedStartDate = "";
				String formattedEndDate = "";
				try {
					SimpleDateFormat displayFormat = new SimpleDateFormat("EEE MMM dd, yyyy");
					SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");

					formattedStartDate = startDate.isEmpty() ? "" :
							displayFormat.format(parseFormat.parse(startDate));

					formattedEndDate = endDate.isEmpty() ? "" :
							displayFormat.format(parseFormat.parse(endDate));
				} catch (Exception e) {
					// If date parsing fails, use the original values
					formattedStartDate = startDate;
					formattedEndDate = endDate;
				}

				// Add the data to the table model
				model.addRow(new Object[] {
						id,
						empNum,
						lastName != null ? lastName : "N/A",
						firstName != null ? firstName : "N/A",
						formattedStartDate,
						formattedEndDate,
						status,
						leaveType != null && !leaveType.isEmpty() ? leaveType : "N/A",
						"View",
						"Delete"
				});

				// Also create a LeaveRequest object for later use when viewing details
				LeaveRequest leaveRequest = new LeaveRequest(empNum);
				leaveRequest.setId(id);
				leaveRequest.setLastName(lastName);
				leaveRequest.setFirstName(firstName);
				leaveRequest.setStartDate(startDate);  // Use the converted YYYY-MM-DD format
				leaveRequest.setEndDate(endDate);      // Use the converted YYYY-MM-DD format
				leaveRequest.setApproved(status);
				leaveRequest.setLeaveType(leaveType);
				// Store in a map or collection if needed for faster access when viewing details
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Click event of Go Back to Dashboard Button
	private void goBackButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				// Go back to the dashboard page
				new DashboardPage().setVisible(true);
			}
		});
	}

	private void deleteLeaveEntry(String value) throws IOException {
		JsonArray jsonArray = JsonFileHandler.getLeaveRequestJSON();

		// Instantiate gson class
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// Remove the object
		JsonFileHandler.removeJsonObject(jsonArray, "id", value);

		// Write the modified JsonArray back to the JSON file
		JsonFileHandler.writeJsonFile(gson.toJson(jsonArray), JsonFileHandler.getLeaveRequestJsonPath());
	}

	// Custom on-render look for the button column
	private class ButtonRenderer extends JButton implements TableCellRenderer {
		private String buttonLabel;
		private Color buttonColor;

		public ButtonRenderer(String buttonLabel, Color buttonColor) {
			this.buttonLabel = buttonLabel;
			this.buttonColor = buttonColor;
			setOpaque(true);
			setBackground(buttonColor);
			setForeground(BUTTON_TEXT_COLOR);
			setFont(new Font("Segoe UI", Font.BOLD, 12));
			setBorderPainted(false);
			setFocusPainted(false);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
													   int row, int column) {
			setText(buttonLabel);
			return this;
		}
	}

	// Custom click-event look for the button column
	private class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
		private JButton button;
		private int targetColumn;
		private String buttonLabel;
		private String page;
		private Color buttonColor;

		public ButtonEditor(int targetColumn, String buttonLabel, String page, Color buttonColor) {
			this.targetColumn = targetColumn;
			this.buttonLabel = buttonLabel;
			this.page = page;
			this.buttonColor = buttonColor;

			button = new JButton(this.buttonLabel);
			button.setBackground(buttonColor);
			button.setForeground(BUTTON_TEXT_COLOR);
			button.setFont(new Font("Segoe UI", Font.BOLD, 12));
			button.setBorderPainted(false);
			button.setFocusPainted(false);

			// Add hover effect
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					button.setBackground(buttonColor.darker());
				}

				@Override
				public void mouseExited(MouseEvent e) {
					button.setBackground(buttonColor);
				}
			});

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					java.awt.EventQueue.invokeLater(new Runnable() {
						public void run() {
							if (!page.equals("DeleteDialogPane"))
								dispose();

							// Check what page to go to
							switch (page) {
								case "FullEmployeeDetailsPage":
									// Go to the employees information page
									new FullEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
									break;
								case "UpdateEmployeeDetailsPage":
									// Go to the employees information page
									new UpdateEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
									break;
								case "LeaveRequestDetailsPage":
									// Go to the employees information page
									new LeaveRequestDetailsPage(employeeGI, employeeComp, leaveRequest).setVisible(true);
									break;
								case "DeleteDialogPane":
									// Display a confirmation dialog with styled components
									JPanel panel = new JPanel();
									panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
									panel.setBorder(new EmptyBorder(10, 10, 10, 10));

									JLabel msgLabel = new JLabel("Are you sure you want to delete this leave request?");
									msgLabel.setFont(BODY_FONT);
									panel.add(msgLabel);

									int result = JOptionPane.showConfirmDialog(null, panel,
											"Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

									// Check the user's choice
									if (result == JOptionPane.YES_OPTION) {
										try {
											performDeleteOperation(targetColumn);
											dispose();
											navigateToLeaveRequestListPage();
										} catch (IOException | ParseException e) {
											e.printStackTrace();
										}
									}
									break;
								default:
									new FullEmployeeDetailsPage(employeeGI, employeeComp).setVisible(true);
									break;
							}

						}
					});
				}
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
													 int column) {

			// Call constructor
			employeeGI = new GovernmentIdentification(jTable1.getValueAt(row, targetColumn).toString());
			employeeComp = new Compensation(jTable1.getValueAt(row, targetColumn).toString());
			leaveRequest = new LeaveRequest(jTable1.getValueAt(row, targetColumn).toString());

			selectedRow = row;

			// Set all the important information to be passed
			try {
				LeaveRequest.setLeaveRequestInformationObject(jTable1.getValueAt(row, targetColumn - 1).toString(),
						leaveRequest);
			} catch (IOException e) {
				e.printStackTrace();
			}

			return button;
		}

		@Override
		public Object getCellEditorValue() {
			return "View";
		}
	}

	private void performDeleteOperation(int targetColumn) throws IOException {
		deleteLeaveEntry(jTable1.getValueAt(selectedRow, targetColumn - 1).toString());
	}

	private void navigateToLeaveRequestListPage() throws ParseException {
		new LeaveRequestListPage(employeeGI, employeeComp).setVisible(true);
	}
}