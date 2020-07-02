package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentthirdpartyconnector.request.EsewaPaymentStatus;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import org.springframework.http.ResponseEntity;

public interface ThirdPartyConnectorService {

    ResponseEntity<?> callThirdPartyDoctorAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo);

    ResponseEntity<?> callThirdPartyHospitalDepartmentAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                                                                ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO checkInDTO);

    ResponseEntity<?> callEsewaRefundService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                              EsewaRefundRequestDTO esewaRefundRequestDTO);

    ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo);

    ResponseEntity<?> callEsewaRefundStatusService(BackendIntegrationApiInfo integrationApiInfo,
                                                    EsewaPaymentStatus esewaPayementStatus);

    String hmacForFrontendIntegration(String esewaId,String merchantCode);
}
