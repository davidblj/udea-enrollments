package com.perficient.udea.enrollment.presentation.errors.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SimpleErrorResponse {
    private String statusCode;
    private String message;
}
