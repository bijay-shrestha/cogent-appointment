package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface ThirdPartyConnectorService {

    ThirdPartyResponse callEsewaRefundService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                              Map<String, Object> esewaRefundRequestDTO) throws IOException;

    ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo);

}
