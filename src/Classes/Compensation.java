package Classes;

import java.io.IOException;
import com.google.gson.annotations.SerializedName;

public class Compensation extends EmployeeInformation {
	private String compensationMonth;

	@SerializedName("basic_salary")
	private double basicSalary;

	@SerializedName("rice_subsidy")
	private double riceSubsidy;

	@SerializedName("phone_allowance")
	private double phoneAllowance;

	@SerializedName("clothing_allowance")
	private double clothingAllowance;

	@SerializedName("gross_semi-monthly_rate")
	private double grossSemiMonthlyRate;

	private double netSalary;

	public Compensation(String userId, String password) throws IOException {
		super(userId, password);
	}

	public Compensation(String employeeNumber) {
		super(employeeNumber);
	}

	public double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(double basicSalary) {
		this.basicSalary = basicSalary;
	}

	public double getRiceSubsidy() {
		return riceSubsidy;
	}

	public void setRiceSubsidy(double riceSubsidy) {
		this.riceSubsidy = riceSubsidy;
	}

	public double getPhoneAllowance() {
		return phoneAllowance;
	}

	public void setPhoneAllowance(double phoneAllowance) {
		this.phoneAllowance = phoneAllowance;
	}

	public double getClothingAllowance() {
		return clothingAllowance;
	}

	public void setClothingAllowance(double clothingAllowance) {
		this.clothingAllowance = clothingAllowance;
	}

	public double getGrossSemiMonthlyRate() {
		return grossSemiMonthlyRate;
	}

	public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) {
		this.grossSemiMonthlyRate = grossSemiMonthlyRate;
	}

	public double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(double netSalary) {
		this.netSalary = netSalary;
	}

	public double calculateGrossSalary(double hourlyRate, double hoursRendered) {
		return hourlyRate * hoursRendered;
	}
}