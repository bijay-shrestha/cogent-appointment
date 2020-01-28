package com.cogent.cogentappointment.client.exception;

import lombok.Getter;

import static com.cogent.cogentappointment.client.exception.utils.ExceptionUtils.getLocalDateTime;
import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;

/**
 * @author smriti on 7/2/19
 */
@Getter
public class OperationUnsuccessfulException extends RuntimeException {
    private ExceptionResponse exception;

    public OperationUnsuccessfulException(String errorMessage) {
        super(errorMessage);
        setErrorResponse(errorMessage, errorMessage);
    }

    private void setErrorResponse(String errorMessage, String debugMessage) {
        exception = ExceptionResponse.builder()
                .errorMessage(errorMessage)
                .debugMessage(debugMessage)
                .status(EXPECTATION_FAILED)
                .timeStamp(getLocalDateTime())
                .build();
    }
}
