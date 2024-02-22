package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.dtos.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.application.domain.ClassRoomPool;
import com.perficient.udea.enrollment.application.domain.CoursePool;
import com.perficient.udea.enrollment.application.domain.EnrollmentRegistration;
import com.perficient.udea.enrollment.application.domain.EnrollmentValidator;
import com.perficient.udea.enrollment.persistence.entities.*;
import com.perficient.udea.enrollment.application.mappers.SimpleCourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final SimpleCourseMapper simpleCourseMapper;
    private final EnrollmentValidator enrollmentValidator;
    private final CoursePool coursePool;
    private final ClassRoomPool classRoomPool;
    private final EnrollmentRegistration enrollmentRegistration;

    public void subscribeStudent(SubscriptionDTO subscriptionDTO) {
        enrollmentValidator.validateSubscription(subscriptionDTO);
        classRoomPool.setNewEnrollments(subscriptionDTO);
        enrollmentRegistration.createTermEnrollment(subscriptionDTO);
    }

    public RegistrationSpotsDTO getEnrollmentInformation(String studentId) {
        enrollmentValidator.validateOpening(studentId);
        return buildDTOWithStudentIdAndAvailableCourses(studentId, coursePool.getAvailableCourses(studentId));
    }

    private RegistrationSpotsDTO buildDTOWithStudentIdAndAvailableCourses(String studentId, List<Course> availableCourses) {
        Long session = System.currentTimeMillis() + 15 * 60000;
        return RegistrationSpotsDTO.builder()
                .courseDTOList(availableCourses.stream().map(simpleCourseMapper::courseToSimpleCourseDTO).toList())
                .personId(studentId)
                .timestamp(session)
                .build();
    }
}
