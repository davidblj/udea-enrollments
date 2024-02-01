package com.perficient.udea.enrollment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class ClassRoom {

    public ClassRoom(UUID id, int term, int currentEnrollments, int availableCapacity, int originalCapacity, String schedule, String recurringDays, LocalDateTime createDate, Course course, Set<Student> enrollments) {
        this.id = id;
        this.term = term;
        this.currentEnrollments = currentEnrollments;
        this.availableCapacity = originalCapacity;
        this.originalCapacity = originalCapacity;
        this.schedule = schedule;
        this.recurringDays = recurringDays;
        this.createDate = createDate;
        this.course = course;
        this.enrollments = enrollments;
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    private int term;

    private int currentEnrollments;

    private int availableCapacity;

    private int originalCapacity;

    private String schedule;

    private String recurringDays;

    @CreationTimestamp
    private LocalDateTime createDate;

    @ManyToOne
    private Course course;

    @Builder.Default
    @ManyToMany
    @JoinTable(name = "enrolled_student",
            joinColumns = @JoinColumn(name = "classroom_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id"))
    private Set<Student> enrollments = new HashSet<>();

    public void addEnrollments(Student student) {
        this.enrollments.add(student);
    }
}
