package com.cogent.cogentappointment.client.resource;

import com.cogent.cogentappointment.client.dto.request.clientIntegration.ApiIntegrationCheckInRequestDTO;
import com.cogent.cogentappointment.client.service.IntegrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cogent.cogentappointment.client.constants.SwaggerConstants.IntegrationConstant.BASE_API_VALUE;
import static com.cogent.cogentappointment.client.constants.SwaggerConstants.IntegrationConstant.FETCH_CLIENT_API_INTEGRATION;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.API_V1;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.IntegrationConstants.BASE_INTEGRATION;
import static com.cogent.cogentappointment.client.constants.WebResourceKeyConstants.IntegrationConstants.CLIENT_API_INTEGRATION_APPOINTMENT_APPROVE;
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

}
