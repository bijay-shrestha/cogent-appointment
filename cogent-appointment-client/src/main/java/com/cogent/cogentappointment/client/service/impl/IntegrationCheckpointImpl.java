package com.cogent.cogentappointment.client.service.impl;

import com.cogent.cogentappointment.client.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.client.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.FeatureIntegrationResponse;
import com.cogent.cogentappointment.client.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.client.exception.NoContentFoundException;
import com.cogent.cogentappointment.client.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.client.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.client.repository.IntegrationRepository;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.ThirdPartyHospitalResponse;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

import static com.cogent.cogentappointment.client.constants.ErrorMessageConstants.IntegrationApiMessages.*;
import static com.cogent.cogentappointment.client.constants.IntegrationApiConstants.BACK_END_CODE;
import static com.cogent.cogentappointment.client.constants.IntegrationApiConstants.FRONT_END_CODE;
import static com.cogent.cogentthirdpartyconnector.utils.HttpHeaderUtils.generateApiHeaders;
import static com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils.map;
import static java.lang.Integer.parseInt;

/**
 * @author rupak ON 2020/06/17-5:40 PM
 */
@Service
@Transactional
@Slf4j
public class IntegrationCheckpointImpl {

    private final IntegrationRepository integrationRepository;

    private final ThirdPartyConnectorService thirdPartyConnectorService;

    private final HospitalPatientInfoRepository hospitalPatientInfoRepository;

    public IntegrationCheckpointImpl(IntegrationRepository integrationRepository,
                                     ThirdPartyConnectorService thirdPartyConnectorService,
                                     HospitalPatientInfoRepository hospitalPatientInfoRepository) {
        this.integrationRepository = integrationRepository;
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.hospitalPatientInfoRepository = hospitalPatientInfoRepository;
    }

    public void apiIntegrationCheckpoint(Appointment appointment,
                                         IntegrationBackendRequestDTO integrationRequestDTO) {

        //front integration
        if (integrationRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(FRONT_END_CODE)) {

            if (integrationRequestDTO.isPatientStatus()) {
                updateHospitalPatientInfo(appointment, integrationRequestDTO.getHospitalNumber());
            }
        }

        //backend integration
        if (integrationRequestDTO.getIntegrationChannelCode().equalsIgnoreCase(BACK_END_CODE)) {

            ThirdPartyHospitalResponse thirdPartyHospitalResponse = hospitalIntegrationCheckpoint(integrationRequestDTO, appointment);

            if (integrationRequestDTO.isPatientStatus()) {
                updateHospitalPatientInfo(appointment, thirdPartyHospitalResponse.getResponseData());
            }
        }
    }

    private void updateHospitalPatientInfo(Appointment appointment, String hospitalNumber) {

        HospitalPatientInfo hospitalPatientInfo = hospitalPatientInfoRepository.
                findByPatientAndHospitalId(appointment.getPatientId().getId(), appointment.getHospitalId().getId())
                .orElseThrow(() -> HOSPITAL_PATIENT_INFO_NOT_FOUND.apply(appointment.getPatientId().getId()));

        hospitalPatientInfo.setHospitalNumber(hospitalNumber);
    }

    private ThirdPartyHospitalResponse hospitalIntegrationCheckpoint(IntegrationBackendRequestDTO integrationBackendRequestDTO,
                                                                     Appointment appointment) {

        BackendIntegrationApiInfo integrationHospitalApiInfo = getHospitalApiIntegration(integrationBackendRequestDTO);

        //dynamic requestBOdy
        //Esewa
        //call thirdparty requestbody utils if not create one.....

        ResponseEntity<?> responseEntity = thirdPartyConnectorService.
                callThirdPartyHospitalService(integrationHospitalApiInfo, appointment);

        if (responseEntity.getStatusCode().value() == 403) {
            throw new OperationUnsuccessfulException(INTEGRATION_BHERI_HOSPITAL_FORBIDDEN_ERROR);
        }

        ThirdPartyHospitalResponse thirdPartyHospitalResponse = null;
        try {
            thirdPartyHospitalResponse = map(responseEntity.getBody().toString(),
                    ThirdPartyHospitalResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        validateBheriHospitalResponse(thirdPartyHospitalResponse);

        return thirdPartyHospitalResponse;
    }

    private BackendIntegrationApiInfo getHospitalApiIntegration(IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        FeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchClientIntegrationResponseDTOforBackendIntegration(integrationBackendRequestDTO);

        Map<String, String> requestHeaderResponse = integrationRepository.
                findApiRequestHeaders(featureIntegrationResponse.getApiIntegrationFormatId());

        Map<String, String> queryParametersResponse = integrationRepository.
                findApiQueryParameters(featureIntegrationResponse.getApiIntegrationFormatId());

        //headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                        " Chrome/54.0.2840.99 Safari/537.36");

        requestHeaderResponse.forEach((key, value) -> {
            headers.add(key, value);
        });

        BackendIntegrationApiInfo backendIntegrationApiInfo = new BackendIntegrationApiInfo();
        backendIntegrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        backendIntegrationApiInfo.setHttpHeaders(headers);

        if (!queryParametersResponse.isEmpty())
            backendIntegrationApiInfo.setQueryParameters(queryParametersResponse);

        backendIntegrationApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

        return backendIntegrationApiInfo;
    }


    public BackendIntegrationApiInfo getAppointmentModeApiIntegration(IntegrationRefundRequestDTO refundRequestDTO,
                                                                      Long appointmentModeId,
                                                                      String generatedHmacKey) {

        AdminFeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchAppointmentModeIntegrationResponseDTOforBackendIntegration(refundRequestDTO,
                        appointmentModeId);

        Map<String, String> requestHeaderResponse = integrationRepository.
                findAdminModeApiRequestHeaders(featureIntegrationResponse.getApiIntegrationFormatId());

        Map<String, String> queryParametersResponse = integrationRepository.
                findAdminModeApiQueryParameters(featureIntegrationResponse.getApiIntegrationFormatId());

//        List<String> requestBody = getRequestBodyByFeature(featureIntegrationResponse.getFeatureId(),
//                featureIntegrationResponse.getRequestMethod());

        BackendIntegrationApiInfo integrationApiInfo = new BackendIntegrationApiInfo();
        integrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        integrationApiInfo.setHttpHeaders(generateApiHeaders(requestHeaderResponse, generatedHmacKey));
//        integrationApiInfo.setRequestBody(requestBody);

        if (!queryParametersResponse.isEmpty()) {
            integrationApiInfo.setQueryParameters(queryParametersResponse);
        }
        integrationApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

        return integrationApiInfo;

    }

//    private List<String> getRequestBodyByFeature(Long featureId, String requestMethod) {
//
//        List<String> requestBody = null;
//        if (requestMethod.equalsIgnoreCase("POST")) {
//            List<IntegrationRequestBodyAttributeResponse> responses = integrationRepository.
//                    fetchRequestBodyAttributeByFeatureId(featureId);
//
//            if (responses != null) {
//                requestBody = responses.stream()
//                        .map(request -> request.getName())
//                        .collect(Collectors.toList());
//            }
//
//        }
//
//
//        return requestBody;
//    }

    public void validateBheriHospitalResponse(ThirdPartyHospitalResponse thirdPartyHospitalResponse) {

        if (parseInt(thirdPartyHospitalResponse.getStatusCode()) == 500) {
            throw new OperationUnsuccessfulException(INTEGRATION_BHERI_HOSPITAL_ERROR);
        }

        if (parseInt(thirdPartyHospitalResponse.getStatusCode()) == 403) {
            throw new OperationUnsuccessfulException(INTEGRATION_BHERI_HOSPITAL_FORBIDDEN_ERROR);
        }

        if (parseInt(thirdPartyHospitalResponse.getStatusCode()) == 400) {
            throw new OperationUnsuccessfulException(INTEGRATION_API_BAD_REQUEST);
        }

    }

    private Function<Long, NoContentFoundException> HOSPITAL_PATIENT_INFO_NOT_FOUND = (id) -> {
        throw new NoContentFoundException(HospitalPatientInfo.class);
    };


}
