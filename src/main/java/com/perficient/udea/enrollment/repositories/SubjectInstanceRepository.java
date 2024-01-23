package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.SubjectInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubjectInstanceRepository extends JpaRepository<SubjectInstance, UUID> {
}
