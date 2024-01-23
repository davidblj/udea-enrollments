package com.perficient.udea.enrollment.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class PensumDTO {

    private UUID id;
    private String career;
    private Boolean active;
    private Integer version;
    private int totalCredits;
    private int minimumTotalCredits;
}
