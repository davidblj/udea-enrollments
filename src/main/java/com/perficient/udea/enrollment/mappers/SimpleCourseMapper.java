package com.perficient.udea.enrollment.mappers;

import com.perficient.udea.enrollment.DTOs.SimpleCourseDTO;
import com.perficient.udea.enrollment.entities.Course;
import org.mapstruct.Mapper;

@Mapper()
public interface SimpleCourseMapper {

    SimpleCourseDTO courseToSimpleCourseDTO(Course course);
}
