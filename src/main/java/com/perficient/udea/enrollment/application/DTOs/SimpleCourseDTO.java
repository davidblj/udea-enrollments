package com.perficient.udea.enrollment.application.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SimpleCourseDTO {

    private UUID id;
    private String courseName;
    private String scienceField;
}
