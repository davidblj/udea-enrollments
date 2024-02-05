package com.perficient.udea.enrollment.controllers;

import com.perficient.udea.enrollment.DTOs.StudentDTO;
import com.perficient.udea.enrollment.services.CourseService;
import com.perficient.udea.enrollment.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    public static final String MEMBERS_PATH = "/api/v1/members";
    public static final String STUDENTS_PATH = MEMBERS_PATH + "/student";

    private final UserService userService;

    @PostMapping(value = STUDENTS_PATH)
    public ResponseEntity<HttpHeaders> saveStudent(@Validated @RequestBody StudentDTO studentDTO) {

        StudentDTO savedStudent = userService.saveStudent(studentDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", STUDENTS_PATH + "/" + savedStudent.getId());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

}
