package ics.course.java.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ EmployeeTest.class, HourlyEmployeeTest.class })
public class AllTests {

}
