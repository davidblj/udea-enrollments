package com.perficient.udea.enrollment.mappers;

import com.perficient.udea.enrollment.entities.Course;
import com.perficient.udea.enrollment.entities.CoursePrerequisiteId;
import com.perficient.udea.enrollment.DTOs.CourseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

//@Mapper(componentModel = "spring")
@Mapper()
public interface CourseMapper {

    @Mapping( target = "pensum.id", source = "pensumId" )
    Course courseDtoToCourse(CourseDTO courseDto);
    CourseDTO courseToCourseDTO(Course course);

    default CoursePrerequisiteId map(String value) {
        Course prerequisite = Course.builder().id(UUID.fromString(value)).build();
        return CoursePrerequisiteId.builder().coursePrerequisite(prerequisite).build();
    }

    default String map(CoursePrerequisiteId value) {
        return value.getCourse().getId().toString();
    }
}
