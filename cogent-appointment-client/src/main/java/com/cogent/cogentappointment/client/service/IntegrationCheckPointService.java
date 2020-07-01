package com.cogent.cogentappointment.client.service;

import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.request.refund.refundStatus.RefundStatusRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;

/**
 * @author smriti on 18/06/20
 */
public interface IntegrationCheckPointService {

    void apiIntegrationCheckpointForDoctorAppointment(Appointment appointment,
                                                      IntegrationBackendRequestDTO integrationRequestDTO);

    void apiIntegrationCheckpointForDepartmentAppointment(Appointment appointment,
                                                          IntegrationBackendRequestDTO integrationRequestDTO);

    BackendIntegrationApiInfo getAppointmentModeApiIntegration(
            IntegrationRefundRequestDTO integrationRefundRequestDTO,
            Long id, String generatedEsewaHmac);

    void apiIntegrationCheckpointForRefundAppointment(Appointment appointment,
                                                      AppointmentTransactionDetail appointmentTransactionDetail,
                                                      AppointmentRefundDetail refundAppointmentDetail,
                                                      IntegrationRefundRequestDTO refundRequestDTO);

    void apiIntegrationCheckpointForRejectAppointment(Appointment appointment,
                                                      AppointmentTransactionDetail appointmentTransactionDetail,
                                                      AppointmentRefundDetail refundAppointmentDetail,
                                                      IntegrationRefundRequestDTO integrationRefundRequestDTO);

    void apiIntegrationCheckpointForRefundStatus(Appointment appointment,
                                                 AppointmentRefundDetail appointmentRefundDetail,
                                                 AppointmentTransactionDetail appointmentTransactionDetail,
                                                 RefundStatusRequestDTO requestDTO);
}
