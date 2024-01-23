package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.model.CourseDTO;

import java.util.List;

public interface CourseService {

    List<CourseDTO> listCourses();

    CourseDTO saveCourse(CourseDTO course);
}
