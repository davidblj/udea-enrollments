package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.persistence.entities.Course;
import com.perficient.udea.enrollment.persistence.repositories.CourseGradesRepository;
import com.perficient.udea.enrollment.persistence.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class CoursePool  {

    private final CourseRepository courseRepository;
    private final CourseGradesRepository courseGradesRepository;

    public List<Course> getAvailableCourses(String studentId) {
        List<Course> successfullyCompletedCourses = getCompletedCoursesBy(studentId);
        List<Course> baseOffering = courseRepository.getBaseOfferingByStudentId(studentId);
        return getAvailableCourses(baseOffering, successfullyCompletedCourses);
    }

    private List<Course> getCompletedCoursesBy(String studentId) {
        return courseGradesRepository
                .findByFinalGradeGreaterThanAndStudentId(2.9d, studentId).stream()
                .map(courseGrade -> courseGrade.getClassRoom().getCourse())
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
        return (uuid) -> completedCourses.stream()
                .anyMatch(course -> course.getId().equals(uuid));
    }

    private Predicate<Course> coursesSuccessfullyCompleted(List<Course> completedCourses) {
        return availableCourse -> completedCourses.stream()
                .noneMatch(course -> course.getId().equals(availableCourse.getId()));
    }
}
