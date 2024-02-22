package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.ClassRoom;
import com.perficient.udea.enrollment.persistence.entities.Course;
import com.perficient.udea.enrollment.persistence.entities.CourseGrades;
import com.perficient.udea.enrollment.persistence.entities.Student;
import com.perficient.udea.enrollment.persistence.repositories.ClassRoomRepository;
import com.perficient.udea.enrollment.persistence.repositories.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ClassRoomPoolTest {

    @Mock
    private ClassRoomRepository classRoomRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    ClassRoomPool classRoomPool;

    @Captor
    ArgumentCaptor<List<ClassRoom>> classRoomCaptor;

    @DisplayName("getClassRooms - Should get all class rooms by class rooms ids")
    @Test
    public void shouldGetAllClassRooms() {
        UUID integralCalculusUuid = UUID.randomUUID();
        UUID diffCalculusUuid = UUID.randomUUID();
        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(integralCalculusUuid).build();
        Course physics = Course.builder().courseName("Physics").id(diffCalculusUuid).build();
        ClassRoom integralCalculusClassRoom = ClassRoom.builder().id(integralCalculusUuid).course(integralCalculus).originalCapacity(40).build();
        ClassRoom physicsClassRoom = ClassRoom.builder().id(diffCalculusUuid).course(physics).originalCapacity(40).build();
        List<UUID> classRoomsUuids = List.of(integralCalculusUuid, diffCalculusUuid);
        List<ClassRoom> classRooms = List.of(integralCalculusClassRoom, physicsClassRoom);
        given(classRoomRepository.getAllByIdIn(classRoomsUuids)).willReturn(classRooms);

        List<ClassRoom> searchedClassRooms = classRoomPool.getClassRooms(classRoomsUuids);

        verify(classRoomRepository, times(1)).getAllByIdIn(classRoomsUuids);
        assertThat(searchedClassRooms.size(), is(2));
    }

    @DisplayName("setNewEnrollments - Should initialize an enrollment with its default grades, updated capacity, and new enrollments")
    @Test
    public void shouldSetNewEnrollments() {

        String studentId = "1152209135";
        Student student = Student.builder().id(studentId).build();
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        UUID integralCalculusUuid = UUID.randomUUID();
        UUID diffCalculusUuid = UUID.randomUUID();
        Course integralCalculus = Course.builder().courseName("Integral Calculus").id(integralCalculusUuid).build();
        Course physics = Course.builder().courseName("Physics").id(diffCalculusUuid).build();
        List<UUID> uuids = List.of(integralCalculusUuid, diffCalculusUuid);
        List<String> uuidsStrings = List.of(integralCalculusUuid.toString(), diffCalculusUuid.toString());
        ClassRoom integralCalculusClassRoom = ClassRoom.builder().id(integralCalculusUuid).course(integralCalculus).originalCapacity(40).build();
        ClassRoom physicsClassRoom = ClassRoom.builder().id(diffCalculusUuid).course(physics).originalCapacity(40).build();
        List<ClassRoom> classRooms = List.of(integralCalculusClassRoom, physicsClassRoom);
        given(classRoomRepository.getAllByIdIn(uuids)).willReturn(classRooms);
        given(studentRepository.getReferenceById(studentId)).willReturn(student);
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().timestamp(activeSession).studentId(studentId).classRoomIds(uuidsStrings).build();

        classRoomPool.setNewEnrollments(subscriptionDTO);

        verify(classRoomRepository).saveAll(classRoomCaptor.capture());
        List<ClassRoom> updatedClassRooms = classRoomCaptor.getValue();
        assertThat(updatedClassRooms, hasItems(hasProperty("availableCapacity", is(39))));
        assertThat(updatedClassRooms, hasItems(hasProperty("currentEnrollments", is(1))));
        assertThat(updatedClassRooms, hasItems(hasProperty("enrollments", is(Set.of(student)))));
        List<CourseGrades> courseGrades = updatedClassRooms.stream().map(ClassRoom::getGrades).flatMap(java.util.Collection::stream).toList();
        assertThat(courseGrades, hasItems(hasProperty("finalGrade", is(0d))));
        assertThat(courseGrades, hasItems(hasProperty("student", is(student))));
        verify(classRoomRepository, times(1)).saveAll(classRooms);
    }
}