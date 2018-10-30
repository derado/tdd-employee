package com.example.employee;

import com.example.employee.domain.Employee;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeTest {

    @Test
    public void testCreateEmployee() {

        UUID testUuid = UUID.randomUUID();

        Employee employee = new Employee(testUuid, "Jon", "Doe", "Red Hat");

        assertThat(employee.getId()).isEqualTo(testUuid);
        assertThat(employee.getFirstName()).isEqualTo("Jon");
        assertThat(employee.getLastName()).isEqualTo("Doe");
        assertThat(employee.getCompany()).isEqualTo("Red Hat");

    }

}
