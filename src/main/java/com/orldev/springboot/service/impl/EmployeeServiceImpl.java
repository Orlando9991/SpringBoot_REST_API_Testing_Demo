package com.orldev.springboot.service.impl;

import com.orldev.springboot.exception.ResourceNotFoundException;
import com.orldev.springboot.model.Employee;
import com.orldev.springboot.repository.EmployeeRepository;
import com.orldev.springboot.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    @Override
    public Employee saveEmployee(Employee employee) {

        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()){
            throw new ResourceNotFoundException("Employee already exist with the given email:" + employee.getEmail());
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Optional<Employee> updateEmployee(Long id, Employee employee) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(!optionalEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee not found for the id: " + id);
        }
        return Optional.of(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
