package GUI.admin;

import javax.swing.table.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Classes.Compensation;
import Classes.EmployeeInformation;
import Classes.GovernmentIdentification;
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
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class EmployeeListPage extends JFrame {

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

	public EmployeeListPage() {
		initComponents();
		loadEmployeeData();
	}

	private void initComponents() {

		// Set JFrame
		setTitle("MotorPH Payroll System | Employee List");
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().setBackground(BACKGROUND_COLOR);

		// Create title label
		titleLabel = new JLabel("Employee List");
		titleLabel.setFont(TITLE_FONT);
		titleLabel.setForeground(PRIMARY_COLOR);

		// Instantiate Table
		jTable1 = new JTable();

		addEmployeeButton = new JButton();
		styleButton(addEmployeeButton, "Add Employee", SUCCESS_COLOR);

		// Instantiate Go Back Button
		goBackButton = new JButton();
		styleButton(goBackButton, "Go Back to Dashboard", PRIMARY_COLOR);
		goBackButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		// Create an empty default table model
		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] { "Employee Number",
				"Last Name", "First Name", "SSS No.", "PhilHealth No.", "TIN", "Pagibig No.", "", "", "" }) {
			@Override
			public Class<?> getColumnClass(int columnIndex) {
				// Return the appropriate class for the last column (column with buttons)
				return (columnIndex == getColumnCount() - 1) || (columnIndex == getColumnCount() - 2) ? JButton.class
						: Object.class;
			}

			@Override
			public boolean isCellEditable(int row, int column) {
				// Allow editing only for the last column
				return column == getColumnCount() - 1 || column == getColumnCount() - 2
						|| column == getColumnCount() - 3;
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
		firstColumn.setPreferredWidth(90); // Set your preferred width here

		// Modify the width of the last column
		TableColumn lastColumn = jTable1.getColumnModel().getColumn(numberOfColumns);
		lastColumn.setPreferredWidth(50); // Set your preferred width here

		// Modify the width of the last column
		TableColumn editColumn = jTable1.getColumnModel().getColumn(numberOfColumns - 2);
		editColumn.setPreferredWidth(40); // Set your preferred width here

		// Modify the width of the last column
		TableColumn deleteColumn = jTable1.getColumnModel().getColumn(numberOfColumns - 1);
		deleteColumn.setPreferredWidth(50); // Set your preferred width here

		// Set a custom renderer and editor for the Edit Column
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 1).setCellRenderer(new ButtonRenderer("Delete", ERROR_COLOR));
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 1)
				.setCellEditor(new ButtonEditor(0, "Delete", "DeleteDialogPane", ERROR_COLOR));

		// Set a custom renderer and editor for the View Employee column
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 2)
				.setCellRenderer(new ButtonRenderer("View", PRIMARY_COLOR));
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 2)
				.setCellEditor(new ButtonEditor(0, "View", "FullEmployeeDetailsPage", PRIMARY_COLOR));

		// Set a custom renderer and editor for the Edit Column
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 3).setCellRenderer(new ButtonRenderer("Edit", PRIMARY_COLOR));
		jTable1.getColumnModel().getColumn(model.getColumnCount() - 3)
				.setCellEditor(new ButtonEditor(0, "Edit", "UpdateEmployeeDetailsPage", PRIMARY_COLOR));

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

		addEmployeeButton.setText("Add Employee");
		addEmployeeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addEmployeeButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(titleLabel)
								.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 996, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup().addComponent(goBackButton)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(addEmployeeButton)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap(13, Short.MAX_VALUE)
						.addComponent(titleLabel)
						.addGap(15, 15, 15)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(goBackButton).addComponent(addEmployeeButton))
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
		button.setPreferredSize(new Dimension(150, 40));

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

	private void loadEmployeeData() {
		try {
			// Read the JSON file and parse it using GSON
			FileReader reader = new FileReader(JsonFileHandler.getEmployeesJsonPath());
			JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

			// Iterate through the JSON array and add data to the table model
			DefaultTableModel model = (DefaultTableModel) ((JTable) jScrollPane1.getViewport().getView()).getModel();
			Gson gson = new Gson();

			// Auto increment employeeNum for record creation
			employeeNum = String
					.valueOf(jsonArray.get(jsonArray.size() - 1).getAsJsonObject().get("employeeNum").getAsInt() + 1);

			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				GovernmentIdentification employee = gson.fromJson(jsonObject, GovernmentIdentification.class);

				// Add the data to the table model
				model.addRow(new Object[] { employee.getEmployeeNumber(), employee.getLastName(),
						employee.getFirstName(), employee.getSSSNumber(), employee.getPhilHealthNumber(),
						employee.getTinNumber(), employee.getPagibigNumber(), "View" });
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Click event of Go Back to Dashboard Button
	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				// Go back to the dashboard page
				new DashboardPage().setVisible(true);
			}
		});
	}

	private void addEmployeeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Close the employee list
				dispose();

				// Refresh the Employees Page
				new AddEmployeeDetailsPage(employeeNum).setVisible(true);
			}
		});
	}

	private void deleteEmployeeButtonActionPerformed(String employeeNumToRemove) throws IOException {
		JsonArray jsonArray = JsonFileHandler.getEmployeesJSON();

		// Instantiate gson class
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// Remove the object
		JsonFileHandler.removeJsonObject(jsonArray, "employeeNum", employeeNumToRemove);

		// Write the modified JsonArray back to the JSON file
		JsonFileHandler.writeJsonFile(gson.toJson(jsonArray), JsonFileHandler.getEmployeesJsonPath());

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				// Remove the EmployeesPage Window
				dispose();

				// Refresh the Employees Page
				new EmployeeListPage().setVisible(true);
			}
		});
	}

	private void deleteLoginCredentials(String employeeNumToRemove) throws IOException {
		JsonArray jsonArray = JsonFileHandler.getLoginCredentialsJSON();

		// Instantiate gson class
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// Remove the object
		JsonFileHandler.removeJsonObject(jsonArray, "employeeNum", employeeNumToRemove);

		// Write the modified JsonArray back to the JSON file
		JsonFileHandler.writeJsonFile(gson.toJson(jsonArray), JsonFileHandler.getLoginCredentialsJsonPath());
	}

	// Custom on-render look for the button column
	private class ButtonRenderer extends JButton implements TableCellRenderer {
		private String buttonLabel;

		public ButtonRenderer(String buttonLabel, Color bgColor) {
			this.buttonLabel = buttonLabel;
			setOpaque(true);
			setBackground(bgColor);
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
		private Color buttonColor;

		public ButtonEditor(int targetColumn, String buttonLabel, String page, Color buttonColor) {
			this.targetColumn = targetColumn;
			this.buttonLabel = buttonLabel;
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
								case "DeleteDialogPane":
									// Display a confirmation dialog
									JPanel panel = new JPanel();
									panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
									panel.setBorder(new EmptyBorder(10, 10, 10, 10));

									JLabel msgLabel = new JLabel("Are you sure you want to delete this employee?");
									msgLabel.setFont(BODY_FONT);
									panel.add(msgLabel);

									int result = JOptionPane.showConfirmDialog(null, panel,
											"Confirm Deletion", JOptionPane.YES_NO_OPTION,
											JOptionPane.WARNING_MESSAGE);

									// Check the user's choice
									if (result == JOptionPane.YES_OPTION) {
										try {
											deleteLoginCredentials(
													jTable1.getValueAt(selectedRow, targetColumn).toString());
											deleteEmployeeButtonActionPerformed(
													jTable1.getValueAt(selectedRow, targetColumn).toString());
										} catch (IOException e) {
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

			selectedRow = row;

			// Set all the important information to be passed
			try {
				EmployeeInformation.setEmployeeInformationObject(jTable1.getValueAt(row, targetColumn).toString(),
						employeeGI, employeeComp);
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
}