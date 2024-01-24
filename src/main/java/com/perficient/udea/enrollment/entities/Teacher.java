package com.perficient.udea.enrollment.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name="id")
public class Teacher extends Person {

    // TODO: use an enum
    private String type;
}
