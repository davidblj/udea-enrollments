package com.perficient.udea.enrollment.application.mappers;

import com.perficient.udea.enrollment.application.DTOs.SimpleCourseDTO;
import com.perficient.udea.enrollment.persistence.entities.Course;
import org.mapstruct.Mapper;

@Mapper()
public interface SimpleCourseMapper {

    SimpleCourseDTO courseToSimpleCourseDTO(Course course);
}
