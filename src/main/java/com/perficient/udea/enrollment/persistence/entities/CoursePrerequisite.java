package com.perficient.udea.enrollment.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursePrerequisite {

    @EmbeddedId
    private CoursePrerequisiteId coursePrerequisiteId;
}
