package com.perficient.udea.enrollment.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Student extends Person {

    private int stratum;
    private int currentTotalCredits;
    private int semester;

    @ManyToOne
    private Pensum pensum;
}
