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
    private String syllabusId;
    private int semester;
    private List<CoursePrerequisiteDTO> coursePrerequisites;
    private List<SubjectDTO> subjects;
}
