//package com.kirinalex.BankBackOffice.dto;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import com.kirinalex.BankBackOffice.models.Contact;
//import lombok.*;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class EmployeeDTO {
//
//    private int id;
//
//    @Size(min = 2, max = 100)
//    @NotNull
//    private String firstName;
//
//    @Size(min = 2, max = 100)
//    @NotNull
//    private String lastName;
//
//    private List<Contact> contacts = new ArrayList<>();
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    @NotNull
//    private Date birthday;
//}
