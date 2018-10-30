package com.example.employee;

import com.example.employee.dao.EmployeeRepository;
import com.example.employee.domain.Employee;
import com.example.employee.exception.InvalidUuidException;
import com.example.employee.exception.UuidPopulatedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

public class EmployeeService {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {

        return employeeRepository.findAll();
    }

    public List<Employee> findByCompany(String company) {

        return employeeRepository.findByCompany(company);
    }

    public Employee findById(String id) {

        return employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid id"));
    }

    public Employee insert(Employee employee) {

        if (employee.getId() != null) {
            throw new UuidPopulatedException();
        }

        employee.setId(UUID.randomUUID());

        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee) {

        if (!employeeRepository.existsById(employee.getId().toString())) {
            throw new InvalidUuidException();
        }

        return employeeRepository.save(employee);
    }
}
