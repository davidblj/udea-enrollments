package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.ClassRoomDTO;
import com.perficient.udea.enrollment.services.ClassRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    public static final String CLASSROOM_PATH = "/api/v1/course/{courseId}/classroom";

    @GetMapping(value = CLASSROOM_PATH)
    public List<ClassRoomDTO> getClassrooms(@PathVariable("courseId") String courseId) {
        return classRoomService.listClassrooms(courseId);
    }

    // TODO: add teacher
    @PostMapping(value = CLASSROOM_PATH)
    public ResponseEntity<HttpHeaders> saveClassroom(@PathVariable("courseId") String courseId, @RequestBody ClassRoomDTO classRoom) {

        classRoom.setCourseId(courseId);
        ClassRoomDTO classRoomDTO = classRoomService.save(classRoom);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CLASSROOM_PATH + "/" + classRoomDTO.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
