package com.perficient.udea.enrollment.persistence.repositories;

import com.perficient.udea.enrollment.persistence.entities.CourseGrades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CourseGradesRepository extends JpaRepository<CourseGrades, UUID> {

    List<CourseGrades> findByFinalGradeGreaterThanAndStudentId(double finalGrade, String studentId);
}
