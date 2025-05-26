package ics.course.java.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ics.course.java.HourlyEmployee;

class HourlyEmployeeTest {
	String expectedName;
	String expectedAddress;
	String expectedPhone;
	int expectedHours;
	double expectedHourlySalary;
	HourlyEmployee e1;
	HourlyEmployee e2;

	@BeforeEach
	void setUp() throws Exception {
		expectedName = "Mats";
		expectedAddress = "Lund";
		expectedPhone = "12345";
		expectedHours = 10;
		expectedHourlySalary = 100;
		e1 = new HourlyEmployee(expectedName, expectedAddress, expectedPhone, expectedHours, expectedHourlySalary);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetHours() {
		assertEquals(expectedHours, e1.getHours());
	}

	@Test
	void testSetHours() {
		int expectedHours2 = 12;
		e1.setHours(expectedHours2);
		assertEquals(expectedHours2, e1.getHours());
	}

	@Test
	void testGetHourlySalary() {
		assertEquals(expectedHourlySalary, e1.getHourlySalary());
	}

	@Test
	void testSetHourlySalary() {
		double expectedHourlySalary2 = 120;
		e1.setHourlySalary(expectedHourlySalary2);
		assertEquals(expectedHourlySalary2, e1.getHourlySalary());
	}

}
