package com.perficient.udea.enrollment.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectDTO {
    private String name;
    private float weight;
}
