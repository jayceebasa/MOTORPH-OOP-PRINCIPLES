// Filename: LeaveRequest.java
package Classes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import UtilityClasses.JsonFileHandler;

/**
 * Represents an employee's leave request
 */
public class LeaveRequest {
	private String id;
	private String employeeNum;
	private String firstName;
	private String lastName;
	private String startDate;
	private String endDate;
	private String notes;
	private String leaveType;
	private String approved = "Not Approved Yet.";

	/**
	 * Constructor with employee number
	 * @param employeeNum Employee number
	 */
	public LeaveRequest(String employeeNum) {
		this.employeeNum = employeeNum;
		this.id = UUID.randomUUID().toString();
	}

	/**
	 * Get employee number
	 * @return Employee number
	 */
	public String getEmployeeNum() {
		return employeeNum;
	}

	/**
	 * Set employee number with validation
	 * @param employeeNum Employee number to set
	 */
	public void setEmployeeNum(String employeeNum) {
		if (employeeNum == null || employeeNum.trim().isEmpty()) {
			throw new IllegalArgumentException("Employee number cannot be empty");
		}
		this.employeeNum = employeeNum.trim();
	}

	/**
	 * Get first name
	 * @return First name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set first name with validation
	 * @param firstName First name to set
	 */
	public void setFirstName(String firstName) {
		if (firstName == null || firstName.trim().isEmpty()) {
			throw new IllegalArgumentException("First name cannot be empty");
		}
		this.firstName = firstName.trim();
	}

	/**
	 * Get last name
	 * @return Last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set last name with validation
	 * @param lastName Last name to set
	 */
	public void setLastName(String lastName) {
		if (lastName == null || lastName.trim().isEmpty()) {
			throw new IllegalArgumentException("Last name cannot be empty");
		}
		this.lastName = lastName.trim();
	}

	/**
	 * Get start date
	 * @return Start date
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Set start date with validation
	 * @param startDate Start date to set
	 */
	public void setStartDate(String startDate) {
		validateDate(startDate);
		this.startDate = startDate;
	}

	/**
	 * Get end date
	 * @return End date
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Set end date with validation
	 * @param endDate End date to set
	 */
	public void setEndDate(String endDate) {
		validateDate(endDate);

		// Ensure end date is after start date if both are set
		if (this.startDate != null) {
			LocalDate start = LocalDate.parse(this.startDate);
			LocalDate end = LocalDate.parse(endDate);
			if (end.isBefore(start)) {
				throw new IllegalArgumentException("End date must be after start date");
			}
		}

		this.endDate = endDate;
	}

	/**
	 * Validate date format
	 * @param date Date to validate
	 */
	private void validateDate(String date) {
		try {
			LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
		}
	}

	/**
	 * Get notes
	 * @return Notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * Set notes with length validation
	 * @param notes Notes to set
	 */
	public void setNotes(String notes) {
		if (notes != null && notes.length() > 500) {
			throw new IllegalArgumentException("Notes cannot exceed 500 characters");
		}
		this.notes = notes;
	}

	/**
	 * Get leave type
	 * @return Leave type
	 */
	public String getLeaveType() {
		return leaveType;
	}

	/**
	 * Set leave type with validation
	 * @param leaveType Leave type to set
	 */
	public void setLeaveType(String leaveType) {
		if (leaveType == null || leaveType.trim().isEmpty()) {
			throw new IllegalArgumentException("Leave type cannot be empty");
		}
		this.leaveType = leaveType.trim();
	}

	/**
	 * Check approval status
	 * @return Approval status
	 */
	public String isApproved() {
		return approved;
	}

	/**
	 * Set approval status
	 * @param approved Approval status to set
	 */
	public void setApproved(String approved) {
		this.approved = approved;
	}

	/**
	 * Get leave request ID
	 * @return Leave request ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set leave request ID
	 * @param id Leave request ID to set
	 */
	public void setId(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("ID cannot be empty");
		}
		this.id = id.trim();
	}

	/**
	 * Set leave request information from JSON data
	 * @param value Identifier value
	 * @param leaveRequest Leave request object to populate
	 * @throws IOException If JSON reading fails
	 */
	public static void setLeaveRequestInformationObject(String value, LeaveRequest leaveRequest) throws IOException {
		// Validate input
		if (value == null || value.trim().isEmpty()) {
			throw new IllegalArgumentException("Identifier value cannot be empty");
		}
		if (leaveRequest == null) {
			throw new IllegalArgumentException("Leave request object cannot be null");
		}

		// Retrieve employee data
		JsonObject employeeData = JsonFileHandler.nameIterator(
				JsonFileHandler.getLeaveRequestJSON(),
				"id",
				value
		);

		// Parse JSON data
		Gson gson = new Gson();
		LeaveRequest leaveRequestInfo = gson.fromJson(employeeData, LeaveRequest.class);

		// Populate leave request object with retrieved data
		leaveRequest.setEmployeeNum(leaveRequestInfo.getEmployeeNum());
		leaveRequest.setLastName(leaveRequestInfo.getLastName());
		leaveRequest.setFirstName(leaveRequestInfo.getFirstName());
		leaveRequest.setEndDate(leaveRequestInfo.getEndDate());
		leaveRequest.setStartDate(leaveRequestInfo.getStartDate());
		leaveRequest.setLeaveType(leaveRequestInfo.getLeaveType());
		leaveRequest.setNotes(leaveRequestInfo.getNotes());
		leaveRequest.setApproved(leaveRequestInfo.isApproved());
	}
}