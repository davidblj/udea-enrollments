package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.model.CourseDTO;
import com.perficient.udea.enrollment.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CourseController {

    public static final String COURSE_PATH = "/api/v1/course";

    private final CourseService courseService;

    @GetMapping(value = COURSE_PATH)
    public List<CourseDTO> listCourses() {
        return courseService.listCourses();
    }
}
