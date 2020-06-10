package com.cogent.cogentthirdpartyconnector.utils;

import org.springframework.http.HttpMethod;

/**
 * @author rupak ON 2020/06/10-1:02 PM
 */
public class HttpMethodUtils {

    public static HttpMethod getHttpRequestMethod(String methodName) {

        HttpMethod httpMethod = null;

        switch (methodName) {
            case "GET":
                httpMethod = HttpMethod.GET;
                break;
            case "POST":
                httpMethod = HttpMethod.POST;
                break;
            case "PUT":
                httpMethod = HttpMethod.PUT;
                break;
            case "DELETE":
                httpMethod = HttpMethod.DELETE;
                break;
            case "PATCH":
                httpMethod = HttpMethod.PATCH;
                break;
        }

        return httpMethod;

    }
}
