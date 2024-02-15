package com.perficient.udea.enrollment.persistence.repositories;

import com.perficient.udea.enrollment.persistence.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectRepository extends JpaRepository<Subject, UUID> {
}
