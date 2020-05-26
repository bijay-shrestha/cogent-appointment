package com.cogent.cogentappointment.client.utils.resttempalte;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author rupak on 2020-05-24
 */
@Component
public class RestTemplateUtils {

    private RestTemplate restTemplate;

    public RestTemplateUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> getRequest(String uri, HttpEntity<?> request) {

        ResponseEntity<?> response = restTemplate
                .exchange(uri,
                        GET,
                        request,
                        String.class);

        System.out.println(response);

        return new ResponseEntity<>(response.getBody(), OK);
    }




    public ResponseEntity<?> postRequest(String uri, HttpEntity<?> request) {

        ResponseEntity<?> response = null;
        try {
            response = restTemplate.exchange(uri,
                    POST,
                    request,
                    String.class);
        } catch (HttpStatusCodeException exception) {
            System.out.println("Response: " + exception.getStatusCode().value());
        }

        return response;
    }


}
