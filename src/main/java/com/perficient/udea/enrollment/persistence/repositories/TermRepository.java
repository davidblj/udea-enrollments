package com.perficient.udea.enrollment.persistence.repositories;

import com.perficient.udea.enrollment.persistence.entities.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, String> {

    Term findByActiveIs(boolean flag);

    @Query("SELECT t FROM Term t JOIN t.students s WHERE s.id = :uuid and t.active = true ")
    Optional<Term> getCurrentTermEnrollmentByStudentId(@Param("uuid") String studentId);
}
