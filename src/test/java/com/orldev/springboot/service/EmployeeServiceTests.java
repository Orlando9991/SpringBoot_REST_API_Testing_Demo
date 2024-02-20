package com.orldev.springboot.service;

import com.orldev.springboot.exception.ResourceNotFoundException;
import com.orldev.springboot.model.Employee;
import com.orldev.springboot.repository.EmployeeRepository;
import com.orldev.springboot.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks //InjectMocks needs a Class to instantiate not a Interface
    private EmployeeServiceImpl employeeService;

    private Employee employee;


    @BeforeEach
    public void setup(){
       //employeeRepository = Mockito.mock(EmployeeRepository.class);
       //employeeService = new EmployeeServiceImpl(employeeRepository);

        employee = Employee.builder()
                .id(1L)
                .firstName("Orlando")
                .lastName("Cruz")
                .email("orlandocruz999@gmail.com")
                .build();
    }

    // JUnit test for save employee method
    @DisplayName("JUnit test for save employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        //given - precondition or setup

        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        BDDMockito.given(employeeService.saveEmployee(employee))
                .willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        //when - action ir the behaviour we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);

        //then  - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for save employee method which throws exception
    @DisplayName("JUnit test for save employee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException(){

        //given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //when - action ir the behaviour we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class, ()-> {
            employeeService.saveEmployee(employee);
        });


        //then  - verify the output
        Mockito.verify(employeeRepository, Mockito.never()).save(employee);
    }

    // JUnit test for get all Employees method
    @DisplayName("JUnit test for get all Employees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Maria")
                .lastName("Silva")
                .email("mariasilva12@gmail.com")
                .build();

        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action ir the behaviour we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then  - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test for get all Employees method (negative)
    @DisplayName("JUnit test for get all Employees method (negative)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        //given - precondition or setup


        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action ir the behaviour we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then  - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    // JUnit test for get Employee by id
    @DisplayName("JUnit test for get Employee by id")
    @Test
    public void givenEmployeeID_whenGetEmployeeById_thenReturnEmployee(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));

        //when - action ir the behaviour we are going to test
        Optional<Employee> savedEmployee = employeeService.getEmployeeById(employee.getId());

        //then  - verify the output
        assertThat(savedEmployee.isPresent()).isTrue();
    }


    // JUnit test for update employee method
    @DisplayName("JUnit test for update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnEmployeeObject(){
        //given - precondition or setup
        BDDMockito.given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));

        BDDMockito.given(employeeService.saveEmployee(employee))
                .willReturn(employee);

        employee.setFirstName("Cruz");
        employee.setLastName("Silva");

        //when - action ir the behaviour we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(1L, employee).get();

        //then  - verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Cruz");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Silva");
    }

    // JUnit test for delete employee method
    @DisplayName("JUnit test for delete employee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing(){
        //given - precondition or setup
        long employeeId = 1L;

        BDDMockito.willDoNothing().given(employeeRepository).deleteById(1L);

        //when - action ir the behaviour we are going to test
        employeeService.deleteEmployee(employeeId);

        //then  - verify the output
        BDDMockito.verify(employeeRepository, Mockito.times(1)).deleteById(employeeId);
    }


}
