package Classes;

import java.io.IOException;
import java.util.UUID;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import UtilityClasses.JsonFileHandler;

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

	public LeaveRequest(String employeeNum) {
		this.employeeNum = employeeNum;
		this.id = UUID.randomUUID().toString();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String isApproved() {
		return approved;
	}

	public void setApproved(String approved) {
		this.approved = approved;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public static void setLeaveRequestInformationObject(String value, LeaveRequest leaveRequest) throws IOException {
		JsonObject employeeData = JsonFileHandler.nameIterator(JsonFileHandler.getLeaveRequestJSON(), "id", value);
		Gson gson = new Gson();
		LeaveRequest leaveRequestInfo = gson.fromJson(employeeData, LeaveRequest.class);

		leaveRequest.setEmployeeNum(leaveRequestInfo.getEmployeeNum());
		leaveRequest.setLastName(leaveRequestInfo.getLastName());
		leaveRequest.setFirstName(leaveRequestInfo.getFirstName());
		leaveRequest.setEndDate(leaveRequestInfo.getEndDate());
		leaveRequest.setStartDate(leaveRequestInfo.getStartDate());
		leaveRequest.setLeaveType(leaveRequestInfo.getLeaveType());
		leaveRequest.setNotes(leaveRequestInfo.getNotes());
		leaveRequest.setApproved(leaveRequestInfo.isApproved());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}