package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.DTOs.StudentDTO;

public interface UserService {

    StudentDTO saveStudent(StudentDTO studentDTO);
}
