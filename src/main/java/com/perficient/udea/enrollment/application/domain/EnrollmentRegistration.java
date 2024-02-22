package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.Student;
import com.perficient.udea.enrollment.persistence.entities.Term;
import com.perficient.udea.enrollment.persistence.repositories.StudentRepository;
import com.perficient.udea.enrollment.persistence.repositories.TermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EnrollmentRegistration {

    private final TermRepository termRepository;
    private final StudentRepository studentRepository;

    public void createTermEnrollment(SubscriptionDTO subscriptionDTO) {
        Student student = studentRepository.getReferenceById(subscriptionDTO.getStudentId());
        Term term = termRepository.findByActiveIs(true);
        student.addTermEnrollments(term);
        studentRepository.save(student);
    }
}
