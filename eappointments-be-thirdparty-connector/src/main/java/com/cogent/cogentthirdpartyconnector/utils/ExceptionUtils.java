package com.cogent.cogentthirdpartyconnector.utils;

import com.cogent.cogentappointment.commons.exception.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.MethodNotAllowedException;

import static java.util.Collections.singleton;

/**
 * @author rupak ON 2020/06/21-9:12 AM
 */
public class ExceptionUtils {

    public static void exceptionHandler(HttpMethod httpMethod,
                                        HttpEntity<?> request,
                                        String uri,
                                        HttpStatusCodeException exception) {

        if (exception.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {

            throw new InternalServerErrorException(ResponseEntity.class, "Third party API Server Error");

        } else if (exception.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {

            int statusCode = exception.getStatusCode().value();

            switch (statusCode) {

                case 400:
                    throw new BadRequestException("Third party API Bad Request");

                case 401:
                    throw new UnauthorisedException(String.class, "Third Party API Unauthorised Error");

                case 403:
                    throw new OperationUnsuccessfulException("Third Party API Forbidden");

                case 404:
                    throw new NoContentFoundException(String.class, "Third Party API Content Not Found");

                case 405:
                    throw new MethodNotAllowedException("Third Party API Method Not Allowed",
                            singleton(httpMethod));

                case 408:
                    throw new OperationUnsuccessfulException("Third Party API Request Time Out");

                default:
                    throw new OperationUnsuccessfulException("Third Party API Error");


            }

        }
    }
    }
