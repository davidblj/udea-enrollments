package com.perficient.udea.enrollment.DTOs;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RegistrationSpotsDTO {

    private String personId;
    private List<CourseDTO> courseDTOList;
    private Long timestamp;
}
