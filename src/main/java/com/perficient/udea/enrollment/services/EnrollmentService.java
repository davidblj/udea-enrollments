package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;

import java.util.UUID;

public interface EnrollmentService {

    RegistrationSpotsDTO getEnrollmentInformation(String studentId);
}
