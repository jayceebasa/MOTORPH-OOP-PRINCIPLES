package GUI.employee;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import Classes.Compensation;
import Classes.GovernmentIdentification;
import UtilityClasses.JsonFileHandler;

public class EmployeeLeaveRequestListPage extends JFrame {
	// UI Design Constants
	private static final Color BACKGROUND_COLOR = new Color(250, 250, 250);
	private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
	private static final Color ACCENT_COLOR = new Color(0, 105, 217);
	private static final Color TEXT_COLOR = new Color(33, 33, 33);
	private static final Color DELETE_COLOR = new Color(244, 67, 54);

	// Employee Context
	private GovernmentIdentification employeeGI;
	private Compensation employeeComp;

	// UI Components
	private JTable leaveRequestTable;
	private DefaultTableModel tableModel;
	private JButton backButton;
	private JButton refreshButton;

	public EmployeeLeaveRequestListPage(GovernmentIdentification employeeGI, Compensation employeeComp) {
		this.employeeGI = employeeGI;
		this.employeeComp = employeeComp;

		initializeComponents();
		setupFrame();
		loadLeaveRequests();
	}

	private void initializeComponents() {
		// Create table model
		String[] columnNames = {"ID", "Start Date", "End Date", "Leave Type", "Status", "Actions"};
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == getColumnCount() - 1;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == getColumnCount() - 1) return JButton.class;
				return String.class;
			}
		};

		// Create table
		leaveRequestTable = new JTable(tableModel);

		// Customize table appearance
		customizeTable();
	}

	private void customizeTable() {
		// Table header styling
		JTableHeader header = leaveRequestTable.getTableHeader();
		header.setBackground(PRIMARY_COLOR);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("SansSerif", Font.BOLD, 14));
		header.setPreferredSize(new Dimension(header.getWidth(), 40));

		// Row styling
		leaveRequestTable.setRowHeight(35);
		leaveRequestTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
		leaveRequestTable.setSelectionBackground(new Color(200, 230, 255));

		// Hide ID column
		leaveRequestTable.getColumnModel().getColumn(0).setMinWidth(0);
		leaveRequestTable.getColumnModel().getColumn(0).setMaxWidth(0);

		// Add delete button column
		TableColumn actionColumn = leaveRequestTable.getColumnModel().getColumn(getColumnCount() - 1);
		actionColumn.setCellRenderer(new DeleteButtonRenderer());
		actionColumn.setCellEditor(new DeleteButtonEditor());
	}

	private void setupFrame() {
		// Frame setup
		setTitle("Leave Requests - " + employeeGI.getFirstName() + " " + employeeGI.getLastName());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 500);
		setLocationRelativeTo(null);

		// Main panel
		JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
		mainPanel.setBackground(BACKGROUND_COLOR);
		mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		// Create header
		JPanel headerPanel = createHeaderPanel();
		mainPanel.add(headerPanel, BorderLayout.NORTH);

		// Add table to scrollpane
		JScrollPane scrollPane = new JScrollPane(leaveRequestTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		// Set content pane
		setContentPane(mainPanel);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBackground(BACKGROUND_COLOR);

		// Title
		JLabel titleLabel = new JLabel("Leave Requests");
		titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		titleLabel.setForeground(PRIMARY_COLOR);

		// Button panel
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBackground(BACKGROUND_COLOR);

		// Back button
		backButton = createStyledButton("Back", ACCENT_COLOR);
		backButton.addActionListener(e -> navigateBack());

		// Refresh button
		refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
		refreshButton.addActionListener(e -> loadLeaveRequests());

		buttonPanel.add(refreshButton);
		buttonPanel.add(backButton);

		headerPanel.add(titleLabel, BorderLayout.WEST);
		headerPanel.add(buttonPanel, BorderLayout.EAST);

		return headerPanel;
	}

	private JButton createStyledButton(String text, Color backgroundColor) {
		JButton button = new JButton(text);
		button.setBackground(backgroundColor);
		button.setForeground(Color.WHITE);
		button.setFont(new Font("SansSerif", Font.BOLD, 12));
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setPreferredSize(new Dimension(100, 35));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setBackground(backgroundColor.darker());
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setBackground(backgroundColor);
			}
		});

		return button;
	}

	private void loadLeaveRequests() {
		// Clear existing rows
		tableModel.setRowCount(0);

		try {
			// Fetch leave requests
			JsonArray leaveRequests = JsonFileHandler.getLeaveRequestJSON();

			// Iterate and filter employee's leave requests
			for (int i = 0; i < leaveRequests.size(); i++) {
				JsonObject request = leaveRequests.get(i).getAsJsonObject();

				// Check if request belongs to current employee
				if (request.get("employeeNum").getAsString().equals(employeeGI.getEmployeeNumber())) {
					String id = request.get("id").getAsString();
					String startDate = formatDate(request.get("startDate").getAsString());
					String endDate = formatDate(request.get("endDate").getAsString());
					String leaveType = request.get("leaveType").getAsString();
					String status = request.get("approved").getAsString();

					tableModel.addRow(new Object[]{
							id, startDate, endDate, leaveType, status, "Delete"
					});
				}
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Error loading leave requests: " + e.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String formatDate(String dateString) {
		try {
			return new SimpleDateFormat("MMM dd, yyyy").format(
					new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(dateString)
			);
		} catch (ParseException e) {
			return dateString;
		}
	}

	private void navigateBack() {
		dispose();
		new LeaveRequestPage(employeeGI, employeeComp).setVisible(true);
	}

	// Custom button renderer
	private class DeleteButtonRenderer extends JButton implements TableCellRenderer {
		public DeleteButtonRenderer() {
			setOpaque(true);
			setText("Delete");
			setBackground(DELETE_COLOR);
			setForeground(Color.WHITE);
			setFont(new Font("SansSerif", Font.BOLD, 12));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value,
													   boolean isSelected, boolean hasFocus, int row, int column) {
			return this;
		}
	}

	// Custom button editor
	private class DeleteButtonEditor extends AbstractCellEditor implements TableCellEditor {
		private JButton deleteButton;
		private int selectedRow;

		public DeleteButtonEditor() {
			deleteButton = new JButton("Delete");
			deleteButton.setBackground(DELETE_COLOR);
			deleteButton.setForeground(Color.WHITE);
			deleteButton.setFont(new Font("SansSerif", Font.BOLD, 12));

			deleteButton.addActionListener(e -> {
				// Confirm deletion
				int confirm = JOptionPane.showConfirmDialog(
						null,
						"Are you sure you want to delete this leave request?",
						"Confirm Deletion",
						JOptionPane.YES_NO_OPTION
				);

				if (confirm == JOptionPane.YES_OPTION) {
					try {
						// Get request ID to delete
						String requestId = tableModel.getValueAt(selectedRow, 0).toString();
						deleteLeaveRequest(requestId);

						// Remove row from table
						tableModel.removeRow(selectedRow);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(
								null,
								"Error deleting leave request: " + ex.getMessage(),
								"Error",
								JOptionPane.ERROR_MESSAGE
						);
					}
				}

				fireEditingStopped();
			});
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value,
													 boolean isSelected, int row, int column) {
			selectedRow = row;
			return deleteButton;
		}

		@Override
		public Object getCellEditorValue() {
			return "Delete";
		}
	}

	private void deleteLeaveRequest(String requestId) throws IOException {
		// Get current leave requests
		JsonArray leaveRequests = JsonFileHandler.getLeaveRequestJSON();

		// Remove matching request
		for (int i = 0; i < leaveRequests.size(); i++) {
			JsonObject request = leaveRequests.get(i).getAsJsonObject();
			if (request.get("id").getAsString().equals(requestId)) {
				leaveRequests.remove(i);
				break;
			}
		}

		// Write updated requests back to file
		JsonFileHandler.writeJsonFile(
				new Gson().toJson(leaveRequests),
				JsonFileHandler.getLeaveRequestJsonPath()
		);
	}

	private int getColumnCount() {
		return tableModel.getColumnCount();
	}
}