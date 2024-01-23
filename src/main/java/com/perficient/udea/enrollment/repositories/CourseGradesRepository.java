package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.CourseGrades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseGradesRepository extends JpaRepository<CourseGrades, UUID> {
}
