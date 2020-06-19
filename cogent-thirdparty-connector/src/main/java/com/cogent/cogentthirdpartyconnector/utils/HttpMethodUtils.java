package com.cogent.cogentthirdpartyconnector.utils;

import com.cogent.cogentappointment.commons.exception.BadRequestException;
import org.springframework.http.HttpMethod;

import static com.cogent.cogentthirdpartyconnector.constants.ErrorMessageConstants.INVALID_HTTP_METHOD_REQUEST;
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

            default:
                throw new BadRequestException(INVALID_HTTP_METHOD_REQUEST, methodName);
        }

        return httpMethod;
    }
}
