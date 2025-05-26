package ics.course.java;

public class HourlyEmployee extends Employee {
	private int hours;
	private double hourlySalary;
	
	public HourlyEmployee(String name, String address, String phone, int hours, double hourlySalary) {
		super(name, address, phone);
		this.hours = hours;
		this.hourlySalary = hourlySalary;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public double getHourlySalary() {
		return hourlySalary;
	}

	public void setHourlySalary(double hourlySalary) {
		this.hourlySalary = hourlySalary;
	}
	
	

}
