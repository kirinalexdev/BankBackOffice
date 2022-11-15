//package com.kirinalex.BankBackOffice.dto;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import com.kirinalex.BankBackOffice.enums.ContactType;
//import com.kirinalex.BankBackOffice.models.Employee;
//import lombok.*;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class ContactDTO {
//
//    private Integer id;
//
//    @Size(min = 5, max = 100)
//    @NotNull
//    private String value;
//
//    @Enumerated(EnumType.STRING)
//    @NotNull
//    private ContactType type;
//}
