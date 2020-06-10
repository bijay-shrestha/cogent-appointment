package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationHospitalApiInfo;
import org.springframework.http.ResponseEntity;

public interface ThirdPartyService {

    public ResponseEntity<?> getHospitalService(BackendIntegrationHospitalApiInfo backendIntegrationHospitalApiInfo);

}
