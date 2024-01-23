package com.perficient.udea.enrollment.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
public class Course {

    public Course(UUID id, String courseName, String scienceField, Pensum pensum, Set<CoursePrerequisite> coursePrerequisites) {
        this.id = id;
        this.courseName = courseName;
        this.scienceField = scienceField;
        this.pensum = pensum;
        this.setCoursePrerequisites(coursePrerequisites);
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @NotBlank
    @Size(max = 50)
    @Column(length = 50)
    private String courseName;

    // TODO: foreign table, ENUMS ?
    private String scienceField;

    // TODO: mappedBy? inverseAssociation?
    @ManyToOne
    private Pensum pensum;

    @Builder.Default
    @OneToMany(mappedBy = "coursePrerequisiteId.course", cascade = CascadeType.PERSIST)
    private Set<CoursePrerequisite> coursePrerequisites = new HashSet<>();

    public void setCoursePrerequisites(Set<CoursePrerequisite> coursePrerequisites) {
        coursePrerequisites.forEach(coursePrerequisite -> {
                    CoursePrerequisiteId coursePrerequisiteId = CoursePrerequisiteId.builder()
                            .course(this)
                            .coursePrerequisite(coursePrerequisite.getCoursePrerequisiteId().getCoursePrerequisite()).build();
                    coursePrerequisite.setCoursePrerequisiteId(coursePrerequisiteId);
        });
        this.coursePrerequisites = coursePrerequisites;
    }
}
