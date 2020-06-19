package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentthirdpartyconnector.request.EsewaPayementStatus;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import org.springframework.http.ResponseEntity;

public interface ThirdPartyConnectorService {

    ResponseEntity<?> callThirdPartyDoctorAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo);

    ResponseEntity<?> callThirdPartyHospitalDepartmentAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                                                                ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO checkInDTO);

    ThirdPartyResponse callEsewaRefundService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                              EsewaRefundRequestDTO esewaRefundRequestDTO);

    ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo);

    ThirdPartyResponse callEsewaRefundStatusService(BackendIntegrationApiInfo integrationApiInfo,
                                                    EsewaPayementStatus esewaPayementStatus);
}
