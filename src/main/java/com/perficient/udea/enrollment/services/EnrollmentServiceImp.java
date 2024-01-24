package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.entities.Course;
import com.perficient.udea.enrollment.mappers.SimpleCourseMapper;
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

    private final SimpleCourseMapper simpleCourseMapper;

    @Override
    public RegistrationSpotsDTO getEnrollmentInformation(String studentId) {

        // TODO: validate if the semester to enroll is on Schedule of enrollments
        // TODO: validate if the student is already enrolled

        // TODO: factory to build the enrollment model with its information
        // TODO: return said information (pop the information out of the domain, create mappers)

        List<Course> successfullyCompletedCourses = getCompletedCoursesBy(studentId);
        List<Course> baseOffering = courseRepository.getDefaultCourseOfferingByStudentId(studentId);
        List<Course> availableCourses = getAvailableCourses(baseOffering, successfullyCompletedCourses);

        return buildDTOWithStudentIdAndAvailableCourses(studentId, availableCourses);
    }

    private List<Course> getCompletedCoursesBy(String studentId) {
        return courseGradesRepository
                .findByFinalGradeGreaterThanAndStudentId(2.9d, studentId).stream()
                .map(courseGrade -> courseGrade.getCourseInstance().getCourse())
                .toList();
    }

    private List<Course> getAvailableCourses(List<Course> baseOffering, List<Course> completedCourses) {
        return baseOffering.stream()
                .filter(coursesSuccessfullyCompleted(completedCourses))
                .filter(coursesWithoutAllPrerequisitesMet(completedCourses))
                .toList();
    }

    private Predicate<Course> coursesWithoutAllPrerequisitesMet(List<Course> completedCourses) {
        return availableCourse -> availableCourse.getCoursePrerequisites().stream()
                .map(coursePrerequisite -> coursePrerequisite.getCoursePrerequisiteId().getCoursePrerequisite().getId())
                .allMatch(courseCompletion(completedCourses));
    }

    private Predicate<UUID> courseCompletion(List<Course> completedCourses) {
        return (uuid) -> completedCourses.stream().anyMatch(course -> course.getId().equals(uuid));
    }

    private Predicate<Course> coursesSuccessfullyCompleted(List<Course> completedCourses) {
        return availableCourse -> completedCourses.stream().noneMatch(course -> course.getId().equals(availableCourse.getId()));
    }

    private RegistrationSpotsDTO buildDTOWithStudentIdAndAvailableCourses(String studentId, List<Course> availableCourses) {
        return RegistrationSpotsDTO.builder()
                .courseDTOList(availableCourses.stream().map(simpleCourseMapper::courseToSimpleCourseDTO).toList())
                .personId(studentId)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
