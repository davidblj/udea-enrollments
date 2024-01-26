package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.CourseDTO;
import com.perficient.udea.enrollment.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    public static final String COURSE_PATH = "/api/v1/course";

    private final CourseService courseService;

    @GetMapping(value = COURSE_PATH)
    public List<CourseDTO> listCourses() {
        return courseService.listCourses();
    }

    // TODO: add validations on the body
    @PostMapping(value = COURSE_PATH)
    public ResponseEntity<HttpHeaders> saveCourse(@RequestBody CourseDTO course) {

        CourseDTO savedCourse = courseService.saveCourse(course);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", COURSE_PATH + "/" + savedCourse.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }
}
