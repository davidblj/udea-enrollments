package com.perficient.udea.enrollment.application.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RegistrationSpotsDTO {

    private String personId;
    private List<SimpleCourseDTO> courseDTOList;
    private Long timestamp;
}
