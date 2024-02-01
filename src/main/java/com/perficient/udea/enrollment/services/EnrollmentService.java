package com.perficient.udea.enrollment.services;

import com.perficient.udea.enrollment.DTOs.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.DTOs.SubscriptionDTO;

public interface EnrollmentService {

    void subscribeStudent(SubscriptionDTO subscriptionDTO);

    RegistrationSpotsDTO getEnrollmentInformation(String studentId);
}
