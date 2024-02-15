package com.perficient.udea.enrollment.persistence.repositories;

import com.perficient.udea.enrollment.persistence.entities.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SyllabusRepository extends JpaRepository<Syllabus, UUID> {

    Syllabus findFirstByCareer(String career);
}
