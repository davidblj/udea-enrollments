package com.perficient.udea.enrollment.mappers;

import com.perficient.udea.enrollment.DTOs.ClassRoomDTO;
import com.perficient.udea.enrollment.entities.ClassRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ClassRoomMapper {

    @Mapping( target = "course.id", source = "courseId")
    ClassRoom classRoomDTOToClassRoom(ClassRoomDTO classRoomDTO);

    @Mapping( target = "courseId", ignore = true)
    ClassRoomDTO classRoomToClassRoomDTO(ClassRoom classRoom);
}
