package com.perficient.udea.enrollment.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
public class CoursePrerequisiteId implements Serializable {

    private static final long serialVersionUID = 1L;

//    private String courseName;
//    private String prerequisiteCourseName;
    @ManyToOne
//    @JoinColumn(name = "fk_course")
    private Course course;

    @ManyToOne
//    @JoinColumn(name = "fk_course_prerequisite")
    private Course coursePrerequisite;
}
