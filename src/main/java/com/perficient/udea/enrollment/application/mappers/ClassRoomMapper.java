package com.perficient.udea.enrollment.application.mappers;

import com.perficient.udea.enrollment.application.DTOs.ClassRoomDTO;
import com.perficient.udea.enrollment.persistence.entities.ClassRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClassRoomMapper {

    @Mapping( target = "course.id", source = "courseId")
    ClassRoom classRoomDTOToClassRoom(ClassRoomDTO classRoomDTO);

    @Mapping( target = "courseId", ignore = true)
    ClassRoomDTO classRoomToClassRoomDTO(ClassRoom classRoom);
}
