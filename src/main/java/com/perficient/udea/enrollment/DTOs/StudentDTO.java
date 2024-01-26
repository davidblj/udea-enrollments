package com.perficient.udea.enrollment.DTOs;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDTO {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String id;
}
