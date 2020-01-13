package com.cogent.cogentappointment.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.exception.utils.ExceptionUtils.getLocalDateTime;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class BadRequestException extends RuntimeException {

    private ExceptionResponse exception;

    public BadRequestException(String errorMessage) {
        super(errorMessage);
        setErrorResponse(errorMessage, errorMessage);
    }

    public BadRequestException(String errorMessage, String debugMessage) {
        setErrorResponse(errorMessage, debugMessage);
    }

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .status(BAD_REQUEST)
                .timeStamp(getLocalDateTime())
                .build();
    }
}
