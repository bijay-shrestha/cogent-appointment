package com.cogent.cogentthirdpartyconnector.service.utils;

import com.cogent.cogentappointment.commons.exception.OperationUnsuccessfulException;
import com.cogent.cogentthirdpartyconnector.log.CommonLogConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static com.cogent.cogentthirdpartyconnector.log.CommonLogConstant.*;
import static com.cogent.cogentthirdpartyconnector.utils.DateUtils.getDifferenceBetweenTwoTime;
import static com.cogent.cogentthirdpartyconnector.utils.DateUtils.getTimeInMillisecondsFromLocalDate;
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

        Long startTime = getTimeInMillisecondsFromLocalDate();

        log.info(CommonLogConstant.REST_TEMPLATE_PROCESS_STARTED, THIRD_PARTY_API);

        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(uri,
                    httpMethod,
                    request,
                    String.class);
        } catch (HttpStatusCodeException exception) {

            log.info(REST_TEMPLATE_PROCESS_ERROR, "Response: " + exception.getStatusCode().value());
            exceptionHandler(httpMethod,
                    request,
                    uri,
                    exception);
        }

        if (response == null || response.getBody() == null) {
            throw new OperationUnsuccessfulException("ThirdParty API response is null");
        }

        log.info(REST_TEMPLATE_PROCESS_COMPLETED, THIRD_PARTY_API, getDifferenceBetweenTwoTime(startTime));


        return new ResponseEntity<>(response.getBody(), response.getStatusCode());

    }
}
