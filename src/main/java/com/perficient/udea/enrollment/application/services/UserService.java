package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.dtos.StudentDTO;

public interface UserService {

    StudentDTO saveStudent(StudentDTO studentDTO);
}
