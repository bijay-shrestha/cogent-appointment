package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ThirdPartyConnectorService {

    ResponseEntity<?> callThirdPartyDoctorAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo);

    ResponseEntity<?> callThirdPartyHospitalDepartmentAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                                                                ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO checkInDTO);

    ResponseEntity<?> callEsewaRefundService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                             Map<String, Object> map);

    ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo);

    ResponseEntity<?> callEsewaRefundStatusService(BackendIntegrationApiInfo integrationApiInfo,
                                                   Map<String, Object> map);

    String hmacForFrontendIntegration(String esewaId,String merchantCode);
}
