package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.mappers.CourseMapper;
import com.perficient.udea.enrollment.entities.Course;
import com.perficient.udea.enrollment.repositories.CourseGradesRepository;
import com.perficient.udea.enrollment.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

// TODO: review the layers application vs domain
@Service
@RequiredArgsConstructor
public class EnrollmentServiceImp implements EnrollmentService {

    private final CourseGradesRepository courseGradesRepository;

    private final CourseRepository courseRepository;

    private final CourseMapper courseMapper;

    @Override
    public RegistrationSpotsDTO getEnrollmentInformation(String studentId) {

        // TODO: validate if the semester to enroll is on Schedule of enrollments
        // TODO: validate if the student is already enrolled

        // TODO: factory to build the enrollment model with its information
        // TODO: return said information (pop the information out of the domain, create mappers)

        List<Course> completedCourses = getCompletedCoursesBy(studentId);
        List<Course> baseOffering = courseRepository.getDefaultCourseOfferingByStudentId(studentId);
        List<Course> availableCourses = getAvailabilityFromBaseOfferingAndCompletedCourses(baseOffering, completedCourses);

        return buildDTOWithStudentIdAndAvailableCourses(studentId, availableCourses);
    }

    private List<Course> getCompletedCoursesBy(String studentId) {
        return courseGradesRepository
                .findByFinalGradeGreaterThanAndStudentId(2.9d, studentId).stream()
                .map(courseGrade -> courseGrade.getCourseInstance().getCourse())
                .toList();
    }

    private List<Course> getAvailabilityFromBaseOfferingAndCompletedCourses(List<Course> baseOffering, List<Course> completedCourses) {
        return baseOffering.stream()
                .filter(coursesWithAllPrerequisitesMet(completedCourses))
                .toList();
    }

    private Predicate<Course> coursesWithAllPrerequisitesMet(List<Course> completedCourses) {
        return availableCourse -> availableCourse.getCoursePrerequisites().stream()
                .map(coursePrerequisite -> coursePrerequisite.getCoursePrerequisiteId().getCoursePrerequisite().getId())
                .allMatch(courseCompletion(completedCourses));
    }

    private Predicate<UUID> courseCompletion(List<Course> completedCourses) {
        return (uuid) -> completedCourses.stream().anyMatch(course -> course.getId().equals(uuid));
    }

    private RegistrationSpotsDTO buildDTOWithStudentIdAndAvailableCourses(String studentId, List<Course> availableCourses) {
        return RegistrationSpotsDTO.builder()
                .courseDTOList(availableCourses.stream().map(courseMapper::courseToCourseDTO).toList())
                .personId(studentId)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
