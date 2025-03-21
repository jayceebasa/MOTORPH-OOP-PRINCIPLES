package Classes;

import java.io.IOException;
import com.google.gson.annotations.SerializedName;

public class GovernmentIdentification extends EmployeeInformation {
	@SerializedName("SSS")
	private String sssNumber;

	@SerializedName("Philhealth")
	private String philHealthNumber;

	@SerializedName("Pag-ibig")
	private String pagibigNumber;

	@SerializedName("TIN")
	private String tinNumber;

	public GovernmentIdentification(String userId, String password) throws IOException {
		super(userId, password);
	}

	public GovernmentIdentification(String employeeNumber) {
		super(employeeNumber);
	}

	public String getSSSNumber() {
		return sssNumber;
	}

	public void setSSSNumber(String sssNumber) {
		this.sssNumber = sssNumber;
	}

	public String getPhilHealthNumber() {
		return philHealthNumber;
	}

	public void setPhilHealthNumber(String philHealthNumber) {
		this.philHealthNumber = philHealthNumber;
	}

	public String getPagibigNumber() {
		return pagibigNumber;
	}

	public void setPagibigNumber(String pagibigNumber) {
		this.pagibigNumber = pagibigNumber;
	}

	public String getTinNumber() {
		return tinNumber;
	}

	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}
}