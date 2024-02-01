package com.perficient.udea.enrollment.repositories;

import com.perficient.udea.enrollment.entities.Course;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    @Query("SELECT c FROM Course c JOIN c.syllabus p JOIN p.students s WHERE s.id = :uuid and c.semester <= s.semester")
    List<Course> getDefaultCourseOfferingByStudentId(@Param("uuid") String studentId);
}
