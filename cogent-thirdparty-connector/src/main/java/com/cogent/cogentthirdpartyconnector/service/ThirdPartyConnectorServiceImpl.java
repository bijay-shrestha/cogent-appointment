package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentappointment.persistence.model.Appointment;
import com.cogent.cogentthirdpartyconnector.request.ClientSaveRequestDTO;
import com.cogent.cogentthirdpartyconnector.request.EsewaPayementStatus;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationThirdParty.ThirdPartyResponse;
import com.cogent.cogentthirdpartyconnector.service.utils.RestTemplateUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentthirdpartyconnector.utils.HttpMethodUtils.getHttpRequestMethod;
import static com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils.map;
import static com.cogent.cogentthirdpartyconnector.utils.QueryParameterUtils.createQueryParameter;
import static com.cogent.cogentthirdpartyconnector.utils.RequestBodyUtils.getHospitalRequestBody;

/**
 * @author rupak ON 2020/06/09-11:41 AM
 */
@Service
public class ThirdPartyConnectorServiceImpl implements ThirdPartyConnectorService {

    private final RestTemplateUtils restTemplateUtils;

    public ThirdPartyConnectorServiceImpl(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    @Override
    public ResponseEntity<?> callThirdPartyHospitalService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                                           Appointment appointment) {

        ClientSaveRequestDTO clientSaveRequestDTO = getHospitalRequestBody(appointment);


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
                        new HttpEntity<>(clientSaveRequestDTO, backendIntegrationApiInfo.getHttpHeaders()));

        //todo
        //exceptions to be handled

        System.out.println(response);

        return response;
    }

    @Override
    public ThirdPartyResponse callEsewaRefundService(BackendIntegrationApiInfo backendIntegrationApiInfo,
                                                     EsewaRefundRequestDTO esewaRefundRequestDTO) {


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
                        new HttpEntity<>(esewaRefundRequestDTO, backendIntegrationApiInfo.getHttpHeaders()));

        System.out.println(response);

        ThirdPartyResponse thirdPartyResponse = null;
        try {
            thirdPartyResponse = map(response.getBody().toString(),
                    ThirdPartyResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return thirdPartyResponse;
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
    public ThirdPartyResponse callEsewaRefundStatusService(BackendIntegrationApiInfo integrationApiInfo,
                                                           EsewaPayementStatus esewaPayementStatus) {
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
                        new HttpEntity<>(esewaPayementStatus, integrationApiInfo.getHttpHeaders()));

        System.out.println(response);

        ThirdPartyResponse thirdPartyResponse = null;
        try {
            thirdPartyResponse = map(response.getBody().toString(),
                    ThirdPartyResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return thirdPartyResponse;
    }

    private ClientSaveRequestDTO getApiRequestBody() {

        ClientSaveRequestDTO saveRequestDTO = ClientSaveRequestDTO.builder()
                .name("Hari Singh Tharu")
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
                .build();

        return saveRequestDTO;
    }
}
