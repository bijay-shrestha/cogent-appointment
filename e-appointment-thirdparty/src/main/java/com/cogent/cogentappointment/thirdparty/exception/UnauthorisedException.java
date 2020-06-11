package com.cogent.cogentappointment.thirdparty.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.thirdparty.exception.utils.ExceptionUtils.generateMessage;
import static com.cogent.cogentappointment.thirdparty.exception.utils.ExceptionUtils.getLocalDateTime;
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

    public UnauthorisedException( String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(debugMessage)
                .debugMessage(debugMessage)
                .responseStatus(UNAUTHORIZED)
                .responseCode(UNAUTHORIZED.value())
                .timeStamp(getLocalDateTime())
                .build();

    }

}
