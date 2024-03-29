package com.perficient.udea.enrollment.presentation.errors.DTOs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

    private String statusCode;
    private String message;
    private List<String> errorList;
}
