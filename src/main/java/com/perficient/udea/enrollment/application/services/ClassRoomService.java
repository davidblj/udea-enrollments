package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.DTOs.ClassRoomDTO;

import java.util.List;


public interface ClassRoomService {

    ClassRoomDTO save(ClassRoomDTO classRoom);

    List<ClassRoomDTO> listClassrooms(String courseId);
}
