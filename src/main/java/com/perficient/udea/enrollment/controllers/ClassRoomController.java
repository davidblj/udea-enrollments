package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.ClassRoomDTO;
import com.perficient.udea.enrollment.services.ClassRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Class room", description = "Class room management API operations")
@RestController
@RequiredArgsConstructor
public class ClassRoomController {

    private final ClassRoomService classRoomService;

    public static final String CLASSROOM_PATH = "/api/v1/course/{courseId}/classroom";

    @Operation(summary = "List all available class rooms by course id")
    @GetMapping(value = CLASSROOM_PATH)
    public List<ClassRoomDTO> getClassrooms(@PathVariable("courseId") String courseId) {
        return classRoomService.listClassrooms(courseId);
    }

    @Operation(summary = "Save a class room  by course id")
    @PostMapping(value = CLASSROOM_PATH)
    public ResponseEntity<HttpHeaders> saveClassroom(@PathVariable("courseId") String courseId, @Validated @RequestBody ClassRoomDTO classRoom) {
        classRoom.setCourseId(courseId);
        ClassRoomDTO classRoomDTO = classRoomService.save(classRoom);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CLASSROOM_PATH + "/" + classRoomDTO.getId().toString());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }
}
