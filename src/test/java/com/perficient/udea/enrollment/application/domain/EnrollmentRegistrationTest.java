package com.perficient.udea.enrollment.application.domain;

import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;
import com.perficient.udea.enrollment.persistence.entities.Student;
import com.perficient.udea.enrollment.persistence.entities.Term;
import com.perficient.udea.enrollment.persistence.repositories.StudentRepository;
import com.perficient.udea.enrollment.persistence.repositories.TermRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EnrollmentRegistrationTest {

    @Mock
    private TermRepository termRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private EnrollmentRegistration enrollmentRegistration;

    @Captor
    ArgumentCaptor<Student> studentCaptor;

    @DisplayName("createTermEnrollment - Should create a term enrollment to the latest active term")
    @Test
    public void shouldCreateAnEnrollment() {
        String studentId = "1152209135";
        Student student = Student.builder().id(studentId).build();
        Long activeSession = System.currentTimeMillis() + (60 * 1000);
        List<String> uuids = List.of(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        SubscriptionDTO subscriptionDTO = SubscriptionDTO.builder().timestamp(activeSession).studentId(studentId).classRoomIds(uuids).build();
        Term term = Term.builder().build();
        given(studentRepository.getReferenceById(studentId)).willReturn(student);
        given(termRepository.findByActiveIs(true)).willReturn(term);

        enrollmentRegistration.createTermEnrollment(subscriptionDTO);

        verify(studentRepository).save(studentCaptor.capture());
        Student studentCaptorValue = studentCaptor.getValue();
        assertThat(studentCaptorValue.getTermEnrollments(), hasItem(term));
    }
}