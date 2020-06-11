package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ClientSaveRequestDTO;
import com.cogent.cogentappointment.client.dto.request.clientIntegration.Dummy;
import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.DummyMessage;
import com.cogent.cogentappointment.client.utils.commons.ObjectMapperUtils;
import com.cogent.cogentappointment.client.utils.resttempalte.RestTemplateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminConstant.SAVE_OPERATION;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.IntegrationConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.IntegrationConstants.BASE_INTEGRATION;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestHeaders.*;
import static com.cogent.cogentappointment.client.utils.resttempalte.IntegrationRequestURI.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak on 2020-05-18
 */
@RestController
@RequestMapping(API_V1 + BASE_INTEGRATION)
@Api(BASE_API_VALUE)
public class IntegrationClientResource {

    private final RestTemplateUtils restTemplateUtils;

    public IntegrationClientResource(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }


    @PostMapping("/esewa")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> savetestEsewa() throws IOException {


        ResponseEntity<?> response = restTemplateUtils.getRequest(ESEWA_API_AUTHENTICATE,
                new HttpEntity<>(getEsewaAPIHeaders()));

        Dummy dummy=ObjectMapperUtils.map(response.getBody().toString(),Dummy.class);

        System.out.println(dummy.getUuid());
        System.out.println(dummy.getDevice_unique_id());
        System.out.println(dummy.getModule());

        return ok().build();
    }

    @PostMapping("/bheri/postticket")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> postticket() throws IOException {

        //request Object
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

        ResponseEntity<?> response = restTemplateUtils.
                postRequest(BHERI_HOSPITAL_POST_TICKET,
                        new HttpEntity<>(saveRequestDTO, getBheriAPIHeaders()));

        System.out.println(response);

        return new ResponseEntity<>(response.getBody(), OK);
    }

    @GetMapping("/bheri/room")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> room() throws IOException {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BHERI_HOSPITAL_GET_MASTER_DATA)
                .queryParam("type", "room");

        ResponseEntity<?> response = restTemplateUtils.getRequest(builder.toUriString(),
                new HttpEntity<>(getBheriAPIHeaders()));

        System.out.println(response);

        return new ResponseEntity<>(response.getBody(), OK);
    }

    @PostMapping("/esewa/paymentstatus")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> paymentStatus(@RequestBody EsewaPayementStatus esewaPayementStatus) throws IOException {

        HttpEntity<?> request = new HttpEntity<>(esewaPayementStatus, getEsewaPaymentStatusAPIHeaders());

        ResponseEntity<DummyMessage> response = (ResponseEntity<DummyMessage>) restTemplateUtils
                .postRequest(ESEWA_API_PAYMENT_STATUS, request);

        System.out.println(response.getBody().getError_message());

        return ok().build();
    }


    @GetMapping("/cogent/patient-profile")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> cogentPatient() throws IOException {

        final String getUri = "https://cogent-uat.cogenthealth.com.np/api/patient-profile/{id}";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);


        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate
                .exchange(getUri, HttpMethod.GET, request, String.class, 1000);

        System.out.println(response);

        return new ResponseEntity<>(response.getBody(), OK);
    }

}
