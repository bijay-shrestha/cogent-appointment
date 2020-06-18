package com.cogent.cogentthirdpartyconnector.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Map;

/**
 * @author rupak ON 2020/06/11-4:25 PM
 */
public class HttpHeaderUtils {

    public static HttpHeaders generateApiHeaders(Map<String, String> requestHeaderResponse, String generatedHmacKey) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        requestHeaderResponse.forEach((key, value) -> {
            headers.add(key, value);
        });

        if (generatedHmacKey != null) {
            headers.add("signature", generatedHmacKey);
        }

        return headers;
    }

}
