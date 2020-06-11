package com.cogent.cogentappointment.admin.service.impl;

import com.cogent.cogentappointment.admin.dto.request.integration.IntegrationBackendRequestDTO;
import com.cogent.cogentappointment.admin.dto.response.integrationAdminMode.AdminFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.dto.response.integrationClient.ClientFeatureIntegrationResponse;
import com.cogent.cogentappointment.admin.repository.IntegrationRepository;
import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentappointment.persistence.model.AppointmentRefundDetail;
import com.cogent.cogentappointment.persistence.model.AppointmentTransactionDetail;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import com.cogent.cogentthirdpartyconnector.service.ThirdPartyConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentappointment.admin.security.hmac.HMACUtils.getSignatureForEsewa;
import static com.cogent.cogentthirdpartyconnector.utils.ApiUriUtils.parseApiUri;
import static com.cogent.cogentthirdpartyconnector.utils.HttpHeaderUtils.generateApiHeaders;
import static com.cogent.cogentthirdpartyconnector.utils.RequestBodyUtils.getEsewaRequestBody;

/**
 * @author rupak ON 2020/06/11-5:48 PM
 */
@Service
@Slf4j
public class IntegrationThirdPartyImpl {

    private final ThirdPartyConnectorService thirdPartyConnectorService;
    private final IntegrationRepository integrationRepository;

    public IntegrationThirdPartyImpl(ThirdPartyConnectorService thirdPartyConnectorService,
                                     IntegrationRepository integrationRepository) {
        this.thirdPartyConnectorService = thirdPartyConnectorService;
        this.integrationRepository = integrationRepository;
    }

    public ThirdPartyResponse processEsewaRefundRequest(Appointment appointment,
                                                        AppointmentTransactionDetail transactionDetail,
                                                        AppointmentRefundDetail appointmentRefundDetail,
                                                        Boolean isRefund,
                                                        IntegrationBackendRequestDTO backendRequestDTO) throws IOException {

        String generatedEsewaHmac = getSignatureForEsewa.apply(appointment.getPatientId().getESewaId(),
                appointment.getHospitalId().getEsewaMerchantCode());

        BackendIntegrationApiInfo integrationApiInfo = getAppointmentModeApiIntegration(backendRequestDTO,
                appointment.getAppointmentModeId().getId(), generatedEsewaHmac);

        EsewaRefundRequestDTO esewaRefundRequestDTO = getEsewaRequestBody(appointment,
                transactionDetail,
                appointmentRefundDetail,
                isRefund);

        parseApiUri(integrationApiInfo.getApiUri(), transactionDetail.getTransactionNumber());

        return thirdPartyConnectorService.callEsewaRefundService(integrationApiInfo,
                esewaRefundRequestDTO);

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

        BackendIntegrationApiInfo integrationApiInfo = new BackendIntegrationApiInfo();
        integrationApiInfo.setApiUri(featureIntegrationResponse.getUrl());
        integrationApiInfo.setHttpHeaders(generateApiHeaders(requestHeaderResponse, generatedHmacKey));

        if (!queryParametersResponse.isEmpty()) {
            integrationApiInfo.setQueryParameters(queryParametersResponse);
        }
        integrationApiInfo.setHttpMethod(featureIntegrationResponse.getRequestMethod());

        return integrationApiInfo;

    }
}
