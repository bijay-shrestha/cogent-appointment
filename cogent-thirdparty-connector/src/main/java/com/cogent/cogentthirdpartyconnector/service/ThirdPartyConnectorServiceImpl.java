package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentappointment.commons.dto.request.thirdparty.ThirdPartyHospitalDepartmentWiseAppointmentCheckInDTO;
import com.cogent.cogentappointment.commons.exception.OperationUnsuccessfulException;
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
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentthirdpartyconnector.utils.HttpMethodUtils.getHttpRequestMethod;
import static com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils.map;
import static com.cogent.cogentthirdpartyconnector.utils.QueryParameterUtils.createQueryParameter;
import static org.springframework.http.HttpStatus.OK;

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
    public ResponseEntity<?> callThirdPartyDoctorAppointmentCheckInService(BackendIntegrationApiInfo backendIntegrationApiInfo) {

        HttpMethod httpMethod = getHttpRequestMethod(backendIntegrationApiInfo.getHttpMethod());

        String uri = getHospitalDeptCheckInQueryParameter(backendIntegrationApiInfo);

        ResponseEntity<?> response = restTemplateUtils.
                requestAPI(httpMethod,
                        uri,
                        new HttpEntity<>(getApiRequestBody(), backendIntegrationApiInfo.getHttpHeaders()));

        //todo
        //exceptions to be handled

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

        ResponseEntity<?> response = null;
        try {
            response = restTemplateUtils.
                    requestAPI(httpMethod,
                            uri,
                            new HttpEntity<>(esewaRefundRequestDTO, backendIntegrationApiInfo.getHttpHeaders()));
        } catch (HttpStatusCodeException exception) {

            String message = "";
            String code = "";
            String status = "";
            if (exception.getStatusCode().value() == 400) {
                code = "400";
                message = "Bad Request";
                status = "ERROR";
            }

            if (exception.getStatusCode().value() == 404) {
                code = "404";
                message = "Not Found";
                status = "ERROR";
            }

            if (exception.getStatusCode().value() == 403) {
                code = "403";
                message = "Forbidden";
                status = "ERROR";
            }

            ThirdPartyResponse thirdPartyResponse = ThirdPartyResponse.builder()
                    .message(message)
                    .code(code)
                    .status(status).build();

            response = new ResponseEntity<>(thirdPartyResponse, OK);

        }


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
