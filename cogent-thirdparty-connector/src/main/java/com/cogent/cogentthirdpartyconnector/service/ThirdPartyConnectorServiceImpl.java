package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentthirdpartyconnector.request.ClientSaveRequestDTO;
import com.cogent.cogentthirdpartyconnector.request.EsewaRefundRequestDTO;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.BackendIntegrationApiInfo;
import com.cogent.cogentthirdpartyconnector.response.integrationBackend.integrationThirdParty.ThirdPartyResponseDTO;
import com.cogent.cogentthirdpartyconnector.service.utils.RestTemplateUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.cogent.cogentthirdpartyconnector.utils.HttpMethodUtils.getHttpRequestMethod;
import static com.cogent.cogentthirdpartyconnector.utils.ObjectMapperUtils.map;
import static com.cogent.cogentthirdpartyconnector.utils.QueryParameterUtils.createQueryPamarameter;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

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
    public ThirdPartyResponseDTO getEsewaService(BackendIntegrationApiInfo hospitalApiInfo,
                                                 EsewaRefundRequestDTO esewaRefundRequestDTO) throws IOException {

        HttpMethod httpMethod = getHttpRequestMethod(hospitalApiInfo.getHttpMethod());

        String uri = "";
        if ((httpMethod == GET) || (httpMethod == POST)) {

            Map<String, String> queryParameter = hospitalApiInfo.getQueryParameters();
            if (queryParameter != null) {
                uri = createQueryPamarameter(hospitalApiInfo.getApiUri(), queryParameter).toUriString();
            } else {
                uri = hospitalApiInfo.getApiUri();
            }
        } else {
            uri = hospitalApiInfo.getApiUri();
        }

        ResponseEntity<?> response = restTemplateUtils.
                requestAPI(httpMethod,
                        uri,
                        new HttpEntity<>(esewaRefundRequestDTO, hospitalApiInfo.getHttpHeaders()));

        System.out.println(response);

        ThirdPartyResponseDTO thirdPartyResponseDTO = map(response.getBody().toString(),
                ThirdPartyResponseDTO.class);


        return thirdPartyResponseDTO;
    }

    @Override
    public ResponseEntity<?> getHospitalService(BackendIntegrationApiInfo hospitalApiInfo) {

        HttpMethod httpMethod = getHttpRequestMethod(hospitalApiInfo.getHttpMethod());

        String uri = "";
        if ((httpMethod == GET) || (httpMethod == POST)) {

            Map<String, String> queryParameter = hospitalApiInfo.getQueryParameters();
            if (!queryParameter.isEmpty()) {
                uri = createQueryPamarameter(hospitalApiInfo.getApiUri(), queryParameter).toUriString();
            }
        } else {
            uri = hospitalApiInfo.getApiUri();
        }

        ResponseEntity<?> response = restTemplateUtils.
                requestAPI(httpMethod,
                        uri,
                        new HttpEntity<>(getApiRequestBody(), hospitalApiInfo.getHttpHeaders()));

        System.out.println(response);

        return response;

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
