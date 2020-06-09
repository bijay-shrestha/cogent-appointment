package com.cogent.cogentthirdpartyconnector.service;

import com.cogent.cogentthirdpartyconnector.service.utils.RestTemplateUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.cogent.cogentthirdpartyconnector.api.IntegrationRequestHeaders.getEsewaAPIHeaders;
import static com.cogent.cogentthirdpartyconnector.api.IntegrationRequestURI.ESEWA_API_AUTHENTICATE;

/**
 * @author rupak ON 2020/06/09-11:41 AM
 */
@Service
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final RestTemplateUtils restTemplateUtils;

    public ThirdPartyServiceImpl(RestTemplateUtils restTemplateUtils) {
        this.restTemplateUtils = restTemplateUtils;
    }

    @Override
    public String getHospitalService() {

        ResponseEntity<?> response = restTemplateUtils.getRequest(ESEWA_API_AUTHENTICATE,
                new HttpEntity<>(getEsewaAPIHeaders()));


        String hospitalService = "This is hospital third party service";

        return hospitalService;

    }
}
