package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.application.dtos.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.persistence.entities.*;
import com.perficient.udea.enrollment.persistence.repositories.CourseGradesRepository;
import com.perficient.udea.enrollment.persistence.repositories.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CoursePoolTest {

    @Mock
    CourseRepository courseRepository;

    @Mock
    CourseGradesRepository courseGradesRepository;

    @InjectMocks
    CoursePool coursePool;

    @DisplayName("getEnrollmentInformation - Should exclude a completed course from the final offering")
    @Test
    public void shouldExcludeCompletedCourses() {
        String studentId = "1152209135";

        Course algebra = Course.builder().courseName("Algebra").id(UUID.randomUUID()).build();
        Course mathematics = Course.builder().courseName("mathematics").id(UUID.randomUUID()).build();
        ClassRoom classRoom = ClassRoom.builder().course(algebra).build();
        CourseGrades courseGrades = CourseGrades.builder().classRoom(classRoom).build();
        List<CourseGrades> courseGradesList = List.of(courseGrades);
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(courseGradesList);
        List<Course> courseList = List.of(algebra, mathematics);
        given(courseRepository.getBaseOfferingByStudentId(studentId)).willReturn(courseList);

        List<Course> availableCourses = coursePool.getAvailableCourses(studentId);

        assertThat(availableCourses.size()).isEqualTo(1);
        assertThat(availableCourses.getFirst().getId()).isEqualTo(mathematics.getId());
    }

    @DisplayName("getEnrollmentInformation - Should exclude a course with its prerequisites not met")
    @Test
    public void shouldExcludeCoursesWithUnmetPrerequisites() {

        String studentId = "1152209135";

        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(UUID.randomUUID()).build();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").id(UUID.randomUUID()).build();
        CoursePrerequisiteId coursePrerequisiteId = CoursePrerequisiteId.builder().course(integralCalculus).coursePrerequisite(differentialCalculus).build();
        CoursePrerequisite coursePrerequisite = CoursePrerequisite.builder().coursePrerequisiteId(coursePrerequisiteId).build();
        integralCalculus.setCoursePrerequisites(Set.of(coursePrerequisite));
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(List.of());
        List<Course> courseList = List.of(integralCalculus, differentialCalculus);
        given(courseRepository.getBaseOfferingByStudentId(studentId)).willReturn(courseList);

        List<Course> availableCourses = coursePool.getAvailableCourses(studentId);

        assertThat(availableCourses.size()).isEqualTo(1);
        assertThat(availableCourses.getFirst().getId()).isEqualTo(differentialCalculus.getId());
    }


    @DisplayName("getEnrollmentInformation - should list all available courses")
    @Test
    public void shouldListAvailableCoursesToEnroll() {

        String studentId = "1152209135";
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").id(UUID.randomUUID()).build();
        Course algebra = Course.builder().courseName("Algebra").id(UUID.randomUUID()).build();
        Course mathematics = Course.builder().courseName("mathematics").id(UUID.randomUUID()).build();
        List<Course> courseList = List.of(algebra, mathematics, differentialCalculus);
        given(courseRepository.getBaseOfferingByStudentId(studentId)).willReturn(courseList);

        List<Course> availableCourses = coursePool.getAvailableCourses(studentId);

        assertThat(availableCourses.size()).isEqualTo(courseList.size());
    }
}