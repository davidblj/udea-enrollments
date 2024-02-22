package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.dtos.ClassRoomDTO;
import com.perficient.udea.enrollment.application.mappers.ClassRoomMapper;
import com.perficient.udea.enrollment.persistence.repositories.ClassRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomMapper classRoomMapper;

    public ClassRoomDTO save(ClassRoomDTO classRoom) {
        return classRoomMapper.classRoomToClassRoomDTO(classRoomRepository.save(classRoomMapper.classRoomDTOToClassRoom(classRoom)));
    }

    public List<ClassRoomDTO> listClassrooms(String courseId) {
        return classRoomRepository.findAllByCourseId(UUID.fromString(courseId)).stream()
                .map(classRoomMapper::classRoomToClassRoomDTO)
                .toList();
    }
}
