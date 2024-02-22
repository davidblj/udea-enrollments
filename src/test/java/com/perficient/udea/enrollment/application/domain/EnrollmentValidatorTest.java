package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.ClassRoom;
import com.perficient.udea.enrollment.persistence.entities.Course;
import com.perficient.udea.enrollment.persistence.entities.Term;
import com.perficient.udea.enrollment.persistence.repositories.TermRepository;
import com.perficient.udea.enrollment.presentation.errors.exceptions.ActiveEnrollmentException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.EnrollmentInvalidSessionException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.InvalidCourseTrayException;
import com.perficient.udea.enrollment.presentation.errors.exceptions.NoSpotsAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class EnrollmentValidatorTest {

    @Mock
    private CoursePool coursePool;
    @Mock
    private ClassRoomPool classRoomPool;
    @Mock
    private TermRepository termRepository;
    @InjectMocks
    EnrollmentValidator enrollmentValidator;


    @DisplayName("validateOpening - Should throw an exception when the student already have an enrolled term")
    @Test
    public void shouldValidateActiveEnrollment() {
        String studentId = "1152209135";
        Term term = Term.builder().build();
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(term);

        assertThrows(ActiveEnrollmentException.class, () -> {
            enrollmentValidator.validateOpening(studentId);
        });
    }

    @DisplayName("validateSubscription - Should throw an exception when the student passed it's due time to subscribe")
    @Test
    public void shouldValidateSession() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() - (60 * 1000);
        List<String> uuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().timestamp(activeSession).studentId(studentId).classRoomIds(uuids).build();

        assertThrows(EnrollmentInvalidSessionException.class, () -> {
            enrollmentValidator.validateSubscription(subscriptionDTO);
        });
    }

    @DisplayName("validateSubscription - Should throw an exception when the student have an active enrollment")
    @Test
    public void shouldValidateActiveEnrollmentOnSubscription() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().timestamp(activeSession).studentId(studentId).classRoomIds(List.of()).build();
        Term term = Term.builder().build();
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(term);
        given(classRoomPool.getClassRooms(Mockito.any())).willReturn(List.of());

        assertThrows(ActiveEnrollmentException.class, () -> {
            enrollmentValidator.validateSubscription(subscriptionDTO);
        });
    }

    @DisplayName("validateSubscription - Should throw an exception when non existing classroom ids were sent")
    @Test
    public void shouldValidateInvalidClassroomIds() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        List<String> uuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().timestamp(activeSession).studentId(studentId).classRoomIds(uuids).build();
        Term term = Term.builder().build();
        given(termRepository.getCurrentTermEnrollmentByStudentId(studentId)).willReturn(term);
        given(classRoomPool.getClassRooms(Mockito.any())).willReturn(List.of());

        assertThrows(ActiveEnrollmentException.class, () -> {
            enrollmentValidator.validateSubscription(subscriptionDTO);
        });
    }

    @DisplayName("validateSubscription - Should throw an exception when a provided classroom is out of spots")
    @Test
    public void shouldValidateClassRoomsOutOfSpots() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(UUID.randomUUID()).build();
        Course physics = Course.builder().courseName("Physics").id(UUID.randomUUID()).build();
        UUID integralCalculusUuid = UUID.randomUUID();
        UUID diffCalculusUuid = UUID.randomUUID();
        List<String> uuids = List.of(integralCalculusUuid.toString(), diffCalculusUuid.toString());
        ClassRoom integralCalculusClassRoom = ClassRoom.builder().id(integralCalculusUuid).course(integralCalculus).availableCapacity(0).build();
        ClassRoom physicsClassRoom = ClassRoom.builder().id(diffCalculusUuid).course(physics).availableCapacity(40).build();
        List<ClassRoom> classRooms = List.of(integralCalculusClassRoom, physicsClassRoom);
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().timestamp(activeSession).studentId(studentId).classRoomIds(uuids).build();
        given(classRoomPool.getClassRooms(Mockito.any())).willReturn(classRooms);

        assertThrows(NoSpotsAvailableException.class, () -> {
            enrollmentValidator.validateSubscription(subscriptionDTO);
        });
    }

    @DisplayName("subscribeStudent - Should throw an exception when a provided course id is not a valid course subscription")
    @Test
    public void shouldValidateCourseSubscription() {

        String studentId = "1152209135";
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(UUID.randomUUID()).build();
        Course differentialCalculus = Course.builder().courseName("Differential Calculus").id(UUID.randomUUID()).build();
        UUID integralCalculusUuid = UUID.randomUUID();
        ClassRoom integralCalculusClassRoom = ClassRoom.builder().id(integralCalculusUuid).course(integralCalculus).originalCapacity(40).build();
        List<ClassRoom> classRooms = List.of(integralCalculusClassRoom);
        List<Course> courses = List.of(differentialCalculus);
        given(classRoomPool.getClassRooms(Mockito.any())).willReturn(classRooms);
        given(coursePool.getAvailableCourses(studentId)).willReturn(courses);
        List<String> uuids = List.of(integralCalculusUuid.toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().timestamp(activeSession).studentId(studentId).classRoomIds(uuids).build();


        assertThrows(InvalidCourseTrayException.class, () -> {
            enrollmentValidator.validateSubscription(subscriptionDTO);
        });
    }
}