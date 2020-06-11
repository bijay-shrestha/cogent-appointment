package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import org.springframework.http.ResponseEntity;

public interface ThirdPartyConnectorService {

    ResponseEntity<?> getEsewaService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                      EsewaRefundRequestDTO esewaRefundRequestDTO);

    ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo);

}
