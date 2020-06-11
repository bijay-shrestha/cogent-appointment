package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface ThirdPartyConnectorService {

    ThirdPartyResponse getEsewaService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                       EsewaRefundRequestDTO esewaRefundRequestDTO) throws IOException;

    ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo);

}
