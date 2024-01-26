package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.ClassRoomDTO;
import com.perficient.udea.enrollment.mappers.ClassRoomMapper;
import com.perficient.udea.enrollment.repositories.ClassRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClassRoomServiceImp implements ClassRoomService {

    private final ClassRoomRepository classRoomRepository;
    private final ClassRoomMapper classRoomMapper;

    @Override
    public ClassRoomDTO save(ClassRoomDTO classRoom) {
        return classRoomMapper.classRoomToClassRoomDTO(classRoomRepository.save(classRoomMapper.classRoomDtoToClassRoom(classRoom)));
    }
}
