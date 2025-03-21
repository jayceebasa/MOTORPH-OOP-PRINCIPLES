// Filename: GovernmentIdentification.java
package Classes;

import java.io.IOException;
import java.util.regex.Pattern;
import com.google.gson.annotations.SerializedName;

/**
 * Represents government identification details for an employee
 */
public class GovernmentIdentification extends EmployeeInformation {
	@SerializedName("SSS")
	private String sssNumber;

	@SerializedName("Philhealth")
	private String philHealthNumber;

	@SerializedName("Pag-ibig")
	private String pagibigNumber;

	@SerializedName("TIN")
	private String tinNumber;

	/**
	 * Constructor with user credentials
	 * @param userId User's unique identifier
	 * @param password User's password
	 * @throws IOException If authentication fails
	 */
	public GovernmentIdentification(String userId, String password) throws IOException {
		super(userId, password);
	}

	/**
	 * Constructor with employee number
	 * @param employeeNumber Unique employee identifier
	 */
	public GovernmentIdentification(String employeeNumber) {
		super(employeeNumber);
	}

	/**
	 * Get SSS number
	 * @return SSS number
	 */
	public String getSSSNumber() {
		return sssNumber;
	}

	/**
	 * Set SSS number with validation
	 * @param sssNumber SSS number to set
	 */
	public void setSSSNumber(String sssNumber) {
		if (sssNumber == null) {
			throw new IllegalArgumentException("Invalid SSS number format");
		}
		this.sssNumber = sssNumber;
	}

	/**
	 * Validate SSS number format
	 * @param sssNumber SSS number to validate
	 * @return True if valid, false otherwise
	 */
	private boolean isValidSSSNumber(String sssNumber) {
		// SSS number format: XX-XXXXXXX-X
		return Pattern.matches("\\d{2}-\\d{7}-\\d{1}", sssNumber);
	}

	/**
	 * Get PhilHealth number
	 * @return PhilHealth number
	 */
	public String getPhilHealthNumber() {
		return philHealthNumber;
	}

	/**
	 * Set PhilHealth number with validation
	 * @param philHealthNumber PhilHealth number to set
	 */
	public void setPhilHealthNumber(String philHealthNumber) {
		if (philHealthNumber == null) {
			throw new IllegalArgumentException("Invalid PhilHealth number format");
		}
		this.philHealthNumber = philHealthNumber;
	}

	/**
	 * Validate PhilHealth number format
	 * @param philHealthNumber PhilHealth number to validate
	 * @return True if valid, false otherwise
	 */
	private boolean isValidPhilHealthNumber(String philHealthNumber) {
		// PhilHealth number format: XX-XXXXXXXXX-X
		return Pattern.matches("\\d{2}-\\d{9}-\\d{1}", philHealthNumber);
	}

	/**
	 * Get Pag-ibig number
	 * @return Pag-ibig number
	 */
	public String getPagibigNumber() {
		return pagibigNumber;
	}

	/**
	 * Set Pag-ibig number with validation
	 * @param pagibigNumber Pag-ibig number to set
	 */
	public void setPagibigNumber(String pagibigNumber) {
		if (pagibigNumber == null) {
			throw new IllegalArgumentException("Invalid Pag-ibig number format");
		}
		this.pagibigNumber = pagibigNumber;
	}

	/**
	 * Validate Pag-ibig number format
	 * @param pagibigNumber Pag-ibig number to validate
	 * @return True if valid, false otherwise
	 */
	private boolean isValidPagibigNumber(String pagibigNumber) {
		// Pag-ibig number format: XXXX-XXXX-XXXX
		return Pattern.matches("\\d{4}-\\d{4}-\\d{4}", pagibigNumber);
	}

	/**
	 * Get TIN number
	 * @return TIN number
	 */
	public String getTinNumber() {
		return tinNumber;
	}

	/**
	 * Set TIN number with validation
	 * @param tinNumber TIN number to set
	 */
	public void setTinNumber(String tinNumber) {
		if (tinNumber == null) {
			throw new IllegalArgumentException("Invalid TIN number format");
		}
		this.tinNumber = tinNumber;
	}

	/**
	 * Validate TIN number format
	 * @param tinNumber TIN number to validate
	 * @return True if valid, false otherwise
	 */
	private boolean isValidTinNumber(String tinNumber) {
		// TIN number format: XXX-XXX-XXX-XXX
		return Pattern.matches("\\d{3}-\\d{3}-\\d{3}-\\d{3}", tinNumber);
	}
}