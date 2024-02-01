package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.ClassRoomDTO;

import java.util.List;


public interface ClassRoomService {

    ClassRoomDTO save(ClassRoomDTO classRoom);

    List<ClassRoomDTO> listClassrooms(String courseId);
}
