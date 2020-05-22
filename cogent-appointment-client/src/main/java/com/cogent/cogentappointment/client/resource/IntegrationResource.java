package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationCheckInRequestDTO;
import com.cogent.cogentappointment.client.dto.request.clientIntegration.ClientSaveRequestDTO;
import com.cogent.cogentappointment.client.dto.request.clientIntegration.Dummy;
import com.cogent.cogentappointment.client.dto.request.clientIntegration.EsewaPayementStatus;
import com.cogent.cogentappointment.client.dto.response.clientIntegration.DummyMessage;
import com.cogent.cogentappointment.client.service.IntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.AdminConstant.SAVE_OPERATION;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.IntegrationConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.IntegrationConstant.FETCH_CLIENT_API_INTEGRATION;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.IntegrationConstants.BASE_INTEGRATION;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.IntegrationConstants.CLIENT_API_INTEGRATION_APPOINTMENT_APPROVE;
import static java.net.URI.create;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

/**
 * @author rupak on 2020-05-18
 */
@RestController
@RequestMapping(API_V1 + BASE_INTEGRATION)
@Api(BASE_API_VALUE)
public class IntegrationResource {

    private final IntegrationService integrationService;

    public IntegrationResource(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @PutMapping(CLIENT_API_INTEGRATION_APPOINTMENT_APPROVE)
    @ApiOperation(FETCH_CLIENT_API_INTEGRATION)
    public ResponseEntity<?> approveAppointmentCheckIn(@Valid @RequestBody ApiIntegrationCheckInRequestDTO requestDTO) {
        integrationService.approveAppointmentCheckIn(requestDTO);
        return ok().build();
    }

    @PostMapping("/client/save")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> save(@RequestBody ClientSaveRequestDTO clientSaveRequestDTO) throws IOException {

        return created(create(API_V1 + "/client/save")).build();
    }


    @PostMapping("/esewa")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> savetestEsewa() throws IOException {

        final String uri = "https://rc.esewa.com.np/api/gprs/authenticate/v1";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        headers.add("esewa_id", "9841409090");
        headers.add("password", "dGVzdEAxMjM0");
        headers.add("device_unique_id", "b91bb1c3-43ac-4f97-846b-42adcf6fad11");

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<Dummy> response = restTemplate
                .exchange(uri, HttpMethod.POST, request, Dummy.class);

        System.out.println(response.getBody().getUuid());
        System.out.println(response.getBody().getDevice_unique_id());
        System.out.println(response.getBody().getModule());

        return ok().build();
    }

    @PostMapping("/bheri/postticket")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> postticket() throws IOException {

        final String uri = "https://eticketbheri.softechnp.com/apiv1/postticket";

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", "DE26851D-AF4D-4CE7-9250-CCC2C9A728C9");
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");


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


        HttpEntity<?> request = new HttpEntity<>(saveRequestDTO, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<?> response = restTemplate
                .exchange(uri, HttpMethod.POST, request, String.class);

        System.out.println(response);

        return new ResponseEntity<>(response.getBody(),OK);
    }

    @GetMapping("/bheri/room")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> room() throws IOException {

        final String getUri = "https://eticketbheri.softechnp.com/apiv1/getmasterdata";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("token", "DE26851D-AF4D-4CE7-9250-CCC2C9A728C9");
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getUri)
                .queryParam("type", "room");

        HttpEntity<?> request = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate
                .exchange(builder.toUriString(), HttpMethod.GET, request, String.class);

        System.out.println(response);

        return new ResponseEntity<>(response.getBody(),OK);
    }


    @PostMapping("/esewa/paymentstatus")
    @ApiOperation(SAVE_OPERATION)
    public ResponseEntity<?> payemntStatus(@RequestBody EsewaPayementStatus esewaPayementStatus) throws IOException {

        final String uri = "https://rc.esewa.com.np/api/esewa/client/payment/status";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        headers.add("signature",
                "a531bcd75978687aa2beb52c94b2fb1fc2e31070c95d12a2da3d8c44cbf9a67b2c9f1da3f99f1f1cdb6605d60ad6dd5b3d8c2d15576e9b92bef5e3caf9d8e65b");


        HttpEntity<?> request = new HttpEntity<>(esewaPayementStatus, headers);

        ResponseEntity<DummyMessage> response = restTemplate
                .exchange(uri, HttpMethod.POST, request, DummyMessage.class);

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
