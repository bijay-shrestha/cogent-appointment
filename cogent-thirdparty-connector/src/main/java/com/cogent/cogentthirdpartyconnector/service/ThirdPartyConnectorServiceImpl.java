package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.commons.exception.OperationUnsuccessfulException;
import com.cogent.cogentthirdpartyconnector.request.ClientSaveRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.service.utils.RestTemplateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Map;

import static com.cogent.cogentappointment.commons.utils.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentappointment.commons.utils.DateUtils.getTimeInMillisecondsFromLocalDate;
import static com.cogent.cogentthirdpartyconnector.log.constants.HmacLog.GENERATING_HMAC_FOR_FRONTEND_PROCESS_COMPLETED;
import static com.cogent.cogentthirdpartyconnector.log.constants.HmacLog.GENERATING_HMAC_FOR_FRONTEND_PROCESS_STARTED;
import static com.cogent.cogentthirdpartyconnector.utils.HMACUtils.getSigatureForEsewa;
import static com.cogent.cogentthirdpartyconnector.utils.HttpMethodUtils.getHttpRequestMethod;
import static com.cogent.cogentthirdpartyconnector.utils.QueryParameterUtils.createQueryParameter;

/**
 * @author rupak ON 2020/06/09-11:41 AM
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class ThirdPartyConnectorServiceImpl implements ThirdPartyConnectorService {

    private final RestTemplateUtils restTemplateUtils;

    public ThirdPartyConnectorServiceImpl(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    @Override
    public ResponseEntity<?> callThirdPartyDoctorAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo) {

        HttpMethod httpMethod = getHttpRequestMethod(backendIntegrationApiInfo.getHttpMethod());

        String uri = getHospitalDeptCheckInQueryParameter(backendIntegrationApiInfo);

        ResponseEntity<?> response = restTemplateUtils.
                requestAPI(httpMethod,
                        uri,
                        new HttpEntity<>(getApiRequestBody(), backendIntegrationApiInfo.getHttpHeaders()));

        System.out.println(response);

        return response;
    }

    @Override
    public ResponseEntity<?> callThirdPartyHospitalDepartmentAppointmentCheckInService(
            BackendIntegrationApiInfo backendIntegrationApiInfo,
            ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO checkInDTO) {

        HttpMethod httpMethod = getHttpRequestMethod(backendIntegrationApiInfo.getHttpMethod());

        String uri = getHospitalDeptCheckInQueryParameter(backendIntegrationApiInfo);

        try {

            ResponseEntity<?> response = restTemplateUtils.requestAPI(
                    httpMethod,
                    uri,
                    new HttpEntity<>(checkInDTO, backendIntegrationApiInfo.getHttpHeaders())
            );

            return response;
        } catch (HttpStatusCodeException exception) {
            exception.printStackTrace();
            throw new OperationUnsuccessfulException(exception.getResponseBodyAsString());
        }
    }

    @Override
    public ResponseEntity<?> callEsewaRefundService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                                    String encrptedRequestBody) {

        HttpMethod httpMethod = getHttpRequestMethod(backendIntegrationApiInfo.getHttpMethod());

        String uri = "";
        Map<String, String> queryParameter = backendIntegrationApiInfo.getQueryParameters();
        if (queryParameter != null) {
            uri = createQueryParameter(backendIntegrationApiInfo.getApiUri(), queryParameter).toUriString();
        } else {
            uri = backendIntegrationApiInfo.getApiUri();
        }

        ResponseEntity<?> response = restTemplateUtils.
                requestAPI(httpMethod,
                        uri,
                        new HttpEntity<>(encrptedRequestBody, backendIntegrationApiInfo.getHttpHeaders()));

        return response;

    }

    @Override
    public ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo integrationApiInfo) {

        HttpMethod httpMethod = getHttpRequestMethod(integrationApiInfo.getHttpMethod());

        String uri = "";
        Map<String, String> queryParameter = integrationApiInfo.getQueryParameters();

        if (queryParameter != null) {
            uri = createQueryParameter(integrationApiInfo.getApiUri(), queryParameter).toUriString();
        } else {
            uri = integrationApiInfo.getApiUri();
        }

        ResponseEntity<?> response = restTemplateUtils.
                requestAPI(httpMethod,
                        uri,
                        new HttpEntity<>(getApiRequestBody(), integrationApiInfo.getHttpHeaders()));

        System.out.println(response);

        return response;
    }

    @Override
    public ResponseEntity<?> callEsewaRefundStatusService(BackendIntegrationApiInfo integrationApiInfo,
                                                          String encryptedRequestBody) {
        HttpMethod httpMethod = getHttpRequestMethod(integrationApiInfo.getHttpMethod());

        String uri = "";
        Map<String, String> queryParameter = integrationApiInfo.getQueryParameters();

        if (queryParameter != null) {
            uri = createQueryParameter(integrationApiInfo.getApiUri(), queryParameter).toUriString();
        } else {
            uri = integrationApiInfo.getApiUri();
        }

        ResponseEntity<?> response = restTemplateUtils.
                requestAPI(httpMethod,
                        uri,
                        new HttpEntity<>(encryptedRequestBody, integrationApiInfo.getHttpHeaders()));

        System.out.println(response);

        return response;
    }

    @Override
    public String hmacForFrontendIntegration(String esewaId, String merchantCode) {

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(GENERATING_HMAC_FOR_FRONTEND_PROCESS_STARTED);

        String hmac = getSigatureForEsewa.apply(esewaId, merchantCode);

        log.info(GENERATING_HMAC_FOR_FRONTEND_PROCESS_COMPLETED, getDifferenceBetweenTwoTime(startTime));

        return hmac;
    }

    private String getHospitalDeptCheckInQueryParameter(BackendIntegrationApiInfo backendIntegrationApiInfo) {

        String uri = "";

        Map<String, String> queryParameter = backendIntegrationApiInfo.getQueryParameters();

        if (queryParameter != null) {
            uri = createQueryParameter(backendIntegrationApiInfo.getApiUri(), queryParameter).toUriString();
        } else {
            uri = backendIntegrationApiInfo.getApiUri();
        }

        return uri;
    }

    private ClientSaveRequestDTO getApiRequestBody() {

        ClientSaveRequestDTO saveRequestDTO = ClientSaveRequestDTO.builder()
                .name("Smriti Mool")
                .age(20)
                .ageMonth(1)
                .ageDay(3)
                .sex("Male")
                .district("Kathmandu")
                .vdc("Kathmandu Metro")
                .wardNo("5")
                .address("Samakhushi")
                .phoneNo("01-4212345")
                .mobileNo("9800000000")
                .emailAddress("abc@fakemail.com")
                .section("ENT")
                .roomNo("10")
                .appointmentNo("BH-12354-90")
                .patientId(null)
                .build();

        return saveRequestDTO;
    }
}
