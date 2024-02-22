package com.perficient.udea.enrollment.application.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CourseDTO {

    private UUID id;

    @NotBlank
    private String courseName;

    @NotBlank
    private String scienceField;

    @NotBlank
    @org.hibernate.validator.constraints.UUID(version = 1)
    private String syllabusId;

    @NotNull
    @Min(value = 1)
    @Max(value = 10)
    private int semester;

    private List<CoursePrerequisiteDTO> coursePrerequisites;

    @NotEmpty
    private List<SubjectDTO> subjects;
}
