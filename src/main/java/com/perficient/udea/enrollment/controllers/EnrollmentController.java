package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

        public static final String ENROLLMENT_PATH = "/api/v1/enrollment";
        public static final String ENROLLMENT_COURSE_PATH_ID = ENROLLMENT_PATH + "/{studentId}/courses";

        private final EnrollmentService enrollmentService;

        @GetMapping({ENROLLMENT_COURSE_PATH_ID})
        public RegistrationSpotsDTO listAvailableCoursesToEnroll(@PathVariable("studentId") String studentId) {
            return enrollmentService.getEnrollmentInformation(studentId);
        }
}
