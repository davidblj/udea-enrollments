package com.perficient.udea.enrollment.mappers;

import com.perficient.udea.enrollment.DTOs.PersonDTO;
import com.perficient.udea.enrollment.DTOs.StudentDTO;
import com.perficient.udea.enrollment.entities.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper
public interface StudentMapper {

    @Mapping( target = "currentTotalCredits", constant = "0")
    @Mapping( target = "semester", constant = "1")
    @Mapping( target = "syllabus.id", source = "syllabusId")
    Student studentDTOToStudent(StudentDTO studentDTO);

    StudentDTO studentToStudentDTO(Student student);
}
