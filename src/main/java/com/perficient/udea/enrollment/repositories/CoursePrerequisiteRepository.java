package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.CoursePrerequisite;
import com.perficient.udea.enrollment.entities.CoursePrerequisiteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePrerequisiteRepository extends JpaRepository<CoursePrerequisite, CoursePrerequisiteId> {
}
