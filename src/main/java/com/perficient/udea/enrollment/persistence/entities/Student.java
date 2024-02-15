package com.perficient.udea.enrollment.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Student extends Person {

    private int stratum;
    private int currentTotalCredits;
    private int semester;

    @ManyToOne
    private Syllabus syllabus;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "enrolled_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "classroom_id"))
    private Set<ClassRoom> classRoomEnrollments = new HashSet<>();

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "enrolled_term",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "term_id"))
    private Set<Term> termEnrollments = new HashSet<>();

    public void addTermEnrollments(Term term) {
        this.termEnrollments.add(term);
    }
}
