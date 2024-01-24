package com.perficient.udea.enrollment.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CourseDTO {

    private UUID id;
    private String courseName;
    private String scienceField;
    private String pensumId;
//    private PensumDTO pensum;
    private List<CoursePrerequisiteDTO> coursePrerequisites;
}
