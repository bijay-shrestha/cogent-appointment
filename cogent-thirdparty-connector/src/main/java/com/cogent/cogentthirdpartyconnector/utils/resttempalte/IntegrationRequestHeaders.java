package com.cogent.cogentthirdpartyconnector.utils.resttempalte;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * @author rupak on 2020-05-24
 */
public class IntegrationRequestHeaders {

    public static HttpHeaders getBheriAPIHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("token", "DE26851D-AF4D-4CE7-9250-CCC2C9A728C9");
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        return headers;

    }

    public static HttpHeaders getEsewaAPIHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("esewa_id", "9841409090");
        headers.add("password", "dGVzdEAxMjM0");
        headers.add("device_unique_id", "b91bb1c3-43ac-4f97-846b-42adcf6fad11");

        return headers;
    }

    public static HttpHeaders getEsewaPaymentStatusAPIHeaders() {

        HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        headers.add("signature",
                "a531bcd75978687aa2beb52c94b2fb1fc2e31070c95d12a2da3d8c44cbf9a67b2c9f1da3f99f1f1cdb6605d60ad6dd5b3d8c2d15576e9b92bef5e3caf9d8e65b");


        return headers;
    }






}
