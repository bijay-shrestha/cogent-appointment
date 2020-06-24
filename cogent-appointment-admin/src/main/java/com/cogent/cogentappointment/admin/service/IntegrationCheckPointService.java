package com.cogent.cogentappointment.admin.service;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;

/**
 * @author smriti on 18/06/20
 */
public interface IntegrationCheckPointService {

    void apiIntegrationCheckpointForDoctorAppointment(Appointment appointment,
                                                      IntegrationBackendRequestDTO integrationRequestDTO);

    void apiIntegrationCheckpointForDepartmentAppointment(Appointment appointment,
                                                          IntegrationBackendRequestDTO integrationRequestDTO);

    BackendIntegrationApiInfo getAppointmentModeApiIntegration(IntegrationBackendRequestDTO backendRequestDTO,
                                                               Long appointmentModeId,
                                                               String generatedHmacKey);

    ThirdPartyResponse processEsewaRefundRequest(Appointment appointment,
                                                 AppointmentTransactionDetail transactionDetail,
                                                 AppointmentRefundDetail appointmentRefundDetail,
                                                 Boolean isRefund,
                                                 IntegrationRefundRequestDTO refundRequestDTO);

    void apiIntegrationCheckpointForRefundAppointment(Appointment appointment,
                                                      AppointmentTransactionDetail appointmentTransactionDetail,
                                                      AppointmentRefundDetail refundAppointmentDetail,
                                                      IntegrationRefundRequestDTO refundRequestDTO);

    void apiIntegrationCheckpointForRejectAppointment(Appointment appointment,
                                                      AppointmentTransactionDetail appointmentTransactionDetail,
                                                      AppointmentRefundDetail refundAppointmentDetail,
                                                      IntegrationRefundRequestDTO refundRequestDTO);
}
