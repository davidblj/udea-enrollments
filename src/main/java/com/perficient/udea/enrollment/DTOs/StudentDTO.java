package com.perficient.udea.enrollment.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

// TODO:
@Data
@Builder
public class StudentDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String fullName;

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;
}
