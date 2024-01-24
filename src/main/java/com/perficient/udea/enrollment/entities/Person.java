package com.perficient.udea.enrollment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @Id
    @NotNull
    private String id;

    private String fullName;

    @Email
    private String email;

    private String phoneNumber;
}
