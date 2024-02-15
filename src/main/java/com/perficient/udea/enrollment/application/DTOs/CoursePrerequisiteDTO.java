package com.perficient.udea.enrollment.application.DTOs;

import lombok.*;
import org.hibernate.validator.constraints.UUID;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CoursePrerequisiteDTO {

    @UUID(version = 1)
    private String coursePrerequisiteId;
}
