package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationCheckInRequestDTO;

/**
 * @author rupak on 2020-05-19
 */
public interface IntegrationService {

    void approveAppointmentCheckIn(ApiIntegrationCheckInRequestDTO requestDTO);
}
