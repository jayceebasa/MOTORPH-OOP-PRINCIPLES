// Filename: Compensation.java
package Classes;

import java.io.IOException;
import com.google.gson.annotations.SerializedName;

/**
 * Represents an employee's compensation details
 */
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

	/**
	 * Constructor with user credentials
	 * @param userId User's unique identifier
	 * @param password User's password
	 * @throws IOException If authentication fails
	 */
	public Compensation(String userId, String password) throws IOException {
		super(userId, password);
	}

	/**
	 * Constructor with employee number
	 * @param employeeNumber Unique employee identifier
	 */
	public Compensation(String employeeNumber) {
		super(employeeNumber);
	}

	/**
	 * Get compensation month
	 * @return Compensation month
	 */
	public String getCompensationMonth() {
		return compensationMonth;
	}

	/**
	 * Set compensation month
	 * @param compensationMonth Compensation month to set
	 */
	public void setCompensationMonth(String compensationMonth) {
		this.compensationMonth = compensationMonth;
	}

	/**
	 * Get basic salary
	 * @return Basic salary
	 */
	public double getBasicSalary() {
		return basicSalary;
	}

	/**
	 * Set basic salary with validation
	 * @param basicSalary Basic salary to set
	 */
	public void setBasicSalary(double basicSalary) {
		if (basicSalary < 0) {
			throw new IllegalArgumentException("Basic salary cannot be negative");
		}
		this.basicSalary = basicSalary;
	}

	/**
	 * Get rice subsidy
	 * @return Rice subsidy
	 */
	public double getRiceSubsidy() {
		return riceSubsidy;
	}

	/**
	 * Set rice subsidy with validation
	 * @param riceSubsidy Rice subsidy to set
	 */
	public void setRiceSubsidy(double riceSubsidy) {
		if (riceSubsidy < 0) {
			throw new IllegalArgumentException("Rice subsidy cannot be negative");
		}
		this.riceSubsidy = riceSubsidy;
	}

	/**
	 * Get phone allowance
	 * @return Phone allowance
	 */
	public double getPhoneAllowance() {
		return phoneAllowance;
	}

	/**
	 * Set phone allowance with validation
	 * @param phoneAllowance Phone allowance to set
	 */
	public void setPhoneAllowance(double phoneAllowance) {
		if (phoneAllowance < 0) {
			throw new IllegalArgumentException("Phone allowance cannot be negative");
		}
		this.phoneAllowance = phoneAllowance;
	}

	/**
	 * Get clothing allowance
	 * @return Clothing allowance
	 */
	public double getClothingAllowance() {
		return clothingAllowance;
	}

	/**
	 * Set clothing allowance with validation
	 * @param clothingAllowance Clothing allowance to set
	 */
	public void setClothingAllowance(double clothingAllowance) {
		if (clothingAllowance < 0) {
			throw new IllegalArgumentException("Clothing allowance cannot be negative");
		}
		this.clothingAllowance = clothingAllowance;
	}

	/**
	 * Get gross semi-monthly rate
	 * @return Gross semi-monthly rate
	 */
	public double getGrossSemiMonthlyRate() {
		return grossSemiMonthlyRate;
	}

	/**
	 * Set gross semi-monthly rate with validation
	 * @param grossSemiMonthlyRate Gross semi-monthly rate to set
	 */
	public void setGrossSemiMonthlyRate(double grossSemiMonthlyRate) {
		if (grossSemiMonthlyRate < 0) {
			throw new IllegalArgumentException("Gross semi-monthly rate cannot be negative");
		}
		this.grossSemiMonthlyRate = grossSemiMonthlyRate;
	}

	/**
	 * Get net salary
	 * @return Net salary
	 */
	public double getNetSalary() {
		return netSalary;
	}

	/**
	 * Set net salary with validation
	 * @param netSalary Net salary to set
	 */
	public void setNetSalary(double netSalary) {
		if (netSalary < 0) {
			throw new IllegalArgumentException("Net salary cannot be negative");
		}
		this.netSalary = netSalary;
	}

	/**
	 * Calculate gross salary
	 * @param hourlyRate Hourly rate
	 * @param hoursRendered Hours worked
	 * @return Gross salary
	 */
	public double calculateGrossSalary(double hourlyRate, double hoursRendered) {
		if (hourlyRate < 0 || hoursRendered < 0) {
			throw new IllegalArgumentException("Hourly rate and hours rendered cannot be negative");
		}
		return hourlyRate * hoursRendered;
	}

	/**
	 * Calculate total compensation
	 * @return Total compensation
	 */
	public double calculateTotalCompensation() {
		return basicSalary + riceSubsidy + phoneAllowance + clothingAllowance;
	}
}