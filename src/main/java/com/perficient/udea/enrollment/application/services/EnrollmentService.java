package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.application.DTOs.SubscriptionDTO;

public interface EnrollmentService {

    void subscribeStudent(SubscriptionDTO subscriptionDTO);

    RegistrationSpotsDTO getEnrollmentInformation(String studentId);
}
