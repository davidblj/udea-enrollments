package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.DTOs.SubscriptionDTO;
import com.perficient.udea.enrollment.entities.*;
import com.perficient.udea.enrollment.errors.exceptions.EnrollmentInvalidSessionException;
import com.perficient.udea.enrollment.errors.exceptions.InvalidCourseEnrollmentException;
import com.perficient.udea.enrollment.errors.exceptions.InvalidCourseTrayException;
import com.perficient.udea.enrollment.errors.exceptions.NoSpotsAvailableException;
import com.perficient.udea.enrollment.mappers.SimpleCourseMapper;
import com.perficient.udea.enrollment.repositories.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceImpTest {

    @Mock
    private CourseGradesRepository courseGradesRepository;

    @Mock
    private ClassRoomRepository classRoomRepository;

    @Mock
    private TermRepository termRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Spy
    private SimpleCourseMapper simpleCourseMapper = Mappers.getMapper(SimpleCourseMapper.class);

    @InjectMocks
    private EnrollmentServiceImp enrollmentServiceImp;

    @DisplayName("getEnrollmentInformation - Should throw an exception when the student already have an enrolled term")
    @Test
    public void shouldValidateActiveEnrollment() {
        String studentId = "1152209135";
        Term term = Term.builder().build();
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(term);

        assertThrows(InvalidCourseEnrollmentException.class, () -> {
            enrollmentServiceImp.getEnrollmentInformation(studentId);
        });
    }

    @DisplayName("getEnrollmentInformation - Should exclude a completed course from the final offering")
    @Test
    public void shouldExcludeCompletedCourses() {
        String studentId = "1152209135";

        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(null);
        Course algebra = Course.builder().courseName("Algebra").id(UUID.randomUUID()).build();
        Course mathematics = Course.builder().courseName("mathematics").id(UUID.randomUUID()).build();
        ClassRoom classRoom = ClassRoom.builder().course(algebra).build();
        CourseGrades courseGrades = CourseGrades.builder().classRoom(classRoom).build();
        List<CourseGrades> courseGradesList = List.of(courseGrades);
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(courseGradesList);
        List<Course> courseList = List.of(algebra, mathematics);
        given(courseRepository.getDefaultCourseOfferingByStudentId(studentId)).willReturn(courseList);

        RegistrationSpotsDTO enrollmentInformation = enrollmentServiceImp.getEnrollmentInformation(studentId);

        assertThat(enrollmentInformation.getCourseDTOList().size()).isEqualTo(1);
        assertThat(enrollmentInformation.getCourseDTOList().getFirst().getId()).isEqualTo(mathematics.getId());
    }

    @DisplayName("getEnrollmentInformation - Should exclude a course with its prerequisites not met")
    @Test
    public void shouldExcludeCoursesWithUnmetPrerequisites() {
        String studentId = "1152209135";

        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(null);
        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(UUID.randomUUID()).build();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").id(UUID.randomUUID()).build();
        CoursePrerequisiteId coursePrerequisiteId = CoursePrerequisiteId.builder().course(integralCalculus).coursePrerequisite(differentialCalculus).build();
        CoursePrerequisite coursePrerequisite = CoursePrerequisite.builder().coursePrerequisiteId(coursePrerequisiteId).build();
        integralCalculus.setCoursePrerequisites(Set.of(coursePrerequisite));
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(List.of());
        List<Course> courseList = List.of(integralCalculus, differentialCalculus);
        given(courseRepository.getDefaultCourseOfferingByStudentId(studentId)).willReturn(courseList);

        RegistrationSpotsDTO enrollmentInformation = enrollmentServiceImp.getEnrollmentInformation(studentId);

        assertThat(enrollmentInformation.getCourseDTOList().size()).isEqualTo(1);
        assertThat(enrollmentInformation.getCourseDTOList().getFirst().getId()).isEqualTo(differentialCalculus.getId());
    }

    @DisplayName("getEnrollmentInformation - Should throw an exception when the student passed it's due time to subscribe")
    @Test
    public void shouldValidateSession() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() - (60 * 1000);
        List<String> uuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder()
                .timestamp(activeSession)
                .studentId(studentId)
                .classRoomIds(uuids).build();
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(List.of());
        given(courseRepository.getDefaultCourseOfferingByStudentId(studentId)).willReturn(List.of());

        assertThrows(EnrollmentInvalidSessionException.class, () -> {
            enrollmentServiceImp.subscribeStudent(subscriptionDTO);
        });
    }

    @DisplayName("getEnrollmentInformation - Should throw an exception when the student have an active enrollment")
    @Test
    public void shouldValidateActiveEnrollmentOnSubscription() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        List<String> uuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder()
                .timestamp(activeSession)
                .studentId(studentId)
                .classRoomIds(uuids).build();
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(List.of());
        given(courseRepository.getDefaultCourseOfferingByStudentId(studentId)).willReturn(List.of());
        Term term = Term.builder().build();
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(term);

        assertThrows(InvalidCourseEnrollmentException.class, () -> {
            enrollmentServiceImp.subscribeStudent(subscriptionDTO);
        });
    }

    @DisplayName("getEnrollmentInformation - Should throw an exception when non existing course ids were sent")
    @Test
    public void shouldValidateInvalidCourseIds() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        List<String> uuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder()
                .timestamp(activeSession)
                .studentId(studentId)
                .classRoomIds(uuids).build();
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(List.of());
        given(courseRepository.getDefaultCourseOfferingByStudentId(studentId)).willReturn(List.of());
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(null);
        given(classRoomRepository.getAllByIdIn(any())).willReturn(List.of());

        assertThrows(InvalidCourseTrayException.class, () -> {
            enrollmentServiceImp.subscribeStudent(subscriptionDTO);
        });
    }

    @DisplayName("getEnrollmentInformation - Should throw an exception when a provided classroom is out of spots")
    @Test
    public void shouldValidateClassRoomsOutOfSpots() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(UUID.randomUUID()).build();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").id(UUID.randomUUID()).build();
        List<Course> courses = List.of(integralCalculus, differentialCalculus);
        UUID integralCalculusUuid = UUID.randomUUID();
        UUID diffCalculusUuid = UUID.randomUUID();
        List<String> uuids = List.of(integralCalculusUuid.toString(), diffCalculusUuid.toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder()
                .timestamp(activeSession)
                .studentId(studentId)
                .classRoomIds(uuids).build();
        ClassRoom integralCalculusClassRoom = ClassRoom.builder().id(integralCalculusUuid).course(integralCalculus).availableCapacity(0).build();
        ClassRoom diffenrentialCalculusClassRoom = ClassRoom.builder().id(diffCalculusUuid).course(integralCalculus).build();
        List<ClassRoom> classRooms = List.of(integralCalculusClassRoom, diffenrentialCalculusClassRoom);
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(List.of());
        given(courseRepository.getDefaultCourseOfferingByStudentId(studentId)).willReturn(courses);
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(null);
        given(classRoomRepository.getAllByIdIn(any())).willReturn(classRooms);


        assertThrows(NoSpotsAvailableException.class, () -> {
            enrollmentServiceImp.subscribeStudent(subscriptionDTO);
        });
    }

    @DisplayName("getEnrollmentInformation - Should throw an exception when a provided course id is not a valid course subscription")
    @Test
    public void shouldValidateCourseSubscription() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(UUID.randomUUID()).build();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").id(UUID.randomUUID()).build();
        List<Course> courses = List.of(integralCalculus, differentialCalculus);
        UUID integralCalculusUuid = UUID.randomUUID();
        UUID diffCalculusUuid = UUID.randomUUID();
        List<String> uuids = List.of(integralCalculusUuid.toString(), diffCalculusUuid.toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder()
                .timestamp(activeSession)
                .studentId(studentId)
                .classRoomIds(uuids).build();
        ClassRoom integralCalculusClassRoom = ClassRoom.builder().id(integralCalculusUuid).course(integralCalculus).build();
        ClassRoom diffenrentialCalculusClassRoom = ClassRoom.builder().id(diffCalculusUuid).course(integralCalculus).build();
        List<ClassRoom> classRooms = List.of(integralCalculusClassRoom, diffenrentialCalculusClassRoom);
        given(courseGradesRepository.findByFinalGradeGreaterThanAndStudentId(2.9, studentId)).willReturn(List.of());
        given(courseRepository.getDefaultCourseOfferingByStudentId(studentId)).willReturn(courses);
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(null);
        given(classRoomRepository.getAllByIdIn(any())).willReturn(classRooms);

        assertThrows(NoSpotsAvailableException.class, () -> {
            enrollmentServiceImp.subscribeStudent(subscriptionDTO);
        });
    }



}