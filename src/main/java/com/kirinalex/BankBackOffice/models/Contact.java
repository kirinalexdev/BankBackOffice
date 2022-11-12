package com.kirinalex.BankBackOffice.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.kirinalex.BankBackOffice.enums.ContactType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor // нужно, иначе ругается на @Builder  TODO написать какая именно ошибка
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Contact {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value", length = 100, nullable = false)
    @Size(min = 5, max = 100)
    @NotNull
    private String value;

    // TODO обязательно посмотреть https://www.youtube.com/watch?v=uYcPB4D-5G4&lc=Ugz6IkqMd3NJkOhRG5N4AaABAg&ab_channel=KirillChokparov-JavaTutorRU
    // TODO как создавать? https://ru.stackoverflow.com/questions/1466064/%d0%9a%d0%b0%d0%ba-%d0%be%d0%b4%d0%bd%d0%be%d0%b9-%d0%be%d0%bf%d0%b5%d1%80%d0%b0%d1%86%d0%b8%d0%b5%d0%b9-%d1%81%d0%be%d0%b7%d0%b4%d0%b0%d1%82%d1%8c-%d0%b7%d0%b0%d0%bf%d0%b8%d1%81%d0%b8-%d1%81%d0%b2%d1%8f%d0%b7%d0%b0%d0%bd%d0%bd%d1%8b%d1%85-%d1%82%d0%b0%d0%b1%d0%bb%d0%b8%d1%86-onetomany
    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable=false)
    @NotNull
    private Employee employee;

    @Column(name = "type", length = 10, nullable=false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ContactType type;

}
