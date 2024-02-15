package com.perficient.udea.enrollment.persistence.entities;

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

    public Course(UUID id, String courseName, int semester, String scienceField, int credits, Syllabus syllabus, Set<CoursePrerequisite> coursePrerequisites, Set<ClassRoom> classRooms, Set<Subject> subjects) {
        this.id = id;
        this.courseName = courseName;
        this.semester = semester;
        this.scienceField = scienceField;
        this.credits = credits;
        this.syllabus = syllabus;
        this.setCoursePrerequisites(coursePrerequisites);
        this.classRooms = classRooms;
        this.setSubjects(subjects);
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
    @Column(length = 50, unique = true)
    private String courseName;

    private int semester;

    private String scienceField;

    private int credits;

    @ManyToOne
    private Syllabus syllabus;

    @Builder.Default
    @OneToMany(mappedBy = "coursePrerequisiteId.course", cascade = CascadeType.PERSIST)
    private Set<CoursePrerequisite> coursePrerequisites = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "course")
    private Set<ClassRoom> classRooms = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "course", cascade = CascadeType.PERSIST)
    private Set<Subject> subjects = new HashSet<>();

    public void setCoursePrerequisites(Set<CoursePrerequisite> coursePrerequisites) {
        coursePrerequisites.forEach(coursePrerequisite -> {
            CoursePrerequisiteId coursePrerequisiteId = CoursePrerequisiteId.builder()
                    .course(this)
                    .coursePrerequisite(coursePrerequisite.getCoursePrerequisiteId().getCoursePrerequisite()).build();
            coursePrerequisite.setCoursePrerequisiteId(coursePrerequisiteId);
        });
        this.coursePrerequisites = coursePrerequisites;
    }

    public void setSubjects(Set<Subject> subjects) {
        subjects.forEach(subject -> subject.setCourse(this));
        this.subjects = subjects;
    }
}
