package com.perficient.udea.enrollment.DTOs;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class StudentDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String fullName;

    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotNull
    @Min(value = 1)
    @Max(value = 6)
    private int stratum;

    @UUID(version = 1)
    private String syllabusId;
}
