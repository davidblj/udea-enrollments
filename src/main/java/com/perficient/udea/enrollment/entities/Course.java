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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
public class Course {

    public Course(UUID id, String courseName, int semester, String scienceField, Pensum pensum, Set<CoursePrerequisite> coursePrerequisites, Set<CourseInstance> courseInstances) {
        this.id = id;
        this.courseName = courseName;
        this.semester = semester;
        this.scienceField = scienceField;
        this.pensum = pensum;
        this.setCoursePrerequisites(coursePrerequisites);
        this.courseInstances = courseInstances;
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

    private int semester;

    // TODO: foreign table, ENUMS ?
    private String scienceField;

    @ManyToOne
    private Pensum pensum;

    @Builder.Default
    @OneToMany(mappedBy = "coursePrerequisiteId.course", cascade = CascadeType.PERSIST)
    private Set<CoursePrerequisite> coursePrerequisites = new HashSet<>();

    @OneToMany(mappedBy = "course")
    private Set<CourseInstance> courseInstances = new HashSet<>();

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
