package com.perficient.udea.enrollment.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@PrimaryKeyJoinColumn(name="id")
public class Teacher extends Person {

    // TODO: use an enum
    private String type;
}
