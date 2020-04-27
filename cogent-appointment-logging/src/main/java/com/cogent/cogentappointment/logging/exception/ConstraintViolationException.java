package com.cogent.cogentappointment.logging.exception;

import com.cogent.cogentappointment.logging.exception.utils.ExceptionUtils;
import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author smriti on 2019-09-09
 * <p>
 * ConstraintViolationException to be thrown when validation on an argument annotated with {@code @Valid}
 * (request dtos) fails on Service layer
 */
@Getter
public class ConstraintViolationException extends RuntimeException {

    private ExceptionResponse exception;

    public ConstraintViolationException(String errorMessage, String debugMessage) {
        super(errorMessage);
        setErrorResponse(errorMessage, debugMessage);
    }

    private void setErrorResponse(String errorMessage, String debugMessage) {

        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .responseStatus(BAD_REQUEST)
                .responseCode(BAD_REQUEST.value())
                .timeStamp(ExceptionUtils.getLocalDateTime())
                .build();
    }
}
