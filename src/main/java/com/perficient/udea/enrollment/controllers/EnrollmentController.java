package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.DTOs.SubscriptionDTO;
import com.perficient.udea.enrollment.services.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {

        public static final String ENROLLMENT_PATH = "/api/v1/enrollment";
        public static final String COURSE_ENROLLMENT_PATH = ENROLLMENT_PATH + "/{studentId}/courses";
        public static final String COURSE_SUBSCRIPTION_PATH = ENROLLMENT_PATH + "/{studentId}/subscription";

        private final EnrollmentService enrollmentService;

        @GetMapping({COURSE_ENROLLMENT_PATH})
        public RegistrationSpotsDTO listAvailableCoursesToEnroll(@PathVariable("studentId") String studentId) {
            return enrollmentService.getEnrollmentInformation(studentId);
        }

        @Transactional(rollbackFor = Exception.class)
        @PostMapping({COURSE_SUBSCRIPTION_PATH})
        public ResponseEntity<HttpHeaders> subscribe(@PathVariable("studentId") String studentId, @Validated @RequestBody SubscriptionDTO subscriptionDTO) {
                enrollmentService.subscribeStudent(subscriptionDTO);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
}
