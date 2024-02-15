package com.perficient.udea.enrollment.persistence.repositories;

import com.perficient.udea.enrollment.persistence.entities.CoursePrerequisite;
import com.perficient.udea.enrollment.persistence.entities.CoursePrerequisiteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePrerequisiteRepository extends JpaRepository<CoursePrerequisite, CoursePrerequisiteId> {
}
