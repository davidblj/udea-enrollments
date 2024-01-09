package com.perficient.udea.enrollment.mappers;

import com.perficient.udea.enrollment.entities.Course;
import com.perficient.udea.enrollment.model.CourseDTO;
import org.mapstruct.Mapper;

//@Mapper(componentModel = "spring")
@Mapper()
public interface CourseMapper {

    Course courseDtoToCourse(CourseDTO dto);
    CourseDTO courseToCourseDTO(Course course);
}
