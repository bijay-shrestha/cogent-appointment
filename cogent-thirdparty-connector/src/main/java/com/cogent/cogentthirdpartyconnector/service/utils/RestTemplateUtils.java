package com.cogent.cogentthirdpartyconnector.service.utils;

import com.cogent.cogentappointment.commons.exception.OperationUnsuccessfulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static com.cogent.cogentthirdpartyconnector.utils.ExceptionUtils.exceptionHandler;

/**
 * @author rupak on 2020-05-24
 */
@Component
@Slf4j
public class RestTemplateUtils {

    private final RestTemplate restTemplate;

    public RestTemplateUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> requestAPI(HttpMethod httpMethod,
                                        String uri,
                                        HttpEntity<?> request) {

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(uri,
                    httpMethod,
                    request,
                    String.class);
        } catch (HttpStatusCodeException exception) {

            System.out.println("Response: " + exception.getStatusCode().value());
            exceptionHandler(httpMethod,
                    request,
                    uri,
                    exception);

        }

        if(response.getBody()==null){
            throw new OperationUnsuccessfulException("ThirdParty API response is null");
        }

        if (response != null) {
            return new ResponseEntity<>(response.getBody(), response.getStatusCode());
        }

        throw new OperationUnsuccessfulException("ThirdParty API response is null");

    }
}
