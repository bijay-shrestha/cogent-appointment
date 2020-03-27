package com.cogent.cogentappointment.client.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.client.exception.utils.ExceptionUtils.generateMessage;
import static com.cogent.cogentappointment.client.exception.utils.ExceptionUtils.getLocalDateTime;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class UnauthorisedException extends RuntimeException {

    private ExceptionResponse exception;

    public UnauthorisedException(Class clazz, String debugMessage) {
        super(generateMessage(clazz));
        exception = ExceptionResponse.builder()
                .errorMessage(generateMessage(clazz))
                .debugMessage(debugMessage)
                .responseStatus(UNAUTHORIZED)
                .responseCode(UNAUTHORIZED.value())
                .timeStamp(getLocalDateTime())
                .build();

    }

}
