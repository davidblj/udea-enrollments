package com.perficient.udea.enrollment.persistence.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;


@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursePrerequisiteId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Course course;

    @ManyToOne
    private Course coursePrerequisite;
}
