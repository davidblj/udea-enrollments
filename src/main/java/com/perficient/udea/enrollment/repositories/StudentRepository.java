package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
