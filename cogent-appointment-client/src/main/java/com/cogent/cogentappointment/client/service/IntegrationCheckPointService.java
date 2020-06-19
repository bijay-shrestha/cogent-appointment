package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;

/**
 * @author smriti on 18/06/20
 */
public interface IntegrationCheckPointService {

    void apiIntegrationCheckpointDoctorWise(Appointment appointment,
                                            IntegrationBackendRequestDTO integrationRequestDTO);

    void apiIntegrationCheckpointDepartmentWise(Appointment appointment,
                                                IntegrationBackendRequestDTO integrationRequestDTO);

    BackendIntegrationApiInfo getAppointmentModeApiIntegration(IntegrationRefundRequestDTO integrationRefundRequestDTO, Long id, String generatedEsewaHmac);
}
