package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.CourseDTO;
import com.perficient.udea.enrollment.services.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Course", description = "Course management API operations")
@RestController
@RequiredArgsConstructor
public class CourseController {

    public static final String COURSE_PATH = "/api/v1/syllabus/{syllabusId}/courses";

    private final CourseService courseService;

    @Operation(summary = "List all available courses by syllabus")
    @GetMapping(value = COURSE_PATH)
    public Page<CourseDTO> listCourses(@PathVariable String syllabusId, @RequestParam int page, @RequestParam int size) {
        return courseService.listCourses(syllabusId, page, size);
    }

    @Operation(summary = "Save a course with its corresponding subjects")
    @PostMapping(value = COURSE_PATH)
    public ResponseEntity<HttpHeaders> saveCourse(@Validated @RequestBody CourseDTO course) {

        CourseDTO savedCourse = courseService.saveCourse(course);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", COURSE_PATH + "/" + savedCourse.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
