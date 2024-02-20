package com.orldev.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orldev.springboot.model.Employee;
import com.orldev.springboot.repository.EmployeeRepository;
import com.orldev.springboot.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@WebMvcTest
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("orlando")
                .lastName("cruz")
                .email("orlandocruz999@gmail.com")
                .build();
    }

    // JUnit test for save Employee post request
    @DisplayName("JUnit test for save Employee of post request")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup

        BDDMockito.given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(0)));

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
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Maria")
                .lastName("Sousa")
                .email("mariasousa@gmail.com")
                .build();

        List<Employee> employeeList = List.of(employee, employee1);

        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employeeList);

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(employeeList.size())));
    }


    //Positive scenario
    // JUnit test for get Employee by id get request
    @DisplayName("JUnit test for get Employee by id of get request")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given - precondition or setup
        Long id = 2L;

        BDDMockito.given(employeeService.getEmployeeById(id))
                .willReturn(Optional.of(employee));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", id));

        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Negative scenario
    // JUnit test for get Employee by id get request
    @DisplayName("JUnit test for get Employee by id of get request")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnENotFound() throws Exception {
        //given - precondition or setup
        Long id = 2L;

        BDDMockito.given(employeeService.getEmployeeById(id))
                .willReturn(Optional.empty());

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
    public void givenEmployeeIdAndEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given - precondition or setup
        Long id = 1L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Maria")
                .lastName("Sousa")
                .email("mariasousa@gmail.com")
                .build();


        BDDMockito.given(employeeService.updateEmployee(anyLong(), any(Employee.class)))
                .willReturn(Optional.of(updatedEmployee));

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", id)
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
    public void givenEmployeeIdAndEmployee_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        //given - precondition or setup
        Long id = 3L;

        Employee updatedEmployee = Employee.builder()
                .firstName("Maria")
                .lastName("Sousa")
                .email("mariasousa@gmail.com")
                .build();


        BDDMockito.given(employeeService.updateEmployee(anyLong(), any(Employee.class)))
                .willReturn(Optional.empty());

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
    public void givenEmployeeId_whenDeleteEmployee_thenReturnString() throws Exception {
        //given - precondition or setup
        Long id = 1L;

        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(id);

        //when - action ir the behaviour we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", id));


        //then  - verify the output
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


}
