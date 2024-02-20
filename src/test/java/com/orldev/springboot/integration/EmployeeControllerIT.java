package com.orldev.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orldev.springboot.model.Employee;
import com.orldev.springboot.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setup(){
        employee = Employee.builder()
                .firstName("orlando")
                .lastName("cruz")
                .email("orlandocruz999@gmail.com")
                .build();
    }

    // JUnit test for save Employee post request
    @DisplayName("JUnit test for save Employee of post request")
    @Test
    @Rollback(value = true)
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    // JUnit test for get all Employees of get request
    @DisplayName("JUnit test for get all Employees of get request")
    @Test
    @Rollback(value = true)
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Maria")
                .lastName("Sousa")
                .email("mariasousa@gmail.com")
                .build();

        List<Employee> employeeList = List.of(employee, employee1);

        employeeRepository.saveAll(employeeList);

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }


    //Positive scenario
    // JUnit positive test for get Employee by id get request
    @DisplayName("JUnit test for get Employee by id of get request")
    @Test
    @Rollback(value = true)
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Employee savedEmployee = employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", savedEmployee.getId()));

        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Negative scenario
    // JUnit negative test for get Employee by id get request
    @DisplayName("JUnit test for get Employee by id of get request")
    @Test
    @Rollback(value = true)
    public void givenEmployeeId_whenGetEmployeeById_thenReturnENotFound() throws Exception {
        //given - precondition or setup
        Long id = 22L;

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", id));

        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    //Positive scenario
    // JUnit test for update Employee put request
    @DisplayName("JUnit test for update Employee put request")
    @Test
    @Rollback(value = true)
    public void givenEmployeeIdAndEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup

        Employee savedEmployee = employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Maria")
                .lastName("Sousa")
                .email("mariasousa@gmail.com")
                .build();

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));


        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmployee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmployee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmployee.getEmail())));
    }


    //Positive scenario
    // JUnit test for update Employee put request
    @DisplayName("JUnit test for update Employee put request")
    @Test
    @Rollback(value = true)
    public void givenEmployeeIdAndEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Long id = 3L;

        Employee savedEmployee = employeeRepository.save(employee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Maria")
                .lastName("Sousa")
                .email("mariasousa@gmail.com")
                .build();

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));


        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    // JUnit test for delete Employee delete request
    @DisplayName("JUnit test for delete Employee delete request")
    @Test
    @Rollback(value = true)
    public void givenEmployeeId_whenDeleteEmployee_thenReturnString() throws Exception {
        //given - precondition or setup

        Employee savedEmployee = employeeRepository.save(employee);

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", savedEmployee.getId()));


        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
