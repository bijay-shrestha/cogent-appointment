package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationRefundRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integration.IntegrationRequestBodyAttributeResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.exception.NoContentFoundException;
import com.cogent.cogentappointment.admin.exception.OperationUnsuccessfulException;
import com.cogent.cogentappointment.admin.repository.HospitalPatientInfoRepository;
import com.cogent.cogentappointment.admin.repository.IntegrationRepository;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentappointment.persistence.model.HospitalPatientInfo;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.ThirdPartyHospitalResponse;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.cogent.cogentappointment.admin.constants.ErrorMessageConstants.IntegrationApiMessages.*;
import static com.cogent.cogentappointment.admin.constants.IntegrationApiConstants.BACK_END_CODE;
import static com.cogent.cogentappointment.admin.constants.IntegrationApiConstants.FRONT_END_CODE;
import static com.cogent.cogentappointment.admin.security.hmac.HMACUtils.getSignatureForEsewa;
import static com.cogent.cogentappointment.admin.utils.commons.ObjectMapperUtils.map;
import static com.cogent.cogentthirdpartyconnector.utils.ApiUriUtils.parseApiUri;
import static com.cogent.cogentthirdpartyconnector.utils.HttpHeaderUtils.generateApiHeaders;
import static com.cogent.cogentthirdpartyconnector.utils.RequestBodyUtils.getEsewaRequestBody;
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

            ThirdPartyHospitalResponse thirdPartyHospitalResponse = apiIntegrationCheckpoint(integrationRequestDTO);

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

    public ThirdPartyHospitalResponse apiIntegrationCheckpoint(IntegrationBackendRequestDTO integrationBackendRequestDTO) {

        BackendIntegrationApiInfo integrationHospitalApiInfo = getHospitalApiIntegration(integrationBackendRequestDTO);
        ResponseEntity<?> responseEntity = thirdPartyConnectorService.getHospitalService(integrationHospitalApiInfo);

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

    public BackendIntegrationApiInfo getHospitalApiIntegration(IntegrationBackendRequestDTO backendRequestDTO) {

        ClientFeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchClientIntegrationResponseDTOforBackendIntegration(backendRequestDTO);

        Map<String, String> requestHeaderResponse = integrationRepository.
                findApiRequestHeadersResponse(featureIntegrationResponse.getApiIntegrationFormatId());

        Map<String, String> queryParametersResponse = integrationRepository.
                findApiQueryParametersResponse(featureIntegrationResponse.getApiIntegrationFormatId());

        BackendIntegrationApiInfo hospitalApiInfo = new BackendIntegrationApiInfo();
        hospitalApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        hospitalApiInfo.setHttpHeaders(generateApiHeaders(requestHeaderResponse, null));

        if (!queryParametersResponse.isEmpty()) {
            hospitalApiInfo.setQueryParameters(queryParametersResponse);
        }
        hospitalApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

        return hospitalApiInfo;

    }

    public BackendIntegrationApiInfo getAppointmentModeApiIntegration(IntegrationBackendRequestDTO backendRequestDTO,
                                                                      Long appointmentModeId,
                                                                      String generatedHmacKey) {

        AdminFeatureIntegrationResponse featureIntegrationResponse = integrationRepository.
                fetchAppointmentModeIntegrationResponseDTOforBackendIntegration(backendRequestDTO,
                        appointmentModeId);

        Map<String, String> requestHeaderResponse = integrationRepository.
                findAdminModeApiRequestHeaders(featureIntegrationResponse.getApiIntegrationFormatId());

        Map<String, String> queryParametersResponse = integrationRepository.
                findAdminModeApiQueryParameters(featureIntegrationResponse.getApiIntegrationFormatId());

        List<String> requestBody = getRequestBodyByFeature(featureIntegrationResponse.getFeatureId(),
                featureIntegrationResponse.getRequestMethod());

        BackendIntegrationApiInfo integrationApiInfo = new BackendIntegrationApiInfo();
        integrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        integrationApiInfo.setHttpHeaders(generateApiHeaders(requestHeaderResponse, generatedHmacKey));
        integrationApiInfo.setRequestBody(requestBody);

        if (!queryParametersResponse.isEmpty()) {
            integrationApiInfo.setQueryParameters(queryParametersResponse);
        }
        integrationApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

        return integrationApiInfo;

    }

    public ThirdPartyResponse processEsewaRefundRequest(Appointment appointment,
                                                        AppointmentTransactionDetail transactionDetail,
                                                        AppointmentRefundDetail appointmentRefundDetail,
                                                        Boolean isRefund,
                                                        IntegrationRefundRequestDTO refundRequestDTO) {

        String generatedEsewaHmac = getSignatureForEsewa.apply(appointment.getPatientId().getESewaId(),
                appointment.getHospitalId().getEsewaMerchantCode());

        IntegrationBackendRequestDTO integrationBackendRequestDTO=IntegrationBackendRequestDTO.builder()
                .appointmentId(refundRequestDTO.getAppointmentId())
                .featureCode(refundRequestDTO.getFeatureCode())
                .integrationChannelCode(refundRequestDTO.getIntegrationChannelCode())
                .build();

        BackendIntegrationApiInfo integrationApiInfo = getAppointmentModeApiIntegration(integrationBackendRequestDTO,
                appointment.getAppointmentModeId().getId(), generatedEsewaHmac);

        EsewaRefundRequestDTO esewaRefundRequestDTO = getEsewaRequestBody(appointment,
                transactionDetail,
                appointmentRefundDetail,
                isRefund);

//        Map<String, Object> esewaRefundRequestDTO = getDynamicEsewaRequestBodyLog(
//                integrationApiInfo.getRequestBody(),
//                appointment,
//                transactionDetail,
//                appointmentRefundDetail,
//                isRefund);

        integrationApiInfo.setApiUri(parseApiUri(integrationApiInfo.getApiUri(), transactionDetail.getTransactionNumber()));

        return thirdPartyConnectorService.callEsewaRefundService(integrationApiInfo,
                esewaRefundRequestDTO);
    }

    private List<String> getRequestBodyByFeature(Long featureId, String requestMethod) {

        List<String> requestBody = null;
        if (requestMethod.equalsIgnoreCase("POST")) {
            List<IntegrationRequestBodyAttributeResponse> responses = integrationRepository.
                    fetchRequestBodyAttributeByFeatureId(featureId);

            if (responses != null) {
                requestBody = responses.stream()
                        .map(request -> request.getName())
                        .collect(Collectors.toList());
            }

        }


        return requestBody;
    }

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
