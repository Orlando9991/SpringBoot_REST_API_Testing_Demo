package com.orldev.springboot.service;

import com.orldev.springboot.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Optional<Employee> getEmployeeById(Long id);

    Optional<Employee> updateEmployee(Long id, Employee employee);

    void deleteEmployee(Long id);

}
