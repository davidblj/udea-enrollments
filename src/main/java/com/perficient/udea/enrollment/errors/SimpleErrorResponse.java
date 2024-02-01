package com.perficient.udea.enrollment.errors;

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
