package com.perficient.udea.enrollment.application.services;

import com.perficient.udea.enrollment.application.dtos.RegistrationSpotsDTO;
import com.perficient.udea.enrollment.application.dtos.SubscriptionDTO;

public interface EnrollmentService {

    void subscribeStudent(SubscriptionDTO subscriptionDTO);

    RegistrationSpotsDTO getEnrollmentInformation(String studentId);
}
