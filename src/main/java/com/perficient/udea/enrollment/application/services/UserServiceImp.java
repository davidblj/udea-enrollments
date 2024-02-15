package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.DTOs.StudentDTO;
import com.perficient.udea.enrollment.application.mappers.StudentMapper;
import com.perficient.udea.enrollment.persistence.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final PersonRepository personRepository;

    private final StudentMapper studentMapper;

    public StudentDTO saveStudent(StudentDTO studentDTO) {
        return studentMapper.studentToStudentDTO(personRepository.save(studentMapper.studentDTOToStudent(studentDTO)));
    }
}
