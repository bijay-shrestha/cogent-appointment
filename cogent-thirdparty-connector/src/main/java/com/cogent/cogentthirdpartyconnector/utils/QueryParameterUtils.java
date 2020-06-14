package com.cogent.cogentthirdpartyconnector.utils;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @author rupak ON 2020/06/10-1:43 PM
 */
public class QueryParameterUtils {

    public static UriComponentsBuilder createQueryPamarameter(String uri, Map<String, String> queryParameter) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        queryParameter.forEach((key, value) -> {
            builder.queryParam(key, value);
        });

        return builder;

    }
}
