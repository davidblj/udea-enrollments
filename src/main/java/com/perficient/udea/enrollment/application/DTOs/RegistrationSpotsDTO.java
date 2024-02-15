package com.perficient.udea.enrollment.application.DTOs;

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
