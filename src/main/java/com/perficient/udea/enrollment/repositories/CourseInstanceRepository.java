package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.CourseInstance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CourseInstanceRepository extends JpaRepository<CourseInstance, UUID> {
}
