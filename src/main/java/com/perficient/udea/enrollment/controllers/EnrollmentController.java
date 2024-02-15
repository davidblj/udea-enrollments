package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.DTOs.SubscriptionDTO;
import com.perficient.udea.enrollment.services.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Enrollment", description = "Enrollment operations API executed during the subscription process")
@RestController
@RequiredArgsConstructor
public class EnrollmentController {

        public static final String ENROLLMENT_PATH = "/api/v1/enrollment";
        public static final String COURSE_ENROLLMENT_PATH = ENROLLMENT_PATH + "/{studentId}/courses";
        public static final String COURSE_SUBSCRIPTION_PATH = ENROLLMENT_PATH + "/{studentId}/subscription";

        private final EnrollmentService enrollmentService;

        @Operation(
                summary = "Fetch an student available class rooms to enroll",
                description = "Fetch all the class rooms that are available at this point of time taking into account all completed and failed courses alongside it's current semester")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "The student is able to begin its subscription"),
                @ApiResponse(responseCode = "400", description = "The student is already enrolled and cannot subscribe", content = @Content(schema = @Schema(hidden = true)))
        })
        @GetMapping({COURSE_ENROLLMENT_PATH})
        public RegistrationSpotsDTO listAvailableCoursesToEnroll(@PathVariable("studentId") String studentId) {
            return enrollmentService.getEnrollmentInformation(studentId);
        }

        @Operation(
                summary = "Subscribe an student into the current term",
                description = "Subscribes an student into the current term validating an active session, checking if is not already subscribed for the current active term, with valid course ids and classrooms.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Operation successful; subscription completed"),
                @ApiResponse(responseCode = "400", description = "Operation not successful; an active enrollment exists, the course tray is non existent or invalid", content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(responseCode = "403", description = "Operation not permitted; the user session expired, try again", content = @Content(schema = @Schema(hidden = true))),
                @ApiResponse(responseCode = "409", description = "Operation not successful; the user tried to subscribe a class room where there are not available spots anymore", content = @Content(schema = @Schema(hidden = true)))
        })
        @Transactional(rollbackFor = Exception.class)
        @PostMapping({COURSE_SUBSCRIPTION_PATH})
        public ResponseEntity<HttpHeaders> subscribe(@PathVariable("studentId") String studentId, @Validated @RequestBody SubscriptionDTO subscriptionDTO) {
                enrollmentService.subscribeStudent(subscriptionDTO);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
}
