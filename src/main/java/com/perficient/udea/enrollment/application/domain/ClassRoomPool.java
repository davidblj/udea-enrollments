package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.ClassRoom;
import com.perficient.udea.enrollment.persistence.entities.CourseGrades;
import com.perficient.udea.enrollment.persistence.entities.Student;
import com.perficient.udea.enrollment.persistence.repositories.ClassRoomRepository;
import com.perficient.udea.enrollment.persistence.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClassRoomPool {

    private final ClassRoomRepository classRoomRepository;

    private final StudentRepository studentRepository;

    public List<ClassRoom> getClassRooms(List<UUID> classRoomIds) {
        return classRoomRepository.getAllByIdIn(classRoomIds);
    }

    public void setNewEnrollments(SubscriptionDTO subscriptionDTO) {
        List<UUID> classRoomIds = subscriptionDTO.getClassRoomIds().stream().map(UUID::fromString).toList();
        List<ClassRoom> classRoomsToEnroll = getClassRooms(classRoomIds);
        classRoomsToEnroll.forEach(classRoom -> {
            Student student = studentRepository.getReferenceById(subscriptionDTO.getStudentId());
            classRoom.setAvailableCapacity(classRoom.getAvailableCapacity() - 1);
            classRoom.setCurrentEnrollments(classRoom.getCurrentEnrollments() + 1);
            classRoom.addGrades(getDefaultGrades(student, classRoom));
            classRoom.addEnrollments(student);
        });
        classRoomRepository.saveAll(classRoomsToEnroll);
    }

    private CourseGrades getDefaultGrades(Student student, ClassRoom classRoom) {
        return CourseGrades.builder()
                .student(student)
                .classRoom(classRoom)
                .finalGrade(0d).build();
    }
}
