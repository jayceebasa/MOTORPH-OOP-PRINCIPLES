// Filename: EmployeeInformation.java
package Classes;

import java.io.IOException;
import java.util.Objects;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import UtilityClasses.JsonFileHandler;

/**
 * Represents detailed employee information
 */
public class EmployeeInformation extends User {
	@SerializedName("last_name")
	private String lastName;

	@SerializedName("first_name")
	private String firstName;

	private String birthday;
	private String address;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("Position")
	private String position;

	@SerializedName("immediate_supervisor")
	private String immediateSupervisor;

	@SerializedName("hourly_rate")
	private double hourlyRate;

	@SerializedName("Status")
	private String status;

	/**
	 * Constructor with user credentials
	 * @param userId User's unique identifier
	 * @param password User's password
	 * @throws IOException If authentication fails
	 */
	public EmployeeInformation(String userId, String password) throws IOException {
		super(userId, password);
	}

	/**
	 * Constructor with employee number
	 * @param employeeNumber Unique employee identifier
	 */
	public EmployeeInformation(String employeeNumber) {
		super(employeeNumber);
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
			throw new IllegalArgumentException("Last name cannot be null or empty");
		}
		this.lastName = lastName.trim();
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
			throw new IllegalArgumentException("First name cannot be null or empty");
		}
		this.firstName = firstName.trim();
	}

	/**
	 * Get birthday
	 * @return Birthday
	 */
	public String getBirthday() {
		return birthday;
	}

	/**
	 * Set birthday
	 * @param birthday Birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * Get address
	 * @return Address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Set address with validation
	 * @param address Address to set
	 */
	public void setAddress(String address) {
		if (address == null || address.trim().isEmpty()) {
			throw new IllegalArgumentException("Address cannot be null or empty");
		}
		this.address = address.trim();
	}

	/**
	 * Get phone number
	 * @return Phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Set phone number with validation
	 * @param phoneNumber Phone number to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber == null) {
			throw new IllegalArgumentException("Invalid phone number format");
		}
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Get position
	 * @return Position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * Set position
	 * @param position Position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * Get immediate supervisor
	 * @return Immediate supervisor
	 */
	public String getImmediateSupervisor() {
		return immediateSupervisor;
	}

	/**
	 * Set immediate supervisor
	 * @param immediateSupervisor Immediate supervisor to set
	 */
	public void setImmediateSupervisor(String immediateSupervisor) {
		this.immediateSupervisor = immediateSupervisor;
	}

	/**
	 * Get hourly rate
	 * @return Hourly rate
	 */
	public double getHourlyRate() {
		return hourlyRate;
	}

	/**
	 * Set hourly rate with validation
	 * @param hourlyRate Hourly rate to set
	 */
	public void setHourlyRate(double hourlyRate) {
		if (hourlyRate < 0) {
			throw new IllegalArgumentException("Hourly rate cannot be negative");
		}
		this.hourlyRate = hourlyRate;
	}

	/**
	 * Get status
	 * @return Status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Set status
	 * @param status Status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Set employee information from JSON data
	 * @param employeeNumber Employee number
	 * @param employeeGI Government identification object
	 * @param employeeComp Compensation object
	 * @throws IOException If JSON reading fails
	 */
	public static void setEmployeeInformationObject(String employeeNumber, GovernmentIdentification employeeGI, Compensation employeeComp) throws IOException {
		JsonObject employeeData = JsonFileHandler.nameIterator(JsonFileHandler.getEmployeesJSON(), "employeeNum", employeeNumber);
		Gson gson = new Gson();
		GovernmentIdentification employeeGovInfo = gson.fromJson(employeeData, GovernmentIdentification.class);
		Compensation employeeCompInfo = gson.fromJson(employeeData, Compensation.class);

		// Detailed copying with validation
		Objects.requireNonNull(employeeGI, "Government Identification cannot be null");
		Objects.requireNonNull(employeeComp, "Compensation cannot be null");

		employeeGI.setLastName(employeeGovInfo.getLastName());
		employeeGI.setFirstName(employeeGovInfo.getFirstName());
		employeeGI.setBirthday(employeeGovInfo.getBirthday());
		employeeGI.setAddress(employeeGovInfo.getAddress());
		employeeGI.setPhoneNumber(employeeGovInfo.getPhoneNumber());
		employeeGI.setImmediateSupervisor(employeeGovInfo.getImmediateSupervisor());
		employeeGI.setStatus(employeeGovInfo.getStatus());
		employeeGI.setPosition(employeeGovInfo.getPosition());

		employeeGI.setSSSNumber(employeeGovInfo.getSSSNumber());
		employeeGI.setPhilHealthNumber(employeeGovInfo.getPhilHealthNumber());
		employeeGI.setPagibigNumber(employeeGovInfo.getPagibigNumber());
		employeeGI.setTinNumber(employeeGovInfo.getTinNumber());

		employeeComp.setBasicSalary(employeeCompInfo.getBasicSalary());
		employeeComp.setClothingAllowance(employeeCompInfo.getClothingAllowance());
		employeeComp.setGrossSemiMonthlyRate(employeeCompInfo.getGrossSemiMonthlyRate());
		employeeComp.setPhoneAllowance(employeeCompInfo.getPhoneAllowance());
		employeeComp.setRiceSubsidy(employeeCompInfo.getRiceSubsidy());
		employeeComp.setHourlyRate(employeeCompInfo.getHourlyRate());
	}
}