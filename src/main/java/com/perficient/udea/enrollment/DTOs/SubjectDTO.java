package com.perficient.udea.enrollment.DTOs;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectDTO {

    @NotBlank
    private String name;

    @DecimalMin(value = "0.0", inclusive = false)
    @DecimalMax(value = "1.0")
    private float weight;
}
