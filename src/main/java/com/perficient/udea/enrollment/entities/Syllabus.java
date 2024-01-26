package com.perficient.udea.enrollment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@AllArgsConstructor
@NoArgsConstructor
public class Syllabus {

    // TODO: rename class to syllabus

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    // TODO: composite key of career and version ?
    @NotNull
    private String career;

    private boolean active;

    @NotNull
    private int totalCredits;

    @NotNull
    private int minimumTotalCredits;

    @CreationTimestamp
    private LocalDateTime createDate;

    @OneToMany(mappedBy = "syllabus")
    private Set<Student> students = new HashSet<>();
}
