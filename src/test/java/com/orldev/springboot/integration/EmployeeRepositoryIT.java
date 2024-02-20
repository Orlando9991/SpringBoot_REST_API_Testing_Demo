package com.orldev.springboot.integration;

import com.orldev.springboot.model.Employee;
import com.orldev.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //Uses in memory database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Uses mysql database
@Transactional
public class EmployeeRepositoryIT extends AbstractionContainerBaseTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Orlando")
                .lastName("Cruz")
                .email("orlandocruz999@gmail.com")
                .build();
    }


    // JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    @Rollback(value = true)
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee(){
        //given - precondition or setup

        //when - action ir the behaviour we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then  - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for get all employees operation
    @DisplayName("JUnit test for get all employees operation")
    @Test
    @Rollback(value = true)
    public void givenEmployeesList_whenFindAll_thenReturnEmployeesList(){
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Maria")
                .lastName("Silva")
                .email("mariasilva21@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        //when - action ir the behaviour we are going to test
        List<Employee> employees = employeeRepository.findAll();

        //then  - verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    // JUnit test for get Employee by id operation
    @DisplayName("JUnit test for get Employee by id operation")
    @Test
    @Rollback(value = true)
    public void givenEmployeeObject_whenFindByID_thenReturnEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        //then  - verify the output
        assertThat(employeeDB).isNotNull();
    }

    // JUnit test for get Employee by email operation
    @DisplayName("JUnit test for get Employee by email operation")
    @Test
    @Rollback(value = true)
    public void givenEmployeeObject_whenFindByEmail_thenReturnEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        //then  - verify the output
        assertThat(employeeDB).isNotNull();
    }

    // JUnit test for update Employee operation
    @DisplayName("JUnit test for update Employee operation")
    @Test
    @Rollback(value = true)
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();

        savedEmployee.setFirstName("Maria");
        savedEmployee.setLastName("silva");
        Employee updateEmployee = employeeRepository.save(savedEmployee);

        //then  - verify the output
        assertThat(updateEmployee.getFirstName()).isEqualTo("Maria");
        assertThat(updateEmployee.getLastName()).isEqualTo("silva");
    }

    // JUnit test for deleting Employee operation
    @DisplayName("JUnit test for deleting Employee operation")
    @Test
    @Rollback(value = true)
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        //then  - verify the output
        assertThat(employeeOptional).isEmpty();
    }

    // JUnit test for custom query using JPQL with index
    @DisplayName("JUnit test for custom query using JPQL with index")
    @Test
    @Rollback(value = true)
    public void givenFirstNameAndLastName_whenFindByJPQLIndex_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLIndex(employee.getFirstName(), employee.getLastName());

        //then  - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test
    @DisplayName("JUnit test for custom query using JPQL with named params")
    @Test
    @Rollback(value = true)
    public void givenFirstNameAndLastName_whenFindByJPQLNamed_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        Employee savedEmployee = employeeRepository.findByJPQLNamed(employee.getFirstName(), employee.getLastName());

        //then  - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using Native SQL with index params
    @DisplayName("JUnit test for custom query using Native SQL with index params")
    @Test
    @Rollback(value = true)
    public void givenFirstNameAndLastName_whenFindByNativeSQLIndex_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLIndex(employee.getFirstName(), employee.getLastName());

        //then  - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using Native SQL with named params
    @DisplayName("JUnit test for custom query using Native SQL with named params")
    @Test
    @Rollback(value = true)
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamed_thenReturnEmployeeObject(){
        //given - precondition or setup
        employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(employee.getFirstName(), employee.getLastName());

        //then  - verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
