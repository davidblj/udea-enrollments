package com.perficient.udea.enrollment.application.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PersonDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String fullName;

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
}
