package com.perficient.udea.enrollment.application.mappers;

import com.perficient.udea.enrollment.application.dtos.StudentDTO;
import com.perficient.udea.enrollment.persistence.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper {

    @Mapping( target = "currentTotalCredits", constant = "0")
    @Mapping( target = "semester", constant = "1")
    @Mapping( target = "syllabus.id", source = "syllabusId")
    Student studentDTOToStudent(StudentDTO studentDTO);

    StudentDTO studentToStudentDTO(Student student);
}
