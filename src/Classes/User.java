package Classes;

import UtilityClasses.JsonFileHandler;
import java.io.IOException;
import java.util.Date;
import com.google.gson.annotations.SerializedName;

public abstract class User {
	@SerializedName("employeeNum")
	private String employeeNumber;
	private String userId;
	private String password;
	private Boolean isVerified = false;
	private Date dateRegistered;
	private Boolean loginStatus = false;
	private Boolean isAdmin = false;

	public User(String userId, String password) throws IOException {
		this.userId = userId;
		this.password = password;
		if (!userId.isEmpty() && !password.isEmpty()) {
			authenticateLogin();
		}
	}

	public User(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsVerified() {
		return isVerified;
	}

	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}

	public Date getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}

	public Boolean getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	private void authenticateLogin() throws IOException {
		if (!userId.equals("admin")) {
			setEmployeeNumber(JsonFileHandler.nameIterator(JsonFileHandler.getLoginCredentialsJSON(), "username", userId, "employeeNum"));
			setLoginStatus(JsonFileHandler.compareLoginCredentials(JsonFileHandler.getLoginCredentialsJSON(), "username", userId, "password", password));
			return;
		}
		setLoginStatus(authenticateAdminLogin(userId, password));
	}

	private Boolean authenticateAdminLogin(String userId, String password) {
		if (userId.equals("admin") && password.equals("123")) {
			setIsAdmin(true);
			return true;
		}
		return false;
	}
}