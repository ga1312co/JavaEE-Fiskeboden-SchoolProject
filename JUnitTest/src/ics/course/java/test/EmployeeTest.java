package ics.course.java.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ics.course.java.Employee;

class EmployeeTest {
	String expectedName;
	String expectedAddress;
	String expectedPhone;
	Employee e1;
	Employee e2;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		expectedName = "Mats";
		expectedAddress = "Lund";
		expectedPhone = "12345";
		e1 = new Employee(expectedName, expectedAddress, expectedPhone);
		e2 = new Employee("Eva", "Malmö", "54321");
	}

	@AfterEach
	void tearDown() throws Exception {
		e1 = null;
		e2 = null;
	}

	@Test
	void testEmployee() {
		fail("Not yet implemented");
	}

	@Test
	void testGetName() {
		assertEquals(expectedName, e1.getName());
	}

	@Test
	void testSetName() {
		String expectedName2 = "Test";
		e1.setName(expectedName2);
		assertEquals(expectedName2, e1.getName());
	}

	@Test
	void testGetAddress() {
		assertNotNull(e1);
		assertEquals(expectedAddress, e1.getAddress());
	}

	@Test
	void testSetAddress() {
		String expectedAddress2 = "TestStad";
		e1.setAddress(expectedAddress2);
		assertEquals(expectedAddress2, e1.getAddress());
	}

	@Test
	void testGetPhone() {
		assertEquals(expectedPhone, e1.getPhone());
	}

	@Test
	void testSetPhone() {
		String expectedPhone2 = "54321";
		e1.setPhone(expectedPhone2);
		assertEquals(expectedPhone2, e1.getPhone());
	}

}
