package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.CourseDTO;
import org.springframework.data.domain.Page;

public interface CourseService {

    Page<CourseDTO> listCourses(String syllabusId, int page, int size);

    CourseDTO saveCourse(CourseDTO course);
}
