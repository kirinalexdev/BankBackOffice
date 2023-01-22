package com.kirinalex.BankBackOffice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kirinalex.BankBackOffice.dto.EmployeeDTO;
import com.kirinalex.BankBackOffice.models.Employee;
import com.kirinalex.BankBackOffice.services.EmployeeService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@ActiveProfiles("test")                   // отключаем security
@AutoConfigureMockMvc(addFilters = false) //
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @SpyBean
    private ModelMapper employeeModelMapper;

    @SneakyThrows
    @Test
    public void givenEmployeeDTO_whenCreateEmployee_thenReturnCreated(){
        // given
        var employeeDTO = employeeDTO();

        // when
        var response = mockMvc.perform(post("/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", matchesPattern("\\/v1\\/employee\\/\\d+")));
    }

    @SneakyThrows
    @Test
    public void givenIncorrectEmployeeDTO_whenCreateEmployee_thenReturnBadRequest(){
        // given
        var employeeDTO = incorrectEmployeeDTO();

        // when
        var response = mockMvc.perform(post("/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("firstName")))
                .andExpect(jsonPath("$.message", containsString("lastName")));
    }

    @SneakyThrows
    @Test
    public void givenEmployeeDTO_whenUpdateEmployee_thenReturnOk(){
        // given
        var employeeDTO = employeeDTO();
        given(employeeService.findById(anyInt()))
                .willReturn(Optional.of(new Employee()));

        // when
        var response = mockMvc.perform(put("/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        // then
        response.andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void givenEmployeeDTO_whenUpdateNotExistEmployee_thenReturnNotFound(){
        // given
        var employeeDTO = employeeDTO();
        given(employeeService.findById(anyInt()))
                .willReturn(Optional.empty());

        // when
        var response = mockMvc.perform(put("/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        // then
        response.andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void givenIncorrectEmployeeDTO_whenUpdateEmployee_thenReturnBadRequest(){
        // given
        var employeeDTO = incorrectEmployeeDTO();

        // when
        var response = mockMvc.perform(put("/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeDTO)));

        // then
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", containsString("firstName")))
                .andExpect(jsonPath("$.message", containsString("lastName")));
    }

    @SneakyThrows
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnOk(){
        // given
        given(employeeService.findById(anyInt()))
                .willReturn(Optional.of(new Employee()));

        // when
        var response = mockMvc.perform(
                    delete("/v1/employee")
                            .param("id", "2"));

        // then
        response.andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    public void givenEmployeeId_whenDeleteNotExistEmployee_thenReturnNotFound(){
        // given
        given(employeeService.findById(anyInt()))
                .willReturn(Optional.empty());

        // when
        var response = mockMvc.perform(
                    delete("/v1/employee")
                            .param("id", "2"));

        // then
        response.andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void givenEmployeeId_whenFindEmployee_thenReturnEmployeeDTO(){
        // given
        var id = 1;
        var firstName = "fn";
        var lastName = "ln";
        var birthday = LocalDate.of(2000,3,4);

        var employee = Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .build();

        var employeeDTO = EmployeeDTO.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .birthday(birthday)
                .build();

        given(employeeService.findById(employeeDTO.getId()))
                .willReturn(Optional.of(employee));

        // when
        var response = mockMvc.perform(
                get("/v1/employee")
                        .param("id", String.valueOf(employee.getId())));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(employeeDTO.getId())))
                .andExpect(jsonPath("$.firstName", is(employeeDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeDTO.getLastName())))
                .andExpect(jsonPath("$.birthday", is(String.valueOf(employeeDTO.getBirthday()))));
    }

    @SneakyThrows
    @Test
    public void givenEmployeeId_whenFindNotExistEmployee_thenReturnNotFound(){
        // given
        var employeeId = 2;

        given(employeeService.findById(employeeId))
                .willReturn(Optional.empty());

        // when
        var response = mockMvc.perform(
                get("/v1/employee")
                        .param("id", String.valueOf(employeeId)));

        // then
        response.andExpect(status().isNotFound());
    }

    private EmployeeDTO employeeDTO(){
        return EmployeeDTO.builder()
                .firstName("first name 1")
                .lastName("last name 1")
                .birthday(LocalDate.of(2000, 3, 4))
                .build();
    }

    private EmployeeDTO incorrectEmployeeDTO() {
        var anyString = "q";
        return EmployeeDTO.builder()
                .firstName("f") // слишком короткое
                .lastName(String.format("%1$101s", anyString)) // слишком длинное
                .birthday(LocalDate.of(2000, 3, 4))
                .build();
    }
}