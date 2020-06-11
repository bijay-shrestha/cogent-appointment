package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import org.springframework.http.ResponseEntity;

public interface ThirdPartyConnectorService {

    public ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo);

}
