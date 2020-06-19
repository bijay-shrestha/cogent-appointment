package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;

/**
 * @author smriti on 18/06/20
 */
public interface IntegrationCheckPointService {

    void apiIntegrationCheckpointForDoctorAppointment(Appointment appointment,
                                                      IntegrationBackendRequestDTO integrationRequestDTO);

    void apiIntegrationCheckpointForDepartmentAppointment(Appointment appointment,
                                                          IntegrationBackendRequestDTO integrationRequestDTO);
}
