package com.cogent.cogentthirdpartyconnector.service.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

/**
 * @author rupak on 2020-05-24
 */
@Component
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

            if (exception.getStatusCode().value() == 400) {
                response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

            }

            if (exception.getStatusCode().value() == 404) {
                response = new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }

            if (exception.getStatusCode().value() == 403) {
                response = new ResponseEntity<>(HttpStatus.FORBIDDEN);

            }


            System.out.println("Response: " + exception.getStatusCode().value());


        }

        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
}
