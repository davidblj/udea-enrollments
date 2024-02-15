package com.perficient.udea.enrollment.application.mappers;

import com.perficient.udea.enrollment.application.DTOs.CourseDTO;
import com.perficient.udea.enrollment.persistence.entities.Course;
import com.perficient.udea.enrollment.persistence.entities.CoursePrerequisiteId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface CourseMapper {

    @Mapping( target = "syllabus.id", source = "syllabusId" )
    Course courseDtoToCourse(CourseDTO courseDto);

    @Mapping( target = "syllabusId", source = "syllabus.id" )
    CourseDTO courseToCourseDTO(Course course);

    default CoursePrerequisiteId map(String value) {
        Course prerequisite = Course.builder().id(UUID.fromString(value)).build();
        return CoursePrerequisiteId.builder().coursePrerequisite(prerequisite).build();
    }

    default String map(CoursePrerequisiteId value) {
        return value.getCourse().getId().toString();
    }
}
