package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.dtos.CourseDTO;
import org.springframework.data.domain.Page;

public interface CourseService {

    Page<CourseDTO> listCourses(String syllabusId, int page, int size);

    CourseDTO saveCourse(CourseDTO course);
}
