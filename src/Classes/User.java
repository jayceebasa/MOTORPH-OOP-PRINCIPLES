// Filename: User.java
package Classes;

import UtilityClasses.JsonFileHandler;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import com.google.gson.annotations.SerializedName;

/**
 * Abstract base class for user management
 */
public abstract class User implements BaseUser {
	@SerializedName("employeeNum")
	private String employeeNumber;
	private String userId;
	private String password;
	private Boolean isVerified = false;
	private Date dateRegistered;
	private Boolean loginStatus = false;
	private Boolean isAdmin = false;

	/**
	 * Constructor for user with credentials
	 * @param userId User's unique identifier
	 * @param password User's password
	 * @throws IOException If authentication fails
	 */
	public User(String userId, String password) throws IOException {
		setUserId(userId);
		setPassword(password);
		this.dateRegistered = new Date();

		if (!userId.isEmpty() && !password.isEmpty()) {
			authenticateLogin();
		}
	}

	/**
	 * Constructor for user with employee number
	 * @param employeeNumber Unique employee identifier
	 */
	public User(String employeeNumber) {
		this.employeeNumber = Objects.requireNonNull(employeeNumber, "Employee number cannot be null");
	}

	/**
	 * Get employee number
	 * @return Employee number
	 */
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	/**
	 * Set employee number
	 * @param employeeNumber Employee number to set
	 */
	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = Objects.requireNonNull(employeeNumber, "Employee number cannot be null");
	}

	/**
	 * Get user ID
	 * @return User ID
	 */
	@Override
	public String getUserId() {
		return userId;
	}

	/**
	 * Set user ID with validation
	 * @param userId User ID to set
	 */
	@Override
	public void setUserId(String userId) {
		if (userId == null || userId.trim().isEmpty()) {
			throw new IllegalArgumentException("User ID cannot be null or empty");
		}
		this.userId = userId.trim();
	}

	/**
	 * Get password
	 * @return Password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set password with basic validation
	 * @param password Password to set
	 */
	public void setPassword(String password) {
		if (password == null || password.length() < 4) {
			throw new IllegalArgumentException("Password must be at least 4 characters long");
		}
		this.password = password;
	}

	/**
	 * Get verification status
	 * @return Verification status
	 */
	@Override
	public Boolean getIsVerified() {
		return isVerified;
	}

	/**
	 * Set verification status
	 * @param isVerified Verification status
	 */
	@Override
	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	/**
	 * Get registration date
	 * @return Registration date
	 */
	public Date getDateRegistered() {
		return dateRegistered;
	}

	/**
	 * Set registration date
	 * @param dateRegistered Registration date
	 */
	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	/**
	 * Get login status
	 * @return Login status
	 */
	public Boolean getLoginStatus() {
		return loginStatus;
	}

	/**
	 * Set login status
	 * @param loginStatus Login status
	 */
	public void setLoginStatus(Boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	/**
	 * Check if user is an admin
	 * @return Admin status
	 */
	public Boolean getIsAdmin() {
		return isAdmin;
	}

	/**
	 * Set admin status
	 * @param isAdmin Admin status
	 */
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	/**
	 * Authenticate login credentials
	 * @throws IOException If authentication fails
	 */
	private void authenticateLogin() throws IOException {
		if (!userId.equals("admin")) {
			setEmployeeNumber(JsonFileHandler.nameIterator(
					JsonFileHandler.getLoginCredentialsJSON(),
					"username",
					userId,
					"employeeNum"
			));
			setLoginStatus(JsonFileHandler.compareLoginCredentials(
					JsonFileHandler.getLoginCredentialsJSON(),
					"username",
					userId,
					"password",
					password
			));
			return;
		}
		setLoginStatus(authenticateAdminLogin(userId, password));
	}

	/**
	 * Authenticate admin login
	 * @param userId User ID
	 * @param password Password
	 * @return Authentication result
	 */
	private Boolean authenticateAdminLogin(String userId, String password) {
		if (userId.equals("admin") && password.equals("12345")) {
			setIsAdmin(true);
			return true;
		}
		return false;
	}
}