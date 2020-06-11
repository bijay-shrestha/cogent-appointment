package com.cogent.cogentthirdpartyconnector.utils;

import org.springframework.http.HttpMethod;

import static org.springframework.http.HttpMethod.*;

/**
 * @author rupak ON 2020/06/10-1:02 PM
 */
public class HttpMethodUtils {

    public static HttpMethod getHttpRequestMethod(String methodName) {

        HttpMethod httpMethod = null;
        switch (methodName) {
            case "GET":
                httpMethod = GET;
                break;
            case "POST":
                httpMethod = POST;
                break;
            case "PUT":
                httpMethod = PUT;
                break;
            case "DELETE":
                httpMethod = DELETE;
                break;
            case "PATCH":
                httpMethod = PATCH;
                break;
        }

        return httpMethod;

    }
}
