package com.example.employee;

import com.example.employee.dao.EmployeeRepository;
import com.example.employee.domain.Employee;
import com.example.employee.exception.InvalidUuidException;
import com.example.employee.exception.UuidPopulatedException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private List<Employee> redHatEmployees;

    @Mock
    private List<Employee> googleEmployees;

    @Mock
    private List<Employee> allEmployees;

    private EmployeeService employeeService;

    private UUID testUuid = UUID.randomUUID();

    @Before
    public void setUp() {

        given(employeeRepository.findAll()).willReturn(allEmployees);
        given(employeeRepository.findByCompany("Red Hat")).willReturn(redHatEmployees);
        given(employeeRepository.findByCompany("Google")).willReturn(googleEmployees);

        Optional<Employee> employeeOptional = Optional.of(new Employee(testUuid, "Jon", "Doe", "Google"));

        given(employeeRepository.findById(anyString())).willReturn(employeeOptional);

        employeeService = new EmployeeService(employeeRepository);

    }

    @Test
    public void testFindAll() {

        List<Employee> employees = employeeService.findAll();

        assertThat(employees).isEqualTo(allEmployees);
    }

    @Test
    public void testFindByCompany() {

        List<Employee> employees = employeeService.findByCompany("Red Hat");
        assertThat(employees).isEqualTo(redHatEmployees);

        employees = employeeService.findByCompany("Google");
        assertThat(employees).isEqualTo(googleEmployees);

    }

    @Test
    public void testFindById() {
        Employee employee = employeeService.findById("test");

        assertThat(employee.getId()).isEqualTo(testUuid);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindById_GivenInvalidId_ShouldReturnException() {

        given(employeeRepository.findById(anyString())).willReturn(Optional.empty());

        employeeService.findById("test");

    }

    @Test
    public void testInsert() {

        Employee testEmployee = Employee.builder()
                .firstName("Jon")
                .lastName("Doe")
                .company("Google")
                .build();

        given(employeeRepository.save(any(Employee.class))).willReturn(testEmployee);

        Employee employee = employeeService.insert(testEmployee);

        verify(employeeRepository).save(testEmployee);

        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isNotNull();

    }

    @Test(expected = UuidPopulatedException.class)
    public void testInsert_GivenUuidPopulated_ShouldReturnException() {

        employeeService.insert(Employee.builder().id(UUID.randomUUID()).build());

    }

    @Test
    public void testUpdate() {

        Employee testEmployee = Employee.builder()
                .id(UUID.randomUUID())
                .firstName("Jon")
                .lastName("Doe")
                .company("Google")
                .build();

        given(employeeRepository.existsById(testEmployee.getId().toString())).willReturn(true);
        given(employeeRepository.save(any(Employee.class))).willReturn(testEmployee);

        Employee employee = employeeService.update(testEmployee);

        verify(employeeRepository).save(testEmployee);

        assertThat(employee).isNotNull();
        assertThat(employee.getId()).isNotNull();
    }

    @Test(expected = InvalidUuidException.class)
    public void testUpdate_GivenInvalidUuid_ShouldReturnException() {

        Employee testEmployee = Employee.builder()
                .id(UUID.randomUUID())
                .firstName("Jon")
                .lastName("Doe")
                .company("Google")
                .build();

        given(employeeRepository.existsById(testEmployee.getId().toString())).willReturn(false);
        employeeService.update(testEmployee);

    }

}
