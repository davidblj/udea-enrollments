package com.perficient.udea.enrollment.persistence.repositories;

import com.perficient.udea.enrollment.persistence.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
